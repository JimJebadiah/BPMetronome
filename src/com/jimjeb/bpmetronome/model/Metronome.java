package com.jimjeb.bpmetronome.model;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Metronome implements Runnable
{
    public static final int MINUTE_CONSTANT = 60000;
    public static final int MAX_BPM = 500;

    private boolean playing = false;
    private int beatsPerMeasure;
    private int noteDuration;
    private int bpm;
    private int currentBeat;
    private MediaPlayer hit;
    private MediaPlayer beat;
    
    public Metronome() 
    {
        beatsPerMeasure = 4;
        noteDuration = 4;
        bpm = 60;
        currentBeat = 1;

        String hitPath = "data/sounds/hit.ogg";
        String beatPath = "data/sounds/beat.ogg";

        Media hitMedia = new Media(new File(hitPath).toURI().toString());
        Media beatMedia = new Media(new File(beatPath).toURI().toString());

        hit = new MediaPlayer(hitMedia);
        beat = new MediaPlayer(beatMedia);
    }

    @Override
    public void run()
    {
        while(true) 
        {
            while(playing) 
            {
                if(currentBeat != 1)
                {
                    beat.play();
                } else 
                {
                    hit.play();
                }
                currentBeat += currentBeat % beatsPerMeasure;
                long waitTime = MINUTE_CONSTANT * (1 / bpm);
                try
                {
                    Thread.sleep(waitTime);
                } catch(InterruptedException ie) 
                {
                    playing = false;
                }
            }
        }
    }

    public void play() 
    {
        playing = !playing;
        currentBeat = 1;
    }

    public void setBpm(int bpm) {
        if(bpm > MAX_BPM) 
        {
            bpm = MAX_BPM;
        }
        this.bpm = bpm;
    }

    public void setBeatsPerMeasure(int beatsPerMeasure) {
        this.beatsPerMeasure = beatsPerMeasure;
    }

    public void setNoteDuration(int noteDuration) {
        this.noteDuration = noteDuration;
    }

    public int getBeatsPerMeasure() {
        return beatsPerMeasure;
    }

    public int getNoteDuration() {
        return noteDuration;
    }
}
