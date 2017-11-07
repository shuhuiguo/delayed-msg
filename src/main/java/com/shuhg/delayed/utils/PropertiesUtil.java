package com.shuhg.delayed.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 大舒 on 2017/8/14.
 */
public class PropertiesUtil {
    public static Properties getProperties(String filePath) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            pps.load(in);
            return pps;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
