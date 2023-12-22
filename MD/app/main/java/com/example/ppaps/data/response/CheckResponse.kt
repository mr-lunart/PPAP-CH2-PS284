package com.example.ppaps.data.response

import com.google.gson.annotations.SerializedName

data class CheckResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("account_status")
	val accountStatus: Int? = null,

	@field:SerializedName("status")
	val status: Check? = null
)

data class Check(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
