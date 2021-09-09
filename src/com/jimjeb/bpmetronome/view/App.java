package com.jimjeb.bpmetronome.view;

import java.util.ArrayList;
import java.util.List;

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
    public void start(Stage arg0) throws Exception 
    {
        scenes.add(new BPMScene());
    }
    
}