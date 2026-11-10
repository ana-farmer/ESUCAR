package cl.duoc.tutorvirtual

import cl.duoc.tutorvirtual.data.model.ResearchJourney
import cl.duoc.tutorvirtual.viewmodel.TutorState
import org.junit.Assert.*
import org.junit.Test

class ResearchJourneyTest {
    @Test fun firstStageRequiresBothProblemAndDelimitation() {
        val values = mapOf("done_0" to "true")
        assertFalse(ResearchJourney.stages[0].isComplete(values))
        assertEquals(1, ResearchJourney.stages[0].nextSection(values))
        assertEquals(0, ResearchJourney.currentIndex(values))
    }
    @Test fun existingSixCompletedDraftsArePreservedAndLeadToMethodology() {
        val values = (0..5).associate { "done_$it" to "true" }
        assertEquals(3, ResearchJourney.currentIndex(values))
        assertEquals(6, ResearchJourney.stages[3].nextSection(values))
        assertEquals(.75f, TutorState(values = values).progress, .001f)
    }
    @Test fun fullJourneyRemainsReviewable() {
        val values = (0..7).associate { "done_$it" to "true" }
        assertTrue(ResearchJourney.stages.all { it.isComplete(values) })
        assertEquals(4, ResearchJourney.currentIndex(values))
        assertEquals(7, ResearchJourney.stages[4].nextSection(values))
    }
}
