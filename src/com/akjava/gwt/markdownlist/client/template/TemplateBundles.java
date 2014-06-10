package com.akjava.gwt.markdownlist.client.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface TemplateBundles extends ClientBundle {
	public static TemplateBundles INSTANCE=GWT.create(TemplateBundles.class);
	TextResource test();

}
