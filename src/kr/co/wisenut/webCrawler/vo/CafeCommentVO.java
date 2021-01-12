package kr.co.wisenut.webCrawler.vo;


/** 카페 댓글 */
public class CafeCommentVO {
	/** 원천 카폐 PK값 */
	private String mainKey = "";
	/** 문서 KEY */
	private String docid 	  = "";
	private String keyword 	  = "";
	private String keywordNum = "";
	private String mainCategory = "";
	private String subCategory  = "";
	
	/** 댓글 작성자 */
	private String cafeCommentWriter = "";
	/** 댓글 내용 */
	private String cafeCommentContents = "";
	/** 댓글 날짜 */
	private String cafeCommentDate = "";
	/** 댓글 에 메인,댓글에 댓글 인지  1 ,2 ,3 ... */
	private String cafeCommentRe = "";
	
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
	public String getCafeCommentWriter() {
		return cafeCommentWriter;
	}
	public void setCafeCommentWriter(String cafeCommentWriter) {
		this.cafeCommentWriter = cafeCommentWriter;
	}
	public String getCafeCommentContents() {
		return cafeCommentContents;
	}
	public void setCafeCommentContents(String cafeCommentContents) {
		this.cafeCommentContents = cafeCommentContents;
	}
	public String getCafeCommentDate() {
		return cafeCommentDate;
	}
	public void setCafeCommentDate(String cafeCommentDate) {
		this.cafeCommentDate = cafeCommentDate;
	}
	public String getCafeCommentRe() {
		return cafeCommentRe;
	}
	public void setCafeCommentRe(String cafeCommentRe) {
		this.cafeCommentRe = cafeCommentRe;
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
	
	// -----------------------------
	
	

}
