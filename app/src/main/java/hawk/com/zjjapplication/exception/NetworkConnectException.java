
package hawk.com.zjjapplication.exception;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title NetworkConnectException.java
 * @Description: 网络连接错误
 * @date 2016-3-17 下午2:02:36
 */
public class NetworkConnectException extends CustomException {
    private static final long serialVersionUID = 1L;

    public NetworkConnectException() {
        super("");
    }

    public NetworkConnectException(String string) {
        super(string);
    }

    public NetworkConnectException(Throwable throwable) {
        super(throwable);
    }

    public NetworkConnectException(String string, Throwable throwable) {
        super(string, throwable);
    }

}
