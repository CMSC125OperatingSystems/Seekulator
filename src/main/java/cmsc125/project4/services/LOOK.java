package cmsc125.project4.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LOOK extends FIFO{

	public LOOK(List<Cylinder> input, int head, boolean direction) {
        super(input, head, direction);
    }

    @Override
    public void preparePath() {
        // 1. Sort the cylinders numerically
        List<Cylinder> sorted = new ArrayList<>(getCylinders());
        sorted.sort((a, b) -> Integer.compare(a.data, b.data));

        List<Cylinder> left = new ArrayList<>();
        List<Cylinder> right = new ArrayList<>();

        // 2. Partition based on current head position
        for (Cylinder c : sorted) {
            if (c.data >= getHead()) {
                right.add(c);
            } else {
                left.add(c);
            }
        }

        // 3. Create the sequence of movement
        List<Cylinder> finalPath = new ArrayList<>();

        if (isDirection()) { // Moving Right/Up (towards 199)
            // Service Right (ascending), then Left (descending)
            finalPath.addAll(right);
            for (int i = left.size() - 1; i >= 0; i--) {
                finalPath.add(left.get(i));
            }
        } else { // Moving Left/Down (towards 0)
            // Service Left (descending), then Right (ascending)
            for (int i = left.size() - 1; i >= 0; i--) {
                finalPath.add(left.get(i));
            }
            finalPath.addAll(right);
        }

        // Assign to the path variable inherited from FIFO
        this.path = finalPath; 
    }

}
