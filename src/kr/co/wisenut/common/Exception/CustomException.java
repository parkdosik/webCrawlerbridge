package kr.co.wisenut.common.Exception;

/**
 * 
 * 날짜                       : 이름      : 내용
 * 2017.07.05 : 박도식  : bridge-v4.0.1-bld0001 동일 내용
 * 
 */

public class CustomException extends Exception{
    protected String message = null;
    protected Throwable throwable = null;

    public CustomException() {
        this(null, null);
    }

    public CustomException(String message) {
        this(message, null);
    }

    public CustomException(Throwable throwable) {
        this(null, throwable);
    }

    public CustomException(String message, Throwable throwable) {
        super();
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return (message);
    }

    public Throwable getThrowable() {
        return (throwable);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (message != null) {
            sb.append(message);
            if (throwable != null) {
                sb.append(":  ");
            }
        }
        if (throwable != null) {
            sb.append(throwable.toString());
        }
        return (sb.toString());
    }
}
