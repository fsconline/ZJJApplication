
package hawk.com.zjjapplication.utils;

/**
 * @author Jack_SJ
 * @version v Copyright Yjlc Co. Ltd.
 * @Title AppConstant.java
 * @Description: 全局常量
 * @date 2016-3-17 下午2:08:33
 */
public class AppConstant {

    public static final boolean isLog = true;

    public static final String downloadDir = "";
//       public static  final  String API_ADRESS="http://123.56.250.82:71/mbl";
//    public static final String API_ADRESS = "http://123.56.250.82:72/pmd";
    public static final String API_ADRESS = "http://192.168.8.144:8080";

    //微信支付注册APPID
    public static final String WX_APP_ID = "wx8dd63af172e40469";

    /**
     * sdcard根目录
     */
    public static final String sdcardRootPath = android.os.Environment
            .getExternalStorageDirectory().getPath();
    /**
     * 机身根目录
     */
    public static final String dataRootPath = android.os.Environment
            .getDataDirectory() + "/data/" + Tools.getPackageName() + "/files/";
    /**
     * 客户端文件根目录
     */
    public static final String root = "/yjlc_yhyy/";
    /**
     * 图片缓存的路径
     */
    public static final String imageCachePath = root + "cache/";

    /**
     * 临时文件后缀名
     */
    public static final String tempFileExtension = ".cache";


    /**
     * 错误日志文件
     */
    public static final String log = "log.txt";

    public static final int LIST_DATA_SIZE = 10;



    // request参数
    public static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    public static final int REQ_PERM_CAMERA = 11003; // 打开摄像头

    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";


}
