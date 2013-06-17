package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import com.smithcarlson.partograph.general.Canvas;
import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.LineCapStyle;
import com.smithcarlson.partograph.general.Canvas.LinePattern;
import com.smithcarlson.partograph.general.Canvas.Orientation;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;
import com.smithcarlson.partograph.general.Font;
import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.pdfbox.PdfBox;

public class CombinedPartograph<T> {
  private final Color[] DYSTOCIA_LINE_COLORS = new Color[] { new Color(0.3f, 0.5f, 0.8f, 0.1f),
      new Color(0.8f, 0.4f, 0.7f, 0.1f), new Color(0.75f, 0.4f, 0.25f, 0.1f),
      new Color(0.5f, 0.3f, 0.6f, 0.1f) };
  private final String title;
  private final String[] labels;
  private final float[][] dystocialDurations;
  private final BasePartograph<T> base;

  public CombinedPartograph(String title, String[] labels, float[][] dystocialDurations,
      BasePartograph<T> base) {
    this.title = title;
    this.labels = labels;
    this.dystocialDurations = dystocialDurations;
    this.base = base;
  }

  public void render(Canvas<T> canvas) throws IOException {
    canvas.write(title, Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, base.layout.getPartographCenterX(),
        base.layout.getPartographTop() - 0.5f, base.layout.getTitleFont(), Color.BLACK);
    Layout<T> l = base.layout;

    for (int i = 0; i < dystocialDurations.length; i++) {
      base.drawDystociaActionPolygon(dystocialDurations[i], DYSTOCIA_LINE_COLORS[i], canvas);
    }

    base.drawLeftYAxis(canvas);
    base.drawVertGridLines(canvas);
    base.drawHorizGridLines(canvas);
    l.headingSetter().vAlign(VerticalAlignment.TOP)
        .write("Time (hrs)", l.getPartographCenterX(), l.getXAxisLabelY(), canvas);

    Pen dystociaLine = new Pen(2.0f, Color.BLACK, LinePattern.SOLID, LineCapStyle.PROJECTING_SQUARE);

    for (int i = 0; i < dystocialDurations.length; i++) {
      drawDystocialLine(labels[i], i, dystocialDurations[i],
          dystociaLine.with(DYSTOCIA_LINE_COLORS[i]), canvas);
    }
  }

  private void drawDystocialLine(String label, int position, float[] durations, Pen pen,
      Canvas<T> canvas) throws IOException {
    Font<T> font = base.layout.getHeadingFont();

    float totalDuration = 0;
    for (float duration : durations) {
      totalDuration += duration;
    }
    float xPos = base.layout.getPartographXForHour(totalDuration)
        + (pen.getLineThickness() / (PdfBox.PPI *   2f));
    float bottom = base.layout.getPartographTop();
    float gap = 0.10f + ((position % 2 != 0) ? 1.5f * font.getLineHeight() : 0f);
    float top = bottom - gap;
    pen.drawLine(xPos, bottom, xPos, top, canvas);

    float baseline = top - (font.getLineHeight() - font.getAscenderHeight());
    canvas.write(label, Orientation.LEFT_TO_RIGHT, HorizontalAlignment.CENTER,
        VerticalAlignment.BOTTOM, xPos, baseline, base.layout.getHeadingFont(), Color.BLACK);
  }

}
