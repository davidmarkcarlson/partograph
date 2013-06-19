package com.smithcarlson.partograph;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import com.smithcarlson.partograph.pdfbox.PdfBox;
import com.smithcarlson.partograph.pdfbox.PdfBoxCanvas;

public class Render {
  static final int LT_25 = 0;
  static final int _25_30 = 1;
  static final int _30_40 = 2;
  static final int GT_40 = 3;

  static String NULLIPAROUS_PREFIX = "Nulliparous, BMI ";

  static final String[] LABELS = new String[] { "< 25", "25-30", "30-40", "> 40", };
  static final float[][] DURATIONS = new float[][] {
      new float[] { 4.5f, 2.75f, 2.25f, 1.75f, 1.25f, 1.5f },
      new float[] { 4.75f, 2.75f, 2.75f, 2f, 1.75f, 2f },
      new float[] { 5.75f, 3.5f, 2.5f, 2f, 1.5f, 2f },
      new float[] { 6.75f, 4f, 3.25f, 2.5f, 1.25f, 1.75f } };

  public static void main(String[] args) throws IOException, COSVisitorException {
    PDDocument document = new PDDocument();
    PDPage page;
    PDPageContentStream contentStream;
    PdfBoxCanvas canvas;

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f);
    addOverviewPartograph(canvas, "Dystocia Lines By BMI");
    contentStream.close();

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f);
    addPartograph(canvas, NULLIPAROUS_PREFIX + LABELS[LT_25], DURATIONS[LT_25]);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 5.0f);
    addPartograph(canvas, NULLIPAROUS_PREFIX + LABELS[_25_30], DURATIONS[_25_30]);
    contentStream.close();

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f);
    addPartograph(canvas, NULLIPAROUS_PREFIX + LABELS[_30_40], DURATIONS[_30_40]);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 5.0f);
    addPartograph(canvas, NULLIPAROUS_PREFIX + LABELS[GT_40], DURATIONS[GT_40]);
    contentStream.close();

    document.save("partographs.pdf");
    document.close();
  }

  private static void addOverviewPartograph(PdfBoxCanvas canvas, String title) throws IOException {
    BasePartograph<PdfBox> base = new BasePartograph<PdfBox>();
    base.setLayout(new PdfBoxLayout());
    CombinedPartograph<PdfBox> partograph = new CombinedPartograph<PdfBox>(title, LABELS,
        DURATIONS, base);
    partograph.render(canvas);
  }

  private static void addPartograph(PdfBoxCanvas canvas, String title, float[] durations)
      throws IOException {
    BasePartograph<PdfBox> base = new BasePartograph<PdfBox>();
    base.setLayout(new PdfBoxLayout());
    SpecializedPartograph<PdfBox> partograph = new SpecializedPartograph<PdfBox>(base);
    partograph.setTitle(title);
    partograph.setDurations(durations);
    partograph.render(canvas);
  }
}
