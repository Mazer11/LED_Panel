package sir.mazer.ledpanel.ui.screens.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sir.mazer.core.room.models.PanelData
import sir.mazer.feture.room.PanelRepository
import sir.mazer.ledpanel.ui.screens.main.states.PanelDemonstrationScreenState
import sir.mazer.ledpanel.ui.screens.main.states.PanelEditorScreenState
import sir.mazer.ledpanel.ui.screens.main.states.SavedPanelsScreenState
import sir.mazer.ledpanel.ui.theme.LEDColors
import sir.mazer.ledpanel.ui.theme.LEDFonts
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val panelRepository: PanelRepository
) : ViewModel() {

    private val _savedPanelsScreenState =
        MutableStateFlow<SavedPanelsScreenState>(SavedPanelsScreenState.Loading)
    val savedPanelsScreenState: StateFlow<SavedPanelsScreenState> =
        _savedPanelsScreenState.asStateFlow()

    private val _panelEditorScreenState =
        MutableStateFlow<PanelEditorScreenState>(PanelEditorScreenState.Loading)
    val panelEditorScreenState: StateFlow<PanelEditorScreenState> =
        _panelEditorScreenState.asStateFlow()

    private val _panelDemonstrationScreenState =
        MutableStateFlow<PanelDemonstrationScreenState>(PanelDemonstrationScreenState.Loading)
    val panelDemonstrationScreenState: StateFlow<PanelDemonstrationScreenState> =
        _panelDemonstrationScreenState.asStateFlow()

    val styles = LEDFonts.entries.toList()
    val backgrounds = LEDColors.entries.toList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _savedPanelsScreenState.value = SavedPanelsScreenState.Success(loadSavedPanels())
            } catch (e: Exception) {
                _savedPanelsScreenState.value =
                    SavedPanelsScreenState.Error("Error while loading saved panels. Try again later")
            }
        }
    }

    //------------------------Data-processing----------------------------
    private suspend fun loadSavedPanels(): List<PanelData> = panelRepository.getAllPanels()

    private suspend fun deleteSavedPanel(panel: PanelData) {
        panelRepository.deletePanel(panel)
    }

    private suspend fun addOrEditPanel(panel: PanelData) {
        panelRepository.insertPanel(panel)
    }

    //----------------------Click-events---------------------------------
    fun onPanelClick(panel: PanelData) {
        _panelDemonstrationScreenState.value = PanelDemonstrationScreenState.Success(panel)
    }

    fun onDeletePanel(panel: PanelData) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteSavedPanel(panel)
                _savedPanelsScreenState.value = SavedPanelsScreenState.Success(loadSavedPanels())
            } catch (e: Exception) {
                SavedPanelsScreenState.Error("Error while delete panel. Try again later")
            }
        }
    }

    fun onOpenEditPanel(panel: PanelData) {
        _panelEditorScreenState.value = PanelEditorScreenState.Success(panel)
    }

    fun onOpenNewPanelEditor() {
        _panelEditorScreenState.value = PanelEditorScreenState.Success(null)
    }

    fun onEditOrCreatePanel(panel: PanelData) {
        viewModelScope.launch {
            addOrEditPanel(panel)
            _savedPanelsScreenState.value = SavedPanelsScreenState.Loading
            try {
                _savedPanelsScreenState.value = SavedPanelsScreenState.Success(loadSavedPanels())
            } catch (e: Exception) {
                _savedPanelsScreenState.value =
                    SavedPanelsScreenState.Error("Error while loading saved panels. Try again later")
            }
        }
    }

}