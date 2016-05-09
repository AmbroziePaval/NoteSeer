package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training;

import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/**
 * Created by Ambrozie on 5/4/2016.
 */
public class ResponseMat extends Mat{
  private String name;
  private String type;
  private ResponseNote responseNote;

  public ResponseMat(long addr) {
    super(addr);
  }

  public ResponseMat() {
  }

  public ResponseMat(int rows, int cols, int type) {
    super(rows, cols, type);
  }

  public ResponseMat(Size size, int type) {
    super(size, type);
  }

  public ResponseMat(int rows, int cols, int type, Scalar s) {
    super(rows, cols, type, s);
  }

  public ResponseMat(Size size, int type, Scalar s) {
    super(size, type, s);
  }

  public ResponseMat(Mat m, Range rowRange, Range colRange) {
    super(m, rowRange, colRange);
  }

  public ResponseMat(Mat m, Range rowRange) {
    super(m, rowRange);
  }

  public ResponseMat(Mat m, Rect roi) {
    super(m, roi);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ResponseNote getResponseNote() {
    return responseNote;
  }

  public void setResponseNote(ResponseNote responseNote) {
    this.responseNote = responseNote;
  }
}
