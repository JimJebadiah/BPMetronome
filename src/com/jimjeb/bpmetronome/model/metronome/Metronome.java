package com.jimjeb.bpmetronome.model.metronome;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Metronome implements Runnable
{
    public static final int MINUTE_CONSTANT = 60000;
    public static final int MIN_BPM = 30;
    public static final int MAX_BPM = 500;

    private boolean playing = false;
    private int beatsPerMeasure;
    private int noteDuration;
    private int bpm;
    private int currentBeat;
    private MediaPlayer hit;
    private MediaPlayer beat;
    private long waitTime;

    private Set<CurrentBeatObserver> cOberservers;
    
    public Metronome() 
    {
        beatsPerMeasure = 4;
        noteDuration = 4;
        bpm = 120;
        currentBeat = 0;

        String hitPath = "data/sounds/hit.wav";
        String beatPath = "data/sounds/beat.wav";

        Media hitMedia = new Media(new File(hitPath).toURI().toString());
        Media beatMedia = new Media(new File(beatPath).toURI().toString());

        hit = new MediaPlayer(hitMedia);
        beat = new MediaPlayer(beatMedia);

        cOberservers = new HashSet<>();
    }

    @Override
    public void run()
    {
        waitTime = (long) (MINUTE_CONSTANT * (1 / (float) bpm));
        while(true) 
        {
            synchronized(this) 
            {
                while(playing) 
                {
                    if(currentBeat != 0)
                    {
                        beat.play();
                    } else 
                    {
                        hit.play();
                    }
                    notifyCurrentBeatObservers();
                    currentBeat = (currentBeat + 1) % beatsPerMeasure;
                    try
                    {
                        Thread.sleep(waitTime);
                    } catch(InterruptedException ie) 
                    {
                        playing = false;
                    }
                    hit.stop();
                    beat.stop();
                }
            }
        }
    }
    

    public void play() 
    {
        playing = !playing;
        currentBeat = 0;
        notifyCurrentBeatObservers();
    }

    public void setBpm(int bpm) 
    {
        if(bpm > MAX_BPM) 
        {
            bpm = MAX_BPM;
        }
        if(bpm < MIN_BPM) 
        {
            bpm = MIN_BPM;
        }
        waitTime = (long) (MINUTE_CONSTANT * (1 / (float) bpm));
        this.bpm = bpm;
    }

    public int getBpm() {
        return bpm;
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

    public int getCurrentBeat() {
        return currentBeat;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void registerCurrentBeat(CurrentBeatObserver c)
    {
        cOberservers.add(c);
    }

    public void notifyCurrentBeatObservers()
    {
        for(CurrentBeatObserver c: cOberservers)
        {
            c.update(currentBeat);
        }
    }

    public void clearCurrentBeatObservers()
    {
        cOberservers.clear();
    }
}
