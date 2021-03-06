package com.biu.wifi.campus.Tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

//	private static String IP = CoreConstants.getProperty("ip");
	/*private static final String IP = "localhost";
	private static final int PORT = 6379;
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
	private static JedisPool pool = null;
	private static int TIMEOUT = 10000;*/

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    private static String IP;
    private static int PORT;
    private static JedisPool pool = null;
    private static int TIMEOUT;
    private static int DBINDEX;
    private static String PASSWORD = "";

    public static String getIP() {
        return IP;
    }

    public static void setIP(String iP) {
        IP = iP;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int pORT) {
        PORT = pORT;
    }

    public static JedisPool getPool() {
        return pool;
    }

    public static void setPool(JedisPool pool) {
        RedisUtils.pool = pool;
    }

    public static int getTIMEOUT() {
        return TIMEOUT;
    }

    public static void setTIMEOUT(int tIMEOUT) {
        TIMEOUT = tIMEOUT;
    }

    public static int getDBINDEX() {
        return DBINDEX;
    }

    public static void setDBINDEX(int dBINDEX) {
        DBINDEX = dBINDEX;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        RedisUtils.PASSWORD = PASSWORD;
    }

    private static void initialPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(1024);
            config.setMaxIdle(200);
            config.setMinIdle(8);//?????????????????????
            config.setMaxWait(10000);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            //Idle?????????????????????
            config.setTestWhileIdle(true);
            //??????idle object evitor?????????????????????sleep????????????
            config.setTimeBetweenEvictionRunsMillis(30000);
            //??????idle object evitor?????????????????????????????????
            config.setNumTestsPerEvictionRun(10);
            //?????????????????????????????????idle???????????????????????????????????????idle object evitor????????????????????????????????????timeBetweenEvictionRunsMillis??????0???????????????
            config.setMinEvictableIdleTimeMillis(60000);
            pool = new JedisPool(config, IP, PORT, TIMEOUT, PASSWORD);
        } catch (Exception e) {
            logger.error("create JedisPool error : " + e);
        }
    }

    private static synchronized void poolInit() {
        if (pool == null) {
            initialPool();
        }
    }

    public synchronized static Jedis getJedis() {
        if (pool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (pool != null) {
                jedis = pool.getResource();
                //??????10??????
                jedis.select(DBINDEX);
            }
        } catch (Exception e) {
            logger.error("Get jedis error : " + e);
        } finally {
//            returnResource(jedis);
        }
        return jedis;
    }

    //?????????jedis????????????
    public static void returnResource(final Jedis jedis) {
        if (jedis != null && pool != null) {
            pool.returnResource(jedis);
        }
    }

    /**
     * ?????????
     *
     * @param key
     * @param value
     * @param seconds
     */
    public static void addValue(String key, String value, Integer seconds) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
            if (seconds != null) {
                jedis.expire(key, seconds);
            }
        } catch (Exception e) {
            logger.debug("?????????????????????.");
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ?????????
     *
     * @param key
     */
    public static void delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key);
        } catch (Exception e) {
            logger.debug("??????key??????.");
        } finally {
            returnResource(jedis);
        }
    }

    public static String getValueByKey(String key) {
        Jedis jedis = null;
        String reslut = "";
        try {
            jedis = getJedis();
            reslut = jedis.get(key);
        } catch (Exception e) {
            logger.debug("??????value??????.");
        } finally {
            returnResource(jedis);
        }
        return reslut;
    }

    public static Long addNumIncrement(String key) {
        Jedis jedis = null;
        Long reslut = 0L;
        try {
            jedis = getJedis();
            reslut = jedis.incr(key);
        } catch (Exception e) {
            logger.debug("????????????.");
        } finally {
            returnResource(jedis);
        }
        return reslut;
    }

    public static byte[] serialize(Object value) {
        if (value == null) {
            throw new NullPointerException("Can't serialize null");
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(value);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-serializable object", e);
        } finally {
            try {
                if (os != null) os.close();
                if (bos != null) bos.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rv;
    }

    public static Object deserialize(byte[] in) {
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                rv = is.readObject();
                is.close();
                bis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bis != null) bis.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rv;
    }

    public static void main(String[] args) {
        //delKey("post_1");
        addValue("js","mcx",null);
//        System.out.println(addNumIncrement("post_1"));
//        System.out.println(addNumIncrement("post_7"));
    }
}
