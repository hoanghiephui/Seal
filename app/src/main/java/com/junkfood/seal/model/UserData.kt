package com.junkfood.seal.model

/**
 * Class summarizing user interest data
 */
data class UserData(
    val downloadCount: Int,
    val makePro: Boolean,
    val lastDay: Long,
)

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}