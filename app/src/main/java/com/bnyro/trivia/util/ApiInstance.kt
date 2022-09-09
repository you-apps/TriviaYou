package com.bnyro.trivia.util

import com.bnyro.trivia.api.opentriviadb.OpenTriviaDBHelper
import com.bnyro.trivia.api.thetriviaapi.TheTriviaApiHelper
import com.bnyro.trivia.obj.ApiType

object ApiInstance {
    private val apiPref = PreferenceHelper.getApi()
    var apiHelper = when (apiPref) {
        ApiType.theTriviaApi -> TheTriviaApiHelper()
        ApiType.openTriviaApi -> OpenTriviaDBHelper()
        else -> TheTriviaApiHelper()
    }
}
