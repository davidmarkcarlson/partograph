package com.smithcarlson.partograph;

import java.util.ArrayList;
import java.util.List;

class PointList {
  List<Float> xPositions = new ArrayList<Float>();
  List<Float> yPositions = new ArrayList<Float>();
  int size = 0;

  void addPoint(float x, float y) {
    xPositions.add(toPoints(x));
    yPositions.add(toPoints(11 - y));
    size++;
  }

  // TODO pull out into single location.
  float toPoints(float inches) {
    return inches * 72;
  }

  float[] getXPositions() {
    return toFloatArray(xPositions);
  }

  float[] getYPositions() {
    return toFloatArray(yPositions);
  }

  float[] toFloatArray(List<Float> positions) {
    float[] array = new float[size];
    for (int i = 0; i < size; i++) {
      array[i] = positions.get(i);
    }
    return array;
  }

  public float getCurrentX() {
    return getXAt(size - 1);
  }

  public float getCurrentY() {
    return getYAt(size - 1);
  }

  public float getXAt(int i) {
    return xPositions.get(i) / 72f;
  }

  public float getYAt(int i) {
    return 11f - (yPositions.get(i) / 72f);
  }

  public int size() {
    return size;
  }
}
