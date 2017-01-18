package mazine.plants.growth

import mazine.plants.avg
import mazine.plants.stderr
import java.util.*

class GrowthRatePhase(val name: String) {
    val plants = ArrayList<Plant>()

    val diffs: List<Double>
        get() = plants.map { it.diff }
    val avg: Double
        get() = diffs.avg
    val stderr: Double
        get() = diffs.stderr

    data class Plant(val name: String, val samples: List<Double>) {
        val diff: Double
            get() = (samples.last() - samples.first()) / (samples.size - 1)
    }
}


