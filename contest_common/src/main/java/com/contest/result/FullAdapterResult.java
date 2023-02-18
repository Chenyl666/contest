package com.contest.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullAdapterResult {
    private Integer errno;
    private Map<String,String> data;

    public FullAdapterResult(ResultModel<String> resultModel){
        data = new HashMap<>();
        if(ResultFlag.SUCCESS.equals(resultModel.getResultFlag())){
            errno = 0;
            data.put("url",resultModel.getData());
        }else{
            errno = 1;
            data.put("message","upload fail!");
        }
    }
}
