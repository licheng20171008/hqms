package com.yx.hqms.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yx.hqms.fileDao.FileHandle;

public class FileDataCompServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(true);
		String uploadHidden = request.getParameter("uploadHidden");
		request.setAttribute("measure", session.getAttribute("measure"));
		request.setAttribute("thList", session.getAttribute("thList"));
		request.setAttribute("tdList", session.getAttribute("tdList"));
		String message = "";
		FileHandle fh = new FileHandle();
		if ("0".equals(uploadHidden)) {
			request.setAttribute("measureList", session.getAttribute("measureList"));
			request.setAttribute("resultList", session.getAttribute("resultList"));
			request.setAttribute("selectName", session.getAttribute("selectName"));
			request.setAttribute("measureName", session.getAttribute("measureName"));
			request.setAttribute("beginDate", session.getAttribute("beginDate"));
			request.setAttribute("endDate", session.getAttribute("endDate"));
			request.setAttribute("mmcurpage", session.getAttribute("mmcurpage"));
			request.setAttribute("dtcurpage", session.getAttribute("dtcurpage"));
			request.getRequestDispatcher("/jsp/hqmsSelect.jsp").forward(request, response);
		} else if ("1".equals(uploadHidden)) {
			Map<String, Object> resultMap = fh.compareResult(request, session);
			List<List<String>> sameComList = (List<List<String>>) resultMap.get("sameComList");
			List<List<String>> fileDiffComList = (List<List<String>>) resultMap.get("fileDiffComList");
			List<List<String>> diffComList = (List<List<String>>) resultMap.get("diffComList");
			request.setAttribute("sameComList", sameComList);
			request.setAttribute("fileDiffComList", fileDiffComList);
			request.setAttribute("diffComList", diffComList);
			request.setAttribute("message", message);
			request.setAttribute("fileMeasure", session.getAttribute("fileMeasure"));
			request.setAttribute("filetdNum", session.getAttribute("filetdNum"));
			request.setAttribute("filethContext", session.getAttribute("filethContext"));
			request.setAttribute("selectList", session.getAttribute("selectList"));
			session.setAttribute("sameComList", sameComList);
			session.setAttribute("fileDiffComList", fileDiffComList);
			session.setAttribute("diffComList", diffComList);
			request.getRequestDispatcher("/jsp/compareDetail.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		this.doGet(request, response);
	}

}
