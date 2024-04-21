package application;

import java.io.File;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VideoController {

	@FXML
	private MediaView mediaView;

	private MediaPlayer mediaPlayer;

	@FXML
	private Slider seekerSlider;

	@FXML
	private Slider volumeSlider;

	private String initialPath = "";

	@FXML
	void processFast(ActionEvent event) {
		mediaPlayer.setRate(2);
		// mediaPlayer.setVolume(0);
	}

	@FXML
	void processMinus5s(ActionEvent event) {
		mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(5)));
	}

	@FXML
	void processOpenVideo(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		if (!initialPath.isEmpty()) {
			fileChooser.setInitialDirectory(new File(initialPath));
		}
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Video Files", "*.mp4", "*.mkv"));

		File videoFile = fileChooser.showOpenDialog(new Stage());

		if (videoFile == null) {
			return;
		}

		initialPath = videoFile.getParent();
		String videoPath = videoFile.toURI().toString();

		Media media = new Media(videoPath);
		mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		// mediaPlayer.setAutoPlay(true);

		DoubleProperty widthProperty = mediaView.fitWidthProperty();
		DoubleProperty heightProperty = mediaView.fitHeightProperty();

		widthProperty.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
		heightProperty.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

		volumeSlider.setValue(mediaPlayer.getVolume() * 100);

		volumeSlider.valueProperty().addListener(observable -> {
			mediaPlayer.setVolume(volumeSlider.getValue() / 100);
		});

		mediaPlayer.setOnReady(() -> {
			seekerSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
		});

		mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
			seekerSlider.setValue(newValue.toSeconds());
		});

//		seekerSlider.valueProperty().addListener(observable -> {
//			mediaPlayer.seek(Duration.seconds(seekerSlider.getValue()));
//		});

		seekerSlider.setOnMouseDragged(mouseEvent -> {
			mediaPlayer.seek(Duration.seconds(seekerSlider.getValue()));
		});

		seekerSlider.setOnMousePressed(mouseEvent -> {
			mediaPlayer.seek(Duration.seconds(seekerSlider.getValue()));
		});

	}

	@FXML
	void processPause(ActionEvent event) {
		mediaPlayer.pause();
	}

	@FXML
	void processPlay(ActionEvent event) {
		mediaPlayer.play();
		mediaPlayer.setRate(1.0);
		mediaPlayer.setVolume(volumeSlider.getValue() / 100);
	}

	@FXML
	void processPlus5s(ActionEvent event) {
		mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
	}

	@FXML
	void processSlow(ActionEvent event) {

		// mediaPlayer.setVolume(0);
		mediaPlayer.setRate(0.5); // ပိုနှေးချင်လို့
	}

	@FXML
	void processStop(ActionEvent event) {
		mediaPlayer.stop();
	}

}
