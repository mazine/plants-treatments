package mazine.plants.mortality

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.Writer

class MortalityRateWriter(val out: Writer) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader()

    fun write(phases: Sequence<PlantMortality>, lastDays: List<Int>) {
        CSVPrinter(out, csvFormat).use { csvPrinter ->
            csvPrinter.printRecord(
                    "Individual",
                    "Replicate",
                    "Treatment",
                    "Time_fad",
                    "Event_fad",
                    "Time_mort",
                    "Event_mort",
                    *lastDays.map {
                        listOf("Time_fad ($it)", "Event_fad ($it)", "Time_mort ($it)", "Event_mort ($it)")
                    }.flatten().toTypedArray()
            )

            phases.forEach { row ->
                val allDaysMortality = row.mortality
                csvPrinter.printRecord(
                        row.name,
                        row.replicate,
                        row.treatment,
                        allDaysMortality.fadeTime,
                        if (allDaysMortality.faded) 1 else 0,
                        allDaysMortality.deathTime,
                        if (allDaysMortality.died) 1 else 0,
                        *lastDays.map {
                            val (fadeTime, faded, deathTime, died) = row.getRangedMortality(it)
                            listOf(fadeTime, if (faded) 1 else 0, deathTime, if (died) 1 else 0)
                        }.flatten().toTypedArray())
            }
        }
    }
}