/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is the OpenCustomer CRM.
 * 
 * The Initial Developer of the Original Code is Thomas Bader (Bader & Jene
 * Software-Ingenieurbüro). Portions created by the Initial Developer are
 * Copyright (C) 2007 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RFC 4180 
 */
public class CSVFormat {

	private final static String MIME_TYPE = "text/csv";
	
	private final static String DEFAULT_DELIMITER = ",";
	
	private final static String DEFAULT_QUOTE = "\"";
	
    private final static String DEFAULT_LINESEPARATOR = System.getProperty("line.separator", "\n\r");
    
	private String quote;
	
	private String delimiter;
	
	private boolean isAlwaysQuoting;
	
    private String lineSeperator = DEFAULT_LINESEPARATOR;
    
	public CSVFormat() {
		this(DEFAULT_DELIMITER.charAt(0));
	}
	
	public CSVFormat(char delimiter) {
		this(delimiter, false);
	}

	public CSVFormat(char delimiter, boolean isAlwaysQuoting) {
		this.delimiter       = Character.toString(delimiter);
		this.quote           = DEFAULT_QUOTE;
		this.isAlwaysQuoting = isAlwaysQuoting;
	}
	
	public String getDelimiter() {
		return delimiter;
	}

	public boolean isAlwaysQuoting() {
		return isAlwaysQuoting;
	}

	public String getQuote() {
		return quote;
	}

	public String getMimeType() {
		return MIME_TYPE;
	}
	
    public String getLineSeperator() {
        return lineSeperator;
    }

	public String format(List<? extends Object> objects) {
		StringBuilder line = new StringBuilder();
		
		boolean firstElement = true;
		for(Object object : objects) {
			if(firstElement) {
				firstElement = false;
			} else {
				line.append(delimiter);
			}
			line.append(format(object));
		}
		
		return line.toString();
	}
	
	private String format(Object object) {
		String value = "";
		
		if(object != null) {
			value = object.toString();
			if(value.contains(quote)) {
				value = value.replaceAll(quote, quote+quote);
			}
			
			// add quotes
			if(isAlwaysQuoting || value.contains(delimiter) || value.contains(quote) || value.contains("\n")) {
				value = quote + value + quote;
			}
		}
		
		return value;
	}
	
	public List<String> parse(String line) {
		// TODO: reads defect lines too
		
		// ",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))"
		
		String splitPattern = delimiter+"(?=([^"+quote+"]*"+quote+"[^"+quote+"]*"+quote+")*(?![^"+quote+"]*"+quote+"))";
		
		ArrayList<String> elements = new ArrayList<String>();
		
		// get the real number of elements
		int size = 1; 
		Pattern pattern = Pattern.compile(splitPattern);
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()) {
			size++;
		}
		
		// get the values
		for(String element : line.split(splitPattern)) {
			if(element.matches("\".*\"")) {
				elements.add(element.substring(1, element.length()-1).replaceAll("\"\"", "\""));
			} else {
				elements.add(element);	
			}
		}
		
		// add empty cells at the end of line
		for(int i=elements.size(); i<size; i++) {
			elements.add("");
		}
		
		return elements;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
