package com.tommwq.jet.runtime.bean;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 管理器
 */
public class BeanManager {

    private final Map<Class, Object> beans;

    public BeanManager() {
        beans = new ConcurrentHashMap<>();
    }

    public BeanManager(Map<Class, Object> aMap) {
        beans = new ConcurrentHashMap<>(aMap);
    }

    public <T> Optional<T> findBean(Class<? extends T> clazz) {
        return Optional.ofNullable((T) beans.get(clazz));
    }

    public void setBean(Class clazz, Object object) {
        beans.put(clazz, object);
    }

    public <T> T getBean(Class<? extends T> clazz) throws InstantiationException, IllegalAccessException {
        Optional<T> bean = findBean(clazz);
        if (bean.isPresent()) {
            return bean.get();
        }

        T instance = clazz.newInstance();
        beans.put(clazz, instance);
        return instance;
    }
}

