package com.akjava.gwt.markdownlist.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.html5.client.download.HTML5Download;
import com.akjava.gwt.html5.client.file.Blob;
import com.akjava.gwt.html5.client.file.File;
import com.akjava.gwt.html5.client.file.FileUploadForm;
import com.akjava.gwt.html5.client.file.FileUtils;
import com.akjava.gwt.html5.client.file.FileUtils.DataURLListener;
import com.akjava.gwt.jszip.client.JSZip;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageDataList;
import com.akjava.gwt.lib.client.datalist.SimpleDataList;
import com.akjava.gwt.lib.client.datalist.SimpleDataList.SimpleTextDataComparator;
import com.akjava.gwt.lib.client.datalist.SimpleTextData;
import com.akjava.gwt.lib.client.datalist.SimpleTextDataPredicates.SameDirectoryOnly;
import com.akjava.gwt.markdowneditor.client.ExtractTextFromMarkdown;
import com.akjava.gwt.markdowneditor.client.ExtractTextFromMarkdown.ExtractedResult;
import com.akjava.gwt.markdowneditor.client.MarkdownEditor;
import com.akjava.gwt.markdowneditor.client.MarkdownFunctions.SimpleTextDataToTitleLinkTextFunction;
import com.akjava.gwt.markdowneditor.client.MarkdownFunctions.SimpleTextKeywordLinksFunction;
import com.akjava.gwt.markdownlist.client.template.SelectionTemplateTab;
import com.akjava.lib.common.functions.StringFunctions.StringToPreFixAndSuffix;
import com.akjava.lib.common.utils.FileNames;
import com.akjava.lib.common.utils.TemplateUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MarkdownDocumentListEditor extends DockLayoutPanel{
	private MarkDownDataList markdownDataList;
	public MarkDownDataList getMarkdownDataList() {
		return markdownDataList;
	}

	public MarkdownDocumentListEditor(StorageDataList storageDataList) {
		super(Unit.PX);
		

		final MarkdownEditor editor=new MarkdownEditor();
		markdownDataList = new MarkDownDataList(editor,storageDataList);
		this.addEast(markdownDataList.getSimpleDataListWidget(),400);//list-side
		markdownDataList.loadData(Optional.<SimpleTextData>absent());
		
		
	
		this.add(editor);
		markdownDataList.addKeyHandler(editor.getTextArea());
		markdownDataList.setTextArea(editor.getTextArea());
		
		//add sync save action
		editor.getTextArea().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.isControlKeyDown()){
					if(event.getNativeKeyCode()==83){//save
						event.preventDefault();
						markdownDataList.getSimpleDataListWidget().save();
						return;
					}else{
						
						
					}
				}
			}
		});
		markdownDataList.unselect();
		
		
		VerticalPanel stripPanel=new VerticalPanel();
		stripPanel.setWidth("100%");
		createStripPanel(stripPanel);
		editor.getRightTabPanel().add(stripPanel,"Extract Text");
		
		SelectionTemplateTab templateTab=new SelectionTemplateTab(editor);
		templateTab.setHeight("100%");
		editor.getRightTabPanel().add(templateTab,"Template Text");
	
		
		markdownDataList.getSimpleDataListWidget().getOptionButtonPanel().add(new Button("zip download",new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<SimpleTextData> datas=getDatas();
				Collections.sort(datas, new SimpleTextDataComparator(SimpleDataList.ORDER_AZ));
				
				//rename tamporaly
				
				
				JSZip zip=JSZip.newJSZip();
				for(SimpleTextData data:datas){
					zip.file(data.getName()+".md", templateData(data,datas),data.getCdate());
				}
				
				String keylinks=createKeywordLinks(datas);
				zip.file("keyword.txt", keylinks);
				
				Blob blob=zip.generateBlob(null);
				Anchor a=new HTML5Download().generateDownloadLink(blob,"application/zip","test.zip","download sample blob",true);
				markdownDataList.getSimpleDataListWidget().getVerticalOptionPanel().add(a);
			}
		}));
		
		  Window.addWindowClosingHandler(new Window.ClosingHandler() {
		        @Override
		        public void onWindowClosing(ClosingEvent event) {
		        	if(markdownDataList.getSimpleDataListWidget().getSelection()!=null){
		        		if(markdownDataList.getSimpleDataListWidget().getSelection().isModified()){
		        			 event.setMessage("Not saved data exist.Do you want close This App?");
		        		}
		        	}
		           
		            
		        }
		    });

		    Window.addCloseHandler(new CloseHandler<Window>() {
		        @Override
		        public void onClose(CloseEvent<Window> event) {
		           
		        }       
		    });
	}
	
	private ExtractTextFromMarkdown extractTextFromMarkdown=new ExtractTextFromMarkdown();
	private TextArea extractArea2;
	private TextArea extractArea3;
	private TextArea extractArea;
	public void createStripPanel(VerticalPanel panel){
		extractArea = new TextArea();
		extractArea.setSize("98%", "200px");
	
		extractArea2 = new TextArea();
		extractArea2.setSize("98%", "200px");
		
		extractArea3 = new TextArea();
		extractArea3.setSize("98%", "200px");
		extractArea3.setReadOnly(true);
		
		HorizontalPanel topButtons=new HorizontalPanel();
		topButtons.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		
		Button update=new Button("Extract to template & properties",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String text=markdownDataList.getTextArea().getText();
				ExtractedResult extractedResult=extractTextFromMarkdown.extract(text,true);
				extractArea.setText(extractedResult.getExtractedMarkdownTemplateText());
				
				String maps=Joiner.on("\n").withKeyValueSeparator("=").join(extractedResult.getMarkdownTemplateMap());
				extractArea2.setText(maps);
			}
		});
		topButtons.add(update);
		
		
		panel.add(topButtons);
		panel.add(extractArea);
		
		HorizontalPanel secondButtons=new HorizontalPanel();
		secondButtons.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		panel.add(secondButtons);
		
		
		secondButtons.add(new Label("Load Properties"));
		FileUploadForm upload=FileUtils.createSingleTextFileUploadForm(new DataURLListener() {
			@Override
			public void uploaded(File file, String text) {
				extractArea2.setText(text);
				
				doExtractRemix();
			}
		}, true, "UTF-8");
		secondButtons.add(upload);
		
		
		final HorizontalPanel links=new HorizontalPanel();
		
		Button download=new Button("Download as properties",new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				links.clear();
				String pName=markdownDataList.getCurrentName()+".properties";
				LogUtils.log(pName);
				Anchor anchor=HTML5Download.get().generateTextDownloadLink(extractArea2.getText(), pName, "click here to download property file",true);
				links.add(anchor);
			}
		});
		secondButtons.add(download);
		secondButtons.add(links);
		
		
		panel.add(extractArea2);
		
		HorizontalPanel thirdButtons=new HorizontalPanel();
		thirdButtons.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		
		

		final Button copy=new Button("Add to List>>",new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String title=Window.prompt("new file name", markdownDataList.getCurrentName());
				if(title==null){
					return;//cancelled
				}
				markdownDataList.getDataList().addData(title, extractArea3.getText());
				markdownDataList.updateList();
			}
		});
		copy.setEnabled(false);
		
		Button mix=new Button("ReMix template & properties",new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doExtractRemix();
				copy.setEnabled(true);
			}
		});
		thirdButtons.add(mix);
		
		
		panel.add(thirdButtons);
		panel.add(extractArea3);
		
		thirdButtons.add(copy);
		
	}
	
	private void doExtractRemix(){
		String text=extractArea2.getText();
		String[] lines=text.split("\n");
		Map<String,String> keyMap=new HashMap<String, String>();
		for(String line:lines){
			int index=line.indexOf("=");
			if(index!=-1){
				String key=line.substring(0,index).trim();
				String value=line.substring(index+1).trim();
				keyMap.put(key, value);
			}
		}
		
		//do merge
		String mixed=TemplateUtils.createText(extractArea.getText(), keyMap);
		extractArea3.setText(mixed);
		
		extractArea3.selectAll();
	}
	
	
	public void addData(String fileName,String markdown){
		SimpleTextData data=new SimpleTextData(-1, fileName, markdown);
		String cdate=System.currentTimeMillis()+",";
		markdownDataList.getDataList().addData(fileName,cdate+markdown);
		markdownDataList.updateList();
	}
	

	private List<SimpleTextData> getDatas(){
		return markdownDataList.getDataList().getDataList();
		//return Lists.newArrayList(FluentIterable.from(mlist.getDataList().getDataList()).transform(new KeywordConvertFunction()));
	}
	

	public String createKeywordLinks(List<SimpleTextData> datas){
		
		
		//TODO set path
		List<List<String>> lines=FluentIterable.from(datas)
				.transform(new SimpleTextKeywordLinksFunction("/",true,".html"))//TODO remove cms,this is temporaly
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
