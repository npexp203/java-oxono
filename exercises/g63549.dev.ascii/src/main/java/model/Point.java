package model;

public class Point {

    private double x;
    private double y;

    // Constructor
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Constructor that copies to not modify it
    public Point(Point p){
        this.x = p.x;
        this.y = p.y;
    }
    // Method for move the point
    public void move(double dx, double dy){
        this.x += dx;
        this.y += dy;
    }

    // Method for calculate the distance between 2 points
    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
    // Getters for x and y
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        Point p = new Point(1,2);
        System.out.println(p);
        Point p1 = new Point(2,3);
        System.out.println(p1);
        p.move(1,1);
        System.out.println(p);
        System.out.println(p.distanceTo(p1));


    }


}

