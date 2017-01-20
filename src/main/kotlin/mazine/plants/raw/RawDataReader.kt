package mazine.plants.raw

import org.apache.commons.csv.CSVFormat
import java.io.Reader

class RawDataReader(val input: Reader) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';')

    fun read(replicate: Replicate): List<Plant> {
        val csvParser = csvFormat.parse(input)

        // First three lines are column names, dates, day names
        return csvParser.records.drop(3).map { record ->
            val capName = record[0]
            val plantName = record[1]
            val treatmentName = record[2]
            val treatment = treatments[treatmentName] ?:
                    throw IllegalArgumentException("$treatmentName is not a valid treatment name")

            val states = record.drop(3).map {
                State.parse(it)
            }.takeWhile {
                it != null
            }.map { it!! }

            Plant(replicate, treatment, plantName, states)
        }
    }
}