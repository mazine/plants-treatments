package mazine.plants.mortality

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.Writer

class MortalityRateFilteredWriter(val out: Writer) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader()

    fun write(phases: Sequence<PlantMortality>, filter: (PlantMortality) -> Boolean) {
        CSVPrinter(out, csvFormat).use { csvPrinter ->
            csvPrinter.printRecord(
                    "Individual",
                    "Replicate",
                    "Treatment",
                    "Time_fad",
                    "Event_fad",
                    "Time_mort",
                    "Event_mort"
            )

            phases.filter(filter).forEach { row ->
                val allDaysMortality = row.mortality
                csvPrinter.printRecord(
                        row.name,
                        row.replicate,
                        row.treatment,
                        allDaysMortality.fadeTime,
                        if (allDaysMortality.faded) 1 else 0,
                        allDaysMortality.deathTime,
                        if (allDaysMortality.died) 1 else 0)
            }
        }
    }
}