package mazine.plants

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


val List<Double>.avg: Average
    get() = Average(if (size >= 1) average() else Double.NaN, if (size >= 2) stderr else 0.0)
val List<Double>.stdev: Double
    get() = Math.sqrt(map { (average() - it) * (average() - it) }.sum() / (size - 1))
val List<Double>.stderr: Double
    get() = stdev / Math.sqrt(size.toDouble())

fun Double.format() = DecimalFormat("###.##").apply {
    decimalFormatSymbols = DecimalFormatSymbols().apply {
        this.decimalSeparator = ','
    }
}.format(this)

class Average(val avg: Double, val stderr: Double) {
    override fun toString(): String {
        return "${avg.format()} ± ${stderr.format()}"
    }
}