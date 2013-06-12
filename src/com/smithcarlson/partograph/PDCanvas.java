package com.smithcarlson.partograph;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class PDCanvas implements Canvas {

  private final PDDocument document;
  private final PDPage page;
  private final PDPageContentStream contentStream;

  public PDCanvas(PDDocument document, PDPage page) throws IOException {
    this.document = document;
    this.page = page;
    contentStream = new PDPageContentStream(document, page);
 }

}
