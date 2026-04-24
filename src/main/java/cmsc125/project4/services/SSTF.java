package cmsc125.project4.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SSTF extends FIFO{

    public SSTF(List<Cylinder> input, int head, boolean direction) {
        super(input, head, direction);
    }

    @Override
    public void preparePath() {
        // 1. Create a pool of pending requests from the input
        List<Cylinder> pool = new ArrayList<>(getCylinders());

        // 2. We need a local tracker for the head to simulate movement
        int currentPos = getHead();

        // 3. This will store the final sequence of movement
        List<Cylinder> finalPath = new ArrayList<>();

        // 4. Greedy search loop
        while (!pool.isEmpty()) {
            int closestIndex = 0;
            int minDistance = Math.abs(pool.get(0).data - currentPos);

            // Find the cylinder with the shortest seek time from currentPos
            for (int i = 1; i < pool.size(); i++) {
                int distance = Math.abs(pool.get(i).data - currentPos);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestIndex = i;
                }
            }

            // Remove the closest from the pool and add to path
            Cylinder closestCylinder = pool.remove(closestIndex);
            finalPath.add(closestCylinder);

            // Move the simulated head to the new cylinder's position
            currentPos = closestCylinder.data;
        }

        this.path = finalPath;
    }
}


