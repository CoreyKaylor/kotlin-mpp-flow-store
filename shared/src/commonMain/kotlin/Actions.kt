package com.sample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

sealed class ProfileAction : Action {
    internal class SavingProfile : ProfileAction()
    internal class ProfileCreated(val name: String) : ProfileAction()
    internal class SavingProfileError(val ex: Exception) : ProfileAction()

    class Create(val name: String) : FlowAction {
        override fun toFlow() = flow {
            try {
                emit(SavingProfile())
                delay(3000)
                emit(ProfileCreated(name))
            } catch(ex: Exception) {
                emit(SavingProfileError(ex))
            }
        }
    }
}