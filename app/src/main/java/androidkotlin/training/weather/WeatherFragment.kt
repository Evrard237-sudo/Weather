package androidkotlin.training.weather

import android.annotation.SuppressLint
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidkotlin.training.App
import androidkotlin.training.R
import androidkotlin.training.openweathermap.WeatherWrapper
import androidkotlin.training.openweathermap.mapOpenWeatherDataWeather
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class WeatherFragment : Fragment() {

    companion object {
        const val EXTRA_CITY_NAME = "androidkotlin.training.extras.EXTRA_CITY_NAME"

        fun newInstance() = WeatherFragment()
    }

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var cityName: String
    private val TAG = WeatherFragment::class.java.simpleName
    private lateinit var city: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherDescription: TextView
    private lateinit var temperature: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        refreshLayout = view.findViewById(R.id.swipe_refresh)
        city = view.findViewById(R.id.city)
        weatherIcon = view.findViewById(R.id.weather_icon)
        weatherDescription = view.findViewById(R.id.weather_description)
        temperature = view.findViewById(R.id.temperature)
        humidity = view.findViewById(R.id.humidity)
        pressure = view.findViewById(R.id.pressure)

        refreshLayout.setOnRefreshListener { refreshWheather() }

        return view
    }

    private fun refreshWheather() {
        updateWeatherForCity(cityName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity?.intent!!.hasExtra(EXTRA_CITY_NAME)) {
            activity?.intent!!.getStringExtra(EXTRA_CITY_NAME)?.let { updateWeatherForCity(it) }
        }
    }

    private fun updateWeatherForCity(cityName: String) {
        this.cityName = cityName

        if (!refreshLayout.isRefreshing){
            refreshLayout.isRefreshing = true
        }

        val call = App.weatherService.getWeather("$cityName")
        call.enqueue(object : retrofit2.Callback<WeatherWrapper> {
            override fun onResponse(
                call: Call<WeatherWrapper>,
                response: Response<WeatherWrapper>
            ) {
                response?.body()?.let {
                    val weather = mapOpenWeatherDataWeather(it)
                    updateUi(weather)
                    Log.i(TAG, "OpenWeatherMap response : $weather")
                    refreshLayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<WeatherWrapper>, t: Throwable) {
                Log.e(TAG, "Could not load weather", t)
                Toast.makeText(activity,
                        getString(R.string.weather_message_could_not_load_weather),
                        Toast.LENGTH_SHORT).show()
                refreshLayout.isRefreshing = false
            }

        })
    }

    private fun updateUi(weather: Weather) {

        Picasso.get()
            .load(weather.iconUrl)
            .placeholder(R.drawable.ic_cloud_off_black)
            .into(weatherIcon)

        city.text = cityName
        weatherDescription.text = weather.description
        temperature.text = getString(R.string.weather_temperature_value, weather.temperature.toInt())
        humidity.text = getString(R.string.weather_humidity_value, weather.humidity)
        pressure.text = getString(R.string.weather_pression_value, weather.pressure)
    }
}