package com.smithcarlson.partograph;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.smithcarlson.partograph.general.Canvas;
import com.smithcarlson.partograph.general.Canvas.HorizontalAlignment;
import com.smithcarlson.partograph.general.Canvas.LineCapStyle;
import com.smithcarlson.partograph.general.Canvas.LinePattern;
import com.smithcarlson.partograph.general.Canvas.Orientation;
import com.smithcarlson.partograph.general.Canvas.VerticalAlignment;
import com.smithcarlson.partograph.general.LayoutTool;
import com.smithcarlson.partograph.general.Pen;
import com.smithcarlson.partograph.general.TypeSetter;
import com.smithcarlson.partograph.pdfbox.PdfBox;
import com.smithcarlson.partograph.pdfbox.PdfBoxCanvas;
import com.smithcarlson.partograph.pdfbox.PdfBoxFont;

public class RenderingTests {
  private static PdfBoxFont title;

  public static void main(String[] args) throws IOException, COSVisitorException {
    title = new PdfBoxFont(PDType1Font.HELVETICA_BOLD, 12f);

    PDDocument document = new PDDocument();
    PDPage page;
    PDPageContentStream contentStream;

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    fontTest(new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f));
    writeOnLineTest(new PdfBoxCanvas(page, contentStream, 3.0f, 0.0f));
    writeInBoxTest(new PdfBoxCanvas(page, contentStream, 3.0f, 3.0f));
    contentStream.close();
    document.save("test.pdf");
    document.close();
  }

  private static void fontTest(PdfBoxCanvas c) throws IOException {
    float baseline = 1;
    c.write("Font positioning tests", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, 1, baseline, title, Color.BLACK);

    PdfBoxFont f = new PdfBoxFont(PDType1Font.HELVETICA, 24f);

    baseline += f.getLineHeight();
    c.write("Blog", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, 1, baseline, f, Color.BLACK);
    drawRefLine(baseline, c);
    drawRefLine(baseline - f.getXHeight(), c);
    drawRefLine(baseline - f.getAscenderHeight(), c);
    drawRefLine(baseline - f.getCenterY(), c);
    drawRefLine(baseline + f.getDescender(), c);
    drawRefLine(baseline + f.getLineHeight() - f.getAscenderHeight(), c);

    baseline += f.getLineHeight();
    c.write("Blog", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, 1, baseline, f, Color.BLACK);
    drawRefLine(baseline, c);

    baseline += f.getLineHeight();
    c.write("Blog", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, 1, baseline, f, Color.BLACK);
    drawRefLine(baseline - f.getCenterY(), c);

    baseline += 1.5f * f.getLineHeight();
    c.drawLine(1.0f, baseline, 3.0f, baseline, 0.25f, LinePattern.SOLID, Color.BLACK,
        Canvas.LineCapStyle.BUTT);
    c.write("Bg", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM,
        1.0f, baseline, f, Color.BLACK);
    c.write("Bg", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT, VerticalAlignment.BASELINE,
        1.5f, baseline, f, Color.BLACK);
    c.write("Bg", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT, VerticalAlignment.CENTER,
        2.0f, baseline, f, Color.BLACK);
    c.write("Bg", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT, VerticalAlignment.TOP, 2.5f,
        baseline, f, Color.BLACK);

  }

  private static void drawRefLine(float yPos, PdfBoxCanvas c) throws IOException {
    c.drawLine(0.5f, yPos, 1.0f, yPos, 0.25f, LinePattern.SOLID, Color.BLACK,
        Canvas.LineCapStyle.BUTT);
  }

  private static void writeInBoxTest(PdfBoxCanvas canvas) throws IOException {
    float baseline = 1;

    canvas.write("Write in box tests", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, 0, baseline, title, Color.BLACK);

    PdfBoxFont f = new PdfBoxFont(PDType1Font.HELVETICA, 24f);
    TypeSetter<PdfBox> typesetter = new TypeSetter<PdfBox>(Orientation.LEFT_TO_RIGHT,
        HorizontalAlignment.CENTER, VerticalAlignment.CENTER, f, Color.BLACK);
    baseline += title.getLineHeight();

    Pen pen = new Pen(0.5f, Color.BLACK, LinePattern.SOLID, LineCapStyle.PROJECTING_SQUARE);

    float x1 = 0f, x2 = 4f, y1 = baseline, y2 = baseline + 2f;
    pen.drawBox(x1, x2, y1, y2, canvas);
    pen.drawLine(x1, (y1 + y2) / 2f, x2, (y1 + y2) / 2f, canvas);
    pen.drawLine((x1 + x2) / 2f, y1, (x1 + x2) / 2f, y2, canvas);

    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.LEFT).vAlign(
        VerticalAlignment.TOP)).writeInBox("Tig", x1, y1, x2, y2, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.CENTER).vAlign(
        VerticalAlignment.CENTER)).writeInBox("Tig", x1, y1, x2, y2, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.RIGHT).vAlign(
        VerticalAlignment.BOTTOM)).writeInBox("Tig", x1, y1, x2, y2, canvas);

    new LayoutTool<PdfBox>(pen, new TypeSetter<PdfBox>(Orientation.TOP_TO_BOTTOM,
        HorizontalAlignment.RIGHT, VerticalAlignment.TOP, f, Color.BLACK)).writeInBox("Tig", x1,
        y1, x2, y2, canvas);
    new LayoutTool<PdfBox>(pen, new TypeSetter<PdfBox>(Orientation.BOTTOM_TO_TOP,
        HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, f, Color.BLACK)).writeInBox("Tig", x1,
        y1, x2, y2, canvas);
  }

  private static void writeOnLineTest(PdfBoxCanvas canvas) throws IOException {
    float baseline = 1;

    canvas.write("Write on line tests", Orientation.LEFT_TO_RIGHT, HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, 0, baseline, title, Color.BLACK);

    PdfBoxFont f = new PdfBoxFont(PDType1Font.HELVETICA, 24f);
    TypeSetter<PdfBox> typesetter = new TypeSetter<PdfBox>(Orientation.LEFT_TO_RIGHT,
        HorizontalAlignment.CENTER, VerticalAlignment.CENTER, f, Color.BLACK);
    baseline += f.getLineHeight();

    Pen pen = new Pen(0.5f, Color.BLACK, LinePattern.SOLID, LineCapStyle.PROJECTING_SQUARE);

    float x1 = 0f, x2 = 4f;
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.LEFT).vAlign(
        VerticalAlignment.BASELINE)).writeOn("Tig", x1, baseline, x2, baseline, false, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.CENTER).vAlign(
        VerticalAlignment.CENTER)).writeOn("Tig", x1, baseline, x2, baseline, true, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.RIGHT).vAlign(
        VerticalAlignment.TOP)).writeOn("Tig", x1, baseline, x2, baseline, false, canvas);

    baseline += f.getLineHeight();
    float y2 = baseline + 2.0f;
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.LEFT).vAlign(
        VerticalAlignment.BASELINE)).writeOn("Tig", x1, baseline, x2, y2, false, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.CENTER).vAlign(
        VerticalAlignment.CENTER)).writeOn("Tig", x1, baseline, x2, y2, true, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.RIGHT).vAlign(
        VerticalAlignment.TOP)).writeOn("Tig", x1, baseline, x2, y2, false, canvas);

    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.RIGHT).vAlign(
        VerticalAlignment.TOP)).writeOn("Tig", x1, y2, x2, baseline, true, canvas);

    new LayoutTool<PdfBox>(pen, typesetter.hAlign(HorizontalAlignment.LEFT).vAlign(
        VerticalAlignment.TOP)).writeOn("Tig", x1, y2, x1, baseline, true, canvas);
  }
}
