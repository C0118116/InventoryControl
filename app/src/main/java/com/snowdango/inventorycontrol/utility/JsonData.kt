package com.snowdango.inventorycontrol.utility

data class CreateData(
    val code:String,
    val name:String,
    val num:Int,
    val limit:Int
)

data class ChangeData(
    val code:String,
    val num:Int
)

data class JsonData(
    val id:String?,
    val name:String?,
    var num:Int?,
    val limit:Int?,
    val code: String?
)

data class ReturnData(
    val inventory: List<JsonData>,
    val order: List<JsonData>
)

data class ReturnPlace(
    val num: Int?,
    val new_place: Int?
)
