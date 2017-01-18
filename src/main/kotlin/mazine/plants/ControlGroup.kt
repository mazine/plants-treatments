package mazine.plants

import mazine.plants.growth.GrowthRatePhase

enum class ControlGroup(val range: IntRange) {
    D_A_1(1..2),
    D_A_2(2..6),
    D_A_3(6..16),
    D_B_1(1..5),
    D_B_2(5..9),
    D_B_3(9..16),
    D_C_1(1..9),
    D_C_2(9..16);

    fun toTreatmentPhase(entireControlGroup: GrowthRatePhase): GrowthRatePhase {
        return GrowthRatePhase(name).apply {
            this.plants.addAll(entireControlGroup.plants.map {
                GrowthRatePhase.Plant(it.name, it.samples.drop(range.first - 1).take(range.last - range.first + 1))
            }.filter { plant ->
                plant.samples.size >= 2
            })
        }
    }
}