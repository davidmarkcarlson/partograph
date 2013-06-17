package com.smithcarlson.partograph.general;

import java.io.IOException;

public interface Font<T> {

  float getAscenderHeight();

  float getCenterY();

  float getDescender();

  float getDescenderHeight();

  float getLineHeight();

  float getSizePts();

  float getStringWidth(String line1Text) throws IOException;

  float getXHeight();
}
