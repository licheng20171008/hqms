package com.yx.hqms.fileDao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileReader {

	// 返回MAP
	public Map<String, Object> resultMap = new HashMap<String, Object>();
	private String message = "";

	// 第一行MAP
	private Map<String, String> line1Map = new HashMap<String, String>();

	// 第一行单元格长度
	private Map<String, Integer> line1cellLMap = new HashMap<String, Integer>();

	// 第一行计数
	private int line1Index = 0;
	
	// 第一行列数组
	private List<Integer> Line1ColumList = new ArrayList<Integer>();

	// 第二行Map
	private Map<String, String> line2Map = new HashMap<String, String>();

	// 行数据
	private Map<String, List<String>> lineMap = new HashMap<String, List<String>>();

	// sheet计数
	private int sheetIndex = 0;

	// 文件名
	private String fileName = "";
	
	// 创建文件输出流
	private BufferedReader reader = null;

	// 文件类型
	private String fileType = "";

	// 文件二进制输入流
	private InputStream is = null;

	// 当前sheet
	private int currSheet = 0;

	// 当前位置
	private int currPosition = 0;

	// sheet数量
	private int numOfSheets = 0;

	// HSSFWorkbook
	Workbook workbook = null;

	// 设置cell之间以空格分割
	private static String EXCEL_LINE_DELIMITER = " ";


	public FileReader(HttpServletRequest request) {
		
		// 使用Apache文件上传组件处理文件上传步骤
		// 1.创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		// 2.创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		// 解决上传文件名的中文乱码
		upload.setHeaderEncoding("UTF-8");
		
		// 3.判断提交上来的数据是否是上传表单的数据
		if (!ServletFileUpload.isMultipartContent(request)) {
			
			// 按照传统方式获取数据
			message = "文件上传方式错误！！";
		}
		
		// 4.使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合
		// 每一个FileItem对应一个Form表单的输入项
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> fileItemList = upload.parseRequest(request);
			for (FileItem fileItem : fileItemList) {

				// 判断上传文件
				if (!fileItem.isFormField()) {
					this.fileName = fileItem.getName();
					if (fileName == null || "".equals(fileName.trim())) {
						continue;
					} else {

						// 注意：不同的浏览器提交的文件名不一样，去除路径
						// 处理获得的上传文件的文件名的路径部分，只保留文件名部分
						this.fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

						// 取得文件名的后缀名赋值给filetype
						this.fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

						// 判断文件类型
						//  && !fileType.equalsIgnoreCase("xls")
						if (!fileType.equalsIgnoreCase("xlsx")) {
							message = "请上传EXCEL文件！！";
							resultMap.put("message", message);
						} else {
							// 设置开始为0
							currPosition = 0;

							// 设置当前位置为0
							currPosition = 0;

							try {
								// 创建文件输入流
								is = fileItem.getInputStream();

								// 判断文件格式
								if (fileType.equalsIgnoreCase("xls")) {

									// 如果是EXCEL文件则创建HSSFWorkbook读取
									workbook = new HSSFWorkbook(is);

									// 设置Sheet数
									numOfSheets = workbook.getNumberOfSheets();
								} else if (fileType.equalsIgnoreCase("xlsx")) {

									// 如果是EXCEL文件则创建HSSFWorkbook读取
									workbook = new XSSFWorkbook(is);

									// 设置Sheet数
									numOfSheets = workbook.getNumberOfSheets();
								}

								// 读取数据
								this.readLine();
								resultMap.put("message", message);
								resultMap.put("line1Map", line1Map);
								resultMap.put("line2Map", line2Map);
								resultMap.put("lineMap", lineMap);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								this.close();
							}
						}
						break;
					}
				}
			}
		} catch (FileUploadException e1) {
			e1.printStackTrace();
			message = "文件上传失败！！";
			resultMap.put("message", message);
		}
	}

	// 函数readLine读取文件的一行
	public void readLine() throws IOException {

		// 如果是xls文件则通过POI提供的API读取文件
		if (fileType.equalsIgnoreCase("xls")) {

			// 根据currSheet值获得当前的SHEET
			HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(currSheet);

			// 判断当前行是否到当前sheet的结尾
			if (currPosition > sheet.getLastRowNum()) {

				// 当前行位置清零
				currPosition = 0;

				// 判断是否还有Sheet
				while (currSheet != numOfSheets - 1) {

					// 得到下一张Sheet
					sheet = (HSSFSheet) workbook.getSheetAt(currSheet + 1);

					// 当前行数是否已经到达文件末尾
					if (currPosition == sheet.getLastRowNum()) {

						// 当前SHEET指向下一张sheet
						currSheet++;
						continue;
					} else {
						// 获取当前行数
						int row = currPosition;
						currPosition++;

						// 读取当前行数据
						this.getHssfSheetLine(sheet, row);
					}
				}
			}

			// 获取当前行数
			int row = currPosition;
			currPosition++;

			// 读取当前行数据
			this.getHssfSheetLine(sheet, row);

			// 如果是xls文件则通过POI提供的API读取文件
		} else if (fileType.equalsIgnoreCase("xlsx")) {

			// 初始化SHEET
			XSSFSheet sheet = null;
			
			// 判断是否还有Sheet
			while (currSheet < numOfSheets) {

				// 根据currSheet值获得当前的SHEET
				sheet = (XSSFSheet) workbook.getSheetAt(currSheet);
				
				// 初始化当前SHEET位置
				currPosition = 0;

				// 读取当前行数据
				while (currPosition <= sheet.getLastRowNum()) {
					// 获取当前行数
					int row = currPosition;
					currPosition++;

					// 读取当前行数据
					this.getXssfSheetLine(sheet, row);
				}
				
				// 当前行数是否已经到达文件末尾
				if (currPosition == (sheet.getLastRowNum() + 1)) {

					// 当前SHEET指向下一张sheet
					currSheet++;
					continue;
				}
			}
		}
	}

	// 函数getLine返回Sheet的一行数据
	private String getHssfSheetLine(HSSFSheet sheet, int row) {

		// 根据行数取得sheet的一行
		HSSFRow rowline = sheet.getRow(row);

		// 创建字符创缓冲区
		StringBuffer buffer = new StringBuffer();

		// 获取当前行的列数
		int filledColumns = 0;
		if (rowline != null) {
			filledColumns = rowline.getLastCellNum();
		}
		HSSFCell cell = null;

		// 循环遍历所有列
		for (int i = 0; i < filledColumns; i++) {

			// 取得当前cell
			cell = rowline.getCell(i);
			String cellvalue = null;
			if (cell != null) {
				// 判断当前cell的type
				switch (cell.getCellType()) {
				// 如果当前cell的type为NUMERIC
				case HSSFCell.CELL_TYPE_NUMERIC: {
					// 判断当前cell是否为date
					if (HSSFDateUtil.isCellDateFormatted(cell)) {

						// 把date转换成本地格式的字符串
						cellvalue = cell.getDateCellValue().toString();
					} else {
						// 如果是纯数字
						// 取得当前cell的数值
						Integer num = new Integer((int) cell.getNumericCellValue());
						cellvalue = String.valueOf(num);
					}
					break;

					// 如果当前cell的type为STRING
				}
				case HSSFCell.CELL_TYPE_STRING: {
					// 取得当前的cell字符串
					cellvalue = cell.getStringCellValue().replaceAll("'", "");
					break;
				}
				default: {
					cellvalue = " ";
				}
				}
			} else {
				cellvalue = "";
			}

			// 在每个字段之间插入分隔符
			buffer.append(cellvalue).append(EXCEL_LINE_DELIMITER);
		}

		// 以字符串返回该行的数据
		return buffer.toString();
	}

	// 函数getLine返回Sheet的一行数据
	private void getXssfSheetLine(XSSFSheet sheet, int row) {

		// 根据行数取得sheet的一行
		XSSFRow rowline = sheet.getRow(row);
		
		// 单元格获取
		XSSFCell cell = null;
		
		// 单元格长度
		int cellLength = 0;

		// 获取当前行的列数
		int filledColumns = 0;
		if (rowline != null) {
			filledColumns = rowline.getLastCellNum();
		}

		// 循环遍历所有列
		if (row == 0) {
			for (int i = 0; i < filledColumns; i++) {

				// 单元格的值初始化
				String cellvalue = "";
				
				// 取得当前cell
				cell = rowline.getCell(i);
				cellvalue = getCellValue(cell, cellvalue);
				
				// 数据处理
				if (!"".equals(cellvalue)) {
					String measureID = this.addZero(sheetIndex, 2) + this.addZero(line1Index + 1, 3);
					line1Map.put(measureID, cellvalue);
					line1cellLMap.put(measureID, cell.getColumnIndex());
					Line1ColumList.add(cell.getColumnIndex());
					cellLength = 0;
				} else {
					cellLength = cellLength + 1;
					line1Index = line1Index - 1;
				}
				line1Index = line1Index + 1;
			}
		} else if (row == 1) {
			for (int i = 0; i < filledColumns; i++) {

				// 单元格的值初始化
				String cellvalue = "";
				
				// 取得当前cell
				cell = rowline.getCell(i);
				cellvalue = getCellValue(cell, cellvalue);
				
				// 数据处理
				if (!"".equals(cellvalue)) {
					int cellColum = cell.getColumnIndex();
					String row1ID = getSortValue(cellColum);
					String measureID = row1ID + this.addZero(i + 1, 3);
					line2Map.put(measureID, cellvalue);
				}
			}
			
			Iterator<?> it = line2Map.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("unchecked")
				Entry<String, String> et = (Entry<String, String>) it.next();
				List<String> lineList = new ArrayList<String>();
				lineMap.put(et.getKey(), lineList);
			}
		} else {
			for (int i = 0; i < filledColumns; i++) {

				// 单元格的值初始化
				String cellvalue = "";
				
				// 取得当前cell
				cell = rowline.getCell(i);
				cellvalue = getCellValue(cell, cellvalue);
				
				// 数据处理
				if (!"".equals(cellvalue)) {
					int cellColum = cell.getColumnIndex();
					String row1ID = getSortValue(cellColum);
					String measureID = row1ID + this.addZero(i + 1, 3);
					lineMap.get(measureID).add(cellvalue);
				}
			}
		}
	}

	private String getSortValue(int cellColum) {
		
		String row1ID = "";
		int cellSortIndex = 0;
		for (int index : Line1ColumList) {
			if (cellColum == index) {
				cellSortIndex = cellColum;
				break;
			} else if (index < cellColum && cellSortIndex < index) {
				cellSortIndex = index;
			}
		}
		Iterator<?> it = line1cellLMap.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Entry<String, Integer> et = (Entry<String, Integer>) it.next();
			if (cellSortIndex == et.getValue()) {
				row1ID = et.getKey();
				break;
			}
		}
		return row1ID;
	}

	private String getCellValue(XSSFCell cell, String cellvalue) {
		
		if (cell != null) {
			// 判断当前cell的type
			switch (cell.getCellType()) {
			// 如果当前cell的type为NUMERIC
			case XSSFCell.CELL_TYPE_NUMERIC: {
				// 判断当前cell是否为date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {

					// 把date转换成本地格式的字符串
					cellvalue = cell.getDateCellValue().toString();
				} else {
					// 如果是纯数字
					// 取得当前cell的数值
					Integer num = new Integer((int) cell.getNumericCellValue());
					cellvalue = String.valueOf(num);
				}
				break;

				// 如果当前cell的type为STRING
			}
			case HSSFCell.CELL_TYPE_STRING: {
				// 取得当前的cell字符串
				cellvalue = cell.getStringCellValue().replaceAll("'", "");
				break;
			}
			}
		}
		return cellvalue;
	}

	// close函数执行流的关闭操作
	public void close() {
		// 如果IS为空，则关闭InputStream文件输入流
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				is = null;
			}
		}

		// 如果reader不为空则关闭BufferedReader文件输入流
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				reader = null;
			}
		}
	}

	public String addZero(int arg, int length) {
		return String.valueOf(arg).length() < length ? String.format("%0" + length + "d", arg) : String.valueOf(arg);
	}
}
