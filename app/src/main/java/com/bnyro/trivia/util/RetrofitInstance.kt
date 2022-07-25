package com.bnyro.trivia.util

import com.bnyro.trivia.OPEN_TRIVIA_URL
import com.bnyro.trivia.TRIVIA_API_URL
import com.bnyro.trivia.api.opentriviadb.OpenTriviaDB
import com.bnyro.trivia.api.thetriviaapi.TheTriviaApi
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {

    val theTriviaApi: TheTriviaApi = Retrofit.Builder()
        .baseUrl(TRIVIA_API_URL)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(TheTriviaApi::class.java)

    val openTriviaApi: OpenTriviaDB = Retrofit.Builder()
        .baseUrl(OPEN_TRIVIA_URL)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(OpenTriviaDB::class.java)
}
