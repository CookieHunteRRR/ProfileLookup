package com.cookiehunterrr.profilelookup.database

data class User(
    val id: String,
    val name: String,
    val realName: String,
    val locCountryCode: String,
    val profileUrl: String,
    val avatarLink: String,
    val avatarHash: String
)
