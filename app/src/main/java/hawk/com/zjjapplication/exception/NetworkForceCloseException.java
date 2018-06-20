
package hawk.com.zjjapplication.exception;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title NetworkForceCloseException.java
 * @Description: 联网取消异常类
 * @date 2016-3-17 下午2:02:56
 */
public class NetworkForceCloseException extends CustomException {

    private static final long serialVersionUID = 1L;

    public NetworkForceCloseException() {
        super();
    }

    public NetworkForceCloseException(String message) {
        super(message);
    }

}
