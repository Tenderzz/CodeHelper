package edu.nuist.codehelper.utils;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.google.common.io.Files;

public class FileUtil {
	public static void processInLine(String data, LineProcess lp){
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data)));
			String line = br.readLine();
			while (line != null)
			{
				lp.doLine(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void writeStringToFile(String str, String fileName){
		File f = new File(fileName);
		try {
			String targetDir = fileName.substring(0, fileName.lastIndexOf("/"));
			if( !new File(targetDir).exists() ){
				Files.createParentDirs(new File(fileName));
			}
		    if(!f.exists()){
		    	f.createNewFile();
		    }
		    FileWriter fw = new FileWriter(f);
		    BufferedWriter out = new BufferedWriter(fw);
		    out.write(str, 0, str.length());
		    out.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyBinFileToSub(InputStream in, String dst)  {
        File destFile = new File(dst);
        try {
			if( !new File(destFile.getParent()).exists() ) Files.createParentDirs(destFile);
			java.nio.file.Files.copy(in, Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public static String getFileContent(InputStream in){	
		BufferedReader br = null;
	    try{	    	
		    String data = "";
		    String jsonString = "";
		    InputStreamReader isr = new InputStreamReader(in, "utf-8");
	    	br = new BufferedReader(isr);  
		    
		    jsonString += data + "\r\n";
		    while( data != null){ 
		        data = br.readLine();
		        if(data != null) jsonString += data + "\r\n";
		    }  		
		    if("".equals(jsonString))
		    	return null;
		    else
		    	return jsonString.trim();
	   }catch(IOException e){
			e.printStackTrace();
			//logger.error(" FileIOException getJSONFromFile - get JSON from the file : "+ targetFile + " IOException");
		}finally{
			if(br != null)
				try{
					br.close();
				}catch(IOException e){;}
		}	    
	    return null;
	} 
	
	public static String getFileContent(String targetFile){	
		BufferedReader br = null;
	    try{	    	
		    String data = "";
		    String jsonString = "";
		    InputStreamReader isr = new InputStreamReader(new FileInputStream(targetFile), "utf-8");
	    	br = new BufferedReader(isr);  
		    data = br.readLine();
		    jsonString += data + "\r\n";
		    while( data != null){ 
		        data = br.readLine();
		        if(data != null) jsonString += data + "\r\n";
		    }  		
		    if("".equals(jsonString))
		    	return null;
		    else
		    	return jsonString.trim();
	   }catch(IOException e){
			//logger.error(" FileIOException getJSONFromFile - get JSON from the file : "+ targetFile + " IOException");
		}finally{
			if(br != null)
				try{
					br.close();
				}catch(IOException e){;}
		}	    
	    return null;
	} 

}
