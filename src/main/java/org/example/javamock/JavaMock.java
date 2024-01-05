package org.example.javamock;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavaMock {
    private Jedis jedis;
    private final int port;

    public JavaMock(int port) throws IOException {
        this.port = port;
        startClient();
    }


    public void startClient() {
        jedis = new Jedis((String) null, port);
    }

    public Jedis getClient() {
        return jedis;
    }


    public Object getCommand(String keys, String args) {
        Object result = null;
        Method[] methods = jedis.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(keys)) {
                try {
                    result = method.invoke(jedis, args);
                } catch (IllegalArgumentException e) {
                    result = null;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
}
