package model;

/**
 * The Shape class is an abstract base class for all geometric shapes.
 * It defines common properties like color and methods that all shapes must implement.
 */
public abstract class Shape {
        private char color;

        /**
         * Constructor for the Shape class.
         */
        public Shape() {
                this.color = color;
        }

        /**
         * Gets the color of the shape.
         *
         * @return The character representing the color of the shape.
         */
        public char getColor() {
                return color;
        }

        /**
         * Sets the color of the shape.
         *
         * @param color The character representing the color of the shape.
         */
        public void setColor(char color) {
                this.color = color;
        }

        /**
         * Moves the shape by specified distances along the x and y axes.
         *
         * @param dx The distance to move along the x-axis.
         * @param dy The distance to move along the y-axis.
         */
        public abstract void move(double dx, double dy);

        /**
         * Checks if a given point is inside the shape.
         *
         * @param p The point to check.
         * @return True if the point is inside the shape, false otherwise.
         */
        public abstract boolean isInside(Point p);
}
