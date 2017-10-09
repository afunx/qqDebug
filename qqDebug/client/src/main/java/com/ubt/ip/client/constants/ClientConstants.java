package com.ubt.ip.client.constants;

/**
 * Created by afunx on 15/09/2017.
 */

public interface ClientConstants {
    /**
     * 错误码
     */
    interface ErrorCode {
        /**
         * 机器人执行成功
         */
        int SUCCESS = 0;
        /**
         * 机器人执行失败
         */
        int FAIL = -2000;
        /**
         * IOException
         */
        int IOEXCEPTION = -2001;
        /**
         * BaseUrl为空
         */
        int BASE_URL_NULL = -2002;
        /**
         * 未知错误
         */
        int UNKNOWN_ERR = -2003;
    }

    /**
     * 回读模式
     */
    interface ReadMode {
        /**
         * 所有舵机都在回读模式
         */
        int ALL = 1;
        /**
         * 部分舵机在回读模式，部分舵机不在回读模式
         */
        int PARTIAL = 0;
        /**
         * 所有舵机都不在回读模式
         */
        int NONE = -1;
    }
}
