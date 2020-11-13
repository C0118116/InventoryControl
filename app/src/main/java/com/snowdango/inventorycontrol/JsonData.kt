package com.snowdango.inventorycontrol

data class CreateData(
    val code:String,
    val name:String,
    val num:Int,
    val limit:Int
)

data class Increase(
    val code:String,
    val num:Int
)

data class JsonData(
    val id:String?,
    val name:String?,
    val num:Int?,
    val limit:Int?,
    val code: String?
)
