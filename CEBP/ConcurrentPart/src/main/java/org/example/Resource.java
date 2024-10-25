package main.java.org.example;

public class Resource {
    private String type;  // The type of resource (e.g., "wood", "stone", "gold")
    private int x;        // X coordinate on the map
    private int y;        // Y coordinate on the map

    public Resource(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "type='" + type + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}