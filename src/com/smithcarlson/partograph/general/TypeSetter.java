package com.smithcarlson.partograph.general;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;

public class TypeSetter<T> {
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

  public TypeSetter(HorizontalAlignment halign, VerticalAlignment valign, Font<T> font, Color color) {
    this.halign = halign;
    this.valign = valign;
    this.font = font;
    this.color = color;
  }

  public Color getColor() {
    return color;
  }

  public Font<T> getFont() {
    return font;
  }

  public HorizontalAlignment getHalign() {
    return halign;
  }

  public VerticalAlignment getValign() {
    return valign;
  }

  public TypeSetter<T> with(Font<T> newFont) {
    return new TypeSetter<T>(halign, valign, newFont, color);
  }

  public TypeSetter<T> with(HorizontalAlignment newHAlign) {
    return new TypeSetter<T>(newHAlign, valign, font, color);
  }

  public TypeSetter<T> with(VerticalAlignment newVAlign) {
    return new TypeSetter<T>(halign, newVAlign, font, color);
  }

  public void write(String text, float x, float y, Canvas<T> canvas) throws IOException {
    write(text, x, y, 0f, canvas);
  }

  public void write(String text, float x, float y, float rotation, Canvas<T> canvas)
      throws IOException {
    canvas.write(text, x, y, canvas.xShift(halign, text, font), canvas.yShift(valign, font),
        rotation, font, color);
  }

}
