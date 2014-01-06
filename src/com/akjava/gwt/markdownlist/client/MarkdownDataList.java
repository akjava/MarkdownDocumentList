package com.akjava.gwt.markdownlist.client;

import java.util.List;

import com.akjava.gwt.lib.client.HeaderAndValue;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.markdownlist.client.datalist.SimpleDataListItemControler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextArea;

public class MarkdownDataList extends SimpleDataListItemControler{
private TextArea textArea;
	public void setTextArea(TextArea textArea) {
	this.textArea = textArea;
}

	public TextArea getTextArea() {
	return textArea;
	}

	private void textModified(){
		getSimpleDataListWidget().setModified(true);//simple modified
	}
	
	public MarkdownDataList(StorageDataList dataList) {
		super(dataList);
		textArea=new TextArea();
		textArea.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
		 textArea.addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					
					if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
						textModified();
					}
					else if(event.isControlKeyDown()){//copy or paste
						textModified();
					}
					else{
						textModified();
					}
				}
			});
	}

	@Override
	public HeaderAndValue createSaveData(String fileName) {
		String text=textArea.getText();
		return new HeaderAndValue(-1,fileName,text);
	}

	@Override
	public HeaderAndValue createNewData(String fileName) {
		String text="";
		return new HeaderAndValue(-1,fileName,text);
	}

	//TODO use Optional
	@Override
	public void loadData(HeaderAndValue hv) {
		if(hv==null){//unselect
			textArea.setReadOnly(true);
			textArea.setText("CREATE NEW or SELECT");
		}else{
			textArea.setReadOnly(false);
			textArea.setText(hv.getData());
		}
		//LogUtils.log("loadData");
		
	}

	@Override
	public void exportDatas(List<HeaderAndValue> list) {
		LogUtils.log("exportDatas");
	}

	@Override
	public void importData() {
		LogUtils.log("importData");
	}

	@Override
	public void clearAll() {
		LogUtils.log("clearAll");
	}

	@Override
	public void copy(Object object) {
		LogUtils.log("copy:"+object);
	}

	@Override
	public void paste() {
		LogUtils.log("paste");
	}

	@Override
	public void recoverLastSaved(HeaderAndValue hv) {
		LogUtils.log("recoverLastSaved");
	}

	@Override
	public void restore() {
		LogUtils.log("restore");
	}

	@Override
	public void doDoubleClick(int clientX, int clientY) {
		LogUtils.log("double-click:"+clientX+","+clientY);
	}

}
