package kr.co.wisenut.webCrawler.job.custom;

import kr.co.wisenut.common.Exception.CustomException;
import kr.co.wisenut.common.util.HtmlUtil;

public class ConvertHtmlEntity implements ICustom {
    public String customData(String str) throws CustomException {
        str = HtmlUtil.convertFormattedTextToPlaintext(str);
        return str;
    }

	@Override
	public String customData(String str, int forNum) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}
}
