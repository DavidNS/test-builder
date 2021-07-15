package com.dns.resttestbuilder.configuration;

public class ReservedNames {

	public static final String SEPARATOR = "\\d+";
	
	public  static final String ARRAY_BEGIN_IDENTIFIER="[";

	public static final  String ARRAY_END_IDENTIFIER="]";
	     
	public static final String STEP_IDENTIFIER="STEP_";

	public static final String INPUT_IDENTIFIER="IN_";

	public static final String OUTPUT_IDENTIFIER="OUT";

	public static final String IDENTIFIER_SEPARATOR="\\.";

	public static final String IDENTIFIER_SEPARATOR_NOT_ESCAPED=".";

	public static final String MAP_COMBINATION_SEPARATOR="/AND/";

	public static final String URL_BEGIN_PARAM="<";

	public static final String URL_END_PARAM=">";

	public static final String STEP_IN_REGEXP=STEP_IDENTIFIER + SEPARATOR + IDENTIFIER_SEPARATOR + INPUT_IDENTIFIER + SEPARATOR;
	
	public static final String STEP_OUT_REGEXP=STEP_IDENTIFIER+SEPARATOR+IDENTIFIER_SEPARATOR+OUTPUT_IDENTIFIER;

}
