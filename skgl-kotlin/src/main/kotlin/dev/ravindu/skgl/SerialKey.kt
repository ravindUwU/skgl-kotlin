package dev.ravindu.skgl

import java.time.LocalDate
import kotlin.random.Random

/**
 * Represents a valid (but not necessarily unexpired) serial key.
 *
 * Build a new serial key via [SerialKey.build].
 */
class SerialKey : SerialKeyData {

	/**
	 * Textual representation of the serial key.
	 */
	val text: String

	override val duration: Int
	override val createdOn: LocalDate
	override val features: Set<Int>

	/**
	 * Constructs a new serial key with the specified text [key] and [secret].
	 *
	 * Build a new serial key via [SerialKey.build].
	 *
	 * @throws InvalidSerialKeyException if the specified [key]/[secret] is invalid.
	 */
	constructor(key: String, secret: String) {

		if (!key.matches(Regex("[a-z]{5}-?[a-z]{5}-?[a-z]{5}-?[a-z]{5}", RegexOption.IGNORE_CASE))) {
			throw IllegalArgumentException("Invalid key format.")
		}

		val leanKey = key.replace("-", "").toUpperCase()
		val decoded = SerialKeyHelpers.decode(leanKey, secret)

		val decodedHash8 = decoded.take(9)
		val calculatedHash8 = MathHelpers.hash8(decoded.substring(9, 9 + 19)).take(9)

		if (decodedHash8 != calculatedHash8) {
			throw InvalidSerialKeyException(key)
		}

		this.text = key
		duration = decoded.substring(17, 17 + 3).toInt()
		createdOn = LocalDate.parse(decoded.substring(9, 9 + 8), SerialKeyHelpers.DATE_FORMATTER)

		val featureInt = decoded.substring(20, 20 + 3).toInt()
		features = (0..7).filter { (featureInt and (1 shl 7 - it)) != 0 }.map { it + 1 }.toSet()
	}

	/**
	 * Constructs a new serial key with the specified text [key] and an empty secret.
	 */
	constructor(key: String): this(key, "")

	/**
	 * Constructor used by [SerialKey.build].
	 */
	private constructor(key: String, duration: Int, createdOn: LocalDate, features: Set<Int>) {
		this.text = key
		this.duration = duration
		this.createdOn = createdOn
		this.features = features
	}

	override fun toString(): String {
		return "SerialKey(" +
			"key='$text', " +
			"duration=$duration, " +
			"createdOn=$createdOn, " +
			"features=${features}" +
		")"
	}

	override fun equals(other: Any?): Boolean = other is SerialKey && other.text == text

	override fun hashCode(): Int = text.hashCode()

	/**
	 * Thrown when a [SerialKey] is constructed with an invalid key/secret.
	 */
	class InvalidSerialKeyException(val key: String): Exception("The specified key is invalid.")

	/**
	 * Configuration for building a [SerialKey] via [SerialKey.build].
	 *
	 * @see SerialKey.build
	 */
	class SerialKeyBuildConfig: SerialKeyData {

		override var duration: Int = 30
			set(value) {
				if (value !in 0..999) {
					throw IllegalArgumentException("The duration of a license key should be in the range 0..999")
				}
				field = value
			}

		override var createdOn: LocalDate = LocalDate.now()

		override var features: Set<Int> = setOf()
			set(value) {
				if (value.any { it !in 1..8 }) {
					throw IllegalArgumentException("Features are expected to be in the range 1..8")
				}
				field = value
			}

		var chunk: Boolean = true
	}

	companion object {

		/**
		 * Constructs a [SerialKey] with the specified [secret] (defaults to an empty string) and the specified
		 * [configure] function applied to an instance of [SerialKeyBuildConfig].
		 *
		 * Example 1,
		 * ```kotlin
		 * val s = SerialKey.build()
		 * ```
		 *
		 * Example 2,
		 * ```kotlin
		 * val s = SerialKey.build("secret") {
		 *     duration = 45
		 *     features = setOf(1, 3, 5)
		 *     // createdOn = LocalDate.now()
		 *     // chunked = true
		 * }
		 * ```
		 */
		@JvmStatic
		fun build(secret: String = "", configure: (SerialKeyBuildConfig.() -> Unit)? = null): SerialKey {
			val config = SerialKeyBuildConfig()

			if (configure != null) {
				config.configure()
			}

			val r = Random.nextInt(0, 99999 + 1)
			val key = SerialKeyHelpers.encode(secret, config.createdOn, config.duration, config.features, r)

			return SerialKey(
				if (config.chunk) key.chunked(5).joinToString("-") else key,
				config.duration, config.createdOn, config.features
			)
		}
	}
}
