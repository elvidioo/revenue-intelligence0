package com.kipas.football;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    // ID DE TEST : remplace par ton bloc d'annonces interstitiel AdMob avant de publier.
    private static final String INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    // Une pub plein écran toutes les 3 parties (pour ne pas agacer les joueurs).
    private static final int GAMES_PER_AD = 3;

    private WebView webView;
    private AdView banner;
    private InterstitialAd interstitial;
    private int gamesPlayed = 0;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, status -> { });
        banner = findViewById(R.id.banner);
        banner.loadAd(new AdRequest.Builder().build());
        loadInterstitial();

        webView = findViewById(R.id.webview);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);        // pour sauvegarder le record
        ws.setAllowFileAccess(false);
        webView.setBackgroundColor(0xFF1A1A17);
        webView.addJavascriptInterface(new Bridge(), "Android");
        webView.loadUrl("file:///android_asset/index.html");
    }

    private void loadInterstitial() {
        InterstitialAd.load(this, INTERSTITIAL_ID, new AdRequest.Builder().build(),
            new InterstitialAdLoadCallback() {
                @Override public void onAdLoaded(@NonNull InterstitialAd ad) {
                    interstitial = ad;
                    ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override public void onAdDismissedFullScreenContent() {
                            interstitial = null;
                            loadInterstitial();
                        }
                        @Override public void onAdFailedToShowFullScreenContent(@NonNull AdError e) {
                            interstitial = null;
                            loadInterstitial();
                        }
                    });
                }
                @Override public void onAdFailedToLoad(@NonNull LoadAdError e) {
                    interstitial = null;
                }
            });
    }

    /** Appelé par le jeu (JavaScript) à la fin de chaque partie. */
    private class Bridge {
        @JavascriptInterface
        public void onGameOver() {
            runOnUiThread(() -> {
                gamesPlayed++;
                if (gamesPlayed % GAMES_PER_AD == 0 && interstitial != null) {
                    interstitial.show(MainActivity.this);
                }
            });
        }
    }

    @Override protected void onPause()  { if (banner != null) banner.pause();  super.onPause(); }
    @Override protected void onResume() { super.onResume(); if (banner != null) banner.resume(); }
    @Override protected void onDestroy() {
        if (banner != null) banner.destroy();
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
