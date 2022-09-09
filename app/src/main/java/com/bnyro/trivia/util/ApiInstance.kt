package com.bnyro.trivia.util

import com.bnyro.trivia.api.opentriviadb.OpenTriviaDBHelper
import com.bnyro.trivia.api.thetriviaapi.TheTriviaApiHelper
import com.bnyro.trivia.obj.ApiType

object ApiInstance {
    var apiHelper = when (
        PreferenceHelper.getApi()
    ) {
        ApiType.theTriviaApi -> TheTriviaApiHelper()
        ApiType.openTriviaApi -> OpenTriviaDBHelper()
        else -> TheTriviaApiHelper()
    }

    fun updateApiHelper(apiPref: Int) {
        apiHelper = when (apiPref) {
            ApiType.theTriviaApi -> TheTriviaApiHelper()
            ApiType.openTriviaApi -> OpenTriviaDBHelper()
            else -> TheTriviaApiHelper()
        }
    }
}
