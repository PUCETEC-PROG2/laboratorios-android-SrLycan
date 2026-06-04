package ec.edu.puce.githubclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme
import ec.edu.puce.githubclient.viewmodels.RepoFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoForm(
    onBackClick: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    viewModel: RepoFormViewModel = viewModel(),
    // NUEVO: Parámetros opcionales para modo edición.
    // Si vienen con valor, estamos editando; si son null, estamos creando.
    repoOwner: String? = null,
    repoNameToEdit: String? = null,
    repoDescriptionToEdit: String? = null
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    // Determinamos si estamos en modo edición
    val isEditMode = repoNameToEdit != null

    // Si estamos editando, los campos empiezan con los datos actuales del repo
    var name by remember { mutableStateOf(repoNameToEdit ?: "") }
    var description by remember { mutableStateOf(repoDescriptionToEdit ?: "") }

    LaunchedEffect(key1 = isSuccess) {
        if (isSuccess) {
            onSaveSuccess()
            viewModel.resetSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // El título cambia según si estamos creando o editando
                title = { Text(text = if (isEditMode) "Editar Repositorio" else "Crear Repositorio") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (!errorMsg.isNullOrBlank()) {
                Text(
                    text = errorMsg!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Nombre del repositorio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Descripción del Repositorio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // NUEVO: Si estamos en modo edición, llamamos a updateRepo; si no, a createRepo
                    if (isEditMode && repoOwner != null && repoNameToEdit != null) {
                        viewModel.updateRepo(repoOwner, repoNameToEdit, name, description)
                    } else {
                        viewModel.createRepo(name, description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Guardar"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Guardar")
            }
        }
    }
}

@Preview
@Composable
fun RepoFormPreview() {
    GithubClientTheme {
        RepoForm()
    }
}
