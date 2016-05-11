package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.knearest;

import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseData;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseMat;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseNote;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleData;

import java.util.Objects;

/**
 * Created by Ambrozie on 5/11/2016.
 */
public class KnearestUtil {

  public static KnearestNote trainWithData(KnearestNote kNearest, SampleData sampleData, ResponseData responseData) {
    for (int i = 0; i < sampleData.getSampleMatList().size(); i++) {
      ResponseNote responseNote = responseData.getNoteTrainingDataList().get(i);

      ResponseMat responseMat = new ResponseMat();
      // TODO add other note types here
      if(Objects.equals(responseNote.getType(), "quarter")){
        responseMat.setResponseNote(responseNote);
      }
      responseMat.setName(responseNote.getStep());
      responseMat.setType(responseNote.getType());

      kNearest.train(sampleData.getSampleMatList().get(i), responseMat);
    }
    return kNearest;
  }
}
