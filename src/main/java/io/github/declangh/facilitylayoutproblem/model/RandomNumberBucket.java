package io.github.declangh.facilitylayoutproblem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>A simple class that creates a bucket of numbers within a range from 0 to some boundary.<p/>
 *
 * <p>The class is expected to emulate an actual bucket from which numbers can be drawn. Once removed, a number
 * cannot be picked again until the bucket is empty and is then repopulated with the initial set of numbers.<p/>
 *
 * <p>Makes use of the {@link ThreadLocalRandom} class to make random selections<p/>
 *
 * @author Declan Onunkwo
 */
public class RandomNumberBucket {

    private final int numberOfSides;
    private final ArrayList<Integer> bucket;

    /**
     * @param bucketSize creates a bucket with numbers from 0 (inclusive) to the specified bucket
     *                   size (exclusive)
     */
    public RandomNumberBucket(int bucketSize) {
        this.numberOfSides = bucketSize;
        bucket = new ArrayList<>(bucketSize);
        populateBucket();
    }

    private void populateBucket() {
        bucket.clear();
        for (int i = 0; i < numberOfSides; i++) bucket.add(i);
    }

    /**
     * This method pick one number out of the bucket
     *
     * @return one random number in the bucket
     */
    public int pickOutOne() {
        if (bucket.isEmpty()) populateBucket();
        int randomIdx = ThreadLocalRandom.current().nextInt(0, bucket.size());
        Collections.swap(bucket, randomIdx, bucket.size() - 1);
        int randomNumber = bucket.getLast();
        bucket.remove(randomIdx);
        return randomNumber;
    }

    /**
     * This method picks two numbers out of the bucket
     *
     * @return an array with two randomly picked numbers in the bucket
     */
    public int[] pickOutTwo() {
        if (bucket.size() < 2) populateBucket();
        return new int[]{pickOutOne(), pickOutOne()};
    }

    /**
     * This method picks three numbers out of the bucket
     *
     * @return an array with three randomly picked numbers in the bucket
     */
    public int[] pickOutThree() {
        if (bucket.size() < 3) populateBucket();
        return new int[]{pickOutOne(), pickOutOne(), pickOutOne()};
    }
}
