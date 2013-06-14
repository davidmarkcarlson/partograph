package com.smithcarlson.partograph;

import com.smithcarlson.partograph.general.Font;

public interface Layout<T> {

  Font<T> getTitleFont();
  Font<T> getHeadingFont();
  Font<T> getBodyFont();

  float getLightLineWeight();
  float getHeavyLineWeight();

  int getCmCount();
  int getLinesPerHour();


  float getHeightPerCm();
  float getHLineLeft();
  float getHLineRight();
  float getHLineY(int i);
  int getHours();

  float getPartographTop();
  float getPartographBottom();
  float getPartographCenterY();
  float getPartographHeight();

  float getPartographLeft();
  float getPartographRight();
  float getPartographCenterX();
  float getPartographWidth();

  float getVerticalLineXFor(int i);

  float getHourGridLineBottom();
  float getHalfHourGridLineBottom();
  float getQuarterHourGridLineBottom();

  float getTimeWriteInTop();
  float getTimeWriteInBottom();

  @Deprecated
  float getWidthPerHour();
  float getHourBoxCenterY();
  float getHourBoxSize();
  float getLeftYAxisHashLabelX();
  float getRightYAxisHashLabelX();
}
