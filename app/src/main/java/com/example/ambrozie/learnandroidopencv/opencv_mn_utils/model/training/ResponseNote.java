package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Ambrozie on 4/23/2016.
 */

@Root(name = "note")
public class ResponseNote {
  @Element(name = "step")
  private String step;
  @Element(name = "octave")
  private Integer octave;
  @Element(name = "type")
  private String type;

  public ResponseNote() {
  }

  public ResponseNote(String step, Integer octave, String type) {
    this.step = step;
    this.octave = octave;
    this.type = type;
  }

  public boolean toBePlayed() {
    if(type.equals("quarter"))
      return true;
    return false;
  }

  public String getStep() {
    return step;
  }

  public void setStep(String step) {
    this.step = step;
  }

  public Integer getOctave() {
    return octave;
  }

  public void setOctave(Integer octave) {
    this.octave = octave;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
