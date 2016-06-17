package org.rascalmpl.library.experiments.vis2.sandbox;
// package application;

import java.io.File;
import java.net.URI;

import javax.imageio.ImageIO;


import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Fx extends Application {
	static int width;
	static int height;
	static String fname;
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setMinWidth(width);
		primaryStage.setMinHeight(height);
		// System.out.println("start:"+fname+":"+width);
	    WebView wb = new WebView();
	    WebEngine webEngine = wb.getEngine();
	    Scene scene = new Scene(wb, width, height);
	    webEngine.getLoadWorker().stateProperty().addListener((obs,oldState,newState)->{
	        if(newState==State.SUCCEEDED){
	            final PauseTransition pause = new PauseTransition(Duration.millis(500));
	            pause.setOnFinished(e->{
	                WritableImage img = new WritableImage(width, height);
	                scene.snapshot(img);
	                // System.out.println("after snapshot");
                    String oname = fname.replaceFirst("[.]html", ".png");
	                try {
	                	File file = new File(new URI(oname));
	                    ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
	                    Platform.exit();
	                    }
	                catch (Exception s) {
	                }   
	            });
	            pause.play(); 
	        }
	    });
	    webEngine.load(fname);
	    // System.out.println("before end");
	    primaryStage.setScene(scene);
	    // System.out.println("end");
	    primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		fname = args[0];
		width = Integer.parseInt(args[1]);
		height = Integer.parseInt(args[2]);
		launch();
	}
}
