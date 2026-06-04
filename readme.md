# Laboratorio Android. Cliente de GitHub

## Datos del estudiante
- Dennys Vela 
- PUCE - Desarrollo de software

## Descripción del Proyecto
Este proyecto es una serie de laboratorios para estudiantes donde se implementará una aplicación Android que funciona como cliente de GitHub utilizando Jetpack Compose. La aplicación evoluciona progresivamente desde una UI estática hasta una integración completa con la API REST de GitHub.

---

## Laboratorio 1 — Lista de Repositorios (UI Estática)

**Objetivo:** Implementar una lista de repositorios con datos estáticos usando Jetpack Compose.

### Componentes principales
- **`RepoItem`**: Composable reutilizable que muestra el nombre de un repositorio.
- **`RepoList`**: Composable que renderiza la lista de `RepoItem` y es el que se despliega desde `MainActivity`.

### Características
- Los datos de los repositorios son estáticos (hardcoded).
- Se utiliza `Column` para mostrar la lista de forma eficiente.
- No hay conexión con ninguna API externa.

---

## Laboratorio 2 — Conexión con la API de GitHub (GET)

**Objetivo:** Conectar la interfaz con la API REST de GitHub para obtener repositorios reales.

### Características
- Integración de **Retrofit** como cliente HTTP.
- Llamada a la API de GitHub para obtener la lista de repositorios de un usuario.
- Los datos obtenidos reemplazan la lista estática del Laboratorio 1.
- Manejo básico de estados de carga y error.

---

## Laboratorio 3 — Formulario y Creación de Repositorio (POST)

**Objetivo:** Implementar un formulario para crear un nuevo repositorio en GitHub mediante la API.

### Características
- Diseño de un formulario con Composables (`TextField` / `OutlinedTextField`).
- Campos incluidos:
  - Nombre del repositorio
  - Descripción del repositorio
- Llamada **POST** a la API de GitHub para crear el repositorio.
- Manejo de la respuesta y retroalimentación al usuario.

---

## Laboratorio 4 — Actualización y Eliminación de Repositorios (PATCH / DELETE)

**Objetivo:** Implementar las operaciones de actualización y eliminación de repositorios a través de la API.

### Características
- Llamada **PATCH** para actualizar el nombre o descripción de un repositorio existente.
- Llamada **DELETE** para eliminar un repositorio.
- Confirmación antes de eliminar.
- Actualización reactiva de la lista tras cada operación.

---

## Tecnologías Utilizadas
- Kotlin
- Android SDK
- Jetpack Compose
- Material Design 3 (Material You)
- Retrofit (Laboratorios 2, 3 y 4)
- API REST de GitHub (Laboratorios 2, 3 y 4)
- Navigation Compose (opcional)


# Datos del docente
Pablo Pérez Martínez
[✉️](mailto:paperez@puce.edu.ec)