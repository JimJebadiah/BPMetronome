package com.jimjeb.bpmetronome.view;

import com.jimjeb.bpmetronome.model.Metronome;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MetronomeScene implements BPMScenes{

    public MetronomeScene() 
    {
    
    }

    @Override
    public Scene scene(Stage s) throws Exception
    {
        VBox box = new VBox();
        box.setMinSize(256, 424);
        box.setMaxSize(256, 424);


        Metronome metronome = new Metronome();
        metronome.setBpm(176);

        Thread thread = new Thread(metronome);
        thread.start();

        Button button = new Button("play");
        button.setOnAction((e) -> metronome.play());
        box.getChildren().add(button);

        Scene scene = new Scene(box);

        return scene;
    }
    
}
