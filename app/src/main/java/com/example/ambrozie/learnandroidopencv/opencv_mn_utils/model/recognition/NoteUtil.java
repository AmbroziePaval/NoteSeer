package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.recognition;

import com.example.ambrozie.learnandroidopencv.R;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseNote;

/**
 * Created by Ambrozie on 5/11/2016.
 */
public class NoteUtil {
  public static int getNoteMp3Resource(ResponseNote responseNote) {
    String note = responseNote.getStep() + responseNote.getOctave();
    if (note.equals("C4")) {
      return R.raw.c4;
    } else if (note.equals("D4")) {
      return R.raw.d4;
    } else if (note.equals("E4")) {
      return R.raw.e4;
    } else if (note.equals("F4")) {
      return R.raw.f4;
    } else if (note.equals("G4")) {
      return R.raw.g4;
    } else if (note.equals("A4")) {
      return R.raw.a4;
    } else if (note.equals("B4")) {
      return R.raw.b4;
    } else if (note.equals("C5")) {
      return R.raw.c5;
    } else {
      return 0;
    }
  }
}
