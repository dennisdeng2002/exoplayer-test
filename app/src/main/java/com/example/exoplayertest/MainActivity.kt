package com.example.exoplayertest

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.exoplayertest.databinding.ActivityMainBinding
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
import com.google.android.exoplayer2.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter.EventListener
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PATH_PREFIX = "asset:///videos/"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exoPlayer = createExoPlayer()
        binding.playerView.player = exoPlayer

        val videoUrls = resources.assets.list("videos").orEmpty().map { file -> "$PATH_PREFIX$file" }

        for (videoUrl in videoUrls) {
            val radioButton = RadioButton(this)
            radioButton.text = videoUrl.removePrefix(PATH_PREFIX)
            binding.radioGroup.addView(radioButton)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val videoUrl = Uri.parse(PATH_PREFIX + radioButton.text.toString())
            exoPlayer.clearMediaItems()
            exoPlayer.setMediaItem(MediaItem.fromUri(videoUrl))
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    private fun createExoPlayer(): ExoPlayer {
        val bufferDurationSeconds = 10L
        val bufferDurationMilliseconds = TimeUnit.SECONDS.toMillis(bufferDurationSeconds).toInt()

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                bufferDurationMilliseconds, // min
                bufferDurationMilliseconds, // max
                DEFAULT_BUFFER_FOR_PLAYBACK_MS, // playback
                DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS // playback re-buffer
            )
            .build()

        val trackSelector = DefaultTrackSelector(this).apply {
            val parameters = buildUponParameters()
                .setExceedRendererCapabilitiesIfNecessary(true)
                .build()

            setParameters(parameters)
        }

        return ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .setTrackSelector(trackSelector)
            .build()
            .apply { repeatMode = REPEAT_MODE_ONE }
    }
}
