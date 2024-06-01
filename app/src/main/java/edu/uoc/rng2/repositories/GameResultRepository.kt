package edu.uoc.rng2.repositories

import android.util.Log
import edu.uoc.rng2.Constants
import edu.uoc.rng2.db.dao.GameResultDao
import edu.uoc.rng2.models.GameResult
import edu.uoc.rng2.repositories.datasources.GamesResultServiceDataSource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.rx3.rxObservable
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.SerializationException
import org.json.JSONException
import javax.inject.Inject

private const val TAG = "GameResultRepository"

private val gameSyncMutex = Mutex()

// Repositorio para gestionar los resultados del juego.
class GameResultRepository @Inject constructor(
    private val gameResultDao: GameResultDao,
    private val gamesResultServiceDataSource: GamesResultServiceDataSource,
) {
    // Obtiene un GameResult por su ID.
    // @param id ID del GameResult a obtener.
    // @return Un objeto Single que emite el GameResult correspondiente.
    fun getGameResultById(id: Long) = gameResultDao
        .getGameResult(id)
        .subscribeOn(Schedulers.io())

    // Obtiene un flujo de todos los resultados del juego.
    // @return Un objeto Flowable que emite una lista de GameResult.
    val history = gameResultDao
        .loadResults()
        .subscribeOn(Schedulers.io())

    suspend fun syncGameResults() {
        gameSyncMutex.lock()
        try {
            val pendingGames = gameResultDao.pendingToSyncGames()

            Log.i(TAG, "pending games to be synced: ${pendingGames.count()}")

            for (game in pendingGames) {
                gamesResultServiceDataSource.postResult(game)
                gameResultDao.markGameResultAsSynced(game.id)
            }
        } finally {
            gameSyncMutex.unlock()
        }
    }

    fun bestGameResult() =
        gamesResultServiceDataSource.getBestGameResult().map { it.values.first() }

    fun getBestGames(): Observable<List<GameResult>> {
        return gamesResultServiceDataSource
            .getTopGameResults()
            .map {
                it.values
                    .toList()
                    .sortedByDescending { game -> game.turns }
            }
    }

    fun currentBalance(): Flowable<Int> {
        return gameResultDao
            .calculatedProfit()
            .map { it + Constants.INITIAL_BALANCE }
            .subscribeOn(Schedulers.io())
    }

    // Inserta un nuevo resultado del juego en la base de datos y devuelve su ID.
    // @param gameResult El resultado del juego a insertar en la base de datos.
    // @return El ID del nuevo GameResult insertado.
    fun insertGameResult(gameResult: GameResult): Maybe<Long> {
        return gameResultDao.insert(gameResult)
    }
}