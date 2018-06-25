package com.BarcodeReader;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ghost4j.Ghostscript;
import org.ghost4j.GhostscriptException;

public class PostScriptReader {
	//*********************** enviroment variables ****************************
	public static String DEFAULT_DEVICE = "png16m";
	public static String APPENDERS = "\n%%EndComments\n%%EOF";
	public static String[] DEFAULT_PARAMETERS_PCL = {
								//"-dQUIET",
								//********************** devices used to test **********************
					            "-sDEVICE=pxlcolor",
					            //"-sDEVICE=pxlmono",
					           // "-sDEVICE=ljet4",
					           //"=sDevice=png16",
					            //"-sDEVICE=pxlmono",
					            "-dBATCH",
					            "-sPAPERSIZE=a2",
					            "-r300",
					            "-dNOPAUSE",
					            "-dNOPROMPT",
					            "-dLOCALFONTS",};
	public static String[] DEFAULT_PARAMETERS_PDF = {
										//"-dQUIET",
										//********************** devices used to test **********************
							            //"-sDEVICE=pxlcolor",
							            //"-sDEVICE=pxlmono",
							            //"-sDEVICE=ljet4",
							           //"=sDevice=png16",
							            "-sDEVICE=pdfwrite",
							            "-dBATCH",
							            "-r300",
							            "-dNOPAUSE",
							            "-dNOPROMPT",
							            "-dLOCALFONTS",};
	// *************** BarCode******************
	private static final String BARCODE_WRITER;
	private static final Ghostscript instance;
	private static String device = DEFAULT_DEVICE;
	
	static{
		//getResourceSetup
		BARCODE_WRITER = new String(FileHandler.readBytesFromResource("postscript/barcode.ps"));
		instance = Ghostscript.getInstance();
        org.apache.log4j.BasicConfigurator.resetConfiguration();
        Logger.getRootLogger().setLevel(Level.OFF);
		try {
			FileHandler.exportStaticResourceAsTempFile("natives/gsdll64.dll", "gsdll64.dll");
			File tempFile = FileHandler.exportStaticResourceAsTempFile("natives/gsdll32.dll", "gsdll32.dll");
			System.setProperty("jna.library.path", tempFile.getParent());

			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}/*
	public static String createCode128BarCodeInPCL(String data,String barcodeParameters, int x, int y, double scaleX,double scaleY) throws IOException{
		return createCustomBarcodeInPCL(data,barcodeParameters,"code128",x,y,scaleX,scaleY);
	}
	public static String createCode39BarCodeInPCL(String data,String barcodeParameters, int x, int y, double scaleX,double scaleY) throws IOException{
		return createCustomBarcodeInPCL(data,barcodeParameters,"code39",x,y,scaleX,scaleY);
	}
	public static String createCustomBarcodeInPCL(String data,String barcodeParameters, String barcode, int x, int y, double scaleX,double scaleY) throws IOException{
		String[] inputs = {
			x+" "+y+" moveto",
			scaleX+" "+scaleY+ " scale",
			"("+data+") ("+barcodeParameters+") /" + barcode + " /uk.co.terryburton.bwipp findresource exec"
		};
		File temp = FileHandler.createTempFile("ghostscript", ".PCL", true);
		try {
			GenericWrite(inputs,DEFAULT_PARAMETERS,temp);
			InputStream is = new FileInputStream(temp);
			return new String(FileHandler.readBytesFromStream(is));
		} catch (GhostscriptException e) {
			throw new IOException(e);
		}
		finally{
			temp.delete();
			temp.deleteOnExit();
		}
	}
	*/
	public static void createCustomBarCodeInPDFIntoFile(File location, String barcode,String data,String barcodeParameters, int x, int y, double scaleX,double scaleY) throws IOException{
		String[] inputs = {
				x+" "+y+" moveto",
				scaleX+" "+scaleY+ " scale",
				"("+data+") ("+barcodeParameters+") /" + barcode + " /uk.co.terryburton.bwipp findresource exec"
			};
			
			try {
				GenericWrite(inputs,DEFAULT_PARAMETERS_PDF,location);
			} catch (GhostscriptException e) {
				throw new IOException(e);
			}
	}
	public static void createCustomBarCodeInPCLIntoFile(File location, String barcode,String data,String barcodeParameters, int x, int y, double scaleX,double scaleY) throws IOException{
		String[] inputs = {
			x+" "+y+" moveto",
			scaleX+" "+scaleY+ " scale",
			"("+data+") ("+barcodeParameters+") /" + barcode + " /uk.co.terryburton.bwipp findresource exec"
		};
		
		try {
			GenericWrite(inputs,DEFAULT_PARAMETERS_PCL,location);
		} catch (GhostscriptException e) {
			throw new IOException(e);
		}
	}
	public static byte[] createCustomBarcodeInPDFAsBytes(String data,String barcodeParameters, String barcode, int x, int y, double scaleX,double scaleY) throws IOException{
		String[] inputs = {
				x+" "+y+" moveto",
				scaleX+" "+scaleY+ " scale",
				"("+data+") ("+barcodeParameters+") /" + barcode + " /uk.co.terryburton.bwipp findresource exec"
			};
			File temp = FileHandler.createTempFile("ghostscript", ".PCL", true);
			try {
				GenericWrite(inputs,DEFAULT_PARAMETERS_PDF,temp);
				InputStream is = new FileInputStream(temp);
				return FileHandler.readBytesFromStream(is);
			} catch (GhostscriptException e) {
				throw new IOException(e);
			}
			finally{
				temp.delete();
				temp.deleteOnExit();
			}
		}
	public static byte[] createCustomBarcodeInPCLAsBytes(String data,String barcodeParameters, String barcode, int x, int y, double scaleX,double scaleY) throws IOException{
		String[] inputs = {
			x+" "+y+" moveto",
			scaleX+" "+scaleY+ " scale",
			"("+data+") ("+barcodeParameters+") /" + barcode + " /uk.co.terryburton.bwipp findresource exec"
		};
		File temp = FileHandler.createTempFile("ghostscript", ".PCL", true);
		try {
			GenericWrite(inputs,DEFAULT_PARAMETERS_PCL,temp);
			InputStream is = new FileInputStream(temp);
			return FileHandler.readBytesFromStream(is);
		} catch (GhostscriptException e) {
			throw new IOException(e);
		}
		finally{
			temp.delete();
			temp.deleteOnExit();
		}
	}
	public static void GenericWrite(String[] inputs, String[] parameters,File output) throws GhostscriptException{
		parameters = merge(new String[]{"-dQUIET","-sDEVICE="+device,},parameters,new String[]{"-sOutputFile="+output.getAbsolutePath()});
		String execute = "";
		for(String input:inputs){
			execute += input+"\n";
		}
		//"%%EndData"
		execute = replaceLast(BARCODE_WRITER,"%%EndData","\n"+ execute+"\nshowpage\n%%EndData");
		execute+= APPENDERS;
		instance.initialize(parameters);
		instance.runString(execute);
		
		instance.exit();
		instance.deleteInstance();
	}
	public static String[] merge(String[]... mergables){
		ArrayList<String> ret = new ArrayList<>();
		for(String[] elements:mergables){
			for(String ele:elements){
				ret.add(ele);
			}
		}
		return ret.toArray(new String[ret.size()]);
	}
	public static void test() throws IOException{
		main();
	}
	private static String replaceLast(String string, String substring, String replacement)
	{	
		int index = string.lastIndexOf(substring);
		if (index == -1){
			return string;
		}
		return string.substring(0, index) + replacement
				+ string.substring(index+substring.length());
	}
	/**
	 * TEST SYSTEM
	 * @param strings
	 * @throws IOException
	 */
	public static void main(String...strings) throws IOException{
       // org.apache.log4j.BasicConfigurator.resetConfiguration();
        //Logger.getRootLogger().setLevel(Level.OFF);
		//String text = createCode128BarCodeInPCL("CODE 39","includecheck includetext textfont=Times-Roman textsize=9",1,1,1,1);
		PostScriptReader.createCustomBarCodeInPCLIntoFile(new File("C:\\Temp\\test.pcl"),"code128","code128","includecheck includetext textfont=Times-Roman textsize=9",10,10,1,1);
	}
}
