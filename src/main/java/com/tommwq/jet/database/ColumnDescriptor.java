package com.tommwq.jet.database;

/**
 * 列描述符
 */
public final class ColumnDescriptor {
    // 列名字
    private final String name;
    // SQLite数据类型
    private final String type;

    public ColumnDescriptor(String name, String type) {
        if (name == null || type == null) {
            throw new IllegalArgumentException("illegal column descriptor");
        }
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return (name + type).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ColumnDescriptor) {
            ColumnDescriptor other = (ColumnDescriptor) obj;
            return type.equals(other.type) && name.equals(other.name);
        }

        return false;
    }
}
