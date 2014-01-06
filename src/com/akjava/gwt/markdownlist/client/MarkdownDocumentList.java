package com.akjava.gwt.markdownlist.client;

import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageDataList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
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
		
		MarkdownDataList mlist=new MarkdownDataList(storageDataList);
		panel1.add(mlist.getSimpleDataListWidget());//list-side
	}
}
