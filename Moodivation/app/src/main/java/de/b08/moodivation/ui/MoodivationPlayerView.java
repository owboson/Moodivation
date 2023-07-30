/*
 * MIT License
 *
 * Copyright (c) 2023 RUB-SE-LAB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.b08.moodivation.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.LoadControl;
import androidx.media3.exoplayer.upstream.DefaultAllocator;
import androidx.media3.ui.PlayerView;

import de.b08.moodivation.R;

public class MoodivationPlayerView extends LinearLayout {

    private final PlayerView playerView;

    private Runnable onInitCompleteHandler = () -> {};

    @OptIn(markerClass = UnstableApi.class)
    public MoodivationPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.player_layout, this);
        playerView = findViewById(R.id.player);

        playerView.setUseController(true);
        playerView.setControllerAutoShow(true);
        playerView.setControllerShowTimeoutMs(1000);
        playerView.setControllerHideOnTouch(true);
        playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS);

        Handler mainHandler = new Handler(Looper.getMainLooper());
        AsyncTask.execute(() -> {
            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setTargetBufferBytes(C.LENGTH_UNSET)
                    .setPrioritizeTimeOverSizeThresholds(true)
                    .setAllocator(new DefaultAllocator(true, 32))
                    .setBufferDurationsMs(300, 1000, 300, 300)
                    .build();

            ExoPlayer player = new ExoPlayer.Builder(getContext())
                    .setLoadControl(loadControl)
                    .build();

            mainHandler.post(() -> {
                player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setMaxVideoSizeSd().build());
                playerView.setPlayer(player);

                onInitCompleteHandler.run();
            });
        });
    }

    public void setOnInitCompleteHandler(Runnable onInitCompleteHandler) {
        this.onInitCompleteHandler = onInitCompleteHandler;
    }

    public void setMediaItem(MediaItem mediaItem) {
        assert playerView.getPlayer() != null;

        playerView.getPlayer().setMediaItem(mediaItem);
        playerView.getPlayer().prepare();
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public void cleanup() {
        if (playerView != null && playerView.getPlayer() != null)
            playerView.getPlayer().release();
    }
}
