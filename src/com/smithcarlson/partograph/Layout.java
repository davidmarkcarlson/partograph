package com.smithcarlson.partograph;

import com.smithcarlson.partograph.general.Font;
import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.general.TypeSetter;

public interface Layout<T> {

  TypeSetter<T> baseLineSetter();
  TypeSetter<T> bodySetter();
  Pen dashDotDotGridLine();

  Pen dottedGridLine();
  Font<T> getBodyFont();
  int getCmCount();
  float getHalfHourGridLineBottom();
  Font<T> getHeadingFont();
  float getHourBoxCenterY();

  float getHourBoxSize();
  float getHourGridLineBottom();
  int getHours();
  int getLinesPerHour();

  float getPartographBottom();
  float getPartographCenterX();


  float getPartographCenterY();
  float getPartographHeight();
  float getPartographLeft();
  float getPartographRight();

  float getPartographTop();
  float getPartographWidth();
  float getPartographXForHour(float hour);
  float getPartographYForPosition(float position);

  float getQuarterHourGridLineBottom();
  float getTimeWorksheetLabelY();
  float getTimeWriteInBottom();
  float getTimeWriteInCenterY();

  float getTimeWriteInTop();
  Font<T> getTitleFont();
  float getVerticalLineXForLine(int line);

  float getXAxisLabelY();
  TypeSetter<T> headingSetter();

  Pen heavyLine();
  Pen heavyLineFS();
  float hLineLeft();
  float hLineRight();

  float hLineY(int i);
  float leftYAxisHashLabelX();
  Pen lightLine();
  Pen lightLineFS();
  float rightYAxisHashLabelX();
  TypeSetter<T> titleSetter();
}
