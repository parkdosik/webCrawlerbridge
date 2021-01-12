/*
 * @(#)SetConfig.java   3.8.1 2009/03/11
 *
 */

package kr.co.wisenut.webCrawler.config;


import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.webCrawler.common.WebCrawlerCommon;
import kr.co.wisenut.webCrawler.config.datasource.GetDataSource;
import kr.co.wisenut.webCrawler.config.source.GetSource;

/**
 *
 * SetConfig
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */

public class SetConfig {
    private Config m_config = new Config();
    private final String dataSourceFile = "DataSource.xml".toLowerCase() ;

    public Config getConfig(RunTimeArgs rta) throws ConfigException,DBFactoryException {
        m_config.setArgs(rta);
       /* // 크롤링 관련
        GetSource crawlerSource = new GetSource(rta.getConf(), WebCrawlerCommon.SOURCEID_CRAWLER );
        //
        m_config.setCrawlerSource(crawlerSource.getSource());
        
        // 크롤링 결과
        GetSource resultSouce = new GetSource(rta.getConf(), WebCrawlerCommon.SOURCEID_RESULT );
        m_config.setResultSource(resultSouce.getSource());
        //
        //m_config.setSource( source.getSource() );
        
        
        // DB 정보
        String dataSourcePath = getDataSourcePath(rta);
        m_config.setDataSource(new GetDataSource(dataSourcePath).getDataSource());
        
        
        
        Mapping mapping = m_config.getCollection();
        mapping.setSubQuery(m_config.getSource().getSubQuery());
        mapping.setXmlQuery(m_config.getSource().getXmlQuery());
        
        if(m_config.getSource().getSubMemory() != null) {
        	mapping.setMemorySelect(m_config.getSource().getSubMemory().getMemorySelectMap());
        }

        mapping.viewInfo();

        // 2009.06.29 filtering file extension mapping
        new GetFilterExt(rta.getConf()).getFilterExtInfo();
        
        
        
        // 원본 SCD구조
        
        Mapping mapping = m_config.getCollection();
        mapping.viewInfo();
        
        // 하위 쿼리 찍어주기
        QuerySelect[] QuerySelect =	m_config.getSource().getQuerySelect();
        if(QuerySelect.length > 0){
        	for(QuerySelect querySelect : QuerySelect ){
             	Log2.out("[info] [querySelect] [type:"+querySelect.getType()+"]");
             	if(querySelect.getQueryArr() != null){
             		for(QueryArr queryArr : querySelect.getQueryArr()) {
                 		String msg = "[info] [queryArr] [type:"+queryArr.getType()+"] [queryType:"+queryArr.getQueryType()+"] ";
                 		if("multi".equals(queryArr.getType())){
                 			msg = msg+"[fieldName:"+queryArr.getFieldName()+"] [split:"+queryArr.getSplit()+"] ";
                 		}
                 		Log2.out(msg);
                 		
                 		ScdMapping scdMapping =  queryArr.getScdMapping();
                 		scdMapping.viewInfo();
                 		
                 	}
             	}
             }
        }
       
       */
        
        return m_config;
    }
    

    /**
     * datasource.xml 이 없고 인자에 datasource 파일이 설정된 경우 설정된 파일 정보를 반환한다.
     * @param rta
     * @return
     */
	private String getDataSourcePath(RunTimeArgs rta) {
		
		String dataSourcePath = rta.getWebCrawler_home() + FileUtil.fileseperator + "config" + FileUtil.fileseperator
				+ dataSourceFile;
		
		if(rta.getDataSourcePath() != null) {
			dataSourcePath = rta.getDataSourcePath();
		}
		
		Log2.out("[info] [SetConfig][datasource xml path : " + dataSourcePath + "]");
		
		return dataSourcePath;

	}    
}
