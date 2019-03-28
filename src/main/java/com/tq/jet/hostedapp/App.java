package com.tq.jet.hostedapp;

import java.util.Optional;
import com.tq.jet.id.Id;

public abstract class App<T> {
        private boolean started = false;
        private Runtime<T> runtime;
        private Id processId;

        /**
         * @return 应用标识
         */
        public abstract Id getId();

        /**
         * @return 应用标题
         */
        public abstract String getTitle();

        /**
         * @return 应用描述
         */
        public abstract String getDescription();

        /**
         * 应用启动时的回调函数
         */
        public abstract void onStart();

        /**
         * 应用停止时的回调函数
         */
        public abstract void onStop();

        /**
         * @return 是否已经启动
         */
        public boolean isStarted() {
                return started;
        }

        /**
         * 检查运行时是否合法
         *
         * @return 如果运行时合法，返回true；否则返回false
         */
        public abstract boolean checkRuntime();

        /**
         * @return 返回绑定的运行时
         */
        public Optional<Runtime<T>> getRuntime() {
                return Optional.ofNullable(runtime);
        }

        /**
         * 启动应用
         * @param runtime 运行应用的运行时
         */
        public void start(Runtime<T> runtime) {
                this.runtime = runtime;
                if (!checkRuntime()) {
                        this.runtime = null;
                        throw new UnsupportedOperationException();
                }           
                processId = this.runtime.onAppStart(this);
                onStart();
                started = true;
        }

        /**
         * 停止应用
         */
        public void stop() {
                onStop();
                this.runtime.onAppStop(processId);
                started = false;
                this.runtime = null;
                this.processId = null;
        }

        /**
         * 置于前台
         */
        public void bringToFront() {
                if (started) {
                        this.runtime.bringToFront(this);
                }
        }
}
