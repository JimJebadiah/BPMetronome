package com.jimjeb.bpmetronome.view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BPMScene implements BPMScenes{

    public BPMScene() {
    
    }

    @Override
    public Scene scene(Stage s) 
    {
        VBox box = new VBox();
        box.setMinSize(256, 424);
        box.setMaxSize(256, 424);

        Scene scene = new Scene(box);

        return scene;
    }
    
}
