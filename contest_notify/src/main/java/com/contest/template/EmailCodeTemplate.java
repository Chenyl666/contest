package com.contest.template;

public class EmailCodeTemplate {

    private static final String template = "您当前正在CloudContest网络竞赛平台%1%，验证码为：%2%，5分钟内有效，请勿泄露给他人！";

    public enum Operation{
        OPERATION_REGISTER("注册"),
        OPERATION_LOGIN("登录"),
        OPERATION_MODIFY("修改密码");
        private final String operationName;

        Operation(String operationName) {
            this.operationName = operationName;
        }

        public String getOperationName() {
            return operationName;
        }
    }

    public static String buildEmailCodeMessageContent(Operation operation,String emailCode){
        return new String(template)
                .replaceAll("%1%", operation.getOperationName())
                .replaceAll("%2%", emailCode);
    }

}
