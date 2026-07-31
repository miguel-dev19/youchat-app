package cu.youchat.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cu.youchat.app.data.preferences.UserPreferences
import cu.youchat.app.data.repository.MailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: UserPreferences,
    private val mailRepository: MailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } }
    fun clearError() { _uiState.update { it.copy(error = null) } }

    fun login() {
        val correo = _uiState.value.email.trim()
        val pass = _uiState.value.password.trim()
        when {
            correo.isEmpty() -> { _uiState.update { it.copy(error = "El correo no puede estar vacío") }; return }
            !correo.endsWith("@nauta.cu") -> { _uiState.update { it.copy(error = "Debe ser un correo Nauta (@nauta.cu)") }; return }
            pass.isEmpty() -> { _uiState.update { it.copy(error = "La contraseña no puede estar vacía") }; return }
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val exito = mailRepository.verificarCredenciales(correo, pass)
                if (exito) {
                    preferences.setCorreo(correo)
                    preferences.setPass(pass)
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Credenciales incorrectas") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error: ${e.message}") }
            }
        }
    }
}
