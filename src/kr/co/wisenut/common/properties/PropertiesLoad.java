package kr.co.wisenut.common.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



/**
 *  프로퍼티 로더
 */

public class PropertiesLoad {
	public static PropertiesVO load(String fullPath) {
		Properties properties = new Properties();
        
		PropertiesVO  propertiesVo  = new PropertiesVO();
        try {
        	InputStream reader = new FileInputStream(new File(fullPath));
            properties.load(reader);
            propertiesVo.setChromedriver(properties.getProperty("chromedriver"));
            propertiesVo.setXlsxPath(properties.getProperty("xlsxPath"));
        } catch (IOException e) {
            
            System.out.println("PropertiesLoad error "+ e);
            System.exit(1);
        }
        
        return propertiesVo;
	}
}
