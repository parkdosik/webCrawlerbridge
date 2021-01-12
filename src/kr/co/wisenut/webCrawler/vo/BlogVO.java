package kr.co.wisenut.webCrawler.vo;

public class BlogVO {
	
	private String docid = "";
	private String keywordNum = "";
	private String keyword = "";
	private String mainCategory = "";
	private String subCategory  = "";

	private int SearchNum;
	/** 제목 ( 네이버 )*/
	private String blogTitle = "";
	/** 내용 ( 네이버 )*/
	private String blogContents = "";
	/** url ( 네이버 )*/
	private String blogUrl = "";
	/** 등록 시간 ( 네이버 )*/
	private String blogInput = "";
	/** 수집시 시간 설정 */
	private String blogCrawlerDate = "";
	/** 블로그 이름 */
	private String blogName = "";
	private String insertDate  = "";
	private String crawlerDate = "";
	private String dataregDate = "";

	/** 구분값1 해당 사이트 구분값( 네이버 )*/
	private String gbn = "";
	//-------------------------------------------------------

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

	public int getSearchNum() {
		return SearchNum;
	}

	public void setSearchNum(int searchNum) {
		SearchNum = searchNum;
	}

	public String getBlogTitle() {
		return blogTitle;
	}

	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}

	public String getBlogContents() {
		return blogContents;
	}

	public void setBlogContents(String blogContents) {
		this.blogContents = blogContents;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}

	public String getBlogInput() {
		return blogInput;
	}

	public void setBlogInput(String blogInput) {
		this.blogInput = blogInput;
	}

	public String getBlogCrawlerDate() {
		return blogCrawlerDate;
	}

	public void setBlogCrawlerDate(String blogCrawlerDate) {
		this.blogCrawlerDate = blogCrawlerDate;
	}

	public String getBlogName() {
		return blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
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

	public String getGbn() {
		return gbn;
	}

	public void setGbn(String gbn) {
		this.gbn = gbn;
	}
	
	
	
	


}
