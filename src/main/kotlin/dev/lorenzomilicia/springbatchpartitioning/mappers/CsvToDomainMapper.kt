package dev.lorenzomilicia.springbatchpartitioning.mappers

import dev.lorenzomilicia.springbatchpartitioning.domain.CountryRawData

fun String.toDomain(): CountryRawData =
	split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*\$)".toRegex())
		.let { columns ->
			CountryRawData(
				countryName = columns.first(),
				yearlyData = columns
					.drop(1)
					.filter { it.isNotBlank() && it.isNotEmpty() }
					.map {
						it.resolveMicronSymbol().toBigDecimal()
					}
			)
		}

fun String.resolveMicronSymbol(): String = replace("µ", "e-6")