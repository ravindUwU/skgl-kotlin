<h1>
	SKGL (Kotlin)
	<a href="https://search.maven.org/artifact/dev.ravindu/skgl-kotlin">
		<img src="https://img.shields.io/maven-central/v/dev.ravindu/skgl-kotlin">
	</a>
	<a href="LICENSE">
		<img src="https://img.shields.io/github/license/RavinduL/skgl-kotlin">
	</a>
</h1>

The [SKGL](https://github.com/Cryptolens/SKGL) library, written in Kotlin, with a shiny new API. Use this to generate &
validate human readable, 20-character serial keys with up to 8 features embedded.

<br>

## Usage

```kotlin
import dev.ravindu.skgl.SerialKey
```

### Generate a Key

```kotlin
val key = SerialKey.build("a-secret") {
	features = setOf(1, 3, 5)
	duration = 10
	chunked = true
}

println(key.text) // XXXXX-XXXXX-XXXXX-XXXXX
```

### Decode a Key

```kotlin
val key = SerialKey("XXXXX-XXXXX-XXXXX-XXXXX", "a-secret")

val createdOn = key.createdOn
val expiresOn = key.expiresOn
val hasFeature1 = 1 in key.features
val isExpired = key.calculateIsExpired()
val daysLeft = key.calculateDaysLeft()
```

<br>

## UI

The [TornadoFX](https://tornadofx.io/)-powered [skgl-ui](skgl-ui) project can be used to generate or validate keys,

![About](images/ui-about.png)

![Generate](images/ui-generate.png)

![Validate](images/ui-validate.png)
