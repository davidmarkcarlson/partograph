package com.smithcarlson.partograph;

import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.general.TypeSetter;

public class BaseLayout<T> {
  int cmCount;
  int hours;
  int linesPerHour;

  Pen lightLine;
  Pen lightLineFS;
  Pen heavyLine;
  Pen heavyLineFS;
  Pen dottedGridLine;
  Pen dashDotDotGridLine;

  TypeSetter<T> titleSetter;
  TypeSetter<T> headingSetter;
  TypeSetter<T> bodySetter;

  float partographTop;
  float partographBottom;
  float partographHeight;
  float partographCenterY;
  float heightPerCm;

  float partographLeft;
  float partographRight;
  float partographWidth;
  float partographCenterX;
  float widthPerHour;

  float xAxisOverhang;
  float xAxisMargin;

  float yAxisOverhang;
  float yAxisMargin;

  float spaceToHourBoxes;
  float halfHourOverhang;
  float hourBoxSize;
  float timeSpace;

}
