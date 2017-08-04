package codebuilder.controller.code;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import codebuilder.App;
import codebuilder.dao.Dao;
import codebuilder.dao.impl.DerbyDao;
import codebuilder.model.Global;
import codebuilder.util.AlertTool;
import codebuilder.util.FileTool;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CodeCreateController implements Initializable{
	@FXML
	private Label title;
	@FXML
	private VBox vbox;

	HBox commonhbox;
	TextField pathVal;
	TextField filePrefixVal;

	@FXML
	public void back(ActionEvent event) {
		App.SceneReplacer("/fxml/code/TempSelect.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		commonhbox = new HBox();
		Label pathLab = new Label();
		pathLab.getStyleClass().add("css0");
		pathLab.setText("磁盘路径：");
		pathVal = new TextField();
		pathVal.setText("d:/");
		pathVal.getStyleClass().add("css1");
		commonhbox.getChildren().addAll(pathLab, pathVal);
		commonhbox.getStyleClass().add("hbox");
		vbox.getChildren().add(commonhbox);
		commonhbox = new HBox();
		Label filePrefix = new Label();
		filePrefix.getStyleClass().add("css0");
		filePrefix.setText("文件前缀：");
		filePrefixVal = new TextField();
		filePrefixVal.getStyleClass().add("css1");
		commonhbox.getChildren().addAll(filePrefix, filePrefixVal);
		commonhbox.getStyleClass().add("hbox");
		vbox.getChildren().add(commonhbox);
		if(Global.marks!=null){
			Set<String> marks = Global.marks;
			Global.clearMarks();
			marks.forEach((mark)->{
				HBox hbox = new HBox();
				Label label = new Label();
				label.getStyleClass().add("css0");
				label.setText(mark);
				TextField value = new TextField();
				value.getStyleClass().add("css1");
				hbox.getChildren().addAll(label, value);
				hbox.getStyleClass().add("hbox");
				vbox.getChildren().add(hbox);
			});

		}
	}

	@FXML
	public void createCode(ActionEvent event) {
		Map<String, List<String>> labelAndValue = getLabelAndValue();
		String tempId = Global.id;
		Global.clearId();
		//参数
		List<String> labels = labelAndValue.get("labels");
		List<String> values = labelAndValue.get("values");
		Dao dao = new DerbyDao();
		List<Map<String, String>> tempDatas = dao.query("select * from temp_data where temp_id='"+tempId+"'");

		List<String> fileDatas = new ArrayList<>();
		List<String> fileNames = new ArrayList<>();
		//模板数据
		for (int i = 0; i < tempDatas.size(); i++) {
			Map<String, String> tempData = tempDatas.get(i);
			String tempName = fmtTempName(tempData.get("LABEL"));
			String tempValue = tempData.get("VALUE");
			//把参数带入到模板中
			String fileData = getFileData(tempValue,labels,values);
			fileDatas.add(fileData);
			fileNames.add(tempName);
		}
		//处理文件名,Emp(参数名)Action(文件名action->Action)
		System.out.println(fileDatas);

		String outPath = pathVal.getText();
		System.out.println();



		boolean isSuccess = true;
		for (int i = 0; i < fileNames.size(); i++) {
			String fileName = fileNames.get(i);
			String fileData = fileDatas.get(i);
			isSuccess = FileTool.write(outPath, fileName, fileData);
		}
		if(isSuccess){
			AlertTool.successAlert();
		}else{
			AlertTool.errorAlert();
		}

	}



	private String fmtTempName(String tempName) {
		String first = tempName.substring(0, 1).toUpperCase();
		String end = tempName.substring(1);
		String sum = filePrefixVal.getText()+first+end ;
		return sum;
	}

	private String getFileData(String tempValue, List<String> labels, List<String> values) {
		for (int i = 0; i < labels.size(); i++) {
			String label = labels.get(i);
			String value = values.get(i);
			tempValue = tempValue.replaceAll("\\$\\{("+label+")\\}", value);
		}
		return tempValue;

	}

	public Map<String,List<String>> getLabelAndValue(){
		Map<String,List<String>> result = new HashMap<>();

		//获取页面上动态添加的值
		List<String> labels = new ArrayList<>();
		List<String> values = new ArrayList<>();
		ObservableList<Node> children = vbox.getChildren();
		for (int i = 0; i < children.size(); i++) {
			HBox hbox = (HBox)children.get(i);
			for (int j = 0; j < hbox.getChildren().size(); j++) {
				ObservableList<Node> chil = hbox.getChildren();
				if(j==0){
					Label label = (Label)chil.get(j);
					labels.add(label.getText());

				}
				if(j==1){
					TextField value = (TextField)chil.get(j);
					values.add(value.getText());
				}

			}
		}
		result.put("labels", labels);
		result.put("values", values);
		return result;

	}
}
