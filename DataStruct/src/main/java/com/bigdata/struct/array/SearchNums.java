package com.bigdata.struct.array;

import java.util.*;

public class SearchNums {

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        int target = 0;
//        System.out.println(maxArea(nums));
        System.out.println(threeSum(nums, 0));
        System.out.println(maxSubArray(nums));
        System.out.println(dpMaxSubArray1(nums));
        System.out.println(dpMaxSubArray(nums));
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
     *
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

    /**
     * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？
     * 请你找出所有和为 0 且不重复的三元组。
     * nums = [-1,0,1,2,-1,-4]
     * [[-1,-1,2],[-1,0,1]]
     */
    public static List<List<Integer>> threeSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (nums == null || nums.length < 3) {
            return res;
        }
        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0) {
                return res;
            }
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int l = i + 1;
            int r = nums.length - 1;
            while (l < r) {
                if (nums[l] + nums[r] + nums[i] == 0) {
                    ArrayList<Integer> tmp = new ArrayList<Integer>();
                    tmp.add(nums[i]);
                    tmp.add(nums[l]);
                    tmp.add(nums[r]);
                    res.add(tmp);
                    l++;
                    r--;
                    // 剔除相同的元素
                    while (l < r && nums[l] == nums[l - 1]) l++;
                    while (l < r && nums[r] == nums[r + 1]) r--;
                } else if (nums[l] + nums[r] + nums[i] > 0) {
                    r--;
                } else {
                    l++;
                }
            }

        }
        return res;
    }

    public boolean containsDuplicate(int[] nums) {
        HashSet<Integer> numSet = new HashSet();
        for (int num : nums) {
            if (numSet.contains(num)) {
                return true;
            } else {
                numSet.add(num);
            }
        }
        return false;
    }

    /**
     * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     *
     * @param nums
     * @return
     */
    public static int maxSubArray(int[] nums) {
        int pre = 0, res = nums[0];
        for (int x : nums) {
            // 之前和当前值相加 和当前之对比 当前指针所指元素之前的和 小于 0 则丢弃之前的数列  巧妙的应用 小于零相加
            pre = Math.max(pre + x, x);
            // 当前值与最大值比较
            res = Math.max(res, pre);
        }
        return res;

    }

    public static int dpMaxSubArray(int[] nums) {
        int pre = 0, res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > 0) {
                nums[i] = nums[i - 1] + nums[i];
            }
        }
        return Arrays.stream(nums).max().getAsInt();

    }

    public static int dpMaxSubArray1(int[] nums) {
        int pre = nums[0];
        int res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (pre < 0) {
                pre = nums[i];
            } else {
                pre = pre + nums[i];
                res = Math.max(pre, res);
            }
        }
        return res;

    }
}
