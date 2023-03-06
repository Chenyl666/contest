package com.contest.enu;

import java.util.HashMap;
import java.util.Map;

public enum QuestionRepoType {
    PROJECT,PROGRAMMING,PAPER;
    static final Map<QuestionRepoType,Integer> contestTypeIdMap = new HashMap<QuestionRepoType,Integer>();

    static {
        contestTypeIdMap.put(QuestionRepoType.PAPER,1);
        contestTypeIdMap.put(QuestionRepoType.PROGRAMMING,2);
        contestTypeIdMap.put(QuestionRepoType.PROJECT,3);
    }
    public static Integer getContestTypeId(QuestionRepoType questionType){
        return contestTypeIdMap.get(questionType);
    }
}
