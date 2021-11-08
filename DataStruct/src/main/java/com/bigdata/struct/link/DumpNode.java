package com.bigdata.struct.link;

import static com.bigdata.struct.link.ListNode.genListNode;
import static com.bigdata.struct.link.ListNode.printListNode;

/**
 * @author hyt
 * @Description <类描述> 重复数组
 * @create 2021/11/6 18:57
 * @contact 269016084@qq.com
 **/
public class DumpNode {
    public static void main(String[] args) {
        int[] nums = {1, 2, 2, 3, 3, 4, 4, 5};
        ListNode head = genListNode(nums);
        ListNode res = deleteDuplicates(head);
        printListNode(res);
    }


    /**
     * 存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除链表中所有存在数字重复情况的节点，
     * 只保留原始链表中 没有重复出现 的数字。返回同样按升序排列的结果链表。
     **/
    public static ListNode deleteAllDuplicates(ListNode head) {
        if (head == null) {
            return head;
        }
        // 涉及到3个节点的初始化会比较麻烦 可以考虑用一个虚拟节点代替 使得他符合一般场景
        ListNode dummy = new ListNode(0, head);
        ListNode cur = dummy;
        while (cur.next != null && cur.next.next != null) {
            if (cur.next.val == cur.next.next.val) {
                int x = cur.next.val;
                while (cur.next != null && cur.next.val == x) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }
        return dummy.next;
    }

    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.val == cur.next.val) {
                int x = cur.next.val;
                while (cur.next != null && cur.next.val == x) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

}
