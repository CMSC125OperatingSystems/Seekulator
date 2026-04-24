package cmsc125.project4.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class C_LOOK extends FIFO{

    public C_LOOK(List<Cylinder> input, int head, boolean direction) {
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

        // 3. Build the circular path without boundaries
        List<Cylinder> finalPath = new ArrayList<>();

        if (isDirection()) { // Moving Right (towards 199)
            // Service Right (ascending)
            finalPath.addAll(right);

            // Circular Jump: Immediately add the left side (still ascending)
            // No virtual 199 or 0 added here!
            finalPath.addAll(left);

        } else { // Moving Left (towards 0)
            // Service Left (descending)
            for (int i = left.size() - 1; i >= 0; i--) {
                finalPath.add(left.get(i));
            }

            // Circular Jump: Immediately add the right side (still descending)
            for (int i = right.size() - 1; i >= 0; i--) {
                finalPath.add(right.get(i));
            }
        }

        this.path = finalPath;
    }
}



