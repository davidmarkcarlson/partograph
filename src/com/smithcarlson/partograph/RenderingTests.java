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
    contentStream.close();
    document.save("test.pdf");
    document.close();
  }

  private static void fontTest(PdfBoxCanvas c) throws IOException {
    float baseline = 1;
    PdfBoxFont f = new PdfBoxFont(PDType1Font.HELVETICA, 24f);
    TypeSetter<PdfBox> typesetter = new TypeSetter<PdfBox>(HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, f, Color.BLACK);
    typesetter.with(title).write("Font positioning tests",  1, baseline, c);


    baseline += f.getLineHeight();
    typesetter.write("Blog",  1, baseline, c);
    drawRefLine(baseline, c);
    drawRefLine(baseline - f.getXHeight(), c);
    drawRefLine(baseline - f.getAscenderHeight(), c);
    drawRefLine(baseline - f.getCenterY(), c);
    drawRefLine(baseline + f.getDescender(), c);
    drawRefLine(baseline + f.getLineHeight() - f.getAscenderHeight(), c);

    baseline += f.getLineHeight();
    typesetter.write("Blog",  1, baseline, c);
    drawRefLine(baseline, c);

    baseline += f.getLineHeight();
    typesetter.write("Blog",  1, baseline, c);
    drawRefLine(baseline - f.getCenterY(), c);

    baseline += 1.5f * f.getLineHeight();
    c.drawLine(1.0f, baseline, 3.0f, baseline, 0.25f, LinePattern.SOLID, Color.BLACK,
        Canvas.LineCapStyle.BUTT);
    typesetter.with(VerticalAlignment.BOTTOM).write("Bg",  1.0f, baseline, c);
    typesetter.with(VerticalAlignment.BASELINE).write("Bg",  1.5f, baseline, c);
    typesetter.with(VerticalAlignment.CENTER).write("Bg",  2.0f, baseline, c);
    typesetter.with(VerticalAlignment.TOP).write("Bg",  2.5f, baseline, c);

  }

  private static void drawRefLine(float yPos, PdfBoxCanvas c) throws IOException {
    c.drawLine(0.5f, yPos, 1.0f, yPos, 0.25f, LinePattern.SOLID, Color.BLACK,
        Canvas.LineCapStyle.BUTT);
  }

  private static void writeOnLineTest(PdfBoxCanvas canvas) throws IOException {
    float baseline = 1;

    PdfBoxFont f = new PdfBoxFont(PDType1Font.HELVETICA, 24f);
    TypeSetter<PdfBox> typesetter = new TypeSetter<PdfBox>(HorizontalAlignment.LEFT,
        VerticalAlignment.BASELINE, f, Color.BLACK);

    typesetter.with(title).write("Write on line tests", 0, baseline, canvas);

    baseline += f.getLineHeight();

    Pen pen = new Pen(0.5f, Color.BLACK, LinePattern.SOLID, LineCapStyle.PROJECTING_SQUARE);

    float x1 = 0f, x2 = 4f;
    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.LEFT).with(
        VerticalAlignment.BASELINE)).writeOn("Tig", x1, baseline, x2, baseline, false, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.CENTER).with(
        VerticalAlignment.CENTER)).writeOn("Tig", x1, baseline, x2, baseline, true, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.RIGHT).with(
        VerticalAlignment.TOP)).writeOn("Tig", x1, baseline, x2, baseline, false, canvas);

    baseline += f.getLineHeight();
    float y2 = baseline + 2.0f;
    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.LEFT).with(
        VerticalAlignment.BASELINE)).writeOn("Tig", x1, baseline, x2, y2, false, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.CENTER).with(
        VerticalAlignment.CENTER)).writeOn("Tig", x1, baseline, x2, y2, true, canvas);
    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.RIGHT).with(
        VerticalAlignment.TOP)).writeOn("Tig", x1, baseline, x2, y2, false, canvas);

    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.RIGHT).with(
        VerticalAlignment.TOP)).writeOn("Tig", x1, y2, x2, baseline, true, canvas);

    new LayoutTool<PdfBox>(pen, typesetter.with(HorizontalAlignment.LEFT).with(
        VerticalAlignment.TOP)).writeOn("Tig", x1, y2, x1, baseline, true, canvas);
  }
}
