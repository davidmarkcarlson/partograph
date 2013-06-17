package com.smithcarlson.partograph.general;

import java.awt.Color;
import java.io.IOException;

import com.smithcarlson.partograph.general.Canvas.LineCapStyle;
import com.smithcarlson.partograph.general.Canvas.LinePattern;

public class Pen {
  private final float lineThickness;
  private final Color color;
  private final Canvas.LinePattern linePattern;
  private final Canvas.LineCapStyle lineCapStyle;

  public Pen(float lineThickness, Color color, LinePattern linePattern, LineCapStyle lineCapStyle) {
    super();
    this.lineThickness = lineThickness;
    this.color = color;
    this.linePattern = linePattern;
    this.lineCapStyle = lineCapStyle;
  }

  public <T> void drawLine(float x1, float y1, float x2, float y2, Canvas<T> c) throws IOException {
    c.drawLine(x1, y1, x2, y2, lineThickness, linePattern, color, lineCapStyle);
  }

  public float getLineThickness() {
    return lineThickness;
  }

  public Color getColor() {
    return color;
  }

  public Canvas.LinePattern getLinePattern() {
    return linePattern;
  }

  public Canvas.LineCapStyle getLineCapStyle() {
    return lineCapStyle;
  }

  public <T> void drawBox(float box_x1, float box_x2, float box_y1, float box_y2, Canvas<T> canvas)
      throws IOException {
    canvas
        .drawLine(box_x1, box_y1, box_x2, box_y1, lineThickness, linePattern, color, lineCapStyle);
    canvas
        .drawLine(box_x1, box_y1, box_x1, box_y2, lineThickness, linePattern, color, lineCapStyle);
    canvas
        .drawLine(box_x1, box_y2, box_x2, box_y2, lineThickness, linePattern, color, lineCapStyle);
    canvas
        .drawLine(box_x2, box_y1, box_x2, box_y2, lineThickness, linePattern, color, lineCapStyle);
  }

  public <T> void drawBoxAround(float x, float y, float width, float height, Canvas<T> canvas)
      throws IOException {
    drawBox(x - (width / 2f), x + (width / 2f), y - (height / 2f), y + (height / 2f), canvas);
  }

  public Pen with(Color newColor) {
    return new Pen(lineThickness, newColor, linePattern, lineCapStyle);
  }
}
