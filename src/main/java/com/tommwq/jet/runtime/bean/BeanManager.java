package com.tommwq.jet.runtime.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean 管理器
 */
public class BeanManager {

    private Map<Class, Object> beans;

    public BeanManager() {
        beans = new HashMap<>();
    }

    public BeanManager(Map<Class, Object> aMap) {
        beans = aMap;
    }

    public Object getBean(Class clazz) {
        return beans.get(clazz);
    }

    public void setBean(Class clazz, Object object) {
        beans.put(clazz, object);
    }

    public Object getOrCreateBean(Class clazz) throws InstantiationException, IllegalAccessException {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        Object instance = clazz.newInstance();
        beans.put(clazz, instance);
        return instance;
    }
}

