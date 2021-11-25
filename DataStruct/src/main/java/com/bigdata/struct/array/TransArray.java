package com.bigdata.struct.array;

import java.util.*;

public class TransArray {

    public static void main(String[] args) {
//        int[] nums = {4, 5, 6, 7, 0, 1, 2};
//        int[] num1 = {2, 0};
//        int[] num2 = {1};
//        int target = 0;
//        search(nums, target);
//        merge(num1, 1, num2, 1);
        combinationSums(new int[]{2, 3, 6, 7}, 7);
    }

    /**
     * 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转，使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]
     * （下标 从 0 开始 计数）。例如， [0,1,2,4,5,6,7] 在下标 3 处经旋转后可能变为 [4,5,6,7,0,1,2] 。
     * 给你 旋转后 的数组 nums 和一个整数 target ，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回 -1 。
     * 输入：nums = [4,5,6,7,0,1,2], target = 0
     * 输出：4
     */
    public static int search(int[] nums, int target) {
        int res = -1;
        return res;
    }

    /**
     * 给你两个按 非递减顺序 排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n ，分别表示 nums1 和 nums2 中的元素数目。
     * <p>
     * 请你 合并 nums2 到 nums1 中，使合并后的数组同样按 非递减顺序 排列。
     */
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n != 0) {
            int[] tmp = Arrays.copyOfRange(nums1, 0, m);
            int lb = 0;
            int rb = 0;
            for (int i = 0; i < nums1.length; i++) {
                if (rb >= n || (lb < m && tmp[lb] <= nums2[rb])) {
                    nums1[i] = tmp[lb];
                    lb++;
                } else {
                    nums1[i] = nums2[rb];
                    rb++;
                }
            }
        }


    }

    /**
     * 给定两个数组，编写一个函数来计算它们的交集。
     */
    public static int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> intMap = new HashMap<>();

        List<Integer> res = new ArrayList<>();
        for (int num : nums1) {
            if (intMap.containsKey(num)) {
                intMap.put(num, intMap.get(num) + 1);
            } else {
                intMap.put(num, 1);
            }
        }
        for (int num : nums2) {
            if (intMap.containsKey(num) && intMap.get(num) > 0) {
                intMap.put(num, intMap.get(num) - 1);
                res.add(num);
            }
        }
        return res.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * 给定一个无重复元素的正整数数组 candidates 和一个正整数 target ，找出 candidates 中所有可以使数字和为目标数 target 的唯一组合。
     * <p>
     * candidates 中的数字可以无限制重复被选取。如果至少一个所选数字数量不同，则两种组合是唯一的。 
     * <p>  candidates = [2,3,6,7], target = 7
     * 对于给定的输入，保证和为 target 的唯一组合数少于 150 个。
     */


    public static void combinationSumRe(int[] candidates, int target, List<List<Integer>> res, ArrayList<Integer> tmp, int index) {
        if (index == candidates.length) {
            return;
        }
        if (target == 0) {
            ArrayList<Integer> re = new ArrayList<>();
            re.addAll(tmp);
            res.add(re);
            return;
        }
        // 当循环用
        combinationSumRe(candidates, target, res, tmp, index + 1);
        // 直接跳过
        if (target - candidates[index] >= 0) {
            tmp.add(candidates[index]);
            combinationSumRe(candidates, target - candidates[index], res, tmp, index);
            tmp.remove(tmp.size() - 1);
        }
    }

    public static void combinationSumRe(int[] candidates, int target, List<List<Integer>> res, ArrayList<Integer> tmp) {
        if (target == 0) {
            ArrayList<Integer> re = new ArrayList<>();
            re.addAll(tmp);
            res.add(re);
            return;
        }
        // 当循环用
        for (int i = 0; i < candidates.length; i++) {
            tmp.add(candidates[i]);
            if (target - candidates[i] >= 0) {
                combinationSumRe(candidates, target - candidates[i], res, tmp);
            }
            tmp.remove(tmp.size() - 1);
        }
    }

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        ArrayList<Integer> tmp = new ArrayList<>();
        combinationSumRe(candidates, target, res, tmp);
        System.out.println(res);
        return res;
    }

    public static List<List<Integer>> combinationSums(int[] candidates, int target) {
        int len = candidates.length;
        List<List<Integer>> res = new ArrayList<>();
        if (len == 0) {
            return res;
        }

        Deque<Integer> path = new ArrayDeque<>();
        dfs(candidates, 0, len, target, path, res);
        System.out.println(res);
        return res;
    }

    /**
     * @param candidates 候选数组
     * @param begin      搜索起点
     * @param len        冗余变量，是 candidates 里的属性，可以不传
     * @param target     每减去一个元素，目标值变小
     * @param path       从根结点到叶子结点的路径，是一个栈
     * @param res        结果集列表
     */
    private static void dfs(int[] candidates, int begin, int len, int target, Deque<Integer> path, List<List<Integer>> res) {
        // target 为负数和 0 的时候不再产生新的孩子结点
        if (target < 0) {
            return;
        }
        if (target == 0) {
            res.add(new ArrayList<>(path));
            return;
        }

        // 重点理解这里从 begin 开始搜索的语意
        for (int i = begin; i < len; i++) {
            path.addLast(candidates[i]);
            // 注意：由于每一个元素可以重复使用，下一轮搜索的起点依然是 i，这里非常容易弄错
            dfs(candidates, i, len, target - candidates[i], path, res);
            // 状态重置
            path.removeLast();
        }
    }



}
