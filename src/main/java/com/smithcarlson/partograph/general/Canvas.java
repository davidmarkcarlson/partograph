package com.smithcarlson.partograph.general;

import java.awt.Color;
import java.io.IOException;

/**
 * Represents an abstract rendering surface.
 *
 * @author dcarlson
 *
 *         TODO switch to using a pen (for lines). TODO maybe similar on
 *         strings.
 */
public interface Canvas<T> {
  enum HorizontalAlignment {
    CENTER, LEFT, RIGHT
  }

  enum Line {
    TOP, HORIZ_CENTER, BOTTOM, LEFT, VERT_CENTER, RIGHT
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
    BOTTOM, BASELINE, CENTER, TOP
  }

  public static final float BOTTOM_TO_TOP = (float) Math.PI * 0.5f, LEFT_TO_RIGHT = 0,
      TOP_TO_BOTTOM = (float) Math.PI * -0.5f;

  void drawLine(float x1, float y1, float x2, float y2, float lineWidth, LinePattern linePattern,
      Color color, LineCapStyle capStyle) throws IOException;

  void fillPolygon(PointList points, Color fillColor) throws IOException;

  float width(String text, Font<T> font) throws IOException;

  void write(String string, float x, float y, float xShift, float yShift, float angle,
      Font<T> font, Color color) throws IOException;

  float xShift(HorizontalAlignment halign, String text, Font<T> font) throws IOException;

  float yShift(VerticalAlignment valign, Font<T> font) throws IOException;

}
