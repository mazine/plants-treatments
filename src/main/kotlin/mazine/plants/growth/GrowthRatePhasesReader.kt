package mazine.plants.growth

import org.apache.commons.csv.CSVFormat
import java.io.Reader
import java.util.*

/**
 * Format:
 * ```
 * First phase name
 * First plant name, sample1_1, sample1_2, ...
 * Second plant name, sample2_1, sample2_2, ...
 * ---empty line---
 * Second phase name
 * First plant name, sample1_1, sample1_2, ...
 * Second plant name, sample2_1, sample2_2, ...
 * ---empty line---
 * ...
 * ```
 */
class GrowthRatePhasesReader(val input: Reader) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';')

    fun read(): List<GrowthRatePhase> {
        val csvParser = csvFormat.parse(input)

        val phases = ArrayList<GrowthRatePhase>()

        /**
         * 0 - expect treatment phase name
         * 1 - expect next plant of the current treatment phase or end of the phase
         */
        var state = 0;
        var currentPhase = GrowthRatePhase("EMPTY")
        csvParser.records.forEach { record ->
            when (state) {
                0 -> {
                    val phaseName = record.first()
                    if (phaseName.isNotEmpty()) {
                        currentPhase = GrowthRatePhase(phaseName)
                        phases.add(currentPhase)
                        state = 1
                    }
                }
                1 -> {
                    if (record.first().isEmpty()) {
                        state = 0
                    } else {
                        val plantName = record.first()
                        val samples = record.asSequence().drop(1).filter {
                            it.isNotEmpty()
                        }.map {
                            it.toDouble()
                        }.toList()
                        currentPhase.plants.add(GrowthRatePhase.Plant(plantName, samples))
                    }
                }
            }
        }

        return phases
    }
}