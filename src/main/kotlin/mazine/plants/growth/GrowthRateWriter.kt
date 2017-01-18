package mazine.plants.growth

import mazine.plants.format
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.Writer
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class GrowthRateWriter(val out: Writer) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';')

    fun write(phases: Sequence<GrowthRatePhase>) {
        val table = ArrayList<List<String>>()

        table.add(listOf("name", "size", "avg", "stderr"))
        phases.forEach { phase ->
            val row = mutableListOf(phase.name, phase.plants.size.toString(), phase.avg.format(), phase.stderr.format())
            row.addAll(phase.plants.asSequence().map { it.diff.format() })
            table.add(row)
        }


        CSVPrinter(out, csvFormat).use { csvPrinter ->
            table.transponse().forEach { row ->
                csvPrinter.printRecord(row)
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