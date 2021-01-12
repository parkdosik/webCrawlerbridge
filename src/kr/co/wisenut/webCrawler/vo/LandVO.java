package kr.co.wisenut.webCrawler.vo;

public class LandVO {
	/**아파트이름*/
	private String aptname = "";
	/**평형타입*/
	private String apttype = "";
	/**아파트 층수*/
	private String aptfloor = "";
	/**매매/전세 구분*/
	private String aptgubun = "";
	/**아파트가격*/
	private String aptprice = "";
	/**아파트계약일*/
	private String aptcontractdate = "";
	
	public String getAptname() {
		return aptname;
	}
	public void setAptname(String aptname) {
		this.aptname = aptname;
	}
	public String getApttype() {
		return apttype;
	}
	public void setApttype(String apttype) {
		this.apttype = apttype;
	}
	public String getAptfloor() {
		return aptfloor;
	}
	public void setAptfloor(String aptfloor) {
		this.aptfloor = aptfloor;
	}
	public String getAptgubun() {
		return aptgubun;
	}
	public void setAptgubun(String aptgubun) {
		this.aptgubun = aptgubun;
	}
	public String getAptprice() {
		return aptprice;
	}
	public void setAptprice(String aptprice) {
		this.aptprice = aptprice;
	}
	public String getAptcontractdate() {
		return aptcontractdate;
	}
	public void setAptcontractdate(String aptcontractdate) {
		this.aptcontractdate = aptcontractdate;
	}
}
