package com.ace.dawdler.json.utils;

import java.io.*;

/**
 * 文件操作相关工具类。
 *
 * @author aceding
 */
public class FileUtils {

    public static byte[] fileToBytes(File file) {

        if (null == file || !file.exists()) {
            return null;
        }

        InputStream is = null;
        try {
            is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE || length <= 0) {
                System.out.println("return null, file length is: " + length);
                return null;
            }

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                System.out.println("Could not completely read file " + file);
            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String filePath2String(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        byte[] fileBytes = FileUtils.fileToBytes(file);
        String fileStr = null;
        try {
            fileStr = new String(fileBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileStr;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        boolean ret = true;
        File f = new File(path);
        if (f.exists()) {
            ret = f.delete();
        }
        return ret;
    }

    /**
     * 字符型文件，文件内容转字符串
     * 内存开销较低，推荐使用
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFileToString(File file) throws IOException {
        if (file == null) {
            return null;
        }
        // check
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }

        // read
        InputStream fis = null;
        InputStreamReader reader = null;
        char[] buffer = null;
        String rtn = null;
        int n = 0;
        try {
            fis = new FileInputStream(file);
            reader = new InputStreamReader(fis, "UTF-8");
            int size = (int) file.length();
            if (size > 1024 * 12) {
                buffer = new char[1024 * 4];
                //StringBuilder的尺寸弄太大，可能造成浪费，弄小了会enlargeBuffer，主要权衡中文是2个byte，字符是1个byte
                StringBuilder result = new StringBuilder(1024 * 12);
                while (-1 != (n = reader.read(buffer))) {
                    result.append(buffer, 0, n);
                }
                rtn = result.toString();
            } else {
                //12k以内，直接不要使用StringBuilder，解决出现两份内存问题
                buffer = new char[size];
                n = reader.read(buffer);
                rtn = new String(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rtn;
    }
}
