package com.jimjeb.bpmetronome.model.metronome;

public interface CurrentBeatObserver
{
    public abstract void update(int currentBeat);
}
