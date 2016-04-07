package com.example.ambrozie.learnandroidopencv.opencv_mn_utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ambrozie on 4/6/2016.
 */
public class OpencvStavesUtil {

  public static Bitmap removeStavesFromBitmap(Bitmap inputImage) {
    int imageHeight = inputImage.getHeight();
    Mat inputImageMat = new Mat();
    Utils.bitmapToMat(inputImage, inputImageMat);

    // change image to gray scale
    Imgproc.cvtColor(inputImageMat, inputImageMat, Imgproc.COLOR_RGBA2GRAY);

    // make image negative binary
    Mat dst = new Mat();
    Imgproc.adaptiveThreshold(inputImageMat, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

    Mat horizontal = inputImageMat.clone();
    Mat vertical = inputImageMat.clone();

    int horizontalsize = horizontal.cols() / (imageHeight * 22 / (100 * 3));
    Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize, 1));
    Imgproc.dilate(horizontal, horizontal, horizontalStructure);

    int verticalsize = vertical.rows() / (imageHeight * 22 / (100 * 3));
    Mat verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, verticalsize));
    Imgproc.dilate(vertical, vertical, verticalStructure);

    Utils.matToBitmap(vertical, inputImage);
    return inputImage;
  }

  public static Bitmap findNotationContours(Bitmap inputImage) {
    Mat inputImageMat = new Mat();
    Mat resultImageMat = new Mat();
    Utils.bitmapToMat(inputImage, inputImageMat);
    Utils.bitmapToMat(inputImage, resultImageMat);

    Imgproc.cvtColor(inputImageMat, inputImageMat, Imgproc.COLOR_BGR2GRAY);
    Imgproc.GaussianBlur(inputImageMat, inputImageMat, new Size(5, 5), 0);
    Imgproc.adaptiveThreshold(inputImageMat, inputImageMat, 255, 1, 1, 11, 2);

    List<MatOfPoint> contours = new ArrayList<>();
    Mat hierarchy = new Mat();
    Imgproc.findContours(inputImageMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

    Scalar contourScalar = new Scalar(255, 0, 0);

    List<Rect> allContourRectangles = new ArrayList<>();
    for (int i = 0; i < contours.size(); i++) {
      Rect rect = Imgproc.boundingRect(contours.get(i));
      allContourRectangles.add(rect);
    }

    for (Rect rect : generifyTheRectangleContours(allContourRectangles)) {
      Imgproc.rectangle(resultImageMat,
              new Point(rect.x, rect.y),
              new Point(rect.x + rect.width, rect.y + rect.height),
              contourScalar, 3);
      Log.i("contour", "contour" + " x:" + rect.x + " y:" + rect.y);
    }

    Utils.matToBitmap(resultImageMat, inputImage);
    return inputImage;
  }

  public static List<Rect> generifyTheRectangleContours(List<Rect> rectangles) {
    for (int i = 0; i < rectangles.size(); i++) {
      int j = 0;
      while (j < rectangles.size()) {
        Rect rect1 = rectangles.get(i);
        Rect rect2 = rectangles.get(j);
        if (i != j && (rectanglesIntersect(rect1, rect2) || rectanglesIntersect(rect2, rect1))) {
          rect1.width = Math.max(rect1.x + rect1.width, rect2.x + rect2.width) - Math.min(rect1.x, rect2.x);
          rect1.x = Math.min(rect1.x, rect2.x);
          rect1.height = Math.max(rect1.y + rect1.height, rect2.y + rect2.height) - Math.min(rect1.y, rect2.y);
          rect1.y = Math.min(rect1.y, rect2.y);
          rectangles.remove(j);
          i = j = 0;
        } else {
          j++;
        }
      }
    }
    return rectangles;
  }

  private static boolean rectanglesIntersect(Rect rect1, Rect rect2) {
    if (rect1.contains(new Point(rect2.x, rect2.y)))
      return true;
    else if (rect1.contains(new Point(rect2.x, rect2.y + rect2.height)))
      return true;
    else if (rect1.contains(new Point(rect2.x + rect2.width, rect2.y)))
      return true;
    else return rect1.contains(new Point(rect2.x + rect2.width, rect2.y + rect2.height));
  }
}
