package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.smithcarlson.partograph.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.Canvas.LineCapStyle;
import com.smithcarlson.partograph.Canvas.LinePattern;
import com.smithcarlson.partograph.Canvas.Orientation;
import com.smithcarlson.partograph.Canvas.VerticalAlignment;

// TODO -- extract out a canvas -- work off that & abstract away PDF details.
// Also, use various canvas types to support different output forms (e.g. js canvas, ios, svg etc.)
// TODO complete transition to using PDCanvas.
// TODO then abstract away to using Canvas.
// TODO abstract away PDFont!
public class Partograph {
  PDFont bodyFont = PDType1Font.HELVETICA;
  PDFont fontBold = PDType1Font.HELVETICA_BOLD;

  private final float top = 2.0f;
  private final float heightPerCM = 0.3f;
  private final float bottom = top + (6 * heightPerCM);
  private final float centerY = (top + bottom) / 2f;

  private final int hours = 20;
  private final float left = 1.5f;
  private final float right = 6.5f;
  private final float centerX = (left + right) / 2f;
  private final float width = right - left;
  private final float widthPerHour = width / hours;

  private final float titleFontSize = 10f;
  private final float headingFontSize = 8f;
  private final float bodyFontSize = 7f;

  private final Color ALERT_AREA_COLOR = new Color(1f, 1f, 0.4f, 0.1f);
  private final Color ACTION_AREA_COLOR = new Color(1f, 0.4f, 0.4f, 0.1f);
  private final Color DYSTOCIA_LINE_COLOR = new Color(0.5f, 0.5f, 1f, 0.1f);

  private float[] durations;
  private String title;

  // TODO work off Canvas
  public void render(PDCanvas canvas) throws IOException {
    try {
      canvas.write(title, Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
          VerticalAlignment.BOTTOM, centerX, top - 0.5f, fontBold, titleFontSize, Color.BLACK);

      drawDystociaAlertPolygon(canvas);
      drawDystociaActionPolygon(canvas);

      drawLeftYAxis(canvas);
      labelLeftYAxis(canvas);

      drawRightYAxis(canvas);
      labelRightAxis(canvas);

      drawHorizGridLines(canvas);
      drawVertGridLines(canvas);

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
    float x = left, y = bottom;

    for (int i = 0; i < 6; i++) {
      // h line
      points.addPoint(x, y);
      x = x + (widthPerHour * durations[i]);
      points.addPoint(x, y);

      // v offset
      y -= heightPerCM;
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
    float box_y1 = top - 0.4f;
    float box_y2 = top - 0.13f;

    canvas.drawBox(box_x1, box_x2, box_y1, box_y2, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.ROUND);
    canvas.write("Dystocia", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, points.getCurrentX(), top - 0.3f, bodyFont, headingFontSize,
        Color.BLACK);
    canvas.write("Line", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, points.getCurrentX(), top - 0.15f, bodyFont, headingFontSize,
        Color.BLACK);
  }

  private void drawDystociaAlertPolygon(PDCanvas canvas) throws IOException {
    canvas.getContentStream().setNonStrokingColor(ALERT_AREA_COLOR);
    PointList points = createDystociaSequence();
    points.addPoint(left, top);
    points.addPoint(left, bottom);

    canvas.getContentStream().setLineJoinStyle(1);
    canvas.getContentStream().fillPolygon(points.getXPositions(), points.getYPositions());
  }

  private void drawDystociaActionPolygon(PDCanvas canvas) throws IOException {
    canvas.getContentStream().setNonStrokingColor(ACTION_AREA_COLOR);
    PointList points = createDystociaSequence();
    points.addPoint(right, top);
    points.addPoint(right, bottom);
    points.addPoint(left, bottom);

    canvas.getContentStream().setLineJoinStyle(1);
    canvas.getContentStream().fillPolygon(points.getXPositions(), points.getYPositions());
  }

  private void drawHorizGridLines(PDCanvas canvas) throws IOException {
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (heightPerCM * i);
      canvas.drawLine(left - 0.1f, y, right + 0.1f, y, 0.5f, LinePattern.SOLID, Color.BLACK,
          LineCapStyle.ROUND);
    }
  }

  private void drawVertGridLines(PDCanvas canvas) throws IOException {
    // X axis
    int vertLines = (hours * 4) + 1;
    for (int i = 0; i < vertLines; i++) {
      int mod = i % 4;
      float x = left + ((i * widthPerHour) / 4f);
      switch (mod) {
      case 0:
        canvas.drawLine(x, top, x, bottom + 0.3f, 1f, LinePattern.SOLID, Color.BLACK,
            LineCapStyle.BUTT);
        break;
      case 2:
        canvas.drawLine(x, top, x, bottom + 0.1f, 0.5f, LinePattern.DASH_DOT_DOT, Color.GRAY,
            LineCapStyle.BUTT);
        break;
      case 1:
      case 3:
        canvas.drawLine(x, top, x, bottom, 0.5f, LinePattern.DOTTED, Color.GRAY, LineCapStyle.BUTT);
        break;
      }
    }
    canvas.write("Time", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.TOP, 4.0f, 6.0f, bodyFont, headingFontSize, Color.BLACK);
  }

  private void drawRightYAxis(PDCanvas canvas) throws IOException {
    String[] rightYAxis = new String[] { "-3 or higher", "-2", "-1", "0", "+1", "+2", "+3 or lower" };
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (heightPerCM * i);
      canvas.write(rightYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
          VerticalAlignment.CENTER, right + 0.2f, y, bodyFont, bodyFontSize, Color.BLACK);
    }
  }

  private void drawLeftYAxis(PDCanvas canvas) throws IOException {
    String[] leftYAxis = new String[] { "10", "9", "8", "7", "6", "Direct Start 5",
        "Earlier Start 4" };
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (heightPerCM * i);
      canvas.write(leftYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.RIGHT,
          VerticalAlignment.CENTER, left - 0.2f, y, bodyFont, bodyFontSize, Color.BLACK);
    }
  }

  private void labelRightAxis(PDCanvas canvas) throws IOException {
    float line1XPos = right + 1.0f;
    String line1Text = "Fetal Station";
    canvas.write(line1Text, Orientation.TOP_TO_BOTTOM, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, line1XPos, centerY, bodyFont, headingFontSize, Color.BLACK);
    float bufferAroundText = 0.15f;
    float line1Width = bodyFontSize * bodyFont.getStringWidth(line1Text) / 72000f;

    canvas.write("[Plot O]", Orientation.TOP_TO_BOTTOM, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, right + 0.8f, centerY, bodyFont, headingFontSize, Color.BLACK);


    float fontHeight = headingFontSize / (72f);
    float leftmost = line1XPos + (1.2f * fontHeight);
    float rightmost = line1XPos - (1.5f * fontHeight / 3f);
    float rangeCenter = (leftmost + rightmost) / 2f;

    canvas.drawLine(leftmost, top, rightmost, top, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, top, rangeCenter, centerY - ((line1Width / 2) + bufferAroundText),
        0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);

    canvas.drawLine(leftmost, bottom, rightmost, bottom, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, centerY + ((line1Width / 2) + bufferAroundText), rangeCenter,
        bottom, 0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
}

  private void labelLeftYAxis(PDCanvas canvas) throws IOException {
    float line1XPos = left - 1.0f;
    String line1Text = "Cervical Dilation";
    canvas.write(line1Text, Orientation.BOTTOM_TO_TOP, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, line1XPos, centerY, bodyFont, headingFontSize, Color.BLACK);
    float bufferAroundText = 0.15f;
    float line1Width = bodyFontSize * bodyFont.getStringWidth(line1Text) / 72000f;

    canvas.write("[Plot X]", Orientation.BOTTOM_TO_TOP, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, left - 0.8f, centerY, bodyFont, headingFontSize, Color.BLACK);

    float fontHeight = headingFontSize / (72f);
    float leftmost = line1XPos - (1.2f * fontHeight);
    float rightmost = line1XPos + (1.5f * fontHeight / 3f);
    float rangeCenter = (leftmost + rightmost) / 2f;

    canvas.drawLine(leftmost, top, rightmost, top, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, top, rangeCenter, centerY - ((line1Width / 2) + bufferAroundText),
        0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);

    canvas.drawLine(leftmost, bottom, rightmost, bottom, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.BUTT);
    canvas.drawLine(rangeCenter, centerY + ((line1Width / 2) + bufferAroundText), rangeCenter,
        bottom, 0.5f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT);
  }
}
