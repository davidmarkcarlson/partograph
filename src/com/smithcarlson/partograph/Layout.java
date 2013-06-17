package com.smithcarlson.partograph;

import com.smithcarlson.partograph.general.Font;
import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.general.TypeSetter;

public interface Layout<T> {

  Font<T> getTitleFont();
  Font<T> getHeadingFont();
  Font<T> getBodyFont();

  Pen lightLineFS();
  Pen lightLine();
  Pen heavyLine();
  Pen heavyLineFS();
  Pen dottedGridLine();
  Pen dashDotDotGridLine();

  TypeSetter<T> titleSetter();
  TypeSetter<T> headingSetter();
  TypeSetter<T> bodySetter();
  TypeSetter<T> baseLineSetter();

  int getCmCount();
  int getLinesPerHour();


  float hLineLeft();
  float hLineRight();
  float hLineY(int i);
  int getHours();

  float getPartographTop();
  float getPartographBottom();
  float getPartographCenterY();
  float getPartographHeight();

  float getPartographLeft();
  float getPartographRight();
  float getPartographCenterX();
  float getPartographWidth();

  float getVerticalLineXForLine(int line);
  float getPartographXForHour(float hour);
  float getPartographYForPosition(float position);

  float getHourGridLineBottom();
  float getHalfHourGridLineBottom();
  float getQuarterHourGridLineBottom();

  float getTimeWriteInTop();
  float getTimeWriteInBottom();
  float getTimeWriteInCenterY();
  float getTimeWorksheetLabelY();

  @Deprecated
  float getWidthPerHour();
  float getHourBoxCenterY();
  float getHourBoxSize();
  float leftYAxisHashLabelX();
  float rightYAxisHashLabelX();
  float getXAxisLabelY();
}
