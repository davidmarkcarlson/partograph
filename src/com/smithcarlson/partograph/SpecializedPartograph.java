package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import com.smithcarlson.partograph.general.Canvas;
import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.LineCapStyle;
import com.smithcarlson.partograph.general.Canvas.LinePattern;
import com.smithcarlson.partograph.general.Canvas.Orientation;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;
import com.smithcarlson.partograph.general.PointList;

// Ultimately, use various canvas types to support different output forms (e.g. js canvas, ios, svg etc.)
// TODO refactor out magic #s./move layout calculations into the layout class.

public class SpecializedPartograph<T> {
  private final Color ALERT_AREA_COLOR = new Color(1f, 1f, 0.4f, 0.1f);
  private final Color ACTION_AREA_COLOR = new Color(1f, 0.4f, 0.4f, 0.1f);
  private final Color DYSTOCIA_LINE_COLOR = new Color(0.5f, 0.5f, 1f, 0.1f);

  private float[] durations;
  private String title;
  private final BasePartograph<T> base;

  public SpecializedPartograph(BasePartograph<T> base) {
    this.base = base;
  }

  public void render(Canvas<T> canvas) throws IOException {
    try {
      canvas.write(title, Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
          VerticalAlignment.BOTTOM, base.layout.getPartographCenterX(), base.layout.getPartographTop() - 0.5f, base.layout.getTitleFont(),
          Color.BLACK);

      drawDystociaAlertPolygon(canvas);
      drawDystociaActionPolygon(canvas);

      base.drawLeftYAxis(canvas);
      base.labelLeftYAxis(canvas);

      base.drawRightYAxis(canvas);
      base.labelRightAxis(canvas);

      base.drawVertGridLines(canvas);
      base.drawHorizGridLines(canvas);

      drawDystociaLine(canvas);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setDurations(float[] durations) {
    this.durations = durations;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  private PointList createDystociaSequence() {
    PointList points = new PointList();
    float x = base.layout.getPartographLeft(), y = base.layout.getPartographBottom();

    for (int i = 0; i < 6; i++) {
      // h line
      points.addPoint(x, y);
      x = x + (base.layout.getWidthPerHour() * durations[i]);
      points.addPoint(x, y);

      // v offset
      y -= base.layout.getHeightPerCm();
    }
    points.addPoint(x, y);
    return points;
  }

  private void drawDystociaLine(Canvas<T> canvas) throws IOException {
    PointList points = createDystociaSequence();
    points.addPoint(points.getCurrentX(), points.getCurrentY() - 0.12f);
    for (int i = 1; i < points.size(); i++) {
      canvas.drawLine(points.getXAt(i - 1), points.getYAt(i - 1), points.getXAt(i),
          points.getYAt(i), 2f, LinePattern.SOLID, DYSTOCIA_LINE_COLOR,
          LineCapStyle.PROJECTING_SQUARE);
    }

    float box_x1 = points.getCurrentX() - 0.3f;
    float box_x2 = points.getCurrentX() + 0.3f;
    float box_y1 = base.layout.getPartographTop() - 0.4f;
    float box_y2 = base.layout.getPartographTop() - 0.13f;

    canvas.drawBox(box_x1, box_x2, box_y1, box_y2, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.ROUND);
    canvas.write("Dystocia", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, points.getCurrentX(), base.layout.getPartographTop() - 0.3f,
        base.layout.getHeadingFont(), Color.BLACK);
    canvas.write("Line", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, points.getCurrentX(), base.layout.getPartographTop() - 0.15f,
        base.layout.getHeadingFont(), Color.BLACK);
  }

  private void drawDystociaAlertPolygon(Canvas<T> canvas) throws IOException {
    PointList points = createDystociaSequence();
    points.addPoint(base.layout.getPartographLeft(), base.layout.getPartographTop());
    points.addPoint(base.layout.getPartographLeft(), base.layout.getPartographBottom());
    canvas.fillPolygon(points, ALERT_AREA_COLOR);
  }

  private void drawDystociaActionPolygon(Canvas<T> canvas) throws IOException {
    PointList points = createDystociaSequence();
    points.addPoint(base.layout.getPartographRight(), base.layout.getPartographTop());
    points.addPoint(base.layout.getPartographRight(), base.layout.getPartographBottom());
    points.addPoint(base.layout.getPartographLeft(), base.layout.getPartographBottom());
    canvas.fillPolygon(points, ACTION_AREA_COLOR);
  }

}
