package mops.zdt;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/24 14:56
 */
public class ZDT2 extends ZDT1 {
    public ZDT2(){
        this(30);
    }

    public ZDT2(Integer integer){
        super(integer);
    }

    @Override
    public double functionH(double f, double g) {
        return 1.0 - Math.pow(f / g, 2);
    }
}