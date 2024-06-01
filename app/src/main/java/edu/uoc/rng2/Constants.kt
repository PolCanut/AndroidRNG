package edu.uoc.rng2

object Constants {
    const val UPLOAD_GAME_RESULT = "UPLOAD_GAME_RESULT"
    const val INITIAL_BALANCE = 50
    const val GAME_PROFIT = 10
}

object NavConstants {
    const val LOGIN_SCREEN = "login"
    const val BEST_GAMES = "bestGames"
    const val WELCOME_SCREEN = "welcome"
    const val GAME_SCREEN = "game"
    const val GAME_RESULT_SCREEN_ARG_GAME_ID = "gameResultId"
    const val GAME_RESULT_SCREEN = "game_result/{$GAME_RESULT_SCREEN_ARG_GAME_ID}"
    const val HISTORY_SCREEN = "history"
    const val HELP_SCREEN = "help"
}

object Notifications {
    const val CHANNEL_GAME_RESULT = "gameResult"
    const val ID_GAME_VICTORY = 10
}