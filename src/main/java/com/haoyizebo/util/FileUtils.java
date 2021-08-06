package com.haoyizebo.util;

/**
 * @author yibo zhao
 * @date 2021/08/07
 */
public class FileUtils {

    public static String trimExtension(String fileName) {
        int endIndex = fileName.lastIndexOf('.');
        if (endIndex != -1) {
            fileName = fileName.substring(0, endIndex);
        }
        return fileName;
    }

}
