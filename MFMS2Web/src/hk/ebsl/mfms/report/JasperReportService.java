package hk.ebsl.mfms.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JasperReportService {

	public final static Logger logger = Logger.getLogger(JasperReportService.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	private Properties propertyConfigurer;

	public synchronized String complieReport(String reportName) throws UnsupportedEncodingException {
		ClassLoader classLoader = getClass().getClassLoader();

		String path = classLoader.getResource("").getPath();
		String filePath = URLDecoder.decode(path, "UTF-8");

		String jrxmlFile = filePath + reportName + ".jrxml";
		String jasperFile = filePath + reportName + ".jasper";

		logger.debug("$$$ Compile Report Name = " + reportName);
		// logger.debug(jrxmlFileName);
		// logger.debug(jasperFileName);

		try {
			logger.debug("Compile Start : " + new Date());
			JasperCompileManager.compileReportToFile(jrxmlFile, jasperFile);
			logger.debug("$$$ Compile Report To  = " + jasperFile);
			logger.debug("Compile Time  : " + new Date());
		} catch (Exception ex) {
			logger.error("Compile Report error:" + ex.toString());
			// throw new OperationFailedException("Compile Report error:" + ex);
		}

		return "";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized String prepareReport(String reportName, Map parameters, Object[] data)
			throws UnsupportedEncodingException {

		ClassLoader classLoader = getClass().getClassLoader();

		String path = classLoader.getResource(reportName + ".jrxml").getFile();

		String jrxmlFile = URLDecoder.decode(path, "UTF-8");

		path = (classLoader.getResource(reportName + ".jasper") == null) ? ""
				: classLoader.getResource(reportName + ".jasper").getFile();

		String jasperFileName = URLDecoder.decode(path, "UTF-8");

		path = classLoader.getResource("").getPath() + reportName + ".jrprint";

		String jrprintFile = URLDecoder.decode(path, "UTF-8");

		logger.debug(jrxmlFile);
		logger.debug(jasperFileName);

		try {
			// Compile

			//complieReport(reportName);
			if (jasperFileName == null || jasperFileName.equals("")) {
				complieReport(reportName);

				path = classLoader.getResource(reportName + ".jasper").getFile();

				jasperFileName = URLDecoder.decode(path, "UTF-8");

			}

			logger.debug("&&& Prepare Report Name = " + reportName);

			// Fill - Bean
			logger.debug("Fill Start : " + new Date());
			JasperFillManager.fillReportToFile(jasperFileName, jrprintFile, parameters,
					new JRBeanArrayDataSource(data));
			logger.debug("Fill End   : " + new Date());

			return jrprintFile;
		} catch (Exception ex) {
			logger.error("Prepare Report error:" + ex.toString());
			// throw new OperationFailedException("Prepare Report error:" + ex);
		}

		return null;
	}

	public synchronized String getExportFile(String exportType, String reportName, String exportFileName,
			Map parameters, Object[] data) throws UnsupportedEncodingException {

		if (exportType.equals("pdf")) {
			return executeReportToPdf(reportName, exportFileName, parameters, data);
		}

		if (exportType.equals("xls")) {
			return executeReportToExcel(reportName, exportFileName, parameters, data);
		}

		return "";
	}

	@SuppressWarnings("rawtypes")
	public synchronized String executeReportToPdf(String reportName, String exportFileName, Map parameters,
			Object[] data) throws UnsupportedEncodingException {

		ClassLoader classLoader = getClass().getClassLoader();

		String jrprintFileName = prepareReport(reportName, parameters, data);
		String path = classLoader.getResource("").getPath();

		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("generatedReportPath"), "UTF-8");

		String pdfFileName = "";

		pdfFileName = filePath + exportFileName + "_" + sdf.format(new Date()) + ".pdf";

		try {
			// Export
			File outputFilePath = new File(filePath);

			if (!outputFilePath.exists()) {
				boolean createSuccess = outputFilePath.mkdirs();

				logger.debug("Create output folder : " + createSuccess);
			}

			logger.debug("PDF Create Start : " + new Date());
			JasperExportManager.exportReportToPdfFile(jrprintFileName, pdfFileName);
			logger.debug("PDF Create End : " + new Date());

			File jrprint = new File(jrprintFileName);
			try {
				jrprint.delete();
				logger.debug("Jrprint file removed.");
			} catch (Exception ex) {
				logger.debug("Jrprint file cannot be removed : " + ex.toString());
			}
		} catch (Exception ex) {
			logger.error("Execute Report To PDF error:" + ex.toString());
			ex.printStackTrace();
			// throw new OperationFailedException("Execute Report To PDF error:"
			// +
			// ex);
		}

		return pdfFileName;
	}

	@SuppressWarnings("rawtypes")
	public synchronized String executeReportToExcel(String reportName, String exportFileName, Map parameters,
			Object[] data) throws UnsupportedEncodingException {
		ClassLoader classLoader = getClass().getClassLoader();

		String jrprintFileName = prepareReport(reportName, parameters, data);
		String path = classLoader.getResource("").getPath();

		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("generatedReportPath"), "UTF-8");

		String xlsFileName = "";

		xlsFileName = filePath + exportFileName + "_" + sdf.format(new Date()) + ".xls";

		try {
			// Export
			File outputFilePath = new File(filePath);

			if (!outputFilePath.exists()) {
				boolean createSuccess = outputFilePath.mkdirs();

				logger.debug("Create output folder : " + createSuccess);
			}

			logger.debug("XLS Create Start : " + new Date());

			JRXlsExporter exporter = new JRXlsExporter();
			// ExporterInput ep = new ExporterInput()

			// JasperPrint jp = new JasperPrint(jrprintFileName);

			// exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME,
			// jrprintFileName);
			// exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
			// xlsFileName);
			// exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
			// Boolean.FALSE);
			// exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
			// Boolean.TRUE);
			// exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
			// Boolean.TRUE);
			// exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
			// Boolean.FALSE);
			// exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
			// Boolean.TRUE);

			exporter.setExporterInput(new SimpleExporterInput(jrprintFileName));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsFileName));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(false);
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			configuration.setWhitePageBackground(false);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setForcePageBreaks(false);
			configuration.setIgnoreCellBackground(true);
			configuration.setIgnorePageMargins(true);
			configuration.setAutoFitPageHeight(true);
			configuration.setIgnoreCellBorder(true);
			// configuration.setExporterFilter(filter);
			// configuration.set
			exporter.setConfiguration(configuration);

			// JasperExportManager.exportReportToPdfFile(jrprintFileName,
			// pdfFileName);
			exporter.exportReport();
			logger.debug("XLS Create End : " + new Date());

			File jrprint = new File(jrprintFileName);
			try {
				jrprint.delete();
				logger.debug("Jrprint file removed.");
			} catch (Exception ex) {
				logger.debug("Jrprint file cannot be removed : " + ex.toString());
			}
		} catch (Exception ex) {
			logger.error("Execute Report To XLS error:" + ex.toString());
			ex.printStackTrace();
			// throw new OperationFailedException("Execute Report To PDF error:"
			// +
			// ex);
		}

		return xlsFileName;
	}

	public synchronized void outPutFile(String type, String fileName, HttpServletResponse response)
			throws UnsupportedEncodingException {

		// File pdfFile = new File(fileName);
		// while(pdfFile.exists()){
		// }

		if (!fileName.equals("")) {
			System.out.println("PDF File Name :" + fileName);

			FileInputStream file = null;
			ServletOutputStream out = null;

			// ClassLoader classLoader = getClass().getClassLoader();
			// String path = classLoader.getResource("").getPath();

			String filePath = URLDecoder.decode(propertyConfigurer.getProperty("generatedReportPath"), "UTF-8");

			// Open file to browser
			try {
				if (type.equals("pdf")) {
					response.setContentType("application/pdf");
				}
				if (type.equals("xls")) {
					response.setContentType("application/vnd.ms-excel");
				}
				// response.setContentType("application/pdf");
				// response.setContentType("application/x-xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName.replace(filePath, ""));
				response.addCookie(new Cookie("fileDownloadToken", "fileDownloadToken"));

				file = new FileInputStream(fileName);
				out = response.getOutputStream();

				byte[] buff = new byte[file.available()];

				file.read(buff);

				for (byte b : buff) {
					out.write(b);
				}

				// Simple read/write loop.
				// while (-1 != (bytesRead = file.read(buff, 0, buff.length))) {
				// out.write(buff, 0, bytesRead);
				// }

				// int bit = file.available();
				//
				// while ((bit) >= 0) {
				//
				// bit = file.read();
				// out.write(bit);
				// }

			} catch (Exception ex) {
				logger.error("File not found : " + fileName);
			} finally {

				try {
					// out.close();

					file.close();
					// response.getOutputStream().flush();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					File pdfFile = new File(fileName);
					pdfFile.delete();
					logger.debug("PdfFile file removed.");
				} catch (Exception ex) {
					logger.debug("PdfFile file cannot be removed : " + ex.toString());
				}

			}
		} else {
			logger.error("File name is empty due to result type is empty.");
		}
	}

	public Properties getPropertyConfigurer() {
		return propertyConfigurer;
	}

	public void setPropertyConfigurer(Properties propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

}
