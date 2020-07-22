package dev.ravindu.skgl

import java.math.BigInteger
import kotlin.math.floor

/**
 * Helper functions that perform mathematical operations.
 */
internal object MathHelpers {

	@Suppress("SpellCheckingInspection")
	private const val BASE26_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

	private val BIGINTEGER_26 = 26.toBigInteger()

	/**
	 * Returns the modulus of dividing the specified number [n] by the specified [base].
	 * `SKGL.methods.modulo`
	 */
	fun modulo(n: Int, base: Int): Int {
		return n - (base * floor(n.toDouble() / base.toDouble()).toInt())
	}

	/**
	 * Returns the modulus of diving the specified number [n] by 10.
	 * `SKGL.methods.modulo`
	 */
	fun modulo10(n: Int): Int = modulo(n, 10)

	/**
	 * Converts the specified decimal value [n] to Base26 representation.
	 * `SKGL.methods.base10ToBase26`
	 */
	fun base10To26(n: BigInteger): String {
		val sb = StringBuilder()

		var rem: BigInteger
		var num = n
		while (num >= BIGINTEGER_26) {
			rem = num % BIGINTEGER_26
			sb.append(BASE26_CHARS[rem.toInt()])
			num = (num - rem) / BIGINTEGER_26
		}
		sb.append(BASE26_CHARS[num.toInt()])

		return sb.reverse().toString()
	}

	/**
	 * Converts the specified Base26 string [s] to its decimal representation.
	 * `SKGL.methods.base26ToBase10`
	 */
	fun base26To10(s: String): BigInteger {
		var res = BigInteger.ZERO
		for (i in s.indices) {
			val p = BIGINTEGER_26.pow(s.length - i - 1)
			res += p * BASE26_CHARS.indexOf(s[i]).toBigInteger()
		}
		return res
	}

	/**
	 * `SKGL.methods.twentyfiveByteHash`
	 */
	fun hash25(s: String): String {

		if (s.isEmpty()) {
			throw IllegalArgumentException("String to be hashed is empty.")
		}

		val nBlocks = s.length / 5
		var hash = ""
		if (s.length <= 5) {
			hash = hash8(s)
		}
		else {
			for (i in 0 until nBlocks) {
				hash += hash8(
					if (i == nBlocks - 1) {
						s.substring((nBlocks - 1) * 5)
					} else {
						s.substring(i * 5, (i * 5) + 5)
					}
				)
			}
		}
		return hash
	}

	/**
	 * `SKGL.methods.getEightByteHash`
	 */
	fun hash8(s: String, exclusiveUpperLimit: Long = 1_000_000_000): String {

		if (s.isEmpty()) {
			throw IllegalArgumentException("String to be hashed is empty.")
		}

		var hash = 0

		val buf = Charsets.UTF_16LE.encode(s)
		while (buf.hasRemaining()) {
			hash += buf.get()
			hash += hash shl 10
			hash = hash xor (hash ushr 6)
		}

		hash += hash shl 3
		hash = hash xor (hash ushr 11)
		hash += hash shl 15

		// Grab the lower 32 bits (8 bytes) of the long.
		val lHash = hash.toLong() and 0xFFFFFFFF

		var res = lHash % exclusiveUpperLimit
		if (res == 0L) {
			res = 1
		}

		val check = exclusiveUpperLimit / res
		if (check > 1) {
			res *= check
		}

		if (exclusiveUpperLimit == res) {
			res /= 10
		}

		return res.toString()
	}
}
