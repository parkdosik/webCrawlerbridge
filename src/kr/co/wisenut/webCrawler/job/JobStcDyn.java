/*
 * @(#)JobStcDyn.java 3.8.1 2009/03/11
 */
package kr.co.wisenut.webCrawler.job;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import kr.co.wisenut.MyBatis.SqlMapClient;
import kr.co.wisenut.common.Exception.BridgeException;
import kr.co.wisenut.common.Exception.CustomException;
import kr.co.wisenut.common.Exception.DBFactoryException;

import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.properties.PropertiesLoad;
import kr.co.wisenut.common.properties.PropertiesVO;
import kr.co.wisenut.common.util.CountView;
import kr.co.wisenut.common.util.DateUtil;
import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.common.util.io.IOUtil;
import kr.co.wisenut.webCrawler.common.WebCrawlerCommon;
import kr.co.wisenut.webCrawler.config.Config;
import kr.co.wisenut.webCrawler.config.RunTimeArgs;
import kr.co.wisenut.webCrawler.config.source.Query;
import kr.co.wisenut.webCrawler.site.CrawlerDaum;
import kr.co.wisenut.webCrawler.site.CrawlerNaver;
import kr.co.wisenut.webCrawler.site.CrawlerLand;
import kr.co.wisenut.webCrawler.site.CrawlerYoutube;
import kr.co.wisenut.webCrawler.vo.CategoryVO;
import kr.co.wisenut.webCrawler.vo.NewsCommentVO;
import kr.co.wisenut.webCrawler.vo.NewsVO;

/**
 * 
 * JobStcDyn
 * 
 * Copyright 2000-2012 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 19 Jun 2012
 * Bridge Release 11 March 2009
 * 
 * @author WISEnut
 * @version 3.8,5. 2012/06/19 Bridge Release
 * @param <trhow>
 * 
 */

public class JobStcDyn<trhow> extends Job {
	
	private boolean isTest = false;
	
	public JobStcDyn(Config config, int mode) throws DBFactoryException {
		super(config, mode);
	}

	public boolean run() throws BridgeException {
		
		SqlMapClient sqlMapClient = new SqlMapClient(m_config.getWebCrawler_home());
		SqlSession session = sqlMapClient.getSqlSession();
        
		
		RunTimeArgs args = m_config.getArgs();
		
		String webCrawlerHomePath = args.getWebCrawler_home();
		String propertiesPath = webCrawlerHomePath+FileUtil.fileseperator+"config"+FileUtil.fileseperator+"webCrawler.properties";
		
		//List<MainVO> CategoryList = new ArrayList<MainVO>();
		// 메인 설정값 
		//MainVO mainVo = new MainVO();
		
		PropertiesVO  propertiesVo = PropertiesLoad.load(propertiesPath);
		
	

		String keywordId = args.getKeywordId();
		String categoryId = args.getCategoryId();
		String day = args.getDay();
		log("[keywordId:"+keywordId+"]");
		log("[categoryId:"+categoryId+"]");
		log("[day:"+day+"]");
		String keyword = session.selectOne("keyword.keywordOne",keywordId);
		
		CategoryVO categoryVo  = session.selectOne("rule.category", categoryId);
		log("[keyword:"+keyword+"]");
		log("[categoryVo:"+categoryVo.toString()+"]");
		
		//log("--------------1111---------------");
		session.getClass();
		
		if(categoryVo.getMainCategory().equals("naver")) {
			// 카페
			CrawlerNaver crawlerNaver = new CrawlerNaver(propertiesVo, keywordId, keyword, categoryVo, day);
			crawlerNaver.crawler();
		}else if(categoryVo.getMainCategory().equals("daum")) {
			// 다음
			CrawlerDaum crawlerDaum = new CrawlerDaum(propertiesVo, keywordId, keyword, categoryVo, day);
			crawlerDaum.crawler();
		}else if(categoryVo.getMainCategory().equals("youtube")) {
			CrawlerYoutube crawlerYoutube = new CrawlerYoutube(propertiesVo, keywordId, keyword, categoryVo, day);
			crawlerYoutube.crawler();
		}else if(categoryVo.getMainCategory().equals("land")) {
			CrawlerLand crawlerLand = new CrawlerLand(propertiesVo, keywordId, keyword, categoryVo, day);
			crawlerLand.crawler();
		}else{
			crawler(propertiesVo, keywordId, keyword, categoryVo , day );
		}
		
		//log("--------------2222---------------");
		
		return true;
	}
	
	// 실세 크롤러
	protected boolean crawler(PropertiesVO  propertiesVo , String keywordId, String keyword, CategoryVO categoryVo, String day ) {
		log("------------------------------");
		
		Path path = Paths.get(propertiesVo.getChromedriver());
		log("[chromedriver path :"+propertiesVo.getChromedriver()+"]");
		
		// WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", path.toString());
        
        
        /// WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        
        options.addArguments("--start-maximized");            // 전체화면으로 실행
        options.addArguments("--disable-popup-blocking");    // 팝업 무시
        options.addArguments("--disable-default-apps");     // 기본앱 사용안함
        
        
        //  대기 시간
        // 
       
        
        // 크롬 생성
        WebDriver driver = new ChromeDriver(options);
        
        // 추후 DB에서 가져온다.
        String baseUrl = categoryVo.getBaseUrl();
        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
        String startDate= "";
        String endDate= "";
        
        String dateOption = categoryVo.getOptionFnc();
        String fullUrl = "";
        
        
        if(categoryVo.getMainCategory().equals(WebCrawlerCommon.NAVER)) {
        	startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
        	endDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
        	
        	fullUrl = baseUrl+sortVal+"&query="+keyword+"&ds="+startDate+"&de="+endDate+"&"+dateOption;	
        	
        	
        }else if(categoryVo.getMainCategory().equals(WebCrawlerCommon.DAUM)) {
        	startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyyMMddHHmmss");
        	endDate = DateUtil.convertDateFormat(day+ "235959", "yyyyMMdd", "yyyyMMddHHmmss");
        	fullUrl = baseUrl+sortVal+"&q="+keyword+"&sd="+startDate+"&ed="+endDate+"&"+dateOption;	
        	
        }
        
        log("fullUrl:"+fullUrl);
        
        
        // 결과 담기
        ArrayList<NewsVO> list = new ArrayList<NewsVO>();
        
        //요청 URL 
        driver.get(fullUrl);
        //driver.manage().wait(timeOutInSeconds03);
        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
        
        
        // 수집 여부
        boolean webCrawlerBl = true;
        // 스크롤 방식할때 건수 체크용
        int reviewCount = 0;
        //
        String naverListAjax  = "";
        int pagingCount = 0 ;
        while(webCrawlerBl) {
        	pagingCount++;
        	// 검색 결과 리스트 
        	
        	// 결과 없을때 나오는 부분 추후 추가해야함.
        	
        	
        	//
        	debug("categoryVo.getResultListCode()):"+categoryVo.getResultListCode());
        	List<WebElement>  searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()));
        	// 검색결과 테그가 여부값
        	int searchListSize = searchList.size();
        	if(searchListSize > 0) {
        		searchList = driver.findElements(By.xpath(categoryVo.getResultListCode() + "/child::*"));	
        	}else {
        		log("검색결과 없음");
        		return true;
        	}
        	//System.out.println("-->"+searchList.size());
        	// 해당 링크 
        	WebElement detailLinkElement = null;
        	// 메인검색 리스트 구분값 ( 페이징  or 스크롤 ) 테스트 코드
        	// 더보기 식
        	if(categoryVo.getMainCategory().equals(WebCrawlerCommon.NAVER) &&  categoryVo.getSubCategory().equals("news") ) {
        		for(int searchListCount  = 0  ; searchListCount < searchList.size()  ; searchListCount++ ) {
        			if(categoryVo.getMainCategory().equals("naver")) {
        				WebElement list_li = searchList.get(searchListCount) ;
        				
        				//List<WebElement> detailLinkElements = list_li.findElements(By.xpath(".//*[@class='news_area']/div/.//a/[@class='info']"));
        				//System.out.println(list_li.findElements(By.xpath(".//*[@class='news_area']/div/a")));
        				List<WebElement> detailLinkElements  = list_li.findElements(By.xpath(".//*[@class='info_group']/a[2]"));
        				//System.out.println(detailLinkElements.size());
        				if(detailLinkElements.size() > 0) { // 해당 테그가 있으면
        					debug("[네이버뉴스 O][pagingCount:"+pagingCount+"][기사 순서"+searchListCount+"][기사URL:"+list_li.findElement(By.xpath(".//*[@class='news_tit']")).getAttribute("href").toString()+"]");
        					detailLinkElement = detailLinkElements.get(0);
        					detailLinkElement.click();
                  			List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                  			driver.switchTo().window(tabs.get(1));
                  			
                  			HashMap<String, Object> mainMap = getMain(driver , pagingCount , searchListCount, categoryVo );
                  			boolean webCrawlerBoolean = (boolean) mainMap.get(WebCrawlerCommon.WEBCRAWLER_BOOLEAN);
                  			if(!webCrawlerBoolean) {
                  				NewsVO  mainVo = (NewsVO) mainMap.get(WebCrawlerCommon.MAIN_VO);
                  				
                  				list.add(mainVo);
                  			}else { // 에러 뜰때
                  				
                  			}
                  			driver.close();
                  			driver.switchTo().window(tabs.get(0));
        				}else { // 네이버 뉴스가 아닐때
        					NewsVO  mainVo  = new NewsVO();
        					mainVo.setPressUrl(list_li.findElement(By.xpath(".//*[@class='news_tit']")).getAttribute("href").toString());
        					mainVo.setTitle(list_li.findElement(By.xpath(".//*[@class='news_tit']")).getText());
        					debug("[네이버뉴스 X][pagingCount:"+pagingCount+"][기사 순서"+searchListCount+"][기사URL:"+list_li.findElement(By.xpath(".//*[@class='news_tit']")).getAttribute("href").toString()+"]");
        				}
        				
        			}
        		}
        		
        		// 페이징 에서 더 보기 css 확인
        		List<WebElement>  pagingNextWebElement = driver.findElements(By.xpath(categoryVo.getPagingNextElementCode()));
                System.out.println("pagingNextWebElement:"+pagingNextWebElement.size());
                
                log("paging code : " + categoryVo.getPagingNextElementCode());
                log("paging code : " + driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).getAttribute("aria-disabled").toString());
                
                if(driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).getAttribute("aria-disabled").toString().equals("false")) {
            		driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).click();
            	}else {
            		webCrawlerBl = false;
            	}
        		
        	// 스크롤 방식
        	}else if (categoryVo.getMainCategory().equals(WebCrawlerCommon.NAVER)  &&  categoryVo.getSubCategory().equals("cafe") ) {
        		
        		List<WebElement>  reviewLoadingList = driver.findElements(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
        		System.out.println("reviewLoadingList : "+ reviewLoadingList.size());
        		
        		if(reviewLoadingList.size() > 0 ) {
        			reviewCount++;
        			String thisListAjax = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]")).getAttribute("data-api");
        			//oldUrl  = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]")).getAttribute("data-api");
        			//WebElement WebElementTest = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
        			System.out.println("thisListAjax : "+ thisListAjax);
            		System.out.println("naverListAjax : "+ naverListAjax);
        			
        			
        			//System.out.println("[reviewCount:"+reviewCount+"][data-api:"+WebElementTest.getAttribute("data-api")+"]");
            		JavascriptExecutor js = (JavascriptExecutor) driver;
            		WebElement element = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
            		// This will scroll the page till the element is found
            		js.executeScript("arguments[0].scrollIntoView(true);", element);
            		
            		WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04); 
            		
            		// 다시 있는지 확인
            		List<WebElement>  naverListAjaxreviewLoadingList = driver.findElements(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
            		
            		if(naverListAjaxreviewLoadingList.size() > 0) {
            			naverListAjax = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]")).getAttribute("data-api");
            		}else {
            			webCrawlerBl = false;
            		}
            		//
            		if(thisListAjax.equals(naverListAjax) ) {
            			webCrawlerBl = false;
            		}
            		
            		// 
            		
            	}else {
            		webCrawlerBl = false;
            	}
        		//webCrawlerBl = false;
        	}
        	searchList = driver.findElements(By.xpath("//*[@id='main_pack']/.//*[@class='lst_total']/child::*"));
        	
        	System.out.println("리스트 건수 : "+ searchList.size());
        		
        		
        		
        		
        	webCrawlerBl = false;
        }

        /*
        	
                
        int pagingCount = 0 ;
              
        while(webCrawlerBl) {
        	pagingCount++;
            log("페이징 : " +  pagingCount);
            WebElement a_WebElement = null;
            List<WebElement> a_WebElements = null;
            
            
            
              	// 결과 페이징 위치
              	List<WebElement> webSearchList = driver.findElements(By.xpath(categoryList.get(0).getResultListCode() + "/child::*"));	
              	log("기사 건수:"+webSearchList.size());
              	
              	for(int webSearchListCount  = 0  ; webSearchListCount < webSearchList.size()  ; webSearchListCount++ ) {
              		WebElement main = webSearchList.get(webSearchListCount);
              		if(categoryList.get(0).getMainCategory().equals("naver")) {
              			// 네이버 뉴스 체크 
                      	List<WebElement> webNaverList = driver.findElements(By.xpath("//*[@class='list_news']/li["+(webSearchListCount+1)+"]/div/div/div/div[@class='info_group']/child::*"));
                      	if(webNaverList.size() > 2) {
                  			a_WebElement = main.findElement(By.xpath("//*[@class='list_news']/li["+(webSearchListCount+1)+"]/div/div/div/div[@class='info_group']/a[2]"));
                      	}
                      }else if(categoryList.get(0).getMainCategory().equals("daum")) {
                    	int newsimg = 0;
                  		newsimg = driver.findElements(By.xpath("//*[@id='news_img_"+ webSearchListCount +"']/div/a/img")).size(); 
                  		if(newsimg > 0) {
                  			a_WebElements = main.findElements(By.xpath("//*[@id='clusterResultUL']/li["+(webSearchListCount+1)+"]/div[2]/div/span[1]/a"));	
                  		}else {
                  			a_WebElements = main.findElements(By.xpath("//*[@id='clusterResultUL']/li["+(webSearchListCount+1)+"]/div[1]/div/span[1]/a"));
                  		}
                      	
                      }
              		
              		if(categoryList.get(0).getMainCategory().equals("naver")) {
              			String a_name = a_WebElement.getText();
              			
                  		if(a_name.equals(WebCrawlerCommon.NEWS_NAME_NAVER)) {
                  			// 클릭
                  			a_WebElement.click();
                  			List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                  			driver.switchTo().window(tabs.get(1));
                  			
                  			HashMap<String, Object> mainMap = getMain(driver , pagingCount , webSearchListCount, categoryList );
                  			boolean webCrawlerBoolean = (boolean) mainMap.get(WebCrawlerCommon.WEBCRAWLER_BOOLEAN);
                  			if(!webCrawlerBoolean) {
                  				MainVO  mainVo = (MainVO) mainMap.get(WebCrawlerCommon.MAIN_VO);
                  				list.add(mainVo);
                  			}
                  			driver.close();
                  			driver.switchTo().window(tabs.get(0));
                  		}
              		}else if(categoryList.get(0).getMainCategory().equals("daum")) {
              			for(WebElement a_WebDaumElement: a_WebElements) {
              				String a_name = a_WebDaumElement.getText();
              				if(a_name.equals(WebCrawlerCommon.NEWS_NAME_DAUM)) {
              					a_WebDaumElement.click();
              					List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                      			driver.switchTo().window(tabs.get(1));
                      			
                      			HashMap<String, Object> mainMap = getMain(driver , pagingCount , webSearchListCount, categoryList );
                      			boolean webCrawlerBoolean = (boolean) mainMap.get(WebCrawlerCommon.WEBCRAWLER_BOOLEAN);
                      			if(!webCrawlerBoolean) {
                      				MainVO  mainVo = (MainVO) mainMap.get(WebCrawlerCommon.MAIN_VO);
                      				list.add(mainVo);
                      			}
                      			
                      			driver.close();
                      			driver.switchTo().window(tabs.get(0));
              				}
              			}
              			
              			
              		}
              		
              	}
              	
              	  // 페이징 에서 더 보기 css 확인
              	  List<WebElement>  pagingNextWebElement = driver.findElements(By.xpath(categoryList.get(0).getPagingNextElementCode()));
                  System.out.println("pagingNextWebElement:"+pagingNextWebElement.size());
                  
                  log("paging code : " + categoryList.get(0).getPagingNextElementCode());
                  if(pagingNextWebElement.size() > 0 ) {
                  	driver.findElement(By.xpath(categoryList.get(0).getPagingNextElementCode())).click();
                  }else {
                  	webCrawlerBl = false;
                  }
              	
              }
        	
       
        
        driver.close();
        driver.quit();
        /*
        // 
        log("[수집 기사 건수:"+list.size()+"]");
        
        // 수집 건수 가 있을때 DB 입력
        if(list.size() > 0) {
        	save(list);
        }
        */
        
        driver.close();
        driver.quit();
        
        
		return true;
	}
	
	// 메인 데이터 
	public HashMap<String, Object> getMain(WebDriver driver, int  pagingCount , int mainListCount, CategoryVO categoryVo) {
		
		HashMap<String, Object> map = new HashMap<String, Object> ();
		
		// 클로링 횟수
		int webCrawlerCount = 0;
		//
		int viewListCount = mainListCount+1;
		boolean webCrawlerBoolean = true;
		NewsVO mainVo  = new NewsVO();
		
		while(webCrawlerBoolean ||  webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT) {
			webCrawlerCount++;
			
			
			
			try {
				log("[JobStcDyn][메인 시도횟수:"+webCrawlerCount+"]");
				// 기사 URL 주소 ( 네이버 기준 )
				String url = driver.getCurrentUrl();
				//articleVo.setArticleUrl(articleUrl);
				mainVo.setUrl(url);
				log("[JobStcDyn][pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][기사URL:"+url+"]");
				
				// 언론사 URL 주소 ( 논평 제외 ) - 네이버만 기사 원문이라는 버튼으로 기사 링크가 연결됨 (다음은 tabs.get(0) 에서 조회 해야 함)
				if(categoryVo.getMainCategory().equals("naver")) {
					List<WebElement> pressUrlList = driver.findElements(By.xpath(categoryVo.getPressUrlCode()));
					
					if(pressUrlList.size() > 0 ) {
						String pressUrl = driver.findElement(By.xpath(categoryVo.getPressUrlCode())).getAttribute("href");
		    			mainVo.setPressUrl(pressUrl);
		    			log("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][언론사 URL :"+pressUrl+"]");
	    			}
				}
				
    			// 제목 
    			String title = driver.findElement(By.xpath(categoryVo.getTitleCode())).getText();
    			mainVo.setTitle(title.replaceAll("\"", ""));
    			log("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][제목:"+title+"]");
    			

    			// 기사 입력 , 최종수정 
    			List<WebElement> tt1ClassWebElementList = driver.findElements(By.xpath(categoryVo.getInputDateCode()));
    			int tt1ClassWebElementListSize = tt1ClassWebElementList.size();
    			//System.out.println("["+i+"][tt1ClassWebElementListSize:"+tt1ClassWebElementListSize+"]");
    			
    			// 기사입력
    			String inputdate = "";
    			// 최종수정
    			String update = "";
    			
    			for(int tt1ClassWebElementListCount = 0 ; tt1ClassWebElementListCount < tt1ClassWebElementListSize ; tt1ClassWebElementListCount++ ) {
    				WebElement tt1ClassWebElement = tt1ClassWebElementList.get(tt1ClassWebElementListCount);
    				if(categoryVo.getMainCategory().equals("naver")) {
        				if(tt1ClassWebElementListCount == 0) {
     						inputdate = tt1ClassWebElement.getText();
        				}else if(tt1ClassWebElementListCount == 1) {
        					update = tt1ClassWebElement.getText();
        				}
    				}else if(categoryVo.getMainCategory().equals("daum")) {
    					if(tt1ClassWebElementListCount == 1) {
    						inputdate = tt1ClassWebElement.getText();
        				}else if(tt1ClassWebElementListCount == 2) {
        					update = tt1ClassWebElement.getText();
        				}
    				}
    			}
    			
    			mainVo.setInputdate(inputdate);
    			mainVo.setUpdate(update);
    			
    			
    			String summary = "";
    			String press = "";
    	        List<WebElement> news_summary = new ArrayList();
    	        
				// 임시
    			
    			// 요약이 없는 기사도 있어서 처리함.
    			List<WebElement> summaryList =  driver.findElements(By.xpath(categoryVo.getSummaryCode())); 
    			//System.out.println("articleSummaryList:"+articleSummaryList.size());
    			if(categoryVo.getMainCategory().equals("naver") && summaryList.size() > 0 ) {
    				driver.findElement(By.xpath(categoryVo.getSummaryCode())).sendKeys(Keys.ENTER);
    				summary = WebCrawlerCommon.waitText(driver, By.xpath("//*[@id='main_content']/div[1]/div[3]/div/div[3]/div[2]/div[contains(@class,'media_end_head_autosummary')]/div/div[2]/div[@class='_contents_body']"), 5);
    			}else if(categoryVo.getMainCategory().equals("daum") && summaryList.size() > 0) {
    				//뉴스 요약본 
        			if(driver.findElements(By.className("btn_summary")).size()!=0) { 
        				WebElement newsdriver = driver.findElement(By.xpath("//*[@class='layer_util layer_summary']"));
        				((RemoteWebDriver) driver).executeScript("arguments[0].style='display: block;'",newsdriver);
        				Thread.sleep(1000);
        				news_summary = driver.findElements(By.xpath(categoryVo.getSummaryCode()));    
        				summary = news_summary.get(0).getText() + news_summary.get(1).getText();
        			}else {
        				System.out.println("요약본없음");
        			}
    			}
    			
				System.out.println("[요약:"+summary+"]");
				log("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][요약:"+summary+"]");
				mainVo.setSummary(summary.replaceAll("\"", ""));
    			
    			// 내용
				String contents = driver.findElement(By.xpath(categoryVo.getContentCode())).getText();
				log("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][내용:"+contents+"]");
				mainVo.setContents(contents.replaceAll("\"", ""));  
				//    
    			
				// 언론사
				if(categoryVo.getMainCategory().equals("naver")) {
					press = driver.findElement(By.xpath(categoryVo.getWriterCode())).getAttribute("title").toString();	
				}else if(categoryVo.getMainCategory().equals("daum")) {
					press = driver.findElement(By.xpath(categoryVo.getWriterCode())).getAttribute("alt").toString();	
				}
				
				log("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][언론사:"+press+"]");
				mainVo.setPress(press);

				if(categoryVo.getMainCategory().equals("naver")) {
					// 기사 언론 반응
					// 좋아요
					String likeitGood = "";
					List<WebElement> likeitGoodList = driver.findElements(By.xpath(categoryVo.getLikeitGood()));
					if(likeitGoodList.size() > 0) {
						likeitGood = driver.findElement(By.xpath(categoryVo.getLikeitGood())).getText();
					}
					mainVo.setLikeitGood(likeitGood);
					System.out.println("likeitGood:"+likeitGood);
					
					//훈훈해요
					String likeitWarm =  "";
					List<WebElement> likeitWarmList =   driver.findElements(By.xpath(categoryVo.getLikeitWarm()));
					if(likeitWarmList.size() > 0 ) {
						likeitWarm = driver.findElement(By.xpath(categoryVo.getLikeitWarm())).getText();
					}
					mainVo.setLikeitWarm(likeitWarm);
					System.out.println("likeitWarm:"+likeitWarm);
					
					//슬퍼요 
					String likeitSad = "";
					List<WebElement> likeitSadList =   driver.findElements(By.xpath(categoryVo.getLikeitSad()));
					if(likeitSadList.size() > 0) {
						likeitSad = driver.findElement(By.xpath(categoryVo.getLikeitSad())).getText();
					}
					mainVo.setLikeitSad(likeitSad);
					System.out.println("likeitSad:"+likeitSad);
					
					//화나요 
					String likeitAngry = "";
					List<WebElement> likeitAngryList =   driver.findElements(By.xpath(categoryVo.getLikeitAngry()));
					if(likeitAngryList.size() > 0 ) {
						likeitAngry = driver.findElement(By.xpath(categoryVo.getLikeitAngry())).getText();
					}
					mainVo.setLikeitAngry(likeitAngry);
					System.out.println("likeitAngry:"+likeitAngry);
					
					//화나요 
					String likeitWant = "" ;
					List<WebElement> likeitWantList =   driver.findElements(By.xpath(categoryVo.getLikeitWant()));
					if(likeitWantList.size() > 0) {
						likeitWant = driver.findElement(By.xpath(categoryVo.getLikeitWant())).getText();
					}
					mainVo.setLikeitWant(likeitWant);
					System.out.println("likeitWant:"+likeitWant);	
				}else if(categoryVo.getMainCategory().equals("daum")) {
					//공강하기 
					String empathy = driver.findElement(By.xpath(categoryVo.getSympathy())).getText();
					mainVo.setEmpathy(empathy);
					System.out.println("empathy:"+empathy);
				}
				
				
				// 댓글 관련 
				
				
				//HashMap<String, Object> commentMap = getComment(driver, categoryList);
				
				//boolean webCrawlerCommentBoolean = (boolean) commentMap.get(WebCrawlerCommon.WEBCRAWLER_COMMENT_BOOLEAN);
    			//if(webCrawlerCommentBoolean) {
    			//	ArrayList<CommentVO>  commentList = (ArrayList<CommentVO>) commentMap.get(WebCrawlerCommon.COMMENT_List);
    			//	mainVo.setCommentList(commentList);
    			//}
				
			}catch(Exception e){
				webCrawlerBoolean = false;
				error("[JobStcDyn][getMain][error:"+driver.getCurrentUrl()+"][webCrawlerCount:"+webCrawlerCount+"]");
				error("[JobStcDyn][getMain][error:"+e+"]");
			}
			
			webCrawlerBoolean = false;
		}
		map.put(WebCrawlerCommon.WEBCRAWLER_BOOLEAN, webCrawlerBoolean);
		map.put(WebCrawlerCommon.MAIN_VO, mainVo);
		return map;
	}
	
	
	// 댓글
	public HashMap<String, Object> getComment(WebDriver driver, List<NewsVO> categoryList){
		HashMap<String, Object> map = new HashMap<>();
		// 댓글 저장 
		ArrayList<NewsCommentVO> commentList = new ArrayList<>();
		Boolean commentBoolean = false;
		
		try {
			// 댓글 기달림
			WebCrawlerCommon.witeLoadElementIsVisible(driver, WebCrawlerCommon.timeOutInSeconds02, By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_view_comment']"));
			
			Boolean articleCommentBl = false;
			
			// 댓글 자체가 없을수 있어세 해당 부분 체크 
			List<WebElement> articleCommentWebElement=  driver.findElements(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_view_comment']"));
			if(articleCommentWebElement.size() > 0) {
				articleCommentBl = true;
			}
			
			// 해당 class 가 있다면 
			if(articleCommentBl) {
				// 더보기 버튼 여부 확인
				String  articleCommenMoreCssDisplay = driver.findElement(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_view_comment']")).getCssValue("display");
				Boolean articleCommenMoreCssDisplayBl = driver.findElement(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_view_comment']")).isDisplayed();
				System.out.println("더보기 : " + articleCommenMoreCssDisplay + "-"+articleCommenMoreCssDisplayBl);
				if(articleCommenMoreCssDisplayBl) {
					driver.findElement(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_view_comment']/a")).click();
					// u_cbox_wrap u_cbox_ko 
					WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds01);
					// 해당 페이지 다시 로딩
					//driver.getPageSource();
					//Thread.sleep(1000);
					
					//System.out.println("getPageSource:"+driver.getPageSource());
					// 최신순으로 변경
					//driver.findElement(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_sort']/div[@class='u_cbox_sort_option']/div/ul/li[2]/a")).click();
					List<WebElement> articleCommonSort = driver.findElements(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_sort']/div[@class='u_cbox_sort_option']/div/ul/li[@data-log='RPS.new']/a"));
					if(articleCommonSort.size() > 0) {
						driver.findElement(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_sort']/div[@class='u_cbox_sort_option']/div/ul/li[@data-log='RPS.new']/a")).click();
					}else {
						System.out.println("댓글 최신순이 없다.");
						
					}
					
					
					
					// 
					// 더 보기 ( css : display 로 구분함 )
					String u_cbox_paginate_display = driver.findElement(By.xpath("//*[@id='cbox_module']/.//div[@class='u_cbox_paginate']")).getCssValue("display");
					Boolean u_cbox_paginate_display_bl = driver.findElement(By.xpath("//*[@id='cbox_module']/.//div[@class='u_cbox_paginate']")).isDisplayed();
					
					System.out.println("시작시 : 더보기 화면 출력 형태:"+u_cbox_paginate_display+"-"+u_cbox_paginate_display_bl);
					int u_cbox_paginate_clickCount = 0;
					//while(u_cbox_paginate_display.equals("block")) {
					while(u_cbox_paginate_display_bl) {
						
						// 더 보기 클릭
						
						driver.findElement(By.xpath("//*[@id='cbox_module']/.//div[@class='u_cbox_paginate']/a")).click();
						u_cbox_paginate_clickCount++;
						WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds01);
						System.out.println("더보기 클릭수:"+u_cbox_paginate_clickCount);
						//Thread.sleep(500);
						
						// css 값 변경
						u_cbox_paginate_display = driver.findElement(By.xpath("//*[@id='cbox_module']/.//div[@class='u_cbox_paginate']")).getCssValue("display");
						u_cbox_paginate_display_bl = driver.findElement(By.xpath("//*[@id='cbox_module']/.//div[@class='u_cbox_paginate']")).isDisplayed();
						System.out.println("중간 : 더보기 화면 출력 형태:"+u_cbox_paginate_display+"-"+u_cbox_paginate_display_bl);
						
						
					}
					
					
					
					// 더보기 다 했을 경우
					// 댓글 li
					System.out.println("더보기 화면 선택 끝:");
					
					List<WebElement>  u_cbox_content_wrap_ul_li_list = driver.findElements(By.xpath("//*[@id='cbox_module']/div[contains(@class,'u_cbox_wrap u_cbox_ko')]/div[@class='u_cbox_content_wrap']/ul/child::*"));
					System.out.println("u_cbox_content_wrap_ul_li 갯수:"+u_cbox_content_wrap_ul_li_list.size());
					//for(WebElement u_cbox_content_wrap_ul_li : u_cbox_content_wrap_ul_li_list) {
					for(int forCount = 0 ; forCount <u_cbox_content_wrap_ul_li_list.size() ; forCount++ ) {
						WebElement u_cbox_content_wrap_ul_li = u_cbox_content_wrap_ul_li_list.get(forCount);
						//System.out.println("u_cbox_content_wrap_ul_li:"+u_cbox_content_wrap_ul_li.getText());
						//String commentWriter = u_cbox_content_wrap_ul_li.findElements("child::*")
						NewsCommentVO commentVo = new NewsCommentVO();
						
						String writer = u_cbox_content_wrap_ul_li.findElement(By.xpath("div[contains(@class,'u_cbox_comment_box')]/div[@class='u_cbox_area']/div[@class='u_cbox_info']/span[@class='u_cbox_info_main']/span[@class='u_cbox_name']")).getText();
						String contents = u_cbox_content_wrap_ul_li.findElement(By.xpath("div[contains(@class,'u_cbox_comment_box')]/div[@class='u_cbox_area']/div[@class='u_cbox_text_wrap']")).getText();
						String date = u_cbox_content_wrap_ul_li.findElement(By.xpath("div[contains(@class,'u_cbox_comment_box')]/div[@class='u_cbox_area']/div[@class='u_cbox_info_base']")).getText();
						String recomm = "";
						String unrecomm = "";
						
						List<WebElement> commentRecommList = u_cbox_content_wrap_ul_li.findElements(By.xpath("div[contains(@class,'u_cbox_comment_box')]/div[@class='u_cbox_area']/div[@class='u_cbox_tool']/div[@class='u_cbox_recomm_set']/a/em[@class='u_cbox_cnt_recomm']"));
						if(commentRecommList.size() > 0 ) {
							recomm = u_cbox_content_wrap_ul_li.findElement(By.xpath("div[contains(@class,'u_cbox_comment_box')]/div[@class='u_cbox_area']/div[@class='u_cbox_tool']/div[@class='u_cbox_recomm_set']/a/em[@class='u_cbox_cnt_recomm']")).getText();
						}
						List<WebElement> commentUnrecommList = u_cbox_content_wrap_ul_li.findElements(By.xpath("div[contains(@class,'u_cbox_comment_box')]/div[@class='u_cbox_area']/div[@class='u_cbox_tool']/div[@class='u_cbox_recomm_set']/a/em[@class='u_cbox_cnt_unrecomm']"));
						if(commentUnrecommList.size() > 0) {
							unrecomm = u_cbox_content_wrap_ul_li.findElement(By.xpath("div[contains(@class,'u_cbox_comment_box')]/div[@class='u_cbox_area']/div[@class='u_cbox_tool']/div[@class='u_cbox_recomm_set']/a/em[@class='u_cbox_cnt_unrecomm']")).getText();
						}
						
						commentVo.setWriter(writer);
						commentVo.setContents(contents);
						commentVo.setDate(date);
						commentVo.setRecomm(recomm);
						commentVo.setUnrecomm(unrecomm);
						commentList.add(commentVo);
						
						System.out.println("기사 댓글 수 :"+forCount);
						
					}
					
				}
			}
			
			//
			commentBoolean = true;
		}catch (Exception e) {
			commentBoolean = false;
			commentList = new ArrayList<NewsCommentVO>();
			
		}
		
		map.put(WebCrawlerCommon.WEBCRAWLER_COMMENT_BOOLEAN, commentBoolean);
		map.put(WebCrawlerCommon.COMMENT_List, commentList);
		
		return map;
	}
	
	public ArrayList<NewsVO> save(ArrayList<NewsVO> list) {

		RunTimeArgs args = m_config.getArgs();
		SqlMapClient sqlMapClient = new SqlMapClient(m_config.getWebCrawler_home());
		SqlSession session = SqlMapClient.getSqlSession();
		int categoryId = Integer.parseInt(args.getCategoryId());
		NewsVO mainVo  = new NewsVO();

		try {
			
			mainVo.setSearchNum(categoryId);
			
			session.insert("webCrawler.CrawlerInsert", list);
			session.commit();

		} catch (Exception e) {
			e.printStackTrace();
			log("e : " + e);
			session.rollback();
		}
		
		return list;
	}
	
} // END Class