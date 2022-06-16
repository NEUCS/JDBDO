package random;

import java.util.Random;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/27 9:31
 */
public class RandomGenerator implements MRandomGenerator {
    private Random random;
    private long seed;
    private static final String NAME = "RandomGenerator";

    public RandomGenerator(long seed) {
        this.seed = seed;
        random = new Random(seed);
    }

    public RandomGenerator() {
        this(System.currentTimeMillis());
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int nextInt(int lowerBound, int upperBound) {
        return lowerBound + random.nextInt((upperBound - lowerBound + 1));
    }

    @Override
    public double nextDouble(double lowerBound, double upperBound) {
        return lowerBound + random.nextDouble() * (upperBound - lowerBound);
    }

    @Override
    public double  nextDouble() {
        return nextDouble(0.0, 1.0);
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
        random.setSeed(seed);
    }

    @Override
    public String getName() {
        return NAME;
    }
}