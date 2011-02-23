package com.ocpsoft.pretty.faces.util;

/**
 * 
 * Helper class providing similar functions like the
 * StringUtils class of Apache Commons.
 * 
 * @author Christian Kaltepoth
 *
 */
public class StringUtils
{
   
   public static boolean isBlank(String s) {
      return s == null || s.trim().length() == 0;
   }
   
   public static boolean isNotBlank(String s) {
      return s != null && s.trim().length() > 0;
   }

}
