package com.ass.sd2550project;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SoundPlayerFragment extends Fragment  {


    //game music file is downloaded from https://freesound.org/people/Magntron/sounds/335571/
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mediaPlayer = MediaPlayer.create(getContext(),R.raw.game_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(getActivity() instanceof PlayerControls) {
            ((PlayerControls) getActivity()).toggleControls(true);
        }
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        if(getActivity() instanceof PlayerControls) {
            ((PlayerControls) getActivity()).toggleControls(false);
        }

        super.onDestroy();
    }

    public void playSound() {
        if(mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    public void stopSound() {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}
