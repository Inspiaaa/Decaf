package inspiaaa.decaf.maths;

public class Rectangle {
    public float x, y, width, height;

    public Rectangle(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    public boolean contains(float x, float y) {
        return getLeft() <= x && x <= getRight()
                && getBottom() <= y && y <= getTop();
    }

    public boolean intersects(Rectangle other) {
        return getLeft() < other.getRight()
                && getRight() > other.getLeft()
                && getTop() > other.getBottom()
                && getBottom() < other.getTop();
    }

    public float getLeft() {
        return x;
    }

    public float getTop() {
        return y + height;
    }

    public float getRight() {
        return x + width;
    }

    public float getBottom() {
        return y;
    }

    public float getCenterX() {
        return x + width / 2;
    }

    public float getCenterY() {
        return y + height / 2;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
