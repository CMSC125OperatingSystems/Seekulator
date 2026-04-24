package cmsc125.lab3;

import org.junit.jupiter.api.Test;
import cmsc125.project4.services.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

class CLOOKTest {

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
    void testCLookUpwardMovement() {
        // Head: 50, Direction: True (Up/Right)
        // Expected Order: 80 -> 150 (Last Right) -> 10 (First Left) -> 20 -> 30
        C_LOOK clook = new C_LOOK(queue, initialHead, true);
        clook.preparePath();

        // 1. Service the right side ascending
        assertTrue(clook.executeStep()); // Move to 80
        assertEquals(80, clook.getHead());

        assertTrue(clook.executeStep()); // Move to 150
        assertEquals(150, clook.getHead());

        // 2. The Circular Jump (Immediate jump to the lowest request)
        // Unlike C-SCAN, we do NOT hit 199 or 0.
        assertTrue(clook.executeStep()); 
        assertEquals(10, clook.getHead()); // Jumped from 150 directly to 10

        // 3. Continue servicing ascending from the start
        assertTrue(clook.executeStep()); // Move to 20
        assertEquals(20, clook.getHead());
        
        assertTrue(clook.executeStep()); // Move to 30
        assertEquals(30, clook.getHead());

        assertFalse(clook.executeStep()); // Finished
    }

    @Test
    void testCLookTotalMovement() {
        // Initial Head 50, Dir: True
        // Path: 50 -> 80 -> 150 -> 10 -> 20 -> 30
        // Distances: 
        // |50 - 80|  = 30
        // |80 - 150| = 70
        // |150 - 10| = 140 (The big circular jump)
        // |10 - 20|  = 10
        // |20 - 30|  = 10
        // Total: 30 + 70 + 140 + 10 + 10 = 260
        
        C_LOOK clook = new C_LOOK(queue, initialHead, true);
        clook.preparePath();

        while (clook.executeStep()) {}

        // Compare this to C-SCAN (378) or LOOK (240)
        assertEquals(260, clook.getTotalMovement());
    }

    @Test
    void testCLookDownwardMovement() {
        // Head: 50, Direction: False (Down/Left)
        // Expected Order: 30 -> 20 -> 10 (Last Left) -> 150 (First Right) -> 80
        C_LOOK clook = new C_LOOK(queue, initialHead, false);
        clook.preparePath();

        // Service Left descending
        clook.executeStep(); // 30
        clook.executeStep(); // 20
        clook.executeStep(); // 10

        // Jump to the highest request on the right side
        assertTrue(clook.executeStep()); 
        assertEquals(150, clook.getHead());

        assertTrue(clook.executeStep());
        assertEquals(80, clook.getHead());
    }
}
