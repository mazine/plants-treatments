package mazine.plants.mortality

import mazine.plants.avg
import mazine.plants.format
import mazine.plants.stderr

class MortalityRateAverageWriter() {
    fun write(phases: Sequence<PlantMortality>) {
        phases.groupBy { it.treatment }.forEach {
            val (treatment, plants) = it
            val notFaded = plants.filter { !it.mortality.faded }.size
            val alive = plants.filter { !it.mortality.died }.size

            val fadeTimes = plants.filter { it.mortality.faded }.map { it.mortality.fadeTime.toDouble() }
            val fadeAvg = fadeTimes.avg
            val fadeStderr = fadeTimes.stderr
            val deathTimes = plants.filter { it.mortality.died }.map { it.mortality.deathTime.toDouble() }
            val deathAvg = deathTimes.avg
            val deathStderr = deathTimes.stderr
            println("$treatment still not faded ${(notFaded.toDouble() * 100 / plants.size).format()}% ($notFaded of ${plants.size}), still alive ${(alive.toDouble() * 100 / plants.size).format()}% ($alive of ${plants.size}). Fade: ${fadeAvg.format()}±${fadeStderr.format()}, death: ${deathAvg.format()}±${deathStderr.format()}")
        }
    }
}