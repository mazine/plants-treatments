package mazine.plants.mortality

data class PlantMortality(
        val name: String,
        val replicate: Int,
        val treatment: String,
        val mortality: Mortality) {

    data class Mortality(
            val fadeTime: Int,
            val faded: Boolean,
            val deathTime: Int,
            val died: Boolean)

    fun getRangedMortality(lastDay: Int): Mortality {
        return Mortality(
                fadeTime = Math.min(mortality.fadeTime, lastDay),
                faded = mortality.faded && mortality.fadeTime <= lastDay,
                deathTime = Math.min(mortality.deathTime, lastDay),
                died = mortality.died && mortality.deathTime <= lastDay
        )
    }
}

