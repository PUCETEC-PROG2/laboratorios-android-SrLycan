package ec.edu.puce.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.ui.screens.RepoForm
import ec.edu.puce.githubclient.ui.screens.RepoList
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme
import ec.edu.puce.githubclient.viewmodels.RepoFormViewModel
import ec.edu.puce.githubclient.viewmodels.RepoListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubClientTheme {
                var currentScreen by remember { mutableStateOf("repoList") }
                val listViewModel: RepoListViewModel = viewModel()
                val formViewModel: RepoFormViewModel = viewModel()

                // NUEVO: Variable para guardar el repo que se va a editar (null si estamos creando)
                var repoToEdit by remember { mutableStateOf<Repository?>(null) }

                when (currentScreen) {
                    "repoList" -> RepoList(
                        viewModel = listViewModel,
                        onNavigatetoForm = {
                            repoToEdit = null // Nos aseguramos de ir en modo "crear"
                            formViewModel.resetError()
                            currentScreen = "repoForm"
                        },
                        // NUEVO: Cuando el usuario desliza a la derecha, recibimos el repo
                        onNavigateToEdit = { repo ->
                            repoToEdit = repo // Guardamos el repo que se quiere editar
                            formViewModel.resetError()
                            currentScreen = "repoForm"
                        }
                    )
                    "repoForm" -> RepoForm(
                        viewModel = formViewModel,
                        onBackClick = {
                            formViewModel.resetError()
                            currentScreen = "repoList"
                        },
                        onSaveSuccess = {
                            listViewModel.fetchRepos()
                            formViewModel.resetError()
                            currentScreen = "repoList"
                        },
                        // NUEVO: Pasamos los datos del repo a editar (o null si es creación)
                        repoOwner = repoToEdit?.owner?.login,
                        repoNameToEdit = repoToEdit?.name,
                        repoDescriptionToEdit = repoToEdit?.description
                    )
                }
            }
        }
    }
}
