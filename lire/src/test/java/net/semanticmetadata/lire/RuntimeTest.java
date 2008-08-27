package net.semanticmetadata.lire;

import junit.framework.TestCase;
import net.semanticmetadata.lire.utils.FileUtils;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
 * This file is part of LIRe.
 *
 * Caliph & Emir is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LIRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LIRe; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Copyright statement:
 * --------------------
 * (c) 2002-2008 by Mathias Lux (mathias@juggle.at)
 * http://www.juggle.at, http://www.SemanticMetadata.net
 */

/**
 * This file is part of LIRe
 * Date: 31.01.2006
 * Time: 23:59:45
 *
 * @author Mathias Lux, mathias@juggle.at
 */
public class RuntimeTest extends TestCase {
    private String[] testFiles = new String[]{"img01.JPG", "img02.JPG", "img03.JPG", "img04.JPG", "img05.JPG",
            "img06.JPG", "img07.JPG", "img08.JPG", "img08a.JPG", "error.jpg", "P�ginas de 060305_b_P�gina_1_Imagem_0004_P�gina_08_Imagem_0002.jpg"};
    private String testFilesPath = "./lire/src/test/resources/images/";
    private String indexPath = "test-index";
    private String testExtensive = "./lire/wang-data-1000";

    public void testCreateIndex() throws IOException {
        DocumentBuilder builder = DocumentBuilderFactory.getExtensiveDocumentBuilder();
        IndexWriter iw = new IndexWriter(indexPath + "-small", new SimpleAnalyzer(), true);
        for (String identifier : testFiles) {
            System.out.println("Indexing file " + identifier);
            Document doc = builder.createDocument(new FileInputStream(testFilesPath + identifier), identifier);
            iw.addDocument(doc);
        }
        iw.optimize();
        iw.close();
    }

    public void testCreateCorrelogramIndex() throws IOException {
        String[] testFiles = new String[]{"img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg"};
        String testFilesPath = "./lire/src/test/resources/small/";

        DocumentBuilder builder = DocumentBuilderFactory.getDefaultAutoColorCorrelationDocumentBuilder();
        IndexWriter iw = new IndexWriter(indexPath + "-small", new SimpleAnalyzer(), true);
        long ms = System.currentTimeMillis();
        for (String identifier : testFiles) {
            Document doc = builder.createDocument(new FileInputStream(testFilesPath + identifier), identifier);
            iw.addDocument(doc);
        }
        System.out.println("Time taken: " + ((System.currentTimeMillis() - ms) / testFiles.length) + " ms");
        iw.optimize();
        iw.close();
    }

    public void testCreateCEDDIndex() throws IOException {
        String[] testFiles = new String[]{"img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg"};
        String testFilesPath = "./lire/src/test/resources/small/";

        DocumentBuilder builder = DocumentBuilderFactory.getCEDDDocumentBuilder();
        IndexWriter iw = new IndexWriter(indexPath + "-cedd", new SimpleAnalyzer(), true);
        long ms = System.currentTimeMillis();
        for (String identifier : testFiles) {
            Document doc = builder.createDocument(new FileInputStream(testFilesPath + identifier), identifier);
            iw.addDocument(doc);
        }
        System.out.println("Time taken: " + ((System.currentTimeMillis() - ms) / testFiles.length) + " ms");
        iw.optimize();
        iw.close();
    }

//    public void testCreateExtensiveIndex() throws IOException {
//        ArrayList<String> images = FileUtils.getAllImages(new File(testExtensive), true);
//        indexFiles(images);
//    }

    /**
     * Tests the runtime for creating an index based on the Wang data set.
     *
     * @throws IOException
     */
    public void testCreateBigIndex() throws IOException {
        ArrayList<String> images = FileUtils.getAllImages(new File(testExtensive), true);
        indexFiles("ColorHist: ", images, DocumentBuilderFactory.getColorHistogramDocumentBuilder(), indexPath + "-extensive");
        indexFiles("CEDD: ", images, DocumentBuilderFactory.getCEDDDocumentBuilder(), indexPath + "-extensive");
        indexFiles("ColorHist: ", images, DocumentBuilderFactory.getColorHistogramDocumentBuilder(), indexPath + "-extensive");
        indexFiles("ACC: ", images, DocumentBuilderFactory.getDefaultAutoColorCorrelationDocumentBuilder(), indexPath + "-extensive");
        indexFiles("FCTH: ", images, DocumentBuilderFactory.getFCTHDocumentBuilder(), indexPath + "-extensive");
        indexFiles("Gabor: ", images, DocumentBuilderFactory.getGaborDocumentBuilder(), indexPath + "-extensive");
        indexFiles("Tamura: ", images, DocumentBuilderFactory.getTamuraDocumentBuilder(), indexPath + "-extensive");
        indexFiles("MPEG7: ", images, DocumentBuilderFactory.getExtensiveDocumentBuilder(), indexPath + "-extensive");
        indexFiles("All: ", images, DocumentBuilderFactory.getFullDocumentBuilder(), indexPath + "-extensive");
    }

    private void indexFiles(String prefix, ArrayList<String> images, DocumentBuilder builder, String indexPath) throws IOException {
        System.out.println(">> Indexing " + images.size() + " files.");
        IndexWriter iw = new IndexWriter(indexPath, new SimpleAnalyzer(), true);
        int count = 0;
        long time = System.currentTimeMillis();
        for (String identifier : images) {
            Document doc = builder.createDocument(new FileInputStream(identifier), identifier);
            iw.addDocument(doc);
            count++;
            if (count % 100 == 0) System.out.print((100 * count) / images.size() + "% ");
        }
        long timeTaken = (System.currentTimeMillis() - time);
        float sec = ((float) timeTaken) / 1000f;
        System.out.println("");
        System.out.println(prefix + sec + " seconds taken, " + (timeTaken / count) + " ms per image.");
        // iw.optimize();
        iw.close();
    }
}