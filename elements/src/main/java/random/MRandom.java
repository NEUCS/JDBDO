package random;

import java.io.Serializable;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/27 9:32
 */
public class MRandom implements Serializable {
    private static MRandom instance;
    private MRandomGenerator randomGenerator;

    private MRandom() {
        randomGenerator = new RandomGenerator();
    }

    public static MRandom getInstance() {
        if (instance == null) {
            instance = new MRandom();
        }
        return instance;
    }

    public void setRandomGenerator(MRandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public MRandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    public int nextInt(int lowerBound, int upperBound) {
        return randomGenerator.nextInt(lowerBound, upperBound);
    }

    public double nextDouble(double lowerBound, double upperBound) {
        return randomGenerator.nextDouble(lowerBound, upperBound);
    }

    public double nextDouble() {
        return randomGenerator.nextDouble();
    }

    public long getSeed() {
        return randomGenerator.getSeed();
    }

    public String getGeneratorName() {
        return randomGenerator.getName();
    }
}