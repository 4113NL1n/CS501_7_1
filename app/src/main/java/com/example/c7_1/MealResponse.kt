package com.example.c7_1

import com.squareup.moshi.Json

data class MealResponse(
    @Json(name = "meals") val meals: List<MealItem>?
)

data class MealItem(
    @Json(name = "strMeal") val name: String,
    @Json(name = "strInstructions") val instructions: String,
    @Json(name = "strMealThumb") val image: String
)