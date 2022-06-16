package optimizerRunner;

import java.io.IOException;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/16 11:15
 */
public class Test {
    public static void main(String[] args) throws IOException {
        DMOPOptimizerRunner runner = new DMOPOptimizerRunner();
        runner.setProblem("DF3").setMetaheuristic("Dynamic NSGA-II").run();
    }
}