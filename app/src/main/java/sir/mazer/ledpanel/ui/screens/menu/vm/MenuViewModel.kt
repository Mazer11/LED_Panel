package sir.mazer.ledpanel.ui.screens.menu.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import sir.mazer.feture.datastore.DataStoreAccessObject
import sir.mazer.ledpanel.ui.screens.menu.states.MenuScreenState
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val dataStore: DataStoreAccessObject
) : ViewModel() {

    private val _menuScreenState = MutableStateFlow<MenuScreenState>(MenuScreenState.Loading)
    val menuScreenState: StateFlow<MenuScreenState> = _menuScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeMenu()
        }
    }

    //------------------------Data-processing----------------------------
    private suspend fun getLocale(): String = dataStore.getLanguage.first()

    private suspend fun checkPremium(): Boolean = dataStore.getPremium.first()

    private suspend fun setPremiumStatus(isPremium: Boolean) = dataStore.setPremium(isPremium)

    private suspend fun changeLanguage(newLocale: String) = dataStore.setLanguage(newLocale)

    private suspend fun initializeMenu() {
        try {
            val locale = getLocale()
            val isPremium = checkPremium()
            _menuScreenState.value =
                MenuScreenState.Success(isPremium = isPremium, currentLanguage = locale)
        } catch (e: Exception) {
            _menuScreenState.value =
                MenuScreenState.Error("Error while loading data. Try again later")
            Log.e("MenuViewModel", "Error: ${e.localizedMessage}")
        }
    }

    //----------------------Click-events---------------------------------
    fun onNewLanguage(newLocale: String) {
        viewModelScope.launch(Dispatchers.IO) {
            changeLanguage(newLocale)
            if (_menuScreenState.value is MenuScreenState.Success)
                _menuScreenState.value =
                    (_menuScreenState.value as MenuScreenState.Success).copy(currentLanguage = newLocale)
        }
    }

    fun onSetPremium(newValue: Boolean) {
        viewModelScope.launch {
            setPremiumStatus(newValue)
        }
    }

}