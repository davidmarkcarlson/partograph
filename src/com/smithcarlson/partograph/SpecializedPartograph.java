package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import com.smithcarlson.partograph.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.Canvas.LineCapStyle;
import com.smithcarlson.partograph.Canvas.LinePattern;
import com.smithcarlson.partograph.Canvas.Orientation;
import com.smithcarlson.partograph.Canvas.VerticalAlignment;

// Ultimately, use various canvas types to support different output forms (e.g. js canvas, ios, svg etc.)
// TODO then abstract away to using Canvas.
public class SpecializedPartograph {
  private final Color ALERT_AREA_COLOR = new Color(1f, 1f, 0.4f, 0.1f);
  private final Color ACTION_AREA_COLOR = new Color(1f, 0.4f, 0.4f, 0.1f);
  private final Color DYSTOCIA_LINE_COLOR = new Color(0.5f, 0.5f, 1f, 0.1f);

  private float[] durations;
  private String title;
  private final BasePartograph base;

  public SpecializedPartograph() {
    base = new BasePartograph();
  }

  // TODO work off Canvas
  // TODO split into base partograph and one with dystocia lines/regions
  // completed
  public void render(PDCanvas canvas) throws IOException {
    try {
      canvas.write(title, Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
          VerticalAlignment.BOTTOM, base.centerX, base.top - 0.5f, base.fontBold,
          base.titleFontSize, Color.BLACK);

      drawDystociaAlertPolygon(canvas);
      drawDystociaActionPolygon(canvas);

      base.drawLeftYAxis(canvas);
      base.labelLeftYAxis(canvas);

      base.drawRightYAxis(canvas);
      base.labelRightAxis(canvas);

      base.drawHorizGridLines(canvas);
      base.drawVertGridLines(canvas);

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

  float toPoints(float inches) {
    return inches * 72;
  }

  private PointList createDystociaSequence() {
    PointList points = new PointList();
    float x = base.left, y = base.bottom;

    for (int i = 0; i < 6; i++) {
      // h line
      points.addPoint(x, y);
      x = x + (base.widthPerHour * durations[i]);
      points.addPoint(x, y);

      // v offset
      y -= base.heightPerCM;
    }
    points.addPoint(x, y);
    return points;
  }

  private void drawDystociaLine(PDCanvas canvas) throws IOException {
    PointList points = createDystociaSequence();
    points.addPoint(points.getCurrentX(), points.getCurrentY() - 0.12f);
    for (int i = 1; i < points.size(); i++) {
      canvas.drawLine(points.getXAt(i - 1), points.getYAt(i - 1), points.getXAt(i),
          points.getYAt(i), 2f, LinePattern.SOLID, DYSTOCIA_LINE_COLOR,
          LineCapStyle.PROJECTING_SQUARE);
    }

    float box_x1 = points.getCurrentX() - 0.3f;
    float box_x2 = points.getCurrentX() + 0.3f;
    float box_y1 = base.top - 0.4f;
    float box_y2 = base.top - 0.13f;

    canvas.drawBox(box_x1, box_x2, box_y1, box_y2, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.ROUND);
    canvas.write("Dystocia", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, points.getCurrentX(), base.top - 0.3f, base.bodyFont,
        base.headingFontSize, Color.BLACK);
    canvas.write("Line", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, points.getCurrentX(), base.top - 0.15f, base.bodyFont,
        base.headingFontSize, Color.BLACK);
  }

  private void drawDystociaAlertPolygon(PDCanvas canvas) throws IOException {
    canvas.getContentStream().setNonStrokingColor(ALERT_AREA_COLOR);
    PointList points = createDystociaSequence();
    points.addPoint(base.left, base.top);
    points.addPoint(base.left, base.bottom);

    canvas.getContentStream().setLineJoinStyle(1);
    canvas.getContentStream().fillPolygon(points.getXPositions(), points.getYPositions());
  }

  private void drawDystociaActionPolygon(PDCanvas canvas) throws IOException {
    canvas.getContentStream().setNonStrokingColor(ACTION_AREA_COLOR);
    PointList points = createDystociaSequence();
    points.addPoint(base.right, base.top);
    points.addPoint(base.right, base.bottom);
    points.addPoint(base.left, base.bottom);

    canvas.getContentStream().setLineJoinStyle(1);
    canvas.getContentStream().fillPolygon(points.getXPositions(), points.getYPositions());
  }

}
