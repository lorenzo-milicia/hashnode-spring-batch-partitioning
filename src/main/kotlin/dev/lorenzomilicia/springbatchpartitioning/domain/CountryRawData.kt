package dev.lorenzomilicia.springbatchpartitioning.domain

import java.math.BigDecimal

data class CountryRawData(
	val countryName: String,
	val yearlyData: List<BigDecimal>,
)