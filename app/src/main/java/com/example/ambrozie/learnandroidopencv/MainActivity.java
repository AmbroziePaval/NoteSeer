package com.example.ambrozie.learnandroidopencv;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.OpencvStavesUtil;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.knearest.KnearestNote;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.knearest.KnearestUtil;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.recognition.NoteUtil;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.recognition.RecognitionDataLoader;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseData;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseMat;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseNote;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleData;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleMat;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.notes.NotePlayer;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.notes.SheetPlayer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

  static {
    System.loadLibrary("opencv_java3");
//    System.loadLibrary("nonfree");
  }

  private ImageView imageView;
  private Bitmap inputImage; // make bitmap from image resource
  private List<Rect> rectangles;
  private int currentRectangleIndex;
  private SampleData sampleData;
  private KnearestNote kNearest;
  private RecognitionDataLoader recognitionDataLoader;
  private MediaPlayer mediaPlayer;
  NotePlayer notePlayer;

  private List<Mat> sample;
  private List<ResponseNote> response;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recognitionDataLoader = new RecognitionDataLoader(getResources());
    final SampleData sampleDataTest = recognitionDataLoader.loadRecognitionSampleData();
    final ResponseData responseData = recognitionDataLoader.loadRecognitionResponseData();

    kNearest = new KnearestNote();
    KnearestUtil.trainWithData(kNearest, sampleDataTest, responseData);
    notePlayer = new NotePlayer(getApplicationContext());

    sample = new ArrayList<>();
    response = new ArrayList<>();
    sampleData = new SampleData();

    final int inputPictureFile = R.drawable.test_full2_1;
    inputImage = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy1 = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy2 = BitmapFactory.decodeResource(getResources(), inputPictureFile);

    setContentView(R.layout.activity_main);
    imageView = (ImageView) this.findViewById(R.id.imageView);

    rectangles = OpencvStavesUtil.getElementsContourWithTheStave(inputImageCopy1, inputImageCopy2);
    currentRectangleIndex = -1;

    imageView.setImageBitmap(inputImage);

    final Button nextButton = (Button) findViewById(R.id.nextRectangleButton);
    if (nextButton != null) {
      nextButton.setText(R.string.startString);
    }
    if (nextButton != null) {
      nextButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          nextButton.setText(R.string.nextCaptureString);
          currentRectangleIndex++;
          if (rectangles.size() <= currentRectangleIndex) {
            currentRectangleIndex = 0;
          }
          inputImage = BitmapFactory.decodeResource(getResources(), inputPictureFile);
          inputImage = OpencvStavesUtil.drawRectOnImage(inputImage, rectangles.get(currentRectangleIndex));

          imageView.setImageBitmap(inputImage);

          try {
            // get rectangle data
            Mat source = new Mat();
            Utils.bitmapToMat(inputImage, source);
            Mat sampleDataMat = OpencvStavesUtil.getElementSampleDataFromRectangle(source, rectangles.get(currentRectangleIndex));

            testWithUserOutput(sampleDataMat);
          } catch (Exception e) {
            e.printStackTrace();
          }

        }
      });
    }

    final Button playSheetButton = (Button) findViewById(R.id.playSheetButton);
    if (playSheetButton != null) {
      playSheetButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          List<Mat> samplesToPlay = new ArrayList<>();
          int rectIndex = 0;
          while(rectangles.size() > rectIndex) {
            inputImage = BitmapFactory.decodeResource(getResources(), inputPictureFile);
            Mat source = new Mat();
            Utils.bitmapToMat(inputImage, source);
            Mat sampleDataMat = OpencvStavesUtil.getElementSampleDataFromRectangle(source, rectangles.get(rectIndex));

            samplesToPlay.add(sampleDataMat);
            rectIndex++;
          }
          playSheet(samplesToPlay);
        }
      });
    }
  }

  private void playSheet(List<Mat> samplesToPlay) {
    SheetPlayer sheetPlayer = new SheetPlayer(new NotePlayer(getApplicationContext()));

    int sampleIndex = 0;
    while(sampleIndex < samplesToPlay.size()) {
      ResponseMat responseMat = kNearest.findNearest(samplesToPlay.get(sampleIndex));
      sheetPlayer.addNote(responseMat.getResponseNote());
      sampleIndex++;
    }

    sheetPlayer.playSheet();
  }

  public void playNote(final ResponseNote responseNote) {
    notePlayer.setResponseNote(responseNote);

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<Integer> future = executorService.submit(notePlayer);
    try {
      future.get(4, TimeUnit.SECONDS);
      executorService.shutdownNow();
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }
  }

  public void testWithUserOutput(Mat sampleDataMat) {
    ResponseMat responseMat;
    responseMat = kNearest.findNearest(sampleDataMat);
    ((EditText) findViewById(R.id.pitchStepText)).setText(responseMat.getName());
    ((EditText) findViewById(R.id.noteTypeText)).setText(responseMat.getType());
    // TODO add other note types here. to be changed!
    if (responseMat.toBePlayed()) {
      Integer octave = responseMat.getResponseNote().getOctave();
      ((EditText) findViewById(R.id.pitchOctaveNumber)).setText(octave.toString());
      playNote(responseMat.getResponseNote());
    } else {
      ((EditText) findViewById(R.id.pitchOctaveNumber)).setText("");
    }
  }

  public void trainWithUserImput(Mat sampleDataMat) {
    SampleMat sampleMatData = OpencvStavesUtil.matToSampleMat(sampleDataMat);
    sampleData.getSampleMatList().add(sampleMatData);
    sample.add(sampleDataMat);

    //get rectangle response
    String noteStep = ((EditText) findViewById(R.id.pitchStepText)).getText().toString();
    int noteOctave = Integer.parseInt(((EditText) findViewById(R.id.pitchOctaveNumber)).getText().toString());
    String noteType = ((EditText) findViewById(R.id.noteTypeText)).getText().toString();
    ResponseNote noteData = new ResponseNote(noteStep, noteOctave, noteType);
    response.add(noteData);

    // get the samples string xml
    String testSampleData = recognitionDataLoader.getRecognitionSampleData(sampleData);
    // get the responses string xml
    ResponseData responseData = new ResponseData();
    responseData.setNoteTrainingDataList(response);
    String testResponseData = recognitionDataLoader.getRecognitionResponseData(responseData);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

}
