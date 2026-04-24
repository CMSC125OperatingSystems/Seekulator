package cmsc125.project4.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class C_SCAN extends FIFO{

    public C_SCAN(List<Cylinder> input, int head, boolean direction) {
        super(input, head, direction);
    }

    @Override
    public void preparePath() {
        List<Cylinder> sorted = new ArrayList<>(getCylinders());
        sorted.sort((a, b) -> Integer.compare(a.data, b.data));

        List<Cylinder> left = new ArrayList<>();
        List<Cylinder> right = new ArrayList<>();

        for (Cylinder c : sorted) {
            if (c.data >= getHead()) {
                right.add(c);
            } else {
                left.add(c);
            }
        }

        List<Cylinder> finalPath = new ArrayList<>();

        if (isDirection()) { // Moving Right (towards 199)
            // 1. Service Right (ascending)
            finalPath.addAll(right);

            // 2. Wrap around logic: Only if there are requests on the other side
            if (!left.isEmpty()) {
                // Touch the end boundary
                Cylinder endBoundary = new Cylinder(199);
                endBoundary.setVirtual(true);
                finalPath.add(endBoundary);

                // Instant jump to the start boundary
                Cylinder startBoundary = new Cylinder(0);
                startBoundary.setVirtual(true);
                finalPath.add(startBoundary);

                // 3. Service the remaining requests (still ascending!)
                finalPath.addAll(left);
            }
        } else { // Moving Left (towards 0)
            // 1. Service Left (descending)
            for (int i = left.size() - 1; i >= 0; i--) {
                finalPath.add(left.get(i));
            }

            if (!right.isEmpty()) {
                // Touch start boundary
                Cylinder startBoundary = new Cylinder(0);
                startBoundary.setVirtual(true);
                finalPath.add(startBoundary);

                // Instant jump to end boundary
                Cylinder endBoundary = new Cylinder(199);
                endBoundary.setVirtual(true);
                finalPath.add(endBoundary);

                // 2. Service remaining (still descending!)
                for (int i = right.size() - 1; i >= 0; i--) {
                    finalPath.add(right.get(i));
                }
            }
        }

        this.path = finalPath;
    }
}


