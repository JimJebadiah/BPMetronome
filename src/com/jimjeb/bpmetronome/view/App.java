package com.jimjeb.bpmetronome.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application 
{
    public static final List<BPMScenes> scenes = new ArrayList<>();

    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage s) throws Exception 
    {
        scenes.add(new BPMScene());
        scenes.add(new MetronomeScene());
        s.setScene(scenes.get(1).scene(s));
        s.setResizable(false);

        s.show();
    }

    @Override
    public void stop() throws Exception 
    {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for(Thread t : threadSet)
        {
            t.interrupt();
        }
    }
}