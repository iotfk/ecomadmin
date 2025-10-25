package com.tfkiot.myshopadmin.model

data class UserModel (
    val name : String = "",
    val email : String = "",
    val userId : String = "",
    val address : String = "",
    val cartItems : Map<String, Long> = emptyMap(),
    val favoriteItems: List<String> = emptyList()
)
