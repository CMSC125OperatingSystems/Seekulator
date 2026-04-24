package cmsc125.lab3;

import org.junit.jupiter.api.Test;
import cmsc125.project4.services.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

class SSTFTest {

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
    void testSSTFExecutionOrder() {
        // Initial Head: 50
        // Targets: 10, 80, 30, 150, 20
        SSTF sstf = new SSTF(queue, initialHead, true);
        sstf.preparePath();

        // 1. Closest to 50 is 30 (dist 20) vs 80 (dist 30)
        assertTrue(sstf.executeStep());
        assertEquals(30, sstf.getHead());

        // 2. Closest to 30 is 20 (dist 10)
        assertTrue(sstf.executeStep());
        assertEquals(20, sstf.getHead());

        // 3. Closest to 20 is 10 (dist 10)
        assertTrue(sstf.executeStep());
        assertEquals(10, sstf.getHead());

        // 4. Closest to 10 is 80 (dist 70)
        assertTrue(sstf.executeStep());
        assertEquals(80, sstf.getHead());

        // 5. Last one is 150 (dist 70)
        assertTrue(sstf.executeStep());
        assertEquals(150, sstf.getHead());
        
        assertFalse(sstf.executeStep()); // Finished
    }

    @Test
    void testSSTFTotalMovement() {
        // Path: 50 -> 30 -> 20 -> 10 -> 80 -> 150
        // |50-30| = 20
        // |30-20| = 10
        // |20-10| = 10
        // |10-80| = 70
        // |80-150|= 70
        // Total: 20 + 10 + 10 + 70 + 70 = 180
        
        SSTF sstf = new SSTF(queue, initialHead, true);
        sstf.preparePath();

        while (sstf.executeStep()) {}

        // Compare: SSTF (180) is much lower than LOOK (240) or SCAN (338)
        assertEquals(180, sstf.getTotalMovement());
    }

    @Test
    void testSSTFWithTie() {
        // Testing how it handles equal distances
        List<Cylinder> tieQueue = new ArrayList<>();
        tieQueue.add(new Cylinder(40));
        tieQueue.add(new Cylinder(60));
        
        // Head is 50. 40 and 60 are both 10 away.
        SSTF sstf = new SSTF(tieQueue, 50, true);
        sstf.preparePath();
        
        assertTrue(sstf.executeStep());
        int firstResult = sstf.getHead();
        // Depending on your loop, it will pick the first one it saw (40)
        assertEquals(40, firstResult);
    }
}
