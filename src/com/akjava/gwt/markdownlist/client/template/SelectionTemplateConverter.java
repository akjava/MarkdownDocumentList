package com.akjava.gwt.markdownlist.client.template;

import java.util.ArrayList;
import java.util.List;

import com.akjava.lib.common.csv.CSVConverter;
import com.akjava.lib.common.utils.CSVUtils;
import com.google.common.base.Converter;

public class SelectionTemplateConverter extends Converter<List<SelectionTemplate>,String>{

	@Override
	protected String doForward(List<SelectionTemplate> datas) {
		// TODO Auto-generated method stub
		
		//later this is for save
		return null;
	}

	@Override
	protected List<SelectionTemplate> doBackward(String text) {
		List<List<String>> lines=new CSVConverter('\t').convert(CSVUtils.toNLineSeparator(text));
		
		List<SelectionTemplate> datas=new ArrayList<SelectionTemplate>();
		for(List<String> csv:lines){
			SelectionTemplate data=new SelectionTemplate();
			if(csv.get(0).isEmpty()){
				continue;//somehow
			}else{
				data.setBaseText(csv.get(0));//basic
			}
			
			for(int i=1;i<csv.size();i++){
				String labels=csv.get(i);
				String[] vs=labels.split(":");
				List<String> labelValues=new ArrayList<String>();
				for(String v:vs){
					labelValues.add(v);
				}
				
				//data.getSelectedValues().add(labelValues.get(0));
				data.getSelections().add(labelValues);
			}
			datas.add(data);
		}
		
		return datas;
	}

	

}
