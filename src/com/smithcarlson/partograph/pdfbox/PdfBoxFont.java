package com.smithcarlson.partograph.pdfbox;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.smithcarlson.partograph.general.Font;

/**
 * Fonts render from the baseline. In addition, they have a line-height (the
 * font size), cap-height (height of a capital C), an x-height, ascender (from
 * top of 'x' to top of 'l'), ascender-height ('l'), and descender-height
 * (height of a 'y') and descender (bottom of 'x' to bottom of 'y')
 *
 * @author dcarlson
 *
 */
public class PdfBoxFont implements Font<PdfBox> {
  private static final byte[] referenceChars = new byte[] { 'C', 'x', 'l', 'y' };
  private static final float FONT_UNIT_MULTIPLIER = 1000f;
  public final float sizePts;
  public final PDFont fontFamily;

  private final float lineHeight;

  private final float capHeight;

  private final float xHeight;

  private final float ascender;

  private final float ascenderHeight;

  private final float descender;

  private final float descenderHeight;

  public PdfBoxFont(PDFont fontFamily, float sizePts) throws IOException {
    super();
    this.fontFamily = fontFamily;
    this.sizePts = sizePts;
    this.lineHeight = sizePts / PdfBox.PPI;
    this.capHeight = lineHeight * fontFamily.getFontHeight(referenceChars, 0, 1)
        / FONT_UNIT_MULTIPLIER;
    this.xHeight = lineHeight * fontFamily.getFontHeight(referenceChars, 1, 1)
        / FONT_UNIT_MULTIPLIER;
    this.ascenderHeight = lineHeight * fontFamily.getFontHeight(referenceChars, 1, 1)
        / FONT_UNIT_MULTIPLIER;
    this.ascender = ascenderHeight - xHeight;
    this.descenderHeight = lineHeight * fontFamily.getFontHeight(referenceChars, 1, 1)
        / FONT_UNIT_MULTIPLIER;
    this.descender = descenderHeight - xHeight;
  }

  public float getAscender() {
    return ascender;
  }

  @Override
  public float getAscenderHeight() {
    return ascenderHeight;
  }

  public float getCapHeight() {
    return capHeight;
  }

  public float getDescender() {
    return descender;
  }

  public float getDescenderHeight() {
    return descenderHeight;
  }

  @Override
  public float getLineHeight() {
    return lineHeight;
  }

  @Override
  public float getSizePts() {
    return sizePts;
  }

  @Override
  public float getStringWidth(String text) throws IOException {
    return sizePts * fontFamily.getStringWidth(text) / (PdfBox.PPI * FONT_UNIT_MULTIPLIER);
  }

  public float getXHeight() {
    return xHeight;
  }

  float getStringWidthRaw(String text) throws IOException {
    return sizePts * fontFamily.getStringWidth(text) / FONT_UNIT_MULTIPLIER;
  }
}
