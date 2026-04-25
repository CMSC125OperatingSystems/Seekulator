package cmsc125.lab5;

import org.junit.jupiter.api.Test;
import cmsc125.project4.services.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

class CSCANTest {

    private List<Cylinder> queue;
    private int initialHead = 50;

    @BeforeEach
    void setUp() {
        // Sample queue: 10, 80, 30, 150, 20
        int[] data = {10, 80, 30, 150, 20};
        queue = new ArrayList<>();
        for (int val : data) {
            queue.add(new Cylinder(val));
        }
    }

    @Test
    void testCScanUpwardMovement() {
        // Head: 50, Direction: True (Up/Right)
        // Expected Order: 80 -> 150 -> 199 (V) -> 0 (V) -> 10 -> 20 -> 30
        C_SCAN cscan = new C_SCAN(queue, initialHead, true);
        cscan.preparePath();

        // 1. Service the right side
        assertTrue(cscan.executeStep()); // Move to 80
        assertEquals(80, cscan.getHead());

        assertTrue(cscan.executeStep()); // Move to 150
        assertEquals(150, cscan.getHead());

        // 2. The Boundary Wrap (Circular logic)
        assertTrue(cscan.executeStep()); // Move to 199 (Virtual)
        assertEquals(199, cscan.getHead());

        assertTrue(cscan.executeStep()); // Jump to 0 (Virtual)
        assertEquals(0, cscan.getHead());

        // 3. Service the remaining requests (Ascending from 0)
        assertTrue(cscan.executeStep()); // Move to 10
        assertEquals(10, cscan.getHead());
        
        assertTrue(cscan.executeStep()); // Move to 20
        assertEquals(20, cscan.getHead());
        
        assertTrue(cscan.executeStep()); // Move to 30
        assertEquals(30, cscan.getHead());

        assertFalse(cscan.executeStep()); // Finished
    }

    @Test
    void testCScanTotalMovement() {
        // Initial Head 50, Dir: True
        // Path: 50 -> 80 -> 150 -> 199 -> 0 -> 10 -> 20 -> 30
        // Distances: 
        // |50-80| = 30
        // |80-150| = 70
        // |150-199| = 49
        // |199-0| = 199 (The big jump)
        // |0-10| = 10
        // |10-20| = 10
        // |20-30| = 10
        // Total: 30 + 70 + 49 + 199 + 10 + 10 + 10 = 378
        
        C_SCAN cscan = new C_SCAN(queue, initialHead, true);
        cscan.preparePath();

        while (cscan.executeStep()) {}

        assertEquals(378, cscan.getTotalMovement());
    }
}
