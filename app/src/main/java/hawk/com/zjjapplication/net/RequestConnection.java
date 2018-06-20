
package hawk.com.zjjapplication.net;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import hawk.com.zjjapplication.exception.NetworkConnectException;
import hawk.com.zjjapplication.exception.NetworkForceCloseException;
import hawk.com.zjjapplication.exception.NetworkNotException;
import hawk.com.zjjapplication.exception.NetworkTimeoutException;
import hawk.com.zjjapplication.utils.Tools;
import hawk.com.zjjapplication.utils.ToolsPreferences;


/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title RequestConnection.java
 * @Description: 联网底层类 负责网络数据的接收和发送
 * @date 2016-3-17 下午2:05:43
 */
public class RequestConnection {

    /**
     * 连接超时时间
     */
    private static final int TIMEOUT = 60000;

    /**
     * 是否取消联网获取数据
     */
    private boolean isCancelled = false;

    /**
     * 取消联网
     */
    public void cancel() {
        isCancelled = true;
    }

    /**
     * get请求
     *
     * @param httpUrl
     * @param params
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    public String doGet(String httpUrl, Map<String, String> params)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        return doRequest(false, httpUrl, params, false);
    }

    /**
     * get请求
     *
     * @param httpUrl
     * @param params
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    public String doGet(String httpUrl, String params)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        return doRequest(false, httpUrl, params, false);
    }

    /**
     * post请求
     *
     * @param httpUrl
     * @param params
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    public String doPost(String httpUrl, Map<String, String> params, boolean isHasCookie)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        return doRequest(true, httpUrl, params, isHasCookie);
    }

    /**
     * post请求
     *
     * @param httpUrl
     * @param params
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    public String doPost(String httpUrl, String params, boolean isHasCookie)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        return doRequest(true, httpUrl, params, isHasCookie);
    }

    /**
     * 联网请求
     *
     * @param isPost  是否为post请求
     * @param httpUrl
     * @param params
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    private String doRequest(boolean isPost, String httpUrl, Map<String, String> params, boolean ishasCookie)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        HttpURLConnection urlConnection = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            if (isCancelled)
                throw new NetworkForceCloseException();

            //判断是否有网络
            NetworkInfo networkInfo = Tools.getNetworkInfo();
            if (!networkInfo.isConnectToNetwork()) {
                throw new NetworkNotException();
            }

            //拼接参数
            String str = "";
            if (params != null && params.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(entry.getKey() + "=");
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
                }
                str = sb.toString();
            }

            // 根据地址创建URL对象(网络访问的url)  
            URL url = new URL(httpUrl);
            // url.openConnection()打开网络链接  
            urlConnection = (HttpURLConnection) url.openConnection();

            // 设置请求的方式  
            if (isPost) {
                urlConnection.setRequestMethod("POST");
            } else {
                urlConnection.setRequestMethod("GET");
            }
            // 设置从主机读取数据超时时间  
            urlConnection.setReadTimeout(TIMEOUT);
            // 设置连接主机超时时间
            urlConnection.setConnectTimeout(TIMEOUT);
            // 设置请求的头  
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 设置请求的头  
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头  
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Length", String.valueOf(str.getBytes().length));

            //设置自定义header
            Map<String, String> headers = RequestHeaders.getHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            if (isPost) {
                // 发送POST请求必须设置允许输出
                urlConnection.setDoOutput(true);
                // 发送POST请求必须设置允许输入，setDoInput的默认值就是true
                urlConnection.setDoInput(true);

                //获取输出流
                os = urlConnection.getOutputStream();
                os.write(str.getBytes());
                os.flush();
            }

            if (isCancelled)
                throw new NetworkForceCloseException();

            // 获取响应的状态码
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                // 获取响应的输入流对象
                is = urlConnection.getInputStream();

                //判断网关是否加入了压缩
                String contentEncoding = urlConnection.getContentEncoding();
                if (contentEncoding != null) {
                    String value = contentEncoding.toLowerCase(Locale.ENGLISH);
                    if (value.indexOf("gzip") != -1) {
                        is = new GZIPInputStream(is);
                    }
                }


                if (isCancelled)
                    throw new NetworkForceCloseException();

                // 创建字节输出流对象  
                baos = new ByteArrayOutputStream();
                // 定义读取的长度  
                int len = 0;
                // 定义缓冲区  
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取  
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中  
                    baos.write(buffer, 0, len);
                }

                // 返回字符串  
                byte[] byteArray = baos.toByteArray();
                String response = new String(byteArray, "utf-8");

                if (isCancelled)
                    throw new NetworkForceCloseException();
                return response;
            } else {
                throw new NetworkConnectException("" + statusCode);
            }
        } catch (NetworkForceCloseException e) {
            throw new NetworkForceCloseException();
        } catch (NetworkNotException e) {
            throw new NetworkNotException();
        } catch (SocketTimeoutException e) {
            throw new NetworkTimeoutException();
        } catch (Exception e) {
            throw new NetworkConnectException();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 联网请求
     *
     * @param isPost  是否为post请求
     * @param httpUrl
     * @param
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    private String doRequest(boolean isPost, String httpUrl, String param, boolean isHasCookie)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        HttpURLConnection urlConnection = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            if (isCancelled)
                throw new NetworkForceCloseException();

            //判断是否有网络
            NetworkInfo networkInfo = Tools.getNetworkInfo();
            if (!networkInfo.isConnectToNetwork()) {
                throw new NetworkNotException();
            }


            // 根据地址创建URL对象(网络访问的url)  
            URL url = new URL(httpUrl);
            // url.openConnection()打开网络链接  
            urlConnection = (HttpURLConnection) url.openConnection();

            // 设置请求的方式  
            if (isPost) {
                urlConnection.setRequestMethod("POST");
            } else {
                urlConnection.setRequestMethod("GET");
            }
            // 设置从主机读取数据超时时间  
            urlConnection.setReadTimeout(TIMEOUT);
            // 设置连接主机超时时间
            urlConnection.setConnectTimeout(TIMEOUT);
            // 设置请求的头  
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 设置请求的头  
            urlConnection.setRequestProperty("Connection", "keep-alive");
            if (isHasCookie) {//是否包含cookie信息
                urlConnection.setRequestProperty("Cookie", "app_token="+ ToolsPreferences.getPreferences(ToolsPreferences.APP_TOKEN));
                Tools.log("该接口请求的apptoken是： "+ToolsPreferences.getPreferences(ToolsPreferences.APP_TOKEN));
            }
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Length", String.valueOf(param.getBytes().length));
            //设置自定义header
//            Map<String, String> headers = RequestHeaders.getHeaders();
//            for (Map.Entry<String,String> entry : headers.entrySet()) {
//            	urlConnection.setRequestProperty(entry.getKey(),entry.getValue()); 
//    		}

            if (isPost) {
                // 发送POST请求必须设置允许输出
                urlConnection.setDoOutput(true);
                // 发送POST请求必须设置允许输入，setDoInput的默认值就是true
                urlConnection.setDoInput(true);

                //获取输出流
                os = urlConnection.getOutputStream();
                os.write(param.getBytes());
                os.flush();
            }

            if (isCancelled)
                throw new NetworkForceCloseException();

            // 获取响应的状态码
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                // 获取响应的输入流对象
                is = urlConnection.getInputStream();

                //判断网关是否加入了压缩
                String contentEncoding = urlConnection.getContentEncoding();
                if (contentEncoding != null) {
                    String value = contentEncoding.toLowerCase(Locale.ENGLISH);
                    if (value.indexOf("gzip") != -1) {
                        is = new GZIPInputStream(is);
                    }
                }


                if (isCancelled)
                    throw new NetworkForceCloseException();

                // 创建字节输出流对象  
                baos = new ByteArrayOutputStream();
                // 定义读取的长度  
                int len = 0;
                // 定义缓冲区  
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取  
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中  
                    baos.write(buffer, 0, len);
                }

                // 返回字符串  
                byte[] byteArray = baos.toByteArray();
                String response = new String(byteArray, "utf-8");

                if (isCancelled)
                    throw new NetworkForceCloseException();
                return response;
            } else {
                throw new NetworkConnectException("" + statusCode);
            }
        } catch (NetworkForceCloseException e) {
            throw new NetworkForceCloseException();
        } catch (NetworkNotException e) {
            throw new NetworkNotException();
        } catch (SocketTimeoutException e) {
            throw new NetworkTimeoutException();
        } catch (Exception e) {
            throw new NetworkConnectException(e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @param files
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    public String doPostFile(String url, Map<String, String> params, Map<String, File> files)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        HttpURLConnection urlConnection = null;
        DataOutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            if (isCancelled)
                throw new NetworkForceCloseException();

            //加入代理
            NetworkInfo networkInfo = Tools.getNetworkInfo();
            if (!networkInfo.isConnectToNetwork()) {
                throw new NetworkNotException();
            }

            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String PREFIX = "--", LINEND = "\r\n";
            String CHARSET = "UTF-8";

            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setReadTimeout(TIMEOUT); // 缓存的最长时间
            urlConnection.setDoInput(true);// 允许输入
            urlConnection.setDoOutput(true);// 允许输出
            urlConnection.setUseCaches(false); // 不允许使用缓存
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("connection", "keep-alive");
            urlConnection.setRequestProperty("Charsert", "UTF-8");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            //设置自定义header
            Map<String, String> headers = RequestHeaders.getHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            os = new DataOutputStream(urlConnection.getOutputStream());

            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            if (params != null) {
                if (params != null && params.size() > 0) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        sb.append(entry.getKey() + "=");
                        sb.append(URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
                    }
                }

//				for (Map.Entry<String, String> entry : params.entrySet()) {
//					sb.append(PREFIX);
//					sb.append(BOUNDARY);
//					sb.append(LINEND);
//					sb.append("Content-Disposition: form-data; name=\""
//							+ entry.getKey() + "\"" + LINEND);
//					sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
//					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
//					sb.append(LINEND);
//					sb.append(entry.getValue());
//					sb.append(LINEND);
//				}

                os.write(sb.toString().getBytes());
            }

            // 发送文件数据
            if (files != null)
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                            + file.getKey() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset="
                            + CHARSET + LINEND);
                    sb1.append(LINEND);
                    os.write(sb1.toString().getBytes());

                    FileInputStream fis = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024 * 1024 * 2];
                    int len = 0;
                    while ((len = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }

                    fis.close();
                    os.write(LINEND.getBytes());
                }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            os.write(end_data);
            os.flush();

            // 得到响应码
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                is = urlConnection.getInputStream();

                //判断网关是否加入了压缩
                String contentEncoding = urlConnection.getContentEncoding();
                if (contentEncoding != null) {
                    String value = contentEncoding.toLowerCase(Locale.ENGLISH);
                    if (value.indexOf("gzip") != -1) {
                        is = new GZIPInputStream(is);
                    }
                }

                // 创建字节输出流对象
                baos = new ByteArrayOutputStream();
                // 定义读取的长度  
                int len = 0;
                // 定义缓冲区  
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取  
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中  
                    baos.write(buffer, 0, len);
                }

                // 返回字符串  
                byte[] byteArray = baos.toByteArray();
                String response = new String(byteArray, "utf-8");

                if (isCancelled)
                    throw new NetworkForceCloseException();

                return response;
            } else {
                throw new NetworkConnectException(String.valueOf(statusCode));
            }
        } catch (NetworkForceCloseException e) {
            throw new NetworkForceCloseException();
        } catch (NetworkNotException e) {
            throw new NetworkNotException();
        } catch (SocketTimeoutException e) {
            throw new NetworkTimeoutException();
        } catch (NetworkConnectException e) {
            throw new NetworkConnectException(e.getMessage());
        } catch (Exception e) {
            throw new NetworkConnectException();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @param files
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    public String doPostFile(String url, String params, Map<String, File> files)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        HttpURLConnection urlConnection = null;
        DataOutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            if (isCancelled)
                throw new NetworkForceCloseException();

            //加入代理
            NetworkInfo networkInfo = Tools.getNetworkInfo();
            if (!networkInfo.isConnectToNetwork()) {
                throw new NetworkNotException();
            }

            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String PREFIX = "--", LINEND = "\r\n";
            String CHARSET = "UTF-8";

            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setReadTimeout(TIMEOUT); // 缓存的最长时间
            urlConnection.setDoInput(true);// 允许输入
            urlConnection.setDoOutput(true);// 允许输出
            urlConnection.setUseCaches(false); // 不允许使用缓存
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("connection", "keep-alive");
            urlConnection.setRequestProperty("Charsert", "UTF-8");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            //设置自定义header
            Map<String, String> headers = RequestHeaders.getHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            os = new DataOutputStream(urlConnection.getOutputStream());

            // 首先组拼文本类型的参数
            if (params != null) {
                os.write(params.getBytes());
            }

            // 发送文件数据
            if (files != null)
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                            + file.getKey() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset="
                            + CHARSET + LINEND);
                    sb1.append(LINEND);
                    os.write(sb1.toString().getBytes());

                    FileInputStream fis = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024 * 1024 * 2];
                    int len = 0;
                    while ((len = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }

                    fis.close();
                    os.write(LINEND.getBytes());
                }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            os.write(end_data);
            os.flush();

            // 得到响应码
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                is = urlConnection.getInputStream();

                //判断网关是否加入了压缩
                String contentEncoding = urlConnection.getContentEncoding();
                if (contentEncoding != null) {
                    String value = contentEncoding.toLowerCase(Locale.ENGLISH);
                    if (value.indexOf("gzip") != -1) {
                        is = new GZIPInputStream(is);
                    }
                }

                // 创建字节输出流对象
                baos = new ByteArrayOutputStream();
                // 定义读取的长度  
                int len = 0;
                // 定义缓冲区  
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取  
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中  
                    baos.write(buffer, 0, len);
                }

                // 返回字符串  
                byte[] byteArray = baos.toByteArray();
                String response = new String(byteArray, "utf-8");

                if (isCancelled)
                    throw new NetworkForceCloseException();

                return response;
            } else {
                throw new NetworkConnectException(String.valueOf(statusCode));
            }
        } catch (NetworkForceCloseException e) {
            throw new NetworkForceCloseException();
        } catch (NetworkNotException e) {
            throw new NetworkNotException();
        } catch (SocketTimeoutException e) {
            throw new NetworkTimeoutException();
        } catch (NetworkConnectException e) {
            throw new NetworkConnectException(e.getMessage());
        } catch (Exception e) {
            throw new NetworkConnectException();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分段下载并写文件
     *
     * @param url
     * @param filePath
     * @param currentLength   当前下载位置
     * @param targetLength    目标下载位置
     * @param requestListener
     * @return
     * @throws NetworkConnectException
     * @throws NetworkNotException
     * @throws NetworkTimeoutException
     * @throws NetworkForceCloseException
     */
    public byte[] doDownloadFile(String url, String filePath, long currentLength, long targetLength, RequestCallback requestListener)
            throws NetworkConnectException, NetworkNotException, NetworkTimeoutException, NetworkForceCloseException {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        RandomAccessFile raFile = null;
        try {
            URL u = new URL(url);
            urlConnection = (HttpURLConnection) u.openConnection();
            // 设置请求的方式  
            urlConnection.setRequestMethod("GET");
            // 设置请求的超时时间  
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setConnectTimeout(TIMEOUT);
            // 设置请求的头  
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            // 设置请求的头  
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头  
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置断点长度
            if (targetLength > 0) {
                urlConnection.setRequestProperty("RANGE", "bytes=" + currentLength + "-" + targetLength);
            } else {
                urlConnection.setRequestProperty("RANGE", "bytes=" + currentLength + "-");
            }

            //设置自定义header
            Map<String, String> headers = RequestHeaders.getHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            //开始连接
            urlConnection.connect();

            if (isCancelled)
                throw new NetworkForceCloseException();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200 || statusCode == 206) {
                // 得到数据总长度
                String val = urlConnection.getHeaderField("Content-Range");
                long totalLength = Long.parseLong(val.substring(val.indexOf("/") + 1));

                // 得到数据长度
                long downloadLength = urlConnection.getContentLength();

                is = urlConnection.getInputStream();

                // 判断网关是否加入了压缩
                String contentEncoding = urlConnection.getContentEncoding();
                if (contentEncoding != null) {
                    String value = contentEncoding.toLowerCase(Locale.ENGLISH);
                    if (value.indexOf("gzip") != -1) {
                        is = new GZIPInputStream(is);
                    }
                }

                if (isCancelled)
                    throw new NetworkForceCloseException();

                bis = new BufferedInputStream(is);

                File file = new File(filePath);
                //创建文件夹
                File fold = file.getParentFile();
                if (null == fold || !fold.isDirectory()) {
                    fold.mkdirs();
                }

                //创建文件
                if (null == file || !file.exists()) {
                    file.createNewFile();
                }

                //写文件
                raFile = new RandomAccessFile(file, "rw");
                raFile.seek(currentLength);

                long cLength = 0;
                byte[] data = new byte[1024 * 5];
                while (cLength < downloadLength) {
                    int bytesRead = bis.read(data, 0, data.length);
                    if (bytesRead == -1)
                        break;
                    raFile.write(data, 0, bytesRead);
                    cLength += bytesRead;
                    //回调
                    if (requestListener != null) {
                        requestListener.uploadProgress(totalLength, currentLength + cLength);
                    }
                    if (isCancelled)
                        break;
                }
            } else {
                throw new NetworkConnectException();
            }
        } catch (Exception e) {
            throw new NetworkConnectException();
        } finally {
            try {
                if (raFile != null) {
                    raFile.close();
                    raFile = null;
                }
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
