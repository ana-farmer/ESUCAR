package cl.duoc.tutorvirtual.data.repository

import cl.duoc.tutorvirtual.data.model.Hito
import cl.duoc.tutorvirtual.data.model.EstadoHito

data class Faq(val category: String, val question: String, val answer: String)
 object AcademicContent {
    fun hitos(values: Map<String, String>): List<Hito> = listOf(
        "Definición del tema", "Problema y objetivos", "Marco conceptual", "Anteproyecto final"
    ).mapIndexed { index, title ->
        val fields = when(index) { 0 -> listOf(0, 1); 1 -> listOf(2, 3, 4); 2 -> listOf(5); else -> (0..5).toList() }
        Hito(index + 1, title, "Revisa tu pauta antes de enviar el trabajo.", values["date_$index"].orEmpty(),
            when {
                values["sent_$index"] == "true" -> EstadoHito.ENVIADO
                fields.any { !values["draft_$it"].isNullOrBlank() } -> EstadoHito.EN_DESARROLLO
                else -> EstadoHito.POR_INICIAR
            })
    }
    val sections = listOf("Problema de investigación", "Delimitación del problema", "Objetivos", "Preguntas de investigación", "Justificación", "Marco conceptual inicial")
    val prompts = listOf(
        "Describe una situación concreta y la evidencia de que requiere investigación.",
        "Delimita el lugar, período y población que estudiarás.",
        "Escribe un objetivo general con verbo en infinitivo y objetivos específicos coherentes.",
        "Formula preguntas que puedan responderse con los datos de tu estudio.",
        "Explica la relevancia, utilidad y viabilidad de la investigación.",
        "Identifica conceptos principales y fuentes que sustenten tu propuesta."
    )
    val faqs = listOf(
        Faq("Objetivos", "¿Cómo formulo un objetivo general?", "Indica qué buscas lograr con un verbo en infinitivo, el objeto de estudio y su contexto. Ejemplo didáctico: Analizar los factores asociados a un problema de seguridad en un sector delimitado. Revisa su coherencia con la pregunta de investigación."),
        Faq("Metodología", "¿Cómo delimito el problema?", "Precisa el fenómeno, la población, el lugar y el período. Explica qué aspectos incluirás y qué límites tendrá el estudio."),
        Faq("Citación", "¿Qué debo registrar para una referencia?", "Registra autor, fecha, título y datos de publicación o enlace de la fuente. Una cita en el texto debe corresponder a una referencia. Verifica el formato específico en el manual APA 7 y con tu docente."),
        Faq("Metodología", "¿Qué diferencia hay entre enfoque cualitativo y cuantitativo?", "El cualitativo explora significados y experiencias; el cuantitativo mide variables y examina patrones numéricos. El enfoque debe responder a tus objetivos y a los datos que puedes obtener."),
        Faq("Formato", "¿Cómo organizo el anteproyecto?", "Comienza por problema, delimitación, objetivos, preguntas, justificación y marco conceptual. Confirma el orden y los apartados exigidos con la pauta de tu asignatura."),
        Faq("Entregas", "¿Quién aprueba mis hitos?", "El profesor guía aprueba y evalúa las entregas. Esta aplicación permite registrar tu preparación y marcar un hito como enviado; no asigna aprobaciones ni calificaciones.")
    )
 }
