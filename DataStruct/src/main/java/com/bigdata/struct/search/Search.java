package com.bigdata.struct.search;

import java.lang.reflect.Array;
import java.util.*;

public class Search {
    public static void main(String[] args) {
        char[][] grid = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        System.out.println(numIslands(grid));

    }

    /**
     * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
     * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
     * 此外，你可以假设该网格的四条边均被水包围。
     */
    public static int numIslands(char[][] grid) {
        int res = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '1') {
                    dfSearch(grid, i, j);
                    res++;
                }
            }
        }

        return res;
    }

    /**
     * 深度优先遍历
     *
     * @param grid
     * @param i
     * @param j
     */
    static void dfSearch(char[][] grid, int i, int j) {
        if (i >= 0 && j >= 0 && i <= grid.length - 1 && j <= grid[i].length - 1 && grid[i][j] != '0') {
            if (grid[i][j] == '1') {
                grid[i][j] = '0';
            } else {
                return;
            }
            dfSearch(grid, i + 1, j);
            dfSearch(grid, i - 1, j);
            dfSearch(grid, i, j + 1);
            dfSearch(grid, i, j - 1);
        }


    }

    /**
     * 广度优先遍历
     *
     * @param grid
     * @param i
     * @param j
     */
    static void bfSearch(char[][] grid, int i, int j) {
        Stack<List<Integer>> stack = new Stack<List<Integer>>();
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        tmp.add(i);
        tmp.add(j);
        grid[i][j] = '0';
        stack.push(tmp);
        Integer l;
        Integer r = 0;
        while (!stack.isEmpty()) {
            tmp = (ArrayList) stack.pop();
            l = tmp.get(0);
            r = tmp.get(1);
            isAdd(grid, l + 1, r, stack);
            isAdd(grid, -1, r, stack);
            isAdd(grid, l, r + 1, stack);
            isAdd(grid, l, r - 1, stack);
        }


    }

    static void isAdd(char[][] grid, int i, int j, Stack<List<Integer>> stack) {
        if (i >= 0 && j >= 0 && i <= grid.length - 1 && j <= grid[i].length - 1 && grid[i][j] != '0') {
            ArrayList tmp = new ArrayList<Integer>();
            tmp.add(i);
            tmp.add(j);
            stack.push(tmp);
            grid[i][j] = '0';
        }

    }

    /**
     * 给你一个有 n 个节点的 有向无环图（DAG），请你找出所有从节点 0 到节点 n-1 的路径并输出（不要求按特定顺序）
     * <p>
     * 二维数组的第 i 个数组中的单元都表示有向图中 i 号节点所能到达的下一些节点，空就是没有下一个结点了。
     * <p>
     * 译者注：有向图是有方向的，即规定了 a→b 你就不能从 b→a 。
     * <p>
     * 输入：graph = [[1,2],[3],[3],[]]
     * 输出：[[0,1,3],[0,2,3]]
     * 解释：有两条路径 0 -> 1 -> 3 和 0 -> 2 -> 3
     */
    List<List<Integer>> ans = new ArrayList<List<Integer>>();
    Deque<Integer> stack = new ArrayDeque<Integer>();

    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        stack.offerLast(0);
        dfs(graph, 0, graph.length - 1);
        return ans;
    }

    public void dfs(int[][] graph, int x, int n) {
        if (x == n) {
            ans.add(new ArrayList<Integer>(stack));
            return;
        }
        for (int y : graph[x]) {
            stack.offerLast(y);
            dfs(graph, y, n);
            stack.pollLast();
        }
    }


}
