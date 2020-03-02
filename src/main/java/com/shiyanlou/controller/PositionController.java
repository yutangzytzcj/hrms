package com.shiyanlou.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shiyanlou.domain.Position;
import com.shiyanlou.service.PositionService;
import com.shiyanlou.util.ResponseUtil;

/**
 * 
 * @author shiyanlou
 *
 */
@Controller
@RequestMapping("/position")
public class PositionController {

    @Autowired
	private PositionService positionService;
	
	/**
	 * 
	 * @param position
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(Position position, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (position.getName() != null
				&& !"".equals(position.getName().trim())) {
			map.put("name", "%" + position.getName() + "%");
		}
		List<Position> dpositionList = positionService.findPositions(map);
		Integer total = positionService.getCount(map);
		JSONObject result = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(dpositionList);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 
	 * @param position
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(Position position, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int resultTotal = 0;
		if (position.getId() == null)
			resultTotal = positionService.addPosition(position);
		else
			resultTotal = positionService.updatePosition(position);
		JSONObject result = new JSONObject();
		if (resultTotal > 0) {
			result.put("success", true);
		} else {
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "ids") String ids,
			HttpServletResponse response) throws Exception {
		JSONObject result = new JSONObject();
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			try {
				positionService.deletePosition(Integer.parseInt(idsStr[i]));
				result.put("success", true);
			} catch (Exception e) {
				result.put("success", false);
			}				
		}		
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getcombobox")
	@ResponseBody
	public JSONArray getPos(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Position> posList = positionService.findPositions(map);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Position pos : posList) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("id", pos.getId());
			result.put("name", pos.getName());
			list.add(result);
		}
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray;
	}
}
