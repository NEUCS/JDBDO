package mops.zdt;


public class ZDT3 extends ZDT1 {
    public ZDT3() {
        this(30);
    }

    public ZDT3(Integer integer) {
        super(integer);
    }

    @Override
    public double functionH(double f, double g) {
        return 1.0 - Math.sqrt(f / g) - (f / g) * Math.sin(10.0 * Math.PI * f);
    }
}