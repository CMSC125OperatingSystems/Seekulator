package cmsc125.lab5;

import org.junit.jupiter.api.Test;
import cmsc125.project4.services.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class FIFOTest {

    private Cylinder[] queue;
    private int initialHead = 100;

    @BeforeEach
    void setUp() {
        // Sample request queue: 55, 58, 160, 38, 184
        int[] data = {55, 58, 160, 38, 184};
        queue = new Cylinder[data.length];
        for (int i = 0; i < data.length; i++) {
            queue[i] = new Cylinder(data[i]);
        }
    }

    @Test
    void testExecutionOrder() {
        FIFO fifo = new FIFO(queue, initialHead, true);
        fifo.preparePath(); // Essential based on your code

        // Step 1: 100 -> 55
        assertTrue(fifo.executeStep());
        assertEquals(55, fifo.getHead());
        assertTrue(queue[0].visited);

        // Step 2: 55 -> 58
        assertTrue(fifo.executeStep());
        assertEquals(58, fifo.getHead());

        // Step 3: 58 -> 160
        assertTrue(fifo.executeStep());
        assertEquals(160, fifo.getHead());
    }

    @Test
    void testTotalMovementCalculation() {
        FIFO fifo = new FIFO(queue, initialHead, true);
        fifo.preparePath();

        // Manual calculation:
        // |100 - 55| = 45
        // |55 - 58|  = 3
        // |58 - 160| = 102
        // |160 - 38| = 122
        // |38 - 184| = 146
        // Total      = 418

        while (fifo.executeStep()) {
            // Processing...
        }

        assertEquals(418, fifo.getTotalMovement());
    }

    @Test
    void testFinishedState() {
        FIFO fifo = new FIFO(queue, initialHead, true);
        fifo.preparePath();

        // Execute all 5 steps
        for (int i = 0; i < 5; i++) {
            assertTrue(fifo.executeStep());
        }

        // The 6th attempt should return false
        assertFalse(fifo.executeStep());
    }

    @Test
    void testEmptyQueue() {
        FIFO fifo = new FIFO(new Cylinder[0], initialHead, true);
        fifo.preparePath();
        
        assertFalse(fifo.executeStep(), "Should return false for empty queue");
        assertEquals(0, fifo.getTotalMovement());
    }
}
