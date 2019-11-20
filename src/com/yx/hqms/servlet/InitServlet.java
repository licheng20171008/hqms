package com.yx.hqms.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yx.hqms.sqlDAO.CallprocedureAction;
import com.yx.hqms.sqlDAO.SQLAction;

/**
 * 初始化添加页面
 */
public class InitServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(true);
		List<String> measureList = null;
		List<String> thList = null;
		List<List<String>> tdList = null;
		List<List<String>> resultList = null;
		Map<String, Object> resultMap = null;
		SQLAction sa = new SQLAction();
		CallprocedureAction cpa = new CallprocedureAction();
		String hiddenValue = request.getParameter("hiddenValue");
		if ("0".equals(hiddenValue)) {
			measureList = new ArrayList<String>();
			thList = new ArrayList<String>();
			tdList = new ArrayList<List<String>>();
			request.setAttribute("measureList", measureList);
			request.setAttribute("resultList", new ArrayList<List<String>>());
			request.setAttribute("selectName", "");
			request.setAttribute("measureName", "");
			request.setAttribute("beginDate", "");
			request.setAttribute("endDate", "");
			request.setAttribute("thList", thList);
			request.setAttribute("tdList", tdList);
			request.setAttribute("mmcurpage", 0);
			request.setAttribute("dtcurpage", 0);
			request.getRequestDispatcher("/jsp/hqmsSelect.jsp").forward(request, response);
		} else if ("1".equals(hiddenValue)) {
			thList = new ArrayList<String>();
			tdList = new ArrayList<List<String>>();
			String selectName = request.getParameter("selectName");
			measureList = sa.nameSql("ACCR_MOH_REPORT_HEAD", selectName, "");
			request.setAttribute("measureList", measureList);
			session.setAttribute("measureList", measureList);
			session.setAttribute("selectName", selectName);
			if (session.getAttribute("measureName") != null) {
				request.setAttribute("measureName", session.getAttribute("measureName"));
			} else {
				request.setAttribute("measureName", "");
			}
			if (session.getAttribute("beginDate") != null) {
				request.setAttribute("beginDate", session.getAttribute("beginDate"));
			} else {
				request.setAttribute("beginDate", "");
			}
			if (session.getAttribute("endDate") != null) {
				request.setAttribute("endDate", session.getAttribute("endDate"));
			} else {
				request.setAttribute("endDate", "");
			}
			request.setAttribute("resultList", new ArrayList<List<String>>());
			request.setAttribute("selectName", selectName);
			request.setAttribute("thList", thList);
			request.setAttribute("tdList", tdList);
			request.setAttribute("mmcurpage", 0);
			request.setAttribute("dtcurpage", 0);
			request.getRequestDispatcher("/jsp/hqmsSelect.jsp").forward(request, response);
		} else if ("2".equals(hiddenValue)) {
			thList = new ArrayList<String>();
			tdList = new ArrayList<List<String>>();
			session.setAttribute("measureName", request.getParameter("measureName"));
			session.setAttribute("beginDate", request.getParameter("beginDate"));
			session.setAttribute("endDate", request.getParameter("endDate"));
			request.setAttribute("measureList", session.getAttribute("measureList"));
			request.setAttribute("selectName", session.getAttribute("selectName"));
			request.setAttribute("measureName", session.getAttribute("measureName"));
			request.setAttribute("beginDate", session.getAttribute("beginDate"));
			request.setAttribute("endDate", session.getAttribute("endDate"));
			resultMap = cpa.detailAction(session);
			session.setAttribute("resultMap", resultMap);
			resultList = cpa.resultAction(resultMap);
			request.setAttribute("resultList", resultList);
			session.setAttribute("resultList", resultList);
			if (resultList.size() > 0) {
				request.setAttribute("mmcurpage", 1);
				session.setAttribute("mmcurpage", 1);
			} else {
				request.setAttribute("mmcurpage", 0);
			}
			request.setAttribute("dtcurpage", 0);
			request.setAttribute("thList", thList);
			request.setAttribute("tdList", tdList);
			session.setAttribute("thList", thList);
			session.setAttribute("tdList", tdList);
			request.getRequestDispatcher("/jsp/hqmsSelect.jsp").forward(request, response);
		} else if ("3".equals(hiddenValue)) {
			List<List<String>> resultTDList = new ArrayList<List<String>>();
			request.setAttribute("measureList", session.getAttribute("measureList"));
			request.setAttribute("resultList", session.getAttribute("resultList"));
			request.setAttribute("selectName", session.getAttribute("selectName"));
			request.setAttribute("measureName", session.getAttribute("measureName"));
			request.setAttribute("beginDate", session.getAttribute("beginDate"));
			request.setAttribute("endDate", session.getAttribute("endDate"));
			String measure = request.getParameter("measure");
			session.setAttribute("measure", request.getParameter("measure"));
			resultMap = (Map<String, Object>) session.getAttribute("resultMap");
			if ((List<String>) resultMap.get("thList") ==null) {
				thList = new ArrayList<String>();
			} else {
				thList = (List<String>) resultMap.get("thList");
			}
			if ((List<List<String>>) resultMap.get("tdList") == null) {
				tdList = new ArrayList<List<String>>();
			} else {
				tdList = (List<List<String>>) resultMap.get("tdList");
				int tdIndex = thList.indexOf("MEASURE");
				for (List<String> tdAList : tdList) {
					if (measure.equals(tdAList.get(tdIndex))) {
						resultTDList.add(tdAList);
					}
				}
			}
			if (tdList.size() > 0) {
				request.setAttribute("dtcurpage", 1);
				session.setAttribute("dtcurpage", 1);
			} else {
				request.setAttribute("dtcurpage", 0);
			}
			request.setAttribute("thList", thList);
			request.setAttribute("tdList", resultTDList);
			session.setAttribute("thList", thList);
			session.setAttribute("tdList", resultTDList);
			request.setAttribute("mmcurpage", session.getAttribute("mmcurpage"));
			request.getRequestDispatcher("/jsp/hqmsSelect.jsp").forward(request, response);
		} else if ("4".equals(hiddenValue)) {
			request.setAttribute("measureList", session.getAttribute("measureList"));
			request.setAttribute("resultList", session.getAttribute("resultList"));
			request.setAttribute("selectName", session.getAttribute("selectName"));
			request.setAttribute("measure", session.getAttribute("measure"));
			request.setAttribute("measureName", session.getAttribute("measureName"));
			request.setAttribute("beginDate", session.getAttribute("beginDate"));
			request.setAttribute("endDate", session.getAttribute("endDate"));
			request.setAttribute("thList", session.getAttribute("thList"));
			request.setAttribute("tdList", session.getAttribute("tdList"));
			request.setAttribute("mmcurpage", Integer.parseInt(request.getParameter("mmcurpage")));
			session.setAttribute("mmcurpage", Integer.parseInt(request.getParameter("mmcurpage")));
			request.setAttribute("dtcurpage", Integer.parseInt(request.getParameter("dtcurpage")));
			session.setAttribute("dtcurpage", Integer.parseInt(request.getParameter("dtcurpage")));
			request.getRequestDispatcher("/jsp/hqmsSelect.jsp").forward(request, response);
		} else if ("5".equals(hiddenValue)) {
			request.setAttribute("measure", session.getAttribute("measure"));
			request.setAttribute("thList", session.getAttribute("thList"));
			request.setAttribute("tdList", session.getAttribute("tdList"));
			request.setAttribute("sameComList", new ArrayList<List<String>>());
			request.setAttribute("fileDiffComList", new ArrayList<List<String>>());
			request.setAttribute("diffComList", new ArrayList<List<String>>());
			if (session.getAttribute("message") == null) {
				request.setAttribute("message", "");
			} else {
				request.setAttribute("message", session.getAttribute("message"));
			}
			if (session.getAttribute("fileMeasure") == null) {
				request.setAttribute("fileMeasure", "");
			} else {
				request.setAttribute("fileMeasure", session.getAttribute("fileMeasure"));
			}
			if (session.getAttribute("filetdNum") == null) {
				request.setAttribute("filetdNum", "");
			} else {
				request.setAttribute("filetdNum", session.getAttribute("filetdNum"));
			}
			if (session.getAttribute("filethContext") == null) {
				request.setAttribute("filethContext", "");
			} else {
				request.setAttribute("filethContext", session.getAttribute("filethContext"));
			}
			if (session.getAttribute("selectList") == null) {
				request.setAttribute("selectList", new ArrayList<String>());
			} else {
				request.setAttribute("selectList", session.getAttribute("selectList"));
			}
			request.getRequestDispatcher("/jsp/compareDetail.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		this.doGet(request, response);
	}

}
