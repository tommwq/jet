package com.tommwq.jet.container;

/**
 * 栈
 */
public interface Stack<T> extends Iterable<T> {
    /**
     * 压栈
     *
     * @param <T>  元素类型
     * @param 压栈元素
     */
    void push(T element);

    /**
     * @return 栈顶元素
     * @throws UnsupportedOperationException 当栈为空时抛出此异常
     */
    T top() throws UnsupportedOperationException;

    /**
     * 弹栈
     *
     * @return 栈顶元素
     * @throws UnsupportedOperationException 当栈为空时抛出此异常
     */
    T pop() throws UnsupportedOperationException;

    /**
     * @return 是否为空
     */
    boolean isEmpty();

    /**
     * 将元素移到栈顶
     *
     * @param element 元素
     */
    void bringToTop(T element);

    /**
     * @return 栈中元素数量
     */
    int size();
}
