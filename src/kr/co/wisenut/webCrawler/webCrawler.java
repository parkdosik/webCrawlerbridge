package kr.co.wisenut.webCrawler;

import java.io.IOException;

import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.msg.WebCrawlerInfoMsg;
import kr.co.wisenut.common.util.ExistCodeConstants;
import kr.co.wisenut.common.util.PidUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.common.util.io.IOUtil;
import kr.co.wisenut.webCrawler.config.Config;
import kr.co.wisenut.webCrawler.config.RunTimeArgs;
import kr.co.wisenut.webCrawler.config.SetConfig;
import kr.co.wisenut.webCrawler.job.IJob;
import kr.co.wisenut.webCrawler.job.JobFactory;


/**
 * 
 * webCrawler
 * 
 * webCrawler 해서  DB에 데이터 입력
 * @author 박도식
 * @version 1.0.1. 2020/10/26  Release
 * 날짜                        : 이름        : 사유
 * 2020/10/26 : 박도식     : 최조 정의

 */
public class webCrawler {
	public static void main(String[] args) {
		if (args.length == 0) {
			WebCrawlerInfoMsg.header();
			WebCrawlerInfoMsg.usage();
			System.exit(-1);
		}
		
		WebCrawlerInfoMsg.header();
		String webCrawler_home = "";
		if (System.getProperty("webCrawler_home") != null) {
			webCrawler_home = System.getProperty("webCrawler_home");
		} else {
			WebCrawlerInfoMsg.usageWEBCRAWLER_HOME();
			System.exit(-1);
		}
		
		if (System.getProperty("webCrawler_home") != null) {
			webCrawler_home = System.getProperty("webCrawler_home");
		} else {
			WebCrawlerInfoMsg.usageWEBCRAWLER_HOME();
			System.exit(-1);
		}
		
		// System Exit Code
		int exit_code = ExistCodeConstants.EXIST_CODE_NORMAL;

		RunTimeArgs rta = new RunTimeArgs();
		if (!rta.readargs(webCrawler_home, args)) {
			System.exit(-1);
		}
		
		String srcID = rta.getKeywordId()+"_"+rta.getCategoryId();
		String srcIdDay = rta.getDay();
		
		// Create Log Object
		try {
			if (rta.getLogPath() != null) {
				Log2.setLogger(rta.getLogPath(), rta.getLog(), rta.isDebug(), rta.getLoglevel(),
						rta.isVerbose(), srcID ,srcIdDay );
			} else {
				Log2.setBridgeLogger(webCrawler_home, rta.getLog(), rta.isDebug(), rta.getLoglevel(),
						rta.isVerbose(), srcID , srcIdDay);
			}
		} catch (Exception e) {
			System.out.println("[scdToDB] [Set Logger fail. " + "\n" + IOUtil.StackTraceToString(e) + "\n]");
			System.exit(-1);
		}
		// Create PidUtil Object
		PidUtil pidUtil = new PidUtil(srcID+"_"+srcIdDay, webCrawler_home);
		
		try {
			if (pidUtil.existsPidFile()) {
				Log2.error("[webCrawler] [webCrawler failed. Is already running .)]" + StringUtil.newLine);
				Log2.error("[webCrawler] [KeywordId:"+rta.getKeywordId()+"][CategoryId:"+rta.getCategoryId()+"][RulesId:"+rta.getRulesId()+"]" + StringUtil.newLine);
				exit_code = ExistCodeConstants.EXIST_CODE_ABNORMAL;
				System.exit(exit_code);
			}
			pidUtil.makePID();
		} catch (IOException e) {
			Log2.error("[webCrawler] [Make PID file fail " + "\n" + IOUtil.StackTraceToString(e) + "\n]");
		}
		
		Config config = null;
		double div = 0;
		long start = 0, end = 0;
		
		try {
			
			config = new SetConfig().getConfig(rta);
			IJob job = JobFactory.getInstance(config, rta.getMode());
			Log2.out("[info] [webCrawler] [START][KeywordId:"+rta.getKeywordId()+"][RulesId:"+rta.getRulesId()+"][CategoryId:"+rta.getCategoryId()+"]");
			
			start = System.currentTimeMillis();
			
			
			
			
			if (job.run()) {
				end = System.currentTimeMillis();
				div = ((double) (end - start) / 1000);
				Log2.out("[info] [webCrawler] [KeywordId: " + rta.getKeywordId() + "][CategoryId:"+rta.getCategoryId()+"][RulesId:"+rta.getRulesId()+"] run time: " + div + " sec]");
				Log2.out("[info] [webCrawler] [END: Successful]" + StringUtil.newLine);
			} else {
				Log2.error("[webCrawler] [Crawling failed. see log messages.]" + StringUtil.newLine);
			}
			
			
			pidUtil.deletePID(); // Normal Exit PidUtil Object
			
			
		} catch (ConfigException e) {
			Log2.error("[webCrawler] [Crawling failed. see log messages.]" + StringUtil.newLine);
			Log2.error("[webCrawler] [ConfigException: " + IOUtil.StackTraceToString(e) + StringUtil.newLine + "]");
			pidUtil.leaveErrorPID(); // Abnormal Exit PidUtil Object
			exit_code = ExistCodeConstants.EXIST_CODE_ABNORMAL;
		} catch (DBFactoryException e) {
			Log2.error("[webCrawler] [Crawling failed. see log messages.]" + StringUtil.newLine);
			Log2.error("[webCrawler] [DBFactory Exception " + "\n" + IOUtil.StackTraceToString(e) + "\n]");
			pidUtil.leaveErrorPID(); // Abnormal Exit PidUtil Object
			exit_code = ExistCodeConstants.EXIST_CODE_ABNORMAL;
		} catch (Throwable e) {
			Log2.error("[webCrawler] [Crawling failed. see log messages.]" + StringUtil.newLine);
			Log2.error("[webCrawler] [Throwable message]" + "[" + IOUtil.StackTraceToString(e) + "]");
			pidUtil.leaveErrorPID(); // Abnormal Exit PidUtil Object
			exit_code = ExistCodeConstants.EXIST_CODE_ABNORMAL;
		} finally {
			System.out.println("[info] [webCrawler] [Process: Finished]");
			System.out.println("[info] [webCrawler] [Exist Code: " + exit_code + " (normal:"
					+ ExistCodeConstants.EXIST_CODE_NORMAL + ", abnormal:" + ExistCodeConstants.EXIST_CODE_ABNORMAL
					+ ")]");
			System.exit(exit_code);
		}
		
		
	}
}
