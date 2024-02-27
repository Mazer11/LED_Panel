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
import sir.mazer.ledpanel.ui.screens.main.states.SavedPanelsScreenState
import sir.mazer.ledpanel.ui.theme.LEDBackgrounds
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

    val styles = LEDFonts.entries.toList()
    val backgrounds = LEDBackgrounds.entries.toList()

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
        //Fill panel screen state
    }

    fun onDeletePanel(panel: PanelData) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteSavedPanel(panel)
            } catch (e: Exception){
                SavedPanelsScreenState.Error("Error while delete panel. Try again later")
            }
        }
    }

    fun onOpenEditPanel(panel: PanelData) {
        //Fill setup panel screen state
    }

}