package com.bigdata.struct.array;

import java.util.HashMap;

public class ArraySum {
    /**
     * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
     */
    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -4};
        int target = 0;
        int[] nums1 = {7, 1, 5, 3, 6, 4};
        System.out.println(twoSum(nums, 3));
        System.out.println(maxProfit(nums1));

    }

    public static int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> index = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (index.get(target - nums[i]) != null) {
                return new int[]{i, index.get(target - nums[i])};
            } else {
                index.put(nums[i], i);
            }
        }
        return null;

    }

    /**
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
     * <p>
     * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
     *
     * @param prices
     * @return
     */
    public static int maxProfit(int[] prices) {
        int res = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            res = Math.max(prices[i] - min, res);
            // 记录左边最小的值
            if (min > prices[i]) {
                min = prices[i];
            }
        }
        return res;
    }


}
