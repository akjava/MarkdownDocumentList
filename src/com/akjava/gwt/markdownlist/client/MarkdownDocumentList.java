package com.akjava.gwt.markdownlist.client;

import com.akjava.gwt.lib.client.HeaderAndValue;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.markdownlist.client.datalist.TextAreaBasedDataList;
import com.google.common.base.Optional;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MarkdownDocumentList implements EntryPoint {
	private static final String MARKDOWN_DOCUMENT_LIST_DATA="MarkDwonDocumentListData";
	public void onModuleLoad() {
		StorageDataList storageDataList=new StorageDataList(new StorageControler(false), MARKDOWN_DOCUMENT_LIST_DATA);
		
		HorizontalPanel root=new HorizontalPanel();
		RootPanel.get().add(root);
		
		VerticalPanel panel1=new VerticalPanel();
		root.add(panel1);
		
		
		VerticalPanel panel2=new VerticalPanel();
		root.add(panel2);
		TextAreaBasedDataList mlist=new TextAreaBasedDataList(storageDataList);
		panel2.add(mlist.getSimpleDataListWidget());//list-side
		mlist.loadData(Optional.<HeaderAndValue>absent());
		
		TextArea textArea=mlist.getTextArea();
		panel1.add(textArea);
		textArea.setSize("400px", "600px");
		
	}
}
