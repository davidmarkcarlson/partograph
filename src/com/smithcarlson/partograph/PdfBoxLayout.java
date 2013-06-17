package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.LineCapStyle;
import com.smithcarlson.partograph.general.Canvas.LinePattern;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;
import com.smithcarlson.partograph.general.Font;
import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.general.TypeSetter;
import com.smithcarlson.partograph.pdfbox.PdfBox;
import com.smithcarlson.partograph.pdfbox.PdfBoxFont;

public class PdfBoxLayout extends BaseLayout<PdfBox> implements Layout<PdfBox> {
  private final Font<PdfBox> titleFont;
  private final Font<PdfBox> headingFont;
  private final Font<PdfBox> bodyFont;

  public PdfBoxLayout() throws IOException {
    hours = 20;
    linesPerHour = 4;
    cmCount = 7;

    titleFont = new PdfBoxFont(PDType1Font.HELVETICA_BOLD, 10f);
    headingFont = new PdfBoxFont(PDType1Font.HELVETICA, 8f);
    bodyFont = new PdfBoxFont(PDType1Font.HELVETICA, 7f);

    titleSetter = new TypeSetter<PdfBox>(HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
        bodyFont, Color.BLACK);
    headingSetter = new TypeSetter<PdfBox>(HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
        bodyFont, Color.BLACK);
    bodySetter = new TypeSetter<PdfBox>(HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
        bodyFont, Color.BLACK);

    lightLineFS = new Pen(0.5f, Color.BLACK, LinePattern.SOLID, LineCapStyle.PROJECTING_SQUARE);
    lightLine = new Pen(0.5f, Color.BLACK, LinePattern.SOLID, LineCapStyle.BUTT);
    heavyLine = new Pen(1.0f, Color.BLACK, LinePattern.SOLID, LineCapStyle.BUTT);
    heavyLineFS = new Pen(1.0f, Color.BLACK, LinePattern.SOLID, LineCapStyle.PROJECTING_SQUARE);
    dottedGridLine = new Pen(0.5f, Color.GRAY, LinePattern.SOLID, LineCapStyle.BUTT);
    dashDotDotGridLine = new Pen(0.5f, Color.GRAY, LinePattern.DASH_DOT_DOT, LineCapStyle.BUTT);

    partographTop = 2.0f;
    heightPerCm = 0.3f;
    partographHeight = (cmCount - 1) * heightPerCm;
    partographBottom = partographTop + partographHeight;
    partographCenterY = partographTop + (partographHeight / 2);

    partographLeft = 1.5f;
    partographRight = 7.0f;
    partographCenterX = (partographLeft + partographRight) / 2f;
    partographWidth = partographRight - partographLeft;
    widthPerHour = partographWidth / hours;

    xAxisOverhang = 0.1f;
    spaceToHourBoxes = 0.25f;
    hourBoxSize = 0.15f;
    timeSpace = 0.35f;
    halfHourOverhang = 0.1f;

    yAxisOverhang = 0.1f;
    yAxisMargin = 0.1f;
  }

  @Override
  public int getCmCount() {
    return cmCount;
  }

  @Override
  public Font<PdfBox> getTitleFont() {
    return titleFont;
  }

  @Override
  public Font<PdfBox> getHeadingFont() {
    return headingFont;
  }

  @Override
  public Font<PdfBox> getBodyFont() {
    return bodyFont;
  }

  @Override
  public float getPartographTop() {
    return partographTop;
  }

  @Override
  public float getPartographBottom() {
    return partographBottom;
  }

  @Override
  public float getPartographHeight() {
    return partographHeight;
  }

  @Override
  public float getPartographCenterY() {
    return partographCenterY;
  }

  @Override
  public float getPartographLeft() {
    return partographLeft;
  }

  @Override
  public float getPartographRight() {
    return partographRight;
  }

  @Override
  public float getPartographWidth() {
    return partographWidth;
  }

  @Override
  public float getPartographCenterX() {
    return partographCenterX;
  }

  @Override
  public int getHours() {
    return hours;
  }

  @Override
  public float hLineLeft() {
    return partographLeft - xAxisOverhang;
  }

  @Override
  public float hLineRight() {
    return partographRight + xAxisOverhang;
  }

  @Override
  public float hLineY(int i) {
    return partographTop + (heightPerCm * (cmCount - (i + 1)));
  }

  @Override
  public int getLinesPerHour() {
    return linesPerHour;
  }

  @Override
  public float getVerticalLineXForLine(int position) {
    return getPartographLeft() + ((position * widthPerHour) / linesPerHour);
  }

  @Override
  public float getPartographXForHour(float hour) {
    return getPartographLeft() + (hour * widthPerHour);
  }

  @Override
  public float getHourGridLineBottom() {
    return partographBottom + spaceToHourBoxes;
  }

  @Override
  public float getHalfHourGridLineBottom() {
    return partographBottom + halfHourOverhang;
  }

  @Override
  public float getQuarterHourGridLineBottom() {
    return partographBottom;
  }

  @Override
  public float getTimeWriteInTop() {
    return partographBottom + spaceToHourBoxes + hourBoxSize;
  }

  @Override
  public float getTimeWriteInBottom() {
    return partographBottom + spaceToHourBoxes + hourBoxSize + timeSpace;
  }

  @Override
  public float getTimeWriteInCenterY() {
    return partographBottom + spaceToHourBoxes + hourBoxSize + timeSpace / 2f;
  }

  @Override
  @Deprecated
  public float getWidthPerHour() {
    return widthPerHour;
  }

  @Override
  public float getHourBoxCenterY() {
    return partographBottom + spaceToHourBoxes + (hourBoxSize / 2f);
  }

  @Override
  public float getHourBoxSize() {
    return hourBoxSize;
  }

  @Override
  public float leftYAxisHashLabelX() {
    return partographLeft - (yAxisOverhang + yAxisMargin);
  }

  @Override
  public float rightYAxisHashLabelX() {
    return partographRight + (yAxisOverhang + yAxisMargin);
  }

  @Override
  public float getPartographYForPosition(float position) {
    return partographBottom - (position * heightPerCm);
  }

  @Override
  public Pen lightLineFS() {
    return lightLineFS;
  }

  @Override
  public Pen lightLine() {
    return lightLine;
  }

  @Override
  public Pen heavyLine() {
    return heavyLine;
  }

  @Override
  public Pen heavyLineFS() {
    return heavyLineFS;
  }

  @Override
  public Pen dottedGridLine() {
    return dottedGridLine;
  }

  @Override
  public Pen dashDotDotGridLine() {
    return dashDotDotGridLine;
  }

  @Override
  public float getXAxisLabelY() {
    return partographBottom + headingFont.getLineHeight();
  }

  @Override
  public float getTimeWorksheetLabelY() {
    return getTimeWriteInBottom() + headingFont.getLineHeight();
  }

  @Override
  public TypeSetter<PdfBox> titleSetter() {
    return titleSetter;
  }

  @Override
  public TypeSetter<PdfBox> headingSetter() {
    return headingSetter;
  }

  @Override
  public TypeSetter<PdfBox> bodySetter() {
    return bodySetter;
  }

  @Override
  public TypeSetter<PdfBox> baseLineSetter() {
    return bodySetter.with(VerticalAlignment.BASELINE);
  }
}
