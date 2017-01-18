package mazine.plants.growth

import mazine.plants.ControlGroup
import java.io.FileWriter
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val reader = GrowthRatePhasesReader(InputStreamReader(GrowthRatePhasesReader::class.java.classLoader.getResourceAsStream("growth_rate_input.csv")))
    val phases = reader.read()

    val entireControlGroup = phases.last()
    val controlGroupPhases = ControlGroup.values().map { it.toTreatmentPhase(entireControlGroup) }

    val writer = GrowthRateWriter(FileWriter("growth_rate_diffs.csv"))
    writer.write(phases.asSequence() + controlGroupPhases)
}
