package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Created by Ambrozie on 4/23/2016.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NoteTrainingData {
  @XmlElement(name="step")
  private String step;
  @XmlElement(name="octave")
  private Integer octave;
  @XmlElement(name="type")
  private String type;

  public NoteTrainingData(String step, Integer octave, String type) {
    this.step = step;
    this.octave = octave;
    this.type = type;
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
