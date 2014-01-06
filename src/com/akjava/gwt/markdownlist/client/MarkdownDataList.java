package com.akjava.gwt.markdownlist.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.akjava.gwt.html5.client.download.HTML5Download;
import com.akjava.gwt.html5.client.file.File;
import com.akjava.gwt.html5.client.file.FileUploadForm;
import com.akjava.gwt.html5.client.file.FileUtils;
import com.akjava.gwt.html5.client.file.FileUtils.DataURLListener;
import com.akjava.gwt.lib.client.HeaderAndValue;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.lib.client.widget.TabInputableTextArea;
import com.akjava.gwt.markdownlist.client.datalist.AbstractContextMenu;
import com.akjava.gwt.markdownlist.client.datalist.SimpleDataListItemControler;
import com.akjava.lib.common.csv.CSVReader;
import com.akjava.lib.common.utils.CSVUtils;
import com.akjava.lib.common.utils.ValuesUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextArea;

/**
 * TODO create this as simple TextAreaDataList
 * @author aki
 *
 */
public class MarkdownDataList extends SimpleDataListItemControler{
private TextArea textArea;
private HeaderAndValue copiedValue;
private HorizontalPanel uploadPanel;
private FileUploadForm uploadForm;
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
		textArea=new TabInputableTextArea();
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
		 
		 
		 getSimpleDataListWidget().setCellContextMenu(new TestContextMenu());
		 
	}

	/**
	 * This context menu is very primitive,so can't modified after create.used globally
	 * TODO 
	 * @author aki
	 *
	 */
	public class TestContextMenu extends AbstractContextMenu{
		@Override
		public void createMenus(MenuBar menuBar) {
			Command test1Command = new Command() {
				  public void execute() {
					LogUtils.log(getCurrentName());  
				    hide(); 
				  }
			};
			
			menuBar.addItem("Test", test1Command);
			
		}
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
	
	private String getKeyName(){
		return "datas";
	}

	@Override
	public void exportDatas(List<HeaderAndValue> list) {
		String exportText=generateExportText(list);
		Anchor anchor=new HTML5Download().generateTextDownloadLink(exportText,getKeyName()+".txt","Download data",true);
		getSimpleDataListWidget().add(anchor);
	}
	
	private String generateExportText(List<HeaderAndValue> list){
		List<String> lines=FluentIterable.from(list).transform(new HeadAndValueToCsvFunction()).toList();
		String exportText=Joiner.on("\r\n").join(lines);
		return exportText;
	}
	
	public String generateExportText(){
		List<HeaderAndValue> list=this.getDataList().getDataList();
		return generateExportText(list);
	}

	@Override
	public void importData() {
		if(getDataList().getDataList().size()>0){
		boolean confirm=Window.confirm("Import datas:add current datas,if you prefer replace all.ClearAll first and do it again");
		if(!confirm){
			return;
		}
		}
		
		uploadForm = FileUtils.createSingleTextFileUploadForm(new DataURLListener() {
			@Override
			public void uploaded(File file, String value) {
				List<HeaderAndValue> list=textToHeaderAndValue(value);
				for(HeaderAndValue hv:list){
					getDataList().addData(hv.getHeader(), hv.getData());
				}
				
				uploadPanel.removeFromParent();
				updateList();
			}
		}, false);
		
		
		createFormPanel("Import Datas:",uploadForm);
		getSimpleDataListWidget().add(uploadPanel);
	}

	@Override
	public void clearAll() {
		boolean confirm=Window.confirm("Clear all data?this operation can not undo\nyou should ExportAll first.");
		if(!confirm){
			return;
		}
		List<HeaderAndValue> hvs= getDataList().getDataList();
		for(HeaderAndValue hv:hvs){
			getDataList().clearData(hv.getId());
		}
		getDataList().setCurrentId(0);//restart id
		
		getSimpleDataListWidget().unselect();
		updateList();
		
	}

	@Override
	public void copy(Object object) {
		copiedValue=(HeaderAndValue) object;
	}

	@Override
	public void paste() {
		if(copiedValue!=null){
		getDataList().addData(copiedValue.getHeader()+" copy", copiedValue.getData());
		updateList();
		}
	}

	@Override
	public void recoverLastSaved(HeaderAndValue hv) {
		copy(hv);
		paste();
	}

	@Override
	public void restore() {
		LogUtils.log("restore");
	}

	@Override
	public void doDoubleClick(int clientX, int clientY) {
		LogUtils.log("double-click:"+clientX+","+clientY);
	}
	
	private void createFormPanel(String text,FileUploadForm form){
		if(uploadPanel==null){
		uploadPanel=new HorizontalPanel();
		uploadPanel.add(new Label(text));
		uploadPanel.add(form);
		Button bt=new Button("close",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				uploadPanel.setVisible(false);
			}
		});
		uploadPanel.add(bt);
		}else{
			uploadPanel.setVisible(true);
		}
	}

	//TODO move somewhere
	public static List<HeaderAndValue> textToHeaderAndValue(String text){
		//List<HeaderAndValue> list=new ArrayList<HeaderAndValue>();
		CSVReader reader=new CSVReader(text,'\t','"',true);
		try {
			List<String[]> csvs=reader.readAll();
			return FluentIterable.from(csvs).transform(new CsvArrayToHeadAndValueFunction()).toList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Window.alert(e.getMessage());
		}
		return null;
	}
}
