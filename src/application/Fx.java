package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import javax.imageio.ImageIO;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Fx extends Application {
	static int width;
	static int height;
	static String fname;
	static String oname;
	static boolean snapshot;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setMinWidth(width);
		primaryStage.setMinHeight(height);
		// System.out.println("start:" + fname + ":" + width);
		WebView wb = new WebView();
		WebEngine webEngine = wb.getEngine();
		Scene scene = new Scene(wb, width, height);
		webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				// System.out.println("SUCCESS");
				if (snapshot) {
					final PauseTransition pause = new PauseTransition(Duration.millis(500));
					pause.setOnFinished(e -> {
						WritableImage img = new WritableImage(width, height);
						scene.snapshot(img);
						// System.out.println("after snapshot");
						try {
							File file = new File(new URI(oname));
							ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
							Platform.exit();
						} catch (Exception s) {
						}
					});
					pause.play();
				}
			}
		});
        webEngine.load(fname);
		//System.out.println("before end");
		primaryStage.setScene(scene);
		System.out.println("end");
		primaryStage.show();

	}

	String htmlInput() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String r = "";
		try {
			StringBuilder sb = new StringBuilder();
			//System.out.println("HALLO");
			String line = br.readLine();
			while (line != null) {
				System.out.println(line);
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			r = sb.toString();
		} finally {
			br.close();
		}
		return r;
	}

	public static void main(String[] args) {
		fname = args[0];
		oname = args[1];
		width = Integer.parseInt(args[2]);
		height = Integer.parseInt(args[3]);
		snapshot = Boolean.parseBoolean(args[4]);
		launch();
	}
}
