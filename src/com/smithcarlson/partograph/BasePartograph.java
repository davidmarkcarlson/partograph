package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import com.smithcarlson.partograph.general.Canvas;
import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.LineCapStyle;
import com.smithcarlson.partograph.general.Canvas.LinePattern;
import com.smithcarlson.partograph.general.Canvas.Orientation;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;

//TODO lots of cleanup!
//TODO move layout calculations into the layout class.
//TODO switch to box variant for layout within (writeAt and writeIn?)/include range logic
public class BasePartograph<T> {

  Layout<T> layout;

  void drawHorizGridLines(Canvas<T> canvas) throws IOException {
    for (int i = 0; i < 7; i++) {
      float y = layout.getPartographTop() + (layout.getHeightPerCm() * i);
      canvas.drawLine(layout.getPartographLeft() - 0.1f, y, layout.getPartographRight() + 0.1f, y,
          0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.PROJECTING_SQUARE);
    }
    canvas.drawLine(layout.getPartographLeft() - 0.1f, layout.getPartographBottom() + 0.75f,
        layout.getPartographRight(), layout.getPartographBottom() + 0.75f, 1f, LinePattern.SOLID,
        Color.BLACK, LineCapStyle.PROJECTING_SQUARE);
  }

  void drawVertGridLines(Canvas<T> canvas) throws IOException {
    // X axis
    int vertLines = (layout.getHours() * 4) + 1;
    for (int i = 0; i < vertLines; i++) {
      int mod = i % 4;
      float x = layout.getPartographLeft() + ((i * layout.getWidthPerHour()) / 4f);
      switch (mod) {
      case 0:
        canvas.drawLine(x, layout.getPartographTop(), x, layout.getPartographBottom() + 0.25f, 1f,
            LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
        canvas.drawBox(x - 0.075f, x + 0.075f, layout.getPartographBottom() + 0.25f,
            layout.getPartographBottom() + 0.4f, 1f, LinePattern.SOLID, Color.BLACK,
            LineCapStyle.PROJECTING_SQUARE);
        canvas.drawLine(x, layout.getPartographBottom() + 0.4f, x,
            layout.getPartographBottom() + 0.75f, 1f, LinePattern.SOLID, Color.BLACK,
            LineCapStyle.BUTT);
        break;
      case 2:
        canvas.drawLine(x, layout.getPartographTop(), x, layout.getPartographBottom() + 0.1f, 0.5f,
            LinePattern.DASH_DOT_DOT, Color.GRAY, LineCapStyle.BUTT);
        canvas.drawLine(x, layout.getPartographBottom() + 0.4f, x,
            layout.getPartographBottom() + 0.75f, 0.5f, LinePattern.DASH_DOT_DOT, Color.GRAY,
            LineCapStyle.BUTT);
        break;
      case 1:
      case 3:
        canvas.drawLine(x, layout.getPartographTop(), x, layout.getPartographBottom(), 0.5f,
            LinePattern.DOTTED, Color.GRAY, LineCapStyle.BUTT);
        canvas.drawLine(x, layout.getPartographBottom() + 0.4f, x,
            layout.getPartographBottom() + 0.75f, 0.5f, LinePattern.DOTTED, Color.GRAY,
            LineCapStyle.BUTT);
        break;
      }
    }
    canvas.write("Time (hrs)", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.TOP, layout.getPartographCenterX(), layout.getPartographBottom() + 0.75f
            + 1.5f * layout.getHeadingFont().getLineHeight(), layout.getHeadingFont(), Color.BLACK);
  }

  void drawRightYAxis(Canvas<T> canvas) throws IOException {
    String[] rightYAxis = new String[] { "-3 or higher", "-2", "-1", "0", "+1", "+2", "+3 or lower" };
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (layout.getHeightPerCm() * i);
      canvas.write(rightYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
          VerticalAlignment.CENTER, layout.getPartographRight() + 0.2f, y, layout.getBodyFont(),
          Color.BLACK);
    }
  }

  void drawLeftYAxis(Canvas<T> canvas) throws IOException {
    String[] leftYAxis = new String[] { "10", "9", "8", "7", "6", "Direct Start 5",
        "Earlier Start 4" };
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (layout.getHeightPerCm() * i);
      canvas.write(leftYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.RIGHT,
          VerticalAlignment.CENTER, layout.getPartographLeft() - 0.2f, y, layout.getBodyFont(),
          Color.BLACK);
    }
    canvas.write("Hour", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.RIGHT,
        VerticalAlignment.BOTTOM, layout.getPartographLeft() - 0.2f,
        layout.getPartographBottom() + 0.35f, layout.getBodyFont(), Color.BLACK);
    canvas.write("Time", Orientation.BOTTOM_TO_TOP, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, layout.getPartographLeft() - 0.2f,
        layout.getPartographBottom() + 0.575f, layout.getBodyFont(), Color.BLACK);
  }

  void labelRightAxis(Canvas<T> canvas) throws IOException {
    float line1XPos = layout.getPartographRight() + 1.0f;
    String line1Text = "Fetal Station";
    canvas.write(line1Text, Orientation.TOP_TO_BOTTOM, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, line1XPos, layout.getPartographCenterY(),
        layout.getHeadingFont(), Color.BLACK);
    float bufferAroundText = 0.15f;
    float line1Width = layout.getHeadingFont().getStringWidth(line1Text);

    canvas.write("[Plot O]", Orientation.TOP_TO_BOTTOM, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, layout.getPartographRight() + 0.8f,
        layout.getPartographCenterY(), layout.getHeadingFont(), Color.BLACK);

    float leftmost = line1XPos + (1.2f * layout.getHeadingFont().getLineHeight());
    float rightmost = line1XPos - (1.5f * layout.getHeadingFont().getLineHeight() / 3f);
    float rangeCenter = (leftmost + rightmost) / 2f;

    canvas.drawLine(leftmost, layout.getPartographTop(), rightmost, layout.getPartographTop(),
        0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, layout.getPartographTop(), rangeCenter,
        layout.getPartographCenterY() - ((line1Width / 2) + bufferAroundText), 0.5f,
        LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);

    canvas.drawLine(leftmost, layout.getPartographBottom(), rightmost,
        layout.getPartographBottom(), 0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, layout.getPartographCenterY()
        + ((line1Width / 2) + bufferAroundText), rangeCenter, layout.getPartographBottom(), 0.5f,
        LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
  }

  void labelLeftYAxis(Canvas<T> canvas) throws IOException {
    float line1XPos = layout.getPartographLeft() - 1.0f;
    String line1Text = "Cervical Dilation";
    canvas.write(line1Text, Orientation.BOTTOM_TO_TOP, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, line1XPos, layout.getPartographCenterY(),
        layout.getHeadingFont(), Color.BLACK);
    float bufferAroundText = 0.15f;
    float line1Width = layout.getHeadingFont().getStringWidth(line1Text);

    canvas.write("[Plot X]", Orientation.BOTTOM_TO_TOP, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, layout.getPartographLeft() - 0.8f, layout.getPartographCenterY(),
        layout.getHeadingFont(), Color.BLACK);

    float leftmost = line1XPos - (1.2f * layout.getHeadingFont().getLineHeight());
    float rightmost = line1XPos + (layout.getHeadingFont().getLineHeight() / 2f);
    float rangeCenter = (leftmost + rightmost) / 2f;

    canvas.drawLine(leftmost, layout.getPartographTop(), rightmost, layout.getPartographTop(),
        0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, layout.getPartographTop(), rangeCenter,
        layout.getPartographCenterY() - ((line1Width / 2) + bufferAroundText), 0.5f,
        LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);

    canvas.drawLine(leftmost, layout.getPartographBottom(), rightmost,
        layout.getPartographBottom(), 0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, layout.getPartographCenterY()
        + ((line1Width / 2) + bufferAroundText), rangeCenter, layout.getPartographBottom(), 0.5f,
        LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
  }

  public void setLayout(Layout<T> layout) {
    this.layout = layout;
  }
}
