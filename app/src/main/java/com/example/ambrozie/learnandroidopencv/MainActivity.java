package com.example.ambrozie.learnandroidopencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.OpencvStavesUtil;

import org.opencv.features2d.FeatureDetector;

public class MainActivity extends AppCompatActivity {

  static {
    System.loadLibrary("opencv_java3");
    System.loadLibrary("nonfree");
  }
  private ImageView imageView;
  private Bitmap inputImage; // make bitmap from image resource
  private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inputImage = BitmapFactory.decodeResource(getResources(), R.drawable.test_full2_1);
    Bitmap inputImageCopy1 = BitmapFactory.decodeResource(getResources(), R.drawable.test_full2_1);
    Bitmap inputImageCopy2 = BitmapFactory.decodeResource(getResources(), R.drawable.test_full2_1);
    setContentView(R.layout.activity_main);
    imageView = (ImageView) this.findViewById(R.id.imageView);
//    inputImage = OpencvStavesUtil.getStavesFromBitmap(inputImage);
//    inputImage = OpencvStavesUtil.removeStavesFromBitmap(inputImage);
//    inputImage = OpencvStavesUtil.findNotationContours(inputImage);
//    inputImage = OpencvStavesUtil.getStaveLineContours(inputImage);
    inputImage = OpencvStavesUtil.drawElementsContourWithTheStave(inputImageCopy1, inputImageCopy2, inputImage);
    imageView.setImageBitmap(inputImage);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

}