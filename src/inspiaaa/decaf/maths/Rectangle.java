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

    public void pushOutOf(Rectangle other) {
        float dxToMoveLeft = -(getRight() - other.getLeft());
        float dxToMoveRight = other.getRight() - getLeft();
        float minDx = Math.abs(dxToMoveLeft) < Math.abs(dxToMoveRight) ? dxToMoveLeft : dxToMoveRight;

        float dyToMoveDown = -(other.getTop() - getBottom());
        float dyToMoveUp = other.getTop() - getBottom();
        float minDy = Math.abs(dyToMoveDown) < Math.abs(dyToMoveUp) ? dyToMoveDown : dyToMoveUp;

        if (Math.abs(minDx) < Math.abs(minDy)) {
            x += minDx;
        }
        else {
            y += minDy;
        }
    }

    public void moveAndCollide(Vector2 deltaPos, Rectangle other) {
        move(deltaPos);

        if (!intersects(other)) {
            return;
        }

        if (deltaPos.isZero()) {
            pushOutOf(other);
            return;
        }

        float overlapWidth = (deltaPos.x > 0)
                ? (getRight() - other.getLeft())    // When moving right
                : (other.getRight() - getLeft());   // When moving left

        float overlapHeight = (deltaPos.y > 0)
                ? (getTop() - other.getBottom())    // When moving up
                : (other.getTop() - getBottom());   // When moving down

        // Resolve the collision by moving the least possible distance in either x direction
        // or y direction
        if (overlapWidth < overlapHeight) {
            x -= overlapWidth * Math.signum(deltaPos.x);
        }
        else {
            y -= overlapHeight * Math.signum(deltaPos.y);
        }
    }

    public void move(Vector2 deltaPos) {
        x += deltaPos.x;
        y += deltaPos.y;
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void setPosition(Vector2 pos) {
        x = pos.x;
        y = pos.y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
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

    public Rectangle copy() {
        return new Rectangle(x, y, width, height);
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
