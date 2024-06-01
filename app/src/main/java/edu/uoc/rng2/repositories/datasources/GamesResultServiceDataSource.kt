package edu.uoc.rng2.repositories.datasources

import edu.uoc.rng2.models.GameResult
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface GamesResultServiceDataSource {
    @GET("games/results.json")
    suspend fun getResults(): Map<String, GameResult>

    @POST("games/results.json")
    suspend fun postResult(@Body gameResult: GameResult)

    @GET("games/results.json?orderBy=\"userWon\"&equalTo=true&orderBy=\"turns\"&limitToFirst=1")
    fun getBestGameResult(): Maybe<Map<String, GameResult>>

    @GET("""games/results.json?orderBy="userWon"&equalTo=true&orderBy="turns"&limitToFirst=10""")
    fun getTopGameResults(): Observable<Map<String, GameResult>>
}