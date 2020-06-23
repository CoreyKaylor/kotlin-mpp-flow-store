package com.sample

fun appReducer(state: AppState, action: Action): AppState {
    return when(action) {
        is ProfileAction -> state.copy(profile = profileReducer(state.profile, action))
        else -> {
            return state
        }
    }
}

private fun profileReducer(profile: Profile, action: ProfileAction): Profile {
    return when (action) {
        is ProfileAction.ProfileCreated -> profile.copy(id = profile.id + 1,
            name = action.name, isSaving = false)
        is ProfileAction.SavingProfile -> profile.copy(isSaving = true)
        is ProfileAction.SavingProfileError -> {
            println(action.ex)
            return profile.copy(isSaving = false)
        }
    }
}