package com.example.permisswion;

import java.io.Closeable;

/**
 * Created by Administrator on 2018/1/24.
 */

public class IOUtils {
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
