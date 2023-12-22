package com.example.ppaps.data.response

import com.google.gson.annotations.SerializedName

data class VerificationResponse(

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("status")
	val status: Status? = null
)

data class Status(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
