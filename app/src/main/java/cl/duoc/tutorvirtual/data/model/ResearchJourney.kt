package cl.duoc.tutorvirtual.data.model

data class ResearchStage(val title: String, val action: String, val sections: List<Int>) {
    fun isComplete(values: Map<String, String>) = sections.all { values["done_$it"] == "true" }
    fun nextSection(values: Map<String, String>) = sections.firstOrNull { values["done_$it"] != "true" } ?: sections.first()
}

object ResearchJourney {
    val stages = listOf(
        ResearchStage("Problema de investigación", "Define tu problema", listOf(0, 1)),
        ResearchStage("Objetivos", "Define tus objetivos", listOf(2, 3, 4)),
        ResearchStage("Marco conceptual", "Construye tu marco conceptual", listOf(5)),
        ResearchStage("Metodología", "Diseña tu metodología", listOf(6)),
        ResearchStage("Resultados", "Analiza tus resultados", listOf(7))
    )
    fun currentIndex(values: Map<String, String>) = stages.indexOfFirst { !it.isComplete(values) }
        .let { if (it < 0) stages.lastIndex else it }
    val sectionCount: Int get() = stages.sumOf { it.sections.size }
}
