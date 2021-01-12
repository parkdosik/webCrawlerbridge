package kr.co.wisenut.webCrawler.site;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.chrome.ChromeOptions;

import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.properties.PropertiesVO;
import kr.co.wisenut.webCrawler.vo.CategoryVO;

public class Crawler  {
	
	protected PropertiesVO propertiesVo ;
	protected String keyword ;
	protected String keywordId ;
	protected CategoryVO categoryVo;
	protected String day;
	
	protected ChromeOptions chromeOptions;
	
	public Crawler(PropertiesVO  propertiesVo , String keywordId, String keyword, CategoryVO categoryVo, String day) {
		//
		this.propertiesVo = propertiesVo;
		this.keywordId = keywordId;
		this.keyword = keyword;
		this.categoryVo = categoryVo;
		this.day = day;
		
		//
		Path path = Paths.get(propertiesVo.getChromedriver());
		log("[info][webCrawler][Crawler][chromedriver path :"+propertiesVo.getChromedriver()+"]");
		
		// WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", path.toString());
		
		/// WebDriver 옵션 설정
        chromeOptions = new ChromeOptions();
        
        if ( !System.getProperty("os.name").startsWith("Windows") ) {
        	chromeOptions.addArguments("--headless");     // 크롬창이 열리지 않음
        	chromeOptions.addArguments("--single-process");     //  에러 떠서 추가 unknown error: DevToolsActivePort file doesn't exist 
            chromeOptions.addArguments("--disable-dev-shm-usage");     //  에러 떠서 추가 unknown error: DevToolsActivePort file doesn't exist
            chromeOptions.addArguments( "--no-sandbox" ) ;  // GUI를 사용할 수 없는 환경에서 설정, linux, docker 등
            chromeOptions.addArguments( "--disable-gpu" ) ; // GUI를 사용할 수 없는 환경에서 설정, linux, docker 등
            chromeOptions.addArguments( "--privileged" ) ;  // 
            
             
        }else{
        	//chromeOptions.addArguments("--headless");     // 크롬창이 열리지 않음
        	chromeOptions.addArguments("--start-maximized");            // 전체화면으로 실행
            chromeOptions.addArguments("--disable-popup-blocking");    // 팝업 무시
            chromeOptions.addArguments("--disable-default-apps");     // 기본앱 사용안함
        }
        
        // 원도우 수집시 
        
        // 리뉵스 수집시 
       
	}
	
	
	
	

	public void setChromeOptions(ChromeOptions chromeOptions) {
		this.chromeOptions = chromeOptions;
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
