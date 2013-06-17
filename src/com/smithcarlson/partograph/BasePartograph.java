package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import com.smithcarlson.partograph.general.Canvas;
import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.Orientation;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;
import com.smithcarlson.partograph.general.LayoutTool;
import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.general.PointList;

public class BasePartograph<T> {

  Layout<T> layout;

  void drawHorizGridLines(Canvas<T> canvas) throws IOException {
    float left = layout.getPartographLeft();
    float right = layout.getPartographRight();
    for (int i = 0; i < layout.getCmCount(); i++) {
      float y = layout.hLineY(i);
      layout.lightLine().drawLine(left, y, right, y, canvas);
    }
  }

  void drawVertGridLines(Canvas<T> canvas) throws IOException {
    Layout<T> l = layout;
    int vertLines = (l.getHours() * 4) + 1;

    float top = l.getPartographTop();
    float bottom = l.getPartographBottom();

    Pen[] penMap = new Pen[] { l.heavyLine(), l.dottedGridLine(), l.dashDotDotGridLine(),
        l.dottedGridLine() };

    for (int i = 0; i < vertLines; i++) {
      float x = layout.getVerticalLineXForLine(i);
      penMap[i % 4].drawLine(x, top, x, bottom, canvas);
    }
  }

  void drawTimelineWorkspace(Canvas<T> canvas) throws IOException {
    Layout<T> l = layout;
    int vertLines = (layout.getHours() * 4) + 1;
    Pen heavyLine = layout.heavyLine();
    Pen dashDotDotLine = layout.dashDotDotGridLine();
    Pen dottedLine = layout.dottedGridLine();

    Pen[] penMap = new Pen[] { heavyLine, dottedLine, dashDotDotLine, dottedLine };

    float top = layout.getPartographBottom();
    float writeInTop = layout.getTimeWriteInTop();
    float writeInBottom = layout.getTimeWriteInBottom();

    for (int i = 0; i < vertLines; i++) {
      float x = layout.getVerticalLineXForLine(i);

      switch (i % 4) {
      case 0:
        heavyLine.drawLine(x, top, x, l.getHourGridLineBottom(), canvas);
        l.heavyLineFS().drawBoxAround(x, l.getHourBoxCenterY(), l.getHourBoxSize(),
            l.getHourBoxSize(), canvas);
        break;
      case 2:
        dashDotDotLine.drawLine(x, top, x, l.getHalfHourGridLineBottom(), canvas);
        break;
      }
    }

    for (int i = 0; i < vertLines; i++) {
      float x = l.getVerticalLineXForLine(i);
      penMap[i % 4].drawLine(x, writeInTop, x, writeInBottom, canvas);
    }

    l.bodySetter().hAlign(HorizontalAlignment.RIGHT)
        .write("Hour", l.leftYAxisHashLabelX(), l.getHourBoxCenterY(), canvas);
    l.bodySetter().orient(Orientation.BOTTOM_TO_TOP).vAlign(VerticalAlignment.BOTTOM)
        .write("Time", l.leftYAxisHashLabelX(), l.getTimeWriteInCenterY(), canvas);

    // bottom line under "Time" label
    l.heavyLineFS().drawLine(l.hLineLeft(), l.getTimeWriteInBottom(), l.getPartographRight(),
        l.getTimeWriteInBottom(), canvas);
    l.headingSetter().vAlign(VerticalAlignment.TOP)
        .write("Time (hrs)", l.getPartographCenterX(), l.getTimeWorksheetLabelY(), canvas);
  }

  void drawLeftYAxis(Canvas<T> canvas) throws IOException {
    Layout<T> l = layout;
    float left = l.hLineLeft();
    float right = l.getPartographLeft();

    for (int i = 0; i < l.getCmCount(); i++) {
      float y = l.hLineY(i);
      l.lightLine().drawLine(left, y, right, y, canvas);
      l.baseLineSetter()
          .hAlign(HorizontalAlignment.RIGHT)
          .write(new String[] { "Earlier Start 4", "Direct Start 5", "6", "7", "8", "9", "10" }[i],
              l.leftYAxisHashLabelX(), y, canvas);
    }
  }

  void drawRightYAxis(Canvas<T> canvas) throws IOException {
    Layout<T> l = layout;
    float left = l.getPartographRight();
    float right = l.hLineRight();
    for (int i = 0; i < l.getCmCount(); i++) {
      float y = l.hLineY(i);
      l.lightLine().drawLine(left, y, right, y, canvas);
      l.baseLineSetter()
          .hAlign(HorizontalAlignment.LEFT)
          .write(new String[] { "+3 or lower", "+2", "+1", "0", "-1", "-2", "-3 or higher" }[i],
              l.rightYAxisHashLabelX(), y, canvas);
    }
  }

  void labelRightAxis(Canvas<T> canvas) throws IOException {
    float top = layout.getPartographTop();
    float bottom = layout.getPartographBottom();

    float line1XPos = layout.getPartographRight() + 1.0f;
    String line1Text = "Fetal Station";
    LayoutTool<T> tool = new LayoutTool<T>(layout.lightLine(), layout.headingSetter());
    tool.writeOn(line1Text, line1XPos, top, line1XPos, bottom, true, canvas);

    float line2XPos = line1XPos - layout.getHeadingFont().getLineHeight();
    tool.writeOn("[Plot O]", line2XPos, top, line2XPos, bottom, false, canvas);

    float left = line1XPos - layout.getHeadingFont().getLineHeight();
    float right = line1XPos + layout.getHeadingFont().getLineHeight();

    layout.lightLine().drawLine(left, top, right, top, canvas);
    layout.lightLine().drawLine(left, bottom, right, bottom, canvas);
  }

  void labelLeftYAxis(Canvas<T> canvas) throws IOException {
    float top = layout.getPartographTop();
    float bottom = layout.getPartographBottom();

    float line1XPos = layout.getPartographLeft() - 1.0f;
    String line1Text = "Cervical Dilation";
    LayoutTool<T> tool = new LayoutTool<T>(layout.lightLine(), layout.headingSetter());
    tool.writeOn(line1Text, line1XPos, bottom, line1XPos, top, true, canvas);

    float line2XPos = line1XPos + layout.getHeadingFont().getLineHeight();
    tool.writeOn("[Plot X]", line2XPos, bottom, line2XPos, top, false, canvas);

    float left = line1XPos - layout.getHeadingFont().getLineHeight();
    float right = line1XPos + layout.getHeadingFont().getLineHeight();

    layout.lightLine().drawLine(left, top, right, top, canvas);
    layout.lightLine().drawLine(left, bottom, right, bottom, canvas);
  }

  public void setLayout(Layout<T> layout) {
    this.layout = layout;
  }

  public PointList createDystociaSequence(float[] durations) {
    PointList points = new PointList();
    float x = layout.getPartographLeft();
    float y = layout.getPartographBottom();

    int increments = layout.getCmCount() - 1;
    float cumulativeTime = 0f;
    points.addPoint(x, y);
    int i = 0;
    while (i < increments) {
      // h line
      cumulativeTime += durations[i];
      x = layout.getPartographXForHour(cumulativeTime);
      points.addPoint(x, y);

      i++;
      // v offset
      y = layout.getPartographYForPosition(i);
      points.addPoint(x, y);
    }
    return points;
  }

  public void drawDystociaActionPolygon(float[] durations, Color color, Canvas<T> canvas)
      throws IOException {
    PointList points = createDystociaSequence(durations);
    points.addPoint(layout.getPartographRight(), layout.getPartographTop());
    points.addPoint(layout.getPartographRight(), layout.getPartographBottom());
    points.addPoint(layout.getPartographLeft(), layout.getPartographBottom());
    canvas.fillPolygon(points, color);
  }

}
