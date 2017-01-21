package mazine.plants.growth

import mazine.plants.raw.Plant
import mazine.plants.raw.State
import mazine.plants.view.TreatmentPhase
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.Writer
import java.util.*

class GrowthRateWriter(val out: Writer) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';')

    fun write(rawData: List<Plant>) {
        CSVPrinter(out, csvFormat).use { printer ->
            TreatmentPhase.values().forEach { phase ->
                printer.printRecord(phase.name)

                val plants = phase.apply(rawData)
                val size = plants.map { it.states.size }.max() ?: 0

                printer.printRecord(null, *(1..size - 1).map { day ->
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
            }
        }

        fun List<List<String>>.transponse(): List<List<String>> {
            val width = map { it.size }.max() ?: 0
            val height = size

            val transponsed = ArrayList<MutableList<String>>(width)
            for (i in 0..width - 1) {
                val tRow = ArrayList<String>(height)
                for (j in 0..height - 1) {
                    if (i < this[j].size) {
                        tRow.add(this[j][i])
                    } else {
                        tRow.add("")
                    }
                }
                transponsed.add(tRow)
            }
            return transponsed
        }
    }
}