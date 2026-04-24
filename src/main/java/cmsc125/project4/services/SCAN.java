package cmsc125.project4.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SCAN extends FIFO{

    public SCAN(List<Cylinder> input, int head, boolean direction) {
        super(input, head, direction);
    }

    @Override
    public void preparePath() {
        // 1. Sort the input cylinders numerically
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

        // 3. Build the path with the boundary (The "SCAN" requirement)
        List<Cylinder> finalPath = new ArrayList<>();

        if (isDirection()) { // Moving Right (towards 199)
            // Service Right (ascending)
            finalPath.addAll(right);

            // ADD BOUNDARY: If there are requests to the left, we MUST touch 199
            if (!left.isEmpty()) {
                Cylinder boundary = new Cylinder(199);
                boundary.setVirtual(true); // <--- Use your new method here
                finalPath.add(boundary);
            }

            // Service Left (descending)
            for (int i = left.size() - 1; i >= 0; i--) {
                finalPath.add(left.get(i));
            }
        } else { // Moving Left (towards 0)
            // Service Left (descending)
            for (int i = left.size() - 1; i >= 0; i--) {
                finalPath.add(left.get(i));
            }

            // ADD BOUNDARY: If there are requests to the right, we MUST touch 0
            if (!right.isEmpty()) {
                Cylinder boundary = new Cylinder(0);
                boundary.setVirtual(true); // <--- Use your new method here
                finalPath.add(boundary);
            }

            // Service Right (ascending)
            finalPath.addAll(right);
        }

        this.path = finalPath;
    }
}

