package com.smithcarlson.partograph;

import com.smithcarlson.partograph.general.Font;

public interface Layout<T> {

  Font<T> getBodyFont();
  float getBoxSide();
  int getCmCount();

  Font<T> getHeadingFont();

  float getHeightPerCm();
  float getHLineLeft();
  float getHLineRight();
  float getHLineY(int i);
  int getHours();

  float getPartographBottom();
  float getPartographCenterX();
  float getPartographCenterY();
  float getPartographHeight();
  float getPartographLeft();
  float getPartographRight();

  float getPartographTop();
  float getPartographWidth();
  float getSpaceToHourBoxes();

  float getTimeSpacing();
  Font<T> getTitleFont();
  float getWidthPerHour();

}
