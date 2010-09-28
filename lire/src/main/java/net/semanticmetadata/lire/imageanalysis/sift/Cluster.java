/*
 * This file is part of the LIRE project: http://www.SemanticMetadata.net/lire.
 *
 * Lire is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Lire is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lire; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * (c) 2008-2010 by Mathias Lux, mathias@juggle.at
 */
package net.semanticmetadata.lire.imageanalysis.sift;

import net.semanticmetadata.lire.utils.SerializationUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

/**
 * Provides a simple implementation for a cluster used with the visual bag of words approach.
 * User: Mathias Lux, mathias@juggle.at
 * Date: 26.03.2010
 * Time: 12:10:19
 */
public class Cluster implements Comparable<Object> {
    float[] mean;
    HashSet<Integer> members = new HashSet<Integer>();

    public Cluster() {
        this.mean = new float[4 * 4 * 8];
    }

    public Cluster(float[] mean) {
        this.mean = mean;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(512);
        for (Integer integer : members) {
            sb.append(integer);
            sb.append(", ");
        }
        for (int i = 0; i < mean.length; i++) {
            sb.append(mean[i]);
            sb.append(';');
        }
        return sb.toString();
    }

    public int compareTo(Object o) {
        return ((Cluster) o).members.size() - members.size();
    }

    public double getDistance(Feature f) {
        double d = 0;
        for (int i = 0; i < mean.length; i++) {
            double a = mean[i] - f.descriptor[i];
            d += a * a;
        }
        return Math.sqrt(d);
    }

    /**
     * Creates a byte array representation from the clusters mean.
     *
     * @return the clusters mean as byte array.
     */
    public byte[] getByteRepresentation() {
        return SerializationUtils.toBytes(mean);
    }

    public void setByteRepresentation(byte[] data) {
        mean = SerializationUtils.toFloatArray(data);
    }

    public static void writeClusters(Cluster[] clusters, String file) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(SerializationUtils.toBytes(clusters.length));
        for (int i = 0; i < clusters.length; i++) {
            fout.write(clusters[i].getByteRepresentation());
        }
        fout.close();
    }

    public static Cluster[] readClusters(String file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        byte[] tmp = new byte[4];
        fin.read(tmp, 0, 4);
        Cluster[] result = new Cluster[SerializationUtils.toInt(tmp)];
        tmp = new byte[128*4];
        for (int i = 0; i < result.length; i++) {
            int bytesRead = fin.read(tmp, 0, 128*4);
            if (bytesRead != 128*4) System.err.println("Didn't read enough bytes ...");
            result[i] = new Cluster();
            result[i].setByteRepresentation(tmp);
        }
        fin.close();
        return result;
    }

}
