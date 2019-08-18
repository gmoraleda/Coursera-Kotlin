class Rational constructor (n: BigInteger, d: BigInteger): Comparable<Rational> {

    var n: BigInteger
    var d: BigInteger

    init {
        if (d == 0.toBigInteger()) { throw IllegalArgumentException("Denominator cannot be zero") }
        this.n = n
        this.d = d
    }

    operator fun plus(increment: Rational): Rational {
        val (lcm, r1, r2) = extractComponents(increment)
        return Rational(r1+r2, lcm)
    }

    operator fun minus(decrement: Rational): Rational {
        val (lcm, r1, r2) = extractComponents(decrement)
        return Rational(r1-r2, lcm)    }

    private fun extractComponents(other: Rational): Triple<BigInteger, BigInteger, BigInteger> {
        val lcm = lcm(d, other.d)
        val r1 = lcm / d * n
        val r2 = lcm / other.d * other.n
        return Triple(lcm, r1, r2)
    }

    operator fun times(multiplier: Rational) = Rational(n * multiplier.n, d * multiplier.d)
    operator fun div(divisor: Rational) = Rational(n * divisor.d, d * divisor.n)
    operator fun unaryMinus() = Rational(-n, d)
    operator fun inc() = Rational(n + 1.toBigInteger(), d)

    override fun equals(other: Any?): Boolean {
        other as Rational
        normalize()
        other.normalize()
        return this.d == other.d && this.n == other.n
    }

    override fun compareTo(other: Rational): Int {
        normalize()
        val (_, r1, r2) = extractComponents(other)
        return r1.compareTo(r2)
    }

    override fun toString(): String {
        normalize()
        if (this.d == 1.toBigInteger()) { return "$n" }
        return "$n/$d"
    }

    private fun normalize() {
        val gcd = this.n.gcd(this.d)
        this.n = this.n / gcd
        this.d = this.d / gcd

        val neg = -1
        val pos = 1

        if (this.n.signum() == neg && this.d.signum() == neg) {
            this.n = this.n.abs()
            this.d = this.d.abs()
        }

        if (this.n.signum() == pos && this.d.signum() == neg) {
            this.n = -this.n
            this.d = this.d.abs()
        }
    }

    private fun lcm(v1: BigInteger, v2: BigInteger): BigInteger = (v1 * v2) / v1.gcd(v2)

}

infix fun Int.divBy(v2: Int): Rational {
    return Rational(this.toBigInteger(), v2.toBigInteger())
}

infix fun Long.divBy(v2: Long): Rational {
    return Rational(this.toBigInteger(), v2.toBigInteger())
}

infix fun BigInteger.divBy(denominator: BigInteger): Rational {
    return Rational(this, denominator)
}

fun String.toRational(): Rational {
    val splicedString = split("/")
    val n = splicedString.first()
    val d: String = splicedString.elementAtOrElse(1, {"1"})

    return Rational(n.toBigInteger(), d.toBigInteger())
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)


    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)
    
    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}
