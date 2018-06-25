package com.BarcodeReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileHandler {
	public static void exportResourceToFile(String resourceName, File file,boolean replace){
		if(!replace && file.exists()){
			return;
		}
		InputStream stream = null;
		FileOutputStream output = null;
        String jarFolder;
        try {
            stream = FileHandler.class.getClassLoader().getResourceAsStream(resourceName);
            output = new FileOutputStream(file);
            byte[] chunk = new byte[1024];
            int size;
            while((size = stream.read(chunk))!=-1){
            	output.write(chunk,0, size);
            }
        }
        catch(IOException e){
        	e.printStackTrace();
        }
        finally{
        	if(output != null){
        		try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(stream != null){
        		try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}
	public static File createTempFileWithExactName(String name,boolean deleteOnExit) throws IOException{
		File f = new File(new File(System.getProperty("java.io.tmpdir")), name);
		if(deleteOnExit){
			f.deleteOnExit();
		}
		return f;
	}
	public static File createTempFile(String name, String ending, boolean deleteOnExit) throws IOException{
		File f = File.createTempFile(name, ending);
		if(deleteOnExit){
			f.deleteOnExit();
		}
		return f;
	}
	public static File exportStaticResourceAsTempFile(String resourceName, String fileName) throws IOException{
		File ret = createTempFileWithExactName(fileName,true);
		exportResourceToFile(resourceName,ret,false);
		return ret;
	}
	public static byte[] readBytesFromResource(String resourceName){
		return readBytesFromStream(FileHandler.class.getClassLoader().getResourceAsStream(resourceName));
	}
	public static byte[] readBytesFromStream(InputStream stream){
		if(stream != null){
			try{
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            byte[] bytes = new byte[1024];
	            int len;
	            while((len = stream.read(bytes))!= -1){
	            	baos.write(bytes, 0,len);
	            }
	            return baos.toByteArray();
			}
			catch(IOException ex){
				
			}
			finally{
				try {
					stream.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
		return null;
	}
}
