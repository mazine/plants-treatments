package mazine.plants.raw

class Treatment(val name: String, val letter: String, val isPositive: Boolean?) {
    override fun toString() = name
}

val treatments = listOf("A+", "A-", "B+", "B-", "C+", "C-", "D").map { it to parseTreatment(it) }.toMap()

private fun parseTreatment(treatmentName: String): Treatment {
    val regex = Regex("([ABCD])([+-])?")
    val (letter, sign) = regex.matchEntire(treatmentName)?.destructured ?:
            throw IllegalArgumentException("Treatment name $treatmentName doesn't match treatment name format")

    return Treatment(treatmentName, letter, when (sign) {
        "+" -> true
        "-" -> false
        else -> null
    })
}