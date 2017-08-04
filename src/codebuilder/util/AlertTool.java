package codebuilder.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertTool {


	public static void alert(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("消息提示框");
		alert.setHeaderText(text);
		alert.setContentText("操作成功!");
		alert.showAndWait();
	}
	public static void successAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("消息提示框");
		alert.setHeaderText(null);
		alert.setContentText("操作成功!");
		alert.showAndWait();
	}
	public static void errorAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("消息提示框");
		alert.setHeaderText(null);
		alert.setContentText("操作失败!");
		alert.showAndWait();
	}
}
