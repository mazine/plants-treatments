package mazine.plants

import mazine.plants.raw.Plant
import mazine.plants.raw.Replicate
import mazine.plants.raw.State
import mazine.plants.view.TreatmentPhase
import mazine.plants.view.TreatmentPhaseWriter
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.math3.stat.regression.SimpleRegression
import java.io.FileWriter


fun main(args: Array<String>) {
    val rawData = Replicate.I.load() + Replicate.II.load()
    writePhases(rawData)
    CSVPrinter(FileWriter("growth_phases.csv"), CSVFormat.EXCEL.withDelimiter(';')).use { printer ->
        TreatmentPhase.values().forEach { phase ->
            printer.printRecord(phase.name)

            val plants = phase.apply(rawData)
            val size = plants.map { it.states.size }.max() ?: 0

            printer.printRecord(null, *(1..size-1).map { day ->
                "$day - ${day + 1}"
            }.toTypedArray())

            plants.forEach { plant ->
                printer.printRecord(plant.name, *(1..size - 1).map { day ->
                    if (day < plant.states.size) {
                        val stateOfCurDay = plant.states[day] as? State.Alive
                        val stateOnPrevDay = plant.states[day - 1] as? State.Alive
                        if (stateOfCurDay != null && stateOnPrevDay != null) {
                            (stateOfCurDay.height - stateOnPrevDay.height).toString()
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }.toTypedArray())
            }

//            val avgIncPerDay = (1..size - 1).map { day ->
//                // Heights of alive plants for the day number `day`
//                val heightIncs = plants.map {
//                    if (day < it.states.size) {
//                        val stateOfCurDay = it.states[day] as? State.Alive
//                        val stateOnPrevDay = it.states[day - 1] as? State.Alive
//                        if (stateOfCurDay != null && stateOnPrevDay != null) {
//                            stateOfCurDay.height - stateOnPrevDay.height
//                        } else {
//                            null
//                        }
//                    } else {
//                        null
//                    }
//                }.filterNotNull().map { heightInc ->
//                    heightInc.toDouble()
//                }
//                heightIncs.avg
//            }
//            println("${phase.name} avg$avgIncPerDay = ${avgIncPerDay.map { it.avg }.filter { it.isFinite() }.avg}")
        }

    }
}

fun writePhases(rawData: List<Plant>) {
    val writer = TreatmentPhaseWriter(FileWriter("growth_phases.csv"))
    writer.write(TreatmentPhase.values().toList(), rawData)
}

fun List<Plant>.regression(): SimpleRegression {
    val regression = SimpleRegression().apply {
        addData(asSequence().flatMap {
            it.states.asSequence().mapIndexedNotNull { i, state ->
                if (state is State.Alive) {
                    doubleArrayOf(i.toDouble(), state.height.toDouble())
                } else {
                    null
                }
            }
        }.toList().toTypedArray())
    }
    return regression
}

fun List<Plant>.avgRegression(): SimpleRegression {
    val totalDays = this.map { it.states.size }.max() ?: 0
    val regression = SimpleRegression().apply {
        val data = (4..totalDays - 1).flatMap { day ->
            listOf(Replicate.I, Replicate.II).mapNotNull { replicate ->
                val heights = this@avgRegression.filter {
                    it.replicate == replicate
                }.mapNotNull { plant ->
                    (plant.states.getOrNull(day) as? State.Alive)?.height
                }
                if (heights.isNotEmpty()) {
                    doubleArrayOf(day.toDouble() + 1, heights.average())
                } else {
                    null
                }
            }
        }.toList().toTypedArray()
        addData(data)

        data.forEach {
            println(it.toList())
        }
    }
    return regression
}

