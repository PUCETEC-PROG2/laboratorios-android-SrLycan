package ec.edu.puce.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.RepositoryPayload
import ec.edu.puce.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepoFormViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun createRepo(name: String, description: String) {
        if (name.isBlank()) {
            _error.value = "El nombre no puede estar vacío"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val payload = RepositoryPayload(
                    name = name,
                    description = if (description.isBlank()) null else description
                )
                RetrofitClient.apiService.createRepository(payload)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // NUEVO: Editar un repositorio existente. Necesita el dueño y el nombre actual del repo.
    fun updateRepo(owner: String, repoName: String, newName: String, newDescription: String) {
        if (newName.isBlank()) {
            _error.value = "El nombre no puede estar vacío"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val payload = RepositoryPayload(
                    name = newName,
                    description = if (newDescription.isBlank()) null else newDescription
                )
                RetrofitClient.apiService.updateRepository(owner, repoName, payload)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al actualizar el repositorio"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccess() {
        _isSuccess.value = false
    }

    fun resetError() {
        _error.value = null
    }
}
