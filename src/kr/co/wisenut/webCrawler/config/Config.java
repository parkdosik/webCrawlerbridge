/*
 * @(#)Config.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.webCrawler.config;

import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.webCrawler.config.source.Source;

import java.util.HashMap;

/**
 *
 * Config
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class Config {
	/** 크롤로 관련 source  */
    private Source crawlerSource = null;
    
    
    
    
    /** 크롤로 결과 source  */
    private Source resultSource = null;
   
    
    /** 사용 */
    private HashMap dataSource;
    private Source source = null;
    
    
    
    private RunTimeArgs m_args;
    
       
   

    public Source getCrawlerSource() {
		return crawlerSource;
	}

	public void setCrawlerSource(Source crawlerSource) {
		this.crawlerSource = crawlerSource;
	}
	
	public Source getResultSource() {
		return resultSource;
	}

	public void setResultSource(Source resultSource) {
		this.resultSource = resultSource;
	}

	public RunTimeArgs getArgs() {
        return m_args;
    }

    public void setArgs(RunTimeArgs args) {
        this.m_args = args;
    }

    public String getWebCrawler_home() {
        return m_args.getWebCrawler_home();
    }
    protected void debug(String msg){
        System.out.println(msg);
    }
    
    public HashMap getDataSource(){
        return dataSource;
    }
    
    public Source getSource() {
        return source;
    }

	public void setDataSource(HashMap dataSource) {
		this.dataSource = dataSource;
	}

	public void setSource(Source source) {
		this.source = source;
	}
    
    
}
