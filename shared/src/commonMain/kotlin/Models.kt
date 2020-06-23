package com.sample

data class Profile(val id: Int, val name: String, val isSaving: Boolean)
data class AppState(val profile: Profile)