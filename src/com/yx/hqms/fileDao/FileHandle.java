package com.yx.hqms.fileDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FileHandle {

	private Map<String, String> measureMap = null;
	private Map<String, String> line2Map = null;
	private Map<String, List<String>> lineMap = null;
	private List<String> thList = null;
	private List<List<String>> tdList = null;
	@SuppressWarnings("unchecked")
	public Map<String, Object> fileResult(Map<String, Object> argMap, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String measureID = "";
		String message = (String) argMap.get("message");
		String filethContext = "";
		
		if (!"".equals(message)) {
			resultMap.put("message", message);
			return resultMap;
		}
		String measureName = (String) session.getAttribute("measure");
		this.measureMap = (Map<String, String>) argMap.get("line1Map");
		Iterator<?> line1IT = measureMap.entrySet().iterator();
		while (line1IT.hasNext()) {
			Entry<String, String> line1et = (Entry<String, String>) line1IT.next();
			if (line1et.getValue().equals(measureName)) {
				measureID = line1et.getKey();
				break;
			}
		}
		if ("".equals(measureID)) {
			message = "相同的指标ID不存在！！";
			resultMap.put("message", message);
			return resultMap;
		}
		
		this.line2Map = (Map<String, String>) argMap.get("line2Map");
		Iterator<?> line2IT = line2Map.entrySet().iterator();
		this.thList = (List<String>) session.getAttribute("thList");
		this.tdList = (List<List<String>>) session.getAttribute("tdList");
		List<String> selectList = new ArrayList<String>();
		while (line2IT.hasNext()) {
			Entry<String, String> line2et = (Entry<String, String>) line2IT.next();
			if (line2et.getKey().startsWith(measureID)) {
				filethContext = filethContext + line2et.getValue() + "  ";
				if (thList.contains(line2et.getValue())) {
					selectList.add(line2et.getValue());
				}
			}
		}
		
		if ("".equals(filethContext)) {
			message = "对比文件指标明细栏位为空，请确认！！";
			resultMap.put("message", message);
			return resultMap;
		}
		
		this.lineMap = (Map<String, List<String>>) argMap.get("lineMap");
		Iterator<?> lineIT = lineMap.entrySet().iterator();
		int columnNum = 0; 
		while (lineIT.hasNext()) {
			Entry<String, List<String>> lineet = (Entry<String, List<String>>) lineIT.next();
			if (lineet.getKey().startsWith(measureID) && lineet.getValue().size() > columnNum) {
				columnNum = lineet.getValue().size();
			}
		}
		
		if (columnNum == 0) {
			message = "对比文件的指标明细为空，请确认！！";
			resultMap.put("message", message);
			return resultMap;
		}
		
		if (selectList.size() == 0) {
			message = "无可对比项目，请确认！！";
			resultMap.put("message", message);
			return resultMap;
		}
		resultMap.put("message", message);
		resultMap.put("selectList", selectList);
		resultMap.put("filetdNum", String.valueOf(columnNum));
		resultMap.put("filethContext", filethContext);
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> compareResult(HttpServletRequest request, HttpSession session){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> argMap = (Map<String, Object>) session.getAttribute("resultMap");
		Map<String, List<String>> sameMap = new HashMap<String, List<String>>();
		Map<String, List<String>> diffMap = new HashMap<String, List<String>>();
		Map<String, List<String>> fileDiffMap = new HashMap<String, List<String>>();
		thList = (List<String>) session.getAttribute("thList");
		tdList = (List<List<String>>) session.getAttribute("tdList");
		line2Map = (Map<String, String>) argMap.get("line2Map");
		lineMap = (Map<String, List<String>>) argMap.get("lineMap");
		String[] selectItem = request.getParameterValues("selectItem");
		for (String item : selectItem) {
			List<String> sameList = new ArrayList<String>();
			List<String> diffList = new ArrayList<String>();
			List<String> fileDiffList = new ArrayList<String>();
			for (int m = 0; m < thList.size(); m++) {
				String th = thList.get(m);
				if (th.equals(item)) {
					String measureID = "";
					Iterator<?> it = line2Map.entrySet().iterator();
					while (it.hasNext()) {
						Entry<String, String> et = (Entry<String, String>) it.next();
						if (th.equals(et.getValue())) {
							measureID = et.getKey();
							break;
						}
					}
					List<String> fileList = lineMap.get(measureID);
					for (int i = 0; i < tdList.size(); i++) {
						String com = tdList.get(i).get(m);
						if (fileList.contains(com) && !"".equals(com)) {
							sameList.add(com);
						} else {
							diffList.add(com);
						}
					}
					for (String fileCom : fileList) {
						if (!sameList.contains(fileCom)) {
							fileDiffList.add(fileCom);
						}
					}
					sameMap.put(th, sameList);
					diffMap.put(th, diffList);
					fileDiffMap.put(th, fileDiffList);
					break;
				}
			}
		}
		List<List<String>> sameComList = this.mapToList(sameMap);
		List<List<String>> fileDiffComList = this.mapToList(fileDiffMap);
		List<List<String>> diffComList = this.mapToList(diffMap);
		resultMap.put("sameComList", sameComList);
		resultMap.put("fileDiffComList", fileDiffComList);
		resultMap.put("diffComList", diffComList);
		return resultMap;
	}
	
	public List<List<String>> mapToList(Map<String, List<String>> argMap) {
		List<List<String>> resultList = new ArrayList<List<String>>();
		List<String> itemList = new ArrayList<String>();
		Iterator<?> itemIT = argMap.keySet().iterator();
		while (itemIT.hasNext()) {
			itemList.add((String) itemIT.next());
		}
		resultList.add(itemList);
		int size = 0;
		for (String item : itemList) {
			if (size < argMap.get(item).size()) {
				size = argMap.get(item).size();
			}
		}
		for (int i = 0; i < size; i++) {
			List<String> valueList = new ArrayList<String>();
			for (String item : itemList) {
				if (argMap.get(item).size() < (i + 1)) {
					valueList.add("");
				} else {
					valueList.add(argMap.get(item).get(i));
				}
			}
			resultList.add(valueList);
		}
		return resultList;
	}
}
