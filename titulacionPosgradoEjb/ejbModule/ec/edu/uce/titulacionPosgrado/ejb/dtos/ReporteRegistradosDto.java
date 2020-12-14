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
   
 ARCHIVO:     RegistradosDto.java	  
 DESCRIPCION: Clase encargada de almacenar los datos de los estudiantes registrados
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018			 Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;
/**
 * Clase (DTOs) RegistradosDto.
 * Clase encargada de almacenar los datos de los estudiantes registrados
 * @author dalbuja.
 * @version 1.0
 */
public class ReporteRegistradosDto implements Serializable{
	private static final long serialVersionUID = -9030532683798345862L;

	// *****************************************************************/
	// ******************* Variables de RegistradosDto *****************/
	// *****************************************************************/
	private String cedula;
	private String apellido1;
	private String apellido2;
	private String nombres;
	private String mail;
	private String convocatoria;
	
	/*
	 * =========================================================================
	 * ====================== Métodos Getters y Setters ========================
	 * =========================================================================
	 */	
	
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getConvocatoria() {
		return convocatoria;
	}
	public void setConvocatoria(String convocatoria) {
		this.convocatoria = convocatoria;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("cedula:" + cedula == null ? "NULL"
				: cedula);
		sb.append(System.getProperty("line.separator"));
		sb.append("apellido1:" + apellido1 == null ? "NULL"
				: apellido1);
		sb.append(System.getProperty("line.separator"));
		sb.append("apellido2:" + apellido2 == null ? "NULL"
				: apellido2);
		sb.append(System.getProperty("line.separator"));
		sb.append("nombres:" + nombres == null ? "NULL"
				: nombres);
		sb.append(System.getProperty("line.separator"));
		sb.append("mail:" + mail == null ? "NULL"
				: mail);
		sb.append(System.getProperty("line.separator"));
		sb.append("convocatoria:" + convocatoria == null ? "NULL"
				: convocatoria);
		return sb.toString();
	}
	
}
