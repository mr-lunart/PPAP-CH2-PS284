package com.example.ppaps.data.response

import com.google.gson.annotations.SerializedName

data class EmergencyVerifResponse(

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("user")
	val user: String? = null,

	@field:SerializedName("status")
	val status: Result? = null
)

data class Result(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
