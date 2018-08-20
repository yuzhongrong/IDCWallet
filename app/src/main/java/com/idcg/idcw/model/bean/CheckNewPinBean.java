package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * Created by hpz on 2018/4/6.
 */

public class CheckNewPinBean implements Serializable {
    private int residueCount;
    private boolean isLocked;
    private boolean isValid ;
    private long countingTime;

    public int getResidueCount() {
        return residueCount;
    }

    public void setResidueCount(int residueCount) {
        this.residueCount = residueCount;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public long getCountingTime() {
        return countingTime;
    }

    public void setCountingTime(long countingTime) {
        this.countingTime = countingTime;
    }
}
