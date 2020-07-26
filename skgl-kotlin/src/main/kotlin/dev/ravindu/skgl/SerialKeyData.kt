package dev.ravindu.skgl

import java.time.LocalDate
import java.time.temporal.ChronoUnit

interface SerialKeyData {

	/**
	 * The duration in days for which the serial key is valid.
	 */
	val duration: Int

	/**
	 * The date on which the serial key was created.
	 */
	val createdOn: LocalDate

	/**
	 * The features that the serial key includes.
	 */
	val features: Set<Int>

	/**
	 * The date on which the serial key expires.
	 */
	val expiresOn: LocalDate
		get() = createdOn.plusDays(duration.toLong())

	/**
	 * Whether the key is expired relative to the specified date [relativeTo] (defaults to the current date).
	 */
	fun calculateIsExpired(relativeTo: LocalDate = LocalDate.now()) = relativeTo.isAfter(expiresOn)

	/**
	 * Calculates the number of days remaining until the serial key expires, relative to the specified date [relativeTo]
	 * (defaults to the current date).
	 */
	fun calculateDaysLeft(relativeTo: LocalDate = LocalDate.now()) = relativeTo.until(expiresOn, ChronoUnit.DAYS).toInt()
}
