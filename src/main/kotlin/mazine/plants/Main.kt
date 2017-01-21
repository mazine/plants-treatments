package mazine.plants

import mazine.plants.growth.GrowthRateWriter
import mazine.plants.growth.RGrowthRateWriter
import mazine.plants.raw.Plant
import mazine.plants.raw.Replicate
import mazine.plants.raw.State
import mazine.plants.raw.treatments
import mazine.plants.view.TreatmentPhase
import mazine.plants.view.TreatmentPhaseWriter
import org.apache.commons.math3.stat.regression.SimpleRegression
import java.io.FileWriter


fun main(args: Array<String>) {
    val rawData = Replicate.I.load() + Replicate.II.load()
    writePhases(rawData)

    GrowthRateWriter(FileWriter("growth_rate.csv")).write(rawData)
    RGrowthRateWriter(FileWriter("growth_rate_R_detailed.csv"), isDetailed = true).write(rawData)
    RGrowthRateWriter(FileWriter("growth_rate_R.csv"), isDetailed = false).write(rawData)

    mortality(Replicate.I)
    mortality(Replicate.II)
}

fun writePhases(rawData: List<Plant>) {
    val writer = TreatmentPhaseWriter(FileWriter("growth_phases.csv"))
    writer.write(TreatmentPhase.values().toList(), rawData)
}


fun mortality(replicate: Replicate) {
    val consideredDays = listOf(
            "A+" to 1..2,
            "A-" to 1..2,
            "B+" to 1..5,
            "B-" to 1..5,
            "C+" to 1..9,
            "C-" to 1..9,
            "D" to 1..16
    ).map {
        treatments[it.first]!! to it.second
    }.toMap()

    val plants = replicate.load()

    println("Replicate ${replicate.name}")
    (1..16).forEach { day ->
        val consideredPlants = plants.filter { day in consideredDays[it.treatment]!! }
        val alive = consideredPlants.count { it.states[day - 1] is State.Alive }
        val faded = consideredPlants.count { it.states[day - 1] is State.Faded }
        val dead = consideredPlants.count { it.states[day - 1] is State.Dead }
        val total = consideredPlants.count()
        println("Day: $day, alive: $alive, faded: $faded, dead: $dead, total: $total")
    }
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

