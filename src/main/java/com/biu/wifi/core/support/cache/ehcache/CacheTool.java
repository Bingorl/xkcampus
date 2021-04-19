package com.biu.wifi.core.support.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CacheTool {

    protected static Log logger = LogFactory.getLog(CacheTool.class);

    private static CacheManager cacheManager = null;
    private static Cache cache = null;

    static {
        CacheTool.initCacheManager();
        // 读取 保留 tokenCache
        initCache("tokenCache");
    }

    /**
     * 初始化缓存管理容器
     */
    public static CacheManager initCacheManager() {

        try {
            if (cacheManager == null)
                System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
            cacheManager = CacheManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheManager;
    }


    /**
     * 初始化cache
     */

    public static Cache initCache(String cacheName) {
        if (null == cacheManager.getCache(cacheName)) {
            cacheManager.addCache(cacheName);
        }
        cache = cacheManager.getCache(cacheName);
        return cache;
    }

    /**
     * 添加缓存
     *
     * @param key   关键字
     * @param value 值
     */
    public static void put(Integer key, Object value, Integer seconds) {
        // 创建Element,然后放入Cache对象中  
        Element element = new Element(key, value);
        element.setTimeToLive(seconds);
        cache.put(element);
        logger.debug("~~~~创建" + key + "缓存!!~~~~~");
        cache.flush();
    }


    /**
     * 获取cache
     *
     * @param key 关键字
     * @return
     */
    public static String get(Integer key) {
        cache.flush();
        Element element = cache.get(key);
        cache.flush();
        if (null == element) {
            return "";
        }
        return element.getObjectValue().toString();
    }

    /**
     * 释放CacheManage
     */
    public static void shutdown() {
        cacheManager.shutdown();
    }

    /**
     * 移除cache中的key
     *
     * @param cacheName
     */
    public static void remove(String key) {
        cache.remove(key);
    }


    /**
     * 移除所有cache
     */
    public static void removeAllCache() {
        cacheManager.removalAll();
    }
}