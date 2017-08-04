package codebuilder.controller.temp;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import codebuilder.App;
import codebuilder.dao.Dao;
import codebuilder.dao.impl.DerbyDao;
import codebuilder.model.Global;
import codebuilder.model.Temp;
import codebuilder.util.AlertTool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class TempListController implements Initializable{

	@FXML
	TableView<Temp> tempList;
	@FXML
	TableColumn<Temp, String> tempCol;
	@FXML
	Label title;
	@FXML
	Button addBtn;
	@FXML
	Button delBtn;
	@FXML
	Button modBtn;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Dao jdbc = new DerbyDao();
		List<Map<String,String>> temps = jdbc.query("select * from temp ");
		ObservableList<Temp> tempObList = FXCollections.observableArrayList();
		for (Map<String,String> temp : temps) {
			tempObList.add(new Temp(temp.get("TEMP_ID"),temp.get("TEMP_NAME")));
		}
		tempCol.setCellValueFactory(cellData -> cellData.getValue().getTempName());
		tempList.setItems(tempObList);
	}
	@FXML
	public void addTempTo(ActionEvent event) {
		App.SceneReplacer("/fxml/temp/TempAdd.fxml");
	}
	@FXML
	public void delTemp(ActionEvent event) {
		int isSuccess = 1;

        Temp t = tempList.getSelectionModel().getSelectedItem();
        String tempId = t.getTempId().getValue();
        Dao dao = new DerbyDao();
        isSuccess = dao.del("delete from temp_data where temp_id='"+tempId+"'");
        isSuccess = dao.del("delete from temp where temp_id='"+tempId+"'");
        if(isSuccess==1){
        	AlertTool.successAlert();
        	App.SceneReplacer("/fxml/temp/TempList.fxml");
        }else{
        	AlertTool.errorAlert();
        }
	}
	@FXML
	public void modTempTo(ActionEvent event) {
        Temp t = tempList.getSelectionModel().getSelectedItem();
        String tempId = t.getTempId().getValue();
        Global.id = tempId;
        App.SceneReplacer("/fxml/temp/TempAdd.fxml");
	}
	@FXML
	public void back(ActionEvent event) {
		App.SceneReplacer("/fxml/Index.fxml");
	}
}
