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

  @Element(name="r", required = true)
  private int r;
  @Element(name="g", required = true)
  private int g;
  @Element(name="b", required = true)
  private int b;
  @Element(name="x", required = true)
  private int x;

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

  public int getR() {
    return r;
  }

  public void setR(int r) {
    this.r = r;
  }

  public int getG() {
    return g;
  }

  public void setG(int g) {
    this.g = g;
  }

  public int getB() {
    return b;
  }

  public void setB(int b) {
    this.b = b;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }
}
