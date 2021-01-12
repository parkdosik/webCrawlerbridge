package kr.co.wisenut.MyBatis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import kr.co.wisenut.common.util.FileUtil;

public class SqlMapClient {
	private static SqlSession _session = null;
    
	public SqlMapClient(String webCrawler_home) {
		try {
			String resourcePath = webCrawler_home+FileUtil.fileseperator+"config"+FileUtil.fileseperator+"mybatis_config.xml";
	        System.out.println("resourcePath:"+resourcePath);
	      
	        
	        FileInputStream fis = new FileInputStream(resourcePath);
	        InputStreamReader isr = new  InputStreamReader(fis) ;
	        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(isr);
	        
	        //SqlSessionFactoryBuilder.b
			//Reader reader = Resources.getResourceAsFile(resource) getResourceAsReader(resource);
	        //SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
	        _session = sqlMapper.openSession();
	     } catch(IOException e) {
	         e.printStackTrace();
	    }
	}
	    
	    public static SqlSession getSqlSession() {
	        return _session;
	    }
}
