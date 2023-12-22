package com.example.ppaps.data.response

import com.google.gson.annotations.SerializedName

data class ConfirmationResponse(

	@field:SerializedName("confirmation_status")
	val confirmationStatus: Int? = null,

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("status")
	val status: Confirm? = null
)

data class Confirm(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
