package cu.youchat.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cu.youchat.app.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    private val _startDestination = MutableStateFlow("onboarding")
    val startDestination: StateFlow<String> = _startDestination

    init {
        viewModelScope.launch {
            preferences.mark.collect { mark ->
                _startDestination.value = when (mark) {
                    0 -> "onboarding"
                    1 -> "login"
                    2 -> "welcome_perfil"
                    else -> "principal"
                }
            }
        }
    }

    fun setOnboardingComplete() { viewModelScope.launch { preferences.setMark(1) } }
    fun setLoginComplete() { viewModelScope.launch { preferences.setMark(2) } }
    fun setWelcomeComplete() { viewModelScope.launch { preferences.setMark(3) } }
}
