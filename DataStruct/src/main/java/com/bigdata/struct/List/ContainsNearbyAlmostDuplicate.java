package com.bigdata.struct.List;

/**
 * @author hyt
 * @Description 给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t
 * ，同时又满足 abs(i - j) <= k 。
 * <p>
 * 如果存在则返回 true，不存在返回 false。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/contains-duplicate-iii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 输入：nums = [1,2,3,1], k = 3, t = 0
 * 输出：true
 * @create 2021/9/3 21:47
 * @contact 269016084@qq.com
 * 思路： 维持一个 k 个的窗口每次判断 窗口k中绝对值最小的 值是多少 滑动窗口 + 有序集合
 *  如果当前窗口有序集合中存在相同元素，那么此时程序将直接返回 \texttt{true}true。因此本题中的有序集合无需处理相同元素的情况。
 **/
public class ContainsNearbyAlmostDuplicate {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 1};
        containsNearbyAlmostDuplicate(numbers, 3, 0);

    }

    public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        for (int i = 0; i < nums.length - k; i++) {
            if (isOK(nums, i, k, t)) {
                return true;
            }
        }

        return false;

    }

    public static boolean isOK(int[] nums, int begin, int k, int t) {
        for (int i = begin; k < nums.length - k; i++) {

        }

        return false;

    }

}
