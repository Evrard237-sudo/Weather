package androidkotlin.training.openweathermap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "ddcc931db7df01befd9cd0b2d85a51a9"

interface OpenWeatherService {

    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") cityName: String,
                   @Query("appid") apiKey: String = API_KEY,
                   @Query("lang") lang: String = "fr",
                   @Query("units") units: String = "metric" ) : Call<WeatherWrapper>
}