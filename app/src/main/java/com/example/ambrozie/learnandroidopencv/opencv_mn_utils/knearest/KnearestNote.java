package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.knearest;

import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseMat;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseNote;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleMat;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SamplePixel;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ambrozie on 5/5/2016.
 */
public class KnearestNote {
  private int samplesCount;
  private List<SampleMat> sampleMatList;
  private List<ResponseMat> responseNoteMatsList;

  public KnearestNote() {
    samplesCount = 0;
    sampleMatList = new ArrayList<>();
    responseNoteMatsList = new ArrayList<>();
  }

  public void train(SampleMat sampleMat, ResponseMat responseMat) {
    samplesCount++;
    sampleMatList.add(sampleMat);
//    sampleMatList.add(samplesCount, sampleMat);
    responseNoteMatsList.add(responseMat);
//    responseNoteMatsList.add(samplesCount, responseMat);
  }

  public ResponseMat findNearest(Mat noteMat) {
    int poz = 0, difference, currentPoz;

    difference = calculateDiff(noteMat, poz);
    currentPoz = poz;
    poz++;
    while (difference != 0 && poz < samplesCount) {
      int newDiff = calculateDiff(noteMat, poz);
      if (newDiff < difference) {
        difference = newDiff;
        currentPoz = poz;
      }
      poz++;
    }

    return responseNoteMatsList.get(currentPoz);
  }

  private int calculateDiff(Mat noteMat, int poz) {
    int diff = 0;
    SampleMat sampleMat = sampleMatList.get(poz);
    for (SamplePixel samplePixel : sampleMat.getSamplePixelList()) {
      int row = samplePixel.getRow();
      int col = samplePixel.getCol();
      double[] noteValues = noteMat.get(row, col);

      diff += Math.abs(samplePixel.getV() - noteValues[0]);
    }
    return diff;
  }
}
