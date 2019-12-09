package com.szydd.software.Test;

import redis.clients.jedis.Jedis;
public class RedisTest {
    public static void main(String[] args) {
    // 虚拟机设置的ip，redis默认端口号
        Jedis jedis = new Jedis("47.97.175.82", 6379);
        jedis.auth("soft@2019");
        jedis.set("key01", "zhangsan");
        jedis.set("key02", "lisi");
        System.out.println(jedis.get("key01"));
    }
}
