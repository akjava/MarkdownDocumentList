package com.akjava.gwt.markdownlist.client;

import java.util.List;

import com.akjava.gwt.lib.client.HeaderAndValue;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.markdownlist.client.datalist.SimpleDataListItemControler;

public class MarkdownDataList extends SimpleDataListItemControler{

	public MarkdownDataList(StorageDataList dataList) {
		super(dataList);
	}

	@Override
	public HeaderAndValue createSaveData(String fileName) {
		String text="";
		return new HeaderAndValue(-1,fileName,text);
	}

	@Override
	public HeaderAndValue createNewData(String fileName) {
		String text="";
		return new HeaderAndValue(-1,fileName,text);
	}

	@Override
	public void loadData(HeaderAndValue hv) {
		LogUtils.log("loadData");
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
