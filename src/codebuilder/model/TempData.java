package codebuilder.model;

import javafx.beans.property.StringProperty;

public class TempData {

	private StringProperty tempDataId;
	private StringProperty tempId;
	private StringProperty label;
	private StringProperty value;

	public TempData(StringProperty tempDataId, StringProperty tempId, StringProperty label, StringProperty value) {
		super();
		this.tempDataId = tempDataId;
		this.tempId = tempId;
		this.label = label;
		this.value = value;
	}

	public StringProperty getTempDataId() {
		return tempDataId;
	}

	public StringProperty getTempId() {
		return tempId;
	}

	public StringProperty getLabel() {
		return label;
	}

	public StringProperty getValue() {
		return value;
	}

	public void setTempDataId(StringProperty tempDataId) {
		this.tempDataId = tempDataId;
	}

	public void setTempId(StringProperty tempId) {
		this.tempId = tempId;
	}

	public void setLabel(StringProperty label) {
		this.label = label;
	}

	public void setValue(StringProperty value) {
		this.value = value;
	}




}
