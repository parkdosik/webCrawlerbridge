package kr.co.wisenut.webCrawler.job.custom;

import kr.co.wisenut.common.Exception.CustomException;

public class Test implements ICustom {

	public String customData(String str) throws CustomException {
		return str + "111";
	}

	@Override
	public String customData(String str, int forNum) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

}
