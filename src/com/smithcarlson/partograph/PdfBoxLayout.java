package com.smithcarlson.partograph;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.smithcarlson.partograph.general.Font;
import com.smithcarlson.partograph.pdfbox.PdfBox;
import com.smithcarlson.partograph.pdfbox.PdfBoxFont;

public class PdfBoxLayout extends BaseLayout implements Layout<PdfBox> {
  private final Font<PdfBox> titleFont;
  private final Font<PdfBox> headingFont;
  private final Font<PdfBox> bodyFont;

  public PdfBoxLayout() throws IOException {
    hours = 20;
    cmCount = 7;

    titleFont = new PdfBoxFont(PDType1Font.HELVETICA_BOLD, 10f);
    headingFont = new PdfBoxFont(PDType1Font.HELVETICA, 8f);
    bodyFont = new PdfBoxFont(PDType1Font.HELVETICA, 7f);

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
  public float getHeightPerCm() {
    return heightPerCm;
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
  public float getWidthPerHour() {
    return widthPerHour;
  }

  @Override
  public float getSpaceToHourBoxes() {
    return spaceToHourBoxes;
  }

  @Override
  public float getBoxSide() {
    return boxSide;
  }

  @Override
  public float getTimeSpacing() {
    return timeSpacing;
  }

  @Override
  public float getHLineLeft() {
    return partographLeft - xAxisOverhang;
  }

  @Override
  public float getHLineRight() {
    return partographRight + xAxisOverhang;
  }

  @Override
  public float getHLineY(int i) {
    return partographTop + (getHeightPerCm() * i);
  }
}
