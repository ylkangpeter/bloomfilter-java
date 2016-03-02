package com.peter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

/**
 * <href a="https://en.wikipedia.org/wiki/Bloom_filter">wiki</href>
 * Created by ylkang on 3/2/16.
 */
public class BloomFilter<E> {
    private static final String CHARSET = "UTF-8";

    private int size;
    private BitSet bitSet_High;
    private BitSet bitSet_Low;

    private int hashFuncs = 4; // http://pages.cs.wisc.edu/~cao/papers/summary-cache/node8.html


    private MessageDigest digest;

    public BloomFilter() {
        try {
            this.digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //TODO proper log
        }
        this.size = Integer.MAX_VALUE;
        this.bitSet_High = new BitSet(size);
        this.bitSet_Low = new BitSet(size);
    }


    public boolean exists(E element) {
        try {
            long[] hashes = calcHashes(element.toString().getBytes(CHARSET));
            for (int i = 0; i < 4; i++) {
                if (!bitSet_Low.get((int) (hashes[i] % size))) {
                    return false;
                }
            }
            for (int i = 4; i < 8; i++) {
                if (!bitSet_High.get((int) (hashes[i] % size))) {
                    return false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();   //TODO proper log
        }
        return true;
    }

    public void add(E element) {
        try {
            long[] hashes = calcHashes(element.toString().getBytes(CHARSET));
            for (int i = 0; i < 4; i++) {
                bitSet_Low.set((int) (hashes[i] % size));
            }
            for (int i = 4; i < 8; i++) {
                bitSet_High.set((int) (hashes[i] % size));
            }
        } catch (Exception e) {
            System.err.println(element);
            e.printStackTrace();   //TODO proper log
        }
    }

    private long[] calcHashes(byte[] bytes) {
        long[] result = new long[hashFuncs * 2];
        byte salt = 0;
        for (int i = 0; i < result.length; i += 2) {
            digest.update(salt);
            byte[] message = digest.digest(bytes);
            for (int j = 0; j < 4; j++) {
                result[i] <<= 8;
                result[i] |= (int) message[j] & 0xFF;
            }
            for (int j = 4; j < 8; j++) {
                result[i + 1] <<= 8;
                result[i + 1] |= (int) message[j] & 0xFF;
            }
        }
        return result;
    }


    public static void main(String[] args) {
        BloomFilter<String> bf = new BloomFilter();
        for (int i = 0; i < 100000; i++) {
            bf.add(String.valueOf(i));
        }
        for (int i = 0; i < 100000; i++) {
            if (!bf.exists(String.valueOf(i))) {
                System.out.println(i);
            }
        }

        for (int i = 100001; i < 300000; i++) {
            if (bf.exists(String.valueOf(i))) {
                System.out.println(i);
            }
        }
    }
}
