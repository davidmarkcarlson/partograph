package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import com.smithcarlson.partograph.general.Canvas;
import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.LineCapStyle;
import com.smithcarlson.partograph.general.Canvas.LinePattern;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;
import com.smithcarlson.partograph.general.Font;
import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.general.PointList;
import com.smithcarlson.partograph.general.TypeSetter;

// Ultimately, use various canvas types to support different output forms (e.g. js canvas, ios, svg etc.)
// TODO refactor out magic #s./move layout calculations into the layout class.

public class SpecializedPartograph<T> {
  private final Color ALERT_AREA_COLOR = new Color(1f, 1f, 0.4f, 0.1f);
  private final Color ACTION_AREA_COLOR = new Color(1f, 0.4f, 0.4f, 0.1f);
  private final Color DYSTOCIA_LINE_COLOR = new Color(0.3f, 0.5f, 0.8f, 0.1f);

  private float[] durations;
  private String title;
  private final BasePartograph<T> base;

  public SpecializedPartograph(BasePartograph<T> base) {
    this.base = base;
  }

  public void render(Canvas<T> canvas) throws IOException {
    (new TypeSetter<T>(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM,
        base.layout.getTitleFont(), Color.BLACK)).write(title, base.layout.getPartographCenterX(),
        base.layout.getPartographTop() - 0.5f, canvas);

    drawDystociaAlertPolygon(canvas);
    base.drawDystociaActionPolygon(durations, ACTION_AREA_COLOR, canvas);

    base.drawLeftYAxis(canvas);
    base.labelLeftYAxis(canvas);

    base.drawRightYAxis(canvas);
    base.labelRightAxis(canvas);

    base.drawVertGridLines(canvas);
    base.drawHorizGridLines(canvas);

    base.drawTimelineWorkspace(canvas);

    drawDystociaLine(canvas);
  }

  public void setDurations(float[] durations) {
    this.durations = new float[durations.length];
    System.arraycopy(durations, 0, this.durations, 0, durations.length);
  }

  public void setTitle(String title) {
    this.title = title;
  }

  private void drawDystociaAlertPolygon(Canvas<T> canvas) throws IOException {
    PointList points = base.createDystociaSequence(durations);
    points.addPoint(base.layout.getPartographLeft(), base.layout.getPartographTop());
    points.addPoint(base.layout.getPartographLeft(), base.layout.getPartographBottom());
    canvas.fillPolygon(points, ALERT_AREA_COLOR);
  }

  private void drawDystociaLine(Canvas<T> canvas) throws IOException {
    PointList points = base.createDystociaSequence(durations);
    points.addPoint(points.getCurrentX(), points.getCurrentY() - 0.12f);

    Pen dystociaLine = new Pen(3.0f, DYSTOCIA_LINE_COLOR, LinePattern.SOLID,
        LineCapStyle.PROJECTING_SQUARE);
    for (int i = 1; i < points.size(); i++) {
      dystociaLine.drawLine(points.getXAt(i - 1), points.getYAt(i - 1), points.getXAt(i),
          points.getYAt(i), canvas);
    }

    Font<T> font = base.layout.getHeadingFont();
    String[] text = new String[] { "Dystocia", "Line" };
    float margin = canvas.width("m", font);
    float width = margin + Math.max(canvas.width(text[0], font), canvas.width(text[1], font));
    float height = (font.getLineHeight() * 2f) + margin;
    float gap = 0.15f;

    base.layout.lightLineFS().drawBoxAround(points.getCurrentX(),
        base.layout.getPartographTop() - (gap + height / 2), width, height, canvas);

    TypeSetter<T> typesetter = new TypeSetter<T>(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM,
        base.layout.getHeadingFont(), Color.BLACK);
    float baseline = base.layout.getPartographTop()
        - (gap + (font.getLineHeight() - font.getAscenderHeight()));
    typesetter.write(text[1], points.getCurrentX(), baseline, canvas);
    baseline -= font.getLineHeight();
    typesetter.write(text[0], points.getCurrentX(), baseline, canvas);
  }

}
