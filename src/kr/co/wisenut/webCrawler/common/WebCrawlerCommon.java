package kr.co.wisenut.webCrawler.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebCrawlerCommon {
	// 이름
	public static String NAME = "webCrawler";
	
	// 구분값
	public static String NEWS_NAME_NAVER = "네이버뉴스"; 
	public static String NEWS_NAME_DAUM = "다음뉴스";
	
	// 
	public static String NAVER = "naver";
	public static String DAUM = "daum";
	
	// 시도 횟수
	public static int WEB_CRAWLER_MAX_RUN_COUNT = 3;
	
	//
	public static String WEBCRAWLER_BOOLEAN = "webCrawlerBoolean";
	public static String MAIN_VO = "mainVo";
	
	public static String WEBCRAWLER_COMMENT_BOOLEAN = "webCrawlerCommentBoolean";
	public static String COMMENT_List = "commentList";
	public static String MAIN_List = "mainList";
	
	// 대기 시간
	public static double timeOutInSeconds01 = 0.5;
	public static double timeOutInSeconds01_2 = 2.0;
	public static double timeOutInSeconds01_3 = 3.0;
	public static double timeOutInSeconds04 = 1.0;
	public static long timeOutInSeconds03 = 5;
	public static int timeOutInSeconds02 = 10;
	public static int timeOutInSeconds05 = 3;
	public static int timeOutInSeconds_int_10 = 3;
	/**
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 */
	public  static String waitText(WebDriver driver ,  By locator , int  seconds) {
	    String val = "";
	    int secondsCount = 0;
	    
	    try {
		    while(val.equals("")) {
		    	val = driver.findElement(locator).getText();
		    	//System.out.println("val:"+val + "-"+"secondsCount:"+secondsCount);
		    	if(secondsCount == seconds ) {
		    		break;
		    	}
		    	secondsCount++;
		    	Thread.sleep(1000);
		    }
	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("[error:waitText:"+e+"]");
		}
	    return  val;
	};
	
	public static void  waitLoad01(double d) {
		int newSecods = (int)(d * 1000) ;
	    try {
	    	//System.out.println("newSecods:"+newSecods);
	    	Thread.sleep(newSecods);
	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("[error:waitText:"+e+"]");
		}
	    
	};
	
	public static void witeLoad02(WebDriver driver , int timeOutInSeconds) {
		WebDriverWait driverWait = new WebDriverWait(driver, timeOutInSeconds);
	}
	
	/**
	 * 해당 요소가 보이지 않을떄까지
	 * @param driver
	 * @param timeOutInSeconds
	 * @param xpath
	 */
	public static void witeLoadVisibilityOfElementLocated(WebDriver driver,int timeOutInSeconds,  By xpath) {
		WebDriverWait driverWait = new WebDriverWait(driver, timeOutInSeconds);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
		
	}
	/**
	 * 해당 요소가 나타날떄까지 대기
	 * @param driver
	 * @param timeOutInSeconds
	 * @param xpath
	 */
	public static void witeLoadElementIsVisible(WebDriver driver,int timeOutInSeconds,  By xpath) {
		WebDriverWait driverWait = new WebDriverWait(driver, timeOutInSeconds);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
		
	}

	
}
