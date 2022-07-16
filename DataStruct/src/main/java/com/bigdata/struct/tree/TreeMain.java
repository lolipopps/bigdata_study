package com.bigdata.struct.tree;

import java.util.ArrayList;

public class TreeMain {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1); list.add(4);
        list.add(null); list.add(null);
        list.add(3);
        list.add(15);
        TreeNode head = TreeUtil.genTree(list);
        TreeUtil.dfsTree(head);
        System.out.println();
        TreeUtil.bfsTree(head);
        System.out.println();
        System.out.println(TreeUtil.isValidBST(head,Long.MIN_VALUE, Long.MAX_VALUE));
    }


}
