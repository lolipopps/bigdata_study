package com.bigdata.hbase;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ZookeeperUtils {


    private final Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);
    private final String url = "hyt2:2181";
    private final int baseSleepTimeMs = 1000;
    private final int maxRetries = 1;
    private final int sessionTimeoutMs = 8000;
    private final int connectionTimeoutMs = 8000;
    CuratorFramework client = null;


    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.newClient(url, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();
    }

    public ZookeeperUtils() {
        init();
    }

    //获取客户端
    public CuratorFramework getClient() {
        return client;
    }

    //创建节点
    public boolean createZnode(String path, String data, CreateMode createMode) {
        try {
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, data.getBytes());
            logger.info("节点创建成功,Path:" + path + ",content:" + data);
            return true;
        } catch (Exception e) {
            logger.error("节点创建失败,Path:" + path + ",content:" + data + ",errMsg:" + e.getMessage(), e);
        }
        return false;
    }

    //删除节点
    public boolean deleteZnode(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
            logger.info("删除{}成功", path);
            return true;
        } catch (Exception e) {
            logger.error("删除失败,Path:" + path + "errMsg" + e.getMessage(), e);
        }
        return false;
    }

    //更新节点信息
    public boolean setZodeData(String path, String data) {
        try {
            client.setData().forPath(path, data.getBytes());
            logger.info("信息修改成功,Path:" + path + ",content:" + data);
            return true;
        } catch (Exception e) {
            logger.error("信息修改失败,Path:" + path + ",content:" + data + ",errMsg" + e.getMessage(), e);
        }
        return false;
    }

    //查看节点信息
    public String getZnodeData(String path) {
        try {
            byte[] bytes = client.getData().forPath(path);
            logger.info("成功获取信息,Path:" + path);
            return new String(bytes);
        } catch (Exception e) {
            logger.info("信息获取失败,Path:" + path + "errMsg" + e.getMessage(), e);
        }
        return null;
    }

    //获取所有子节点名称
    public List<String> getChildren(String path) {
        try {
            List<String> childrenList = client.getChildren().forPath(path);
            logger.info("获取{}子节点成功", path);
            return childrenList;
        } catch (Exception e) {
            logger.error("获取{}子节点失败 path:" + path + ",errMsg" + e.getMessage(), e);
        }
        return null;
    }

    public Stat ifExist(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            return stat;
        } catch (Exception e) {
            logger.error("获取{}详细信息失败 errMsg" + e.getMessage(), path, e);
        }
        return null;
    }

    /**
     * 监听节点
     *
     * @param path      znode路径
     * @param watchTime 监听时间
     */
    public void watchZnode(String path, long watchTime) {
        TreeCache treeCache = new TreeCache(client, path);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                ChildData data = treeCacheEvent.getData();
                if (null != data) {
                    switch (treeCacheEvent.getType()) {
                        case NODE_ADDED:
                            System.out.println("监控到有新增节点");
                            break;
                        case NODE_UPDATED:
                            System.out.println("监控到节点被更新");
                            break;
                        case NODE_REMOVED:
                            System.out.println("有节点被移除");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        try {
            treeCache.start();
            logger.info("成功监听节点：{}", path);
            Thread.sleep(watchTime);
        } catch (Exception e) {
            logger.error("监听失败，path:" + path + ",errMsg" + e.getMessage(), e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (null != client) {
            client.close();
        }
    }
}
