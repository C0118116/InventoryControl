package com.snowdango.inventorycontrol

import kotlinx.serialization.*

@Serializable
data class CreateItem(
        val code : String?,
        val name : String?,
        val num : Int?
)

@Serializable
data class DataResponse(
        val text : String?

)
