package com.bigdata.struct.tree;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

class TreeUtil {
    public static TreeNode genTree(ArrayList<Integer> nums) {
        HashMap<Integer, TreeNode> mapTree = new HashMap<Integer, TreeNode>();
        if (nums.size() > 0) {
            TreeNode head = new TreeNode(nums.get(0));
            mapTree.put(0, head);
            for (int i = 1; i <= nums.size() - 1; i++) {
                int pId = (i - 1) / 2;
                if (nums.get(i) != null) {
                    TreeNode tmp = new TreeNode(nums.get(i));
                    TreeNode pTree = mapTree.get(pId);
                    mapTree.put(i, tmp);
                    if ((pId + 1) * 2 == i) {
                        pTree.right = tmp;
                    } else {
                        pTree.left = tmp;
                    }
                }
            }
            return head;
        } else {
            return null;
        }
    }

    public static void bfsTree(TreeNode head) {
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(head);
        TreeNode curr;
        while (queue.size() != 0) {
            curr = queue.poll();
            System.out.print(curr.val + "->");
            if (curr.left != null)
                queue.add(curr.left);
            if (curr.right != null)
                queue.add(curr.right);
        }

    }


    public static void dfsTree(TreeNode head) { // 先序遍历
        if (head != null) {
            System.out.print(head.val + "->");
            dfsTree(head.left);
            dfsTree(head.right);
        }

    }


    public static int getTreeDeep(TreeNode head) { // 先序遍历
        if (head != null) {
            int left = getTreeDeep(head.left);
            int right = getTreeDeep(head.right);
            int max = Math.max(left, right) + 1;
            return max;
        } else {
            return 0;
        }
    }

    public static Boolean isValidBST(TreeNode root, long minVal, long maxVal) { // 判断是否二叉搜索树
        if (root == null)
            return true;
        //每个节点如果超过这个范围，直接返回false
        if (root.val >= maxVal || root.val <= minVal)
            return false;
        //这里再分别以左右两个子节点分别判断，
        //左子树范围的最小值是minVal，最大值是当前节点的值，也就是root的值，因为左子树的值要比当前节点小
        //右子数范围的最大值是maxVal，最小值是当前节点的值，也就是root的值，因为右子树的值要比当前节点大
        return isValidBST(root.left, minVal, root.val) && isValidBST(root.right, root.val, maxVal);

    }


}