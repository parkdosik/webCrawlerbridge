package kr.co.wisenut.webCrawler.vo;


/**
 * 카테고리 관련
 * @author USER
 *
 */
public class CategoryVO {
	
	/** 카테고리 ID */
	private int searchNum;
	/** 메인 카테고리 메인 */
	private String mainCategory = "";
	/** 서브카테고리 */
	private String subCategory = "";
	/** baseurl ( 네이버 / 다음 )*/
	private String baseUrl = "";
	/** 검색 결과 정렬 */
	private String sortOption = ""; 
	/** 검색 결과 날짜 */
	private String optionFnc = "";
	/** ? */
	private String etcOption = "";
	/** 구독자 수 (유튜브)*/
	private String subscribe = "";
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
	
	
	
	/** ruleset 검색결과 리스트 코드 */
	private String resultListCode = "";
	/** 포털 클릭 코드 */
	private String portalLinkCode = "";
	/** 언론사기사Url */
	private String pressUrlCode = ""; 
	/** 기사제목 */
	private String TitleCode = "";
	/** 제목 클릭 코드 ( 네이버 )*/
	private String TitleLinkCode  = "";
	/** 제목 클릭 확장 코드 (다음)*/
	private String TitleLinkCode2 = "";
	/** 기사입력일자 */
	private String InputDateCode = "";
	/** 요약확인 코드 */
	private String SummaryExistsCode = "";
	/** 기사요약본 */
	private String SummaryCode = "";
	/** 기사요약 클릭본 */
	private String SummaryclickCode = "";
	/** 기사내용코드 */
	private String ContentCode 		= "";
	private String ContentCodeExt 	= "";
	private String ContentCodeExt2 	= "";
	/** 사이트 이름 코드 */
	private String NameCode = "";
	
	/** 작성자 코드(언론사명확인) */
	private String WriterCode = "";
	/** 공감하기 코드 */
	private String sympathy  = "";
	
	private String recommend 	= "";
	private String notrecomment = "";
	
	/** 스크랩 코드 */
	private String scrap   = "";
	/** 조회수 코드 */
	private String readnum = "";
	/** 채널코멘트인트로코드 */
	private String commentchannelIntro  = "";
	/** 채널코멘트모어코드 */
	private String commentchannelmore   = "";
	
	/** 댓글리스트 코드 */
	private String CommentListCode = "";
	/** 페이지 이동 코드 */
	private String pagingNextElementCode = "";
	/** 코멘트 페이지 이동 코드 */
	private String commentNextpaging  = "";
	
	private String commentPaginate  = "";
	
	//코멘트 코드 
	/** commentList 코드 */
	private String commentList = "";
	/** commentList Sort 코드 */
	private String commentListSort = "";
	/** comment 작성자 코드 */
	private String commentWriter   = "";
	
	private String commentWriterExt   = "";
	
	/** comment 코멘트 코드 */
	private String commentContents   = "";
	/** comment 코멘트 확장 코드 */
	private String commentContentsExt   = "";
	/** comment 코멘트 작성일자 코드 */
	private String commentDate   = "";
	/** comment 코멘트 작성일자 확장 코드 */
	private String commentDateExt   = "";
	/** comment 코멘트 likeit 코드 */
	private String commentRecommand  = "";
	/** comment 코멘트 likeit 확장 코드 */
	private String commentRecommandExt  = "";
	/** comment 코멘트 disagree 코드 */
	private String commentNotRecommand  = "";
	/** comment 코멘트 disagree 확장 코드 */
	private String commentNotRecommandExt  = "";
	private String displayedbutton  = "";
	/** 채널영상설명class 확인 코드 */
	private String channelDescClass  		= "";
	
	public int getSearchNum() {
		return searchNum;
	}
	public void setSearchNum(int searchNum) {
		this.searchNum = searchNum;
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
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getSortOption() {
		return sortOption;
	}
	public void setSortOption(String sortOption) {
		this.sortOption = sortOption;
	}
	public String getOptionFnc() {
		return optionFnc;
	}
	public void setOptionFnc(String optionFnc) {
		this.optionFnc = optionFnc;
	}
	public String getEtcOption() {
		return etcOption;
	}
	public void setEtcOption(String etcOption) {
		this.etcOption = etcOption;
	}
	public String getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public String getResultListCode() {
		return resultListCode;
	}
	public void setResultListCode(String resultListCode) {
		this.resultListCode = resultListCode;
	}
	
	public String getPortalLinkCode() {
		return portalLinkCode;
	}
	public void setPortalLinkCode(String portalLinkCode) {
		this.portalLinkCode = portalLinkCode;
	}
	public String getPressUrlCode() {
		return pressUrlCode;
	}
	public void setPressUrlCode(String pressUrlCode) {
		this.pressUrlCode = pressUrlCode;
	}
	public String getTitleCode() {
		return TitleCode;
	}
	public void setTitleCode(String titleCode) {
		TitleCode = titleCode;
	}
	
	public String getTitleLinkCode() {
		return TitleLinkCode;
	}
	public void setTitleLinkCode(String titleLinkCode) {
		TitleLinkCode = titleLinkCode;
	}
	
	public String getTitleLinkCode2() {
		return TitleLinkCode2;
	}
	public void setTitleLinkCode2(String titleLinkCode2) {
		TitleLinkCode2 = titleLinkCode2;
	}
	public String getInputDateCode() {
		return InputDateCode;
	}
	public void setInputDateCode(String inputDateCode) {
		InputDateCode = inputDateCode;
	}
	
	public String getSummaryExistsCode() {
		return SummaryExistsCode;
	}
	public void setSummaryExistsCode(String summaryExistsCode) {
		SummaryExistsCode = summaryExistsCode;
	}
	public String getSummaryCode() {
		return SummaryCode;
	}
	public void setSummaryCode(String summaryCode) {
		SummaryCode = summaryCode;
	}
	
	public String getSummaryclickCode() {
		return SummaryclickCode;
	}
	public void setSummaryclickCode(String summaryclickCode) {
		SummaryclickCode = summaryclickCode;
	}
	public String getContentCode() {
		return ContentCode;
	}
	public void setContentCode(String contentCode) {
		ContentCode = contentCode;
	}
	public String getContentCodeExt() {
		return ContentCodeExt;
	}
	public void setContentCodeExt(String contentCodeExt) {
		ContentCodeExt = contentCodeExt;
	}
	public String getContentCodeExt2() {
		return ContentCodeExt2;
	}
	public void setContentCodeExt2(String contentCodeExt2) {
		ContentCodeExt2 = contentCodeExt2;
	}
	public String getWriterCode() {
		return WriterCode;
	}
	public void setWriterCode(String writerCode) {
		WriterCode = writerCode;
	}
	
	public String getNameCode() {
		return NameCode;
	}
	public void setNameCode(String nameCode) {
		NameCode = nameCode;
	}
	public String getSympathy() {
		return sympathy;
	}
	public void setSympathy(String sympathy) {
		this.sympathy = sympathy;
	}
	
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	
	public String getNotrecomment() {
		return notrecomment;
	}
	public void setNotrecomment(String notrecomment) {
		this.notrecomment = notrecomment;
	}
	public String getScrap() {
		return scrap;
	}
	public void setScrap(String scrap) {
		this.scrap = scrap;
	}
	
	public String getReadnum() {
		return readnum;
	}
	public void setReadnum(String readnum) {
		this.readnum = readnum;
	}
	public String getCommentchannelIntro() {
		return commentchannelIntro;
	}
	public void setCommentchannelIntro(String commentchannelIntro) {
		this.commentchannelIntro = commentchannelIntro;
	}
	public String getCommentchannelmore() {
		return commentchannelmore;
	}
	public void setCommentchannelmore(String commentchannelmore) {
		this.commentchannelmore = commentchannelmore;
	}
	public String getCommentListCode() {
		return CommentListCode;
	}
	public void setCommentListCode(String commentListCode) {
		CommentListCode = commentListCode;
	}
	public String getCommentListSort() {
		return commentListSort;
	}
	public void setCommentListSort(String commentListSort) {
		this.commentListSort = commentListSort;
	}
	public String getCommentWriter() {
		return commentWriter;
	}
	public void setCommentWriter(String commentWriter) {
		this.commentWriter = commentWriter;
	}
	
	public String getCommentWriterExt() {
		return commentWriterExt;
	}
	public void setCommentWriterExt(String commentWriterExt) {
		this.commentWriterExt = commentWriterExt;
	}
	public String getCommentContents() {
		return commentContents;
	}
	public void setCommentContents(String commentContents) {
		this.commentContents = commentContents;
	}
	public String getCommentContentsExt() {
		return commentContentsExt;
	}
	public void setCommentContentsExt(String commentContentsExt) {
		this.commentContentsExt = commentContentsExt;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	public String getCommentDateExt() {
		return commentDateExt;
	}
	public void setCommentDateExt(String commentDateExt) {
		this.commentDateExt = commentDateExt;
	}
	public String getCommentRecommand() {
		return commentRecommand;
	}
	public void setCommentRecommand(String commentRecommand) {
		this.commentRecommand = commentRecommand;
	}
	public String getCommentRecommandExt() {
		return commentRecommandExt;
	}
	public void setCommentRecommandExt(String commentRecommandExt) {
		this.commentRecommandExt = commentRecommandExt;
	}
	public String getCommentNotRecommand() {
		return commentNotRecommand;
	}
	public void setCommentNotRecommand(String commentNotRecommand) {
		this.commentNotRecommand = commentNotRecommand;
	}
	public String getCommentNotRecommandExt() {
		return commentNotRecommandExt;
	}
	public void setCommentNotRecommandExt(String commentNotRecommandExt) {
		this.commentNotRecommandExt = commentNotRecommandExt;
	}
	public String getPagingNextElementCode() {
		return pagingNextElementCode;
	}
	public void setPagingNextElementCode(String pagingNextElementCode) {
		this.pagingNextElementCode = pagingNextElementCode;
	}
	
	public String getCommentNextpaging() {
		return commentNextpaging;
	}
	public void setCommentNextpaging(String commentNextpaging) {
		this.commentNextpaging = commentNextpaging;
	}
	
	public String getCommentPaginate() {
		return commentPaginate;
	}
	public void setCommentPaginate(String commentPaginate) {
		this.commentPaginate = commentPaginate;
	}
	public String getCommentList() {
		return commentList;
	}
	public void setCommentList(String commentList) {
		this.commentList = commentList;
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
	
	public String getDisplayedbutton() {
		return displayedbutton;
	}
	public void setDisplayedbutton(String displayedbutton) {
		this.displayedbutton = displayedbutton;
	}
	
	public String getChannelDescClass() {
		return channelDescClass;
	}
	public void setChannelDescClass(String channelDescClass) {
		this.channelDescClass = channelDescClass;
	}
	@Override
	public String toString() {
		return "CategoryVO [searchNum=" + searchNum + ", mainCategory=" + mainCategory + ", subCategory=" + subCategory
				+ ", baseUrl=" + baseUrl + ", sortOption=" + sortOption + ", optionFnc=" + optionFnc + ", etcOption="
				+ etcOption + ", likeitGood=" + likeitGood + ", likeitWarm=" + likeitWarm + ", likeitSad=" + likeitSad
				+ ", likeitAngry=" + likeitAngry + ", likeitWant=" + likeitWant + ", resultListCode=" + resultListCode
				+ ", pressUrlCode=" + pressUrlCode + ", TitleCode=" + TitleCode + ", InputDateCode=" + InputDateCode
				+ ", SummaryCode=" + SummaryCode + ", ContentCode=" + ContentCode + ", WriterCode=" + WriterCode
				+ ", sympathy=" + sympathy + ", CommentListCode=" + CommentListCode + ", pagingNextElementCode="
				+ pagingNextElementCode + "]";
	}
	
	
	
	
	
	
	
	
}
