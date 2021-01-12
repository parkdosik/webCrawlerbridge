/*
 * @(#)JobFactory.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.webCrawler.job;

import kr.co.wisenut.webCrawler.config.Config;
import kr.co.wisenut.common.Exception.DBFactoryException;

/**
 *
 * JobFactory
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class JobFactory {
    private static IJob m_job;

    public static IJob getInstance(Config config, int mode) throws DBFactoryException {
        if(mode == IJob.STATIC) {
            m_job = new JobStcDyn(config, IJob.STATIC);
        }else if(mode == IJob.TEST) {
            m_job = new JobStcDyn(config, IJob.TEST);
        }
        return m_job;
    }
}
