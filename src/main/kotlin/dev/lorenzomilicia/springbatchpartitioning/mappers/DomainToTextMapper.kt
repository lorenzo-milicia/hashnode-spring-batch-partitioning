package dev.lorenzomilicia.springbatchpartitioning.mappers

import dev.lorenzomilicia.springbatchpartitioning.domain.CountrySummedData

fun CountrySummedData.toOutputText(): String =
	"$countryName: $totalAmount"