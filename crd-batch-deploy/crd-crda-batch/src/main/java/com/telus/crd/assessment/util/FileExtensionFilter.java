package com.telus.crd.assessment.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtensionFilter implements FilenameFilter{
	 private String ext="*";
	   public FileExtensionFilter(String ext){
	     this.ext = ext;
	   }
	   public boolean accept(File dir, String name){
	     if (name !=null && name.indexOf(ext)!=-1)
	       return true;
	     return false;
	   }

}
