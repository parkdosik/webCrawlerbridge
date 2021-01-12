/*
 * @(#)GetSource.java 3.8.1 2009/03/11
 */
package kr.co.wisenut.webCrawler.config.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.XmlUtil;
import kr.co.wisenut.webCrawler.common.WebCrawlerCommon;
import kr.co.wisenut.webCrawler.config.source.node.XmlQuery;

import org.jdom.Element;

/**
 * 
 * GetSource
 * 
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 * 
 * @author WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 * 
 */
public class GetSource extends XmlUtil {
	private Element element = null;
	/** sourceId  */
	private String srcid = null;
	public GetSource(String path, String srcid) throws ConfigException {
		
		super(path);
		debug("[info] [webCrawler] [GetSource] [srcid:"+srcid+"]");
		
		HashMap m_source_map = getElementHashMap(WebCrawlerCommon.TAG_SOURCE);
		
		this.srcid = srcid;
		element = (Element) m_source_map.get(srcid);
		if (element == null) {
			throw new ConfigException(": Missing <Source id=\"" + srcid + "\"> " + "setting in configuration file.");
		}
	}

	/**
	 * GetCatalogInfo Return function
	 * @return Mapping Class
	 * @throws ConfigException
	 *             error
	 */
	public Source getSource() throws ConfigException {
		boolean isCharSet = false;
		Source source = new Source();
		if (element == null) {
			throw new ConfigException(": Missing " + "<Source id=" + srcid + "> setting in configuration file."
					+ "Could not parse Source Config.");
		}
		
		
		// <DSN type="target" dsn="dbsrcid" encrypt="y"/>
		// DB 정보
		source.setTargetDSN(getElementListValue(element.getChildren("DSN"), "target"));
		source.setLogDSN(getElementListValue(element.getChildren("DSN"), "log"));
		
		debug("[info] [webCrawler] [GetSource] [source.getTargetDSN():"+source.getTargetDSN()+"]");
		debug("[info] [webCrawler] [GetSource] [source.getLogDSN():"+source.getLogDSN()+"]");
		// 
		// 
		//source.setQuerySelect(getQuerySelectConf(element));
		
		if(this.srcid.equals(WebCrawlerCommon.SOURCEID_CRAWLER)) {
			Query keywordQuery = getKeywordQuery();
			
			source.setKeywordQuery(keywordQuery);
			
			
			Query rulesQuery = getRulesQuery();
			source.setRulesQuery(rulesQuery);
			debug("[info] [webCrawler] [GetSource] [keywordQuery.toString():"+keywordQuery.toString()+"]");
			debug("[info] [webCrawler] [GetSource] [rulesQuery.toString():"+rulesQuery.toString()+"]");			
		}
		
		// 입력관련
		if(this.srcid.equals(WebCrawlerCommon.SOURCEID_RESULT)) {
			HashMap<String, Query> crawlerMainQeuryMap = getCrawlerMainQeury();
			source.setCrawlerCommentQeuryMap(crawlerMainQeuryMap);
			debug("[info] [webCrawler] [GetSource] [crawlerMainQeuryMap.toString():"+crawlerMainQeuryMap.toString()+"]");
			HashMap<String, Query> crawlerCommentQeuryMap = getCrawlerCommentQeury();
			source.setCrawlerCommentQeuryMap(crawlerCommentQeuryMap);
			debug("[info] [webCrawler] [GetSource] [crawlerCommentQeuryMap.toString():"+crawlerCommentQeuryMap.toString()+"]");
		}
		
		
		
		
		
		
		
		
		//getQuerySelectConf
		
		
		//source.setSubQuery(getSubQueryConf(element));

		// <SubMemory>
		//source.setSubMemory(getSubMemoryConf(element));
		
		// <XmlQuery>
		//source.setXmlQuery(getXmlQueryConf(element));

		
		// order id cross duplicate check.
		/*
		if (isDuplecateOrderNum(source.getSubQuery(), source.getSubMemory())) {
			throw new ConfigException(": Duplicated \"order=\" value in <SubQuery>, <MemorySelect>, "
					+ " in configuration file.");
		}
		*/
		// <Query>
		//source.setQuery(getQueryConf(element));

		// <TableSchema
		//source.setTblSchema(getTableSchema(element));

		//source.setRemoteinfo(getRemoteConf(element));

		//source.setCustomServerInfo(getCustomServerConf(element));

		return source;
	}
	
	
	// 해당 키워드 
	protected Query getKeywordQuery() throws ConfigException {
		Query keywordQuery = new Query();
		List list = getChildrenElementList(element.getChild(WebCrawlerCommon.TAG_KEYWORD), WebCrawlerCommon.TAG_QUERY);
		//System.out.println("list.size():"+list.size());
		if (list != null) {
			if(list.size() == 1) {
				String statement = getElementValue((((Element) list.get(0)).getChild(WebCrawlerCommon.TAG_SQL)), "statement","n", false);
				String sql= getElementChildText(((Element) list.get(0)),  WebCrawlerCommon.TAG_SQL );
				
				keywordQuery.setStatement(statement);
				keywordQuery.setSql(sql);
				
			}else {
				throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_KEYWORD+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> is only 1 ");
			}
		}else {
			throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_KEYWORD+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> setting in configuration file ");
		}
		return keywordQuery;
	}
	
	// 룰 ID  Rules
	protected Query getRulesQuery() throws ConfigException {
		Query rulesQuery = new Query();
		List list = getChildrenElementList(element.getChild(WebCrawlerCommon.TAG_RULES), WebCrawlerCommon.TAG_QUERY);
		//System.out.println("list.size():"+list.size());
		if (list != null) {
			if(list.size() == 1) {
				String statement = getElementValue((((Element) list.get(0)).getChild(WebCrawlerCommon.TAG_SQL)), "statement","n", false);
				String sql= getElementChildText(((Element) list.get(0)),  WebCrawlerCommon.TAG_SQL );
				
				rulesQuery.setStatement(statement);
				rulesQuery.setSql(sql);
			}else {
				throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_RULES+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> is only 1 ");
			}
		}else {
			throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_RULES+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> setting in configuration file. ");
		}
		return rulesQuery;
	}
	
	// 메인 데이터 
	protected HashMap<String, Query> getCrawlerMainQeury() throws ConfigException {
		HashMap<String, Query> map = new HashMap<String, Query>();
		List list = getChildrenElementList(element.getChild(WebCrawlerCommon.TAG_CRAWLER_MAIN), WebCrawlerCommon.TAG_QUERY);
		
		if (list != null) {
			if(list.size() == 3) {
				int forSize = list.size();
				for(int forCount = 0 ; forCount < forSize ; forCount++) {
					Query query = new Query();
					String statement = getElementValue((((Element) list.get(forCount)).getChild(WebCrawlerCommon.TAG_SQL)), "statement","n", false);
					String sql= getElementChildText(((Element) list.get(forCount)),  WebCrawlerCommon.TAG_SQL );
					String type = getElementValue(((Element) list.get(forCount)),  WebCrawlerCommon.ATTRIBUTE_TYPE ,"S", false);
					
					if(!type.equals("S") && !type.equals("I") && !type.equals("U")) {
						throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_MAIN+"/> - <"+WebCrawlerCommon.TAG_QUERY+"   type=\"S or I or U\"/> is only ");
					}
					
					query.setStatement(statement);
					query.setSql(sql);
					query.setType(type);
					
					map.put(type, query);
					
				}
				
				if(map.size() != 3) {
					throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_MAIN+"/> - <"+WebCrawlerCommon.TAG_QUERY+"   type=\"\"/> is only S - 1 , I - 1 , U - 1");
				}
			}else {
				throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_MAIN+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> is only 3 ");
			}
		}else {
			throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_MAIN+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> setting in configuration file. ");
		}
		
		
		return map;
	}
	
	// 메인 댓글
	protected HashMap<String, Query> getCrawlerCommentQeury() throws ConfigException {
		HashMap<String, Query> map = new HashMap<String, Query>();
		List list = getChildrenElementList(element.getChild(WebCrawlerCommon.TAG_CRAWLER_COMMENT), WebCrawlerCommon.TAG_QUERY);
		
		if (list != null) {
			if(list.size() == 3) {
				int forSize = list.size();
				for(int forCount = 0 ; forCount < forSize ; forCount++) {
					Query query = new Query();
					String statement = getElementValue((((Element) list.get(forCount)).getChild(WebCrawlerCommon.TAG_SQL)), "statement","n", false);
					String sql= getElementChildText(((Element) list.get(forCount)),  WebCrawlerCommon.TAG_SQL );
					String type = getElementValue(((Element) list.get(forCount)),  WebCrawlerCommon.ATTRIBUTE_TYPE ,"S", false);
					
					if(!type.equals("S") && !type.equals("I") && !type.equals("U")) {
						throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_COMMENT+"/> - <"+WebCrawlerCommon.TAG_QUERY+"   type=\"S or I or U\"/> is only ");
					}
					
					query.setStatement(statement);
					query.setSql(sql);
					query.setType(type);
					
					map.put(type, query);
					
				}
				if(map.size() != 3) {
					throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_COMMENT+"/> - <"+WebCrawlerCommon.TAG_QUERY+"   type=\"\"/> is only S - 1 , I - 1 , U - 1");
				}
			}else {
				throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_COMMENT+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> is only 3 ");
			}
		}else {
			throw new ConfigException(": Missing  <"+WebCrawlerCommon.TAG_CRAWLER_COMMENT+"/> - <"+WebCrawlerCommon.TAG_QUERY+"/> setting in configuration file. ");
		}
		
		
		return map;
	}

	/**
	 * Get DNS ID
	 * @param list
	 *            Element list
	 * @param mode
	 *            database type
	 * @return dsn id
	 * @throws ConfigException
	 *             error info
	 */
	protected String getElementListValue(List list, String mode) throws ConfigException {
		String retStr = "";
		Element element;

		for (int i = 0; i < list.size(); i++) {
			element = (Element) list.get(i);
			String value = getElementValue(element, "type");
			if (!value.equals("") && value.equals(mode)) {
				if (mode.equals("target")) {
					retStr = getElementValue(element, "dsn");
				} else {
					retStr = getElementValue(element, "dsn", "");
				}
				break;
			}
		}
		return retStr;
	}
	
	
	
	

	public void log(String msg){
		Log2.out(msg);
    }

    public void log(String msg, int level){
        Log2.debug(msg, level);
    }

    public void log(Exception ex){
        Log2.error(ex);
    }

    public void error(String msg){
        Log2.error(msg);
    }

    public void error(Exception ex){
        Log2.error(ex);
    }

    public void debug(String msg, int level){
        Log2.debug(msg, level);
    }

    public void debug(String msg) {
        Log2.debug(msg, 4);
    }
}
