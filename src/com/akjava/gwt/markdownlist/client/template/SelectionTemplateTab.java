package com.akjava.gwt.markdownlist.client.template;

import java.util.ArrayList;
import java.util.List;

import javax.swing.border.StrokeBorder;

import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.lib.client.StorageControler;
import com.akjava.gwt.lib.client.StorageException;
import com.akjava.gwt.lib.client.TextSelection;
import com.akjava.gwt.markdowneditor.client.MarkdownEditor;
import com.akjava.lib.common.utils.ValuesUtils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectionTemplateTab extends VerticalPanel {

	public static final String KEY_SELECTION_TEMPLATE_VALUE="markdownlist_selection_template_value";
	
	private VerticalPanel container;

	private MarkdownEditor editor;
	private StorageControler storageControler=new StorageControler();

	private TextArea editArea;
	public SelectionTemplateTab(MarkdownEditor editor){
		this.editor=editor;
		this.setWidth("100%");
		TabLayoutPanel tab=new TabLayoutPanel(30, Unit.PX);
		tab.setSize("100%","500px");
		add(tab);
		
		ScrollPanel scroll=new ScrollPanel();
		tab.add(scroll,"List");
		scroll.setSize("100%", "500px");
		container = new VerticalPanel();
		scroll.add(container);
		
		
		//test
		
		
		//editor area
		VerticalPanel editors=new VerticalPanel();
		editors.setWidth("100%");
		editArea = new TextArea();
		editors.add(editArea);
		editArea.setSize("100%", "400px");
		HorizontalPanel buttons=new HorizontalPanel();
		Button update=new Button("Update",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				try {
					storageControler.setValue(KEY_SELECTION_TEMPLATE_VALUE, editArea.getText());
				} catch (StorageException e) {
					Window.alert(e.getMessage());
				}
				
				loadData();
			}
		});
		buttons.add(update);
		editors.add(buttons);
		tab.add(editors,"Edit");
		tab.selectTab(0);
		
		loadData();
	}
	
	public void loadData(){
		
		String text=storageControler.getValue(KEY_SELECTION_TEMPLATE_VALUE, TemplateBundles.INSTANCE.test().getText());
		SelectionTemplateConverter converter=new SelectionTemplateConverter();
		List<SelectionTemplate> templates=converter.reverse().convert(text);
		
		editArea.setText(text);
		setTemplates(templates);
	}
	
	public void setTemplates(List<SelectionTemplate> templates){
		container.clear();
		for(SelectionTemplate template:templates){
			LogUtils.log(template);
		}
		List<SelectionTemplateWidget> widgets=FluentIterable.from(templates).transform(new WidgetFunction()).toList();
		for(SelectionTemplateWidget widget:widgets){
			container.add(widget);
		}
	}
	
	public class WidgetFunction implements Function<SelectionTemplate,SelectionTemplateWidget>{
		@Override
		public SelectionTemplateWidget apply(SelectionTemplate input) {
			return new SelectionTemplateWidget(input);
		}
	}
	
	
	public class SelectionTemplateWidget extends HorizontalPanel{
		List<ListBox> lists=new ArrayList<ListBox>();
		private List<String> texts;
		public SelectionTemplateWidget(SelectionTemplate template){
			this.setVerticalAlignment(ALIGN_MIDDLE);
			
			
			Button debug=new Button("Update",new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					for(TextSelection selection:editor.getTextSelection().asSet()){
						String text=selection.getSelection();
						if(text.isEmpty()){//no selection,line-mode
							
							
							String line=selection.getCurrentLine().getLineEndRemovedSelection();
							selection.getCurrentLine().replaceInLine(getTemplatedText(line));
							
							/*
							String end=line.substring(line.length()-1);//must be "\n"
							if(end.equals("\n")){
								String newText=getTemplatedText(line.substring(0,line.length()-1));
								selection.getCurrentLine().replace(newText+end);
							}else{
								String newText=getTemplatedText(line);
								selection.getCurrentLine().replace(newText);
							}
							*/
							//LogUtils.log("end:"+(int)end.charAt(0));
							
						}else{
							String newText=getTemplatedText(text);
							selection.replace(newText);
						}
						editor.onTextAreaUpdate();
					}
					
				}
			});
			add(debug);
			
			
			texts=new ArrayList<String>();
			String base=template.getBaseText();
			int index;
			do {
				index=base.indexOf("$");
				if(index==-1){
					texts.add(base);//add remaining
				}else{
					String sub=base.substring(index+1);
					if(sub.startsWith("value")){
						
						String before=base.substring(0,index);
						if(!before.isEmpty()){
						texts.add(before);
						}
						texts.add(base.substring(index,index+"value".length()+1));//add value
						base=base.substring(index+"value".length()+1);
					}else if(sub.startsWith("selection")){
						int selectionLength="selection".length();
						int end=selectionLength-1;
						
						for(int i=end+1;i<sub.length();i++){
							if(Character.isDigit(sub.charAt(i))){
								end=i;
							}else{
								LogUtils.log(i+":"+sub.charAt(i));
								break;
							}
						}
						
						if(end==selectionLength-1){//not contain number;
							texts.add(base.substring(0,index+selectionLength+1));
							base=sub.substring(selectionLength);
						}else{
							texts.add(base.substring(0,index));//add before number
							texts.add("$"+sub.substring(0,end+1));//add $selection{number}
							base=sub.substring(end+1);
						}
						
					}else{
						LogUtils.log("invalid  $ case:"+sub);
						texts.add(base.substring(0,index+1));
						base=base.substring(index+1);
					}
					
				}
			} while (index!=-1);
			
			//base text
			LogUtils.log(Joiner.on(",").join(texts));
			
			for(String text:texts){
				if(text.equals("$value")){
					TextBox fakeBox=new TextBox();
					fakeBox.setReadOnly(true);
					fakeBox.setWidth("50px");
					add(fakeBox);
				}else if(text.startsWith("$selection")){
					String remain=text.substring("$selection".length());
					int number=ValuesUtils.toInt(remain, 0);
					if(number!=0){
						LogUtils.log("number:"+number +" of "+template.getSelections().size());
						//create ListBox
						List<String> vs=template.getSelections().get(number-1);
						ListBox listBox=new ListBox();
						for(String option:vs){
							listBox.addItem(option);
						}
						listBox.setName(text);
						lists.add(listBox);
						add(listBox);
					}else{
						add(new Label(text));
					}
				}else{
					add(new Label(text));
				}
			}
			
			
		}
		public String getTemplatedText(String value){
			String result="";
			for(String text:texts){
			if(text.equals("$value")){
				result+=value;
			}else if(text.startsWith("$selection")){
				boolean find=false;
				for(int i=0;i<lists.size();i++){
					if(lists.get(i).getName().equals(text)){
						result+=lists.get(i).getItemText(lists.get(i).getSelectedIndex());
						find=true;
						break;
					}
				}
				
				if(!find){
					result+=text;
				}
				
			}else{
				result+=text;
			}
			
			}
			return result;
		}
	}
	
	
}
