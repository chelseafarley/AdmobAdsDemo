package com.tripwiretech.admobadsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : AppCompatActivity() {
    private var _interstitialAd: InterstitialAd? = null
    private var _bannerAd: AdView? = null
    private var _rewardAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)
        loadInterstitial()
        loadRewardAd()
        loadBannerAd()
    }

    private fun loadRewardAd() {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                _rewardAd = null
            }

            override fun onAdLoaded(p0: RewardedAd) {
                _rewardAd = p0
            }
        })
    }

    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                _interstitialAd = null;
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                _interstitialAd = interstitialAd
            }
        })
    }

    private fun loadBannerAd() {
        _bannerAd = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        _bannerAd?.loadAd(adRequest)
    }

    fun showInterstitial(view: View) {
        _interstitialAd?.show(this)
    }

    fun showReward(view: View) {
        _rewardAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                _rewardAd = null
                loadRewardAd()
            }
        }

        _rewardAd?.show(this, OnUserEarnedRewardListener { rewardItem ->
            var rewardAmount = rewardItem.amount
            var rewardType = rewardItem.type
            println("$rewardAmount $rewardType Awarded")
        })
    }
}