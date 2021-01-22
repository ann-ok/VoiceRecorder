package com.example.vaytsu_voicerecorder.player

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class PlayerViewModel(itemPath: String, application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player

    private var contentPosition = 0L
    private var playbackPosition = 0L;
    private var playWhenReady = true

    var itemPath: String? = itemPath

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        initializePlayer()
        Log.i("Player", "start")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        initializePlayer()
        Log.i("Player", "resume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        releasePlayer()
        Log.i("Player", "pause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        releasePlayer()
        Log.i("Player", "stop")
    }

    private fun initializePlayer() {
        val player = SimpleExoPlayer.Builder(getApplication()).build()
        val mMediaItem = MediaItem.fromUri(Uri.parse(itemPath))

        player.setMediaItem(mMediaItem);

        player.playWhenReady = playWhenReady
        player.seekTo(contentPosition)

        player.prepare()

        this._player.value = player
    }

    private fun releasePlayer() {
        val player = _player.value ?: return

        playbackPosition = player.currentPosition
        contentPosition = player.contentPosition
        playWhenReady = player.playWhenReady

        player.release()
        this._player.value = null
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}
