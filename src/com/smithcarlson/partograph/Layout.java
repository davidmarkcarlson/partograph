package com.smithcarlson.partograph;

import com.smithcarlson.partograph.general.Font;

public interface Layout<T> {

  Font<T> getBodyFont();

  Font<T> getHeadingFont();

  Font<T> getTitleFont();

  float getPartographTop();
  float getPartographBottom();
  float getPartographHeight();
  float getPartographCenterY();
  float getHeightPerCm();

  float getPartographLeft();
  float getPartographRight();
  float getPartographWidth();
  float getPartographCenterX();
  int getHours();
  float getWidthPerHour();

  float getXAxisOverhang();
  float getXAxisMargin();

  float getYAxisOverhang();
  float getYAxisMargin();

  float getSpaceToHourBoxes();
  float getBoxSide();
  float getTimeSpacing();

}
