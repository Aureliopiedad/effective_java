package com.effective.java.chapter2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(Singleton1.INSTANCE);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            Singleton1 singleton1 = (Singleton1) ois.readObject();
            log.info(String.valueOf(singleton1 == Singleton1.INSTANCE));

            ois.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

/**
 * 因为没有实现{@code readResolve()}导致内存地址不匹配，会返回false，不符合单例的含义
 */
class Singleton1 implements Serializable {
    public static final Singleton1 INSTANCE = new Singleton1();

    private Singleton1() {}

    public String call() {
        return "singleton1";
    }
}

/**
 * 会返回true
 */
class Singleton2 implements Serializable {
    private static final Singleton2 INSTANCE = new Singleton2();

    private Singleton2() {}

    public Singleton2 getInstance() {
        return INSTANCE;
    }

    public String call() {
        return "singleton2";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}

enum Singleton3 {
    INSTANCE;

    public String call() {
        return "singleton3";
    }
}

@Getter
class SpellChecker {
    private final String dictionary;

    public SpellChecker(String dictionary) {
        this.dictionary = dictionary;
    }
}