package com.bigdata.struct.array;

public class SearchNums {

    public static void main(String[] args) {
        int[] nums = {4, 3, 2, 1, 4};
        int target = 0;
        System.out.println(maxArea(nums));

    }


    /**
     * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，
     * 使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
     * 注意：答案中不可以包含重复的三元组。
     **/
    public static int search(int[] nums, int target) {
        int res = -1;
        return res;
    }

    /**
     * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 
     * 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     * 暴力法 遍历任意两个之间的面积存下来
     */
    public static int maxArea(int[] height) {
        int maxArea = 0;
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int area = Math.min(height[i], height[j]) * (j - i);
                if (maxArea < area) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    /**
     * 双指针移动法 控制一个变量的思想 宽度 最大尽量不要动 ，动 最短的
     * @param height
     * @return
     */
    public static int maxArea1(int[] height) {

        int l = 0, r = height.length - 1;
        int ans = 0;
        while (l < r) {
            int area = Math.min(height[l], height[r]) * (r - l);
            ans = Math.max(ans, area);
            if (height[l] <= height[r]) {  // 移动较小的那一端
                ++l;
            } else {
                --r;
            }
        }
        return ans;


    }

}
