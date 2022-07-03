package d.spidchenko.ponggame.physics

import java.util.*
import kotlin.math.*

class Vector2d {
    // fields
    private var r: Double
    private var theta: Double

    // Backing getters/setters for tests
    var x: Double
        get() = r
        set(value) {
            r = value
        }

    // Backing getters/setters for tests
    var y: Double
        get() = theta
        set(value) {
            theta = value
        }

    // default is to make them immutable.
    private var mutable = false

    /**
     * Create a new immutable vector of size (0,0).
     */
    constructor() {
        r = 0.0
        theta = 0.0
    }

    /**
     * Create a new vector of size (0,0).
     *
     * @param mutable true if vector can be modified
     */
    constructor(mutable: Boolean) {
        r = 0.0
        theta = 0.0
        this.mutable = mutable
    }

    /**
     * Create a new immutable vector of a user defined size.
     *
     * @param x the x component
     * @param y the y component
     */
    constructor(x: Double, y: Double) {
        r = x
        theta = y
    }

    /**
     * Create a new vector of a user defined size.
     *
     * @param x       the x component
     * @param y       the y component
     * @param mutable true if vector can be modified
     */
    constructor(x: Double, y: Double, mutable: Boolean) : this(x, y) {
        this.mutable = mutable
    }

    /**
     * Create an immutable copy of vector which is a copy of another vector.
     *
     * @param v the vector to copy
     */
    constructor(v: Vector2d) {
        r = v.r
        theta = v.theta
    }

    /**
     * Create an immutable copy of vector which is a copy of another vector.
     *
     * @param v       the vector to copy
     * @param mutable true if vector can be modified
     */
    constructor(v: Vector2d, mutable: Boolean) : this(v) {
        this.mutable = mutable
    }

    private fun setToMutable() {
        mutable = true
    }

    /**
     * Update the position of this vector.
     *
     * @param x the new x position of this vector
     * @param y the new y position of this vector
     * @throws IllegalArgumentException is this is an immutable vector
     */
    operator fun set(x: Double, y: Double) {
        if (mutable) {
            r = x
            theta = y
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    /**
     * Update the position of this vector.
     *
     * @param v the vector to copy from
     * @throws IllegalArgumentException is this is an immutable vector
     */
    fun set(v: Vector2d) {
        if (mutable) {
            set(v.r, v.theta)
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }
    // compare for equality (needs to allow for Object type argument...)
    /**
     * Returns whether a provided object is equal to this one
     *
     * @param o the object to compare against
     * @return boolean
     * whether or not o was the same as this
     */
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val vector2D = o as Vector2d
        if (vector2D.r.compareTo(r) != 0) return false
        return vector2D.theta.compareTo(theta) == 0
    }

    /**
     * Returns whether the provided object is at least as close to this
     * as provided
     *
     * @param o   the object to compare against
     * @param eps The provided error rate epsilon
     * @return boolean
     * The result - true if they are ~equal
     */
    fun roughlyEquals(o: Any?, eps: Float): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val vector2D = o as Vector2d
        if (abs(r - vector2D.r) > eps) return false
        return abs(theta - vector2D.theta) <= eps
    }

    fun zero() {
        r = 0.0
        theta = 0.0
    }

    /**
     * Returns the hashcode for this object
     *
     * @return int
     * The hashcode result
     */
    override fun hashCode(): Int {
        var result: Int
        var temp: Long
        temp = if (r != +0.0) java.lang.Double.doubleToLongBits(r) else 0L
        result = (temp xor (temp ushr 32)).toInt()
        temp = if (theta != +0.0) java.lang.Double.doubleToLongBits(theta) else 0L
        result = 31 * result + (temp xor (temp ushr 32)).toInt()
        return result
    }

    /**
     * Returns the mathematical magnitude of this
     *
     * @return double
     * The magnitude
     */
    fun mag(): Double {
        return sqrt(magSquared())
    }

    /**
     * Returns the mathematical magnitude squared of this
     *
     * @return double
     * The magnitude squared
     */
    fun magSquared(): Double {
        return r * r + theta * theta
    }

    /**
     * Returns the angle of this vector in respect to (1,0)
     *
     * @return double
     * The angle - in Radians
     */
    fun theta(): Double {
        return atan2(theta, r)
    }

    /**
     * Returns the direction between the two vectors starting from this one
     *
     * @param other The other vector for the comparison
     * @return Vector2d
     * The result of the operation
     */
    fun getNormalDirectionBetween(other: Vector2d): Vector2d {
        val direction = Vector2d(true)
        direction.r = other.r - r
        direction.theta = other.theta - theta
        direction.normalise()
        // System.out.println(direction);
        return direction
    }

    /**
     * Returns the angle between the two Vectors in radians
     *
     * @param v The other vector
     * @return double
     * The angle - In radians
     */
    fun angleBetween(v: Vector2d): Double {
        // return (float)
        // Math.toDegrees(Math.acos(Math.toRadians(scalarProduct(v) / (mag() *
        // v.mag()))));
        // return (float)
        // Math.toDegrees(Vector2d.toPolar(getNormalDirectionBetween(v)).getTheta());
        return Math.toDegrees(theta() - v.theta())
    }

    // String for displaying vector as text
    override fun toString(): String {
        return "Vector2d{x=$r, y=$theta}"
    }
    // add argument vector
    /**
     * Mathematical add operation
     * @param v
     * The Vector to add to this one
     */
    fun add(v: Vector2d) {
        add(v.r, v.theta)
    }
    // add coordinate values
    /**
     * Mathematical add operation with two inputs
     * @param x
     * The value to be added to X axis
     * @param y
     * The value to be added to Y axis
     */
    fun add(x: Double, y: Double) {
        if (mutable) {
            r += x
            theta += y
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    // weighted add - frequently useful
    fun add(v: Vector2d, fac: Double) {
        add(v.r * fac, v.theta * fac)
    }

    // subtract argument vector
    fun subtract(v: Vector2d) {
        subtract(v.r, v.theta)
    }

    // subtract coordinate values
    fun subtract(x: Double, y: Double) {
        if (mutable) {
            r -= x
            theta -= y
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    // multiply with factor
    fun multiply(fac: Double) {
        if (mutable) {
            r *= fac
            theta *= fac
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    fun divide(fac: Double) {
        // TODO Check for zero division
        require(fac != 0.0) { "Factor is 0 - Can't divide by 0" }
        if (mutable) {
            r /= fac
            theta /= fac
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    // "wrap" vector with respect to given positive values w and h
    // method assumes that x >= -w and y >= -h
    fun wrap(w: Double, h: Double) {
        if (mutable) {
            if (r >= w) {
                r %= w
            }
            if (theta >= h) {
                theta %= h
            }
            if (r < 0) {
                r = (r + w) % w
            }
            if (theta < 0) {
                theta = (theta + h) % h
            }
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    // rotate by angle given in radians
    fun rotate(theta: Double) {
        set(
            r * cos(theta) - this.theta * sin(theta), r * sin(theta) + this.theta
                    * cos(theta)
        )
    }

    // scalar product with argument vector
    fun scalarProduct(v: Vector2d): Double {
        return r * v.r + theta * v.theta
    }

    fun dot(v: Vector2d): Double {
        return scalarProduct(v)
    }

    // returns the magnitude of the cross product
    fun crossMag(v1: Vector2d): Double {
        return r * v1.theta - theta * v1.r
    }

    // distance to argument vector
    fun dist(v: Vector2d): Double {
        return sqrt(distSquared(v))
    }

    // distance squared to argument vector
    fun distSquared(v: Vector2d): Double {
        val tempX = (r - v.r) * (r - v.r)
        val tempY = (theta - v.theta) * (theta - v.theta)
        return tempX + tempY
    }

    fun clone(target: Vector2d) {
        if (mutable) {
            r = target.r
            theta = target.theta
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    // normalise vector so that mag becomes 1
    // direction is unchanged
    fun normalise() {
        if (mutable) {
            if (r != 0.0 || theta != 0.0) {
                multiply(1 / mag())
            }
        } else {
            throw IllegalAccessError("This Vector2d is immutable")
        }
    }

    companion object {

        private val random = Random()

        /**
         * Static mathematical add operation, returning a new Vector2d
         * @param first
         * The first argument
         * @param second
         * The second argument
         * @return Vector2d
         * A unique Vector2d that represents the addition of
         * the two inputs
         */
        fun add(first: Vector2d, second: Vector2d): Vector2d {
            val third = Vector2d(first, true)
            third.add(second)
            return third
        }

        /**
         * Static mathematical add operation with two inputs
         * @param first
         * The starting point to add to
         * @param x
         * The value to be added to the X axis
         * @param y
         * The value to be added to the Y axis
         * @return
         * The unique Vector2d that represents the result of the operation
         */
        fun add(first: Vector2d, x: Double, y: Double): Vector2d {
            val third = Vector2d(first, true)
            third.add(x, y)
            return third
        }

        fun add(
            first: Vector2d, second: Vector2d,
            fac: Double
        ): Vector2d {
            val third = Vector2d(first, true)
            third.add(second.r * fac, second.theta * fac)
            return third
        }

        fun subtract(first: Vector2d, second: Vector2d): Vector2d {
            val third = Vector2d(first, true)
            third.subtract(second)
            return third
        }

        fun subtract(first: Vector2d, x: Double, y: Double): Vector2d {
            val third = Vector2d(first, true)
            third.subtract(x, y)
            return third
        }

        @JvmOverloads
        fun multiply(first: Vector2d, fac: Double = 1.0): Vector2d {
            val second = Vector2d(first, true)
            second.multiply(fac)
            return second
        }

        fun divide(first: Vector2d, fac: Double): Vector2d {
            val second = Vector2d(first, true)
            second.divide(fac)
            second.mutable = first.mutable
            return second
        }

        fun scalarProduct(v1: Vector2d, v2: Vector2d): Double {
            val vector = Vector2d(v1, true)
            return vector.scalarProduct(v2)
        }

        fun dot(v1: Vector2d, v2: Vector2d): Double {
            return scalarProduct(v1, v2)
        }

        fun crossMag(v1: Vector2d, v2: Vector2d): Double {
            return v1.crossMag(v2)
        }

        fun toCartesian(input: Vector2d): Vector2d {
            val x = input.theta * Math.cos(input.r)
            val y = input.theta * Math.sin(input.r)
            return Vector2d(x, y, input.mutable)
        }

        fun toPolar(input: Vector2d): Vector2d {
            val r = sqrt(
                input.r * input.r + input.theta
                        * input.theta
            )
            val theta = tanh(input.theta / input.r)
            return Vector2d(r, theta, input.mutable)
        }

        fun clone(target: Vector2d, source: Vector2d) {
            if (source.mutable) {
                source.r = target.r
                source.theta = target.theta
            } else {
                throw IllegalAccessError("This Vector2d is immutable")
            }
        }

        fun normalise(first: Vector2d): Vector2d {
            val second = Vector2d(first, true)
            second.normalise()
            // if (first.mutable)
            //     second.setToMutable();
            return second
        }

        fun getRandomCartesian(
            xLimit: Double, yLimit: Double,
            mutable: Boolean
        ): Vector2d {
            return Vector2d(
                random.nextFloat() * xLimit, random.nextFloat()
                        * yLimit, mutable
            )
        }

        fun getRandomPolar(
            angleRange: Double, speedMin: Double,
            speedMax: Double, mutable: Boolean
        ): Vector2d {
            return Vector2d(
                random.nextFloat() * angleRange - angleRange / 2,
                if (speedMin != speedMax) random.nextFloat() * speedMax - speedMin
                        + speedMin else speedMax, mutable
            )
        }
    }
}