package kr.co.wisenut.webCrawler.vo;

import java.util.ArrayList;

public class YoutubeCommentVO {

	/** 원천 유튜브 키 */
	private String mainkey = "";
	private String keywordNum 	= "";
	private String keyword 		= "";
	private String mainCategory = "";
	private String subCategory 	= "";
	
	private String commentRe = "";
	
	/** 댓글 키 */
	private String docid = "";

	/** 코멘트 작성자 (유튜브 ) */
	private String commentWriter = ""; 
	
	/** 코멘트 등록내용 (유튜브 ) */
	private String commentContents = "";
	
	/** 코멘트 등록일자 (유튜브 ) */
	private String commentDate = "";
	
	/** 코멘트 등록일자-원표시 (유튜브 ) */
	private String commentRegDate = "";
	
	/** 코멘트 등록일자 구분 (유튜브 ) */
	private String TimeGubun = "";
	
	private int TimeNum;
	
	private String youtubeCommentRecommand = "";
	
	/** 코멘트 비추천 (유튜브 ) */
	private String YoutubeNotCommentRecommand = "";
	/** 코멘트 등록일자 년월일시분초 (유튜브 ) */
	private String regdate = "";
	/** 코멘트 등록일자 년월일시분초 14자리 (유튜브 ) */
	private String dataregdate = "";
	/** 코멘트 수집일자 년월일시분초 (유튜브 ) */
	private String insertDate = "";
	/** 코멘트 수집일자 년월일시분초 14자리 (유튜브 ) */
	private String crawlerDate = "";
	
	public String getMainkey() {
		return mainkey;
	}
	public void setMainkey(String mainkey) {
		this.mainkey = mainkey;
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
	public String getCommentRe() {
		return commentRe;
	}
	public void setCommentRe(String commentRe) {
		this.commentRe = commentRe;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getCommentWriter() {
		return commentWriter;
	}
	public void setCommentWriter(String commentWriter) {
		this.commentWriter = commentWriter;
	}
	public String getCommentContents() {
		return commentContents;
	}
	public void setCommentContents(String commentContents) {
		this.commentContents = commentContents;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	public String getTimeGubun() {
		return TimeGubun;
	}


	public void setTimeGubun(String timeGubun) {
		TimeGubun = timeGubun;
	}


	public int getTimeNum() {
		return TimeNum;
	}


	public void setTimeNum(int timeNum) {
		TimeNum = timeNum;
	}


	public String getYoutubeCommentRecommand() {
		return youtubeCommentRecommand;
	}


	public void setYoutubeCommentRecommand(String youtubeCommentRecommand) {
		this.youtubeCommentRecommand = youtubeCommentRecommand;
	}


	public String getYoutubeNotCommentRecommand() {
		return YoutubeNotCommentRecommand;
	}


	public void setYoutubeNotCommentRecommand(String youtubeNotCommentRecommand) {
		YoutubeNotCommentRecommand = youtubeNotCommentRecommand;
	}


	public String getCommentRegDate() {
		return commentRegDate;
	}


	public void setCommentRegDate(String commentRegDate) {
		this.commentRegDate = commentRegDate;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getDataregdate() {
		return dataregdate;
	}
	public void setDataregdate(String dataregdate) {
		this.dataregdate = dataregdate;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	public String getCrawlerDate() {
		return crawlerDate;
	}
	public void setCrawlerDate(String crawlerDate) {
		this.crawlerDate = crawlerDate;
	}
	
	
}
