package com.tommwq.jet.runtime.concurrent;

import java.util.concurrent.CompletableFuture;

/**
 * Receipt是请求凭证。Receipt用于保存和请求或应答相关联的数据。
 */
public class Receipt {

    private long id;
    private Object request;
    private CompletableFuture response = new CompletableFuture();

    //private volatile boolean isDone = false;

    protected Receipt(long id, Object request) {
        this.id = id;
        this.request = request;
    }

    public long id() {
        return id;
    }

        /*
        public boolean isDone() {
                return isDone;
        }
        */

    public Object request() {
        return request;
    }

    public void response(Object resp) {
        response.complete(resp);
    }

    public CompletableFuture response() {
        return response;
    }

        /*
        public void join() throws TimeoutException, InterruptedException {
                while (!isDone) {
                        sleep();
                }
        }
        
        public void join(long timeoutMillis) throws TimeoutException, InterruptedException {
                long start = System.currentTimeMillis();
                
                while (true) {
                        if (isDone) {
                                break;
                        }

                        if (System.currentTimeMillis() - start > timeoutMillis) {
                                throw new TimeoutException();
                        }

                        sleep();
                }
                System.out.println(response);
        }

        private void sleep() throws InterruptedException {
                Thread.sleep(30);
        }
        */
}
