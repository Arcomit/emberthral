package mod.arcomit.emberthral.randomizedalgorithm;

import java.util.Random;

public class PRD {
    private static final double EPSILON = 0.0000001;  // 计算精度
    private static final int MAX_ITERATIONS = 1000;   // 最大迭代次数

    private final double baseProbability;  // 基础概率（显示给玩家的概率and我们开发者设置的概率）
    private final double C;                 // PRD算法参数
    private int failureCount;               // 连续失败次数
    private final Random random;

    public PRD(double baseProbability) {
        this.baseProbability = baseProbability;
        this.C = calculateC(baseProbability); // 计算C参数
        this.failureCount = 0;
        this.random = new Random();
    }

    /**
     * 通过二分查找算法计算C值
     * @param p 期望的基础概率
     * @return 计算得到的C值
     */
    private double calculateC(double p) {
        double low = 0.0;
        double high = 1.0;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double mid = (low + high) / 2;
            double calculatedP = calculateExpectedProbability(mid);

            if (Math.abs(calculatedP - p) < EPSILON) {
                return mid;
            } else if (calculatedP < p) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return (low + high) / 2; // 返回近似值
    }

    /**
     * 计算给定C值时的实际期望概率
     * @param C PRD参数
     * @return 实际概率
     */
    private double calculateExpectedProbability(double C) {
        double sum = 0.0;
        double product = 1.0; // 累积失败概率

        for (int n = 1; ; n++) {
            double currentProbability = Math.min(C * n, 1.0);
            double successProb = product * currentProbability;
            sum += n * successProb;

            if (currentProbability >= 1.0) break;
            product *= (1 - currentProbability);

            if (n > 1000) break; // 防止无限循环
        }
        return 1.0 / sum; // 期望概率 = 1 / 平均成功次数
    }

    /**
     * 检查本次是否触发
     * @return 触发结果
     */
    public boolean checkTrigger() {
        failureCount++;
        double currentProbability = C * failureCount;

        if (random.nextDouble() < currentProbability) {
            failureCount = 0; // 触发后重置计数器
            return true;
        }
        return false;
    }

    /**
     * 获取当前尝试的实际概率
     * @return 当前实际概率
     */
    public double getCurrentProbability() {
        return Math.min(C * (failureCount + 1), 1.0);
    }

    // 测试用例
    public static void main(String[] args) {
        double targetProbability = 0.20; // 20%基础概率
        PRD prd = new PRD(targetProbability);

        int totalAttempts = 1_000_000;
        int successCount = 0;
        int totalAttemptsCount = 0;

        // 模拟测试
        for (int i = 0; i < totalAttempts; i++) {
            totalAttemptsCount++;
            if (prd.checkTrigger()) {
                successCount++;
            }
        }

        // 输出统计结果
        System.out.println("基础概率: " + targetProbability);
        System.out.println("计算得到的C值: " + prd.C);
        System.out.println("实际触发概率: " + (double) successCount / totalAttemptsCount);
        System.out.println("理论平均触发间隔: " + (1 / targetProbability));
        System.out.println("实际平均触发间隔: " + (double) totalAttemptsCount / successCount);
    }
}