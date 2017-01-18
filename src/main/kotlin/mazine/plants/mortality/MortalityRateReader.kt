package mazine.plants.mortality

import org.apache.commons.csv.CSVFormat
import java.io.Reader

/**
 * Format:
 * ```
Individual	Replicate	Treatment	Time_fad	Event_fad	Time_mort	Event_mort
IA2	1	A+	10	1	14	1
IA4	1	A+	4	1	6	1
IA5	1	A+	10	1	14	1
 * ```
 */
class MortalityRateReader(val input: Reader) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader()

    fun read(): List<PlantMortality> {
        val csvParser = csvFormat.parse(input)

        return csvParser.records.map { record ->
            PlantMortality(
                    name = record["Individual"],
                    replicate = record["Replicate"].toInt(),
                    treatment = record["Treatment"],
                    mortality = PlantMortality.Mortality(
                            fadeTime = record["Time_fad"].toInt(),
                            faded = record["Event_fad"].toInt() != 0,
                            deathTime = record["Time_mort"].toInt(),
                            died = record["Event_mort"].toInt() != 0
                    )
            )
        }
    }
}