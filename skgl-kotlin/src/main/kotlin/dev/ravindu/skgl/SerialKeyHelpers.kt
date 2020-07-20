package dev.ravindu.skgl

import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal object SerialKeyHelpers {

	val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

	/**
	 * `SKGL.methods._decrypt`, `SKGL.methods._decText`
	 */
	fun decode(key: String, secret: String): String {
		val info = MathHelpers.base26To10(key).toString()

		val slice = info.substring(9)
		val secretHash25 = MathHelpers.hash25(secret)

		var decodedSlice = ""
		for (i in slice.indices) {
			val n = slice[i].toString().toInt() - secretHash25[MathHelpers.modulo(i, secretHash25.length)].toString().toInt()
			decodedSlice += MathHelpers.modulo10(n).toString()
		}

		return info.take(9) + decodedSlice
	}

	/**
	 * `SKGL.methods._encrypt`, `SKGL.methods._encText`
	 */
	fun encode(secret: String, createdOn: LocalDate, duration: Int, features: Set<Int>, r: Int): String {
		var result = 0L

		result += DATE_FORMATTER.format(createdOn).toInt()
		result *= 1000

		result += duration
		result *= 1000

		result += features.fold(0) { acc, n -> acc or (1 shl (8 - n)) }
		result *= 100000

		result += r

		val resultHash = MathHelpers.hash8(result.toString())
		val secretHash = MathHelpers.hash25(secret)

		val sliceBuilder = StringBuilder()
		val resultString = result.toString()
		for (i in resultString.indices) {
			val resultPart = resultString[i].toString().toInt()
			val secretPart = secretHash[MathHelpers.modulo(i, secretHash.length)].toString().toInt()
			sliceBuilder.append(MathHelpers.modulo10(resultPart + secretPart))
		}

		return MathHelpers.base10To26((resultHash + sliceBuilder.toString()).toBigInteger())
	}
}
