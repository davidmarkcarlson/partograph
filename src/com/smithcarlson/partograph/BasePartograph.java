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

//TODO abstract away PDFont!
//TODO lots of cleanup!
public class BasePartograph {
  PDFont bodyFont = PDType1Font.HELVETICA;
  PDFont fontBold = PDType1Font.HELVETICA_BOLD;

  final float top = 2.0f;
  final float heightPerCM = 0.3f;
  final float bottom = top + (6 * heightPerCM);
  final float centerY = (top + bottom) / 2f;

  final int hours = 20;
  final float left = 1.5f;
  final float right = 6.5f;
  final float centerX = (left + right) / 2f;
  final float width = right - left;
  final float widthPerHour = width / hours;

  final float titleFontSize = 10f;
  final float headingFontSize = 8f;
  final float bodyFontSize = 7f;

  void drawHorizGridLines(PDCanvas canvas) throws IOException {
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (heightPerCM * i);
      canvas.drawLine(left - 0.1f, y, right + 0.1f, y, 0.5f, LinePattern.SOLID, Color.BLACK,
          LineCapStyle.ROUND);
    }
  }

  void drawVertGridLines(PDCanvas canvas) throws IOException {
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

  void drawRightYAxis(PDCanvas canvas) throws IOException {
    String[] rightYAxis = new String[] { "-3 or higher", "-2", "-1", "0", "+1", "+2", "+3 or lower" };
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (heightPerCM * i);
      canvas.write(rightYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
          VerticalAlignment.CENTER, right + 0.2f, y, bodyFont, bodyFontSize, Color.BLACK);
    }
  }

  void drawLeftYAxis(PDCanvas canvas) throws IOException {
    String[] leftYAxis = new String[] { "10", "9", "8", "7", "6", "Direct Start 5",
        "Earlier Start 4" };
    for (int i = 0; i < 7; i++) {
      float y = 2.0f + (heightPerCM * i);
      canvas.write(leftYAxis[i], Orientation.LEFT_TO_RIGHT, HorizontalAlignment.RIGHT,
          VerticalAlignment.CENTER, left - 0.2f, y, bodyFont, bodyFontSize, Color.BLACK);
    }
  }

  void labelRightAxis(PDCanvas canvas) throws IOException {
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

  void labelLeftYAxis(PDCanvas canvas) throws IOException {
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
