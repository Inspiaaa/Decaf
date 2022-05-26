package inspiaaa.decaf.maths;

import java.util.Objects;

public class Vector2 {
    public float x;
    public float y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(float x) {
        this.x = x;
        this.y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // In place
    public void normalize() {
        float length = magnitude();

        if (length != 0) {
            x /= length;
            y /= length;
        }
    }

    public Vector2 normalized() {
        Vector2 vec = new Vector2(x, y);
        vec.normalize();
        return vec;
    }

    public float magnitude() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public float sqrMagnitude() {
        return x * x + y * y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 add(float x, float y) {
        return new Vector2(this.x + x, this.y + y);
    }

    // In place
    public Vector2 addi(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    // In place
    public Vector2 addi(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 sub(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 sub(float x, float y) {
        return new Vector2(this.x - x, this.y - y);
    }

    // In place
    public Vector2 subi(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    // In place
    public Vector2 subi(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 mul(float factor) {
        return new Vector2(x * factor, y * factor);
    }

    // In place
    public Vector2 muli(float factor) {
        this.x *= factor;
        this.y *= factor;
        return this;
    }

    public Vector2 div(float divisor) {
        return new Vector2(x / divisor, y / divisor);
    }

    // In place
    public Vector2 divi(float divisor) {
        this.x /= divisor;
        this.y /= divisor;
        return this;
    }

    public float angle() {
        return (float)Math.atan2(y, x);
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public boolean equals(Vector2 other) {
        return x == other.x && y == other.y;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Vector2)) {
            return false;
        }

        Vector2 other = (Vector2) o;
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

    public static float distance(Vector2 a, Vector2 b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    public static float dot(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Vector2 fromAngleRad(float angle) {
        return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
    }

    public static Vector2 fromPolarRad(float radius, float angle) {
        return new Vector2(radius * (float)Math.cos(angle), radius * (float)Math.sin(angle));
    }

    public static Vector2 lerp(Vector2 start, Vector2 end, float t) {
        t = (t > 1) ? 1 : ((t < 0) ? 0 : t);
        return new Vector2((end.x - start.x) * t + start.x, (end.y - start.y) * t + start.y);
    }
}
