
package hawk.com.zjjapplication.exception;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title NetworkTimeoutException.java
 * @Description: 自定义网络超时异常
 * @date 2016-3-17 下午2:03:33
 */
public class NetworkTimeoutException extends CustomException {

    private static final long serialVersionUID = 1L;

    public NetworkTimeoutException() {
        super("NetworkTimeoutException");
    }

}
