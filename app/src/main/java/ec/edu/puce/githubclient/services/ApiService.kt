package ec.edu.puce.githubclient.services

import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.models.RepositoryPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("user/repos")
    suspend fun getRepositories(
        @Query(value = "sort") created: String = "Created",
        @Query(value = "direction") direction: String = "desc",
        @Query(value = "affiliation") affiliation: String = "owner",
        @Query(value = "per_page") perPage: Int = 100,
        @Query(value = "t") t: String = "${System.currentTimeMillis()}",
    ): List<Repository>

    @POST(value = "/user/repos")
    suspend fun createRepository(
        @Body repository: RepositoryPayload
    ): Repository

    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Unit>

    @PATCH("repos/{owner}/{repo}")
    suspend fun updateRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body repository: RepositoryPayload
    ): Repository
}