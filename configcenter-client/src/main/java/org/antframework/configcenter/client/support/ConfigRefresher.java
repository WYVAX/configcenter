/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 10:24 创建
 */
package org.antframework.configcenter.client.support;

import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.core.ConfigurableConfigProperties;
import org.antframework.configcenter.client.core.ModifiedProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 配置刷新器
 */
public class ConfigRefresher {
    private static final Logger logger = LoggerFactory.getLogger(ConfigRefresher.class);

    // 执行刷新任务的线程池
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            0,
            1,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardPolicy());
    // 刷新配置的任务
    private RefreshTask refreshTask = new RefreshTask();
    // 配置属性
    private ConfigurableConfigProperties properties;
    // 监听器注册器
    private ListenerRegistrar listenerRegistrar;
    // 服务端请求器
    private ServerRequester serverRequester;
    // 缓存文件处理器
    private CacheFileHandler cacheFileHandler;

    public ConfigRefresher(ConfigurableConfigProperties properties, ListenerRegistrar listenerRegistrar, ConfigContext.InitParams initParams) {
        this.properties = properties;
        this.listenerRegistrar = listenerRegistrar;
        serverRequester = new ServerRequester(initParams);
        if (initParams.getCacheFilePath() != null) {
            cacheFileHandler = new CacheFileHandler(initParams.getCacheFilePath());
        }
    }

    /**
     * 刷新配置
     */
    public void refresh() {
        threadPool.execute(refreshTask);
    }

    /**
     * 关闭（释放相关资源）
     */
    public void close() {
        threadPool.shutdown();
    }

    /**
     * 初始化配置
     * （先从服务端读取配置，如果失败则尝试从本地缓存文件读取配置）
     */
    public void initConfig() {
        try {
            Map<String, String> newProperties;
            boolean fromServer = true;
            try {
                newProperties = serverRequester.queryConfig();
            } catch (Throwable e) {
                logger.error("从配置中心读取配置失败：{}", e.getMessage());
                if (cacheFileHandler != null) {
                    logger.warn("尝试从缓存文件读取配置");
                    newProperties = cacheFileHandler.readConfig();
                    fromServer = false;
                } else {
                    throw e;
                }
            }
            if (fromServer && cacheFileHandler != null) {
                cacheFileHandler.storeConfig(newProperties);
            }
            properties.replaceProperties(newProperties);
        } catch (Throwable e) {
            close();
            throw e;
        }
    }

    // 刷新配置的任务
    private class RefreshTask implements Runnable {
        @Override
        public void run() {
            try {
                Map<String, String> newProperties = serverRequester.queryConfig();
                if (cacheFileHandler != null) {
                    cacheFileHandler.storeConfig(newProperties);
                }
                List<ModifiedProperty> modifiedProperties = properties.replaceProperties(newProperties);
                listenerRegistrar.configModified(modifiedProperties);
            } catch (Throwable e) {
                logger.error("刷新配置出错：{}", e.getMessage());
            }
        }
    }
}
