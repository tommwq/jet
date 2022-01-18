/**
 * File: Artifact.java
 * Description:
 * Author: Wang Qian
 * Create: 2017-03-10
 * Modify: 2017-03-10
 */

package com.tommwq.jet.procedure;

/**
 * Artifact是一个工件。
 */
public interface Artifact {

    public boolean isCancelled();

    public void cancel();

    public boolean isReady();

    public void setReady();

    public boolean isTimeout();

    public void setTimeout();

    public void addPart(Artifact artifact);

    public boolean isAllPartsReady();
}

