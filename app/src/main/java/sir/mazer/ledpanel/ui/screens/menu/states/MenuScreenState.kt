package sir.mazer.ledpanel.ui.screens.menu.states

sealed interface MenuScreenState {
    data class Success(
        val isPremium: Boolean,
        val currentLanguage: String
    ) : MenuScreenState

    data class Error(val message: String) : MenuScreenState

    data object Loading : MenuScreenState
}