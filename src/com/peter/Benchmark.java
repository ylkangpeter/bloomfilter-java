package com.peter;

/**
 * Created by ylkang on 3/2/16.
 */
public class Benchmark {

    public void testFunction() {
        int elements = 1000000;
        BloomFilter<String> filter = new BloomFilter<String>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < elements; i++) {
            filter.add(String.valueOf(i)); // or use int directly to boost
        }
        System.out.println(String.format("save: %d(s)/%d(elements)", (System.currentTimeMillis() - start
        ), elements));


        start = System.currentTimeMillis();
        // exists?
        for (int i = 0; i < elements; i++) {
            if (!filter.exists(String.valueOf(i))) { // or use int directly to boost
                System.err.println(String.format("%d not in filter", i));
                return;
            }
        }
        System.out.println(String.format("get: %d(s)/%d(elements)", (System.currentTimeMillis() - start
        ), elements));

        // exists?
        for (int i = elements * 2; i < elements * 3; i++) {
            if (filter.exists(String.valueOf(i))) { // or use int directly to boost
                System.err.println(String.format("%d in filter", i));
            }
        }
    }

    public static void main(String[] args) {
        Benchmark bm = new Benchmark();
        bm.testFunction();
    }
}
