package com.smithcarlson.partograph;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Render {
  public static void main(String[] args) throws IOException, COSVisitorException {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage(PDPage.PAGE_SIZE_LETTER);
    document.addPage(page);
    PDCanvas canvas = new PDCanvas(document, page);
    addPartograph(canvas, "Nulliparous, BMI <25",
        new float[] { 5f, 3f, 2f, 2f, 1.5f, 1.5f });
    // addPartograph(document, "Nulliparous, BMI 25-30", new float[] { 5f, 3f,
    // 2f, 1.5f, 1.5f, 1.75f });
    // addPartograph(document, "Nulliparous, BMI 30-40", new float[] { 5.5f,
    // 3.25f, 2.25f, 1.5f, 1.5f, 1.75f });
    // addPartograph(document, "Nulliparous, BMI >40", new float[] { 7.5f,
    // 4.25f, 2.5f, 1.75f, 1.5f, 1.75f });
    document.save("partographs.pdf");
    document.close();
  }

  private static void addPartograph(PDCanvas canvas, String title, float[] durations)
      throws IOException {
    Partograph partograph = new Partograph();
    partograph.setTitle(title);
    partograph.setDurations(durations);
    partograph.render(canvas);
    canvas.close();
  }
}
