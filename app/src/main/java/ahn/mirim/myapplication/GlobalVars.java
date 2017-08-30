package ahn.mirim.myapplication;


import android.app.Application;

public class GlobalVars extends Application {

    private int status;
    private boolean isMusic;	// Music
    private boolean isSound;	// Sound
    private boolean isVibe;		// Vibrator

    public int getStatus(){
        return status;
    }

    public void setStatus(int value){
        status=value;
    }

    public boolean getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(boolean value) {
        isMusic = value;
    }

    //-----------------------------
    // isSound
    //-----------------------------
    public boolean getIsSound() {
        return isSound;
    }

    public void setIsSound(boolean value) {
        isSound = value;
    }

    //-----------------------------
    // isVibe
    //-----------------------------
    public boolean getIsVibe() {
        return isVibe;
    }

    public void setIsVibe(boolean value) {
        isVibe = value;
    }

}