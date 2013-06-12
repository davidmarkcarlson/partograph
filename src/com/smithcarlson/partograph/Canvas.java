package com.smithcarlson.partograph;

/**
 * Represents an abstract rendering surface.
 *
 * @author dcarlson
 */
public interface Canvas {
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

}
