package com.dbapp;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.dbapp.DBConnFactory;
/**
 * 
 * 날짜                       : 이름      : 내용
 * 2017.07.05 : 박도식  : bridge-v4.0.1-bld0001 동일 내용
 * 
 */
public class ProtoApp {
	
	DBConnFactory factory;
	
	public ProtoApp() {
	    
	    factory = new DBConnFactory();
	}
	
	private Connection getConnection(String arg) throws Exception {
    	
    	    //return factory.getConnection("acd@ACCDEV");
    	    return factory.getConnection(arg);
 	}
	
	private void getDBData(String arg) {
		
	    Connection        conn 		=   null;
	    PreparedStatement pstmt   	=   null;
	    Vector            vt 		=   new Vector();
	    StringBuffer  	  sbSql     =   new StringBuffer();
	    int nColCnt  =   0;
	    String[] rowdata;
		
	    try  {
		String passwd = "";
            	ResultSet rs = null;
            	conn   = getConnection(arg);
            	pstmt = conn.prepareStatement(
            		"SELECT * FROM TAB");
            	rs    = pstmt.executeQuery();
            	nColCnt=rs.getMetaData().getColumnCount();
            	while (rs.next()) {
                    rowdata = new String[nColCnt];
                    for(int i = 0; i < nColCnt; i++) 
                    rowdata[i] = rs.getString(i+1);
                    vt.addElement(rowdata);
                }
            
                for(int i=0; i<vt.size(); i++) {
		    String[] temp = (String[])vt.get(i);
		    for(int j=0; j<temp.length; j++) {
		    	System.out.print( temp[j] );
		    	System.out.print("\t");
		    }
		    
                }
        	
            } catch(Exception se) {
                se.printStackTrace();
            } finally {
                try {
                    if(pstmt != null) pstmt.close();
                    if(conn  != null) conn.close();       
                }
                catch(Exception e) {
            	    e.printStackTrace();
                }
            } 
	}
	
	public static void main(String[] args) {
		/**
		* Usage : java ProtoApp DB USER ID
		*/
		ProtoApp app = new ProtoApp();
		app.getDBData(args[0]);
	}	
}
