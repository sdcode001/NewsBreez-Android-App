package eu.deysouvik.newsbreeze

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY="4a60ea57c5ed4034b98435beffc62ce0"
const val BASE_URL="https://newsapi.org/"

interface NewsApiInterface {
   @GET("v2/top-headlines?apiKey=$API_KEY")
   fun getDataByCountry(@Query("country") country:String): Call<News>
}

object NewsService{
    val interfaceApi:NewsApiInterface

    init {
        val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        interfaceApi=retrofit.create(NewsApiInterface::class.java)
    }
}