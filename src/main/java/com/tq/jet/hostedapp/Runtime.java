package com.tq.jet.hostedapp;

import java.util.List;
import java.util.Optional;

import com.tq.jet.id.Id;
import com.tq.jet.storage.Storage;
import com.tq.jet.time.Clock;

/**
 * 托管应用运行时
 *
 * @param <T> 托管宿主类型
 */
public interface Runtime<T> {

        /**
         * @return 托管宿主
         */
        T getHost();

        /**
         * @return 返回时钟
         */
        Clock getClock();

        /**
         * @return 存储器
         */
        Storage getStorage();
        /**
         * 应用启动时的回调函数
         * @param app 要启动的应用
         * @return 为应用分配的processId
         */
        Id onAppStart(App app);
        /**
         * 应用停止时的回调函数
         * @param processId 应用的processId
         */
        void onAppStop(Id processId);
        /**
         * 将应用置于前台
         * @param app 应用
         */
        void bringToFront(App app);
}
