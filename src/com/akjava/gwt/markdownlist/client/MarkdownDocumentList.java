package com.akjava.gwt.markdownlist.client;

import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageDataList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MarkdownDocumentList implements EntryPoint {
	private static final String MARKDOWN_DOCUMENT_LIST_DATA="MarkDwonDocumentListData";
	
	@Override
	public void onModuleLoad() {
		
		LogUtils.log("hello1");
		StorageDataList storageDataList=new StorageDataList(new StorageControler(true), MARKDOWN_DOCUMENT_LIST_DATA);
		
		MarkdownDocumentListEditor root=new MarkdownDocumentListEditor(storageDataList);
		root.setStylePrimaryName("margin");
		RootLayoutPanel.get().add(root);
		
		
		LogUtils.log("hello");
		
		
	}
	
	
	/**
	 * TODO remove after finish
	 * add keyword folder
	 * replace image url
	 * @author aki
	 *
	 */
	/*
	private class KeywordConvertFunction implements Function<SimpleTextData,SimpleTextData>{

		@Override
		public SimpleTextData apply(SimpleTextData input) {
			SimpleTextData newData= input.copy();
			if(input.getName().indexOf("/")==-1){//root file
				if(input.getName().indexOf("index")==-1){//not index
					newData.setName("keyword/"+input.getName());
				}
			}
			
			String replaced=newData.getData().replace("www2.akjava.com", "www.akjava.com");
			replaced=replaced.replace("http://www3.akjava.com//img2", "http://www.akjava.com/img2");
			
			newData.setData(replaced);
			
			return newData;
		}
		
	}
	*/
	
}
