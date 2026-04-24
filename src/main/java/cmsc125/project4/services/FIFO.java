package cmsc125.project4.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FIFO {
    private List<Cylinder> cylinders;
    protected List<Cylinder> path;
    private int head;
    private Cylinder currentCylinder;

	private boolean direction;  //true means going up/right towards 199 and away from 0
                                // direction does nothing for fifo and sstf

    private int totalMovement = 0;

	public FIFO(List<Cylinder> input, int head, boolean direction) {
        cylinders = input;
        this.head = head;
        this.direction = direction;
    }

	public FIFO(Cylinder[] input, int head, boolean direction) {
        this.cylinders = new ArrayList<>(Arrays.asList(input));
        this.head = head;
        this.direction = direction;
    }

    public boolean executeStep() {
        if (!path.isEmpty()) {
            currentCylinder = path.removeFirst();

            int movement = Math.abs(head - currentCylinder.data);
            totalMovement += movement;

            head = currentCylinder.data;
            currentCylinder.visited = true;

            return true; // Step successful
        }
        return false; // No more cylinders to process
    }

    public void preparePath() {
        // For FIFO, the path is just the input order
        this.path = new ArrayList<>(cylinders);
    }

    public int getTotalMovement() {
        return totalMovement;
    }

    public List<Cylinder> getCylinders() {
		return cylinders;
	}

	public int getHead() {
		return head;
	}

	public boolean isDirection() {
		return direction;
	}

    public List<Cylinder> getPath() {
		return path;
	}

	public Cylinder getCurrentCylinder() {
		return currentCylinder;
	}

}
