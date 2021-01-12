package kr.co.wisenut.webCrawler.vo;

public class NewsCommentVO {
	
	/** 원천 뉴스 키 */
	private String mainKey = "";
	
	/** 댓글 키 */
	private String docid = "";
	private String keyword 		= "";
	private String keywordNum 	= "";
	private String mainCategory = "";
	private String subCategory 	= "";
	
	/** 댓글작성자 */
	private String writer = "";
	
	/** 댓글 내용 */
	private String contents = "";
	
	/** 댓글 날짜 */
	private String date = "";
	
	/** 댓글 공감 */
	private String recomm = "";
	
	/** 댓글 비공감 */
	private String unrecomm = "";

	/** 댓글 에 메인,댓글에 댓글 인지  1 ,2 ,3 ... */
	private String commentRe = "";
	
	/** */
	private String dataregDate = "";
	/** */
	private String insertDate = "";
	/** */
	private String crawlerDate = "";
	

	public String getMainKey() {
		return mainKey;
	}
	public void setMainKey(String mainKey) {
		this.mainKey = mainKey;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeywordNum() {
		return keywordNum;
	}
	public void setKeywordNum(String keywordNum) {
		this.keywordNum = keywordNum;
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
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRecomm() {
		return recomm;
	}
	public void setRecomm(String recomm) {
		this.recomm = recomm;
	}
	public String getUnrecomm() {
		return unrecomm;
	}
	public void setUnrecomm(String unrecomm) {
		this.unrecomm = unrecomm;
	}
	public String getCommentRe() {
		return commentRe;
	}
	public void setCommentRe(String commentRe) {
		this.commentRe = commentRe;
	}
	public String getDataregDate() {
		return dataregDate;
	}
	public void setDataregDate(String dataregDate) {
		this.dataregDate = dataregDate;
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
