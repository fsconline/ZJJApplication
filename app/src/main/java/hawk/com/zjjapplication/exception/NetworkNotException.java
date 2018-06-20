
package hawk.com.zjjapplication.exception;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title NetworkNotException.java
 * @Description: 自定义无网络异常
 * @date 2016-3-17 下午2:03:15
 */
public class NetworkNotException extends CustomException {

    private static final long serialVersionUID = 1L;

    public NetworkNotException() {
        super("NetworkNotAvailableException");
    }

}
