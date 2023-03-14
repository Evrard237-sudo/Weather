package androidkotlin.training

import android.app.Application
import androidkotlin.training.openweathermap.OpenWeatherService
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        private lateinit var instance: App

        val database: Database by lazy {
            Database(instance)
        }

        private val httpClient = okhttp3.OkHttpClient.Builder()
            .addInterceptor(okhttp3.logging.HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        private val retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService: OpenWeatherService = retrofit.create(OpenWeatherService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}