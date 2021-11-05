package com.demo.jolindubbo.client.registry;

import com.demo.jolindubbo.client.loadbalance.LoadBalance;
import com.demo.jolindubbo.client.loadbalance.RandomLoadBalance;
import com.demo.jolindubbo.common.ZkConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

public class ServiceDiscoverImpl implements IServiceDiscover {

    List<String> repos = new ArrayList<String>();
    private CuratorFramework curatorFramework;

    public ServiceDiscoverImpl() {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.addr).sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        curatorFramework.start();
    }

    @Override
    public String discover(String serviceName) {
        String path = ZkConfig.ZK_REGISTER_PATH.concat("/").concat(serviceName);
        try {
            repos = curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerWatch(path);
        LoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.select(repos);
    }

    /**
     * 监听ZK节点内容刷新
     *
     * @param path 路径
     */
    private void registerWatch(final String path) {
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                repos = curatorFramework.getChildren().forPath(path);
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}