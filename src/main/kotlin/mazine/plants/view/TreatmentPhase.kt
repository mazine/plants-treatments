package mazine.plants.view

import mazine.plants.raw.*


enum class TreatmentPhase(vararg val view: Pair<Filter, IntRange>) {
    A_1(I_II("A*") to (1..2)),
    A_2(I_II("A*") to (2..6)),
    `A+_3`(I_II("A+") to (6..16)),
    `A-_3`(I_II("A-") to (6..16)),
    B_1(I_II("B*") to (1..5)),
    B_2(I_II("B*") to (5..9)),
    `B+_3`(I_II("B+") to (9..16)),
    `B-_3`(I_II("B-") to (9..16)),
    C_1(I_II("C*") to (1..9)),
    C_2(I_II("C*") to (9..13)),
    D_A_1(I_II("D*") to (1..2)),
    D_A_2(I_II("D*") to (2..6)),
    D_A_3(I_II("D*") to (6..16)),
    D_B_1(I_II("D*") to (1..5)),
    D_B_2(I_II("D*") to (5..9)),
    D_B_3(I_II("D*") to (9..16)),
    D_C_1(I_II("D*") to (1..9)),
    D_C_2(I_II("D*") to (9..13)),
    R_I(I("C*") to (5..9), I("D*") to (5..16)),
    R_II(II("C*") to (5..9), II("D*") to (5..16)),
    R(I_II("C*") to (5..9), I_II("D*") to (5..16));

    fun apply(plants: List<Plant>): List<Plant> {
        return view.fold(emptySequence<Plant>()) { cur, it ->
            val (filter, range) = it
            plants.asSequence().filter { plant ->
                plant.replicate in filter.replicates && plant.treatment in filter.treatments
            }.map {
                Plant(it.replicate, it.treatment, it.name, it.states.subList(range.start - 1, range.endInclusive))
            }.filter {
                it.states.any { it is State.Alive }
            }
        }.toList()
    }
}

private class Filter(val replicates: Set<Replicate>, val treatments: Set<Treatment>)

private fun I(treatmentDescriptor: String) = new(setOf(Replicate.I), treatmentDescriptor)
private fun II(treatmentDescriptor: String) = new(setOf(Replicate.II), treatmentDescriptor)
private fun I_II(treatmentDescriptor: String) = new(setOf(Replicate.I, Replicate.II), treatmentDescriptor)

private val treatmentDescriptorRegex = Regex("([ABCD])([-+*])")
private fun new(replicates: Set<Replicate>, treatmentDescriptor: String): Filter {
    val (letter, isPositive) = treatmentDescriptorRegex.matchEntire(treatmentDescriptor)?.destructured ?:
            throw IllegalArgumentException("$treatmentDescriptor is not a valid treatment descriptor for the phase")

    return Filter(replicates, treatments.values.filter {
        it.letter == letter
    }.filter {
        when (isPositive) {
            "+" -> it.isPositive == true
            "-" -> it.isPositive == false
            else -> true
        }
    }.toSet())
}
