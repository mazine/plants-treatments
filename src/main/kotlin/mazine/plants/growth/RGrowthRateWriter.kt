package mazine.plants.growth

import mazine.plants.avg
import mazine.plants.raw.Plant
import mazine.plants.raw.State
import mazine.plants.view.TreatmentPhase
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.Writer
import java.util.*


class RGrowthRateWriter(val out: Writer, val isDetailed: Boolean) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';')

    fun write(rawData: List<Plant>) {
        CSVPrinter(out, csvFormat).use { printer ->
            if (isDetailed) {
                printer.printRecord("phase", "plant", "at day", "growth rate")
            } else {
                printer.printRecord("phase", "growth rate")
            }
            TreatmentPhase.values().forEach { phase ->
                val plants = phase.apply(rawData)
                val size = plants.map { it.states.size }.max() ?: 0

                val values = ArrayList<Double>()
                plants.forEach { plant ->
                    (1..size - 1).forEach { day ->
                        if (day < plant.states.size) {
                            val stateOfCurDay = plant.states[day] as? State.Alive
                            val stateOnPrevDay = plant.states[day - 1] as? State.Alive
                            if (stateOfCurDay != null && stateOnPrevDay != null) {
                                val diff = stateOfCurDay.height - stateOnPrevDay.height
                                values.add(diff.toDouble())
                                if (isDetailed) {
                                    printer.printRecord(phase.name, plant.name, "${day + 1}", diff.toString())
                                } else {
                                    printer.printRecord(phase.name, diff.toString())
                                }
                            }
                        }
                    }
                }
                println("${phase.name} plants: ${plants.filter { it.states.count { it is State.Alive } > 1 }.size} / ${plants.size}, values: ${values.size}, growth rate avg = ${values.avg}")
            }
        }
    }
}