package com.airsupply.calcite.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.airsupply.calcite.web.model.TreeModel;

public class CalciteUtil {
	public static TreeModel treeModel = new TreeModel();

	public static String jsonPath(String model) {
		String s = System.getProperty("calcite.model.path");
		s = s.concat(File.separator).concat(model).concat(".json");
		if (s.startsWith("file:")) {
			s = s.substring("file:".length());
		}
		return s;
	}

	public static void getFileTree(File file) {
		Random r = new Random();

		treeModel.setCheckstate(0);
		treeModel.setId(String.valueOf(r.nextInt()));
		treeModel.setIsexpand(true);
		treeModel.setShowcheck(true);
		treeModel.setText(file.getName());
		treeModel.setValue(file.getAbsolutePath());
		if (file.isFile()) {
			treeModel.setHasChildren(false);
			treeModel.setChildNodes(null);
		} else {
			treeModel.setHasChildren(true);
			List<TreeModel> childNodes = getChildren(file);
			treeModel.setChildNodes(childNodes);

		}

	}

	public static List<TreeModel> getChildren(File folder) {
		Random r = new Random();
		List<TreeModel> childNodes = new ArrayList<TreeModel>();
		File[] files = folder.listFiles();
		for (File file : files) {
			TreeModel treeModel = new TreeModel();
			treeModel.setCheckstate(0);
			treeModel.setId(String.valueOf(r.nextInt()));
			treeModel.setIsexpand(true);
			treeModel.setShowcheck(true);
			treeModel.setText(file.getName());
			treeModel.setValue(file.getAbsolutePath());
			if (file.isFile()) {
				treeModel.setHasChildren(false);
				treeModel.setChildNodes(null);
			} else {
				treeModel.setHasChildren(true);
				List<TreeModel> childNodes2 = getChildren(file);
				treeModel.setChildNodes(childNodes2);

			}
			childNodes.add(treeModel);

		}
		return childNodes;

	}

	public static void printTree(TreeModel treeModel) {
		System.out.println(treeModel.getText());
		if (treeModel.hasChildren) {
			List<TreeModel> children = treeModel.getChildNodes();
			for (TreeModel model : children) {
				if (model.hasChildren == false) {
					System.out.println(" --" + model.getText());
				} else {

					printTree(model);
				}
			}
		}
	}
	
	public static String readFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString+"</br>";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}

	public static void main(String[] args) {
		File f = new File("D:\\olapworkspace\\calcite-web\\src");
		getFileTree(f);
		printTree(treeModel);
	}
}
