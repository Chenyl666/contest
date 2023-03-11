package com.contest.enu;

import java.util.HashMap;
import java.util.Map;

public enum ContestStatus {
    CHECKING, PASSABLE, FAIL, ENROLLING, ENROLL_END, CONTESTING, CONTEST_END;

    private static final Map<ContestStatus, ContestStatus> nextStatus;

    static {
        nextStatus = new HashMap<>();
        nextStatus.put(CHECKING, PASSABLE);
        nextStatus.put(PASSABLE, ENROLLING);
        nextStatus.put(ENROLLING, ENROLL_END);
        nextStatus.put(ENROLL_END, CONTESTING);
        nextStatus.put(CONTESTING, CONTEST_END);
    }

    public ContestStatus getNextStatus() {
        try {
            if (ContestStatus.FAIL.equals(this)) {
                throw new Exception("FAIL status is the end status");
            }
            if (ContestStatus.CONTEST_END.equals(this)) {
                throw new Exception("CONTEST_END status is the end status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextStatus.get(this);
    }

}
