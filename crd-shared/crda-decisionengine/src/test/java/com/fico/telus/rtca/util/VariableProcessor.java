package com.fico.telus.rtca.util;

public class VariableProcessor {

	private static final String DATE_TIME_VAR = "${VAR-CURRENTDATETIME}";
	
	private static final String[] VAR_LIST = new String[] { DATE_TIME_VAR };
	
	public static String processVariables(String input) {
		String result = input;
		for ( String variable: VAR_LIST ) {
			int index = 0;
			while ( (index=result.indexOf(variable)) > -1 ) {
				String valueToReplace;
				if ( variable.equals( DATE_TIME_VAR ) ) {
					valueToReplace = DateUtil.getCurrentDateTimeAsXmlString();
					result = result.substring( 0, index ) 
							+  valueToReplace
							+ result.substring( index +  variable.length() );
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(processVariables("TEST-BEFOREXXX${VAR-CURRENTDATETIME}XXX END${VAR-CURRENTDATETIME}END2 "));
	}
}
