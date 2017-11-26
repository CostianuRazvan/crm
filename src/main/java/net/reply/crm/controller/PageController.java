package net.reply.crm.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.reply.backendcrm.dao.EmployeesDAO;
import net.reply.backendcrm.dao.TemplatesDAO;
import net.reply.backendcrm.dto.Employees;
import net.reply.backendcrm.dto.Templates;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;

@Controller
public class PageController {

	@Autowired
	private EmployeesDAO employeesDAO;
	@Autowired
	private TemplatesDAO templatesDAO;

	@RequestMapping(value = { "/", "/home" })
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("greeting", "Welcome to CRM!");
		return mv;
	}

	@RequestMapping(value = { "/employees" })
	public ModelAndView employees() {
		ModelAndView mv = new ModelAndView("employees");

		mv.addObject("employees", employeesDAO.list());
		mv.addObject("templates", templatesDAO.list());
		mv.addObject("greeting", "Welcome to CRM!");
		return mv;
	}

	@RequestMapping(value = { "/templates" })
	public ModelAndView templates() {
		ModelAndView mv = new ModelAndView("templates");

		mv.addObject("templates", templatesDAO.list());
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/updateEmployees" })
	public ModelAndView updateEmployees(@RequestParam("file") MultipartFile file) {
		try {
			ArrayList<String> employeesAttributes = new ArrayList<>();
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			int rowsNumber = 0;
			employeesDAO.deleteALL();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				if (rowsNumber > 0) {
					Employees employees = new Employees();
					employeesAttributes = new ArrayList<>();
					while (cellIterator.hasNext()) {

						Cell nextCell = cellIterator.next();
						int columnIndex = nextCell.getColumnIndex();
						employeesAttributes.add(getCellValue(nextCell).toString());

					}
					employees.setFirstName(employeesAttributes.get(0));
					employees.setLastName(employeesAttributes.get(1));
					employees.setCNP(Double.valueOf(employeesAttributes.get(2)).intValue());
					employees.setFunction(employeesAttributes.get(3));
					employeesDAO.add(employees);
				}
				rowsNumber++;
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView("employees");

		mv.addObject("employees", employeesDAO.list());
		mv.addObject("templates", templatesDAO.list());
		mv.addObject("greeting", "Welcome to CRM!");

		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/addTemplate" })
	public ModelAndView addTemplate(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
		try {

			Templates templates = new Templates();
			templates.setName(name);
			templates.setTemplate(file.getBytes());
			templatesDAO.add(templates);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mv = new ModelAndView("templates");

		mv.addObject("templates", templatesDAO.list());

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/deleteTemplate/{id}" })
	public ModelAndView deleteTemplate(@PathVariable int id) {
		templatesDAO.deleteTemplate(id);

		ModelAndView mv = new ModelAndView("templates");

		mv.addObject("templates", templatesDAO.list());

		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/generateTemplate/{templateName}/{id}" })
	public void generateTemplate(HttpServletResponse response, @PathVariable String templateName,
			@PathVariable int id) {
		try {
			Templates template = templatesDAO.get(templateName).get(0);
			byte[] contents = template.getTemplate();
			File file = new File("/backendcrm/src/main/java/Files/cerere.docx");
			file.setWritable(true);
			FileUtils.writeByteArrayToFile(file, contents);

			byte[] fileToDownload = fillTemplate(file.getAbsolutePath(), employeesDAO.get(id));

			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setContentLength(fileToDownload.length);
			response.setHeader("FileName", templateName + ".docx");

			FileCopyUtils.copy(fileToDownload, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		}
		return null;
	}

	private byte[] fillTemplate(String path, Employees e) {
		Docx docx = new Docx(path);

		// set the variable pattern. In this example the pattern is as follows:
		// #{variableName}
		docx.setVariablePattern(new VariablePattern("#{", "}"));

		// read docx content as simple text
		String content = docx.readTextContent();

		// and display it
		System.out.println(content);

		// find all variables satisfying the pattern #{...}
		List<String> findVariables = docx.findVariables();

		// and display its
		for (String var : findVariables) {
			System.out.println("VARIABLE => " + var);
		}

		// prepare map of variables for template

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = formatter.format(new Date(System.currentTimeMillis()));

		Variables var = new Variables();
		var.addTextVariable(new TextVariable("#{e.firstName}", e.getFirstName()));
		var.addTextVariable(new TextVariable("#{e.lastName}", e.getLastName()));
		var.addTextVariable(new TextVariable("#{e.CNP}", String.valueOf(e.getCNP())));
		var.addTextVariable(new TextVariable("#{e.function}", e.getFunction()));
		var.addTextVariable(new TextVariable("#{date}", dateString));

		// fill template by given map of variables
		docx.fillTemplate(var);

		// save filled document
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		docx.save(byteStream);
		byte[] contentOfStream = byteStream.toByteArray();
		return contentOfStream;
	}
}
