package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Ambrozie on 4/26/2016.
 */

@Root(name="mat")
public class SampleMat {

  @ElementList
  private List<SamplePixel> samplePixelList;

  public List<SamplePixel> getSamplePixelList() {
    return samplePixelList;
  }

  public void setSamplePixelList(List<SamplePixel> samplePixelList) {
    this.samplePixelList = samplePixelList;
  }
}
