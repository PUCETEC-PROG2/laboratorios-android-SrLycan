package ec.edu.puce.githubclient.models

data class Repository(
    val id: String,
    val name: String,
    val description: String?,
    val language: String?,
    val owner: GithubUser
)