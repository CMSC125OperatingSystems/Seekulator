package cmsc125.lab3;

import org.junit.jupiter.api.Test;
import cmsc125.project4.services.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

class SCANTest {

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
    void testScanUpwardWithBoundary() {
        // Head: 50, Direction: True (Up/Right)
        // Expected Path: 80 -> 150 -> 199 (Virtual) -> 30 -> 20 -> 10
        SCAN scan = new SCAN(queue, initialHead, true);
        scan.preparePath();

        // 1. Service Right
        assertTrue(scan.executeStep());
        assertEquals(80, scan.getHead());
        assertFalse(scan.getCurrentCylinder().virtual); // Assuming you add a getHeadCylinder() or similar

        assertTrue(scan.executeStep());
        assertEquals(150, scan.getHead());

        // 2. Touch Boundary (SCAN specific)
        assertTrue(scan.executeStep());
        assertEquals(199, scan.getHead());
        // This is the important check for your new flag!
        // You'll need a way to access the current cylinder being processed
    }

    @Test
    void testScanDownwardWithBoundary() {
        // Head: 50, Direction: False (Down/Left)
        // Expected Path: 30 -> 20 -> 10 -> 0 (Virtual) -> 80 -> 150
        SCAN scan = new SCAN(queue, initialHead, false);
        scan.preparePath();

        // 1. Service Left
        scan.executeStep(); // 30
        scan.executeStep(); // 20
        scan.executeStep(); // 10

        // 2. Touch Boundary 0
        assertTrue(scan.executeStep());
        assertEquals(0, scan.getHead());

        // 3. Reverse and Service Right
        assertTrue(scan.executeStep());
        assertEquals(80, scan.getHead());
    }

    @Test
    void testTotalMovementScan() {
        // Head 50, Dir: True. 
        // Path: 50 -> 80 -> 150 -> 199 -> 30 -> 20 -> 10
        // Distances: |50-80|=30, |80-150|=70, |150-199|=49, |199-30|=169, |30-20|=10, |20-10|=10
        // Total: 30 + 70 + 49 + 169 + 10 + 10 = 338
        
        SCAN scan = new SCAN(queue, initialHead, true);
        scan.preparePath();

        while (scan.executeStep()) {}

        assertEquals(338, scan.getTotalMovement());
    }
}
