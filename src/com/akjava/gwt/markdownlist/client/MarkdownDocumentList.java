package com.akjava.gwt.markdownlist.client;

import com.akjava.gwt.lib.client.HeaderAndValue;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.lib.client.datalist.TextAreaBasedDataList;
import com.akjava.gwt.markdowneditor.client.MarkdownEditor;
import com.google.common.base.Optional;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
		final TextAreaBasedDataList mlist=new TextAreaBasedDataList(storageDataList);
		panel2.add(mlist.getSimpleDataListWidget());//list-side
		mlist.loadData(Optional.<HeaderAndValue>absent());
		
		
		final MarkdownEditor editor=new MarkdownEditor();
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
	}
}
