package mazine.plants

import mazine.plants.raw.Plant
import mazine.plants.raw.Replicate
import mazine.plants.view.TreatmentPhase
import mazine.plants.view.TreatmentPhaseWriter
import java.io.FileWriter

fun main(args: Array<String>) {
    val rawData = Replicate.I.load() + Replicate.II.load()
    writePhases(rawData)
}

fun writePhases(rawData: List<Plant>) {
    val writer = TreatmentPhaseWriter(FileWriter("growth_phases.csv"))

    writer.write(listOf(
            TreatmentPhase.A_1,
            TreatmentPhase.A_2,
            TreatmentPhase.`A+_3`,
            TreatmentPhase.`A-_3`,
            TreatmentPhase.B_1,
            TreatmentPhase.B_2,
            TreatmentPhase.`B+_3`,
            TreatmentPhase.`B-_3`,
            TreatmentPhase.C_1,
            TreatmentPhase.C_2,
            TreatmentPhase.D_A_1,
            TreatmentPhase.D_A_2,
            TreatmentPhase.D_A_3,
            TreatmentPhase.D_B_1,
            TreatmentPhase.D_B_2,
            TreatmentPhase.D_B_3,
            TreatmentPhase.D_C_1,
            TreatmentPhase.D_C_2
    ), rawData)
}
