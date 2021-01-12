package kr.co.wisenut.webCrawler.config.source;

public class Query {
	/** sql */
	private String sql = "";
	/** statement 값 */
	private String statement = "n";
	/** 쿼리 타입*/
	private String type = "s";
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Query [sql=" + sql + ", statement=" + statement + ", type=" + type + "]";
	}
	
	
	
}
