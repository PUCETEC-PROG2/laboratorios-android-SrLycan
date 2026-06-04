package ec.edu.puce.githubclient.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.ui.components.RepoItem
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme
import ec.edu.puce.githubclient.viewmodels.RepoListViewModel
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    viewModel: RepoListViewModel = viewModel(),
    onNavigatetoForm: () -> Unit = {},
    onNavigateToEdit: (Repository) -> Unit = {}  // NUEVO: para navegar al formulario de edición
) {
    val repos by viewModel.repos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // NUEVO: guardamos el repositorio que el usuario quiere borrar (o null si no hay ninguno)
    var repoToDelete by remember { mutableStateOf<Repository?>(null) }

    // NUEVO: Diálogo de confirmación de borrado
    // Solo se muestra cuando repoToDelete tiene un valor (no es null)
    repoToDelete?.let { repo ->
        AlertDialog(
            onDismissRequest = { repoToDelete = null }, // Si toca fuera, cancela
            title = { Text(text = "Borrar repositorio") },
            text = { Text(text = "¿Estás seguro que deseas borrar \"${repo.name}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteRepo(repo.owner.login, repo.name)
                    repoToDelete = null // Cerramos el diálogo
                }) {
                    Text(text = "Sí, borrar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { repoToDelete = null }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigatetoForm() },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (!isLoading && error == null) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = repos,
                        // La key le dice a Compose qué elemento es cuál cuando la lista cambia
                        key = { repo -> repo.id }
                    ) { repo ->

                        // NUEVO: SwipeToDismissBox envuelve cada item para detectar el deslizamiento
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { direction ->
                                when (direction) {
                                    // Deslizar a la izquierda = borrar
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        repoToDelete = repo // Guardamos el repo y mostramos el diálogo
                                        true
                                    }
                                    // Deslizar a la derecha = editar
                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        onNavigateToEdit(repo) // Navegamos al formulario de edición
                                        true
                                    }
                                    else -> false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            // backgroundContent es lo que se ve DETRÁS del item mientras lo deslizas
                            backgroundContent = {
                                val direction = dismissState.dismissDirection
                                // Color rojo para borrar, verde para editar
                                val color = when (direction) {
                                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFE53935)   // rojo
                                    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF43A047)   // verde
                                    else -> Color.Transparent
                                }
                                val icon = when (direction) {
                                    SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                                    SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                                    else -> Icons.Default.Delete
                                }
                                val alignment = when (direction) {
                                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                                    else -> Alignment.CenterStart
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 24.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        ) {
                            RepoItem(
                                name = repo.name,
                                description = repo.description ?: "Sin descripción",
                                avatarUrl = repo.owner.avatarUrl,
                                language = repo.language ?: "Desconocido"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoListPreview() {
    GithubClientTheme {
        RepoList()
    }
}
