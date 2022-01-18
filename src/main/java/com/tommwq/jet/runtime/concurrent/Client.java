package com.tommwq.jet.runtime.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Client是客户端。Client通过Agency向后端服务发送请求，并接收应答。
 */
public class Client {

    private Agency agency;

    /**
     * Client构造函数。
     *
     * @param agency 后端服务代理。
     */
    public Client(Agency agency) {
        this.agency = agency;
    }

    /**
     * 向服务发送请求。
     *
     * @param request       请求。
     * @param timeoutMillis 超时时间。
     */
    public Object request(Object request, long timeoutMillis)
            throws TimeoutException, InterruptedException, ExecutionException {
        Receipt receipt = this.agency.post(request);
        return receipt.response().get(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 向服务发送请求。
     *
     * @param request 请求。
     */
    public Object request(Object request)
            throws TimeoutException, InterruptedException, ExecutionException {

        Receipt receipt = this.agency.post(request);
        return receipt.response().get();
    }
}

