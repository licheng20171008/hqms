package com.yx.hqms.sqlDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.yx.hqms.DBConnect.DBConnect;

public class CallprocedureAction {

	ResultSet rs = null;
	Connection conn = null;
	CallableStatement cs = null;
	
	public Map<String, Object> detailAction(HttpSession session){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String measure = (String) session.getAttribute("measureName");
		String beginDate = ((String) session.getAttribute("beginDate")).replace("-", "");
		String endDate = ((String) session.getAttribute("endDate")).replace("-", "");
		
		try {
			conn = DBConnect.getConnection();
			String sql = "{call SP_" + measure + "(?, ?)}";
			cs = conn.prepareCall(sql);
			cs.setString(1, beginDate);
			cs.setString(2, endDate);
			cs.execute();
			rs = cs.getResultSet();
			resultMap = this.processResult(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBConnect.close(rs, conn, cs);
		}
		return resultMap;
	}
	
	public Map<String, Object> processResult(ResultSet rs) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> thList = new ArrayList<String>();
		List<List<String>> tdAllList = new ArrayList<List<String>>();
		if (rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			for (int i = 1; i <= colNum; i++) {
				thList.add(rsmd.getColumnName(i).trim());
			}
			do {
				List<String> tdList = new ArrayList<String>();
				for (int i = 1; i <= colNum; i++) {
					tdList.add(rs.getString(i) == null ? "null" : rs.getString(i).trim());
				}
				tdAllList.add(tdList);
			} while (rs.next());
		}
		resultMap.put("thList", thList);
		resultMap.put("tdList", tdAllList);
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<List<String>> resultAction(Map<String, Object> argMap) {
		List<String> thList = (List<String>) argMap.get("thList");
		List<List<String>> tdAllList = (List<List<String>>) argMap.get("tdList");
		List<List<String>> resultList = new ArrayList<List<String>>();
		int thIndex = 0;
		for (String th : thList) {
			thIndex++;
			if ("MEASURE".equals(th)) {
				break;
			}
		}
		if (thIndex != 0) {
			String measure = "";
			List<String> valueList = null;
			int index = 0;
			for (int i = 0; i < tdAllList.size(); i++) {
				String measureA = tdAllList.get(i).get(thIndex - 1);
				if (!measure.equals(measureA)) {
					if (valueList != null) {
						valueList.add(measure);
						valueList.add(String.valueOf(index));
						resultList.add(valueList);
						index = 0;
						valueList = new ArrayList<String>();
					} else {
						valueList = new ArrayList<String>();
					}
					measure = measureA;
				}
				index++;
				if (i == (tdAllList.size() - 1)) {
					valueList.add(measure);
					valueList.add(String.valueOf(index));
					resultList.add(valueList);
				}
			}
		}
		return resultList;
	}
}
