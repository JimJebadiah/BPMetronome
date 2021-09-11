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
    private double currentBeat;
    private MediaPlayer hit;
    private MediaPlayer beat;
    private MediaPlayer tap;
    private long waitTime;
    private int divide;

    private Set<CurrentBeatObserver> cOberservers;
    
    public Metronome() 
    {
        beatsPerMeasure = 4;
        noteDuration = 4;
        bpm = 120;
        currentBeat = 0;
        divide = 1;

        String hitPath = "data/sounds/hit.wav";
        String beatPath = "data/sounds/beat.wav";
        String tapPath = "data/sounds/tap.wav";

        Media hitMedia = new Media(new File(hitPath).toURI().toString());
        Media beatMedia = new Media(new File(beatPath).toURI().toString());
        Media tapMedia = new Media(new File(tapPath).toURI().toString());

        hit = new MediaPlayer(hitMedia);
        beat = new MediaPlayer(beatMedia);
        tap = new MediaPlayer(tapMedia);

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
                    if(currentBeat == 0)
                    {
                        hit.play();
                    } else if((int) currentBeat == currentBeat || (currentBeat - (int) currentBeat) > 0.9)
                    {
                        beat.play();
                    }
                    else 
                    {
                        tap.play();
                    }
                    notifyCurrentBeatObservers();
                    currentBeat = (currentBeat + (1 / (double) divide));
                    if((currentBeat - (int) currentBeat) > 0.9 || currentBeat - (int) currentBeat < 0.1) 
                    {
                        currentBeat = (int) Math.round(currentBeat);
                    }
                    if(currentBeat >= beatsPerMeasure)
                    {
                        currentBeat = 0;
                    }
                    System.out.println(currentBeat);
                    try
                    {
                        Thread.sleep(waitTime / divide);
                    } catch(InterruptedException ie) 
                    {
                        playing = false;
                    }
                    hit.stop();
                    beat.stop();
                    tap.stop();
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

    public void setDivide(int divide) {
        this.divide = divide;
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

    public double getCurrentBeat() {
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
