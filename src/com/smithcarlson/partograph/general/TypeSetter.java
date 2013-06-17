package com.smithcarlson.partograph.general;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.Orientation;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;

public class TypeSetter<T> {
  private final Orientation orientation;
  private final HorizontalAlignment halign;
  private final VerticalAlignment valign;
  private final Font<T> font;
  private final Color color;

  private static Map<VerticalAlignment, HorizontalAlignment> topToBottomV2HMap = new HashMap<VerticalAlignment, HorizontalAlignment>();
  private static Map<HorizontalAlignment, VerticalAlignment> topToBottomH2VMap = new HashMap<HorizontalAlignment, VerticalAlignment>();

  private static Map<VerticalAlignment, HorizontalAlignment> bottomToTopV2HMap = new HashMap<VerticalAlignment, HorizontalAlignment>();
  private static Map<HorizontalAlignment, VerticalAlignment> bottomToTopH2VMap = new HashMap<HorizontalAlignment, VerticalAlignment>();

  static {
    topToBottomV2HMap.put(VerticalAlignment.TOP, HorizontalAlignment.LEFT);
    topToBottomV2HMap.put(VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
    topToBottomV2HMap.put(VerticalAlignment.BASELINE, HorizontalAlignment.RIGHT);
    topToBottomV2HMap.put(VerticalAlignment.BOTTOM, HorizontalAlignment.RIGHT);

    topToBottomH2VMap.put(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
    topToBottomH2VMap.put(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
    topToBottomH2VMap.put(HorizontalAlignment.RIGHT, VerticalAlignment.TOP);

    bottomToTopV2HMap.put(VerticalAlignment.TOP, HorizontalAlignment.RIGHT);
    bottomToTopV2HMap.put(VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
    bottomToTopV2HMap.put(VerticalAlignment.BASELINE, HorizontalAlignment.LEFT);
    bottomToTopV2HMap.put(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT);

    bottomToTopH2VMap.put(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    bottomToTopH2VMap.put(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
    bottomToTopH2VMap.put(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
  }

  public TypeSetter(Orientation orientation, HorizontalAlignment halign, VerticalAlignment valign,
      Font<T> font, Color color) {
    this.orientation = orientation;
    this.halign = halign;
    this.valign = valign;
    this.font = font;
    this.color = color;
  }

  public void write(String text, float x, float y, Canvas<T> canvas) throws IOException {
    canvas.write(text, orientation, halign, valign, x, y, font, color);
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public HorizontalAlignment getHalign() {
    return halign;
  }

  public VerticalAlignment getValign() {
    return valign;
  }

  public Font<T> getFont() {
    return font;
  }

  public Color getColor() {
    return color;
  }

  public TypeSetter<T> transform(Orientation orientation) {
    switch (orientation) {
    case LEFT_TO_RIGHT:
      return this;
    case TOP_TO_BOTTOM:
      return new TypeSetter<T>(orientation, topToBottomV2HMap.get(valign),
          topToBottomH2VMap.get(halign), font, color);
    case BOTTOM_TO_TOP:
      return new TypeSetter<T>(orientation, bottomToTopV2HMap.get(valign),
          bottomToTopH2VMap.get(halign), font, color);
    default:
      throw new IllegalStateException("unkown orientation");
    }
  }

  public TypeSetter<T> hAlign(HorizontalAlignment newHAlign) {
    return new TypeSetter<T>(orientation, newHAlign, valign, font, color);
  }

  public TypeSetter<T> vAlign(VerticalAlignment newVAlign) {
    return new TypeSetter<T>(orientation, halign, newVAlign, font, color);
  }

  public TypeSetter<T> orient(Orientation newOrientation) {
    return new TypeSetter<T>(newOrientation, halign, valign, font, color);
  }

}
