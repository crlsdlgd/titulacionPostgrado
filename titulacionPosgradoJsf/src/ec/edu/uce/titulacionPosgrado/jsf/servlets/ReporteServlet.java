/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ReporteServlet.java	  
 DESCRIPCION: Clase encargada de generar el reporte de JasperReport. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			 Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.servlets;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

import org.apache.commons.io.IOUtils;
/**
 * Clase (servlet) ReporteServlet.
 * Clase encargada de generar el reporte de JasperReport.
 * @author dalbuja.
 * @version 1.0
 */
public class ReporteServlet extends HttpServlet{

	private static final long serialVersionUID = -4029426518014683903L;

	public void init(ServletConfig conf) throws ServletException{
		super.init(conf);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//parametros del doGet
		final String nombreJasper = request.getParameter("nombreJasper");
		final String formato = request.getParameter("formato");
		final String tipo = request.getParameter("tipo");  
		
		//parametros de sesion
		String nombreReporte=null;
		List<Map<String, Object>> campos = null;
		Map<String, Object> parametros = null;
		
		//directorio especifico del jasper
		String directorioBase = null;
		//directorio general de los jaspers
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(getServletConfig().getServletContext().getRealPath("/"));
		pathGeneralReportes.append("/titulacionPosgrado/reportes/archivosJasper/");
		//directorio completo del jasper
		StringBuilder pathDeReporte = new StringBuilder();
		
		//**************************************************************************//		
		//********************* VERIFICACION DEL TIPO *****************************//
		//*************************************************************************//
		//Reporte de errores de carga
		if (tipo.equals("INSCRIPCION")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "inscripcion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("ESTADOS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "estadosPostulantes/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("CALIFICACIONES")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "reporteCalificaciones/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("MIGRACION")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "migracionDatos/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("ORDENCOBRO")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "inscripcion/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if (tipo.equals("MERITOS")) {
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "meritosPostulantesMedicina/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if(tipo.equals("ACTAOTROSMECANISMOS")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "actasGrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}else if(tipo.equals("ACTAOTROSMECANISMOS3COMP")){
			nombreReporte = (String)request.getSession().getAttribute("frmCargaNombreReporte");
			campos = (List<Map<String, Object>>) request.getSession().getAttribute("frmCargaCampos");
			parametros = (Map<String, Object>) request.getSession().getAttribute("frmCargaParametros");
			directorioBase = "actasGrado/";
			request.getSession().removeAttribute("frmCargaNombreReporte");
			request.getSession().removeAttribute("frmCargaCampos");
			request.getSession().removeAttribute("frmCargaParametros");
		}
		
		else if(tipo.equals("AQUI_VA_EL_OTRO_REPORTE")){
			
		}
		//defino el path completo del jasper
		pathDeReporte.append(pathGeneralReportes);
		pathDeReporte.append(directorioBase);
		pathDeReporte.append(nombreJasper);
		pathDeReporte.append(".jasper");
		if(campos!=null){
			//genero el datasource de jasper a partir de nuestro datasource
			JRDataSource dataSource = new JRBeanCollectionDataSource(campos);
			//verifico el formato
			try {
				switch (formato) {
					case "PDF":
						exportarPDF(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "XLS":
						exportarExcel(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "CSV":
						exportarCsv(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "HTML":
						exportarHtml(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					case "DOCX":
						exportarDocx(generarJasperPrint(pathDeReporte.toString(),parametros,dataSource), nombreReporte, response);
						break;
					default:
						break;
				}
			} catch (JRException e) {
				e.printStackTrace();
//				System.out.println("*********************************************************************************");
//				System.out.println("********************* ERROR EN LA GENERACION DEL REPORTE ************************");
//				System.out.println("*********************************************************************************");
//				e.printStackTrace();
//				System.out.println("*********************************************************************************");
			}catch (IOException e) {
				e.printStackTrace();
//				System.out.println("*********************************************************************************");
//				System.out.println("********************* ERROR EN LA GENERACION DEL REPORTE ************************");
//				System.out.println("*********************************************************************************");
//				e.printStackTrace();
//				System.out.println("*********************************************************************************");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		throw new RuntimeException("No es posible usar doPost");
	}

	/**
	 * genera el jasperprint a partir del path del jasper, el datasource y los parametros
	 * @param reportPath - path del jasper
	 * @param parametros - parametros para el reporte
	 * @param dataSource - datasource para el reporte
	 * @return - jasperprint respectivo 
	 * @throws JRException
	 */
	private JasperPrint generarJasperPrint(String reportPath, Map<String, Object> parametros, JRDataSource dataSource) throws JRException{
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(reportPath));
		return JasperFillManager.fillReport(jasperReport, parametros, dataSource);
	}
	
	/**
	 * Genera el reporte en archivo PDF 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarPDF(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/pdf");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".pdf\"");
		
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		exporter.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo XLS 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarExcel(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/vnd.ms-excel");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".xls\"");
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		//configuracion del excel 
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);//una pagina por hoja
		configuration.setDetectCellType(true);//detectar el tipo dato de la celda
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setRemoveEmptySpaceBetweenRows(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo CSV 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarCsv(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/CSV");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".csv\"");
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getOutputStream()));
		exporter.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo HTML 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarHtml(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		HtmlExporter exporterHTML = new HtmlExporter();
		SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
		exporterHTML.setExporterInput(exporterInput);
		SimpleHtmlExporterOutput exporterOutput;
		exporterOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
		exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
		exporterHTML.setExporterOutput(exporterOutput);
	    SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
		reportExportConfiguration.setWhitePageBackground(false);
		reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
		exporterHTML.setConfiguration(reportExportConfiguration);
		exporterHTML.exportReport();
	}
	
	/**
	 * Genera el reporte en archivo DOCX 
	 * @param jasperPrint - jasperprint sobre el que se generará el reporte
	 * @param nombreReporte - nombre del reporte
	 * @param response - response del servlet donde se generará el reporte
	 * @throws JRException - Excepcion de jasper
	 * @throws IOException - excepcion de lectura / escritura de archivos
	 */
	protected <T> void exportarDocx(final JasperPrint jasperPrint, final String nombreReporte, final HttpServletResponse response) throws JRException, IOException {
		//definicion del tipo de archivo de salida
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		//definicion del nombre del archivo de salida
		response.setHeader("Content-disposition", "attachment; filename=\""+ nombreReporte +".docx\"");
		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
	}
		
	/**
	 * Compila un jrxml, para esto es necesario que en el bin del servidor esten todas las librerias de jasper
	 * @param pathJrxml - directorio absoluto del archivo jrxml
	 * @return - el compilado jasper del jrxml indicado
	 */
	@SuppressWarnings("unused")
	private JasperReport compilarJrxml(String pathJrxml){
		JasperReport jasperReport = null;
		try {
			jasperReport = JasperCompileManager.compileReport(pathJrxml);
		} catch (JRException e) {
			e.printStackTrace();
			jasperReport = null;
		}
		return jasperReport;
	}
	
	/**
	 * Transforma un jason en un datasource para el reporte jasper
	 * @param jsonData - cadena del json
	 * @return - JRDatasource
	 */
	public JRDataSource jsonToJRDatasource(String jsonData) {
		JRDataSource dataSource = null;
		if ("null".equals(jsonData) || jsonData == null || "".equals(jsonData)) {
			dataSource = new JREmptyDataSource();
			return dataSource;
		}
		InputStream jsonInputStream = null;
		try {
			// Convert the jsonData string to inputStream
			jsonInputStream = IOUtils.toInputStream(jsonData, "UTF-8");
			// selectExpression is based on the jsonData that your string contains
			dataSource = new JsonDataSource(jsonInputStream, "data");
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		if (dataSource == null) {
			dataSource = new JREmptyDataSource();
		}
		return dataSource;
	}
	
}
