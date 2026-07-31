package cu.youchat.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cu.youchat.app.data.preferences.UserPreferences
import cu.youchat.app.data.repository.ChatRepository
import cu.youchat.app.domain.model.ChatPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PerfilInfo(
    val alias: String = "",
    val correo: String = "",
    val rutaImgPerfil: String = "",
    val cantSeguidores: Int = 0
)

@HiltViewModel
class PrincipalViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _chats = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chats: StateFlow<List<ChatPreview>> = _chats

    private val _perfil = MutableStateFlow<PerfilInfo?>(null)
    val perfil: StateFlow<PerfilInfo?> = _perfil

    init {
        viewModelScope.launch {
            preferences.correo.collect { correo ->
                _perfil.update { it?.copy(correo = correo) ?: PerfilInfo(correo = correo) }
            }
        }
        viewModelScope.launch {
            preferences.alias.collect { alias ->
                _perfil.update { it?.copy(alias = alias) ?: PerfilInfo(alias = alias) }
            }
        }
        cargarChats()
    }

    private fun cargarChats() {
        viewModelScope.launch {
            chatRepository.getChatsPreview().collect { lista ->
                _chats.value = lista
            }
        }
    }
}
