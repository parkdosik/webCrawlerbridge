package kr.co.wisenut.webCrawler.site;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import kr.co.wisenut.common.properties.PropertiesVO;
import kr.co.wisenut.webCrawler.common.WebCrawlerCommon;
import kr.co.wisenut.webCrawler.vo.CategoryVO;
import kr.co.wisenut.webCrawler.vo.LandVO;

public class CrawlerLand  extends Crawler {
	public CrawlerLand(PropertiesVO propertiesVo, String keywordId, String keyword, CategoryVO categoryVo, String day) {
		super(propertiesVo, keywordId, keyword, categoryVo, day);
	}
	
	public void crawler() {
		if(categoryVo.getSubCategory().equals("naver")) {
			crawlerDaumrealty();
		}
		log("[info][webCrawler][CrawlerNaverrealty][end]");
	}
	
	/** 다음 부동산 */
	protected void  crawlerDaumrealty() {
		log("[info][webCrawler][CrawlerNaverrealty][start]");
		// 크롬 생성
        WebDriver driver = new ChromeDriver(chromeOptions);
        
        // 추후 DB에서 가져온다.
        String fullUrl = "";
        String baseUrl = categoryVo.getBaseUrl();
        log("baseUrl : " + baseUrl);
        
        fullUrl = baseUrl;
        
        // 결과 담기
        ArrayList<LandVO> list = new ArrayList<LandVO>();

        
        //요청 URL 
        driver.get(fullUrl);
        //driver.manage().wait(timeOutInSeconds03);
        WebCrawlerCommon.witeLoad02(driver, WebCrawlerCommon.timeOutInSeconds02);
        log("[fullUrl:"+fullUrl+"]");
        
        // 클로링 횟수
     	int webCrawlerCount = 0;
        boolean webCrawlerBoolean = true;
        LandVO mainVo  = new LandVO();
        
        // 수집 여부
        boolean webCrawlerBl = true;
        int pagingCount = 0 ;

        while(webCrawlerBoolean ||  webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT) {
			webCrawlerCount++;
			
			if(webCrawlerCount > WebCrawlerCommon.WEB_CRAWLER_MAX_RUN_COUNT ) {  
				break;
			}
			
	        try {
	        	//검색어 입력 
	        	driver.findElement(By.xpath(categoryVo.getSubscribe())).sendKeys(keyword);
	    		WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
	    		
	        	//검색 버튼 클릭 
				WebElement a_naverLand = driver.findElement(By.xpath(categoryVo.getPortalLinkCode()));
				a_naverLand.click();
				Thread.sleep(500); 
				
				//정보수집 변수명 정의 
				String aptname;  //아파트 이름 
				String apttype;  //평형타입
				String aptfloor; //아파트 층수
				String aptgubun; //매매전세구분
				String aptprice; //아파트 가격
				String aptcontractdate; //아파트 계약일 
				
				aptname = keyword; 
				mainVo.setAptname(aptname);
				debug("더보기");
				//실거래가 조회 
				WebElement a_realLand = driver.findElement(By.xpath("//*[@class='complex_detail_link']/button[2]"));
				a_realLand.click();
				Thread.sleep(500); 
				
				List<WebElement> landrealList =  driver.findElements(By.xpath("//*[@id='area_tab_list']/child::*"));
				log("landrealList : " + landrealList.size());
				
				//평형별 조회 버튼 클릭 
				for(int forCount = 0 ; forCount < landrealList.size() ; forCount++ ) {
					WebElement a_tab = driver.findElement(By.xpath("//*[@id='area_tab_list']/a[@id='tab"+ (forCount+1) +"']"));
					a_tab.click();
					Thread.sleep(500);
					
					int u_cbox_paginate_clickCount = 0;
					boolean buttondisplayed = false;
					buttondisplayed = driver.findElement(By.xpath(categoryVo.getCommentListSort())).isDisplayed();
					
					while(buttondisplayed) {
						if(driver.findElements(By.className("detail_data_more")).size()!=0) {
                 			WebElement a_realestatebtn = driver.findElement(By.xpath(categoryVo.getCommentListSort()));
                     		((RemoteWebDriver) driver).executeScript("arguments[0].click();", a_realestatebtn);
     						u_cbox_paginate_clickCount++;
     						WebCrawlerCommon.waitLoad01(WebCrawlerCommon.timeOutInSeconds04);
     						debug("더보기 클릭수:"+u_cbox_paginate_clickCount);
						}else {
 							buttondisplayed = false;
 						}
					}

				}
				
				
				
				//List<WebElement> landNaverList =  driver.findElements(By.xpath(categoryVo.getDisplayedbutton()));
				
				/*Boolean u_cbox_paginate_display_bl = driver.findElement(By.xpath(categoryVo.getDisplayedbutton())).isDisplayed();
				int u_cbox_paginate_clickCount = 0;
				while(u_cbox_paginate_display_bl) {
					driver.findElement(By.xpath(categoryVo.getDisplayedbutton())).click();
					u_cbox_paginate_clickCount++;
					log("Cnt :" + u_cbox_paginate_clickCount);
				}
				debug("더보기 화면 선택 끝:");
				
				*/
				webCrawlerBoolean = false;
	        }catch (Exception e) {
	        	webCrawlerBoolean = true;
	        	error("[CrawlerNaverrealty][Exception e:"+e+"]");
			}finally {
				//
		        //driver.close();
		        //driver.quit();
			}
        }    
	}
}
