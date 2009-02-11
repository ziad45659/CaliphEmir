package net.semanticmetadata.lire.imageanalysis;

import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * This file is part of Caliph & Emir.
 *
 * Caliph & Emir is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Caliph & Emir is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Caliph & Emir; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Copyright statement:
 * --------------------
 * (c) 2002-2007 by Mathias Lux (mathias@juggle.at)
 * http://www.juggle.at, http://caliph-emir.sourceforge.net
 */
public class AutoColorCorrelogramTest extends TestCase {
    private String[] testFiles = new String[]{"img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg"};
    private String testFilesPath = "./lire/src/test/resources/small/";
    private static int[] sampleQueries = {284, 77, 108, 416, 144, 534, 898, 104, 67, 10, 607, 165, 343, 973, 591, 659, 812, 231, 261, 224, 227, 914, 427, 810, 979, 716, 253, 708, 751, 269, 531, 699, 835, 370, 642, 504, 297, 970, 929, 20, 669, 434, 201, 9, 575, 631, 730, 7, 546, 816, 431, 235, 289, 111, 862, 184, 857, 624, 323, 393, 465, 905, 581, 626, 212, 459, 722, 322, 584, 540, 194, 704, 410, 267, 349, 371, 909, 403, 724, 573, 539, 812, 831, 600, 667, 672, 454, 873, 452, 48, 322, 424, 952, 277, 565, 388, 149, 966, 524, 36, 528, 75, 337, 655, 836, 698, 230, 259, 897, 652, 590, 757, 673, 937, 676, 650, 297, 434, 358, 789, 484, 975, 318, 12, 506, 38, 979, 732, 957, 904, 852, 635, 620, 28, 59, 732, 84, 788, 562, 913, 173, 508, 32, 16, 882, 847, 320, 185, 268, 230, 259, 931, 653, 968, 838, 906, 596, 140, 880, 847, 297, 77, 983, 536, 494, 530, 870, 922, 467, 186, 254, 727, 439, 241, 12, 947, 561, 160, 740, 705, 619, 571, 745, 774, 845, 507, 156, 936, 473, 830, 88, 66, 204, 737, 770, 445, 358, 707, 95, 349};
    private static String testExtensive = "./lire/wang-data-1000";

    public void testExtraction() throws IOException {
        AutoColorCorrelogram acc = new AutoColorCorrelogram();
        BufferedImage image = ImageIO.read(new FileInputStream(testFilesPath + testFiles[0]));
        System.out.println("image = " + image.getWidth() + " x " + image.getHeight());
        acc.extract(image);
        System.out.println("acc = " + acc.getStringRepresentation());
    }

    public void testPerformance() throws IOException {
        long ms, sum = 0;
        for (int i = 0; i < sampleQueries.length; i++) {
            int id = sampleQueries[i];
            System.out.println("id = " + id + ": ");
            String file = testExtensive + "/" + id + ".jpg";
            AutoColorCorrelogram acc = new AutoColorCorrelogram(AutoColorCorrelogram.Mode.SuperFast);
//            OldColorCorrelogram occ = new OldColorCorrelogram(OldColorCorrelogram.Mode.SuperFast);
            BufferedImage image = ImageIO.read(new FileInputStream(file));
//            occ.extract(image);
            ms = System.currentTimeMillis();
            acc.extract(image);
            ms = System.currentTimeMillis() - ms;
            sum += ms;
//            System.out.println("the same? " + acc.getStringRepresentation().equals(occ.getStringRepresentation()));
        }
        System.out.println("time per image = " + sum / sampleQueries.length);
        System.out.println("sum = " + sum);
    }

    public void testEquality() throws IOException {
        long ms, sum = 0;
        for (int i = 0; i < sampleQueries.length; i++) {
            int id = sampleQueries[i];
            System.out.println("id = " + id + ": ");
            String file = testExtensive + "/" + id + ".jpg";
            AutoColorCorrelogram acc = new AutoColorCorrelogram(AutoColorCorrelogram.Mode.SuperFast);
//            OldColorCorrelogram occ = new OldColorCorrelogram(OldColorCorrelogram.Mode.SuperFast);
            BufferedImage image = ImageIO.read(new FileInputStream(file));
//            occ.extract(image);
            acc.extract(image);
//            System.out.println("the same? " + acc.getStringRepresentation().equals(occ.getStringRepresentation()));
//            if (!acc.getStringRepresentation().equals(occ.getStringRepresentation())) {
//                System.out.println("acc.getStringRepresentation() = " + acc.getStringRepresentation());
//                System.out.println("occ.getStringRepresentation() = " + occ.getStringRepresentation());
//            }
        }
    }

    public void testRetrieval() throws Exception {
        AutoColorCorrelogram[] acc = new AutoColorCorrelogram[testFiles.length];
        LinkedList<String> vds = new LinkedList<String>();
        for (int i = 0; i < acc.length; i++) {
            System.out.println("Extracting from number " + i);
            acc[i] = new AutoColorCorrelogram(AutoColorCorrelogram.Mode.SuperFast);
            acc[i].extract(ImageIO.read(new FileInputStream(testFilesPath + testFiles[i])));
            vds.add(acc[i].getStringRepresentation());
        }

        System.out.println("Calculating distance for " + testFiles[5]);
        for (int i = 0; i < acc.length; i++) {
            AutoColorCorrelogram autoColorCorrelogram = acc[i];
            float distance = acc[i].getDistance(acc[5]);
            System.out.println(testFiles[i] + " distance = " + distance);
        }
        int count = 0;
        for (Iterator<String> iterator = vds.iterator(); iterator.hasNext();) {
            String s = iterator.next();
            AutoColorCorrelogram a = new AutoColorCorrelogram();
            a.setStringRepresentation(s);
            float distance = acc[count].getDistance(a);
            System.out.println(testFiles[count] + " distance = " + distance);
            count++;
        }
    }
}
