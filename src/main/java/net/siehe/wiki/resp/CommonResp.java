package net.siehe.wiki.resp;
//后端会有很多的接口,为了让前端能够统一处理
//逻辑(登录校验、权限校验)需要统一后端的返回值设置通用返回类
//实际工作中,有些项目会在 CommonResp里加上其它通用的属性,比如接口 返回码等
public class CommonResp<T> {
    /**
     * 业务上的成功或失败
     */
    private boolean success = true;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回泛型数据，自定义类型
     */
    private T content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }


}
