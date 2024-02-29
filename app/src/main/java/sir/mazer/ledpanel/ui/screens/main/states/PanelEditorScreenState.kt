package sir.mazer.ledpanel.ui.screens.main.states

import sir.mazer.core.room.models.PanelData

sealed interface PanelEditorScreenState {
    data object Loading: PanelEditorScreenState
    data class Error(val message: String): PanelEditorScreenState
    data class Success(val panel: PanelData?): PanelEditorScreenState
}