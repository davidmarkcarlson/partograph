package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

// TODO -- extract out a canvas -- work off that & abstract away PDF details.
// Also, use various canvas types to support different output forms (e.g. js canvas, ios, svg etc.)
public class Partograph {
  enum HorizontalAlignment {
    CENTER, LEFT, RIGHT
  }

  enum LinePattern {
    DASH_DOT_DOT, DOTTED, SOLID
  }

  enum LineCapStyle {
    BUTT, ROUND, PROJECTING_SQUARE
  }

  enum Orientation {
    BOTTOM_TO_TOP, LEFT_TO_RIGHT, TOP_TO_BOTTOM
  }

  class PointList {
    List<Float> xPositions = new ArrayList<Float>();
    List<Float> yPositions = new ArrayList<Float>();
    int size = 0;

    void addPoint(float x, float y) {
      xPositions.add(toPoints(x));
      yPositions.add(toPoints(11 - y));
      size++;
    }

    float[] getXPositions() {
      return toFloatArray(xPositions);
    }

    float[] getYPositions() {
      return toFloatArray(yPositions);
    }

    float[] toFloatArray(List<Float> positions) {
      float[] array = new float[size];
      for (int i = 0; i < size; i++) {
        array[i] = positions.get(i);
      }
      return array;
    }

    public float getCurrentX() {
      return getXAt(size - 1);
    }

    public float getCurrentY() {
      return getYAt(size - 1);
    }

    public float getXAt(int i) {
      return xPositions.get(i) / 72f;
    }

    public float getYAt(int i) {
      return 11f - (yPositions.get(i) / 72f);
    }

    public int size() {
      return size;
    }
  }

  enum VerticalAlignment {
    BOTTOM, CENTER, TOP
  }

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

  // TODO include offset
  // TODO perhaps work off contentStream?
  public void render(PDDocument document, PDPage page) throws IOException {
    try {
      PDPageContentStream contentStream = new PDPageContentStream(document, page);
      contentStream.setNonStrokingColor(ALERT_AREA_COLOR);
      drawDystociaAlertPolygon(contentStream);
      contentStream.setNonStrokingColor(ACTION_AREA_COLOR);
      drawDystociaActionPolygon(contentStream);
      contentStream.setNonStrokingColor(Color.BLACK);

      write(title, Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM,
          centerX, top - 0.5f, fontBold, titleFontSize, contentStream);
      drawYAxes(contentStream);
      drawXAxis(contentStream);
      contentStream.setStrokingColor(DYSTOCIA_LINE_COLOR);
      drawDystociaLine(contentStream);
      contentStream.setStrokingColor(Color.BLACK);
      contentStream.close();
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

  private void drawDystociaLine(PDPageContentStream contentStream) throws IOException {
    PointList points = createDystociaSequence();
    points.addPoint(points.getCurrentX(), points.getCurrentY() - 0.12f);
    for (int i = 1; i < points.size(); i++) {
      drawLine(points.getXAt(i - 1), points.getYAt(i - 1), points.getXAt(i), points.getYAt(i),
          2f, LinePattern.SOLID, DYSTOCIA_LINE_COLOR, LineCapStyle.PROJECTING_SQUARE, contentStream);
    }

    float box_x1 = points.getCurrentX() - 0.3f;
    float box_x2 = points.getCurrentX() + 0.3f;
    float box_y1 = top - 0.4f;
    float box_y2 = top - 0.13f;

    drawLine(box_x1, box_y1, box_x2, box_y1, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.ROUND, contentStream);
    drawLine(box_x1, box_y1, box_x1, box_y2, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.ROUND, contentStream);
    drawLine(box_x1, box_y2, box_x2, box_y2, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.ROUND, contentStream);
    drawLine(box_x2, box_y1, box_x2, box_y2, 0.5f, LinePattern.SOLID, Color.BLACK,
        LineCapStyle.ROUND, contentStream);
    write("Dystocia", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, points.getCurrentX(), top - 0.3f, bodyFont, headingFontSize,
        contentStream);
    write("Line", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM,
        points.getCurrentX(), top - 0.15f, bodyFont, headingFontSize, contentStream);
  }

  private void drawDystociaAlertPolygon(PDPageContentStream contentStream) throws IOException {
    PointList points = createDystociaSequence();
    points.addPoint(left, top);
    points.addPoint(left, bottom);

    contentStream.setLineJoinStyle(1);
    contentStream.fillPolygon(points.getXPositions(), points.getYPositions());
  }

  private void drawDystociaActionPolygon(PDPageContentStream contentStream) throws IOException {
    PointList points = createDystociaSequence();
    points.addPoint(right, top);
    points.addPoint(right, bottom);
    points.addPoint(left, bottom);

    contentStream.setLineJoinStyle(1);
    contentStream.fillPolygon(points.getXPositions(), points.getYPositions());
  }

  private void drawLine(float x1, float y1, float x2, float y2, float width, LinePattern style,
      Color color, LineCapStyle cap, PDPageContentStream contentStream) throws IOException {
    contentStream.setStrokingColor(color);
    switch (style) {
    case SOLID:
      contentStream.setLineDashPattern(new float[] { 72f * 20, 1.0f }, 0f);
      break;
    case DASH_DOT_DOT:
      contentStream.setLineDashPattern(new float[] { 3.0f, 1.0f, 1.0f, 1.0f }, 1.0f);
      break;
    case DOTTED:
      contentStream.setLineDashPattern(new float[] { 1.0f, 1.0f }, 0f);
      break;
    }
    switch (cap) {
    case BUTT:
      contentStream.setLineCapStyle(0);
      break;
    case ROUND:
      contentStream.setLineCapStyle(1);
      break;
    case PROJECTING_SQUARE:
      contentStream.setLineCapStyle(2);
      break;
    }
    contentStream.setLineWidth(width);
    contentStream.drawLine(toPoints(x1), toPoints(11 - y1), toPoints(x2), toPoints(11 - y2));
  }

  private void drawXAxis(PDPageContentStream contentStream) throws IOException {
    // X axis
    int vertLines = (hours * 4) + 1;
    for (int i = 0; i < vertLines; i++) {
      int mod = i % 4;
      float x = left + ((i * widthPerHour) / 4f);
      switch (mod) {
      case 0:
        drawLine(x, top, x, bottom + 0.3f, 1f, LinePattern.SOLID, Color.BLACK, LineCapStyle.BUTT,
            contentStream);
        break;
      case 2:
        drawLine(x, top, x, bottom + 0.1f, 0.5f, LinePattern.DASH_DOT_DOT, Color.GRAY,
            LineCapStyle.BUTT, contentStream);
        break;
      case 1:
      case 3:
        drawLine(x, top, x, bottom, 0.5f, LinePattern.DOTTED, Color.GRAY, LineCapStyle.BUTT,
            contentStream);
        break;
      }
    }
    write("Time", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER, VerticalAlignment.TOP,
        4.0f, 6.0f, bodyFont, headingFontSize, contentStream);
  }

  private void drawYAxes(PDPageContentStream contentStream) throws IOException {
    // left Y axis
    write("Cervical Dilation", Orientation.BOTTOM_TO_TOP, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, left - 1.0f, centerY, bodyFont, headingFontSize, contentStream);

    write("[Plot X]", Orientation.BOTTOM_TO_TOP, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, left - 0.8f, centerY, bodyFont, headingFontSize, contentStream);

    // right Y axis
    write("Fetal Station", Orientation.TOP_TO_BOTTOM, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, right + 1.0f, centerY, bodyFont, headingFontSize, contentStream);

    write("[Plot O]", Orientation.TOP_TO_BOTTOM, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, right + 0.8f, centerY, bodyFont, headingFontSize, contentStream);

    String[] leftYAxis = new String[] { "10", "9", "8", "7", "6", "Direct Start 5",
        "Earlier Start 4" };
    String[] rightYAxis = new String[] { "-3 or higher", "-2", "-1", "0", "+1", "+2", "+3 or lower" };

    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (heightPerCM * i);
      write(leftYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.RIGHT,
          VerticalAlignment.CENTER, left - 0.2f, y, bodyFont, bodyFontSize, contentStream);
      write(rightYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
          VerticalAlignment.CENTER, right + 0.2f, y, bodyFont, bodyFontSize, contentStream);
      drawLine(left - 0.1f, y, right + 0.1f, y, 0.5f, LinePattern.SOLID, Color.BLACK,
          LineCapStyle.ROUND, contentStream);
    }
  }

  private void write(String string, Orientation orientation, HorizontalAlignment halign,
      VerticalAlignment valign, float x, float y, PDFont font, float fontSize,
      PDPageContentStream contentStream) throws IOException {

    float posx = toPoints(x);
    float posy = toPoints(11 - y);

    float shiftx = 0f, shifty = 0f;
    switch (halign) {
    case CENTER:
      shiftx -= fontSize * (font.getStringWidth(string) / 2000f);
      break;
    case RIGHT:
      shiftx -= fontSize * (font.getStringWidth(string) / 1000f);
      break;
    case LEFT:
      break;
    }

    switch (valign) {
    case CENTER:
      shifty -= fontSize * (font.getFontHeight(new byte[] { 'X' }, 0, 1) / 2000f);
      break;
    case TOP:
      shifty -= fontSize * (font.getFontHeight(new byte[] { 'X' }, 0, 1) / 1000f);
      break;
    case BOTTOM:
      break;
    }

    Matrix shift = Matrix.getTranslatingInstance(shiftx, shifty);

    float angle = 0.0f;
    switch (orientation) {
    case BOTTOM_TO_TOP:
      angle = (float) Math.PI * 0.5f;
      break;
    case TOP_TO_BOTTOM:
      angle = (float) Math.PI * -0.5f;
      break;
    case LEFT_TO_RIGHT:
      break;
    }
    float angleCos = (float) Math.cos(angle);
    float angleSin = (float) Math.sin(angle);

    Matrix rotation = Matrix.getScaleInstance(1f, 1f);
    rotation.setValue(0, 0, angleCos);
    rotation.setValue(0, 1, angleSin);
    rotation.setValue(1, 0, -angleSin);
    rotation.setValue(1, 1, angleCos);

    Matrix position = Matrix.getTranslatingInstance(posx, posy);
    float[][] tx = shift.multiply(rotation).multiply(position).getValues();
    contentStream.beginText();
    contentStream.setFont(font, fontSize);
    contentStream.moveTextPositionByAmount(posx, posy);
    contentStream.setTextMatrix(tx[0][0], tx[0][1], tx[1][0], tx[1][1], tx[2][0], tx[2][1]);
    contentStream.drawString(string);
    contentStream.endText();
  }
}
