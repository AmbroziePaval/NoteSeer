package com.example.ambrozie.learnandroidopencv.opencv_mn_utils;

import android.graphics.Bitmap;
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
 * A class that works on pictures related to musical notation
 * Created by Ambrozie on 4/6/2016.
 */
public class OpencvStavesUtil {

  /**
   * The function gets a Bitmap image of a stave with elements, from which it removes the elements
   *
   * @param inputImage the image from which the elements will be removed
   * @return the same image but with just the lines
   */
  public static Bitmap getStavesFromBitmap(Bitmap inputImage) {
    int imageHeight = inputImage.getHeight();
    Mat inputImageMat = new Mat();
    Utils.bitmapToMat(inputImage, inputImageMat);

    // change image to gray scale
    Imgproc.cvtColor(inputImageMat, inputImageMat, Imgproc.COLOR_RGBA2GRAY);

    // make image negative binary
    Mat dst = new Mat();
    Imgproc.adaptiveThreshold(inputImageMat, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

    Mat horizontal = inputImageMat.clone();

    int horizontalsize = horizontal.cols() / (imageHeight * 22 / (100 * 3));
    Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize, 1));
    Imgproc.dilate(horizontal, horizontal, horizontalStructure);

    Utils.matToBitmap(horizontal, inputImage);
    return inputImage;
  }

  /**
   * The function gets a Bitmap image of a stave with elements, from which it removes the lines
   *
   * @param inputImage the image from which the stave line will be removed
   * @return the same image but with no lines
   */
  public static Bitmap removeStavesFromBitmap(Bitmap inputImage) {
    int imageHeight = inputImage.getHeight();
    Mat inputImageMat = new Mat();
    Utils.bitmapToMat(inputImage, inputImageMat);

    // change image to gray scale
    Imgproc.cvtColor(inputImageMat, inputImageMat, Imgproc.COLOR_RGBA2GRAY);

    // make image negative binary
    Mat dst = new Mat();
    Imgproc.adaptiveThreshold(inputImageMat, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

    Mat vertical = inputImageMat.clone();

    int verticalsize = vertical.rows() / (imageHeight * 22 / (100 * 3));
    Mat verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, verticalsize));
    Imgproc.dilate(vertical, vertical, verticalStructure);

    Utils.matToBitmap(vertical, inputImage);
    return inputImage;
  }

  /**
   * Function that gets the input image and contours every separate element from it
   *
   * @param inputImage the image bitmap
   * @return an image bitmap with all the elements contoured in a red rectangle
   */
  public static Bitmap findNotationContours(Bitmap inputImage) {
    Mat resultImageMat = new Mat();
    Utils.bitmapToMat(inputImage, resultImageMat);

    Scalar contourScalar = new Scalar(255, 0, 0);

    List<Rect> allContourRectangles = getImageElementContourRectangles(inputImage);

    for (Rect rect : allContourRectangles) {
      Imgproc.rectangle(resultImageMat,
              new Point(rect.x, rect.y),
              new Point(rect.x + rect.width, rect.y + rect.height),
              contourScalar, 3);
      Log.i("contour", "contour" + " x:" + rect.x + " y:" + rect.y);
    }

    Utils.matToBitmap(resultImageMat, inputImage);
    return inputImage;
  }

  /**
   * Functions that finds the element contours separated rectangles
   *
   * @param inputImage the image with the elements
   * @return a list of Rect objects for each element
   */
  private static List<Rect> getImageElementContourRectangles(Bitmap inputImage) {
    Mat inputImageMat = new Mat();
    Utils.bitmapToMat(inputImage, inputImageMat);

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

    return generifyTheRectangleContours(allContourRectangles);
  }

  /**
   * Functions that generifies the contours that intersect to a contour that contains all intersections
   *
   * @param rectangles the list of rectangles
   * @return a new list of rectangles that do not intersect
   */
  private static List<Rect> generifyTheRectangleContours(List<Rect> rectangles) {
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

  /**
   * Function that checkes if 2 rectangles intersect
   *
   * @param rect1
   * @param rect2
   * @return
   */
  private static boolean rectanglesIntersect(Rect rect1, Rect rect2) {
    if (rect1.contains(new Point(rect2.x, rect2.y)))
      return true;
    else if (rect1.contains(new Point(rect2.x, rect2.y + rect2.height)))
      return true;
    else if (rect1.contains(new Point(rect2.x + rect2.width, rect2.y)))
      return true;
    else return rect1.contains(new Point(rect2.x + rect2.width, rect2.y + rect2.height));
  }

  public static Bitmap getStaveLineContours(Bitmap inputImage) {
    Mat resultImageMat = new Mat();
    Utils.bitmapToMat(inputImage, resultImageMat);
    Scalar contourScalar = new Scalar(255, 0, 0);

    List<Rect> allContourRectangles = getStavesContainingRectangles(inputImage);

    for (Rect rect : allContourRectangles) {
      Imgproc.rectangle(resultImageMat,
              new Point(rect.x, rect.y),
              new Point(rect.x + rect.width, rect.y + rect.height),
              contourScalar, 3);
      Log.i("contour", "contour" + " x:" + rect.x + " y:" + rect.y);
    }

    Utils.matToBitmap(resultImageMat, inputImage);
    return inputImage;
  }

  /**
   * Function calculates the rectangles containing each stave from the picture
   *
   * @param inputImage the picture
   * @return a list of Rect object corresponding to each stave
   */
  public static List<Rect> getStavesContainingRectangles(Bitmap inputImage) {
    List<Rect> countourRectangles = getImageElementContourRectangles(inputImage);

    // remove rectangles that do not contain a stave line
    List<Rect> stavesLineRectangles = new ArrayList<>();
    for (Rect rectangle : countourRectangles) {
      if (rectangle.width > 500) {
        stavesLineRectangles.add(rectangle);
      }
    }

    // contain the line contours in a containing rectangle contour
    for (int i = 0; i < stavesLineRectangles.size(); i++) {
      int j = 0;
      while (j < stavesLineRectangles.size()) {
        Rect rect1 = stavesLineRectangles.get(i);
        Rect rect2 = stavesLineRectangles.get(j);
        if (i != j && (Math.abs(rect1.y - rect2.y) < 250)) {
          rect1.width = Math.max(rect1.x + rect1.width, rect2.x + rect2.width) - Math.min(rect1.x, rect2.x);
          rect1.x = Math.min(rect1.x, rect2.x);
          rect1.height = Math.max(rect1.y + rect1.height, rect2.y + rect2.height) - Math.min(rect1.y, rect2.y);
          rect1.y = Math.min(rect1.y, rect2.y);
          stavesLineRectangles.remove(j);
          i = j = 0;
        } else {
          j++;
        }
      }
    }
    return stavesLineRectangles;
  }

  public static Bitmap drawElementsContourWithTheStave(Bitmap inputImage1, Bitmap inputImage2, Bitmap inputImage3) {
    Mat resultImageMat = new Mat();
    Utils.bitmapToMat(inputImage3, resultImageMat);
    Scalar contourScalar = new Scalar(255, 0, 0);
    List<Rect> rectangles = getElementsContourWithTheStave(inputImage1, inputImage2);

    for (Rect rect : rectangles) {
      Imgproc.rectangle(resultImageMat,
              new Point(rect.x, rect.y),
              new Point(rect.x + rect.width, rect.y + rect.height),
              contourScalar, 3);
      Log.i("contour", "contour" + " x:" + rect.x + " y:" + rect.y);
    }

    Utils.matToBitmap(resultImageMat, inputImage3);

    return inputImage3;
  }

  /**
   * Function calculates the rectangles containing each element with the it's part of the stave
   *
   * @param inputImage1 the image with the staves and elements
   * @param inputImage2 the image with the staves and elements
   * @return a list of Rect objects containing the element and the stave
   */
  public static List<Rect> getElementsContourWithTheStave(Bitmap inputImage1, Bitmap inputImage2) {
    Bitmap inputImageStaves = getStavesFromBitmap(inputImage1);
    List<Rect> imageStaveContourRectangles = getStavesContainingRectangles(inputImageStaves);

    Bitmap inputImageElements = removeStavesFromBitmap(inputImage2);
    List<Rect> imageElementContourRectangles = getImageElementContourRectangles(inputImageElements);

    for (Rect elementRectangle : imageElementContourRectangles) {
      boolean contained = false;
      Rect closestStave = null;
      int rangeToClosestStave = 9999;
      for (Rect staveRectangle : imageStaveContourRectangles) {
        if (rectanglesIntersect(elementRectangle, staveRectangle)) {
          contained = true;
          elementRectangle.height =
                  Math.max(elementRectangle.y + elementRectangle.height, staveRectangle.y + staveRectangle.height)
                          - Math.min(elementRectangle.y, staveRectangle.y) + 4;
          elementRectangle.y = Math.min(elementRectangle.y, staveRectangle.y) - 2;
        } else {
          int distance;
          if (elementRectangle.y > staveRectangle.y + staveRectangle.height) {
            distance = elementRectangle.y - staveRectangle.y - staveRectangle.height;
          } else {
            distance = staveRectangle.y - elementRectangle.y;
          }
          if (distance < rangeToClosestStave) {
            rangeToClosestStave = distance;
            closestStave = staveRectangle;
          }
        }
      }
      if (!contained && closestStave != null) {
        elementRectangle.height =
                Math.max(elementRectangle.y + elementRectangle.height, closestStave.y + closestStave.height)
                        - Math.min(elementRectangle.y, closestStave.y) + 4;
        elementRectangle.y = Math.min(elementRectangle.y, closestStave.y) - 2;
      }
      elementRectangle.x = elementRectangle.x - 2;
      elementRectangle.width = elementRectangle.width + 4;
    }
    return imageElementContourRectangles;
  }
}
