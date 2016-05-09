package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Ambrozie on 4/26/2016.
 */

@Root(name="pixel")
public class SamplePixel {
  @Attribute(name="row", required = true)
  private int row;
  @Attribute(name="col", required = true)
  private int col;

  @Element(name="v", required = true)
  private int v;

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public int getV() {
    return v;
  }

  public void setV(int r) {
    this.v = r;
  }
}
