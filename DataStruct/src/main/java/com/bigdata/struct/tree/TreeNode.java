package com.bigdata.struct.tree;


import java.util.HashMap;

class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {
    }

    public TreeNode(int _val) {
        val = _val;
    }

    public TreeNode(int _val, TreeNode _left, TreeNode _right) {
        val = _val;
        left = _left;
        right = _right;
    }

    public TreeNode genTree(int[] nums) {
        HashMap<Integer, TreeNode> mapTree = new HashMap<Integer, TreeNode>();
        if (nums.length > 0) {
            TreeNode head = new TreeNode(nums[0]);
            for (int i = 1; i < nums.length - 1; i++) {
                TreeNode tmp;
                if (mapTree.containsKey(i)) {
                    tmp = mapTree.get(i);
                } else {
                    tmp = new TreeNode(nums[i]);
                    mapTree.put(i, tmp);
                }
                if (2 * i < nums.length - 1) {

                }

            }
            return head;
        } else {
            return null;
        }
    }
}