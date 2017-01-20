package mazine.plants.raw

data class Plant(
        val replicate: Replicate,
        val treatment: Treatment,
        val name: String,
        val states: List<State>
)