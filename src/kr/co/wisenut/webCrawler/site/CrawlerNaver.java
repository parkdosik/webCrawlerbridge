package kr.co.wisenut.webCrawler.site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import kr.co.wisenut.webCrawler.vo.BlogVO;
import kr.co.wisenut.webCrawler.vo.CafeCommentVO;
import kr.co.wisenut.webCrawler.vo.CafeVO;
import kr.co.wisenut.webCrawler.vo.CategoryVO;
import kr.co.wisenut.webCrawler.vo.NewsCommentVO;
import kr.co.wisenut.webCrawler.vo.NewsVO;

public class CrawlerNaver  extends Crawler {

	public CrawlerNaver(PropertiesVO propertiesVo, String keywordId, String keyword, CategoryVO categoryVo, String day) {
		super(propertiesVo, keywordId, keyword, categoryVo, day);
		// TODO Auto-generated constructor stub
	}
	
	
	/** 간단한 구분값  */
	public void crawler() {
		
		if(categoryVo.getSubCategory().equals("news")) {
			crawlerNews();
		}else if(categoryVo.getSubCategory().equals("cafe")) {
			crawlerCafe();
		}else if(categoryVo.getSubCategory().equals("blog")) {
			crawlerBlog();
		}
	}
	
	/** 뉴스 */
	protected boolean crawlerNews() {
		log("[info][webCrawler][CrawlerNaver.crawlerNews][start]");
		// 크롬 생성
        WebDriver driver = new ChromeDriver(chromeOptions);
        
        // 추후 DB에서 가져온다.
        String baseUrl = categoryVo.getBaseUrl();
        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
        String startDate= "";
        String endDate= "";
        
        String dateOption = categoryVo.getOptionFnc();
        String fullUrl = "";
        
        
    	startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
    	endDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
    	
    	fullUrl = baseUrl+sortVal+"&query="+keyword+"&ds="+startDate+"&de="+endDate+"&"+dateOption;
        // 결과 담기
        ArrayList<NewsVO> list = new ArrayList<NewsVO>();
        
        //요청 URL 
        driver.get(fullUrl);
        //driver.manage().wait(timeOutInSeconds03);
        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
        log("[fullUrl:"+fullUrl+"]");
        
        // 수집 여부
        boolean webCrawlerBl = true;
        int pagingCount = 0 ;
        
        try {
        	// 검색 결과 테그값 
        	debug("categoryVo.getResultListCode()):"+categoryVo.getResultListCode());
        	List<WebElement>  searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()));
        	
        	// 검색결과 테그가 여부값
        	int searchListSize = searchList.size();
        	if(searchListSize > 0) {
        		while(webCrawlerBl) {
                	pagingCount++;
            		searchList = driver.findElements(By.xpath(categoryVo.getResultListCode() + "/child::*"));
            		
            		for(int searchListCount  = 0  ; searchListCount < searchList.size()  ; searchListCount++ ) {
            			WebElement list_li = searchList.get(searchListCount) ;
            			List<WebElement> detailLinkElements  = list_li.findElements(By.xpath(categoryVo.getPortalLinkCode()));
            			List<WebElement> a_WebElements = driver.findElements(By.xpath(categoryVo.getTitleLinkCode()));
            			if(detailLinkElements.get(searchListCount).getText().contains(WebCrawlerCommon.NEWS_NAME_NAVER)) {
            				list_li.findElement(By.xpath(".//*[@class='info_group']/a[2]")).click();
            				List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                  			driver.switchTo().window(tabs.get(1));
                  			log("뉴스 순서 : " + (searchListCount+1));
                  			NewsVO  mainVo  = getNewsMain(driver , pagingCount , searchListCount, categoryVo );
                  			list.add(mainVo);
                  			driver.close();
                  			driver.switchTo().window(tabs.get(0));
            			}else {
        					NewsVO  mainVo  = new NewsVO();
        					mainVo.setPressUrl(list_li.findElement(By.xpath(categoryVo.getTitleLinkCode2())).getAttribute("href").toString());
        					mainVo.setTitle(list_li.findElement(By.xpath(categoryVo.getTitleLinkCode2())).getText());
        					log("[네이버뉴스 X][pagingCount:"+pagingCount+"][기사 순서"+searchListCount+"][기사URL:"+a_WebElements.get(searchListCount).getAttribute("href").toString()+"]");
            			}
            		}
            		
            		// 페이징 에서 더 보기 css 확인
            		List<WebElement>  pagingNextWebElement = driver.findElements(By.xpath(categoryVo.getPagingNextElementCode()));
                    debug("pagingNextWebElement:"+pagingNextWebElement.size());
                    
                    debug("paging code : " + categoryVo.getPagingNextElementCode());
                    debug("paging val : " + driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).getAttribute("aria-disabled").toString());
                    //log("paging boolean : " + driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).getAttribute("aria-disabled").toString().equals("false"));
                    if(driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).getAttribute("aria-disabled").toString().equals("false")) {
                		driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).click();
                	}else {
                		webCrawlerBl = false;
                	}
                    
                    
        		}
        		
        		
        		
        	}else {
        		debug("[info][webCrawler][CrawlerNaver.crawlerNews][ no searchList count]");
        	}
        }catch (Exception e) {
        	error("[CrawlerNews][Exception e:"+e+"]");
		}finally {
			//
			driver.close();
	        driver.quit();
		}
        debug("[info][webCrawler][CrawlerNaver.crawlerNews][crawlerNews count:"+list.size()+"]");
        
        
        
        // DB INSERT
        InsertNews(list);
        
        debug("[info][webCrawler][CrawlerNaver.crawlerNews][start]");
        
		
		
		return true;
	}
	
	/** 뉴스 메인  */
	protected NewsVO getNewsMain(WebDriver driver, int  pagingCount , int mainListCount, CategoryVO categoryVo) {
		
		// 클로링 횟수
		int webCrawlerCount = 0;
		//
		int viewListCount = mainListCount+1;
		boolean webCrawlerBoolean = true;
		NewsVO newsVo  = new NewsVO();
		
		while(webCrawlerBoolean ||  webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT) {
			webCrawlerCount++;
			
			if(webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT ) {  
				break;
			}
			
			try {
				
				
				newsVo.setSearchNum(categoryVo.getSearchNum());
				debug("[JobStcDyn][메인 시도횟수:"+webCrawlerCount+"]");
				
				// 현제 시간
				String insertDate = DateUtil.getCurrSysTime();
				newsVo.setInsertDate(insertDate); 
				
				String crawlerdate = "";
				crawlerdate = insertDate.replace("-", "");
				crawlerdate = crawlerdate.replace(":", "");
				crawlerdate = crawlerdate.replace(" ", "");
				
				newsVo.setCrawlerDate(crawlerdate); 
				newsVo.setKeyword(keyword);
				newsVo.setKeywordNum(keywordId);
				
				// PK 생성  : URL + 카테고리 넘버 : 첫번째 생성으로 기록ㅊ
				String docid = "";
				// URL 주소 ( 네이버 기준 )
				String url = driver.getCurrentUrl();
				
				// 강제로 건너띠기 ( 스포츠 ) 
				if(url.indexOf("sports") != -1 ) {
					break;
					//return newsVo;
				}
				
				//articleVo.setArticleUrl(articleUrl);
	        	newsVo.setMainCategory(categoryVo.getMainCategory());
	        	newsVo.setSubCategory(categoryVo.getSubCategory());
	        	debug("[JobStcDyn][pagingCount:"+pagingCount+"][PK:"+url+"-"+categoryVo.getSearchNum()+"]");
				newsVo.setUrl(url);
				log("[JobStcDyn][pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][기사URL:"+url+"]");
				
				docid = EncryptUtil.MD5(url+"-"+categoryVo.getSearchNum());
				newsVo.setDocid(docid);	
				debug("[JobStcDyn][pagingCount:"+pagingCount+"][PK MD5:"+newsVo.getDocid()+"]");
				//}
				
				
				
				List<WebElement> pressUrlList = driver.findElements(By.xpath(categoryVo.getPressUrlCode()));
				
				if(pressUrlList.size() > 0 ) {
					String pressUrl = driver.findElement(By.xpath(categoryVo.getPressUrlCode())).getAttribute("href");
					newsVo.setPressUrl(pressUrl);
					debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][언론사 URL :"+pressUrl+"]");
    			}
				
    			// 제목 
				List<WebElement> titleList = driver.findElements(By.xpath(categoryVo.getTitleCode()));
				if(titleList.size()>0) {
	    			String title = driver.findElement(By.xpath(categoryVo.getTitleCode())).getText();
	    			newsVo.setTitle(StringUtil.replaceAllWebCrawler(title));
	    			debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][제목:"+title+"]");
				}

    			// 기사 입력 , 최종수정 
    			List<WebElement> tt1ClassWebElementList = driver.findElements(By.xpath(categoryVo.getInputDateCode()));
    			int tt1ClassWebElementListSize = tt1ClassWebElementList.size();
    			debug("[pagingCount:"+pagingCount+"][tt1ClassWebElementListSize:"+tt1ClassWebElementListSize+"]");
    			
    			// 기사입력
    			String inputdate = "";
    			// 최종수정
    			String update = "";
    			
    			for(int tt1ClassWebElementListCount = 0 ; tt1ClassWebElementListCount < tt1ClassWebElementListSize ; tt1ClassWebElementListCount++ ) {
    				WebElement tt1ClassWebElement = tt1ClassWebElementList.get(tt1ClassWebElementListCount);
    				if(tt1ClassWebElementListCount == 0) {
 						inputdate = tt1ClassWebElement.getText();
    				}else if(tt1ClassWebElementListCount == 1) {
    					update = tt1ClassWebElement.getText();
    				}
    			}
    			debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][inputdate:"+inputdate+"]");
    			debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][inputdate:"+update+"]");
    			newsVo.setInputdate(inputdate);
    			newsVo.setUpdate(update);
    			
    			String dataregdate = "";

    			if(inputdate.length() > 0) {
    				dataregdate = DateUtil.getDate14(inputdate);
    			}
    			
    			newsVo.setDataregDate(dataregdate);
    			
    			String summary = "";
    			String press = "";
    	        List<WebElement> news_summary = new ArrayList();
    	        
				// 임시
    			
    			// 요약이 없는 기사도 있어서 처리함.
    			List<WebElement> summaryList =  driver.findElements(By.xpath(categoryVo.getSummaryExistsCode())); 
    			//log("summaryList:"+summaryList.size());
    			if(summaryList.size()>3) {
        			driver.findElement(By.xpath(categoryVo.getSummaryclickCode())).sendKeys(Keys.ENTER);
    				summary = WebCrawlerCommon.waitText(driver, By.xpath(categoryVo.getSummaryCode()), 5);
    				//log("[요약:"+summary+"]");
    				debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][요약:"+summary+"]");
    				newsVo.setSummary(StringUtil.replaceAllWebCrawler(summary));
    			}
    			// 내용
				String contents = driver.findElement(By.xpath(categoryVo.getContentCode())).getText();
				debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][내용:"+contents+"]");
				int reportEndPosition = 0;
				reportEndPosition = contents.indexOf("기자");
				if(reportEndPosition>0) {
					String reporter = contents.substring(reportEndPosition-4, reportEndPosition);
					newsVo.setReporter(StringUtil.replaceAllWebCrawler(reporter));
				}else {
					String reporter = "기고";
					newsVo.setReporter(reporter);
				}
				newsVo.setContents(StringUtil.replaceAllWebCrawler(contents));
				
				//    
    			
				// 언론사
				List<WebElement> pressList =  driver.findElements(By.xpath(categoryVo.getWriterCode())); 
				if(pressList.size()>0) {
					press = driver.findElement(By.xpath(categoryVo.getWriterCode())).getAttribute("title").toString();
					debug("[pagingCount:"+pagingCount+"][기사 순서"+viewListCount+"][언론사:"+press+"]");
					newsVo.setPress(press);
				}

				// 기사 언론 반응
				// 좋아요
				String likeitGood = "";
				List<WebElement> likeitGoodList = driver.findElements(By.xpath(categoryVo.getLikeitGood()));
				if(likeitGoodList.size() > 0) {
					likeitGood = driver.findElement(By.xpath(categoryVo.getLikeitGood())).getText();
				}
				newsVo.setLikeitGood(likeitGood);
				
				//훈훈해요
				String likeitWarm =  "";
				List<WebElement> likeitWarmList =   driver.findElements(By.xpath(categoryVo.getLikeitWarm()));
				if(likeitWarmList.size() > 0 ) {
					likeitWarm = driver.findElement(By.xpath(categoryVo.getLikeitWarm())).getText();
				}
				newsVo.setLikeitWarm(likeitWarm);
				
				//슬퍼요 
				String likeitSad = "";
				List<WebElement> likeitSadList =   driver.findElements(By.xpath(categoryVo.getLikeitSad()));
				if(likeitSadList.size() > 0) {
					likeitSad = driver.findElement(By.xpath(categoryVo.getLikeitSad())).getText();
				}
				newsVo.setLikeitSad(likeitSad);
				
				//화나요 
				String likeitAngry = "";
				List<WebElement> likeitAngryList =   driver.findElements(By.xpath(categoryVo.getLikeitAngry()));
				if(likeitAngryList.size() > 0 ) {
					likeitAngry = driver.findElement(By.xpath(categoryVo.getLikeitAngry())).getText();
				}
				newsVo.setLikeitAngry(likeitAngry);
				
				//화나요 
				String likeitWant = "" ;
				List<WebElement> likeitWantList =   driver.findElements(By.xpath(categoryVo.getLikeitWant()));
				if(likeitWantList.size() > 0) {
					likeitWant = driver.findElement(By.xpath(categoryVo.getLikeitWant())).getText();
				}
				newsVo.setLikeitWant(likeitWant);
				
				
				
				// 댓글 관련 
				ArrayList<NewsCommentVO> newsCommentList= getNewsComment(driver, pagingCount, viewListCount, categoryVo, newsVo.getDocid());
				log("[pagingCount:"+pagingCount+"][기사 순서:"+viewListCount+"][댓글:"+newsCommentList.size()+"]");
				newsVo.setCommentList(newsCommentList);
				
				//boolean webCrawlerCommentBoolean = (boolean) commentMap.get(WebCrawlerCommon.WEBCRAWLER_COMMENT_BOOLEAN);
    			//if(webCrawlerCommentBoolean) {
    			//	ArrayList<CommentVO>  commentList = (ArrayList<CommentVO>) commentMap.get(WebCrawlerCommon.COMMENT_List);
    			//	mainVo.setCommentList(commentList);
    			//}
				
				
				webCrawlerBoolean = false;
				
			}catch(Exception e){
				webCrawlerBoolean = true;
				error("[JobStcDyn][getMain][error:"+driver.getCurrentUrl()+"][webCrawlerCount:"+webCrawlerCount+"]");
				error("[JobStcDyn][getMain][error:"+e+"]");
			}
		}
		
		return newsVo;
	}
	
	protected ArrayList<NewsCommentVO> getNewsComment(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo , String mainKey) {
		
		debug("[댓글 DOCID  : " + mainKey +"]");
		
		// 댓글 더보기 선택
		// 댓글 저장
		ArrayList<NewsCommentVO> list = new ArrayList<>();
		try {
			Boolean articleCommentBl = false;
			
			// 댓글 자체가 없을수 있어세 해당 부분 체크 
			int CommentCnt = 0;
			List<WebElement> CommentWebElementSize = driver.findElements(By.xpath("//*[@id='cbox_module']/child::*"));
			String pressLinkClass = driver.findElement(By.xpath("//*[@id='cbox_module']/div[1]")).getAttribute("class");
			if(CommentWebElementSize.size()==1 && !pressLinkClass.equals("article_simplecmt")) {
					CommentCnt = Integer.parseInt(driver.findElement(By.xpath("//*[@id='cbox_module']/div[1]/div[1]/a/span[1]")).getText());				
			}else if(CommentWebElementSize.size()==2) {
					CommentCnt = Integer.parseInt(driver.findElement(By.xpath("//*[@id='cbox_module']/div[2]/div[1]/a/span[1]")).getText().replace(",", ""));				
			}


			if(CommentCnt > 0) {
				articleCommentBl = true;
			}
			if(articleCommentBl) {
				//댓긆이 하나라도 있으면 1차 댓글 더보기 먼저 클릭한다.
				driver.findElement(By.xpath(".//*[@class='u_cbox_btn_view_comment']")).click();
				WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds01);
				
				List<WebElement> articleCommonSort = driver.findElements(By.xpath(categoryVo.getCommentListSort()));
				if(articleCommonSort.size() > 0) {
					driver.findElement(By.xpath(categoryVo.getCommentListSort())).click();
				}else {
					debug("댓글 최신순이 없다.");
				}

				// 1차 댓글 더보기 후에 하단에 더보기 버튼이 존재 하면 클릭 하여 펼친다. 
				String u_cbox_paginate_display = driver.findElement(By.xpath(categoryVo.getCommentPaginate())).getCssValue("display");
				Boolean u_cbox_paginate_display_bl = driver.findElement(By.xpath(categoryVo.getCommentPaginate())).isDisplayed();
				
				debug("시작시 : 더보기 화면 출력 형태:"+u_cbox_paginate_display+"-"+u_cbox_paginate_display_bl);
				int u_cbox_paginate_clickCount = 0;
				while(u_cbox_paginate_display_bl) {
					// 더 보기 클릭
					driver.findElement(By.xpath(categoryVo.getCommentPaginate() + "/a")).click();
					u_cbox_paginate_clickCount++;
					WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds01);
					debug("더보기 클릭수:"+u_cbox_paginate_clickCount);
					
					// css 값 변경
					u_cbox_paginate_display = driver.findElement(By.xpath(categoryVo.getCommentPaginate())).getCssValue("display");
					u_cbox_paginate_display_bl = driver.findElement(By.xpath(categoryVo.getCommentPaginate())).isDisplayed();
					debug("중간 : 더보기 화면 출력 형태:"+u_cbox_paginate_display+"-"+u_cbox_paginate_display_bl);
				}
				
				// 더보기 다 했을 경우
				// 댓글 li
				debug("더보기 화면 선택 끝:");
				WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds01);
				
				String findClass = "";
				String secClass  = "";
				List<WebElement>  u_cbox_content_wrap_ul_li_list = driver.findElements(By.xpath(categoryVo.getCommentList()));
				log("u_cbox_content_wrap_ul_li 갯수:"+u_cbox_content_wrap_ul_li_list.size());
				for(int forCount = 0 ; u_cbox_content_wrap_ul_li_list.size() > forCount ; forCount++ ) {
					log("u_cbox_content_wrap_ul_li count:" + forCount);
					WebElement u_cbox_content_wrap_ul_li = u_cbox_content_wrap_ul_li_list.get(forCount);
					NewsCommentVO newsCommentVO = new NewsCommentVO();
					findClass = u_cbox_content_wrap_ul_li.findElement(By.xpath("//*[@class='u_cbox_content_wrap']/ul[@class='u_cbox_list']/li["+(forCount+1)+"]/div[1]")).getAttribute("class");
					//댓글 중 작성자에 의해 삭제된 댓글 및 클린봇 필터링 된 댓글 컨텐츠만 등록 
					if(findClass.equals("u_cbox_comment_box")) {
						secClass  = u_cbox_content_wrap_ul_li.findElement(By.xpath("//*[@class='u_cbox_content_wrap']/ul[@class='u_cbox_list']/li["+(forCount+1)+"]/div[@class='u_cbox_comment_box']/div[@class='u_cbox_area']/div[2]/span[1]")).getAttribute("class");
						if(!secClass.equals("u_cbox_cleanbot_contents")) {
							String writer = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentWriter()+(forCount+1)+categoryVo.getCommentWriterExt())).getText();
							String contents = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentContents()+(forCount+1)+categoryVo.getCommentContentsExt())).getText();
							String date = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentDate()+(forCount+1)+categoryVo.getCommentDateExt())).getText();
							String recomm = "";
							String unrecomm = "";
							
							List<WebElement> commentRecommList = u_cbox_content_wrap_ul_li.findElements(By.xpath(categoryVo.getCommentRecommand()+(forCount+1)+categoryVo.getCommentRecommandExt()));
							if(commentRecommList.size() > 0 ) {
								recomm = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentRecommand()+(forCount+1)+categoryVo.getCommentRecommandExt())).getText();
							}
							List<WebElement> commentUnrecommList = u_cbox_content_wrap_ul_li.findElements(By.xpath(categoryVo.getCommentNotRecommand()+(forCount+1)+categoryVo.getCommentNotRecommandExt()));
							if(commentUnrecommList.size() > 0) {
								unrecomm = u_cbox_content_wrap_ul_li.findElement(By.xpath(categoryVo.getCommentNotRecommand()+(forCount+1)+categoryVo.getCommentNotRecommandExt())).getText();
							}
							
							newsCommentVO.setWriter(writer); 
							newsCommentVO.setContents(StringUtil.replaceAllWebCrawler(contents));
							newsCommentVO.setDate(date);
							newsCommentVO.setRecomm(recomm);
							newsCommentVO.setUnrecomm(unrecomm);
							newsCommentVO.setKeyword(keyword);
							newsCommentVO.setKeywordNum(keywordId);
							newsCommentVO.setMainCategory(categoryVo.getMainCategory());
							newsCommentVO.setSubCategory(categoryVo.getSubCategory());
							
							
							//
							String insertDate = DateUtil.getCurrSysTime();
							newsCommentVO.setInsertDate(insertDate);
							newsCommentVO.setCrawlerDate(DateUtil.getDate14(insertDate) );
							newsCommentVO.setDataregDate(DateUtil.getDate14(date));
							
							
							// 뉴스 키값
							newsCommentVO.setMainKey(mainKey);
							// PK 값
							String docid = EncryptUtil.MD5(writer+"-"+date+"-"+StringUtil.replaceAllWebCrawler(contents));
							newsCommentVO.setDocid(docid);
							
							list.add(newsCommentVO);	
						}else {
							debug("클린봇 대상글입니다.");
						}
						
					}else{
						debug("삭제된 글입니다.");
					}
					
				}	
				
			}else {
				debug("댓글 없음");
			}
			
		}catch (Exception e) {
			log("[CrawlerNaver.getNewsComment error:"+e+" ]");
			error("[CrawlerNaver.getNewsComment error:"+e+" ]");
		}
		
		
		
		return list;
	}
	
	
	protected void InsertNews(ArrayList<NewsVO> list ) {
		
		SqlSession session = SqlMapClient.getSqlSession();
		
		// 뉴스 메인
		try {
			for(NewsVO newsVo : list) {
				
				int count  = session.selectOne("naver.CrawlerNewsSelectCount", newsVo); 
				//debug("[CrawllerNaver.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][count:"+count+"]");
				
				// 메인 데이터
				// 같은 건이 있을때 건너뒨다.
				if(count > 0) {
					debug("[CrawllerNaver.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][update]"); 
					session.update("naver.CrawlerNewsUpdate", newsVo);
				}else {
					debug("[CrawllerNaver.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][insert]"); 
					session.insert("naver.CrawlerNewsInsert", newsVo);
				}
				// 댓글 데이터
				ArrayList<NewsCommentVO> commentList =  newsVo.getCommentList();
				for(NewsCommentVO commentVo : commentList ) {
					int commentVoCount  = session.selectOne("naver.CrawlerNewscommentSelectCount", commentVo);
					
					if(commentVoCount > 0 ) {
						debug("[CrawllerNaver news comment no insert][docid:"+commentVo.getDocid()+"][duplicate]");
					}else {
						debug("[CrawllerNaver.InsertNews][naver.CrawlerNewscommentSelectCount][docid:"+commentVo.getDocid()+"][insert]");
						session.insert("naver.CrawlerNewscommentInsert", commentVo);
					}
				}				
			}
			
			session.commit();

		} catch (Exception e) {
			log("[CrawlerNaver.InsertNews error: " + e+"]");
			error("[CrawlerNaver.InsertNews error: " + e+"]");
			
			e.printStackTrace();
			log("e : " + e);
			session.rollback();
		}
	}
	
	/** 카페 */
	protected boolean  crawlerCafe() {
		debug("[info][webCrawler][CrawlerNaver.crawlerCafe][start]");
		// 크롬 생성
        WebDriver driver = new ChromeDriver(chromeOptions);
        
        // 추후 DB에서 가져온다.
        String baseUrl = categoryVo.getBaseUrl();
        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
        String startDate= "";
        String endDate= "";
        
        String dateOption = categoryVo.getOptionFnc();
        String fullUrl = "";
        
        
        startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
    	endDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
    	
    	fullUrl = baseUrl+sortVal+"&query="+keyword+"&date_from="+startDate+"&date_to="+endDate+"&"+dateOption;	
    	
    
        // 결과 담기
        ArrayList<CafeVO> list = new ArrayList<CafeVO>();
        
        //요청 URL 
        driver.get(fullUrl);
        //driver.manage().wait(timeOutInSeconds03);
        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
        
        debug("[fullUrl:"+fullUrl+"]");
        // 수집 여부
        boolean webCrawlerBl = true;
        // 네이버 스크롤 방식
        String naverListAjax  = "";
        int pagingCount = 0 ;
        try {
        	
        	// 검색 결과 테그값 
        	debug("categoryVo.getResultListCode()):"+categoryVo.getResultListCode());
        	List<WebElement>  searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()));
        	
        	// 검색결과 테그가 여부값
        	int searchListSize = searchList.size();
        	if(searchListSize > 0) {
        		searchList = driver.findElements(By.xpath(categoryVo.getResultListCode() + "/child::*"));
        		
        		while(webCrawlerBl) {
                	pagingCount++;
            		
        			// 네이버
        			List<WebElement>  reviewLoadingList = driver.findElements(By.xpath(categoryVo.getPagingNextElementCode()));
        			if(reviewLoadingList.size() > 0 ) {
        				String thisListAjax = driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).getAttribute("data-api");
            			//oldUrl  = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]")).getAttribute("data-api");
            			//WebElement WebElementTest = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
            			debug("[pagingCount:"+pagingCount+"]thisListAjax : "+ thisListAjax);
            			debug("[pagingCount:"+pagingCount+"]naverListAjax : "+ naverListAjax);
            			
            			
            			//log("[reviewCount:"+reviewCount+"][data-api:"+WebElementTest.getAttribute("data-api")+"]");
                		JavascriptExecutor js = (JavascriptExecutor) driver;
                		WebElement element = driver.findElement(By.xpath(categoryVo.getPagingNextElementCode()));
                		// This will scroll the page till the element is found
                		js.executeScript("arguments[0].scrollIntoView(true);", element);
                		
                		WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04); 
                		
                		// 다시 있는지 확인
                		List<WebElement>  naverListAjaxreviewLoadingList = driver.findElements(By.xpath(categoryVo.getPagingNextElementCode()));
                		debug("[pagingCount:"+pagingCount+"][naverListAjaxreviewLoadingList.size:"+naverListAjaxreviewLoadingList.size()+"]");
                		if(naverListAjaxreviewLoadingList.size() > 0) {
                			naverListAjax = driver.findElement(By.xpath(categoryVo.getPagingNextElementCode())).getAttribute("data-api");
                			debug("[pagingCount:"+pagingCount+"][naverListAjax 1 :"+naverListAjax+"]");
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
        			// 수집이 끝났다면
        			if(!webCrawlerBl) {
        				searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()+"/child::*"));
        				
        				debug("[info][webCrawler][searchList count :"+searchList.size()+"]");
        				
        				// 메인 데이터 수집
            			for(int listCount = 0 ; listCount < searchList.size() ; listCount++ ) {
            				WebElement oneRow = searchList.get(listCount);
            				
            				CafeVO  cafeVo = new CafeVO();
            				
            				log("까페 순서 : " + (listCount+1));
            				oneRow.findElement(By.xpath("./*[@class='total_wrap api_ani_send']/a")).sendKeys(Keys.ENTER);
            				// 해당 디바이스에 텝을 가져온다.
                			List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                			// 2번쨰로 전환
                			driver.switchTo().window(tabs.get(1));
                			// 실제 수집
                			cafeVo = getCafeMain(driver, pagingCount, listCount, categoryVo);
                			list.add(cafeVo);
                			driver.close();
							driver.switchTo().window(tabs.get(0));
							
							if(listCount > 10 ) {
								break;
							}
            			}
        			}
                }
            	
        		
        	}else {
        		debug("[info][webCrawler][CrawlerCafe][ no searchList count]");
        	}
        	
        	
        	
        	
        }catch (Exception e) {
        	error("[CrawlerCafe][Exception e:"+e+"]");
		}finally {
			//
			driver.close();
	        driver.quit();
		}
        
        debug("[info][webCrawler][CrawlerNaver.crawlerCafe][crawlerCafe count:"+list.size()+"]");
		
        InsertCafe(list);
        
        
        
        debug("[info][webCrawler][CrawlerNaver.crawlerCafe][end]");
		return true;
	}
	
	protected CafeVO  getCafeMain(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo) {
		
		CafeVO cafeVo = new CafeVO();
		
		// 클로링 횟수
		int webCrawlerCount = 0;
		//
		int viewListCount = listCount+1;
		boolean webCrawlerBoolean = true;
		
		
		while(webCrawlerBoolean) {
			if(webCrawlerCount >  WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT) {
				break;
			}
			log("[카폐 순서:"+viewListCount+"][webCrawlerCount:"+webCrawlerCount+"]");
			try {
				// 수집 하는 시간 표시 :네이버(수집시 X시간 전 )
				cafeVo.setSearchNum(categoryVo.getSearchNum());
				cafeVo.setMainCategory(categoryVo.getMainCategory());
				cafeVo.setSubCategory(categoryVo.getSubCategory());
				
				// 현제 시간
				String insertDate = DateUtil.getCurrSysTime();
				cafeVo.setInsertDate(insertDate); 
				
				debug("[카폐 순서:"+viewListCount+"][cafeCrawlerDate:"+insertDate+"]");
				
				String crawlerDate = "";
				crawlerDate = insertDate.replace("-", "");
				crawlerDate = crawlerDate.replace(":", "");
				crawlerDate = crawlerDate.replace(" ", "");
				
				cafeVo.setCrawlerDate(crawlerDate);
				cafeVo.setKeyword(keyword);
				cafeVo.setKeywordNum(keywordId);
				
				// 카폐 URL 주소 ( 네이버 기준 )
	    		String cafeUrl = driver.getCurrentUrl();
	    		
	    		// DB PK 작업
    			// 카페 URL 
    			String docid = EncryptUtil.MD5(cafeUrl);
    			cafeVo.setDocid(docid);
    			debug("[카폐 순서:"+viewListCount+"][docid:"+docid+"]");
				
    			
	    		cafeVo.setCafeUrl(cafeUrl);
	    		debug("[카폐 순서:"+viewListCount+"][cafeUrl:"+cafeUrl+"]");
				
				// 카페 이름
				//String cafeName =  driver.findElement(By.xpath("/html/body/h1[@class='d-none']")).getText();
				//String cafeName =  driver.findElement(By.xpath("//*[@class='footer']/.//@[@class='cafe_name']")).getText();
	    		
	    		List<WebElement> cafeNameList =  driver.findElements(By.xpath(categoryVo.getNameCode()));
	    		if(cafeNameList.size()>1) {
		    		String cafeName =  driver.findElement(By.xpath(categoryVo.getNameCode())).getText();
					cafeVo.setCafeName(StringUtil.replaceAllWebCrawler(cafeName));
					debug("[카폐 순서:"+viewListCount+"][cafeName:"+cafeName+"]");
	    		}
				
				// 해당 내용이  iframe 안에 있어서 다시 선택
				driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='cafe_main']")));
				//log("driver:"+driver.findElement(By.xpath("//*")).getText());
				
				// 해당 내용이 있을때까지 대기
				//Thread.sleep(1000);
				WebCrawlerCommon.witeLoadVisibilityOfElementLocated(driver, WebCrawlerCommon.timeOutInSeconds05 ,  By.xpath("//*[@id='app']")  );
				List<WebElement> cafeTitleList =  driver.findElements(By.xpath(categoryVo.getTitleCode()));
				if(cafeTitleList.size()>0) {
					String cafeTitle = driver.findElement(By.xpath(categoryVo.getTitleCode())).getText();
	    			cafeVo.setCafeTitle(StringUtil.replaceAllWebCrawler(cafeTitle));
	    			debug("[카폐 순서:"+viewListCount+"][cafeTitle:"+cafeTitle+"]");
				}
    			
    			// 카페 작성자 
				List<WebElement> cafeWriterList =  driver.findElements(By.xpath(categoryVo.getWriterCode()));
				if(cafeWriterList.size()>0) {
	    			String cafeWriter = driver.findElement(By.xpath(categoryVo.getWriterCode())).getText();
	    			cafeVo.setCafeWriter(StringUtil.replaceAllWebCrawler(cafeWriter));
	    			debug("[카폐 순서:"+viewListCount+"][cafeWriter:"+cafeWriter+"]");
				}
    			
    			// 카페 작성시간
				List<WebElement> cafeInputList =  driver.findElements(By.xpath(categoryVo.getInputDateCode()));
				if(cafeInputList.size()>0) {
	    			String cafeInput = driver.findElement(By.xpath(categoryVo.getInputDateCode())).getText();
	    			cafeVo.setCafeInput(cafeInput);
	    			debug("[카폐 순서:"+viewListCount+"][cafeInput:"+cafeInput+"]");
	    			
	    			String dataregDate = "";
	    			if(cafeInput.length() > 0) {
	    				dataregDate = DateUtil.getDate14(cafeInput);
	    			}
	    			cafeVo.setDataregDate(dataregDate);
				}
    			
    			// 카폐 내용
    			String cafeContents = driver.findElement(By.xpath(categoryVo.getContentCode())).getText();
    			cafeVo.setCafeContents(StringUtil.replaceAllWebCrawler(cafeContents));
    			debug("[카폐 순서:"+viewListCount+"][cafeContents:"+cafeContents+"]");
    			// 네이버 까페는 추천해요 대신 좋아요 / 다음은 추천해요 (다음은 스크랩 수 카운트 / 네이버는 스크랩 수는 공유라고 있지만 카운트 표시는 없음 - 없는경우 0으로 기본 셋팅)
    			String cafeRecommand = driver.findElement(By.xpath(categoryVo.getRecommend())).getText();
    			String cafeScrap = "0";
    			cafeVo.setCafeRecommand(cafeRecommand);
    			cafeVo.setCafeScrap(cafeScrap);
    			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    			
    			
    			
    			ArrayList<CafeCommentVO> cafeCommentList = getCafeComment(driver, pagingCount, viewListCount, categoryVo, docid);
    			debug("[카폐 순서:"+viewListCount+"][댓글 갯수:"+cafeCommentList.size()+"]");
    			cafeVo.setCafeCommentList(cafeCommentList);
    			
    			
    			
				webCrawlerBoolean = false;
				//&&  webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT
			}catch(Exception e) {
				WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
				error("[CrawlerCafe][getMain][Exception e:"+e+"]");
				webCrawlerCount++;
				
				
			}
			
			
		}
		
		return cafeVo;
	}
	
	/** 카페 댓글  */
	protected ArrayList<CafeCommentVO> getCafeComment(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo , String mainKey){
		ArrayList<CafeCommentVO> cafeCommentList = new ArrayList<CafeCommentVO>();
		
		List<WebElement> CommentBoxWebElementList = driver.findElements(By.xpath("//*[@class='CommentBox']"));
		log("[카폐 순서:"+listCount+"][CommentBoxWebElementList:"+CommentBoxWebElementList.size()+"]");
		
		// 클로링 횟수
		int webCrawlerCount = 0;
		//
		int viewListCount = listCount+1;
				
				
		Boolean cafeCommentBl = false;
		if(CommentBoxWebElementList.size() > 0) {
			cafeCommentBl = true;
		}
		
		
		if(cafeCommentBl) {
			// 댓글 대기
			WebCrawlerCommon.witeLoadVisibilityOfElementLocated(driver,WebCrawlerCommon.timeOutInSeconds_int_10 ,  By.xpath("//*[@class='CommentBox']/.//*[contains(@class,'comment_list')]") );
			// 댓글
			List<WebElement> catefCommontList = driver.findElements(By.xpath(categoryVo.getCommentList()));
			log("[카폐 순서:"+listCount+"][catefCommontList 갯수:"+catefCommontList.size()+"]");
			if(catefCommontList.size() > 0) {
				for(int catefCommontCount = 0 ; catefCommontCount < catefCommontList.size(); catefCommontCount++ ) {
					log("[카폐 순서:"+listCount+"][catefCommontList 카운트:"+catefCommontCount+"]");
					WebElement  catefCommont = catefCommontList.get(catefCommontCount);
					//log(catefCommont.getText());
					String cafeCommentWriter = "";
					// 내용
					String cafeCommentContents = "";
					// 작성자
					String cafeCommentDate = "";
					// 댓글 관련
					String cafeCommentRe = "1"; // 메인
					
					
					// 댓글에 메인 찾기
					String catefCommontClass  = catefCommont.getAttribute("class");
					if(catefCommontClass.indexOf("CommentItem--reply") != -1) {
						cafeCommentRe = "2";
					}
					
							
					// 삭제 여부 확인
					List<WebElement> commentDeleted = catefCommont.findElements(By.xpath(".//*[@class='comment_deleted']"));
					
					//log("commentDeleted.size() : "+commentDeleted.size());
					if(commentDeleted.size()  > 0) { // 삭제 된것
						continue;
						//cafeCommentContents = catefCommont.findElement(By.xpath(".//*[@class='comment_deleted']")).getText();
					}else {
						cafeCommentWriter = catefCommont.findElement(By.xpath(categoryVo.getCommentWriter())).getText();
						cafeCommentContents = catefCommont.findElement(By.xpath(categoryVo.getCommentContents())).getText();
    					cafeCommentDate = catefCommont.findElement(By.xpath(categoryVo.getCommentDate())).getText();
    					
					}
					CafeCommentVO cafeCommentVo = new CafeCommentVO();
					cafeCommentVo.setCafeCommentContents(StringUtil.replaceAllWebCrawler(cafeCommentContents));
					cafeCommentVo.setCafeCommentDate(cafeCommentDate);
					cafeCommentVo.setCafeCommentWriter(StringUtil.replaceAllWebCrawler(cafeCommentWriter));
					cafeCommentVo.setCafeCommentRe(cafeCommentRe);
					cafeCommentVo.setKeyword(keyword);
					cafeCommentVo.setKeywordNum(keywordId);
					cafeCommentVo.setMainCategory(categoryVo.getMainCategory());
					cafeCommentVo.setSubCategory(categoryVo.getSubCategory());
					
					
					//
					String insertDate = DateUtil.getCurrSysTime();
					cafeCommentVo.setInsertDate(insertDate);
					cafeCommentVo.setCrawlerDate(DateUtil.getDate14(insertDate) );
					cafeCommentVo.setDataregDate(DateUtil.getDate14(cafeCommentDate));
					
					// 카페 원천 Key
					cafeCommentVo.setMainKey(mainKey);
					// 
					String docid = EncryptUtil.MD5(cafeCommentWriter+"-"+cafeCommentDate+"-"+StringUtil.replaceAllWebCrawler(cafeCommentContents));
					cafeCommentVo.setDocid(docid);
					
					cafeCommentList.add(cafeCommentVo);
					
					debug("[카폐 순서:"+listCount+"][catefCommontCount:"+(catefCommontCount+1)+"][cafeCommentWriter :"+cafeCommentWriter+"]");
					debug("[카폐 순서:"+listCount+"][catefCommontCount:"+(catefCommontCount+1)+"][cafeCommentContents :"+cafeCommentContents+"]");
					debug("[카폐 순서:"+listCount+"][catefCommontCount:"+(catefCommontCount+1)+"][cafeCommentDate :"+cafeCommentDate+"]");
				}
			}
		}
		
		return cafeCommentList;
		
	}
	
	protected void InsertCafe(ArrayList<CafeVO> list ) {
		
		SqlSession session = SqlMapClient.getSqlSession();
		
		// 뉴스 메인
		try {
			for(CafeVO cafeVo : list) {
				
				int count  = session.selectOne("naver.CrawlerCafeSelectCount", cafeVo); 
				//debug("[CrawllerNaver.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][count:"+count+"]");
				
				// 메인 데이터
				// 같은 건이 있을때 건너뒨다.
				if(count > 0) {
					debug("[CrawllerNaver.InsertNews][naver.CrawlerCafeSelectCount][docid:"+cafeVo.getDocid()+"][update]"); 
					session.update("naver.CrawlerCafeUpdate", cafeVo);
				}else {
					debug("[CrawllerNaver.InsertNews][naver.CrawlerCafeSelectCount][docid:"+cafeVo.getDocid()+"][insert]"); 
					session.insert("naver.CrawlerCafeInsert", cafeVo);
				}
				
				// 댓글 데이터
				ArrayList<CafeCommentVO> commentList =  cafeVo.getCafeCommentList();
				for(CafeCommentVO commentVo : commentList ) {
					int commentVoCount  = session.selectOne("naver.CrawlerCafecommentSelectCount", commentVo);
					
					if(commentVoCount > 0 ) {
						debug("[CrawllerNaver.InsertNews][naver.CrawlerCafecommentSelectCount][docid:"+commentVo.getDocid()+"][no insert]");
					}else {
						debug("[CrawllerNaver.InsertNews][naver.CrawlerCafecommentSelectCount][docid:"+commentVo.getDocid()+"][insert]");
						session.insert("naver.CrawlerCafecommentInsert", commentVo);
					}
				}
			}
			
			session.commit();

		} catch (Exception e) {
			log("[CrawlerNaver.InsertCafe error: " + e+"]");
			error("[CrawlerNaver.InsertCafe error: " + e+"]");
			
			//e.printStackTrace();
			//log("e : " + e);
			session.rollback();
		}
	}

	
	/** 블로그 */
	protected void  crawlerBlog() {
		log("[info][webCrawler][CrawlerNaver.crawlerBlog][start]");
		// 크롬 생성
        WebDriver driver = new ChromeDriver(chromeOptions);
        
        // 추후 DB에서 가져온다.
        String baseUrl = categoryVo.getBaseUrl();
        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
        String startDate= "";
        String endDate= "";
        String dateOption = categoryVo.getOptionFnc();
        String fullUrl = "";

        // 네이버 블로그만 나오게 세팅
        String postBlogurl = "post_blogurl=blog.naver.com";
        /*
        startDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
    	endDate = DateUtil.convertDateFormat(day, "yyyyMMdd", "yyyy.MM.dd");
    	*/
        startDate = day;
    	endDate = day;
    	String nso="nso=so%3Add%2Ca%3Aall%2Cp%3Afrom"+startDate+"to"+endDate;
    	fullUrl = baseUrl+sortVal+"&query="+keyword+"&"+nso+"&"+postBlogurl;
    	
        // 결과 담기
        ArrayList<BlogVO> list = new ArrayList<BlogVO>();
        
        //요청 URL 
        driver.get(fullUrl);
        //driver.manage().wait(timeOutInSeconds03);
        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
        
        debug("[fullUrl:"+fullUrl+"]");
        // 수집 여부
        boolean webCrawlerBl = true;
        // 네이버 스크롤 방식
        String naverListAjax  = "";
        int pagingCount = 0 ;
        try {
        	
        	// 검색 결과 테그값 
        	debug("categoryVo.getResultListCode()):"+categoryVo.getResultListCode());
        	List<WebElement>  searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()));
        	
        	// 검색결과 테그가 여부값
        	int searchListSize = searchList.size();
        	if(searchListSize > 0) {
        		searchList = driver.findElements(By.xpath(categoryVo.getResultListCode() + "/child::*"));
        		
        		while(webCrawlerBl) {
                	pagingCount++;
            		
        			// 네이버
        			List<WebElement>  reviewLoadingList = driver.findElements(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
        			if(reviewLoadingList.size() > 0 ) {
        				String thisListAjax = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]")).getAttribute("data-api");
            			//oldUrl  = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]")).getAttribute("data-api");
            			//WebElement WebElementTest = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
            			debug("[pagingCount:"+pagingCount+"]thisListAjax : "+ thisListAjax);
            			debug("[pagingCount:"+pagingCount+"]naverListAjax : "+ naverListAjax);
            			
            			
            			//log("[reviewCount:"+reviewCount+"][data-api:"+WebElementTest.getAttribute("data-api")+"]");
                		JavascriptExecutor js = (JavascriptExecutor) driver;
                		WebElement element = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
                		// This will scroll the page till the element is found
                		js.executeScript("arguments[0].scrollIntoView(true);", element);
                		
                		WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04); 
                		
                		// 다시 있는지 확인
                		List<WebElement>  naverListAjaxreviewLoadingList = driver.findElements(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]"));
                		debug("[pagingCount:"+pagingCount+"][naverListAjaxreviewLoadingList.size:"+naverListAjaxreviewLoadingList.size()+"]");
                		if(naverListAjaxreviewLoadingList.size() > 0) {
                			naverListAjax = driver.findElement(By.xpath("//*[@id='main_pack']/.//*[contains(@class,'review_loading')]")).getAttribute("data-api");
                			debug("[pagingCount:"+pagingCount+"][naverListAjax 1 :"+naverListAjax+"]");
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
        			// 수집이 끝났다면
        			if(!webCrawlerBl) {
        				searchList = driver.findElements(By.xpath(categoryVo.getResultListCode()+"/child::*"));
        				
        				debug("[info][webCrawler][searchList count :"+searchList.size()+"]");
        				
        				// 메인 데이터 수집
            			for(int listCount = 0 ; listCount < searchList.size() ; listCount++ ) {
            				WebElement oneRow = searchList.get(listCount);
            				
            				BlogVO  blogVo = new BlogVO();
            				
            				log("블로그 순서 : " + (listCount+1));
            				oneRow.findElement(By.xpath("./*[contains(@class,'total_wrap')]/a")).sendKeys(Keys.ENTER);
            				// 해당 디바이스에 텝을 가져온다.
                			List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                			// 2번쨰로 전환
                			driver.switchTo().window(tabs.get(1));
                			// 실제 수집
                			blogVo = getBlogMain(driver, pagingCount, listCount, categoryVo);
                			list.add(blogVo);
                			driver.close();
							driver.switchTo().window(tabs.get(0));
            			}
        			}
                }
            	
        		
        	}else {
        		log("[info][webCrawler][crawlerBlog][ no searchList count]");
        	}
        	
        	
        	
        	
        }catch (Exception e) {
        	error("[CrawlerCafe][Exception e:"+e+"]");
		}finally {
			//
			driver.close();
	        driver.quit();
		}
        
        log("[info][webCrawler][CrawlerNaver.crawlerBlog][crawlerCafe count:"+list.size()+"]");
        
		
        InsertBlog(list);
        
		log("[info][webCrawler][CrawlerNaver.crawlerBlog][end]");
	}
	protected BlogVO  getBlogMain(WebDriver driver ,int  pagingCount , int listCount, CategoryVO categoryVo) {
		
		BlogVO  blogVo = new BlogVO();
		
		// 클로링 횟수
		int webCrawlerCount = 0;
		//
		int viewListCount = listCount+1;
		boolean webCrawlerBoolean = true;
		
		
		while(webCrawlerBoolean) {
			if(webCrawlerCount >  WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT) {
				break;
			}
			log("[블로그 순서:"+viewListCount+"][webCrawlerCount:"+webCrawlerCount+"]");
			try {
				
				WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds01_3);
				//WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds05);
    			
				// 수집 하는 시간 표시 :네이버(수집시 X시간 전 )
    			String  blogCrawlerDate = DateUtil.getCurrSysTime();
    			blogVo.setBlogCrawlerDate(blogCrawlerDate);
    			debug("[블로그 순서:"+viewListCount+"][blogCrawlerDate:"+blogCrawlerDate+"]");
    			
    			String crawlerDate = "";
    			crawlerDate = blogCrawlerDate.replace("-", "");
    			crawlerDate = crawlerDate.replace(":", "");
    			crawlerDate = crawlerDate.replace(" ", "");
				
				blogVo.setCrawlerDate(crawlerDate);
				debug("[블로그 순서:"+viewListCount+"][crawlerDate:"+crawlerDate+"]");
    			blogVo.setMainCategory(categoryVo.getMainCategory());
    			blogVo.setSubCategory(categoryVo.getSubCategory());
    			blogVo.setKeyword(keyword);
    			blogVo.setKeywordNum(keywordId);
    			
    			// 블로그 URL 주소 ( 네이버 기준 )
    			String blogUrl = driver.getCurrentUrl();
    			blogVo.setBlogUrl(blogUrl);
    			debug("[블로그 순서:"+viewListCount+"][blogUrl:"+blogUrl+"]");
    			
    			//
    			String docid = EncryptUtil.MD5(blogUrl);
    			blogVo.setDocid(docid);
    			debug("[블로그 순서:"+viewListCount+"][docid:"+docid+"]");
    			
    			//
    			blogVo.setSearchNum(categoryVo.getSearchNum());
    			
    			// 해당 내용이  iframe 안에 있어서 다시 선택
    			driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='mainFrame']")));
    			//Thread.sleep(1000);
    			// 명시적 대기
    			//WebCrawlerCommon.witeLoadElementIsVisible(driver, WebCrawlerCommon.timeOutInSeconds02 , By.xpath("//*[@id='head-skin']")); 
    		
    			
    			
    			// 블로그 이름
    			List<WebElement> blogNameList =  driver.findElements(By.xpath(categoryVo.getNameCode()));
    			if(blogNameList.size()>0) {
        			String blogName = driver.findElement(By.xpath(categoryVo.getNameCode())).getText();
        			blogVo.setBlogName(StringUtil.replaceAllWebCrawler(blogName));
        			debug("[블로그 순서:"+viewListCount+"][blogName:"+blogName+"]");
    			}
    					
    					
    			// 블로그 제목
    			List<WebElement> blogTitleList =  driver.findElements(By.xpath(categoryVo.getTitleCode()));
    			if(blogTitleList.size()>=1) {
        			String blogTitle = driver.findElement(By.xpath(categoryVo.getTitleCode())).getText();
        			blogVo.setBlogTitle(StringUtil.replaceAllWebCrawler(blogTitle));
        			//blogVo.setBlogTitle(blogTitle.replaceAll("\"", "\\\"").replaceAll("\'", "\\\'") );
        			debug("[블로그 순서:"+viewListCount+"][blogTitle:"+blogTitle+"]");
    			}
    			
    			// 블로그 내용
    			String blogContentsXpath = "";
    			blogContentsXpath  = blogContentsXpath + categoryVo.getContentCode();
    			String blogContents = driver.findElement(By.xpath(blogContentsXpath)).getText();
    			//blogVo.setBlogContents(  blogContents.replaceAll("\"", "").replaceAll("\'", ""));
    			blogVo.setBlogContents(StringUtil.replaceAllWebCrawler(blogContents));
    			
    			debug("[블로그 순서:"+viewListCount+"][blogContents:"+blogContents+"]");
    			
    			// 등록 시간
    			String blogInputXpath = "";
    			blogInputXpath = blogInputXpath + categoryVo.getInputDateCode();
    			String blogInput = driver.findElement(By.xpath(blogInputXpath)).getText();
    			blogVo.setBlogInput(blogInput);
    			
    			String dataregDate = "";
    			if(blogInput.length() > 0) {
    				dataregDate = DateUtil.getDate14(blogInput);
    			}
    			
    			blogVo.setDataregDate(dataregDate);
    			
    			webCrawlerBoolean = false;
			}catch(Exception e) {
				error("[CrawlerNaver][getBlogMain][Exception e:"+e+"]");
				WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
				
				webCrawlerCount++;
				
				
			}
		}
		
		return blogVo;
	}
	
	protected void InsertBlog(ArrayList<BlogVO> list ) {
		
		SqlSession session = SqlMapClient.getSqlSession();
		
		// 블로그
		try {
			for(BlogVO blogVo : list) {
				
				int count  = session.selectOne("naver.CrawlerBlogSelectCount", blogVo); 
				//debug("[CrawllerNaver.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][count:"+count+"]");
				
				// 메인 데이터
				// 같은 건이 있을때 건너뒨다.
				if(count > 0) {
					debug("[CrawllerNaver Blog main no insert][no insert][docid:"+blogVo.getDocid()+"][duplicate]"); 
					//session.update("naver.CrawlerBlogUpdate", blogVo);
				}else {
					debug("[CrawllerNaver.InsertBlog][naver.CrawlerBlogInsert][docid:"+blogVo.getDocid()+"][insert]"); 
					session.insert("naver.CrawlerBlogInsert", blogVo);
				}	
			}
			
			session.commit();

		} catch (Exception e) {
			log("[CrawlerNaver.InsertBlog error: " + e+"]");
			error("[CrawlerNaver.InsertBlog error: " + e+"]");
			session.rollback();
		}
	}
}
