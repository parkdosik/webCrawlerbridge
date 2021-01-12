package kr.co.wisenut.common.logger;

import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.webCrawler.common.WebCrawlerCommon;

/**
 * 
 * 날짜                       : 이름      : 내용
 * 2017.07.05 : 박도식  : bridge-v4.0.1-bld0001 동일 내용
 * 2017.07.05 : 박도식  : 브릿지 버전에 따른 메소드 삭제
 * 
 */
public class Log2 {
    private static Logger logger;

    private Log2() {}

    public static void setLogger(String logBase, String logType, boolean debug, int verbosity, boolean verbose, String srcID , String day) throws Exception {
        if(logger == null) {
            logger = new Logger(logBase, logType, debug, verbosity, verbose, srcID , day);
        }
    }

    public static void setBridgeLogger(String logBase, String logType, boolean debug, int verbosity, boolean verbose, String srcID, String day) throws Exception {
        if(logger == null) {
            logBase = logBase + FileUtil.fileseperator + "log" + FileUtil.fileseperator + WebCrawlerCommon.NAME;
            logger = new Logger(logBase, logType, debug, verbosity, verbose, srcID , day);
        }
    }
    
    public static void setLogger(String logBase, String logType, boolean debug,  int verbosity, String modName, String srcID, String day)  {
        if(logger == null) {
            logger = new Logger(logBase, logType, debug, verbosity, modName, srcID , day);
        }
    }

    public static void setLogger(String logBase, String logType, boolean debug,  int verbosity, String modName)  {
        if(logger == null) {
            logger = new Logger(logBase, logType, debug, verbosity, modName);
        }
    }

    public static void out(String msg) {
        logger.log(msg);
    }

    public static void debug(String debug) {
        logger.log(debug, 4);
    }

    public static void debug(String debug, int level) {
        logger.log(debug, level);
    }

    public static void error(String error) {
        logger.error(error);
    }

    public static void error(Exception e) {
        logger.error(e);
    }

    public static void trace(String msg) {
        logger.trace(msg);

    }


}
