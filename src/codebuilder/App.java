package codebuilder;

import java.io.IOException;
import java.net.URL;

import codebuilder.dao.Dao;
import codebuilder.dao.impl.DerbyDao;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import rcu.InitTable;

public class App extends Application {

	public static Stage stage;

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		replaceScene("/fxml/Index.fxml");
		initTable();
		stage.setResizable(false);
		stage.getIcons().add(new Image("/img/chuizi.png"));
		stage.setTitle("codebuilder");
		stage.show();
	}

	private void initTable() {
		Platform.runLater(() -> {
			boolean isNotExists = false;
			Dao dao = new DerbyDao();
			isNotExists = dao.tableIsNotExists("TEMP");
			isNotExists = dao.tableIsNotExists("TEMP_DATA");
			if (isNotExists) {
				try {
					InitTable.createTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	private static void replaceScene(String sceneFxml) {
		URL url = App.class.getResource(sceneFxml);
		Parent root = null;
		try {
			root = FXMLLoader.load(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add(App.class.getResource("/css/temp.css").toExternalForm());
		stage.setScene(scene);
	}

	/**
	 * 场景替换器
	 *
	 * @param sceneFxml
	 */
	public static void SceneReplacer(String sceneFxml) {
		new Thread(() -> {
			Platform.runLater(() -> {
				App.replaceScene("/fxml/Loading.fxml");
			});
			App.sleep(1000);
			Platform.runLater(() -> {
				App.replaceScene(sceneFxml);

			});
		}).start();
	}

	/**
	 * 模拟加载文件的延迟效果
	 *
	 * @param millis
	 *            延迟时间
	 */
	public static void sleep(long timeout) {
		try {
			Thread.sleep(timeout/2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
