package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Ambrozie on 4/26/2016.
 */
@Root(name="responses")
public class ResponseData {

  @ElementList
  private List<ResponseNote> noteTrainingDataList;

  public List<ResponseNote> getNoteTrainingDataList() {
    return noteTrainingDataList;
  }

  public void setNoteTrainingDataList(List<ResponseNote> noteTrainingDataList) {
    this.noteTrainingDataList = noteTrainingDataList;
  }
}
