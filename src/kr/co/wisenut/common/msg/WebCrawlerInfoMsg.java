package kr.co.wisenut.common.msg;

import kr.co.wisenut.common.util.StringUtil;
/**
 * 
 * 날짜                       : 이름      : 내용
 * 2020.10.26 : 박도식  : 신규 
 * 2020.10.26 : 박도식  : 신규 메시지 생성
 * 
 */
public class WebCrawlerInfoMsg {
	private final static String build = "0001";
	private final static String buildName = "released";
	private final static String copyright = "Copyright 2020 Reserved.";
	
	/**
     * Print scdToDB message
     */
	public static void header() {
        String msgWebCrawler = "webCrawler";
        
        
        String buildTime = getCompileTimeStamp();

        System.out.println(new StringBuffer().append("\n")
                .append(msgWebCrawler).append(" (Build ").append(build)
                .append(" ").append("- ").append(buildName).append("), ")
                .append(buildTime).toString());

        System.out.println(copyright);
    }
	public static void usageWEBCRAWLER_HOME() {
        String usage = "*** Error webcrawler_home ***\n";
        usage += "use -D<name>=<value> option in java\n";
        usage += "usage: java -DwebCrawler_home=<webcrawler_home>";
        System.out.println(usage);
    }
	
	
	public static String getVersion() {
    	return "-bld" + build + " " + buildName + ", " + getCompileTimeStamp();  
    }
	
	public static void usage() {
		String msgClassName = "kr.co.wisenut.webCrawler.webCrawler";
		String usage = "\n";
        usage += "Usage : java -DwebCrawler_home=<WEBCRAWLER_HOME>  "+ msgClassName +"\n";
        usage += StringUtil.padLeft("-conf <file> -keywordId <keywordId> -rulesId <rulesId> -dbYN <Y or N> \n\n", ' ', 80);
        usage += "mode include:\n";
        
        usage += "\n";
        usage += "Options include:\n";
        usage += option("-log <stdout|day>", "");
        usage += option("-debug <1~4>", "Debug Mode Run [default: -debug 2]");
        usage += desc("1 : ERROR 2: WARNING\n");
        usage += desc("3: INFO 4 : DEBUG\n");
        usage += option("-help", "help");
        System.out.println(usage);
	}
	
	public static void ImvalidArg(String arg) {
        System.out.println("*** Invalid Run-time arguments check arguments" +
                " ***\n>> argument name: " + arg);
    }

    public static String option(String oName, String desc){
        String option = StringUtil.padRight("    "+oName, ' ', 20);
        option += desc+"\n";
        return option;
    }

    public static String desc(String desc){
        return StringUtil.padRight("", ' ', 20) + desc;
    }
	
	public static String getCompileTimeStamp( ) {
        return "2017-07-25 09:47:00";
    }
}
