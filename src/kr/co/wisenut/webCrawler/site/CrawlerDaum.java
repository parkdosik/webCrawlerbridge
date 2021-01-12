package kr.co.wisenut.webCrawler.site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import kr.co.wisenut.MyBatis.SqlMapClient;
import kr.co.wisenut.common.properties.PropertiesVO;
import kr.co.wisenut.common.util.DateUtil;
import kr.co.wisenut.common.util.EncryptUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.webCrawler.common.WebCrawlerCommon;
import kr.co.wisenut.webCrawler.vo.CafeVO;
import kr.co.wisenut.webCrawler.vo.BlogVO;
import kr.co.wisenut.webCrawler.vo.CafeCommentVO;
import kr.co.wisenut.webCrawler.vo.CategoryVO;
import kr.co.wisenut.webCrawler.vo.NewsCommentVO;
import kr.co.wisenut.webCrawler.vo.NewsVO;

public class CrawlerDaum extends Crawler {

	public CrawlerDaum(PropertiesVO propertiesVo, String keywordId, String keyword, CategoryVO categoryVo, String day) {
		super(propertiesVo, keywordId, keyword, categoryVo, day);
	}
	
	public void crawler() {

		if(categoryVo.getSubCategory().equals("news")) {
			crawlerNews();
		}else if(categoryVo.getSubCategory().equals("cafe")) {
			crawlerCafe();
		}else if(categoryVo.getSubCategory().equals("blog")) {
			crawlerBlog();
		}
		log("[info][webCrawler][CrawlerDaum][end]");
	}
		/** 뉴스 */
		protected void  crawlerNews() {
			
			log("[info][webCrawler][CrawlerNews][start]");
			
			// 크롬 생성
	        WebDriver driver = new ChromeDriver(chromeOptions);
	        
	        // 추후 DB에서 가져온다.
	        String baseUrl = categoryVo.getBaseUrl();
	        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
	        String startDate= "";
	        String endDate= "";
	        
	        String dateOption = categoryVo.getOptionFnc();
	        String fullUrl = "";
	        
        	startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyyMMddHHmmss");
        	//endDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyyMMddHHmmss");
        	endDate = day + "235959";
        	fullUrl = baseUrl+sortVal+"&q="+keyword+"&sd="+startDate+"&ed="+endDate+"&"+dateOption;		
	        	
	        log("fullUrl:"+fullUrl);
	        // 결과 담기
	        ArrayList<NewsVO> list = new ArrayList<NewsVO>();

	        
	        //요청 URL 
	        driver.get(fullUrl);
	        //driver.manage().wait(timeOutInSeconds03);
	        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
	        debug("[fullUrl:"+fullUrl+"]");
	        
	        // 수집 여부
	        boolean webCrawlerBl = true;
	        int pagingCount = 0 ;
	        
	        try {
			
	        	// 검색 결과 테그값 
	        	debug("categoryVo.getResultListCode()):"+categoryVo.getResultListCode());
	        	//List<WebElement>  searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()));
	        	List<WebElement>  searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()));
	        	// 검색결과 테그가 여부값
	        	int searchListSize = searchList.size();
	        	
	        	  // 다음 뉴스 
				  webCrawlerBl = true;
				  while(searchListSize > 0 && webCrawlerBl) {
					  pagingCount++;
		              	log("페이징 : " +  pagingCount);
		              	WebElement a_WebElement = null;
		              	List<WebElement> a_WebElements = null;
		              	WebElement a_WebPressElement = null;
		              	// 결과 페이징 위치
		              	List<WebElement> webSearchList = driver.findElements(By.xpath(categoryVo.getResultListCode() + "/child::*"));	
		              	log("기사 건수:"+webSearchList.size());
		              	
		              	for(int webSearchListCount  = 0  ; webSearchListCount < webSearchList.size()  ; webSearchListCount++ ) {
		              		WebElement main = webSearchList.get(webSearchListCount);
		              		
		              		int newsimg = 0;
	                 		newsimg = driver.findElements(By.xpath("//*[@id='news_img_"+ webSearchListCount +"']/div/a/img")).size();
	                 		if(newsimg > 0) {
	                 			a_WebPressElement = main.findElement(By.xpath(categoryVo.getTitleLinkCode()+(webSearchListCount+1)+categoryVo.getTitleLinkCode2()));
	                 			a_WebElements = main.findElements(By.xpath(categoryVo.getTitleLinkCode()+(webSearchListCount+1)+"]/div[2]/div/span[1]/a"));
	                 		}else {
	                 			a_WebPressElement = main.findElement(By.xpath(categoryVo.getTitleLinkCode()+(webSearchListCount+1)+"]/div/div/div/a"));
	                 			a_WebElements = main.findElements(By.xpath(categoryVo.getTitleLinkCode()+(webSearchListCount+1)+"]/div[1]/div/span[1]/a"));
	                 		}
	                 		
	                 		for(WebElement a_WebDaumElement: a_WebElements) {
	                 			String a_name = a_WebDaumElement.getText();
	              				if(a_name.equals(WebCrawlerCommon.NEWS_NAME_DAUM)) {
	              					a_WebDaumElement.click();
	              					List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
	                      			driver.switchTo().window(tabs.get(1));
	                      			log("뉴스 순서 : " + (webSearchListCount+1));
	                      			HashMap<String, Object> mainMap = getNewsMain(driver , pagingCount , webSearchListCount, categoryVo );
	                      			boolean webCrawlerBoolean = (boolean) mainMap.get(WebCrawlerCommon.WEBCRAWLER_BOOLEAN);
	                      			if(!webCrawlerBoolean) {
	                      				NewsVO  mainVo = (NewsVO) mainMap.get(WebCrawlerCommon.MAIN_VO);
	                      				list.add(mainVo);
	                          			driver.close();
	                          			driver.switchTo().window(tabs.get(0));
	                      			}
	              				}	
	                 		}
	              				
		              	}
		              	
		               // 페이징 에서 더 보기 css 확인
	              	   List<WebElement>  pagingNextWebElement = driver.findElements(By.xpath(categoryVo.getPagingNextElementCode()));
	                   if(pagingNextWebElement.size() > 0 ) {
	                   	 driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).click();
	                   }else {
	                  	 webCrawlerBl = false;
	                   }
				  }
	        	
	        }catch (Exception e) {
	        	error("[CrawlerNews][Exception e:"+e+"]");
			}finally {
				//
		        driver.close();
		        driver.quit();
			}
	        
	        // DB INSERT
	        InsertNews(list);
	        			
		}
	
		/** 카페 */
		protected void  crawlerCafe() {
			log("[info][webCrawler][CrawlerCafe][start]");
			
			
			 // 크롬 생성
	        WebDriver driver = new ChromeDriver(chromeOptions);
	        
	        // 추후 DB에서 가져온다.
	        String baseUrl = categoryVo.getBaseUrl();
	        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
	        String startDate= "";
	        String endDate= "";
	        
	        String dateOption = categoryVo.getOptionFnc();
	        String fullUrl = "";
	        
	       	startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyyMMddHHmmss");
	       	//endDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyyMMddHHmmss");
	       	endDate = day + "235959";
	       	
	       	fullUrl = baseUrl+sortVal+"&q="+keyword+"&sd="+startDate+"&ed="+endDate+"&"+dateOption;	

	        // 결과 담기
	        ArrayList<CafeVO> cafeList = new ArrayList<CafeVO>();
	        
	        //요청 URL 
	        driver.get(fullUrl);
	        //driver.manage().wait(timeOutInSeconds03);
	        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
	        
	        debug("[fullUrl:"+fullUrl+"]");
	        
	        try {
	        	StringUtil sUtil = new StringUtil();
	        	
	        	 // 요청
	            driver.get(fullUrl);
	            
	            // 수집 여부 체크  list_info mg_cont clear
	            boolean webCrawlerCf = true;
	            
	            int CountCafe = 0;
	            CountCafe 	 = driver.findElements(By.xpath(categoryVo.getResultListCode())).size();
	            int pagingCount = 0 ;
	            
	        	 while(CountCafe > 0 && webCrawlerCf) {
	             	pagingCount++;
	             	log("페이징 : " +  pagingCount);
	                Thread.sleep(1000);
	                
	                WebElement  WebElement_ul = driver.findElement(By.xpath(categoryVo.getResultListCode()));
	                
	                if(WebElement_ul.getTagName().equals("ul")) {
	                	List<WebElement>  WebElement_li_list = WebElement_ul.findElements(By.xpath("child::*"));
	                	System.out.println("까페 건수:"+WebElement_li_list.size());
	                	
	                	for(int listCount = 0 ; listCount < WebElement_li_list.size() ; listCount++ ) {
	                		long start2 = System.currentTimeMillis();
	                		log("------------------------------------------");
	                		WebElement webElement_li = WebElement_li_list.get(listCount);
	                		
	               		    WebElement a_webElementCafe = webElement_li.findElement(By.xpath(categoryVo.getTitleLinkCode() + "/a"));
	                		
	                		// 해당 까페 제목 클릭 
	                		a_webElementCafe.click();
            				// 해당 디바이스에 텝을 가져온다.
                			List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                			// 2번쨰로 전환
                			driver.switchTo().window(tabs.get(1));
                			// 까페 내용 답을것
	                		CafeVO CafeVo = new CafeVO();
	                		log("까페 순서 : " + (listCount+1));
                			// 실제 수집
	                		CafeVo = getCafeMain(driver, pagingCount, listCount, categoryVo);
	                		cafeList.add(CafeVo);
                			driver.close();
							driver.switchTo().window(tabs.get(0));
	                	}	
	                }
	                
	                // 페이징 에서 Next xpath 확인 
	                List<WebElement>  pagingNextWebElement = driver.findElements(By.xpath(categoryVo.getPagingNextElementCode()));
	                if(pagingNextWebElement.size() > 0 ) {
	                	driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).click();
	                }else {
	                	webCrawlerCf = false;
	                }
	                 
	        	 }    
	        	
	        }catch (Exception e) {
	        	error("[CrawlerCafe][Exception e:"+e+"]");
			}finally {
				//
				driver.close();
		        driver.quit();
			}
	        
	        InsertCafe(cafeList);
	        
			log("[info][webCrawler][CrawlerCafe][end]");
	        
		}
	
		/** 블로그 */
		protected void  crawlerBlog() {
			log("[info][webCrawler][CrawlerCafe][start]");
			
			
			 // 크롬 생성
	        WebDriver driver = new ChromeDriver(chromeOptions);
	        
	        // 추후 DB에서 가져온다.
	        String baseUrl = categoryVo.getBaseUrl();
	        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
	        String startDate= "";
	        String endDate= "";
	        
	        String dateOption = categoryVo.getOptionFnc();
	        
	       	startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyyMMddHHmmss");
	       	//endDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyyMMddHHmmss");
	       	endDate = day + "235959";
       	
       	
	        // 다음 블로그만 나오게 세팅 (티스토리 제외)
	        String postBlogurl = categoryVo.getEtcOption();
	        
	        // 요청 URL 
	        String fullUrl = baseUrl+sortVal+"&q="+keyword+"&sd="+startDate+"&ed="+endDate+"&"+postBlogurl+"&"+dateOption;

	        // 결과 담기
	        ArrayList<BlogVO> blogList = new ArrayList<BlogVO>();
	        
	        try {
	        	StringUtil sUtil = new StringUtil();
	        	
	        	//요청 URL 
		        driver.get(fullUrl);
		        //driver.manage().wait(timeOutInSeconds03);
		        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
		        
		        debug("[fullUrl:"+fullUrl+"]");
		        
		         // 수집 여부
	        	 boolean webCrawlerBl = true;
	        	 int CountBlog = 0;
	             CountBlog 	 = driver.findElements(By.xpath(categoryVo.getResultListCode())).size();
	             
	            int pagingCount = 0 ;
	        	List<WebElement> a_WebElements = null;
	        	
	        	while(CountBlog > 0 && webCrawlerBl) {
	        		pagingCount++;
	              	WebElement  WebElement_ul = driver.findElement(By.xpath(categoryVo.getResultListCode()));
	              	if(WebElement_ul.getTagName().equals("ul")) {
	              		List<WebElement>  WebElement_li_list = WebElement_ul.findElements(By.xpath("child::*"));
	                	log("블로그 건수:"+WebElement_li_list.size());
	                	
                		for(int listCount  = 0  ; listCount < WebElement_li_list.size()  ; listCount++ ) {
                			WebElement webElement_li = WebElement_li_list.get(listCount);
                			WebElement a_webElementBlog = webElement_li.findElement(By.xpath(categoryVo.getTitleLinkCode()));
                			
                			BlogVO  blogVo = new BlogVO();
                			
                      		// 해당 블로그 제목 클릭 
                      		((RemoteWebDriver) driver).executeScript("arguments[0].click();", a_webElementBlog);
                      		
                      		// 해당 디바이스에 텝을 가져온다.
                  			List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                  			// 2번쨰로 전환
                  			driver.switchTo().window(tabs.get(1));
                  			log("블로그 순서: " + (listCount+1));
                			// 실제 수집
                			blogVo = getBlogMain(driver, pagingCount, listCount, categoryVo);
                			blogList.add(blogVo);
                			// 2번쨰 텝 종료
                			driver.close();
                			// 1번쨰로 전환
                			driver.switchTo().window(tabs.get(0));
                			
	                	}
	              	}
	              	
	              	 // 페이징 에서 Next xpath 확인 
	                List<WebElement>  pagingNextWebElement = driver.findElements(By.xpath(categoryVo.getPagingNextElementCode()));
	                if(pagingNextWebElement.size() > 0 ) {
	                	driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).click();
	                }else {
	                	webCrawlerBl = false;
	                }
	        	}
	        	
	        }catch (Exception e) {
	        	error("[CrawlerBlog][Exception e:"+e+"]");
			}finally {
				//
				driver.close();
		        driver.quit();
			}
	        
	        InsertBlog(blogList);
		}
		
		// 메인 데이터 
		protected HashMap<String, Object> getNewsMain(WebDriver driver, int  pagingCount , int mainListCount, CategoryVO categoryVo) {
			
			HashMap<String, Object> map = new HashMap<String, Object> ();
			
			// 클로링 횟수
			int webCrawlerCount = 0;
			//
			int viewListCount = mainListCount+1;
			boolean webCrawlerBoolean = true;
			NewsVO mainVo  = new NewsVO();
			
			while(webCrawlerBoolean ||  webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT) {
				webCrawlerCount++;
				
				if(webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT ) {  
					break;
				}
				
				try {
					
					StringUtil sUtil = new StringUtil();
					mainVo.setMainCategory(categoryVo.getMainCategory());
		        	mainVo.setSubCategory(categoryVo.getSubCategory());

					// 현제 시간
					String insertDate = DateUtil.getCurrSysTime();
					mainVo.setInsertDate(insertDate); 
					
					String crawlerdate = "";
					crawlerdate = insertDate.replace("-", "");
					crawlerdate = crawlerdate.replace(":", "");
					crawlerdate = crawlerdate.replace(" ", "");
					
					mainVo.setCrawlerdate(crawlerdate);
					mainVo.setKeyword(keyword);
					mainVo.setKeywordNum(keywordId);

					debug("[JobStcDyn][메인 시도횟수:"+webCrawlerCount+"]");
					// 기사 URL 주소 ( 네이버 기준 )
					String url = driver.getCurrentUrl();
					//articleVo.setArticleUrl(articleUrl);
					mainVo.setUrl(url);
					log("[JobStcDyn][pagingCount:"+pagingCount+"]");
					debug("[JobStcDyn][pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][기사URL:"+url+"]");
					
	    			// 제목 
	    			String title = driver.findElement(By.xpath(categoryVo.getTitleCode())).getText();
	    			mainVo.setTitle(sUtil.replaceAllWebCrawler(title));
	    			debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][제목:"+title+"]");
	    			

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
	    			
	    			String dataregdate = "";

	    			if(inputdate.length() > 0) {
	    				dataregdate = DateUtil.getDate14(inputdate);
	    			}
	    			
	    			mainVo.setInputdate(inputdate);
	    			mainVo.setUpdate(update);
	    			mainVo.setDataregdate(dataregdate);
	    			
	    			String summary = "";
	    			String press = "";
	    	        List<WebElement> news_summary = new ArrayList();
	    	        
					// 임시
	    			
	    			// 요약이 없는 기사도 있어서 처리함.
	    			List<WebElement> summaryList =  driver.findElements(By.xpath(categoryVo.getSummaryCode())); 
	    			if(summaryList.size() > 0) {
	    				//뉴스 요약본 
	        			if(driver.findElements(By.className("btn_summary")).size()!=0) { 
	        				WebElement newsdriver = driver.findElement(By.xpath(categoryVo.getSummaryclickCode()));
	        				((RemoteWebDriver) driver).executeScript("arguments[0].style='display: block;'",newsdriver);
	        				Thread.sleep(1000);
	        				news_summary = driver.findElements(By.xpath(categoryVo.getSummaryCode()));    
	        				summary = news_summary.get(0).getText() + news_summary.get(1).getText();
	        				debug("[요약:"+summary+"]");
	    					debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][요약:"+summary+"]");
	    					mainVo.setSummary(sUtil.replaceAllWebCrawler(summary));
	        			}else {
	        				debug("요약본없음");
	        			}
	    			}
	    			
	    			// 내용
					String contents = driver.findElement(By.xpath(categoryVo.getContentCode())).getText();
					debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][내용:"+contents+"]");
					mainVo.setContents(sUtil.replaceAllWebCrawler(contents));  
					//    
	    			
					// 언론사
					List<WebElement> pressUrlList = driver.findElements(By.xpath(categoryVo.getWriterCode()));
					if(pressUrlList.size()>0) {
						press = driver.findElement(By.xpath(categoryVo.getWriterCode())).getAttribute("alt").toString();	
						debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][언론사:"+press+"]");
						mainVo.setPress(press);
					}

					// PK 생성
					String docid = EncryptUtil.MD5(url);
					mainVo.setDocid(docid);
					debug("[댓글 수집 전 내용 mainVo:"+mainVo.toString()+"]");
					
					// 댓글 관련 
					ArrayList<NewsCommentVO> newsCommentList= getNewsComment(driver, pagingCount, viewListCount, categoryVo, mainVo.getDocid());
					debug("[pagingCount:"+pagingCount+"][댓글:"+newsCommentList.size()+"]");
					mainVo.setCommentList(newsCommentList);
					webCrawlerBoolean = false;
					
				}catch(Exception e){
					webCrawlerBoolean = true;
					error("[JobStcDyn][getMain][error:"+driver.getCurrentUrl()+"][webCrawlerCount:"+webCrawlerCount+"]");
					error("[JobStcDyn][getMain][error:"+e+"]");
				}
			}
			map.put(WebCrawlerCommon.WEBCRAWLER_BOOLEAN, webCrawlerBoolean);
			map.put(WebCrawlerCommon.MAIN_VO, mainVo);
			return map;
		}
		
		// 댓글
		protected ArrayList<NewsCommentVO> getNewsComment(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo , String mainKey) {
			
	        //  대기 시간
	        // 
	        double timeOutInSeconds01 = 2.2;

			
			HashMap<String, Object> map = new HashMap<>();
			// 댓글 저장 
			ArrayList<NewsCommentVO> commentList = new ArrayList<>();
			Boolean commentBoolean = false;
			
			try {

				 List<WebElement>  u_alex_area_btn_list = driver.findElements(By.xpath(categoryVo.getCommentList()));
				 
				 if(u_alex_area_btn_list.size() > 2) {
						int u_cbox_paginate_clickCount = 0;
						boolean buttondisplayed = false;
						buttondisplayed = driver.findElement(By.xpath(categoryVo.getCommentListSort())).isDisplayed();
 					
 					while(buttondisplayed) {
 						if(driver.findElements(By.className("alex_more")).size()!=0) {
                 			WebElement a_commentCafebtn = driver.findElement(By.xpath(categoryVo.getCommentListSort() + "/button"));
                     		((RemoteWebDriver) driver).executeScript("arguments[0].click();", a_commentCafebtn);
     						u_cbox_paginate_clickCount++;
     						WebCrawlerCommon.waitLoad01(timeOutInSeconds01);
     						debug("더보기 클릭수:"+u_cbox_paginate_clickCount);
 						}else {
 							buttondisplayed = false;
 						}
 					}
				 }	
						 
				List<WebElement>  u_alex_area_ul_li_list = driver.findElements(By.xpath(categoryVo.getCommentListCode()));
				for(int forCount = 0 ; forCount < u_alex_area_ul_li_list.size() ; forCount++ ) {
					WebElement u_cbox_content_wrap_ul_li = u_alex_area_ul_li_list.get(forCount);
					NewsCommentVO newsCommentVO = new NewsCommentVO(); 
					String commentWriter = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentWriter() + (forCount+1) + categoryVo.getCommentWriterExt())).getText();
					String commentContents = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentContents() + (forCount+1) + categoryVo.getCommentContentsExt())).getText();
					String commentDate = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentDate() + (forCount+1) + categoryVo.getCommentDateExt())).getText();
					String likeitGood  = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentRecommand()+ (forCount+1) +categoryVo.getCommentRecommandExt())).getText();
					String disagreeable  = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentNotRecommand()+ (forCount+1) + categoryVo.getCommentNotRecommandExt())).getText();		

					newsCommentVO.setWriter(StringUtil.replaceAllWebCrawler(commentWriter));
					newsCommentVO.setContents(StringUtil.replaceAllWebCrawler(commentContents));
					newsCommentVO.setDate(commentDate);
					newsCommentVO.setRecomm(likeitGood);
					newsCommentVO.setUnrecomm(disagreeable);
					newsCommentVO.setKeyword(keyword);
					newsCommentVO.setKeywordNum(keywordId);
					newsCommentVO.setMainCategory(categoryVo.getMainCategory());
					newsCommentVO.setSubCategory(categoryVo.getSubCategory());
					commentList.add(newsCommentVO);
					
					//
					String insertDate = DateUtil.getCurrSysTime();
					newsCommentVO.setInsertDate(insertDate);
					newsCommentVO.setCrawlerDate(DateUtil.getDate14(insertDate) );
					newsCommentVO.setDataregDate(DateUtil.getDate14(commentDate));

					
					// 카페 원천 Key
					newsCommentVO.setMainKey(mainKey);
					// 
					String docid = EncryptUtil.MD5(commentWriter+"-"+commentDate+"-"+StringUtil.replaceAllWebCrawler(commentContents));
					newsCommentVO.setDocid(docid);

				}
				//
				commentBoolean = true;
			}catch (Exception e) {
				commentBoolean = false;
				commentList = new ArrayList<NewsCommentVO>();
				
			}
			
			map.put(WebCrawlerCommon.WEBCRAWLER_COMMENT_BOOLEAN, commentBoolean);
			map.put(WebCrawlerCommon.COMMENT_List, commentList);
			
			return commentList;
		}
		
		protected void InsertNews(ArrayList<NewsVO> list ) {
			
			SqlSession session = SqlMapClient.getSqlSession();
			
			// 뉴스 메인
			try {
				for(NewsVO newsVo : list) {
					
					int count  = session.selectOne("daum.CrawlerNewsSelectCount", newsVo.getDocid()); 
					//debug("[CrawllerNaver.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][count:"+count+"]");
					
					// 메인 데이터
					// 같은 건이 있을때 건너뒨다.
					if(count > 0) {
						debug("[CrawllerDaum.InsertNews][daum.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][update]"); 
						session.update("daum.CrawlerNewsUpdate", newsVo);
					}else {
						debug("[CrawllerDaum.InsertNews][daum.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][insert]"); 
						session.insert("daum.CrawlerNewsInsert", newsVo);
					}
					// 댓글 데이터
					ArrayList<NewsCommentVO> commentList =  newsVo.getCommentList();
					for(NewsCommentVO commentVo : commentList ) {
						int commentVoCount  = session.selectOne("daum.CrawlerNewscommentSelectCount", commentVo.getDocid());
						
						if(commentVoCount > 0 ) {
							debug("[CrawllerDaum news comment no insert][docid:"+commentVo.getDocid()+"][duplicate]");
						}else {
							debug("[CrawllerDaum.InsertNews][daum.CrawlerNewscommentSelectCount][docid:"+commentVo.getDocid()+"][insert]");
							session.insert("daum.CrawlerNewscommentInsert", commentVo);
						}
					}				
				}
				
				session.commit();

			} catch (Exception e) {
				e.printStackTrace();
				log("e : " + e);
				session.rollback();
			}
		}
		// 메인 데이터 
		protected CafeVO getCafeMain(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo) {
			CafeVO CafeVo = new CafeVO();
			// 클로링 횟수
			int webCrawlerCount = 0;
			//
			int viewListCount = listCount+1;
			boolean webCrawlerBoolean = true;
			
			try {
				
				CafeVo.setMainCategory(categoryVo.getMainCategory());
        		CafeVo.setSubCategory(categoryVo.getSubCategory());
        		CafeVo.setKeyword(keyword);
        		CafeVo.setKeywordNum(keywordId);
 				
				// 현재 시간
				String insertDate = DateUtil.getCurrSysTime();
				CafeVo.setInsertDate(insertDate); 
				
				String crawlerDate = "";
				crawlerDate = insertDate.replace("-", "");
				crawlerDate = crawlerDate.replace(":", "");
				crawlerDate = crawlerDate.replace(" ", "");
				
				CafeVo.setCrawlerDate(crawlerDate);
				log("[pagingCount:"+pagingCount+"]");
				
				debug("[카폐 순서:"+viewListCount+"][cafeCrawlerDate:"+insertDate+"]");
				
    			// 까페 URL 주소 ( 다음 기준 )
    			String cafeUrl = driver.getCurrentUrl();
    			CafeVo.setCafeUrl(cafeUrl);
    			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeUrl:"+cafeUrl+"]");
    			
    			
    			// 해당 내용이  iframe 안에 있어서 다시 선택
    			driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='down']")));
    			
    			// 해당 내용이 있을때까지 대기
    			WebDriverWait wait = new WebDriverWait(driver, 600);
    			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='wrap']")));
    			
    			//작성자 
    			String cafeWriter = ""; 
    			
    			List<WebElement> cafeWriterList =  driver.findElements(By.xpath(categoryVo.getWriterCode()));
    			if(cafeWriterList.size()>0) {
        			cafeWriter = driver.findElement(By.xpath(categoryVo.getWriterCode())).getText();
        			CafeVo.setCafeWriter(cafeWriter);
        			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeWriter:"+cafeWriter+"]");
    			}
    			
    			// 카페 이름
    			String cafeName =  "";
    			List<WebElement> cafeNameList =  driver.findElements(By.xpath(categoryVo.getNameCode()));
    			if(cafeNameList.size()>0) {
        			cafeName =  driver.findElement(By.xpath(categoryVo.getNameCode())).getAttribute("content").split(" - ")[0];
            		CafeVo.setCafeName(StringUtil.replaceAllWebCrawler(cafeName));
            		debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeName:"+cafeName+"]");
    			}

    			// 까페 제목
    			String cafeTitle = "";
    			List<WebElement> cafeTitleList =  driver.findElements(By.xpath(categoryVo.getTitleCode()));
    			if(cafeTitleList.size()>0) {
        			cafeTitle = driver.findElement(By.xpath(categoryVo.getTitleCode())).getText();
        			CafeVo.setCafeTitle(StringUtil.replaceAllWebCrawler(cafeTitle));
        			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeTitle:"+cafeTitle+"]");
    			}

    			// 까페 내용
    			String cafeContentsXpath = "";
    			String cafeContents = "";
    			cafeContentsXpath = cafeContentsXpath + categoryVo.getContentCode();
    			cafeContents = driver.findElement(By.xpath(cafeContentsXpath)).getText();
    			cafeContents = StringUtil.replaceAllWebCrawler(cafeContents);
    			CafeVo.setCafeContents(cafeContents);
    			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeContents:"+cafeContents+"]");
    			
    			// 등록 시간
    			String cafeInputXpath = "";
    			List<WebElement> cafeInputList =  driver.findElements(By.xpath(categoryVo.getInputDateCode()));
    			if(cafeInputList.size()>0) {
        			cafeInputXpath = cafeInputXpath + categoryVo.getInputDateCode();
        			String cafeInput = driver.findElement(By.xpath(cafeInputXpath)).getText();
        			CafeVo.setCafeInput(cafeInput);
        			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeInput:"+cafeInput+"]");
        			
        			String dataregDate = "";
        			if(cafeInput.length() > 0) {
        				dataregDate = DateUtil.getDate14(cafeInput);
        			}
        			
        			CafeVo.setDataregDate(dataregDate);
    			}
    			
    			//댓글 수 
    			String cafeReplXpath = "";
    			cafeReplXpath = cafeReplXpath + "//*[@id='comment-btn']/span";
    			String cafeRepl = driver.findElement(By.xpath(cafeReplXpath)).getText();
    			CafeVo.setCafeRepl(cafeRepl);
    			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeRepl:"+cafeRepl+"]");
    			
    			//추천해요
    			String cafeRecommandXpath = "";
    			cafeRecommandXpath = cafeRecommandXpath + categoryVo.getRecommend();
    			String cafeRecommand = driver.findElement(By.xpath(cafeRecommandXpath)).getText();
    			CafeVo.setCafeRecommand(cafeRecommand);
    			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeRecommand:"+cafeRecommand+"]");
    			
    			//스크랩
    			String cafeScrapXpath = "";
    			cafeScrapXpath = cafeScrapXpath + categoryVo.getScrap();
    			String cafeScrap = driver.findElement(By.xpath(cafeScrapXpath)).getText();
    			CafeVo.setCafeScrap(cafeScrap);
    			debug("[pagingCount:"+pagingCount+"][까페 순서:"+viewListCount+"][cafeScrap:"+cafeScrap+"]");
    			
				// PK 생성
				String docid = EncryptUtil.MD5(cafeUrl);
				CafeVo.setDocid(docid);
    			
				// 댓글 관련 
				ArrayList<CafeCommentVO> cafeCommentList= getCafeComment(driver, pagingCount, viewListCount, categoryVo, CafeVo.getDocid());
				log("[pagingCount:"+pagingCount+"][댓글:"+cafeCommentList.size()+"]");
				CafeVo.setCafeCommentList(cafeCommentList);
				webCrawlerBoolean = false;
				
			}catch(Exception e) {
				WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
				error("[CrawlerCafe][getMain][Exception e:"+e+"]");
				webCrawlerCount++;
			}

			return CafeVo;
		}

		/** 카페 댓글  */
		protected ArrayList<CafeCommentVO> getCafeComment(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo , String mainKey){
			
			CafeVO CafeVo = new CafeVO();
			
			// 댓글 관련 class 확인
			WebElement CommentBox = driver.findElement(By.xpath("//*[@id='comment-btn']/span[1]"));
			
			List<WebElement> CommentBoxPaging = driver.findElements(By.xpath("//*[@id='comment-paging']/ul[1]/child::*"));
			
			if(Integer.parseInt(CommentBox.getText()) > 0 && CommentBoxPaging.size() > 3) {
    			//댓글 페이징 1페이지로 이동 
    			WebElement a_commentCafe = driver.findElement(By.xpath("//*[@id='comment-paging']/ul/li[2]/a"));
        		
        		// 해당 까페 댓글 1페이지 이동 
        		((RemoteWebDriver) driver).executeScript("arguments[0].click();", a_commentCafe);
        		WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
        		
    			// 댓글 대기
    			WebCrawlerCommon.witeLoadVisibilityOfElementLocated(driver,WebCrawlerCommon.timeOutInSeconds_int_10 ,  By.xpath(categoryVo.getCommentList()) );
    			
			}
			
			ArrayList<CafeCommentVO> cafeCommentList = new ArrayList<CafeCommentVO>();
			
			// 작성자
			String cafeCommentWriter = "";
			// 내용
			String cafeCommentContents = "";
			// 작성일자
			String cafeCommentDate = "";
			// 댓글 관련
			String cafeCommentRe = "1"; // 메인
			
			Boolean cafeCommentBl = false;
			
			if(Integer.parseInt(CommentBox.getText()) > 0) {
				
				cafeCommentBl = true;
        		
        		while(cafeCommentBl) {
        			// 댓글
        			List<WebElement> catefCommontList = driver.findElements(By.xpath(categoryVo.getCommentListCode()));
        			debug("[pagingCount:"+pagingCount+"][카폐 순서:"+listCount+"][catefCommontList 갯수:"+catefCommontList.size()+"]");
        			
        			if(catefCommontList.size() > 0) {
        				for(int catefCommontCount = 0 ; catefCommontCount < catefCommontList.size()-1; catefCommontCount++ ) {
        					WebElement  catefCommont = catefCommontList.get(catefCommontCount);
        					
        					// 삭제 여부 확인
        					String commentDeleted = catefCommont.findElement(By.xpath("//*[@class='list_comment']/li["+ (catefCommontCount+1) +"]/div/div/div/div/p")).getText();

        					if(!commentDeleted.equals("삭제된 댓글 입니다.")) {
        						
            					// 댓글에 메인 찾기
            					String catefCommontparseq  = catefCommont.getAttribute("data-parseq");
            					
            					if(catefCommontparseq.equals("0")) {
            						cafeCommentRe = "1";
            					}else {
            						cafeCommentRe = "2";
            					}
        						
        						cafeCommentWriter   = catefCommont.findElement(By.xpath(categoryVo.getCommentWriter() + (catefCommontCount+1) + categoryVo.getCommentWriterExt())).getText();
      						    cafeCommentContents = catefCommont.findElement(By.xpath(categoryVo.getCommentContents() + (catefCommontCount+1) + categoryVo.getCommentContentsExt())).getText();
            					cafeCommentDate     = catefCommont.findElement(By.xpath(categoryVo.getCommentDate() + (catefCommontCount+1) + categoryVo.getCommentDateExt())).getText();
            					
            					CafeCommentVO cafeCommentVo = new CafeCommentVO();
            					cafeCommentVo.setCafeCommentContents(StringUtil.replaceAllWebCrawler(cafeCommentContents));
            					cafeCommentVo.setCafeCommentDate(cafeCommentDate);
            					cafeCommentVo.setCafeCommentWriter(cafeCommentWriter);
            					cafeCommentVo.setCafeCommentRe(cafeCommentRe);
            					cafeCommentVo.setKeyword(keyword);
            					cafeCommentVo.setKeywordNum(keywordId);
            					cafeCommentVo.setMainCategory(categoryVo.getMainCategory());
            					cafeCommentVo.setSubCategory(categoryVo.getSubCategory());
            					cafeCommentList.add(cafeCommentVo);
            					
            					//
            					String insertDate = DateUtil.getCurrSysTime();
            					cafeCommentVo.setInsertDate(insertDate);
            					cafeCommentVo.setCrawlerDate(DateUtil.getDate14(insertDate) );
            					cafeCommentVo.setDataregDate(DateUtil.getDate14(cafeCommentDate));

            					
            					// 카페 원천 Key
            					cafeCommentVo.setMainKey(mainKey);
            					// 
            					String CafeCommentdocid = EncryptUtil.MD5(cafeCommentWriter+"-"+cafeCommentDate+"-"+StringUtil.replaceAllWebCrawler(cafeCommentContents));
            					cafeCommentVo.setDocid(CafeCommentdocid);
        					}
        				}	
        				// 댓글 페이징 이동 
        				List<WebElement>  pagingCommentNextWebElement = driver.findElements(By.xpath(categoryVo.getCommentNextpaging()));
        				 if(pagingCommentNextWebElement.size() > 0 ) {
                         	//driver.findElement(By.xpath("//*[@id='comment-paging']/ul[1]/li[4]/a")).click();
                         	
                 			//댓글 페이징 다음페이지로 이동 
                 			WebElement a_commentCafeNext = driver.findElement(By.xpath(categoryVo.getCommentNextpaging()));
                     		
                     		// 해당 까페 댓글 다음 페이지 이동 
                     		((RemoteWebDriver) driver).executeScript("arguments[0].click();", a_commentCafeNext);
                     		WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
                         }else {
                         	cafeCommentBl = false;
                         }
        			}else {
        				cafeCommentBl = false;
        			}
        		}
        		// 댓글 리스트 저장
    			CafeVo.setCafeCommentList(cafeCommentList);
			}	

			return cafeCommentList;
		}	
		
		protected BlogVO  getBlogMain(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo) {
			// 블로그 내용 답을것
    		BlogVO blogVo = new BlogVO();

			// 클로링 횟수
			int webCrawlerCount = 0;
			//
			int viewListCount = listCount+1;
			boolean webCrawlerBoolean = true;
			
			
			while(webCrawlerBoolean) {
				if(webCrawlerCount >  WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT) {
					break;
				}
				debug("[블로그 순서:"+viewListCount+"][webCrawlerCount:"+webCrawlerCount+"]");
				
				try {
					log("[블로그 순서:"+viewListCount+"]");
					
        			// 수집 하는 시간 표시 :네이버(수집시 X시간 전 )
            		String  blogCrawlerDate = DateUtil.getCurrSysTime();
        			blogVo.setBlogCrawlerDate(blogCrawlerDate);
        			debug("[블로그 순서:"+pagingCount+"][blogCrawlerDate:"+blogCrawlerDate+"]");
        			
        			String crawlerDate = "";
    				crawlerDate = blogCrawlerDate.replace("-", "");
    				crawlerDate = crawlerDate.replace(":", "");
    				crawlerDate = crawlerDate.replace(" ", "");
    				
    				blogVo.setCrawlerDate(crawlerDate);
    				
    				blogVo.setMainCategory(categoryVo.getMainCategory());
        			blogVo.setSubCategory(categoryVo.getSubCategory());
        			
        			blogVo.setKeyword(keyword);
        			blogVo.setKeywordNum(keywordId);
        			
        			// 블로그 URL 주소 ( 네이버 기준 )
        			String blogUrl = driver.getCurrentUrl();
        			blogVo.setBlogUrl(blogUrl);
        			debug("[pagingCount:"+pagingCount+"][블로그 순서:"+viewListCount+"][blogUrl:"+blogUrl+"]");
        			
        			
        			// 블로그 이름 area-slogun headertextalignleft
        			//String blogName = driver.findElement(By.xpath("//*[@id='titleBlogName']/a/img")).getAttribute("alt");
        			
        			List<WebElement> blogNameList =  driver.findElements(By.xpath(categoryVo.getNameCode()));
        			if(blogNameList.size()>0) {
            			String blogName = "";
            			blogName =  driver.findElement(By.xpath(categoryVo.getNameCode())).getAttribute("content");
            			
            			blogVo.setBlogName(StringUtil.replaceAllWebCrawler(blogName));
            			debug("[pagingCount:"+pagingCount+"][블로그 순서:"+viewListCount+"][blogName:"+blogName+"]");
        			}
        			
        			// 블로그 제목
        			List<WebElement> blogTitleList =  driver.findElements(By.xpath(categoryVo.getTitleCode()));
        			if(blogTitleList.size()>0) {
            			String blogTitle = "";
            			blogTitle = driver.findElement(By.xpath(categoryVo.getTitleCode())).getAttribute("content");
            			
            			blogVo.setBlogTitle(StringUtil.replaceAllWebCrawler(blogTitle));
            			debug("[pagingCount:"+pagingCount+"][블로그 순서:"+viewListCount+"][blogTitle:"+blogTitle+"]");
        			}

        			// 블로그 내용
        			String blogContentsXpath = "";
        			String blogContents = "";
        			
        			int countblogContentsXpath 		= 0;
        			int countsecblogContentsXpath 	= 0;
        			
        			countblogContentsXpath 		= driver.findElements(By.xpath(categoryVo.getContentCode())).size(); 
        			countsecblogContentsXpath 	= driver.findElements(By.xpath(categoryVo.getContentCodeExt())).size();
        			
        			if(countblogContentsXpath > 0) {
            			blogContentsXpath = blogContentsXpath + categoryVo.getContentCode();
        			}else if(countsecblogContentsXpath > 0) {
            			blogContentsXpath = blogContentsXpath + categoryVo.getContentCodeExt();
        			}else {
        				blogContentsXpath = blogContentsXpath + categoryVo.getContentCodeExt2();
        			}

        			blogContents = driver.findElement(By.xpath(blogContentsXpath)).getText();
        			
        			blogVo.setBlogContents(StringUtil.replaceAllWebCrawler(blogContents));
        			debug("[pagingCount:"+pagingCount+"][블로그 순서:"+viewListCount+"][blogContents:"+blogContents+"]");
        			
        			// 등록 시간
        			String blogInputXpath = "";
        			
        			int countblogInputXpath = 0; 
        			countblogInputXpath = driver.findElements(By.xpath("//*[@class='cB_Tdate']")).size(); 
        			 if(countblogInputXpath > 0) {
        				 blogInputXpath = blogInputXpath + "//*[@class='cB_Tdate']";	 
        			 }else {
        				 blogInputXpath = blogInputXpath + "//*[@class='article-header']/div/div/div/p[@class='date']";
        			 }
        			
        			String blogInput = driver.findElement(By.xpath(blogInputXpath)).getText();
        			blogVo.setBlogInput(blogInput);
        			debug("[pagingCount:"+pagingCount+"][블로그 순서:"+viewListCount+"][blogInput:"+blogInput+"]");
        			
        			String dataregDate = "";
        			if(blogInput.length() > 0) {
        				dataregDate = DateUtil.getDate14(blogInput);
        			}
        			
        			blogVo.setDataregDate(dataregDate);
        			
        			// PK 생성
					String docid = EncryptUtil.MD5(blogUrl);
					blogVo.setDocid(docid);
					webCrawlerBoolean = false;
				}catch(Exception e) {
					WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
					error("[CrawlerDaum][getBlogMain][Exception e:"+e+"]");
					webCrawlerCount++;
				}	
			}
			return blogVo;
		}	
		protected void InsertCafe(ArrayList<CafeVO> list ) {
			
			SqlSession session = SqlMapClient.getSqlSession();
			
			// 까페 메인
			try {
				for(CafeVO cafeVo : list) {
					
					int count  = session.selectOne("daum.CrawlerCafeSelectCount", cafeVo.getDocid()); 
					//debug("[CrawllerNaver.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][count:"+count+"]");
					
					// 메인 데이터
					// 같은 건이 있을때 건너뒨다.
					if(count > 0) {
						debug("[CrawllerDaum.InsertNews][daum.CrawlerCafeSelectCount][docid:"+cafeVo.getDocid()+"][update]"); 
						session.update("daum.CrawlerCafeUpdate", cafeVo);
					}else {
						debug("[CrawllerDaum.InsertNews][daum.CrawlerCafeSelectCount][docid:"+cafeVo.getDocid()+"][insert]"); 
						session.insert("daum.CrawlerCafeInsert", cafeVo);
					}
					
					// 댓글 데이터
					ArrayList<CafeCommentVO> commentList =  cafeVo.getCafeCommentList();
					for(CafeCommentVO commentVo : commentList ) {
						int commentVoCount  = session.selectOne("daum.CrawlerCafecommentSelectCount", commentVo.getDocid());
						
						if(commentVoCount > 0 ) {
							debug("[CrawllerDaum.InsertNews][daum.CrawlerCafecommentSelectCount][docid:"+commentVo.getDocid()+"][no insert]");
						}else {
							debug("[CrawllerDaum.InsertNews][daum.CrawlerCafecommentSelectCount][docid:"+commentVo.getDocid()+"][insert]");
							session.insert("daum.CrawlerCafecommentInsert", commentVo);
						}
					}
				}
				
				session.commit();

			} catch (Exception e) {
				e.printStackTrace();
				log("e : " + e);
				session.rollback();
			}

		}
		
		protected void InsertBlog(ArrayList<BlogVO> list ) {
			
			SqlSession session = SqlMapClient.getSqlSession();
			
			// 블로그
			try {
				for(BlogVO blogVo : list) {
					
					int count  = session.selectOne("daum.CrawlerBlogSelectCount", blogVo.getDocid()); 
					//debug("[CrawllerDaum.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][count:"+count+"]");
					
					// 메인 데이터
					// 같은 건이 있을때 건너뒨다.
					if(count > 0) {
						debug("[CrawllerDaum Blog main no insert][no insert][docid:"+blogVo.getDocid()+"][duplicate]"); 
						//session.update("naver.CrawlerBlogUpdate", blogVo);
					}else {
						debug("[CrawllerDaum.InsertBlog][daum.CrawlerBlogInsert][docid:"+blogVo.getDocid()+"][insert]"); 
						session.insert("daum.CrawlerBlogInsert", blogVo);
					}	
				}
				
				session.commit();

			} catch (Exception e) {
				e.printStackTrace();
				log("e : " + e);
				session.rollback();
			}
		}
			
}


