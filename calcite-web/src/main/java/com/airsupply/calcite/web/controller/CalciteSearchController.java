package com.airsupply.calcite.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.airsupply.calcite.web.model.TreeModel;
import com.airsupply.calcite.web.service.CalciteSearchService;
import com.airsupply.calcite.web.util.CalciteUtil;

@Controller
@RequestMapping(value = "/search")
public class CalciteSearchController {
	@Autowired
	private CalciteSearchService calciteSearchService;

	@RequestMapping(value = "/select", method = { RequestMethod.POST,
			RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> select(String model, String sql) {
		System.setProperty("calcite.model.path",
				"D:\\olapworkspace\\calcite-web\\calcite_models");
		return calciteSearchService.select(model, sql);
	}

	@RequestMapping(value = "/getConfigFileTree", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<TreeModel> getConfigFileTree() {
		System.setProperty("calcite.model.path",
				"D:\\olapworkspace\\calcite-web\\calcite_models");
		List<TreeModel> models=new ArrayList<TreeModel>();
		models.add(calciteSearchService.getConfigFileTree());
		return models ;
	}
	

	@RequestMapping(value = "/getConfigFileContent", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String,String> getConfigFileContent(String filePath) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("path", filePath);
		map.put("content", CalciteUtil.readFile(filePath));
		return map  ;
	}
}
