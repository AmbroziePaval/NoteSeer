package com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.recognition;

import android.content.res.Resources;

import com.example.ambrozie.learnandroidopencv.R;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.ResponseData;
import com.example.ambrozie.learnandroidopencv.opencv_mn_utils.model.training.SampleData;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Ambrozie on 5/11/2016.
 */
public class RecognitionDataLoader {
  private Resources resources;

  public RecognitionDataLoader(Resources resources) {
    this.resources = resources;
  }

  public String getRecognitionResponseData(ResponseData responseData) {
    Serializer serializer = new Persister();
    String xml;
    OutputStream outputStream = new OutputStream() {
      StringBuilder stringBuilder = new StringBuilder();

      @Override
      public void write(int oneByte) throws IOException {
        this.stringBuilder.append((char) oneByte);
      }

      public String toString() {
        return this.stringBuilder.toString();
      }
    };

    try {
      serializer.write(responseData, outputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return outputStream.toString();
  }

  public String getRecognitionSampleData(SampleData sampleData) {
    Serializer serializer = new Persister();
    String xml;
    OutputStream outputStream = new OutputStream() {
      StringBuilder stringBuilder = new StringBuilder();

      @Override
      public void write(int oneByte) throws IOException {
        this.stringBuilder.append((char) oneByte);
      }

      public String toString() {
        return this.stringBuilder.toString();
      }
    };

    try {
      serializer.write(sampleData, outputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return outputStream.toString();
  }

  public SampleData loadRecognitionSampleData() {
    Serializer serializer = new Persister();

    try {
//      SampleData sampleData = serializer.read(SampleData.class, source);
      return serializer.read(SampleData.class, resources.openRawResource(R.raw.recognition_sample_data));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseData loadRecognitionResponseData() {
    Serializer serializer = new Persister();

    try {
      return serializer.read(ResponseData.class, resources.openRawResource(R.raw.recognition_response_data));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
