package com.ace.dawdler.json.utils;

import java.io.*;

/**
 * util class help handle file.
 *
 * @author aceding
 */
public class FileUtils {
    /**
     * delete the file by the path.
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
     * read file's content to string.
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
                StringBuilder result = new StringBuilder(1024 * 12);
                while (-1 != (n = reader.read(buffer))) {
                    result.append(buffer, 0, n);
                }
                rtn = result.toString();
            } else {
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
