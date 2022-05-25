package inspiaaa.decaf.maths;

import java.util.Objects;

public class Vector2Int {

    public int x;
    public int y;

    public Vector2Int() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2Int(int x) {
        this.x = x;
        this.y = 0;
    }

    public Vector2Int(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2Int add(Vector2Int other) {
        return new Vector2Int(x + other.x, y + other.y);
    }

    public Vector2Int add(int x, int y) {
        return new Vector2Int(this.x + x, this.y + y);
    }

    // In place
    public Vector2Int addi(Vector2Int other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    // In place
    public Vector2Int addi(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2Int sub(Vector2Int other) {
        return new Vector2Int(x + other.x, y + other.y);
    }

    public Vector2Int sub(int x, int y) {
        return new Vector2Int(this.x - x, this.y - y);
    }

    // In place
    public Vector2Int subi(Vector2Int other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    // In place
    public Vector2Int subi(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2Int mul(int factor) {
        return new Vector2Int(x * factor, y * factor);
    }

    // In place
    public Vector2Int muli(int factor) {
        this.x *= factor;
        this.y *= factor;
        return this;
    }

    public Vector2Int div(int divisor) {
        return new Vector2Int(x / divisor, y / divisor);
    }

    // In place
    public Vector2Int divi(int divisor) {
        this.x /= divisor;
        this.y /= divisor;
        return this;
    }

    public Vector2Int copy() {
        return new Vector2Int(x, y);
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public boolean equals(Vector2Int other) {
        return x == other.x && y == other.y;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Vector2Int)) {
            return false;
        }

        Vector2Int other = (Vector2Int) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2(" + x + ", " + y + ')';
    }

}
