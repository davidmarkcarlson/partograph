package com.smithcarlson.partograph.general;

import java.io.IOException;

public class LayoutTool<T> {
  private final Pen pen;
  private final TypeSetter<T> typeSetter;

  public LayoutTool(Pen pen, TypeSetter<T> typeSetter) {
    super();
    this.pen = pen;
    this.typeSetter = typeSetter;
  }

  public void writeInBox(String text, float x1, float y1, float x2, float y2, Canvas<T> canvas)
      throws IOException {
    float x = 0f, y = 0f;
    switch (typeSetter.getHalign()) {
    case LEFT:
      x = Math.min(x1, x2);
      break;
    case CENTER:
      x = (x1 + x2) / 2f;
      break;
    case RIGHT:
      x = Math.max(x1, x2);
      break;
    }

    switch (typeSetter.getValign()) {
    case TOP:
      y = Math.min(y1, y2);
      break;
    case CENTER:
      y = (y1 + y2) / 2f;
      break;
    case BASELINE:
    case BOTTOM:
      y = Math.max(y1, y2);
      break;
    }

    typeSetter.transform(typeSetter.getOrientation()).write(text, x, y, canvas);
  }

  public void writeOn(String text, float x1, float y1, float x2, float y2, boolean drawLine,
      Canvas<T> canvas) throws IOException {
    float angle = (float) Math.atan2(y1 - y2, x2 - x1);
    float length = (float) Math.hypot(y1 - y2, x2 - x1);
    float xShift = canvas.xShift(typeSetter.getHalign(), text, typeSetter.getFont());
    float yShift = canvas.yShift(typeSetter.getValign(), typeSetter.getFont());

    float xPos = 0, yPos = 0;
    switch (typeSetter.getHalign()) {
    case LEFT:
      xPos = x1;
      yPos = y1;
      break;
    case CENTER:
      xPos = (x1 + x2) / 2f;
      yPos = (y1 + y2) / 2f;
      break;
    case RIGHT:
      xPos = x2;
      yPos = y2;
      break;
    }

    if (drawLine) {
      float textWidth = canvas.width(text, typeSetter.getFont());
      float margin = canvas.width("m", typeSetter.getFont()) / 2f;

      float elideLength = 0f;
      switch (typeSetter.getHalign()) {
      case LEFT:
        elideLength = textWidth + margin;
        if (length > elideLength) {
          float afterX = x1 + (float) Math.cos(angle) * elideLength;
          float afterY = y1 - (float) Math.sin(angle) * elideLength;
          pen.drawLine(afterX, afterY, x2, y2, canvas);
        }
        break;
      case CENTER:
        elideLength = textWidth + 2 * margin;
        if (length > elideLength) {
          float beforeX = x1 + (float) Math.cos(angle) * ((length - elideLength) / 2);
          float beforeY = y1 - (float) Math.sin(angle) * ((length - elideLength) / 2);
          pen.drawLine(x1, y1, beforeX, beforeY, canvas);

          float afterX = x2 - (float) Math.cos(angle) * ((length - elideLength) / 2);
          float afterY = y2 + (float) Math.sin(angle) * ((length - elideLength) / 2);
          pen.drawLine(afterX, afterY, x2, y2, canvas);
        }
        break;
      case RIGHT:
        elideLength = textWidth + margin;
        if (length > elideLength) {
          float beforeX = x1 + (float) Math.cos(angle) * (length - elideLength);
          float beforeY = y1 - (float) Math.sin(angle) * (length - elideLength);
          pen.drawLine(x1, y1, beforeX, beforeY, canvas);
        }
        break;
      }
    }

    canvas.write(text, xPos, yPos, xShift, yShift, angle, typeSetter.getFont(),
        typeSetter.getColor());
  }
}
