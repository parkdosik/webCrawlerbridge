package kr.co.wisenut.webCrawler.vo;

import java.util.ArrayList;

public class CafeVO {
	
	private String docid = "";
	private String keywordNum = "";
	private String keyword = "";
	
	private String mainCategory = "";
	private String subCategory  = "";
	
	/** 카폐 명 ( 네이버 ) */
	private String cafeName = "";
	/** 제목 ( 네이버 )*/
	private String cafeTitle = "";
	/** 내용 ( 네이버 )*/
	private String cafeContents = "";
	/** url ( 네이버 )*/
	private String cafeUrl = "";
	/** 작성자 (네이버 ) */
	private String cafeWriter = "";
	/** 등록 시간 ( 네이버 )*/
	private String cafeInput = "";
	/** 수집시 시간 설정 */
	private String cafeCrawlerDate = "";
	/** 댓글수 ( 다음 )*/
	private String cafeRepl = "";
	/** 추천해요 ( 다음 )*/
	private String cafeRecommand = "";
	/** 스크랩 ( 다음 )*/
	private String cafeScrap = "";
	private String insertDate  = "";
	private String crawlerDate = "";
	private String dataregDate = "";
	
	/* */
	private int SearchNum;
	
	/** 댓글 리스트 ( 네이버 )  */
	private ArrayList<CafeCommentVO> cafeCommentList = new ArrayList<CafeCommentVO>();
	
	/** 구분값1 해당 사이트 구분값( 네이버 )*/
	private String gbn = "";

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

	public String getCafeName() {
		return cafeName;
	}

	public void setCafeName(String cafeName) {
		this.cafeName = cafeName;
	}

	public String getCafeTitle() {
		return cafeTitle;
	}

	public void setCafeTitle(String cafeTitle) {
		this.cafeTitle = cafeTitle;
	}

	public String getCafeContents() {
		return cafeContents;
	}

	public void setCafeContents(String cafeContents) {
		this.cafeContents = cafeContents;
	}

	public String getCafeUrl() {
		return cafeUrl;
	}

	public void setCafeUrl(String cafeUrl) {
		this.cafeUrl = cafeUrl;
	}

	public String getCafeWriter() {
		return cafeWriter;
	}

	public void setCafeWriter(String cafeWriter) {
		this.cafeWriter = cafeWriter;
	}

	public String getCafeInput() {
		return cafeInput;
	}

	public void setCafeInput(String cafeInput) {
		this.cafeInput = cafeInput;
	}

	public String getCafeCrawlerDate() {
		return cafeCrawlerDate;
	}

	public void setCafeCrawlerDate(String cafeCrawlerDate) {
		this.cafeCrawlerDate = cafeCrawlerDate;
	}

	public String getCafeRepl() {
		return cafeRepl;
	}

	public void setCafeRepl(String cafeRepl) {
		this.cafeRepl = cafeRepl;
	}

	public String getCafeRecommand() {
		return cafeRecommand;
	}

	public void setCafeRecommand(String cafeRecommand) {
		this.cafeRecommand = cafeRecommand;
	}

	public String getCafeScrap() {
		return cafeScrap;
	}

	public void setCafeScrap(String cafeScrap) {
		this.cafeScrap = cafeScrap;
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

	public String getDataregDate() {
		return dataregDate;
	}

	public void setDataregDate(String dataregDate) {
		this.dataregDate = dataregDate;
	}

	public int getSearchNum() {
		return SearchNum;
	}

	public void setSearchNum(int searchNum) {
		SearchNum = searchNum;
	}

	public ArrayList<CafeCommentVO> getCafeCommentList() {
		return cafeCommentList;
	}

	public void setCafeCommentList(ArrayList<CafeCommentVO> cafeCommentList) {
		this.cafeCommentList = cafeCommentList;
	}

	public String getGbn() {
		return gbn;
	}

	public void setGbn(String gbn) {
		this.gbn = gbn;
	}

	
	// ----------------------------------------
	
	
	
	
	
	
	
	
	
}
