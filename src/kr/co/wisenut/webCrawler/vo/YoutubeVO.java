package kr.co.wisenut.webCrawler.vo;

import java.util.ArrayList;

public class YoutubeVO {

	private String docid = "";
	private String keywordNum = "";
	private String keyword = "";
	private String mainCategory = "";
	private String subCategory  = "";
	
	/** 작성자 - 채널명 (유튜브 ) */
	private String youtubeWriter = "";
	
	/** 조회수 (유튜브 ) */
	private String youtubeReadnum = "";

	/** 컨텐츠 주소 (유튜브 ) */
	private String youtubearticleUrl = "";

	/** 영상 업로드 일자 (유튜브 ) */
	private String youtubeuploadDate = "";
	
	/** 컨텐츠 제목 (유튜브 ) */
	private String youtubearticleTitle = "";
	
	/** 추천 (유튜브 ) */
	private String youtubeRecommand = "";
	
	/** 비추천 (유튜브 ) */
	private String youtubeNtRecommand = "";
	
	/** 구독자 수 (유튜브 ) */
	private String youtubeSubscribe = "";
	
	/** 채널 소개 더보기 (유튜브 ) */
	private String youtubeChannelmore = "";
	
	private String youtubeDataRegdate 	= "";
	
	private String youtubeinsertDate 	= "";
	
	private String youtubecrawlerdate 	= "";
	
	/** 댓글 리스트 ( 유튜브 )  */
	private ArrayList<YoutubeCommentVO> youtubeCommentList = new ArrayList<YoutubeCommentVO>();

	
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getKeywordNum() {
		return keywordNum;
	}
	public void setKeywordNum(String keywordNum) {
		this.keywordNum = keywordNum;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMainCategory() {
		return mainCategory;
	}
	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getYoutubeWriter() {
		return youtubeWriter;
	}

	public void setYoutubeWriter(String youtubeWriter) {
		this.youtubeWriter = youtubeWriter;
	}

	public String getYoutubeReadnum() {
		return youtubeReadnum;
	}

	public void setYoutubeReadnum(String youtubeReadnum) {
		this.youtubeReadnum = youtubeReadnum;
	}

	public String getYoutubearticleUrl() {
		return youtubearticleUrl;
	}

	public void setYoutubearticleUrl(String youtubearticleUrl) {
		this.youtubearticleUrl = youtubearticleUrl;
	}

	public String getYoutubeuploadDate() {
		return youtubeuploadDate;
	}

	public void setYoutubeuploadDate(String youtubeuploadDate) {
		this.youtubeuploadDate = youtubeuploadDate;
	}

	public String getYoutubearticleTitle() {
		return youtubearticleTitle;
	}

	public void setYoutubearticleTitle(String youtubearticleTitle) {
		this.youtubearticleTitle = youtubearticleTitle;
	}

	public String getYoutubeRecommand() {
		return youtubeRecommand;
	}

	public void setYoutubeRecommand(String youtubeRecommand) {
		this.youtubeRecommand = youtubeRecommand;
	}

	public String getYoutubeNtRecommand() {
		return youtubeNtRecommand;
	}

	public void setYoutubeNtRecommand(String youtubeNtRecommand) {
		this.youtubeNtRecommand = youtubeNtRecommand;
	}

	public String getYoutubeSubscribe() {
		return youtubeSubscribe;
	}

	public void setYoutubeSubscribe(String youtubeSubscribe) {
		this.youtubeSubscribe = youtubeSubscribe;
	}

	public String getYoutubeChannelmore() {
		return youtubeChannelmore;
	}

	public void setYoutubeChannelmore(String youtubeChannelmore) {
		this.youtubeChannelmore = youtubeChannelmore;
	}

	public String getYoutubeDataRegdate() {
		return youtubeDataRegdate;
	}
	public void setYoutubeDataRegdate(String youtubeDataRegdate) {
		this.youtubeDataRegdate = youtubeDataRegdate;
	}
	public String getYoutubeinsertDate() {
		return youtubeinsertDate;
	}
	public void setYoutubeinsertDate(String youtubeinsertDate) {
		this.youtubeinsertDate = youtubeinsertDate;
	}
	public String getYoutubecrawlerdate() {
		return youtubecrawlerdate;
	}
	public void setYoutubecrawlerdate(String youtubecrawlerdate) {
		this.youtubecrawlerdate = youtubecrawlerdate;
	}
	public ArrayList<YoutubeCommentVO> getYoutubeCommentList() {
		return youtubeCommentList;
	}

	public void setYoutubeCommentList(ArrayList<YoutubeCommentVO> youtubeCommentList) {
		this.youtubeCommentList = youtubeCommentList;
	}
	
}
