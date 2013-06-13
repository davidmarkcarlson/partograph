package com.smithcarlson.partograph.general;

import java.util.ArrayList;
import java.util.List;

public class PointList {
  List<Float> xPositions = new ArrayList<Float>();
  List<Float> yPositions = new ArrayList<Float>();
  int size = 0;

  public void addPoint(float x, float y) {
    xPositions.add(x);
    yPositions.add(y);
    size++;
  }

  public float getCurrentX() {
    return getXAt(size - 1);
  }

  public float getCurrentY() {
    return getYAt(size - 1);
  }

  public float getXAt(int i) {
    return xPositions.get(i);
  }

  public float[] getXPositions() {
    return toFloatArray(xPositions);
  }

  public float getYAt(int i) {
    return yPositions.get(i);
  }

  public float[] getYPositions() {
    return toFloatArray(yPositions);
  }

  public int size() {
    return size;
  }

  private float[] toFloatArray(List<Float> positions) {
    float[] array = new float[size];
    for (int i = 0; i < size; i++) {
      array[i] = positions.get(i);
    }
    return array;
  }
}
