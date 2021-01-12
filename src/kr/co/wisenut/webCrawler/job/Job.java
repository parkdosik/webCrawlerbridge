/*
 * @(#)Job.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.webCrawler.job;

import kr.co.wisenut.common.Exception.BridgeException;
import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.webCrawler.config.Config;
import kr.co.wisenut.common.logger.Log2;

/**
 *
 * Job
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public abstract class Job implements IJob{
    protected int m_mode = -1;
    protected Config m_config;
    protected IJob m_job;
    
    
    public Job(Config config, int mode) throws DBFactoryException {
    	m_config = config;
        m_mode = mode;
    	/*
    	m_config = config;
        m_mode = mode;
        m_job = this;
        
        
        
        
        
        
        // 구분한다.
        // 
        Config configCrawler = config;
        configCrawler.setSource(m_config.getCrawlerSource());
        
        m_sourceCrawler = configCrawler.getSource();
        m_dbFactoryCrawler = new DBConnFactory(m_config.getWebCrawler_home(), m_config.getDataSource());
        m_dbjobCrawler = new DBJob(configCrawler, m_dbFactoryCrawler.getDbmsType( m_sourceCrawler.getTargetDSN()), mode);
       
        //
        Config configResult = config;
        configResult.setSource(m_config.getResultSource());
        
        m_sourceResult = config.getSource();
        m_dbFactoryResult = new DBConnFactory(m_config.getWebCrawler_home(), m_config.getDataSource());
        m_dbjobResult = new DBJob(configResult, m_dbFactoryResult.getDbmsType( m_sourceResult.getTargetDSN()), mode);
        
        
        
        m_queryGen = new QueryGenerator();
        */
    }

    public IJob getInstance(){
        return m_job;
    }

    public String getState() {
        return null; 
    }

    public abstract boolean run() throws BridgeException, DBFactoryException;

    //public abstract void destroy();

    public void stop() throws BridgeException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setConfig(Config config) throws ConfigException {
        m_config = config;
    }

    public Config getConfig() {
        return m_config;
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
