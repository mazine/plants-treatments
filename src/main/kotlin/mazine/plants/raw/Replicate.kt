package mazine.plants.raw

import java.io.InputStreamReader

enum class Replicate(val resourceName: String) {
    I("raw-replicate-I.csv"),
    II("raw-replicate-II.csv");

    fun load() = RawDataReader(InputStreamReader(javaClass.classLoader.getResourceAsStream(resourceName))).read(this)
}