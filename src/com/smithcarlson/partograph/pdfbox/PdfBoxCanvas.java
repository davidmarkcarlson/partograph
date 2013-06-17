package com.smithcarlson.partograph.pdfbox;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.util.Matrix;

import com.smithcarlson.partograph.general.Canvas;
import com.smithcarlson.partograph.general.Font;
import com.smithcarlson.partograph.general.PointList;

/**
 * TODO figure out line patterns for real TODO figure out font sizing for real
 * -- improve vertical centering
 *
 * @author dcarlson
 *
 */
public class PdfBoxCanvas implements Canvas<PdfBox> {

  private final PDPageContentStream contentStream;
  private final PDRectangle mediaBox;
  private State state = null;
  private final float offsetX, offsetY;

  private static final Map<LineCapStyle, Integer> LINE_CAPS;
  static {
    Map<LineCapStyle, Integer> map = new HashMap<LineCapStyle, Integer>();
    map.put(LineCapStyle.BUTT, 0);
    map.put(LineCapStyle.ROUND, 1);
    map.put(LineCapStyle.PROJECTING_SQUARE, 2);
    LINE_CAPS = map;
  }

  private static final Map<HorizontalAlignment, Float> HORIZ_SHIFT_MULT;
  static {
    Map<HorizontalAlignment, Float> map = new HashMap<HorizontalAlignment, Float>();
    map.put(HorizontalAlignment.LEFT, 0f);
    map.put(HorizontalAlignment.CENTER, 0.5f);
    map.put(HorizontalAlignment.RIGHT, 1f);
    HORIZ_SHIFT_MULT = map;
  }

  public PdfBoxCanvas(PDPage page, PDPageContentStream contentStream, float atX, float atY)
      throws IOException {
    this.mediaBox = page.findMediaBox();
    this.contentStream = contentStream;
    state = State.OPEN;
    this.offsetX = atX;
    this.offsetY = atY;
  }

  @Override
  public void drawLine(float x1, float y1, float x2, float y2, float width, LinePattern style,
      Color color, LineCapStyle cap) throws IOException {
    if (state != State.OPEN)
      throw new IllegalStateException("Writing to closed canvas");

    contentStream.setStrokingColor(color);
    switch (style) {
    case SOLID:
      contentStream.setLineDashPattern(new float[] { 2000f, 1.0f }, 0f);
      break;
    case DASH_DOT_DOT:
      contentStream.setLineDashPattern(new float[] { 3.0f, 1.0f, 1.0f, 1.0f }, 1.0f);
      break;
    case DOTTED:
      contentStream.setLineDashPattern(new float[] { 1.0f, 1.0f }, 0f);
      break;
    }
    contentStream.setLineCapStyle(LINE_CAPS.get(cap));
    contentStream.setLineWidth(width);
    float _x1 = PdfBox.toPoints(x1 + offsetX);
    float _x2 = PdfBox.toPoints(x2 + offsetX);
    float _y1 = mediaBox.getHeight() - PdfBox.toPoints(y1 + offsetY);
    float _y2 = mediaBox.getHeight() - PdfBox.toPoints(y2 + offsetY);

    contentStream.drawLine(_x1, _y1, _x2, _y2);
  }

  @Override
  public void fillPolygon(PointList points, Color fillColor) throws IOException {
    contentStream.setNonStrokingColor(fillColor);
    float[] xPos = PdfBox.toPoints(points.getXPositions(), offsetX);
    float[] yPos = PdfBox.toPoints(points.getYPositions(), offsetY, mediaBox.getHeight());
    contentStream.fillPolygon(xPos, yPos);
  }

  @Override
  public float width(String text, Font<PdfBox> font) throws IOException {
    if (!(font instanceof PdfBoxFont)) {
      throw new IllegalArgumentException("Bad PdfBoxFont!!");
    }
    return ((PdfBoxFont) font).getStringWidth(text);

  }

  @Override
  public void write(String string, float x, float y, float xShift, float yShift, float angle,
      Font<PdfBox> font, Color color) throws IOException {
    PdfBoxFont _font = (PdfBoxFont) font;

    float posx = PdfBox.toPoints(x + offsetX);
    float posy = mediaBox.getHeight() - PdfBox.toPoints(y + offsetY);

    Matrix shift = Matrix.getTranslatingInstance(PdfBox.toPoints(xShift), PdfBox.toPoints(yShift));

    float angleCos = (float) Math.cos(angle);
    float angleSin = (float) Math.sin(angle);

    Matrix rotation = Matrix.getScaleInstance(1f, 1f);
    rotation.setValue(0, 0, angleCos);
    rotation.setValue(0, 1, angleSin);
    rotation.setValue(1, 0, -angleSin);
    rotation.setValue(1, 1, angleCos);

    Matrix position = Matrix.getTranslatingInstance(posx, posy);
    float[][] tx = shift.multiply(rotation).multiply(position).getValues();

    contentStream.setNonStrokingColor(color);
    contentStream.beginText();
    contentStream.setFont(_font.fontFamily, font.getSizePts());
    contentStream.moveTextPositionByAmount(posx, posy);
    contentStream.setTextMatrix(tx[0][0], tx[0][1], tx[1][0], tx[1][1], tx[2][0], tx[2][1]);
    contentStream.drawString(string);
    contentStream.endText();
  }

  @Override
  public float xShift(HorizontalAlignment halign, String text, Font<PdfBox> font)
      throws IOException {
    return -HORIZ_SHIFT_MULT.get(halign) * width(text, font);
  }

  @Override
  public float yShift(VerticalAlignment valign, Font<PdfBox> font) {
    switch (valign) {
    case TOP:
      return -(font.getLineHeight() - font.getDescender());
    case CENTER:
      return font.getXHeight() / -2f;
    case BASELINE:
      return 0;
    case BOTTOM:
      return (font.getLineHeight() - font.getAscenderHeight());
    }
    throw new IllegalStateException("Unknown vertical alignment type");
  }
}
