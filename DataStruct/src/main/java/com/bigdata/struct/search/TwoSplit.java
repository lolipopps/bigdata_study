package com.bigdata.struct.search;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author hyt
 * @Description <类描述> 二分查找
 * @create 2021/10/24 11:53
 * @contact 269016084@qq.com
 **/
public class TwoSplit {

    /***
     给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
     如果数组中不存在目标值 target，返回 [-1, -1]。
     进阶：你可以设计并实现时间复杂度为 O(log n) 的算法解决此问题吗？
     */

    public static void main(String[] args) {
        int[] nums = {4,5,6,7,0,1,2};
        int target = 8;
        System.out.println(search(nums, target));

    }

    public static int[] searchRange(int[] nums, int target) {
        int leftIdx = binarySearch(nums, target, true);
        int rightIdx = binarySearch(nums, target, false) - 1;
        if (leftIdx <= rightIdx && rightIdx < nums.length && nums[leftIdx] == target && nums[rightIdx] == target) {
            return new int[]{leftIdx, rightIdx};
        }
        return new int[]{-1, -1};
    }


    public static int binarySearch(int[] nums, int target, boolean lower) {
        int left = 0, right = nums.length - 1, ans = nums.length;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] > target || (lower && nums[mid] >= target)) {
                right = mid - 1;
                ans = mid;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    public static int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1, ans = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return mid + 1;

            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (ans == -1) {
            return ans;
        } else {
            return ans + 1;
        }
    }

    /**
     * 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转，使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]
     * （下标 从 0 开始 计数）。例如， [0,1,2,4,5,6,7] 在下标 3 处经旋转后可能变为 [4,5,6,7,0,1,2,3] 。
     * 给你 旋转后 的数组 nums 和一个整数 target ，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回 -1 。
     * 输入：nums = [4,5,6,7,0,1,2], target = 0
     * 输出：4
     */
    public static int search(int[] nums, int target) {
        int res = -1;
        int left = 0, right = nums.length - 1, mid = (left + right) / 2;
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < nums[left]) { // 如果 中间的小于第一个 代表 右边有序
            int[] nums2 = Arrays.copyOfRange(nums, mid + 1, right);
            res = binarySearch(nums2, target);
            if (res != -1) {  // 右边没找到
                return mid + res;
            } else {
                return search(Arrays.copyOfRange(nums, left, mid), target);
            }
        } else {   // 如果 中间的大于第一个 代表左边有序
            int[] nums3 = Arrays.copyOfRange(nums, left, mid);
            res = binarySearch(nums3, target);
            if (res != -1) {  // 右边没找到
                return res;
            } else {
               res = mid + search(Arrays.copyOfRange(nums, mid + 1, right+1), target);
            }
        }
        return res;
    }

}
