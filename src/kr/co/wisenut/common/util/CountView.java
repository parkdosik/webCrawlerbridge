package kr.co.wisenut.common.util;

public final class CountView {
	public static void  count(int totalCount){
		if (totalCount % 100 == 0) {
			if (totalCount % 1000 == 0 || totalCount == 0) {
				System.out.print("[" + totalCount + "]");
			} else {
				System.out.print(".");
			}
		}
	}
	public static void  count(long totalCount){
		if (totalCount % 100 == 0) {
			if (totalCount % 1000 == 0 || totalCount == 0) {
				System.out.print("[" + totalCount + "]");
			} else {
				System.out.print(".");
			}
		}
	}
}
