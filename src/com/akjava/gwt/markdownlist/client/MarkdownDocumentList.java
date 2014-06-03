package com.akjava.gwt.markdownlist.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.html5.client.download.HTML5Download;
import com.akjava.gwt.html5.client.file.Blob;
import com.akjava.gwt.jszip.client.JSZip;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.lib.client.datalist.SimpleDataList;
import com.akjava.gwt.lib.client.datalist.SimpleDataList.SimpleTextDataComparator;
import com.akjava.gwt.lib.client.datalist.SimpleTextData;
import com.akjava.gwt.lib.client.datalist.SimpleTextDataPredicates.SameDirectoryOnly;
import com.akjava.gwt.markdowneditor.client.MarkdownEditor;
import com.akjava.gwt.markdowneditor.client.MarkdownFunctions.SimpleTextDataToTitleLinkTextFunction;
import com.akjava.gwt.markdowneditor.client.MarkdownFunctions.SimpleTextKeywordLinksFunction;
import com.akjava.lib.common.functions.StringFunctions.StringToPreFixAndSuffix;
import com.akjava.lib.common.utils.TemplateUtils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MarkdownDocumentList implements EntryPoint {
	private static final String MARKDOWN_DOCUMENT_LIST_DATA="MarkDwonDocumentListData";
	
	public void onModuleLoad() {
		StorageDataList storageDataList=new StorageDataList(new StorageControler(true), MARKDOWN_DOCUMENT_LIST_DATA);
		
		MarkdownDocumentListEditor root=new MarkdownDocumentListEditor(storageDataList);
		root.setStylePrimaryName("margin");
		RootLayoutPanel.get().add(root);
		
		
		
		
		
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
