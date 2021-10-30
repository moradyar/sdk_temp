package com.moradyar.sdkarchitecturetemplate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.loopnow.fireworkdemo.R
import com.moradyar.authenticationfeature.core.Authenticator
import com.moradyar.carouselfeature.core.FeedRepository
import com.moradyar.carouselfeature.core.model.FeedResult
import com.moradyar.carouselfeature.impl.FeedRequestState
import com.moradyar.networkcore.core.HttpClient
import com.moradyar.utilitycore.core.UniqueIdProvider
import com.moradyar.utilitycore.core.UserAgentInfoHelper

class MainActivity : AppCompatActivity() {

    private val clientId by lazy {
        "f6d6ec1275217f178cdff91363825cb390e038c1168f64f6efa23cb95ec6b325"
    }

    // Network core dependencies
    private val httpClient by lazy {
        HttpClient.Factory.getInstance("https://api.fw.tv")
    }

    // Utility core dependencies
    private val userAgentInfoHelper by lazy {
        UserAgentInfoHelper.Factory.getInstance(this)
    }
    private val uniqueIdProvider by lazy {
        UniqueIdProvider.Factory.getInstance()
    }

    // Authentication feature dependencies
    private val authenticator by lazy {
        Authenticator.Factory.getInstance(
            httpClient,
            clientId,
            "123jdk",
            userAgentInfoHelper,
            uniqueIdProvider
        )
    }

    // Carousel feature dependencies
    private val feedRepository by lazy {
        FeedRepository.Factory.getInstance(httpClient, authenticator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnFetch).setOnClickListener {
            fetchData()
        }
    }

    private fun fetchData() {
        feedRepository.fetchDiscoveryFeed { state ->
            when (state) {
                is FeedRequestState.Error -> Log.i("XXXX", "${state.e.message}")
                FeedRequestState.Loading -> Log.i("XXXX", "Loading . . .")
                is FeedRequestState.Success -> {
                    when (val res = state.value) {
                        is FeedResult.Error -> Log.i("XXXX", res.error)
                        FeedResult.FeedExpired -> Log.i("XXXX", "$res")
                        FeedResult.FeedOver -> Log.i("XXXX", "$res")
                        is FeedResult.Videos -> Log.i("XXXX", "${res.videos.size}")
                    }
                }
            }
        }
    }
}
