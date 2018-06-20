
package hawk.com.zjjapplication.exception;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title CustomException.java
 * @Description: 自定义异常基类
 * @date 2016-3-17 下午2:01:50
 */
public class CustomException extends Exception {

    private static final long serialVersionUID = 1L;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CustomException(Throwable throwable) {
        super(throwable);
    }

}
