@file:JvmName("Program")

package com.example.program

import dev.ravindu.skgl.SerialKey

const val SECRET = "a-secret"
const val BAD_FORMAT = "THIS-KEY-IS-OF-A-WRONG-FORMAT"
const val BAD_KEY = "THISX-KEYXX-ISNTX-VALID"

fun main() {

	section("Generating a key") {
		val key = SerialKey.build {
			// Configure here.
		}
		println("Key: $key")
		println("Text: ${key.text}")
	}

	section("Generating a key that isn't chunked") {
		val key = SerialKey.build {
			chunk = false
		}
		println("Key without chunks: $key")
	}

	section("Decoding a key") {
		val key = SerialKey.build {
			features = setOf(1, 3, 5)
		}
		val decodedKey = SerialKey(key.text)
		println(
			"""
			Decoded key: $decodedKey

			Feature 1? ${1 in decodedKey.features}
			Feature 2? ${2 in decodedKey.features}
			Feature 3? ${3 in decodedKey.features}

			Created on: ${decodedKey.createdOn}
			Expires on: ${decodedKey.expiresOn}
			Expired? ${decodedKey.calculateIsExpired()}
			Days left: ${decodedKey.calculateDaysLeft()}
		""".trimIndent()
		)
	}

	section("Decoding invalid keys") {

		println("Bad format ($BAD_FORMAT)")
		printException { SerialKey(BAD_FORMAT) }

		println("Invalid key ($BAD_KEY)")
		printException { SerialKey(BAD_KEY) }
	}

	section("Using secrets") {

		val key = SerialKey.build(SECRET) {
			features = setOf(1, 2)
		}

		println("Decoding:")
		println(SerialKey(key.text, SECRET))

		println("Decoding without a secret:")
		printException { SerialKey(key.text) }

		println("Decoding with a bad secret:")
		printException { SerialKey(key.text, "BAD-SECRET") }
	}
}

fun section(title: String, fn: () -> Unit) {
	println(title)
	println("`".repeat(title.length))
	fn()
	println()
}

fun printException(fn: () -> Unit) {
	try {
		fn()
	}
	catch (e: Exception) {
		println("Caught ${e::class.simpleName}: $e")
	}
}
