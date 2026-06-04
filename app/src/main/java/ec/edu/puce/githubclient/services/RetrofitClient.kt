package ec.edu.puce.githubclient.services

import ec.edu.puce.githubclient.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL="https://api.github.com"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val token = BuildConfig.GITHUB_TOKEN
            // println("Token: $token") // ¡Borra esto en producción!

            val request = chain.request().newBuilder()
                .addHeader("Authorization", "token $token") // Cambiado a 'token'
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Pragma", "no-cache")
                .addHeader("Expires", "0") // Cambiado de "g" a "0"
                .addHeader("Accept", "application/vnd.github+json") // Recomendado por GitHub
                .build()

            chain.proceed(request)
        }
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}