package sir.mazer.ledpanel.ui.screens.main.states

import sir.mazer.core.room.models.PanelData

sealed interface SavedPanelsScreenState {
    data object Loading: SavedPanelsScreenState
    data class Success(val panels: List<PanelData>): SavedPanelsScreenState
    data class Error(val message: String): SavedPanelsScreenState
}