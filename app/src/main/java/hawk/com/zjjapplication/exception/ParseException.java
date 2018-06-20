
package hawk.com.zjjapplication.exception;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title ParseException.java
 * @Description: 自定义数据解析错误
 * @date 2016-3-17 下午2:04:02
 */
public class ParseException extends CustomException {

    private static final long serialVersionUID = 1L;

    public ParseException(Throwable throwable) {
        super(throwable);
    }
}
