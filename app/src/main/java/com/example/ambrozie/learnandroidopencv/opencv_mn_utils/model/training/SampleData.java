package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ambrozie on 4/26/2016.
 */

@Root(name="samples")
public class SampleData {
  @ElementList
  private List<SampleMat> sampleMatList;

  public SampleData(SampleData sampleData) {
    this.sampleMatList = sampleData.getSampleMatList();
  }

  public SampleData() {
    sampleMatList = new ArrayList<>();
  }

  public List<SampleMat> getSampleMatList() {
    return sampleMatList;
  }

  public void setSampleMatList(List<SampleMat> sampleMatList) {
    this.sampleMatList = sampleMatList;
  }
}
