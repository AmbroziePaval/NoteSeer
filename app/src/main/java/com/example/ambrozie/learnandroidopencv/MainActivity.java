package com.example.ambrozie.learnandroidopencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.OpencvStavesUtil;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.NoteTrainingData;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleData;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  static {
    System.loadLibrary("opencv_java3");
//    System.loadLibrary("nonfree");
  }
  private ImageView imageView;
  private Bitmap inputImage; // make bitmap from image resource
  private List<Rect> rectangles;
  private int currentRectangleIndex;

  private List<Mat> sample;
  private List<NoteTrainingData> response;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    sample = new ArrayList<>();
    response = new ArrayList<>();

    final int inputPictureFile = R.drawable.test_full2_1;
    inputImage = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy1 = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy2 = BitmapFactory.decodeResource(getResources(), inputPictureFile);

    setContentView(R.layout.activity_main);
    imageView = (ImageView) this.findViewById(R.id.imageView);

    rectangles = OpencvStavesUtil.getElementsContourWithTheStave(inputImageCopy1, inputImageCopy2);
    currentRectangleIndex = 0;
    inputImage = OpencvStavesUtil.drawRectOnImage(inputImage, rectangles.get(currentRectangleIndex));

    imageView.setImageBitmap(inputImage);

    final Button nextButton = (Button) findViewById(R.id.nextRectangleButton);
    if (nextButton != null) {
      nextButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
          testJAXBMatLoader();
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
            Mat sampleData = OpencvStavesUtil.getElementSampleDataFromRectangle(source, rectangles.get(currentRectangleIndex));
            String sampleDataJSON = OpencvStavesUtil.matToJson(sampleData);
            sample.add(sampleData);

            // get rectangle response
            String noteStep = ((EditText)findViewById(R.id.pitchStepText)).getText().toString();
            int noteOctave = Integer.parseInt(((EditText) findViewById(R.id.pitchOctaveNumber)).getText().toString());
            String noteType = ((EditText)findViewById(R.id.noteTypeText)).getText().toString();
            NoteTrainingData noteData = new NoteTrainingData(noteStep, noteOctave, noteType);
            response.add(noteData);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }

  private void testJAXBMatLoader() {
  }

  @Override
  public void onResume() {
    super.onResume();
  }

}