package com.smithcarlson.partograph;

import com.smithcarlson.partograph.pdfbox.PdfBox;
import com.smithcarlson.partograph.pdfbox.PdfBoxCanvas;
import java.io.IOException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class Render {
  static final int LT_25 = 0;
  static final int _25_30 = 1;
  static final int _30_35 = 2;
  static final int _35_40 = 3;
  static final int GT_40 = 4;

  static String NULLIPAROUS_PREFIX = "Nulliparous, BMI ";

  static final String[] LABELS = new String[] { "< 25", "25-30", "30-35", "35-40", "> 40", };
  static final float[][] DURATIONS = new float[][] {
      new float[] {4.6f, 7.5f, 9.5f, 11.0f, 12.3f, 13.9f},
      new float[] {5.0f, 7.9f, 9.9f, 11.4f, 12.7f, 14.4f},
      new float[] {5.2f, 8.3f, 10.4f, 11.9f, 13.3f, 15.1f},
      new float[] {5.9f, 9.4f, 11.7f, 13.4f, 14.7f, 16.6f},
      new float[] {7.4f, 11.6f, 14.1f, 15.8f, 17.2f, 19.1f}
  };

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
    contentStream.close();

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f);
    addPartograph(canvas, NULLIPAROUS_PREFIX + LABELS[_25_30], DURATIONS[_25_30]);
    contentStream.close();

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f);
    addPartograph(canvas, NULLIPAROUS_PREFIX + LABELS[_30_35], DURATIONS[_30_35]);
    contentStream.close();

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f);
    addPartograph(canvas, NULLIPAROUS_PREFIX + LABELS[_35_40], DURATIONS[_35_40]);
    contentStream.close();

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    canvas = new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f);
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
