/*
 * @(#)SCDManager.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.util;

import kr.co.wisenut.common.logger.Log2;

import java.io.*;

/**
 *
 * SCDManager
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class SCDManager {
    public static final int BUFFER_SIZE =  4096;
    private final String filterDir = "Filter"; //Fixed directory name
    //Define SCD File type
    public static final String[] TYPE_STRING = new String[] { "I", "U", "D", "R" };

    public static final int TYPE_INSERT = 0;
    public static final int TYPE_UPDATE = 1;
    public static final int TYPE_DELETE = 2;
    public static final int TYPE_REPLACE = 3;

    // SCD file directory variable
    private final File scdDir;
    // writing... (TEMP) SCD file name
    private final File[] scdFile;
    private final String sourceIdx;
    // writing... (TEMP) SCD WRITER
    private final BufferedWriter[] scdWriter;
    // Add SCD document count
    private final int[] scdCount;
    private static int MaxScdCount = 0;
    private static int  MaxBufferSize = 2048 * 1000 * 1000;
    //private boolean isUTF = false;
    private boolean isTEST = false;
    private boolean isMKE = false;
    private String charset = "";

    /**
     * Construct new instance with variables
     * @param scdDir SCD file directory
     *                Max count of documents are written to a file
     * @param charSet scd encoding charSet
     */
    public SCDManager(String scdDir, String charSet) {
        this(new File(scdDir), charSet, "00");
    }

    public SCDManager(String scdDir, String charSet, boolean isMKE) {
        this(new File(scdDir), charSet, "00");
        this.isMKE = isMKE;
    }

    public SCDManager(File scdDir,String charSet, boolean isMKE) {
        this(scdDir, charSet, "00");
        this.isMKE = isMKE;
    }

    /**
     * Construct new instance with variables
     * @param scdDir SCD file directory
     *                Max count of documents are written to a file
     * @param charSet scd encoding charSet
     */
    public SCDManager(File scdDir,String charSet, String sourceIdx) {
        this.scdDir = scdDir;
        // Variables initialize
        this.scdFile = new File[4];
        this.sourceIdx = sourceIdx;
        this.scdWriter = new BufferedWriter[4];
        this.scdCount = new int[] { 0, 0, 0, 0 };

        if( charset.equals("") || charSet.equalsIgnoreCase("UTF-8")) {
            Log2.out("[SCDManager] [SCD File Encoding : UTF-8]");
            //isUTF = true;
            this.charset = "UTF-8";
        }else{
            Log2.out("[SCDManager] [SCD File Encoding :+" + charset + "]");
            //isUTF = false;
            this.charset = charSet;
        }
        // Create the SCD directory, if not exists
        if (!scdDir.exists()) {
            scdDir.mkdirs();
        }
        if(System.getProperty("scd.count") != null) {
            try {
                MaxScdCount = Integer.parseInt(System.getProperty("scd.count"));
            }catch(Exception e) {}
        }
        
        if( System.getProperty("scd.size") != null ) {
        	try {
        		MaxBufferSize = Integer.parseInt( System.getProperty("scd.size") ) * 1000 * 1000;
        	}catch(Exception e) {}
        }
    }

    /**
     * INSERT Type document add
     *
     * @param doc :
     *            SCD Document string value
     * @throws java.io.IOException error info
     */
    public void insert(String doc) throws IOException {
        appendDoc(TYPE_INSERT, doc);
    }

    /**
     * UPDATE Type document add
     *
     * @param doc :
     *            SCD Document string value
     * @throws IOException error info
     */
    public void update(String doc) throws IOException {
        appendDoc(TYPE_UPDATE, doc);
    }

    /**
     * DELETE Type document add
     *
     * @param doc :
     *            SCD Document string value
     * @throws IOException  error info
     */
    public void delete(String doc) throws IOException {
        appendDoc(TYPE_DELETE, doc);
    }

    /**
     * REPLACE Type document add
     *
     * @param doc :
     *            SCD Document string value
     * @throws IOException error info
     */
    public void replace(String doc) throws IOException {
        appendDoc(TYPE_REPLACE, doc);
    }

    /**
     * Make flush and close all SCD files (I/U/D/R Type)
     * @throws IOException error info
     */
    public void errorFlushAll() throws IOException {
        for (int i = 0; i < scdWriter.length; i++) {
            errorFlush(i);
        }
    }

    /**
     * Make flush and close all SCD files (I/U/D/R Type)
     * @throws IOException error info
     */
    public void flushAll() throws IOException {
        for (int i = 0; i < scdWriter.length; i++) {
            flush(i);
        }
    }

    /**
     *
     * @param isTest test y/n
     * @throws IOException error info
     */
    public void errorClose(boolean isTest) throws IOException {
        this.isTEST = isTest;
        errorFlushAll();
    }

    /**
     *
     * @param isTest test y/n
     * @throws IOException error info
     */
    public void close(boolean isTest) throws IOException {
        this.isTEST = isTest;
        flushAll();
    }

    /*
    * Append the document to the SCD file int type : SCD type (I/U/D/R)
    */
    private void appendDoc(int type, String doc) throws IOException {
        // If the SCDWriter is null, create new SCDWrit
        if (scdWriter[type] == null) {
            File sf = getSCDTemp(type);
            scdFile[type] = sf;
//            if(isUTF) {     // Write UTF-8 Encoding
//                scdWriter[type] =  new BufferedWriter(new OutputStreamWriter( new FileOutputStream(sf), "UTF-8"), BUFFER_SIZE);
//                //scdWriter[type] = new BufferedWriter(new FileWriter(sf), BUFFER_SIZE);
//            }else{
//                scdWriter[type] = new BufferedWriter(new FileWriter(sf), BUFFER_SIZE);
//            }
            scdWriter[type] =  new BufferedWriter(new OutputStreamWriter( new FileOutputStream(sf), charset), BUFFER_SIZE);
            
            Log2.out("[SCDManager] [SCD Temp File : " + sf.getPath()  + "]");
        }
        scdWriter[type].write(doc);

        scdCount[type]++;

        //if(scdCount[type] == 10) scdWriter[type].flush();

        if(MaxScdCount > 0){
            if( scdCount[type] >= MaxScdCount){
                flush(type);
            }
        }else{
        	long size = scdFile[type].length();
            if (scdFile[type].length() >= MaxBufferSize) {
                flush(type);
            }
        }
    }


    /*
    * SCD Name format : "B-00-yyyyMMddHHmm-sssss-[I/U/D/R]-[C/F].SCD"
    */
    private File getSCDTemp(int type) {
        File sf, tf;

        do {
            String scdName = DateUtil.getScdFileTime();
            sf = new File(scdDir, "B-" + sourceIdx + "-" + scdName + "-" + TYPE_STRING[type] + "-C.SCD");
            tf = new File(scdDir, "B-" + sourceIdx + "-" + scdName + "-" + TYPE_STRING[type] + "-F.SCD");
        } while (sf.exists() || tf.exists());
        // Temporary & real SCD file are exists.. loop

        return tf;
    }

    private void errorFlush(int type) throws IOException {
        if (scdWriter[type] == null) {
            return;
        }

        scdWriter[type].flush();
        scdWriter[type].close();

        // Set SCDWriter to null
        scdWriter[type] = null;
        scdCount[type] = 0;

        // Rename temporary SCD file to real SCD file
        File tmpFile = scdFile[type];
        String tmpName = tmpFile.getName();
        if (tmpFile.exists() && tmpFile.length() == 0) {
            // Temporary SCD file is blank
            if (!tmpFile.delete()) {
                System.out.println( "[SCDWriter] SCD file delete error. " + tmpName);
            }
        }
    }

    private void flush(int type) throws IOException {
        if (scdWriter[type] == null) {
            return;
        }

        scdWriter[type].flush();
        scdWriter[type].close();

        // Set SCDWriter to null
        scdWriter[type] = null;
        scdCount[type] = 0;

        // Rename temporary SCD file to real SCD file
        File tmpFile = scdFile[type];
        String tmpName = tmpFile.getName();

        if (tmpFile.exists()) {
            // Temporary SCD file is blank
            if (tmpFile.length() == 0) {
                if (!tmpFile.delete()) {
                    System.out.println( "[SCDWriter] SCD file delete error. " + tmpName);
                }
            } else {
                String scdName = "";
                if(isTEST){
                    scdName = tmpName.substring(0, 25) + "-T.SCD";
                } else if(isMKE){
                    scdName = tmpName.substring(0, 25) + "-M.SCD";
                }else{
                    scdName = tmpName.substring(0, 25) + "-C.SCD";
                }
                File scdFile = new File(scdDir, scdName);
                if (!tmpFile.renameTo(scdFile)) {
                    System.out.println( "[SCDWriter] SCD file rename error. " + tmpName);
                }else{
                	System.out.println();
                	Log2.out("[SCDManager] [SCD complete File : " + scdFile.getPath()  + "]");
                }
                
            }
        }
    }

    /**
     * delete scd Function
     * @return True / False
     */
    public boolean delete() {
        String m_scdPath = scdDir.getAbsolutePath();
        boolean isRet = true;
        if( !FileUtil.deleteFile(m_scdPath, ".SCD") ){
            Log2.error("[SCDManager ] [SCD File Delete Fail, Directory: "+m_scdPath+"]");
            isRet = false;
        }
        if( !FileUtil.deleteFile(m_scdPath+filterDir, ".txt") ){
            Log2.error("[SCDManager ] [Filtered File Delete Fail, Directory: "+m_scdPath+"]");
            isRet = false;
        }
        return isRet;
    }
}
