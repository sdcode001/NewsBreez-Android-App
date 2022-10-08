package eu.deysouvik.newsbreeze

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class News(
    val totalResults:Int,
    val articles:ArrayList<ArticleData>
):Serializable

data class ArticleData(
    var id:Int,
    val author:String,
    val title:String,
    val description:String,
    val urlToImage:String,
    val publishedAt:String,
    val content:String,
    var isSaved:Int=0
):Serializable

