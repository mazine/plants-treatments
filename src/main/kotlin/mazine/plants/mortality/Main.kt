package mazine.plants.mortality

import java.io.FileWriter
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val reader = MortalityRateReader(InputStreamReader(MortalityRateReader::class.java.classLoader.getResourceAsStream("mortality_rate_input.csv")))
    val plants = reader.read()

//    mortalityRateMultiplied(plants)
    mortalityRateAverage(plants)
}

private fun mortalityRateAverage(plants: List<PlantMortality>) {
    val phases = plants.map {
        if (it.mortality.fadeTime <= 16 && it.mortality.deathTime <= 16) {
            it
        } else {
            PlantMortality(it.name, it.replicate, it.treatment, PlantMortality.Mortality(
                    fadeTime = Math.min(it.mortality.fadeTime, 16),
                    faded = it.mortality.faded && it.mortality.fadeTime <= 16,
                    deathTime = Math.min(it.mortality.deathTime, 16),
                    died = it.mortality.died && it.mortality.deathTime <= 16
            ))
        }
    }.asSequence()

    MortalityRateAverageWriter().write(phases)
    MortalityRateFilteredWriter(FileWriter("fade_filtered_rate_output.csv")).write(phases, { it.mortality.faded })
    MortalityRateFilteredWriter(FileWriter("death_filtered_rate_output.csv")).write(phases, { it.mortality.died })
}
