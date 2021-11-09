package com.bigdata.struct.dp;

/**
 * @author huyingttai
 * @Description 二维dp
 * @create 10:03 上午 2021/11/2
 */

public class TwoDim {
    public static void main(String[] args) {
        int[][] nums = {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
        int[][] nums1 = {{1, 2, 3}, {4, 5, 6}};
        System.out.println(LessLongpath(nums));

    }

    /**
     * @param
     * @return  123
     * @description 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
     * 说明：每次只能向下或者向右移动一步。
     * @author huyingtai
     * @date 2021/11/2 10:05 上午
     **/
    public static int LessLongpath(int[][] nums) {
        int[][] dp = new int[nums.length][nums[0].length];
        dp[0][0] = nums[0][0];
        // 第一行初始化
        for (int i = 1; i < nums[0].length; i++) {
            dp[0][i] = dp[0][i - 1] + nums[0][i];
        }

        // 第一列初始化
        for (int i = 1; i < nums.length; i++) {
            dp[i][0] = dp[i - 1][0] + nums[i][0];
        }

        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j < nums[i].length; j++) {
                dp[i][j] = Math.min(dp[i - 1][j] + nums[i][j], dp[i][j - 1] + nums[i][j]);
            }
        }
        return dp[nums.length - 1][nums[nums.length - 1].length - 1];

    }

}
