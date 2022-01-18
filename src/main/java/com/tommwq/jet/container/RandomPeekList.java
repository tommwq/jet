package com.tq.tquitl.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// TODO 做成List子类。
public class RandomPeekList<T> {
    private List<T> array = new ArrayList();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
    private Random random = new Random(System.currentTimeMillis());

    public void add(T t) {
        rwlock.writeLock().lock();
        array.add(t);
        rwlock.writeLock().unlock();
    }

    public T get() {
        T t = null;
        rwlock.readLock().lock();
        int pos = random.nextInt(array.size());
        t = array.get(pos);
        rwlock.readLock().unlock();
        return t;
    }

    public void remove(T t) {
        rwlock.writeLock().lock();
        array.remove(t);
        rwlock.writeLock().unlock();
    }
}
