package com.example.domain.model

data class UserName(
    val firstName: String,
    val lastName: String
) {

    override fun toString(): String {
        return "${firstName.replaceFirstChar { it.uppercase() }} ${lastName.replaceFirstChar { it.uppercase() }}"
    }
}