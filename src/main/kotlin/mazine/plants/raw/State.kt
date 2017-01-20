package mazine.plants.raw

sealed class State {
    class Alive(val height: Int): State() {
        override fun toString() = height.toString()
    }
    object Faded: State() {
        override fun toString() = "w"
    }
    object Dead: State() {
        override fun toString() = "t"
    }

    companion object {
        fun parse(value: String?) = when {
            value == null || value.isBlank() -> null
            value.all(Char::isDigit) -> Alive(value.toInt())
            value == "w" -> Faded
            value == "t" -> Dead
            else -> throw IllegalArgumentException("$value is not a valid plant state should be either height, or 'w' for faded, or 't' for dead")
        }
    }
}

