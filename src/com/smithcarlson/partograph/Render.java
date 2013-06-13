package com.smithcarlson.partograph;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import com.smithcarlson.partograph.pdfbox.PdfBox;
import com.smithcarlson.partograph.pdfbox.PdfBoxCanvas;

public class Render {
  public static void main(String[] args) throws IOException, COSVisitorException {
    PDDocument document = new PDDocument();
    PDPage page;
    PDPageContentStream contentStream;

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    addPartograph(new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f), "Nulliparous, BMI <25",
        new float[] { 5f, 3f, 2f, 2f, 1.5f, 1.5f });
    addPartograph(new PdfBoxCanvas(page, contentStream, 0.0f, 5.0f), "Nulliparous, BMI 25-30",
        new float[] { 5f, 3f, 2f, 1.5f, 1.5f, 1.75f });

    page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    contentStream = new PDPageContentStream(document, page);
    document.addPage(page);
    addPartograph(new PdfBoxCanvas(page, contentStream, 0.0f, 0.0f), "Nulliparous, BMI 30-40",
        new float[] { 5.5f, 3.25f, 2.25f, 1.5f, 1.5f, 1.75f });
    addPartograph(new PdfBoxCanvas(page, contentStream, 0.0f, 5.0f), "Nulliparous, BMI >40",
        new float[] { 7.5f, 4.25f, 2.5f, 1.75f, 1.5f, 1.75f });

    document.save("partographs.pdf");
    document.close();
  }

  private static void addPartograph(PdfBoxCanvas canvas, String title, float[] durations)
      throws IOException {
    BasePartograph<PdfBox> base = new BasePartograph<PdfBox>();
    base.setLayout(new PdfBoxLayout());
    SpecializedPartograph<PdfBox> partograph = new SpecializedPartograph<PdfBox>(base);
    partograph.setTitle(title);
    partograph.setDurations(durations);
    partograph.render(canvas);
    canvas.close();
  }
}
