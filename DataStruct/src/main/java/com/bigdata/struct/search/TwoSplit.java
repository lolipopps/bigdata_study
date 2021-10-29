package com.bigdata.struct.search;

/**
 * @author huyingttai
 * @Description 二分法查找
 * @create 8:56 下午 2021/10/28
 * 如果在数组中找不到目标，返回[-1，-1 ]。
 * 例如，给定[5, 7, 7，8, 8, 10 ]和目标值8，返回[3, 4 ]。
 */

public class TwoSplit {
    public static void main(String[] args) {
        int[] nums = {5,7, 7,8, 8, 10 };
        int target = 8;
        twoSplitTwoSearch(nums,target);
        System.out.println((twoSplitLessSearch(nums, target)));
        System.out.println((twoSplitMoreSearch(nums, target)));
        System.out.println((twoSplitSearch(nums, target)));
    }

    /*
     * 如果在数组中找不到目标，返回[-1，-1 ]。
     * 例如，给定[5, 7, 7，8, 8, 10 ]和目标值8，返回[3, 4 ]。
     * 分别调用 找最左边 和 最右边的值
     */
    public static int[] twoSplitTwoSearch(int[] nums, int target) {
        int[] res = {-1, -1};
        int left = twoSplitLessSearch(nums, target);
        int right = twoSplitMoreSearch(nums, target);
        if (left == 0) {
            return new int[]{-1, -1};
        } else {
            return new int[]{left, right};
        }

    }

    /*
     * 二分查找
     */
    public static int twoSplitSearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int mid = 0;
        int res = 0;
        // 判断是否到了终点 当left = right 时 min = left = right 因此需要 left 或者 right +1 -1 用来退出循环
        while (left <= right) {
            mid = (left + right) / 2;
            if (nums[mid] == target) {
                res = mid;
                break;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (left == right) {
            return 0;
        }
        return res;
    }

    /*
     * 二分查找最左边等于的
     */
    public static int twoSplitLessSearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int mid = (left + right) / 2;
        int res = 0;
        // 判断是否到了终点 当left = right 时 min = left = right 因此需要 left 或者 right +1 -1 用来退出循环
        while (left <= right) {
            if (nums[mid] >= target) {
                if (nums[mid] == target) {
                    res = mid;
                }
                right = mid - 1;
            } else {
                left = mid + 1;
            }
            mid = (right + left) / 2;
        }

        return res;
    }

    /*
     * 二分查找最右边等于的
     */
    public static int twoSplitMoreSearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int mid = (left + right) / 2;
        int res = 0;
        // 判断是否到了终点 当left = right 时 min = left = right 因此需要 left 或者 right +1 -1 用来退出循环
        while (left <= right) {
            if (nums[mid] <= target) {
                if (nums[mid] == target) {
                    res = mid;
                }
                left = mid + 1;
            } else {
                right = mid - 1;
            }
            mid = (right + left) / 2;

        }

        return res;
    }
}
