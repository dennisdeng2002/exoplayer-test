package com.example.exoplayertest

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
        private const val WERTHER = "https://videodelivery.net/6d5fc91ff47ef00c96bf8064d528f203/manifest/video.m3u8"
        private const val SKIPPY = "https://videodelivery.net/dfe8a45bb191b6530d9c726a2ecde27d/manifest/video.m3u8"
        private const val OSCAR_MEYER = "https://videodelivery.net/b7c1a9d1ef8ddce31e4e82bfbdf9307d/manifest/video.m3u8"
        private const val SARGENTO = "https://videodelivery.net/18f35249cde7aa09195521f6b8e20135/manifest/video.m3u8"
        private const val COLGATE = "https://videodelivery.net/f8a39cb54b523888882c6cce0a990059/manifest/video.m3u8"
        private const val PEPCID = "https://videodelivery.net/69658ce511848ef50bcd843e1e108c15/manifest/video.m3u8"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Text View 1
        binding.textView1.setOnClickListener {
            binding.playerView1.visibility = if (binding.playerView1.visibility == VISIBLE) INVISIBLE else VISIBLE
        }
        // Text View 2
        binding.textView2.setOnClickListener {
            binding.playerView2.visibility = if (binding.playerView2.visibility == VISIBLE) INVISIBLE else VISIBLE
        }
        // Player 480
        var bandwidth480 = 0L
        val exoPlayer480 = createExoPlayer(854, 480) { _, b, _ ->
            bandwidth480 += b
            binding.bytes480.text = "480p: ${bandwidth480.toDouble() / 1_000_000} MB Downloaded"
        }
        // Player 360
        var bandwidth360 = 0L
        val exoPlayer360 = createExoPlayer(640, 360) { _, b, _ ->
            bandwidth360 += b
            binding.bytes360.text = "360p: ${bandwidth360.toDouble() / 1_000_000} MB Downloaded"
        }
        binding.playerView2.player = exoPlayer360
        // Radio Group
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val url = when (checkedId) {
                R.id.werther -> {
                    binding.playerView1.player = exoPlayer480
                    binding.playerView2.player = exoPlayer360
                    binding.textView1.tag = "480p"
                    binding.textView2.tag = "360p"
                    WERTHER
                }
                R.id.skippy -> {
                    binding.playerView1.player = exoPlayer360
                    binding.playerView2.player = exoPlayer480
                    binding.textView1.tag = "360p"
                    binding.textView2.tag = "480p"
                    SKIPPY
                }
                R.id.oscar_meyer -> {
                    binding.playerView1.player = exoPlayer360
                    binding.playerView2.player = exoPlayer480
                    binding.textView1.tag = "360p"
                    binding.textView2.tag = "480p"
                    OSCAR_MEYER
                }
                R.id.sargento -> {
                    binding.playerView1.player = exoPlayer480
                    binding.playerView2.player = exoPlayer360
                    binding.textView1.tag = "480p"
                    binding.textView2.tag = "360p"
                    SARGENTO
                }
                R.id.colgate -> {
                    binding.playerView1.player = exoPlayer480
                    binding.playerView2.player = exoPlayer360
                    binding.textView1.tag = "480p"
                    binding.textView2.tag = "360p"
                    COLGATE
                }
                R.id.pepcid -> {
                    binding.playerView1.player = exoPlayer360
                    binding.playerView2.player = exoPlayer480
                    binding.textView1.tag = "360p"
                    binding.textView2.tag = "480p"
                    PEPCID
                }
                else -> throw IllegalStateException("Uh oh!")
            }

            val debugInfo = binding.debugInfo.isChecked
            binding.textView1.text = if (debugInfo) "Video 1 (${binding.textView1.tag})" else "Video 1"
            binding.textView2.text = if (debugInfo) "Video 2 (${binding.textView2.tag})" else "Video 2"

            for (exoPlayer in listOf(exoPlayer480, exoPlayer360)) {
                exoPlayer.clearMediaItems()
                exoPlayer.setMediaItem(MediaItem.fromUri(url))
                exoPlayer.prepare()
                exoPlayer.seekToDefaultPosition()
                exoPlayer.play()
            }

        }
        binding.radioGroup.check(R.id.werther)
        // Debug Info
        binding.debugInfo.setOnCheckedChangeListener { _, b ->
            val visibility = if (b) VISIBLE else INVISIBLE
            binding.textView1.text = if (b) "Video 1 (${binding.textView1.tag})" else "Video 1"
            binding.textView2.text = if (b) "Video 2 (${binding.textView2.tag})" else "Video 2"
            binding.bytes480.visibility = visibility
            binding.bytes360.visibility = visibility
        }
        binding.debugInfo.isChecked = false
    }

    private fun createExoPlayer(width: Int, height: Int, bandwidthListener: EventListener): ExoPlayer {
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

        val bandwidthMeter = DefaultBandwidthMeter.Builder(this).build()

        bandwidthMeter.addEventListener(Handler(Looper.getMainLooper()), bandwidthListener)

        val trackSelector = DefaultTrackSelector(this).apply {
            val parameters = buildUponParameters()
                .setMaxVideoSize(width, height)
                .build()

            setParameters(parameters)
        }

        return ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .setBandwidthMeter(bandwidthMeter)
            .setTrackSelector(trackSelector)
            .build()
            .apply { repeatMode = REPEAT_MODE_ONE }
    }
}
