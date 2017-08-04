package codebuilder.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import codebuilder.App;
import codebuilder.model.Global;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;

public class IndexController {

	@FXML
	private Button diyTemp;
	@FXML
	private Button createCode;

	@FXML
	public void diyTempTo(ActionEvent event) {
		App.SceneReplacer("/fxml/temp/TempList.fxml");
	}
	@FXML
	public void createCodeTo(ActionEvent event) {
		App.SceneReplacer("/fxml/code/TempSelect.fxml");
	}
}
