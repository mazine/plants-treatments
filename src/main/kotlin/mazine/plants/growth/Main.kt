package mazine.plants.growth

import mazine.plants.ControlGroup
import java.io.FileWriter
import java.io.InputStreamReader

fun main(args: Array<String>) {
    growthRate()

    val reader2 = GrowthRatePhasesReader(InputStreamReader(GrowthRatePhasesReader::class.java.classLoader.getResourceAsStream("growth_input.csv")))
    val phases2 = reader2.read()
    val joined = GrowthRatePhase("Joined").apply {
        plants.addAll(phases2[0].plants)
        plants.addAll(phases2[1].plants)
    }

    val writer2 = GrowthRateWriter(FileWriter("replicates_growth_rate_diffs.csv"))
    writer2.write(phases2.asSequence() + sequenceOf(joined))

}

private fun growthRate() {
    val reader = GrowthRatePhasesReader(InputStreamReader(GrowthRatePhasesReader::class.java.classLoader.getResourceAsStream("growth_input.csv")))
    val phases = reader.read()

    val entireControlGroup = phases.last()
    val controlGroupPhases = ControlGroup.values().map { it.toTreatmentPhase(entireControlGroup) }

    val writer = GrowthRateWriter(FileWriter("growth_rate_diffs.csv"))
    writer.write(phases.asSequence() + controlGroupPhases)
}
