package com.tommwq.jet.runtime.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Receipt是请求凭证。Receipt用于保存和请求或应答相关联的数据。
 */
public class Context {
    private Map<String, Object> values = new HashMap();
    private Receipt receipt = null;

    public Context(Receipt r) {
        receipt = r;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void put(String key, Object value) {
        values.put(key, value);
    }

    public Optional<Object> find(String key) {
        if (!values.containsKey(key)) {
            return Optional.ofNullable(null);
        }

        return Optional.of(values.get(key));
    }

}
