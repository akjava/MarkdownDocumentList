package com.akjava.gwt.markdownlist.client;

import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.lib.client.datalist.SimpleTextData;
import com.akjava.gwt.lib.client.datalist.TextAreaBasedDataList;
import com.akjava.gwt.markdowneditor.client.MarkdownEditor;
import com.google.common.base.Optional;

public class MarkDownDataList extends TextAreaBasedDataList{
private MarkdownEditor editor;
	public MarkDownDataList(MarkdownEditor editor,StorageDataList dataList) {
		super(dataList);
		this.editor=editor;
		
		unselectedText="CREATE NEW OR SELECT FROM RIGHT SIDE CONTROLER";
	}
	@Override
	public void loadData(Optional<SimpleTextData> hv) {
		super.loadData(hv);
		/**
		 * why refresh instead of convert.
		 * because for just check the mdtext,converting is annoying.
		 * but in future double and load & convert would be supported
		 */
		editor.getHtmlArea().setText("");//fresh first;
		editor.getPreviewHTML().setHTML("");
		
	}
	
	@Override
	public void onLoad() {
		editor.clearHistory();
		editor.addHistory(editor.getMarkdownText());
	}
	
	@Override
	public void doDoubleClick(int clientX, int clientY) {
		editor.doConvert();
	}

}
