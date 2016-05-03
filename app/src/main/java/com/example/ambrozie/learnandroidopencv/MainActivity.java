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
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseData;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseNote;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleData;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleMat;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.ml.KNearest;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.OutputStream;
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
  private SampleData sampleData;

  private List<Mat> sample;
  private List<ResponseNote> response;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    SampleData sampleDataTest = loadRecognitionSampleData();
    ResponseData responseData = loadRecognitionResponseData();

    KNearest kNearest = KNearest.create();
    kNearest.train(new Mat(), 0, new Mat());

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
    currentRectangleIndex = 0;
    inputImage = OpencvStavesUtil.drawRectOnImage(inputImage, rectangles.get(currentRectangleIndex));

    imageView.setImageBitmap(inputImage);

    final Button nextButton = (Button) findViewById(R.id.nextRectangleButton);
    if (nextButton != null) {
      nextButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//          SampleData sampleDataXml = loadRecognitionSampleData();
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
            SampleMat sampleMatData = OpencvStavesUtil.matToSampleMat(sampleDataMat);
            sampleData.getSampleMatList().add(sampleMatData);
            sample.add(sampleDataMat);

            // get rectangle response
            String noteStep = ((EditText) findViewById(R.id.pitchStepText)).getText().toString();
            int noteOctave = Integer.parseInt(((EditText) findViewById(R.id.pitchOctaveNumber)).getText().toString());
            String noteType = ((EditText) findViewById(R.id.noteTypeText)).getText().toString();
            ResponseNote noteData = new ResponseNote(noteStep, noteOctave, noteType);
            response.add(noteData);
          } catch (Exception e) {
            e.printStackTrace();
          }

//          String testSampleData = getRecognitionSampleData(sampleData);
//          ResponseData responseData = new ResponseData();
//          responseData.setNoteTrainingDataList(response);
//          String testResponseData = getRecognitionResponseData(responseData);
//          int a =2;
        }
      });
    }
  }

  private String getRecognitionResponseData(ResponseData responseData) {
    Serializer serializer = new Persister();
    String xml;
    OutputStream outputStream = new OutputStream() {
      StringBuilder stringBuilder = new StringBuilder();

      @Override
      public void write(int oneByte) throws IOException {
        this.stringBuilder.append((char) oneByte);
      }

      public String toString(){
        return this.stringBuilder.toString();
      }
    };

    try {
      serializer.write(responseData, outputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return outputStream.toString();
  }

  private String getRecognitionSampleData(SampleData sampleData) {
    Serializer serializer = new Persister();
    String xml;
    OutputStream outputStream = new OutputStream() {
      StringBuilder stringBuilder = new StringBuilder();

      @Override
      public void write(int oneByte) throws IOException {
        this.stringBuilder.append((char) oneByte);
      }

      public String toString(){
        return this.stringBuilder.toString();
      }
    };

    try {
      serializer.write(sampleData, outputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return outputStream.toString();
  }

  private SampleData loadRecognitionSampleData() {
    Serializer serializer = new Persister();

    try {
//      SampleData sampleData = serializer.read(SampleData.class, source);
      return serializer.read(SampleData.class, getResources().openRawResource(R.raw.recognition_sample_data));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private ResponseData loadRecognitionResponseData() {
    Serializer serializer = new Persister();

    try {
      return serializer.read(ResponseData.class, getResources().openRawResource(R.raw.recognition_response_data));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

}
