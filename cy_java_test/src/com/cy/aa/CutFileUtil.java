//package com.cy.aa;
//
//public class CutFileUtil {
//	import java.io.File;  
//	import java.io.IOException;  
//	  
//	/** 
//	 * @description 文件分割|文件合并 
//	 * @DATE 2012-1-3上午11:30:43 
//	 */  
//	public abstract class PartitionFile {  
//	 /** 
//	  * 单个文件设置的字节数 
//	  */  
//	 public static long MAX_BYTE = 1024 * 1024 * 1024;  
//	  
//	 /** 
//	  * 获取可以分割的文件数 
//	  *  
//	  * @param filePath 
//	  * @param max_byte 
//	  * @return 
//	  */  
//	 public int getPartitionFileNum(long fileByte, String filePath) {  
//	  if (MAX_BYTE < fileByte) {  
//	   if (fileByte % MAX_BYTE == 0) {  
//	    return (int) (fileByte / MAX_BYTE);  
//	   } else {  
//	    return (int) (fileByte / MAX_BYTE) + 1;  
//	   }  
//	  }  
//	  return 1;  
//	 }  
//	  
//	 /** 
//	  * 获取文件长度 
//	  *  
//	  * @param file 
//	  * @return 
//	  * @throws IOException 
//	  */  
//	 public abstract long getFileLength(File file) throws IOException;  
//	  
//	 /** 
//	  * 分割Byte文件 
//	  *  
//	  * @param file 
//	  * @throws IOException 
//	  * @throws IOException 
//	  */  
//	 public abstract String[] partitionFile(File srcFile, int partitionFileNum) throws IOException;  
//	  
//	 /** 
//	  * 合并文件 
//	  *  
//	  * @param files 
//	  * @param newFile 
//	  * @throws IOException 
//	  */  
//	 public abstract void uniteFile(String[] files, String newFile)  
//	   throws IOException;  
//	}  
//	  
//	  
//	   
//	  
//	package com.lss.common.file.partition;  
//	  
//	import java.io.DataInputStream;  
//	import java.io.DataOutputStream;  
//	import java.io.File;  
//	import java.io.FileInputStream;  
//	import java.io.FileNotFoundException;  
//	import java.io.FileOutputStream;  
//	import java.io.IOException;  
//	  
//	/** 
//	 * 二进制文件分割 
//	 *  
//	 * @description 
//	 * @DATE 2012-1-8下午01:38:15 
//	 */  
//	public class PartitionBinary extends PartitionFile {  
//	  
//	 @Override  
//	 public long getFileLength(File file) {  
//	  return file.length();  
//	 }  
//	  
//	 @Override  
//	 public String[] partitionFile(File srcFile, int partitionFileNum) throws IOException {  
//	  FileInputStream fis = null;  
//	  DataInputStream dis = null;  
//	  FileOutputStream fos = null;  
//	  DataOutputStream out = null;  
//	  String[] partitions = new String[partitionFileNum];  
//	  try {  
//	   fis = new FileInputStream(srcFile);  
//	   dis = new DataInputStream(fis);  
//	   for (int i = 0; i < partitionFileNum; i++) {  
//	    String name = null;  
//	    if (srcFile.getName().indexOf(".") != -1) {  
//	     name = srcFile.getName().substring(0,  
//	       srcFile.getName().indexOf("."));  
//	    } else {  
//	     name = srcFile.getName();  
//	    }  
//	    partitions[i] = srcFile.getParent() + "/" + name + "_" + i;  
//	    fos = new FileOutputStream(partitions[i]);  
//	    out = new DataOutputStream(fos);  
//	    long transMaxByte = MAX_BYTE;  
//	    while (transMaxByte > 0) {  
//	     byte[] b = null;  
//	     if (transMaxByte > 1024) {  
//	      b = new byte[1024];  
//	     } else {  
//	      b = new byte[(int) transMaxByte];  
//	     }  
//	     if ((dis.read(b)) != -1) {  
//	      fos.write(b);  
//	      transMaxByte = transMaxByte - b.length;  
//	     } else {  
//	      System.out.println("transMaxByte" + transMaxByte);  
//	      break;  
//	     }  
//	    }  
//	    out.flush();  
//	    fos.flush();  
//	    out.close();  
//	    fos.close();  
//	   }  
//	  } catch (FileNotFoundException ex) {  
//	   throw ex;  
//	  } finally {  
//	   if (fis != null)  
//	    fis.close();  
//	   if (out != null)  
//	    out.close();  
//	  }  
//	  return partitions;  
//	 }  
//	  
//	 @Override  
//	 public void uniteFile(String[] files, String newFile) throws IOException {  
//	  FileInputStream fis = null;  
//	  DataInputStream dis = null;  
//	  FileOutputStream fos = null;  
//	  DataOutputStream out = null;  
//	  try {  
//	   fos = new FileOutputStream(newFile);  
//	   out = new DataOutputStream(fos);  
//	   for (int i = 0; i < files.length; i++) {  
//	    fis = new FileInputStream(files[i]);  
//	    dis = new DataInputStream(fis);  
//	    byte[] b = new byte[1024];  
//	    while ((dis.read(b)) != -1) {  
//	     fos.write(b);  
//	    }  
//	    out.flush();  
//	    fos.flush();  
//	    fis.close();  
//	    dis.close();  
//	   }  
//	  } catch (FileNotFoundException ex) {  
//	   throw ex;  
//	  } finally {  
//	   if (fis != null)  
//	    fis.close();  
//	   if (out != null)  
//	    out.close();  
//	  }  
//	 }  
//	  
//	}  
//	  
//	  
//	   
//	  
//	   
//	  
//	   
//	  
//	package com.lss.common.file.partition;  
//	  
//	import java.io.BufferedReader;  
//	import java.io.BufferedWriter;  
//	import java.io.File;  
//	import java.io.FileNotFoundException;  
//	import java.io.FileReader;  
//	import java.io.FileWriter;  
//	import java.io.IOException;  
//	  
//	/** 
//	 * TEXT 文件分割，该分割方式按行分割 
//	 *  
//	 * @description 
//	 * @DATE 2012-1-8下午01:36:39 
//	 */  
//	public class PartitionTextFile extends PartitionFile {  
//	  
//	 @SuppressWarnings("finally")  
//	 @Override  
//	 public long getFileLength(File file) throws IOException {  
//	  FileReader fr = null;  
//	  BufferedReader br = null;  
//	  long fileSize = 0;  
//	  try {  
//	   fr = new FileReader(file);  
//	   br = new BufferedReader(fr);  
//	   String line = br.readLine();  
//	   while (line != null) {  
//	    fileSize += line.length();  
//	    line = br.readLine();  
//	   }  
//	  } catch (FileNotFoundException ex) {  
//	   ex.printStackTrace();  
//	  } catch (IOException ex) {  
//	   ex.printStackTrace();  
//	  } finally {  
//	   if (br != null)  
//	    br.close();  
//	   if (fr != null)  
//	    fr.close();  
//	   return fileSize;  
//	  }  
//	 }  
//	  
//	 @Override  
//	 public String[] partitionFile(File srcFile, int partitionFileNum)  
//	   throws IOException {  
//	  if (partitionFileNum <= 0)  
//	   return null;  
//	  FileReader fr = null;  
//	  BufferedReader br = null;  
//	  long readNum = 0;  
//	  String[] partitions = new String[partitionFileNum];  
//	  try {  
//	   fr = new FileReader(srcFile);  
//	   br = new BufferedReader(fr);  
//	   int i = 0;  
//	   while (partitionFileNum > i) {  
//	    String name = null;  
//	    if (srcFile.getName().indexOf(".") != -1) {  
//	     name = srcFile.getName().substring(0,srcFile.getName().indexOf("."));  
//	    } else {  
//	     name = srcFile.getName();  
//	    }  
//	    partitions[i] = srcFile.getParent() + "/" + name + "_" + i;  
//	    File wfile = new File(partitions[i]);  
//	    if (!wfile.exists()) {  
//	     wfile.getParentFile().mkdirs();  
//	     wfile.createNewFile();  
//	    }  
//	    FileWriter fw = new FileWriter(wfile,false);  
//	    BufferedWriter bw = new BufferedWriter(fw);  
//	    String line = br.readLine();  
//	    int flush=0;  
//	    while (line != null) {  
//	     if (line.trim().length() == 0) {  
//	      line = br.readLine();  
//	      continue;  
//	     }  
//	     readNum += line.length();  
//	     if (i + 1 == partitionFileNum) {  
//	      bw.write(line);  
//	      bw.newLine();  
//	     } else {  
//	      if (readNum >= MAX_BYTE) {  
//	       bw.write(line);  
//	       bw.newLine();  
//	       break;  
//	      } else {  
//	       bw.write(line);  
//	       bw.newLine();  
//	      }  
//	     }  
//	     line = br.readLine();  
//	     if(flush%1000==0){  
//	      bw.flush();  
//	     }  
//	    }  
//	    bw.flush();  
//	    fw.flush();  
//	    bw.close();  
//	    fw.close();  
//	    readNum = 0;  
//	    i++;  
//	   }  
//	  } finally {  
//	   try {  
//	    if (br != null)  
//	     br.close();  
//	    if (fr != null)  
//	     fr.close();  
//	   } catch (IOException e) {  
//	   } finally {  
//	    br = null;  
//	    fr = null;  
//	   }  
//	  }  
//	  return partitions;  
//	 }  
//	  
//	 @Override  
//	 public void uniteFile(String[] files, String newFile) throws IOException {  
//	  File wFile = new File(newFile);  
//	  FileWriter writer = null;  
//	  BufferedWriter bufferedWriter = null;  
//	  try {  
//	   writer = new FileWriter(wFile,false);  
//	   bufferedWriter = new BufferedWriter(writer);  
//	   for (int i = 0; i < files.length; i++) {  
//	    File rFile = new File(files[i]);  
//	    FileReader reader = new FileReader(rFile);  
//	    BufferedReader bufferedReader = new BufferedReader(reader);  
//	    String line = bufferedReader.readLine();  
//	    while (line != null) {  
//	     if (line.trim().length() == 0) {  
//	      line = bufferedReader.readLine();  
//	      continue;  
//	     }  
//	     bufferedWriter.write(line);  
//	     bufferedWriter.newLine();  
//	     line = bufferedReader.readLine();  
//	    }  
//	    bufferedWriter.flush();  
//	    writer.flush();  
//	   }  
//	  } finally {  
//	   if (bufferedWriter != null)  
//	    bufferedWriter.close();  
//	   bufferedWriter = null;  
//	   if (writer != null)  
//	    writer.close();  
//	   writer = null;  
//	  }  
//	 }  
//	  
//	}  
//	  
//}
