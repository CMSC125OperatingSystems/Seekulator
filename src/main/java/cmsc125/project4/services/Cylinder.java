package cmsc125.project4.services;

/**
 * Cylinder
 * this is just a custon data type
 */
public class Cylinder implements Comparable<Cylinder> {
    public int data;
    public boolean visited;
    public boolean virtual;

    public Cylinder(int data) {
        this.data = data;
        visited = false;
        virtual = false;
    }

    public void setVirtual(boolean val) {
        virtual = val;
    }

    @Override
    public int compareTo(Cylinder other) {
        return Integer.compare(this.data, other.data);
    }
}
