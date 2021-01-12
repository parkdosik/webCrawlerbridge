/*
 * @(#)Source.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.webCrawler.config.source;

import java.util.HashMap;

/**
 *
 * Source
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class Source {
	
	// DB 정보
    private String dbms = "";
    private String targetDBSrc = "";
    private String logDBSrc = "";
    
    //
    // 검색 키워드 쿼리
    private Query keywordQuery  = new Query();
    
    // 룰 키워드 쿼리
    private Query rulesQuery = new Query();
    
    // 메인 데이터
    private HashMap<String, Query> crawlerMainQeuryMap = new  HashMap<String, Query>();
    
    // 메인 뎃글 데이터
    private HashMap<String, Query> crawlerCommentQeuryMap = new  HashMap<String, Query>();
   
	
    /*
    * Setting DB Bridge  Source
    */
    public String getDbms() {
        return dbms;
    }

    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    public String getTargetDSN() {

        return targetDBSrc;
    }

    public void setTargetDSN(String targetDBSrc) {

        this.targetDBSrc = targetDBSrc;
    }

    public String getLogDSN() {
        return logDBSrc;
    }

    public void setLogDSN(String logDBSrc) {
        this.logDBSrc = logDBSrc;
    }

	
	

	
    
    
    /*
     *  수집 관련
     */
    
    public Query getKeywordQuery() {
		return keywordQuery;
	}

	public void setKeywordQuery(Query keywordQuery) {
		this.keywordQuery = keywordQuery;
	}

	public Query getRulesQuery() {
		return rulesQuery;
	}

	public void setRulesQuery(Query rulesQuery) {
		this.rulesQuery = rulesQuery;
	}

	


	
	/*
	 * 입력관련
	 */
	public HashMap<String, Query> getCrawlerMainQeuryMap() {
		return crawlerMainQeuryMap;
	}
	
	public void setCrawlerMainQeuryMap(HashMap<String, Query> crawlerMainQeuryMap) {
		this.crawlerMainQeuryMap = crawlerMainQeuryMap;
	}
	
	public HashMap<String, Query> getCrawlerCommentQeuryMap() {
		return crawlerCommentQeuryMap;
	}
	
	public void setCrawlerCommentQeuryMap(HashMap<String, Query> crawlerCommentQeuryMap) {
		this.crawlerCommentQeuryMap = crawlerCommentQeuryMap;
	}
}
