
package hawk.com.zjjapplication.exception;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title SdcardException.java
 * @Description: 自定义sdcard的一些错误
 * @date 2016-3-17 下午2:04:30
 */
public class SdcardException extends CustomException {

    public static final int SDCARD_ERROR = 1;
    public static final int OTHE＿ERROR = 2;
    public static final int SDCARD_FULL = 3;
    private static final long serialVersionUID = 1L;
    private int errorCode;

    public SdcardException() {
        super();
    }

    public SdcardException(String string) {
        super(string);
    }

    public SdcardException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public SdcardException(String string, int errorCode) {
        super(string);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
