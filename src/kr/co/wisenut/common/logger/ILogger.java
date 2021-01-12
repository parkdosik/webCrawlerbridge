package kr.co.wisenut.common.logger;
/**
 * 
 * 날짜                       : 이름      : 내용
 * 2017.07.05 : 박도식  : bridge-v4.0.1-bld0001 동일 내용
 * 2017.07.05 : 박도식 : 불필요한 소스 삭제
 * 
 * 
 */public interface ILogger {
	
    public static final int CRIT = Integer.MIN_VALUE;
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final String STDOUT = "SDTOUT";
    public static final String ERROUT = "ERROUT";
    public static final String DAILY = "DAILY";
    
    public void log(String message);

    public void log(Exception ex);

    public void log(String message, int verbosity);

    public void log(Exception exception, String msg);

    public void log(String message, Throwable throwable);

    public void log(String message, Throwable throwable, int verbosity);

    public void error(String message);

    public void error(Exception ex);

    public void verbose(String message);

    public void finalize();
}
