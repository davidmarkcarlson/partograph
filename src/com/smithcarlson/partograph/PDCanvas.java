package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;

/**
 * // TODO include offset
 *
 * @author dcarlson
 *
 */
public class PDCanvas implements Canvas {

  private final PDPageContentStream contentStream;
  private State state = null;

  public PDCanvas(PDDocument document, PDPage page) throws IOException {
    contentStream = new PDPageContentStream(document, page);
    state = State.OPEN;
  }

  public PDPageContentStream getContentStream() {
    return contentStream;
  }

  public void write(String string, Orientation orientation, HorizontalAlignment halign,
      VerticalAlignment valign, float x, float y, PDFont font, float fontSize, Color color)
      throws IOException {

    if (state != State.OPEN)
      throw new IllegalStateException("Writing to closed canvas");

    contentStream.setNonStrokingColor(color);

    float posx = toPoints(x);
    float posy = toPoints(11 - y);

    byte[] referenceChars = new byte[] { 'l', 'x', 'y' };
    float shiftx = 0f, shifty = 0f;
    float ascent = fontSize * font.getFontHeight(referenceChars, 0, 1) / 1000f;

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
      shifty -= ascent / 2f;
      break;
    case TOP:
      shifty -= ascent;
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

  float toPoints(float inches) {
    return inches * 72;
  }

  public void drawBox(float box_x1, float box_x2, float box_y1, float box_y2,
      float width, LinePattern style, Color color, LineCapStyle cap) throws IOException {
    drawLine(box_x1, box_y1, box_x2, box_y1, width, style, color, cap);
    drawLine(box_x1, box_y1, box_x1, box_y2, width, style, color, cap);
    drawLine(box_x1, box_y2, box_x2, box_y2, width, style, color, cap);
    drawLine(box_x2, box_y1, box_x2, box_y2, width, style, color, cap);
  }

  public void drawLine(float x1, float y1, float x2, float y2, float width, LinePattern style,
      Color color, LineCapStyle cap) throws IOException {
    if (state != State.OPEN)
      throw new IllegalStateException("Writing to closed canvas");

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

  public void close() throws IOException {
    state = State.CLOSED;
    contentStream.close();
  }

}
