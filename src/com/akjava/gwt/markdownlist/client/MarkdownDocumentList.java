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
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MarkdownDocumentList implements EntryPoint {
	private static final String MARKDOWN_DOCUMENT_LIST_DATA="MarkDwonDocumentListData";
	public void onModuleLoad() {
		StorageDataList storageDataList=new StorageDataList(new StorageControler(true), MARKDOWN_DOCUMENT_LIST_DATA);
		
		HorizontalPanel root=new HorizontalPanel();
		RootPanel.get().add(root);
		
		VerticalPanel panel1=new VerticalPanel();
		root.add(panel1);
		
		
		VerticalPanel panel2=new VerticalPanel();
		root.add(panel2);
		final MarkdownEditor editor=new MarkdownEditor();
		final MarkDownDataList mlist=new MarkDownDataList(editor,storageDataList);
		panel2.add(mlist.getSimpleDataListWidget());//list-side
		mlist.loadData(Optional.<SimpleTextData>absent());
		
		
	
		panel1.add(editor);
		mlist.addKeyHandler(editor.getTextArea());
		mlist.setTextArea(editor.getTextArea());
		
		//add sync save action
		editor.getTextArea().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.isControlKeyDown()){
					if(event.getNativeKeyCode()==83){//save
						event.preventDefault();
						mlist.getSimpleDataListWidget().save();
						return;
					}
				}
			}
		});
		mlist.unselect();
		
	
		
		mlist.getSimpleDataListWidget().getOptionButtonPanel().add(new Button("zip download",new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<SimpleTextData> datas=mlist.getDataList().getDataList();
				Collections.sort(datas, new SimpleTextDataComparator(SimpleDataList.ORDER_AZ));
				JSZip zip=JSZip.newJSZip();
				for(SimpleTextData data:datas){
					zip.file(data.getName()+".md", templateData(data,datas),data.getCdate());
				}
				
				String keylinks=createKeywordLinks(datas);
				zip.file("keyword.txt", keylinks);
				
				Blob blob=zip.generateBlob(null);
				Anchor a=new HTML5Download().generateDownloadLink(blob,"application/zip","test.zip","download sample blob",true);
				mlist.getSimpleDataListWidget().getVerticalOptionPanel().add(a);
			}
		}));
	}
	
	public String createKeywordLinks(List<SimpleTextData> datas){
		
		
		
		List<List<String>> lines=FluentIterable.from(datas)
				.transform(new SimpleTextKeywordLinksFunction("/cms/",true,".html"))
				.toList();
		
		List<String> all=Lists.newArrayList();
		for(List<String> data:lines){
			Iterables.addAll(all, data);
		}
		
		return Joiner.on("\n").join(all);
				
		
	}
	
	
	public String templateData(SimpleTextData data,List<SimpleTextData> datas){
		String textData=data.getData();
		if(textData.indexOf("${")!=-1){
			Map<String,String> map=new HashMap<String,String>();
			//do template
			map.put("indexes", createIndexes(data,datas));
			return TemplateUtils.createAdvancedText(textData, map);
		}else{
			return textData;
		}
	}
	
	private String createIndexes(SimpleTextData data,List<SimpleTextData> datas){
		List<String> lines=FluentIterable.from(datas)
		.filter(new SameDirectoryOnly(data.getName(),false))
		.transform(new SimpleTextDataToTitleLinkTextFunction("",false,".html"))
		.transform(new StringToPreFixAndSuffix("- ","")).toList();
		return Joiner.on("\n").join(lines);
	}
}
