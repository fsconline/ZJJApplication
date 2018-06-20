
package hawk.com.zjjapplication.net;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title RequestListener.java
 * @Description: 调用接口回调
 * @date 2016-3-17 下午2:06:23
 */
public interface RequestListener {
    /**
     * 回调
     *
     * @param object *传递的对象
     */
    public void successBack(Object object);

    public void failBack(Object object);
}
