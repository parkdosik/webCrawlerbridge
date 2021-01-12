package kr.co.wisenut.webCrawler.vo;

import java.util.ArrayList;

public class NewsVO {
	private String docid = "";
	private String keywordNum = "";
	private String keyword = "";
	/** 제목 ( 네이버 )*/
	private String title = "";
	/** 이름 ( 까페 / 블로그 )*/
	private String name = "";
	/** 내용 ( 네이버 )*/
	private String contents = "";
	/** url ( 네이버 )*/
	private String url = "";
	/** baseurl ( 네이버 / 다음 )*/
	private String baseUrl = "";
	/** 원천 기사 URL 주소*/
	private String pressUrl = "";
	/** 기사입력 ( 네이버 )*/
	private String inputdate 	= "";
	private String crawlerdate  = "";
	private String dataregdate  = "";
	/** 최종수정 ( 네이버 )*/
	private String update = "";
	/** 요약  ( 네이버 )*/
	private String summary = "";
	/** 영상 업로드 일자 (유튜브 ) */
	private String uploadDate = "";
	/** 검색 결과 날짜 */
	private String optionFnc = "";
	private String portalnewsurl = ""; 
	
	/** 언론사 ( 네이버 ) */
	private String press = "";
	/** 기자 ( 네이버 ) */
	private String reporter = "";
	/** 언론 반응 : 좋아요 ( 네이버 )*/
	private String likeitGood = "";
	/** 언론 반응 : 훈훈해요 ( 네이버 )*/
	private String likeitWarm = "";
	/** 언론 반응 : 슬퍼요 ( 네이버 )*/
	private String likeitSad = "";
	/** 언론 반응 : 화나요 ( 네이버 )*/
	private String likeitAngry = "";
	/** 언론 반응 : 후속기사 원해요 ( 네이버 )*/
	private String likeitWant = "";
	
	/** 기사댓글수 ( 다음 )*/
	private String CommentCount = "";
	/** 기사작성자 - 기자 ( 다음 )*/
	private String Author = ""; 
	/** 공감하기 ( 다음 )*/
	private String empathy = ""; 
	/** 댓글 수(다음)*/
	private String ReplCount = "";
	/** 추천해요(다음)*/
	private String Recommand = "";
	/** 비추천 (유튜브 ) */
	private String NtRecommand = "";
	
	/** 스크랩 (다음)*/
	private String Scrap = ""; 
	
	/** 구독자 수 (유튜브 ) */
	private String Subscribe = "";
	/** 채널 소개 더보기 (유튜브 ) */
	private String Channelmore = "";
	/** 작성자 - 채널명 (유튜브 ) */
	private String Writer = "";
	
	private int SearchNum;
	
	private String mainCategory		 = "";
	private String subCategory		 = "";
	
	/** 수집 시간 */
	private String insertDate  = "";
	private String crawlerDate = "";
	private String dataregDate = "";
	
	/** 댓글 */
	private ArrayList<NewsCommentVO> commentList  = new ArrayList<NewsCommentVO> ();

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getPressUrl() {
		return pressUrl;
	}

	public void setPressUrl(String pressUrl) {
		this.pressUrl = pressUrl;
	}

	public String getInputdate() {
		return inputdate;
	}

	public void setInputdate(String inputdate) {
		this.inputdate = inputdate;
	}

	public String getCrawlerdate() {
		return crawlerdate;
	}

	public void setCrawlerdate(String crawlerdate) {
		this.crawlerdate = crawlerdate;
	}

	public String getDataregdate() {
		return dataregdate;
	}

	public void setDataregdate(String dataregdate) {
		this.dataregdate = dataregdate;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getOptionFnc() {
		return optionFnc;
	}

	public void setOptionFnc(String optionFnc) {
		this.optionFnc = optionFnc;
	}

	public String getPortalnewsurl() {
		return portalnewsurl;
	}

	public void setPortalnewsurl(String portalnewsurl) {
		this.portalnewsurl = portalnewsurl;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getLikeitGood() {
		return likeitGood;
	}

	public void setLikeitGood(String likeitGood) {
		this.likeitGood = likeitGood;
	}

	public String getLikeitWarm() {
		return likeitWarm;
	}

	public void setLikeitWarm(String likeitWarm) {
		this.likeitWarm = likeitWarm;
	}

	public String getLikeitSad() {
		return likeitSad;
	}

	public void setLikeitSad(String likeitSad) {
		this.likeitSad = likeitSad;
	}

	public String getLikeitAngry() {
		return likeitAngry;
	}

	public void setLikeitAngry(String likeitAngry) {
		this.likeitAngry = likeitAngry;
	}

	public String getLikeitWant() {
		return likeitWant;
	}

	public void setLikeitWant(String likeitWant) {
		this.likeitWant = likeitWant;
	}

	public String getCommentCount() {
		return CommentCount;
	}

	public void setCommentCount(String commentCount) {
		CommentCount = commentCount;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getEmpathy() {
		return empathy;
	}

	public void setEmpathy(String empathy) {
		this.empathy = empathy;
	}

	public String getReplCount() {
		return ReplCount;
	}

	public void setReplCount(String replCount) {
		ReplCount = replCount;
	}

	public String getRecommand() {
		return Recommand;
	}

	public void setRecommand(String recommand) {
		Recommand = recommand;
	}

	public String getNtRecommand() {
		return NtRecommand;
	}

	public void setNtRecommand(String ntRecommand) {
		NtRecommand = ntRecommand;
	}

	public String getScrap() {
		return Scrap;
	}

	public void setScrap(String scrap) {
		Scrap = scrap;
	}

	public String getSubscribe() {
		return Subscribe;
	}

	public void setSubscribe(String subscribe) {
		Subscribe = subscribe;
	}

	public String getChannelmore() {
		return Channelmore;
	}

	public void setChannelmore(String channelmore) {
		Channelmore = channelmore;
	}

	public String getWriter() {
		return Writer;
	}

	public void setWriter(String writer) {
		Writer = writer;
	}

	public int getSearchNum() {
		return SearchNum;
	}

	public void setSearchNum(int searchNum) {
		SearchNum = searchNum;
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

	public ArrayList<NewsCommentVO> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<NewsCommentVO> commentList) {
		this.commentList = commentList;
	}

	@Override
	public String toString() {
		return "NewsVO [docid=" + docid + ", keywordNum=" + keywordNum + ", keyword=" + keyword + ", title=" + title
				+ ", name=" + name + ", contents=" + contents + ", url=" + url + ", baseUrl=" + baseUrl + ", pressUrl="
				+ pressUrl + ", inputdate=" + inputdate + ", crawlerdate=" + crawlerdate + ", dataregdate="
				+ dataregdate + ", update=" + update + ", summary=" + summary + ", uploadDate=" + uploadDate
				+ ", optionFnc=" + optionFnc + ", portalnewsurl=" + portalnewsurl + ", press=" + press + ", reporter="
				+ reporter + ", likeitGood=" + likeitGood + ", likeitWarm=" + likeitWarm + ", likeitSad=" + likeitSad
				+ ", likeitAngry=" + likeitAngry + ", likeitWant=" + likeitWant + ", CommentCount=" + CommentCount
				+ ", Author=" + Author + ", empathy=" + empathy + ", ReplCount=" + ReplCount + ", Recommand="
				+ Recommand + ", NtRecommand=" + NtRecommand + ", Scrap=" + Scrap + ", Subscribe=" + Subscribe
				+ ", Channelmore=" + Channelmore + ", Writer=" + Writer + ", SearchNum=" + SearchNum + ", mainCategory="
				+ mainCategory + ", subCategory=" + subCategory + ", insertDate=" + insertDate + ", crawlerDate="
				+ crawlerDate + ", dataregDate=" + dataregDate + ", commentList=" + commentList + "]";
	}
	
	// ---------------------------------
	
	
	
}