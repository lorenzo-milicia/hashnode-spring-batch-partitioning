package dev.lorenzomilicia.springbatchpartitioning.domain

import java.math.BigDecimal

data class CountrySummedData(
	val countryName: String,
	val totalAmount: BigDecimal,
)
