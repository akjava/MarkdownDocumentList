package com.akjava.gwt.markdownlist.client.template;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class SelectionTemplate {
private String baseText;
public String getBaseText() {
	return baseText;
}

public void setBaseText(String baseText) {
	this.baseText = baseText;
}

public List<List<String>> getSelections() {
	return selections;
}

public void setSelections(List<List<String>> selections) {
	this.selections = selections;
}



private List<List<String>> selections=Lists.newArrayList();


public String toString(){
	List<String> results=new ArrayList<String>();
	results.add(baseText);
	

	for(List<String> list:selections){
		results.add(Joiner.on(":").join(list));
	}
	
	return Joiner.on(",").join(results);
}



}
