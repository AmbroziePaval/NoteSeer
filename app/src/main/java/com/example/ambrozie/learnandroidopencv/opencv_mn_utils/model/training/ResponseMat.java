package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training;

import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/**
 * Created by Ambrozie on 5/4/2016.
 */
public class ResponseMat {
  private String name;
  private String type;
  private ResponseNote responseNote;

  public ResponseMat() {
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
