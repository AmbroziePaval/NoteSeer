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
    int inputPictureFile = R.drawable.test1;
    inputImage = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy1 = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    Bitmap inputImageCopy2 = BitmapFactory.decodeResource(getResources(), inputPictureFile);
    setContentView(R.layout.activity_main);
    imageView = (ImageView) this.findViewById(R.id.imageView);
    inputImage = OpencvStavesUtil.drawElementsContourWithTheStave(inputImageCopy1, inputImageCopy2, inputImage);
    imageView.setImageBitmap(inputImage);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

}