package codebuilder.controller.temp;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import codebuilder.App;
import codebuilder.dao.Dao;
import codebuilder.dao.impl.DerbyDao;
import codebuilder.model.Global;
import codebuilder.util.AlertTool;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TempAddController implements Initializable{

	@FXML
	VBox vbox;
	@FXML
	TextField tempName;
	@FXML
	Button saveBtn;
	@FXML
	TextField tempId;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(Global.id != null){
			String tempId = Global.id;
			Global.clearId();
	        Dao dao = new DerbyDao();
	        //获取模板
	        Map<String, String> temp = dao.queryOne("select * from temp where temp_id = '"+tempId+"'");
	        //获取模板数据
	        List<Map<String, String>> tempDatas = dao.query("SELECT * FROM TEMP_DATA WHERE TEMP_ID = '"+tempId+"' ");

	        this.tempId.setText(temp.get("TEMP_ID"));
	        tempName.setText(temp.get("TEMP_NAME"));

	        for (int i = 0; i < tempDatas.size(); i++) {
	        	Map<String, String> tempData = tempDatas.get(i);
	        	appendTempData(tempData.get("LABEL"),tempData.get("VALUE"),tempData.get("TEMP_DATA_ID"));
			}
	        //修改保存按钮的text和点击事件
	        saveBtn.setText("更新模板");
	        saveBtn.setOnAction((evt)->updataTemp());

		}
	}
	public void updataTemp(){
		int isSuccess = 1;

		String tempId = this.tempId.getText();
		String tempName = this.tempName.getText();


		Map<String, List<String>> lableAndValue = getLabelAndValue();
		List<String> labels = lableAndValue.get("labels");
		List<String> values = lableAndValue.get("values");
		List<String> tempDataIds = lableAndValue.get("tempDataIds");

		Dao dao = new DerbyDao();

		isSuccess = dao.mod("update temp set temp_name = '"+tempName+"' where temp_id='"+tempId+"'");
		if(isSuccess==1){
			for (int i = 0; i < tempDataIds.size(); i++) {
				String tempDataId = tempDataIds.get(i);
				String label = labels.get(i);
				String value = values.get(i);
				isSuccess = dao.mod("update temp_data set label='"+label+"',value='"+value+"' where temp_data_id='"+tempDataId+"'");
			}
			if(isSuccess==1){
				AlertTool.successAlert();
				App.SceneReplacer("/fxml/temp/TempList.fxml");
			}else{
				AlertTool.errorAlert();
			}
		}



	}
	@FXML
	public void addTempChil(ActionEvent event) {
		appendTempData(null,null,null);
	}

	public void appendTempData(String labTxt,String valTxt,String idTxt) {
		HBox hbox = new HBox();
		TextField id = new TextField();
		id.getStyleClass().add("hidId");
		id.setText(idTxt);
		id.setManaged(false);
		TextField label = new TextField();
		label.setId("labels");
		if(labTxt!=null)label.setText(labTxt);
		TextArea value = new TextArea();
		value.setId("values");
		if(valTxt!=null)value.setText(valTxt);
		value.getStyleClass().add("values");
		hbox.getChildren().addAll(id,label, value);
		hbox.getStyleClass().add("hbox");
		vbox.getChildren().addAll(hbox);
	}

	public Map<String,List<String>> getLabelAndValue(){
		Map<String,List<String>> result = new HashMap<>();
		//获取页面上动态添加的值
		List<String> labels = new ArrayList<>();
		List<String> values = new ArrayList<>();
		List<String> tempDataIds = new ArrayList<>();
		ObservableList<Node> children = vbox.getChildren();
		for (int i = 0; i < children.size(); i++) {
			HBox hbox = (HBox)children.get(i);
			for (int j = 0; j < hbox.getChildren().size(); j++) {
				if(j==0){
					TextField label = (TextField)hbox.getChildren().get(j);
					tempDataIds.add(label.getText());

				}
				if(j==1){
					TextField label = (TextField)hbox.getChildren().get(j);
					labels.add(label.getText());
				}
				if(j==2){
					TextArea value = (TextArea)hbox.getChildren().get(j);
					values.add(value.getText());
				}
			}
		}
		result.put("labels", labels);
		result.put("values", values);
		result.put("tempDataIds", tempDataIds);
		return result;

	}
	@FXML
	public void addTemp(ActionEvent event) {
		//执行结果标记，默认是成功
		int isSuccess = 1;
		Map<String, List<String>> lableAndValue = getLabelAndValue();
		List<String> labels = lableAndValue.get("labels");
		List<String> values = lableAndValue.get("values");
		//把数据存储到数据库中
		Dao dao = new DerbyDao();
		String tempId = UUID.randomUUID().toString();
		isSuccess = dao.add("insert into temp values('"+tempId+"','"+tempName.getText()+"')");

		if(isSuccess==1){
			for (int i = 0; i < labels.size(); i++) {
				String label = labels.get(i);
				String value = values.get(i);
				isSuccess = dao.add("insert into temp_data values('"+UUID.randomUUID().toString()+"','"+tempId+"','"+label+"','"+value+"')");
			}
			if(isSuccess==1){
				AlertTool.successAlert();
				App.SceneReplacer("/fxml/temp/TempList.fxml");
			}else{
				AlertTool.errorAlert();
			}
		}
	}

	@FXML
	public void back(ActionEvent event) {
		App.SceneReplacer("/fxml/temp/TempList.fxml");
	}
}
