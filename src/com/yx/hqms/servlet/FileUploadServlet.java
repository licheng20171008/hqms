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

import com.yx.hqms.fileDao.FileHandle;
import com.yx.hqms.fileDao.FileReader;

public class FileUploadServlet extends HttpServlet {

	/**
	 * 
	 */
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
		request.setAttribute("measure", session.getAttribute("measure"));
		request.setAttribute("thList", session.getAttribute("thList"));
		request.setAttribute("tdList", session.getAttribute("tdList"));
		String message = "";
		FileHandle fh = new FileHandle();
		FileReader fr = new FileReader(request);
		Map<String, Object> frMap = fr.resultMap;
		session.setAttribute("resultMap", frMap);
		Map<String, Object> resultMap = fh.fileResult(frMap, session);
		message = (String) resultMap.get("message");
		if (message == null) {
			message = "";
		}
		message = (String) frMap.get("message");
		if (!"".equals(message)) {
			request.setAttribute("fileMeasure", "");
			request.setAttribute("filetdNum", "");
			request.setAttribute("filethContext", "");
			request.setAttribute("selectList", new ArrayList<String>());
		} else {
			String filetdNum = (String) resultMap.get("filetdNum");
			String filethContext = (String) resultMap.get("filethContext");
			List<String> selectList = (List<String>) resultMap.get("selectList");
			request.setAttribute("fileMeasure", session.getAttribute("measure"));
			request.setAttribute("filetdNum", filetdNum);
			request.setAttribute("filethContext", filethContext);
			request.setAttribute("selectList", selectList);
		}
		request.setAttribute("sameComList", new ArrayList<List<String>>());
		request.setAttribute("fileDiffComList", new ArrayList<List<String>>());
		request.setAttribute("diffComList", new ArrayList<List<String>>());
		request.setAttribute("message", message);
		session.setAttribute("fileMeasure", request.getAttribute("fileMeasure"));
		session.setAttribute("filetdNum", request.getAttribute("filetdNum"));
		session.setAttribute("filethContext", request.getAttribute("filethContext"));
		session.setAttribute("selectList", request.getAttribute("selectList"));
		request.getRequestDispatcher("/jsp/compareDetail.jsp").forward(request, response);
		this.doGet(request, response);
	}

}
