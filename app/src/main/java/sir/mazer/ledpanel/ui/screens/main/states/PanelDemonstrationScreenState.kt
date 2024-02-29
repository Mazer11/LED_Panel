package sir.mazer.ledpanel.ui.screens.main.states

import sir.mazer.core.room.models.PanelData

interface PanelDemonstrationScreenState {
    data object Loading : PanelDemonstrationScreenState
    data class Error(val message: String) : PanelDemonstrationScreenState

    data class Success(val panel: PanelData) : PanelDemonstrationScreenState
}