package mazine.plants

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


val List<Double>.avg: Double
    get() = sum() / size
val List<Double>.stdev: Double
    get() = Math.sqrt(map { (avg - it) * (avg - it) }.sum() / (size - 1))
val List<Double>.stderr: Double
    get() = stdev / Math.sqrt(size.toDouble())

fun Double.format() = DecimalFormat("###.##").apply {
    decimalFormatSymbols = DecimalFormatSymbols().apply {
        this.decimalSeparator = ','
    }
}.format(this)

