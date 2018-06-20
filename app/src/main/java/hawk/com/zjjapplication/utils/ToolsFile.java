
package hawk.com.zjjapplication.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import hawk.com.zjjapplication.NetApplication;
import hawk.com.zjjapplication.exception.SdcardException;


/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title ToolsFile.java
 * @Description: 全局共用的对文件操作的方法
 * @date 2016-3-17 下午2:09:34
 */
public class ToolsFile {

    private static int BUFFER_SIZE = 1024 * 8;

    /**
     * @param fromF
     * @param toF
     */
    public static void copyFile(File fromF, File toF) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            if (!toF.exists()) {
                toF.createNewFile();
            }
            fileInputStream = new FileInputStream(fromF);
            fileOutputStream = new FileOutputStream(toF);
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int bytesRead = 0; (bytesRead = fileInputStream.read(buffer,
                    0, buffer.length)) != -1; ) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (fileOutputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制一个目录或文件
     *
     * @param from    需要复制的目录 例如：/home/from
     * @param to      复制到的目录 例如：/home/to
     * @param isCover 是否覆盖
     */
    public static void copy(String from, String to, boolean isCover) {
        File fromF = new File(from);
        File toF = new File(to + "/" + fromF.getName());
        copyR(from, toF.getAbsolutePath(), isCover);
    }

    private static void copyR(String from, String to, boolean isCover) {
        File fromF = new File(from);
        if (fromF.isDirectory()) {
            File toF = new File(to);
            toF.mkdirs();
            File[] files = fromF.listFiles();
            for (File f : files) {
                try {
                    File toTmpF = new File(toF.getAbsolutePath() + "/"
                            + f.getName());
                    copyR(f.getAbsolutePath(), toTmpF.getAbsolutePath(),
                            isCover);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            File toF = new File(to);
            if (!toF.exists()) {
                try {
                    toF.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                copyFile(fromF, toF);
            } else {
                if (isCover) {
                    try {
                        toF.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    copyFile(fromF, toF);
                }
            }

        }
    }

    /**
     * 拷贝assets下的文件
     *
     * @param assetFilePath
     * @param to
     */
    public static void copyAssetFile(String assetFilePath, String to)
            throws Exception {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = NetApplication.application.getAssets().open(
                    assetFilePath);
            File toDir = new File(to);
            toDir.mkdirs();
            File toFile = new File(
                    toDir.getAbsolutePath()
                            + "/"
                            + assetFilePath.substring(assetFilePath
                            .lastIndexOf("/") + 1));
            fileOutputStream = new FileOutputStream(toFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            for (int bytesRead = 0; (bytesRead = inputStream.read(buffer, 0,
                    buffer.length)) != -1; ) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     * 解压zip文件
     *
     * @param srcFileFullName 需要被解压的文件地址（包括路径+文件名）例如：/home/kx.apk
     * @param targetPath      需要解压到的目录 例如： /home/kx
     */
    public static boolean unzip(String srcFileFullName, String targetPath)
            throws Exception {
        try {
            ZipFile zipFile = new ZipFile(srcFileFullName);
            Enumeration<? extends ZipEntry> emu = zipFile.entries();
            while (emu.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) emu.nextElement();
                // 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
                if (entry.isDirectory()) {
                    new File(targetPath + entry.getName()).mkdirs();
                    continue;
                }
                BufferedInputStream bis = new BufferedInputStream(
                        zipFile.getInputStream(entry));
                File file = new File(targetPath + entry.getName());
                // 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
                // 而这个文件所在的目录还没有出现过，所以要建出目录来。
                File parent = file.getParentFile();
                if (parent != null && (!parent.exists())) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos,
                        BUFFER_SIZE);

                int count;
                byte data[] = new byte[BUFFER_SIZE];
                while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                    bos.write(data, 0, count);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
            zipFile.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 压缩文件或目录
     *
     * @param srcPath            被压缩的文件或目录地址 例如：/home/kx 或 /home/kx/kx.apk
     * @param targetFileFullName 压缩过后的文件地址全称（包括路径+文件名）例如： /home/kx.apk
     */
    public static void zip(String srcPath, String targetFileFullName) {
        ZipOutputStream outputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFileFullName);
            outputStream = new ZipOutputStream(fileOutputStream);
            zip(outputStream, new File(srcPath), "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void zip(ZipOutputStream out, File f, String base) {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            FileInputStream in = null;
            BufferedInputStream bis = null;
            try {
                out.putNextEntry(new ZipEntry(base));
                in = new FileInputStream(f);
                byte[] buffer = new byte[BUFFER_SIZE];
                bis = new BufferedInputStream(in, BUFFER_SIZE);
                int size;
                while ((size = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (in != null)
                        in.close();
                    if (bis != null)
                        bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static void deleteFile(String filePath) throws Exception {
        if (null == filePath || 0 == filePath.length()) {
            return;
        }
        try {
            File file = new File(filePath);
            if (null != file && file.exists()) {
                if (file.isDirectory()) {// 判断是否为文件夹
                    File[] fileList = file.listFiles();
                    for (int i = 0; i < fileList.length; i++) {
                        String path = fileList[i].getPath();
                        deleteFile(path);
                    }
                    file.delete();
                }
                if (file.isFile()) {// 判断是否为文件
                    file.delete();// 成功返回true，失败返回false
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /***
     * 删除目录下的制定文件
     *
     * @param filePath
     * @param type
     */
    public static void deleteFileByType(String filePath, String type) {
        if (null == filePath || 0 == filePath.length()) {
            return;
        }
        try {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    String path = fileList[i].getPath();
                    if (null != path && path.endsWith(type)) {
                        File fileDel = new File(path);
                        if (null != fileDel && fileDel.exists()) {
                            fileDel.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dirStr1中是否包含所有dirStr1中的内容
     *
     * @param dirStr1
     * @param dirStr1
     * @return
     */
    public static boolean isContain(String dirStr1, String dirStr2) {
        File dir1 = new File(dirStr1);
        File dir2 = new File(dirStr2);
        boolean result = false;
        try {
            result = Arrays.asList(dir1.list()).containsAll(
                    Arrays.asList(dir2.list()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inputstream->byte[]方法
     *
     * @param inputstream
     * @param wantReadLen
     * @return
     * @throws IOException
     */
    public static final byte[] readBytes(InputStream inputstream,
                                         int wantReadLen) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte bys[] = null;
        try {
            byte abyte0[] = new byte[1024];
            int readlength;
            for (int totlalLen = 0; (wantReadLen == 0 || wantReadLen > 0
                    && wantReadLen > totlalLen)
                    && -1 != (readlength = inputstream.read(abyte0)); ) {
                totlalLen += readlength;
                bytearrayoutputstream.write(abyte0, 0, readlength);
            }
            bys = bytearrayoutputstream.toByteArray();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            bytearrayoutputstream.close();
        }
        return bys;
    }

    /**
     * 取得文件夹大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists())
            return 0;
        long size = 0;
        try {
            File flist[] = file.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 转换文件大小
     * @return
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSize = "";
        if (size <= 0) {
            fileSize = "0 KB";
        } else if (size < 1024) {
            fileSize = df.format((double) size) + " B";
        } else if (size < 1048576) {
            fileSize = df.format((double) size / 1024) + " KB";
        } else if (size < 1073741824) {
            fileSize = df.format((double) size / 1048576) + " M";
        } else {
            fileSize = df.format((double) size / 1073741824) + " G";
        }
        return fileSize;
    }

    /***
     * 判断某个文件是否存在
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean isFileExit(String filePath, String fileName) {
        boolean isExit = false;
        try {
            File file = new File(filePath + fileName);
            if (null == file || !file.exists()) {
                isExit = false;
            } else {
                isExit = true;
            }
        } catch (Exception e) {
            isExit = false;
        }
        return isExit;
    }

    /**
     * 保存数据到sd卡
     *
     * @param content
     * @param fileName
     * @param filePath
     * @param isGzip   true压缩保存
     * @param isAppend true续写文件，false重新写文件
     * @return 0:成功，1：sd卡错误，2：其他错误,3:存储卡已满
     * @throws SdcardException
     */
    public synchronized static void writeDataToSdcard(byte[] content,
                                                      String filePath, String fileName, boolean isGzip, boolean isAppend)
            throws SdcardException {
        FileOutputStream fos = null;
        GZIPOutputStream gzin = null;
        try {
            // 判断当前的可用盘符，优先使用sdcard
            String rootPath = Tools.getRootPath();

            File testDir = new File(rootPath + filePath);
            if (!testDir.exists()) {
                testDir.mkdirs();
            }

            if (Tools.getSdcardFreeSize(rootPath) > content.length) {
                File file = new File(rootPath + filePath + fileName);
                if (isAppend) {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } else {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                }

                if (file.exists() && file.canWrite()) {
                    fos = new FileOutputStream(file, isAppend);
                    if (isGzip) {
                        gzin = new GZIPOutputStream(fos);
                        gzin.write(content);
                        gzin.flush();
                    } else {
                        fos.write(content);
                        fos.flush();
                    }
                } else {
                    throw new SdcardException("存储卡错误",
                            SdcardException.SDCARD_ERROR);
                }
            } else {
                throw new SdcardException("存储卡已满", SdcardException.SDCARD_FULL);
            }
        } catch (SdcardException e) {
            throw e;
        } catch (Exception e) {
            throw new SdcardException("存储卡读写错误", SdcardException.OTHE＿ERROR);
        } finally {
            try {
                if (gzin != null) {
                    gzin.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public static String readSDFile(String SDPATH, String fileName) {
        String str = "";

        StringBuffer sb = new StringBuffer();
        File file = new File(SDPATH + fileName);
        try {
//            FileInputStream fis = new FileInputStream(file);
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader reader = new BufferedReader(read);
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            str = new String(sb.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
