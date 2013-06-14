package com.smithcarlson.partograph.general;

import java.awt.Color;
import java.io.IOException;

/**
 * Represents an abstract rendering surface.
 *
 * @author dcarlson
 */
public interface Canvas<T> {
  enum HorizontalAlignment {
    CENTER, LEFT, RIGHT
  }

  enum LineCapStyle {
    BUTT, PROJECTING_SQUARE, ROUND
  }

  enum LinePattern {
    DASH_DOT_DOT, DOTTED, SOLID
  }

  enum Orientation {
    BOTTOM_TO_TOP, LEFT_TO_RIGHT, TOP_TO_BOTTOM
  }

  public static enum State {
    CLOSED, OPEN
  }

  enum VerticalAlignment {
    BOTTOM, CENTER, TOP
  }

  void drawLine(float x1, float y1, float x2, float y2, float lineWidth, LinePattern linePattern,
      Color color, LineCapStyle capStyle) throws IOException;

  void drawBox(float x1, float x2, float y1, float y2, float lineWidth, LinePattern linePattern,
      Color color, LineCapStyle capStyle) throws IOException;

  void drawBoxAround(float x, float y, float width, float height, float heavyLineWeight,
      LinePattern solid, Color black, LineCapStyle projectingSquare) throws IOException;

  void fillPolygon(PointList points, Color fillColor) throws IOException;

  void write(String string, Orientation orientation, HorizontalAlignment halign,
      VerticalAlignment valign, float x, float y, Font<T> font, Color color) throws IOException;
}
