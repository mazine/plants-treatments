package mazine.plants.view

import mazine.plants.raw.Plant
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.Writer
import java.util.*

class TreatmentPhaseWriter(val out: Writer) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';')

    fun write(phases: List<TreatmentPhase>, rawData: List<Plant>) {
        CSVPrinter(out, csvFormat).use { csvPrinter ->
            phases.forEach { view ->
                csvPrinter.printRecord(view.name)
                val viewData = view.apply(rawData)
                viewData.forEach { plant ->
                    csvPrinter.printRecord(plant.name, *plant.states.map { it.toString() }.toTypedArray())
                }
                csvPrinter.printRecord()
            }
        }
    }
}