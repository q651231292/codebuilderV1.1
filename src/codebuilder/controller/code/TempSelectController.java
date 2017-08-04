package codebuilder.controller.code;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codebuilder.App;
import codebuilder.dao.Dao;
import codebuilder.dao.impl.DerbyDao;
import codebuilder.model.Global;
import codebuilder.model.Temp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class TempSelectController implements Initializable{

	@FXML
	TableView<Temp> tempList;
	@FXML
	TableColumn<Temp, String> tempCol;
	@FXML
	Label title;
	@FXML
	Button selectBtn;
	@FXML VBox vbox;


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
	public void back(ActionEvent event) {
		App.SceneReplacer("/fxml/Index.fxml");
	}


	@FXML
	public void selectTemp(ActionEvent event) {
        Temp t = tempList.getSelectionModel().getSelectedItem();
        String tempId = t.getTempId().getValue();
//        System.out.println(tempId);

        Dao dao = new DerbyDao();
        List<Map<String, String>> tempDatas = dao.query("select * from temp_data where temp_id='"+tempId+"'");
        String values = "";
        for (int i = 0; i < tempDatas.size(); i++) {
			Map<String,String> tempData = tempDatas.get(i);
			String label = tempData.get("LABEL");
			String value = tempData.get("VALUE");
			values+=value;
		}
        Set<String> marks = getValueMarks(values);
        System.out.println(marks);
        Global.marks=marks;
        Global.id = tempId;

        App.SceneReplacer("/fxml/code/CodeCreate.fxml");
	}


	private Set<String> getValueMarks(String line) {
		Set<String> set = new TreeSet<>();
    	String regex = "\\$\\{(.*?)\\}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line); // 获取 matcher 对象
        while(m.find()) {
          String mark = m.group(1);
          set.add(mark);
       }
		return set;
	}
}
