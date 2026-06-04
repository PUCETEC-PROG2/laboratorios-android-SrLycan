package ec.edu.puce.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel : ViewModel() {

    private val _repos = MutableStateFlow<List<Repository>>(emptyList())
    val repos: StateFlow<List<Repository>> = _repos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchRepos()
    }

    fun fetchRepos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _repos.value = RetrofitClient.apiService.getRepositories()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al cargar repositorios"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // NUEVO: Borrar repositorio por su dueño y nombre
    fun deleteRepo(owner: String, repoName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                RetrofitClient.apiService.deleteRepository(owner, repoName)
                // Después de borrar, recargamos la lista para que desaparezca
                fetchRepos()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al borrar el repositorio"
                _isLoading.value = false
            }
        }
    }
}
