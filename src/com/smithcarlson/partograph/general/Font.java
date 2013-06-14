package com.smithcarlson.partograph.general;

import java.io.IOException;

public interface Font<T> {

  float getLineHeight();

  float getStringWidth(String line1Text) throws IOException;

  float getAscenderHeight();

  float getSizePts();

  float getDescenderHeight();

}
