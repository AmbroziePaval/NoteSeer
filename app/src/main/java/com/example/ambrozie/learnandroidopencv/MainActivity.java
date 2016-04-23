package com.example.ambrozie.learnandroidopencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.OpencvStavesUtil;

import org.opencv.core.Rect;
import org.opencv.features2d.FeatureDetector;

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
//  private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final int inputPictureFile = R.drawable.test_full2_1;
    inputImage = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy1 = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy2 = BitmapFactory.decodeResource(getResources(), inputPictureFile);

    setContentView(R.layout.activity_main);
    imageView = (ImageView) this.findViewById(R.id.imageView);

//    inputImage = OpencvStavesUtil.drawElementsContourWithTheStave(inputImageCopy1, inputImageCopy2, inputImage);

    rectangles = OpencvStavesUtil.getElementsContourWithTheStave(inputImageCopy1, inputImageCopy2);
    currentRectangleIndex = 0;
    inputImage = OpencvStavesUtil.drawRectOnImage(inputImage, rectangles.get(currentRectangleIndex));

    imageView.setImageBitmap(inputImage);

    final Button nextButton = (Button) findViewById(R.id.nextRectangleButton);
    if (nextButton != null) {
      nextButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
          currentRectangleIndex++;
          if (rectangles.size() <= currentRectangleIndex) {
            currentRectangleIndex = 0;
          }
          inputImage = BitmapFactory.decodeResource(getResources(), inputPictureFile);
          inputImage = OpencvStavesUtil.drawRectOnImage(inputImage, rectangles.get(currentRectangleIndex));

          imageView.setImageBitmap(inputImage);
        }
      });
    }
  }

  @Override
  public void onResume() {
    super.onResume();
  }

}