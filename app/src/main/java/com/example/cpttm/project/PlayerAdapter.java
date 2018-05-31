package com.example.cpttm.project;

/**
 * Allows {@link Player} to control media playback of {@link MediaPlayerHolder}.
 */

public interface PlayerAdapter {

    void loadMedia(int resourceId);

    void release();

    boolean isPlaying();

    void play();

    void reset();

    void pause();

    void initializeProgressCallback();

    void seekTo(int position);
}