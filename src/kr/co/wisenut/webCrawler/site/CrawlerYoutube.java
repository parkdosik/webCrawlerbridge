package kr.co.wisenut.webCrawler.site;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import kr.co.wisenut.MyBatis.SqlMapClient;
import kr.co.wisenut.common.properties.PropertiesVO;
import kr.co.wisenut.common.util.DateUtil;
import kr.co.wisenut.common.util.EncryptUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.webCrawler.common.WebCrawlerCommon;
import kr.co.wisenut.webCrawler.vo.BlogVO;
import kr.co.wisenut.webCrawler.vo.CategoryVO;
import kr.co.wisenut.webCrawler.vo.NewsCommentVO;
import kr.co.wisenut.webCrawler.vo.NewsVO;
import kr.co.wisenut.webCrawler.vo.YoutubeVO;
import kr.co.wisenut.webCrawler.vo.YoutubeCommentVO;

public class CrawlerYoutube extends Crawler  {

	public CrawlerYoutube(PropertiesVO propertiesVo, String keywordId, String keyword, CategoryVO categoryVo, String day) {
		super(propertiesVo, keywordId, keyword, categoryVo, day);
	}
	
	public void crawler() {
		crawlerYoutube();
		log("[info][webCrawler][CrawlerYoutube][end]");
	}
	
	/** 유튜브 */
	protected void  crawlerYoutube() {
		log("[info][webCrawler][crawlerYoutube][start]");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date(); 
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);

	    
		// 크롬 생성
        WebDriver driver = new ChromeDriver(chromeOptions);
        Actions action = new Actions(driver); //마우스 움직임 
        
        WebDriverWait driverWait;
        
        // 유튜브 영상 리스트
        ArrayList<YoutubeVO> youtubeList = new ArrayList<YoutubeVO>();

        
        // 추후 DB에서 가져온다.
        String baseUrl = categoryVo.getBaseUrl();
        String sortVal = categoryVo.getSortOption();		// 1 : 최신순 , 0 : 정확도
        String startDate= "";
        String endDate= "";
        
        String dateOption = categoryVo.getOptionFnc();
        
        
        // 기간 설정 (업로드 날짜 기준으로 하는 이유는 실시간 스트리밍을 제외하기 위함)
        //String sp = "CAISBAgBEAE%253D";    //지난 1시간 
        //String sp = "CAISBAgCEAE%253D";      //오늘(업로드날짜기준) 정렬
        String sp = "CAISBAgDEAE%253D";      //이번주
        //String sp = "CAISBAgEEAE%253D";    //이번달 
        //String sp = "CAISBAgFEAE%253D";    //올해
        
        // 요청 URL 
        String fullUrl = baseUrl+keyword+"&sp="+sp;
        try {
        	StringUtil sUtil = new StringUtil();
        	String insertDate = DateUtil.getCurrSysTime();
        	
        	log("[fullUrl:"+fullUrl+"]");
            // 요청
            driver.get(fullUrl);
            
         // 수집 여부 체크  list_info mg_cont clear
            boolean webYoutubeCrawlerCf = true;
            int CountYoutube = 0;
            CountYoutube 	 = driver.findElements(By.xpath(categoryVo.getResultListCode()+"/child::*")).size();
            
            while(CountYoutube > 0  && webYoutubeCrawlerCf) {

            	
            	Thread.sleep(1000);
            	
                WebElement  WebElement_ul = driver.findElement(By.xpath(categoryVo.getResultListCode()));
                
                if(WebElement_ul.getTagName().equals("div")) {
                	List<WebElement>  WebElement_li_list = WebElement_ul.findElements(By.xpath("child::*"));
	            	for(int i  = 0  ; i < WebElement_li_list.size()  ; i++ ) {
	            		
	            		// 유튜브
	            		YoutubeVO youtubeVo = new YoutubeVO();
                		
	            		long start2 = System.currentTimeMillis();
                		log("------------------------------------------");
                		WebElement webElement_li = WebElement_li_list.get(i);
                		
                		
                		if(!webElement_li.getTagName().equals("ytd-search-pyv-renderer")) {
                			//영상제목 클릭 
                    		WebElement a_webElementYoutube = webElement_li.findElement(By.xpath(categoryVo.getTitleLinkCode()));
                    		((RemoteWebDriver) driver).executeScript("window.open(arguments[0])", a_webElementYoutube);
                    		
                    		Thread.sleep(1000);	                		

                    		// 해당 디바이스에 텝을 가져온다.
                			List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                			
                			// 2번쨰로 전환
                			driver.switchTo().window(tabs.get(1));
                			
                			youtubeVo.setKeyword(keyword);
                			youtubeVo.setKeywordNum(keywordId);
                			youtubeVo.setMainCategory(categoryVo.getMainCategory());
                			youtubeVo.setSubCategory(categoryVo.getSubCategory());
                			
                			// 유튜브 URL 주소 ( 유튜브 기준 )
                			String youtubearticleUrl = driver.getCurrentUrl();
                			youtubeVo.setYoutubearticleUrl(youtubearticleUrl);

                			//제목
                			String youtubearticleTitle = driver.findElement(By.xpath(categoryVo.getTitleCode())).getText();
                			debug("youtubearticleTitle : " + youtubearticleTitle);
                			youtubeVo.setYoutubearticleTitle(sUtil.replaceAllWebCrawler(youtubearticleTitle));

                			//조회수
                			int readNumCnt = driver.findElement(By.xpath(categoryVo.getReadnum())).getText().split(" ").length;
                			if(readNumCnt>2) { //실시간 스트리밍의 경우 조회수 표시가 없다 
                    			String youtubeReadnum = driver.findElement(By.xpath(categoryVo.getReadnum())).getText().split(" ")[1].split("회")[0];
                    			debug("youtubeReadnum : " + youtubeReadnum);  
                    			youtubeVo.setYoutubeReadnum(youtubeReadnum);
                			}else {
                				String youtubeReadnum = "실시간 스트리밍"; 
                				debug("youtubeReadnum : " + youtubeReadnum);
                				youtubeVo.setYoutubeReadnum(youtubeReadnum);
                			}
                			
                			//등록일자
                			String youtubeuploadDate = driver.findElement(By.xpath(categoryVo.getInputDateCode())).getText();
 
                			if(youtubeuploadDate.split(":").length==1) {
                    			youtubeVo.setYoutubeuploadDate(youtubeuploadDate);
                    			youtubeVo.setYoutubeDataRegdate(DateUtil.getDate14(youtubeuploadDate));
                    			youtubeVo.setYoutubeinsertDate(insertDate);
                    			youtubeVo.setYoutubecrawlerdate(DateUtil.getDate14(insertDate));
                			}
                			
                			//추천합니다
                			String youtubeRecommand = driver.findElement(By.xpath(categoryVo.getRecommend())).getText();
                			debug("youtubeRecommand : " + youtubeRecommand);
                			youtubeVo.setYoutubeRecommand(youtubeRecommand);

                			//비추천 
                			String youtubeNtRecommand = driver.findElement(By.xpath(categoryVo.getNotrecomment())).getText();
                			debug("youtubeNtRecommand : " + youtubeNtRecommand);
                			youtubeVo.setYoutubeNtRecommand(youtubeNtRecommand);
                			
                			//채널명 
                			String youtubeWriter = driver.findElement(By.xpath(categoryVo.getNameCode())).getText();
                			debug("youtubeWriter : " + youtubeWriter);
                			youtubeVo.setYoutubeWriter(sUtil.replaceAllWebCrawler(youtubeWriter));
                			
                			//구독자수  
                			String youtubeSubscribe = "";
                			int SubscribeCnt = driver.findElement(By.xpath(categoryVo.getSubscribe())).getText().length();
                			if(SubscribeCnt > 4) {
                				youtubeSubscribe = driver.findElement(By.xpath(categoryVo.getSubscribe())).getText().split(" ")[1];
                			}
                			youtubeVo.setYoutubeSubscribe(youtubeSubscribe);
                			
                			
                			
                			// 채널 영상 설명 class 확인
                			List<WebElement> CommentChannel = driver.findElements(By.xpath(categoryVo.getChannelDescClass() + "/child::*"));
                			
                			String youtubeChannelmore = "";
                			
                			if(CommentChannel.size() > 0) {
                				// 해당 채널 소개 더보기 클릭  
                    			WebElement a_commentIntro = driver.findElement(By.xpath(categoryVo.getCommentchannelIntro()));
                    			((RemoteWebDriver) driver).executeScript("arguments[0].click();", a_commentIntro);
                    			Thread.sleep(1000);
                    			
                    			youtubeChannelmore = driver.findElement(By.xpath(categoryVo.getCommentchannelmore())).getText();
                    			debug("youtubeChannelmore : " + youtubeChannelmore);
                    			youtubeVo.setYoutubeChannelmore(sUtil.replaceAllWebCrawler(youtubeChannelmore));
                			}
                			
                			// PK 생성
        					String docid = EncryptUtil.MD5(youtubearticleUrl);
        					youtubeVo.setDocid(docid);
                			
                			List<WebElement> messageCount = driver.findElements(By.xpath("//*[@id='contents']/ytd-message-renderer"));
                			
                			// 댓글 리스트 
                			ArrayList<YoutubeCommentVO> youtubeCommentList = new ArrayList<YoutubeCommentVO>();

                			if(messageCount.size() == 0 && youtubeuploadDate.split(":").length==1) {
                				boolean buttondisplayed = false;
            					buttondisplayed = driver.findElement(By.xpath(categoryVo.getDisplayedbutton())).isDisplayed();
            					
            					while(buttondisplayed) {

            						List<WebElement> buttonCount = driver.findElements(By.xpath(categoryVo.getDisplayedbutton()));
            						debug("buttonCount : " + buttonCount.size());

            						if(buttonCount.size()>0) {
    	        						//마우스 무브
    	                    			action.moveToElement(driver.findElement(By.xpath(categoryVo.getDisplayedbutton()))).click().perform();
    	                    			Thread.sleep(2000);	
            						}else {
            							buttondisplayed = false;
            						}
            					}
            					
            					//댓글 정보 수집 
                    			List<WebElement> CommentBox = driver.findElements(By.xpath(categoryVo.getCommentList()+"/child::*"));
                    			debug("CommentBox : " + CommentBox.size());
                    			
                    			String TimeGubun = "";
                    			int TimeNum;
                    			
                			
                    			for(int forCount = 0 ; forCount < CommentBox.size() ; forCount++ ) {
                    			    cal.setTime(date);
            						WebElement CommentBox_li = CommentBox.get(forCount);
            						String commentWriter = CommentBox_li.findElement(By.xpath(categoryVo.getCommentWriter()+ (forCount+1) +categoryVo.getCommentWriterExt())).getText();
            						String commentDate = CommentBox_li.findElement(By.xpath(categoryVo.getCommentDate()+ (forCount+1) +categoryVo.getCommentDateExt())).getText();
            						
            						
            						
            						if(commentDate.length() == 4) {
            							TimeNum = Integer.parseInt(commentDate.substring(0, 1));
            							TimeGubun = commentDate.substring(1, 2);
            						}else if(commentDate.length() == 5) {
            							TimeNum = Integer.parseInt(commentDate.substring(0, 1));
            							TimeGubun = commentDate.substring(1, 2);
            						}else if(commentDate.length() == 6) {
            							TimeNum = Integer.parseInt(commentDate.substring(0, 2));
            							TimeGubun = commentDate.substring(2, 3);
            						}else if(commentDate.length() == 9) {
            							TimeNum = Integer.parseInt(commentDate.substring(0, 1));
            							TimeGubun = commentDate.substring(1, 2);
            						}else if(commentDate.length() == 10) {
            							TimeNum = Integer.parseInt(commentDate.substring(0, 1));
            							TimeGubun = commentDate.substring(1, 2);
            						}else {
            							TimeNum = Integer.parseInt(commentDate.substring(0, 2));
            							TimeGubun = commentDate.substring(2, 3);
            						}
            						
            						if(TimeGubun.equals("초")) {
            							TimeGubun = "s";
            							cal.add(Calendar.SECOND, -TimeNum);
            						}else if(TimeGubun.equals("분")) {
            							TimeGubun = "m";
            							cal.add(Calendar.MINUTE, -TimeNum);
            						}else if(TimeGubun.equals("시")) {
            							TimeGubun = "h";
            							cal.add(Calendar.HOUR, -TimeNum);
            						}else if(TimeGubun.equals("일")) {
            							TimeGubun = "d";
            							cal.add(Calendar.DATE, -TimeNum);
            						}else if(TimeGubun.equals("월")) {
            							TimeGubun = "M";
            							cal.add(Calendar.MONTH, -TimeNum);
            						}else if(TimeGubun.equals("년")) {
            							TimeGubun = "y";
            							cal.add(Calendar.YEAR, -TimeNum);
            						}
            						SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            						SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyyMMddHHmmss");            								
            								
            						String commentContents = CommentBox_li.findElement(By.xpath(categoryVo.getCommentContents()+ (forCount+1) +categoryVo.getCommentContentsExt())).getText();
            						String youtubeCommentRecommand 		= CommentBox_li.findElement(By.xpath(categoryVo.getCommentRecommand()+ (forCount+1) +categoryVo.getCommentRecommandExt())).getText();
            						String YoutubeNotCommentRecommand 	= CommentBox_li.findElement(By.xpath(categoryVo.getCommentNotRecommand()+ (forCount+1) +categoryVo.getCommentNotRecommandExt())).getText();
            						
            						YoutubeCommentVO youtubeCommentVO = new YoutubeCommentVO();
            						
            						youtubeCommentVO.setCommentDate(format.format(cal.getTime()));
            						youtubeCommentVO.setCommentRegDate(commentDate);
            						youtubeCommentVO.setTimeGubun(TimeGubun);
            						youtubeCommentVO.setTimeNum(TimeNum);
            						youtubeCommentVO.setInsertDate(insertDate);
            						youtubeCommentVO.setCrawlerDate(DateUtil.getDate14(insertDate) );
            						youtubeCommentVO.setRegdate(simpledate.format(cal.getTime()));
            						youtubeCommentVO.setDataregdate(simpledateFormat.format(cal.getTime()));
            						youtubeCommentVO.setCommentWriter(sUtil.replaceAllWebCrawler(commentWriter));
            						youtubeCommentVO.setCommentContents(sUtil.replaceAllWebCrawler(commentContents));
            						youtubeCommentVO.setYoutubeCommentRecommand(youtubeCommentRecommand);
            						youtubeCommentVO.setYoutubeNotCommentRecommand(YoutubeNotCommentRecommand);
            						youtubeCommentVO.setKeyword(keyword);
            						youtubeCommentVO.setKeywordNum(keywordId);
            						youtubeCommentVO.setMainCategory(categoryVo.getMainCategory());
            						youtubeCommentVO.setSubCategory(categoryVo.getSubCategory());
            						youtubeCommentList.add(youtubeCommentVO);	
            						
            						// 유튜브 원천 Key
            						youtubeCommentVO.setMainkey(youtubeVo.getDocid());
                					// 
            						String youtubeCommentdocid = EncryptUtil.MD5(commentWriter+"-"+simpledate.format(cal.getTime())+"-"+StringUtil.replaceAllWebCrawler(commentContents));
            						debug((forCount+1) + "번째 : " + youtubeCommentdocid); 
            						youtubeCommentVO.setDocid(youtubeCommentdocid);
                    			}
                    			
                    			// 댓글 리스트 저장
                    			youtubeVo.setYoutubeCommentList(youtubeCommentList);
                    			
                			}
                			

                			// 2번쨰 텝 종료
                			driver.close();
                			// 1번쨰로 전환
                			driver.switchTo().window(tabs.get(0));
                			
                			long end2 = System.currentTimeMillis();
                    		
                			double div2 = ((double) (end2 - start2) / 1000);
                    		
                    		log((i+1) + "번째 유튜브 수집:   " + div2 + " sec]");
                    		
                    		youtubeList.add(youtubeVo);
                		}
	            	}
                }
                
                webYoutubeCrawlerCf = false;
            }
            
        }catch (Exception e) {
			log("[error]"+e);
		}finally {
			//
			driver.close();
	        driver.quit();
		}    
        
        InsertYoutube(youtubeList);
	}
	
	protected void InsertYoutube(ArrayList<YoutubeVO> list ) {
		
		SqlSession session = SqlMapClient.getSqlSession();
		
		// 유튜브
		try {
			for(YoutubeVO youtubeVO : list) {
				
				int count  = session.selectOne("youtube.CrawlerYoutubeSelectCount", youtubeVO.getDocid()); 
				//debug("[CrawllerDaum.InsertNews][naver.CrawlerNewsSelectCount][docid:"+newsVo.getDocid()+"][count:"+count+"]");
				
				// 메인 데이터
				// 같은 건이 있을때 건너뒨다.
				if(count > 0) {
					debug("[Crawller Youtube main no insert][no insert][docid:"+youtubeVO.getDocid()+"][duplicate]"); 
					session.update("youtube.CrawlerYoutubeUpdate", youtubeVO);
				}else {
					debug("[Crawller.InsertYoutube][youtube.CrawlerYoutubeInsert][docid:"+youtubeVO.getDocid()+"][insert]"); 
					session.insert("youtube.CrawlerYoutubeInsert", youtubeVO);
				}	
				// 댓글 데이터
				ArrayList<YoutubeCommentVO> commentList =  youtubeVO.getYoutubeCommentList();
				for(YoutubeCommentVO commentVo : commentList ) {
					int commentVoCount  = session.selectOne("youtube.CrawlerYoutubecommentSelectCount", commentVo.getDocid());
					
					if(commentVoCount > 0 ) {
						debug("[CrawllerYoutube comment no insert][docid:"+youtubeVO.getDocid()+"][duplicate]");
					}else {
						debug("[CrawllerYoutube.InsertYoutube][youtube.CrawlerYoutubecommentSelectCount][docid:"+commentVo.getDocid()+"][insert]");
						session.insert("youtube.CrawlerYoutubecommentInsert", commentVo);
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
}
