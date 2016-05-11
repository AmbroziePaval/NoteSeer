package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.notes;

import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseNote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ambrozie on 5/11/2016.
 */
public class SheetPlayer {
  private NotePlayer notePlayer;
  private LinkedList<ResponseNote> sheetNotes;

  public SheetPlayer(NotePlayer notePlayer) {
    this.notePlayer = notePlayer;
    sheetNotes = new LinkedList<>();
  }

  public void addNote(ResponseNote responseNote) {
    sheetNotes.add(responseNote);
  }

  public void playSheet() {
    for (ResponseNote note : sheetNotes) {
      if (note != null && note.toBePlayed()) {
        notePlayer.setResponseNote(note);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(notePlayer);
        try {
          future.get(1, TimeUnit.SECONDS);
          executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
