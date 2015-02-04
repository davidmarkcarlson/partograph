package com.smithcarlson.partograph.pdfbox;

public class PdfBox {
  // Points per inch
  public static final float PPI = 72.0f;

  public static float toPoints(float inches) {
    return inches * PPI;
  }

  /**
   * Destructively converts and returns the modified values for convenience.
   */
  public static float[] toPoints(float[] inches, float offset) {
    for (int i = 0; i < inches.length; i++) {
      inches[i] = (inches[i] + offset) * PPI;
    }
    return inches;
  }

  /**
   * Destructively converts and returns the modified values for convenience.
   */
  public static float[] toPoints(float[] inches, float offset, float reverseFrom) {
    for (int i = 0; i < inches.length; i++) {
      inches[i] = reverseFrom - ((inches[i] + offset) * PPI);
    }
    return inches;
  }
}
