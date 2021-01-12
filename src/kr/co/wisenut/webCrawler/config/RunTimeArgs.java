/*
 * @(#)RunTimeArgs.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.webCrawler.config;

import java.util.ArrayList;
import java.util.List;

import kr.co.wisenut.common.msg.WebCrawlerInfoMsg;

/**
 *
 * RunTimeArgs
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */

public class RunTimeArgs {
	/** DB 설정관련 정보 */
    //private String conf = "";
    /** 가져오는 keyword Id */
    private String keywordId = "";
    /** 가져오는 rulesId  */
    private String rulesId = "";
    /** 가져오는 categoryId  */
    private String categoryId = "";
    /** 가져오는 날짜 */
    private String day = "";
    /** DB 저장 여북밧  */
    private String dbYN = "";
    
    
    private String log = "day";
    
    private String webCrawler_home = "";
    private int loglevel = 1;
    private boolean verbose = false;
    private int mode = -1;
    private boolean debug = false;
    private String logPath = null;
    
    private String dataSourcePath = null;
    
    /**
     *Argument Class's Main Function
     * @param args runtime
     */
    public boolean readargs(String webCrawler_home, String[] args) {
        boolean iskeywordId = false;
        boolean isCategoryId = false;
        boolean isDay= false;
        boolean isDbYN = false;
        boolean isRet = true;
        int length = args.length;
        if(length == 0){
        	WebCrawlerInfoMsg.usage();
            return false;
        }
        
        this.webCrawler_home = webCrawler_home;

        for (int i = 0; i < length; i++) {
            if (!args[i].startsWith("-")) {//check Argument  - text
            	WebCrawlerInfoMsg.usage();
                isRet = false;
            }
            if (args[i].equalsIgnoreCase("-help")) {
            	WebCrawlerInfoMsg.usage();
                isRet = false;
            }
            
            if (args[i].equalsIgnoreCase("-keywordId")) {
                if (i<length-1 && !args[i+1].startsWith("-")) {
                    iskeywordId = true;
                    this.keywordId = args[i+1];
                    i++;
                }else{
                    isRet = false;
                    break;
                }
            } else if (args[i].equalsIgnoreCase("-categoryId")) {
                if (i<length-1 && !args[i+1].startsWith("-")) {
                    isCategoryId = true;
                    this.categoryId = args[i+1];
                    i++;
                }else{
                    isRet = false;
                    break;
                } 
            } else if (args[i].equalsIgnoreCase("-day")) {
                if (i<length-1 && !args[i+1].startsWith("-")) {
                	String dayStr = args[i+1];
                	System.out.println("dayStr:"+dayStr+":"+dayStr.length());
                	if(dayStr.length() != 8) {
                		error("*** JBridge RunTime Mode Error !! -day "+dayStr+" YYYYMMDD ***");
                        isRet = false;
                        break;
                	}else {
                		isDay = true;
                        this.day = args[i+1];
                	}
                }else{
                    isRet = false;
                    break;
                } 
                i++;
            } else if (args[i].equalsIgnoreCase("-dbYN")) {
                if (i<length-1 && !args[i+1].startsWith("-")) {
                    String tMode = args[i+1];
                   if (tMode.equalsIgnoreCase("Y")) {
                        isDbYN = true;
                        this.dbYN = args[i+1];
                        this.mode = 1;
                   }else if (tMode.equalsIgnoreCase("N")) {
                	   isDbYN = true;
                	   this.dbYN = args[i+1];
                       this.mode = 2;
                   }else {
                        error("*** JBridge RunTime Mode Error !! -dbYN "+tMode+" ***");
                        isRet = false;
                    }
                    i++;
                } else {
                    isRet = false;
                    break;
                }
            } else if (args[i].equalsIgnoreCase("-log")) {
                if(i+1 < length) {
                    if ( !args[i+1].startsWith("-") ) {
                        if( args[i+1].equalsIgnoreCase("stdout")){
                            this.log = "stdout";
                        } else if( args[i+1].equalsIgnoreCase("day")){
                            this.log = "day";
                        } else if( args[i+1].equalsIgnoreCase("week")){
                            this.log = "day";
                        } else {
                            this.log = "stdout";
                        }
                        i++;
                    }
                }
            } else if (args[i].equalsIgnoreCase("-datasourcepath")) {
                if(i+1 < length) {
                    if ( !args[i+1].startsWith("-") ) {
                        try{
                            dataSourcePath = args[i+1];
                        }catch(Exception e){}
                        i++;
                    }
                }
            } else if (args[i].equalsIgnoreCase("-debug")) {
                debug = true;
                loglevel = 1;
                if(i+1 < length) {
                    if ( !args[i+1].startsWith("-") ) {
                        try{
                            loglevel = Integer.parseInt(args[i+1]);
                        }catch(Exception e){}
                        i++;
                    }
                }
            } else if (args[i].equalsIgnoreCase("-logpath")) {
                if(i+1 < length) {
                    if ( !args[i+1].startsWith("-") ) {
                        try{
                            logPath = args[i+1];
                        }catch(Exception e){}
                        i++;
                    }
                }
            }
        }
        //System.out.println(!isRet  +"||"+ !iskeywordId +"||"+ !isCategoryId +"||"+ !isRulesId  +"||"+  !isDay+"||"+ !isDbYN) ;
        if(!isRet  || !iskeywordId || !isCategoryId  || !isDay|| !isDbYN){
            error("*** Runtime Arg Error ***");
            isRet = false;
        }
       
        if (keywordId.equals("")) {
            error(">> Not Found -keywordId <keywordId id>");
            isRet = false;
        }
        
        if (categoryId.equals("")) {
            error(">> Not Found -categoryId <categoryId id>");
            isRet = false;
        }
        if (day.equals("")) {
            error(">> Not Found -day <YYYYMMDD>");
            isRet = false;
        }
        if (dbYN.equals("")) {
            error(">> Not Found -dbYN  < Y or N >");
            isRet = false;
        }
        if (mode == -1) {
            error(">> Not Found -mode <init|test|static|dynamic|replace>");
            isRet = false;
        }
        return isRet;
    }


    public String getKeywordId() {
		return keywordId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getRulesId() {
		return rulesId;
	}
	
	
    public String getDbYN() {
		return dbYN;
	}

	public String getLog() {
        return log;
    }

    public int getLoglevel() {
        return loglevel;
    }
    
    public boolean isDebug() {
        return debug;
    }
    
    public String getDataSourcePath() {
		return dataSourcePath;
	}
    
    public boolean isVerbose() {
        return verbose;
    }
    
    public int getMode() {
        return mode;
    }
    
    public String getDay() {
		return day;
	}


	public String getWebCrawler_home() {
		return webCrawler_home;
	}

	private void error(String err){
        System.out.println(err);
    }

    public String getLogPath() {
        return logPath;
    }
}

