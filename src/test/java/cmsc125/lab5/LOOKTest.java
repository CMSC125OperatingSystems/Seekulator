package cmsc125.lab5;

import org.junit.jupiter.api.Test;
import cmsc125.project4.services.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

class LOOKTest {

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
    void testLookUpwardDirection() {
        // Initial Head: 50, Direction: True (Up/Right)
        LOOK look = new LOOK(queue, initialHead, true);
        look.preparePath();

        // Path should be: 80 -> 150 (End of Right) -> 30 -> 20 -> 10 (Descending Left)
        
        // Move Up
        assertTrue(look.executeStep());
        assertEquals(80, look.getHead());
        
        assertTrue(look.executeStep());
        assertEquals(150, look.getHead());

        // Turn Around (Notice it doesn't go to 199)
        assertTrue(look.executeStep());
        assertEquals(30, look.getHead());

        assertTrue(look.executeStep());
        assertEquals(20, look.getHead());

        assertTrue(look.executeStep());
        assertEquals(10, look.getHead());
        
        assertFalse(look.executeStep()); // Finished
    }

    @Test
    void testLookDownwardDirection() {
        // Initial Head: 50, Direction: False (Down/Left)
        LOOK look = new LOOK(queue, initialHead, false);
        look.preparePath();

        // Path should be: 30 -> 20 -> 10 (End of Left) -> 80 -> 150 (Ascending Right)
        
        assertTrue(look.executeStep());
        assertEquals(30, look.getHead());

        assertTrue(look.executeStep());
        assertEquals(20, look.getHead());

        assertTrue(look.executeStep());
        assertEquals(10, look.getHead());

        // Turn Around (Notice it doesn't go to 0)
        assertTrue(look.executeStep());
        assertEquals(80, look.getHead());

        assertTrue(look.executeStep());
        assertEquals(150, look.getHead());
    }

    @Test
    void testTotalMovementLook() {
        // Head 50, Dir: True. Path: 50->80->150->30->20->10
        // Distances: |50-80|=30, |80-150|=70, |150-30|=120, |30-20|=10, |20-10|=10
        // Total: 30 + 70 + 120 + 10 + 10 = 240
        
        LOOK look = new LOOK(queue, initialHead, true);
        look.preparePath();

        while (look.executeStep()) {}

        assertEquals(240, look.getTotalMovement());
    }
}
