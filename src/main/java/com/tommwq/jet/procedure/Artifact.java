package com.tommwq.jet.procedure;

/**
 * Artifact是一个工件。
 */
public interface Artifact {

    boolean isCancelled();

    void cancel();

    boolean isReady();

    void setReady();

    boolean isTimeout();

    void setTimeout();

    void addPart(Artifact artifact);

    boolean isAllPartsReady();
}

