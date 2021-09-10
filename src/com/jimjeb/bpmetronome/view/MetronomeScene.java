package com.jimjeb.bpmetronome.view;

import java.util.ArrayList;
import java.util.List;

import com.jimjeb.bpmetronome.model.metronome.Metronome;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MetronomeScene implements BPMScenes{

    public static Background NAVY = new Background(new BackgroundFill(Color.NAVY, CornerRadii.EMPTY, Insets.EMPTY));
    public static Background AQUA = new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY));

    public static final Insets STANDARD = new Insets(10);

    public static final int HEIGHT = 424;
    public static final int WIDTH = 256;

    public static final int OFFSET = 10;

    public MetronomeScene() 
    {
    
    }

    @Override
    public Scene scene(Stage s) throws Exception
    {
        s.setTitle("Metronome");

        Metronome metronome = new Metronome();

        VBox box = new VBox();
        box.setMinSize(WIDTH, HEIGHT);
        box.setMaxSize(WIDTH, HEIGHT);

        HBox ticker = new HBox();

        Label BPM = new Label(Integer.toString(metronome.getBpm()));
        BPM.setFont(new Font("Impact", 144));

        double tickerWidth = ((double) WIDTH / (double) metronome.getBeatsPerMeasure()) - OFFSET;
        System.out.println(tickerWidth);
        double spacing = (WIDTH - (metronome.getBeatsPerMeasure() * tickerWidth)) / (metronome.getBeatsPerMeasure() + 1);
        System.out.println(spacing);
        List<Label> tickers = generateTickers(metronome, tickerWidth);
        ticker.getChildren().addAll(tickers);
        ticker.setSpacing(spacing);
        ticker.setAlignment(Pos.CENTER);

        Thread thread = new Thread(metronome);
        thread.start();

        Button button = new Button("Play");
        button.setOnAction((e) -> metronome.play());

        TextField textField = new TextField();

        Slider slider = new Slider();
        slider.setBlockIncrement(1);
        slider.setMin(Metronome.MIN_BPM);
        slider.setMax(Metronome.MAX_BPM);
        slider.setValue(metronome.getBpm());
        slider.valueProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                int bpm = arg2.intValue();
                metronome.setBpm(bpm);
                textField.setText("");
                BPM.setText(Integer.toString(bpm));
                slider.setValue(bpm);
            }       
        });

        HBox inputAndButton = new HBox();

        textField.setPromptText("Enter BPM:");
        Button enter = new Button("Enter");
        enter.setOnAction((e) -> {
            int bpm = Integer.parseInt(textField.getText());
            metronome.setBpm(bpm);
            textField.setText(Integer.toString(metronome.getBpm()));
            BPM.setText(Integer.toString(bpm));
            slider.setValue(bpm);
        });
        inputAndButton.setAlignment(Pos.CENTER);
        inputAndButton.setPadding(STANDARD);
        inputAndButton.getChildren().addAll(textField, enter);

        Label boxLabel = new Label("Beats Per Measure:");

        ComboBox<Integer> comboBox = new ComboBox<>();
        comboBox.setPromptText(Integer.toString(metronome.getBeatsPerMeasure()));
        comboBox.getItems().addAll(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        comboBox.setOnAction((e) -> {
            metronome.setBeatsPerMeasure(comboBox.getValue());
            ticker.getChildren().clear();
            double width = ((double) WIDTH / (double) metronome.getBeatsPerMeasure()) - OFFSET;
            double space = (WIDTH - (metronome.getBeatsPerMeasure() * width)) / (metronome.getBeatsPerMeasure() + 1);
            ticker.getChildren().addAll(generateTickers(metronome, width));
            ticker.setSpacing(space);
        });
    
        box.getChildren().addAll(BPM, ticker, button, slider, inputAndButton, boxLabel, comboBox);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(10);
        Scene scene = new Scene(box);
        return scene;
    }

    public List<Label> generateTickers(Metronome metronome, double tickerWidth) 
    {
        int size = metronome.getBeatsPerMeasure();
        List<Label> tickers = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            Label label = createTickerLabel(metronome.getBeatsPerMeasure(), tickerWidth);
            tickers.add(label);
        }
        metronome.clearCurrentBeatObservers();
        metronome.registerCurrentBeat((c) -> {
            for(Label label : tickers) 
            {
                label.setBackground(NAVY);
            }
            if(c <= tickers.size() && metronome.isPlaying())
            {
                tickers.get(c).setBackground(AQUA);
            }
        });
        return tickers;
    }
    
    public Label createTickerLabel(int beatsPerMeasure, double tickerWidth)
    {
        Label label = new Label();
        label.setMinSize(tickerWidth, 54);
        label.setMaxSize(tickerWidth, 54);
        label.setBackground(NAVY);
        label.setAlignment(Pos.CENTER);
        return label;
    }
}
