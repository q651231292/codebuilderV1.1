package codebuilder.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Temp {
	private StringProperty tempId;
	private StringProperty tempName;

	public Temp(String tempId,String tempName){
		this.tempId = new SimpleStringProperty(tempId);
		this.tempName = new SimpleStringProperty(tempName);

	}

	public StringProperty getTempId() {
		return tempId;
	}

	public StringProperty getTempName() {
		return tempName;
	}

	@Override
	public String toString() {
		return "Temp [tempId=" + tempId + ", tempName=" + tempName + "]";
	}



}
