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
   
 ARCHIVO:     PostuladosDto.java	  
 DESCRIPCION: Clase encargada de almacenar los datos de los estudiantes postulados
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018			 Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTOs) RegistradosDto. Clase encargada de almacenar los datos de los
 * estudiantes registrados
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class ReporteActualizacionConocDto implements Serializable {
	private static final long serialVersionUID = 609639612642062044L;

	// *****************************************************************/
	// ******************* Variables de PostuladosDto *****************/
	// *****************************************************************/
	private String carrera;
	private String facultad;
	private String nivel;
	private String cedula;
	private String apellido1;
	private String apellido2;
	private String nombres;
	private String mail;
	private String telefono;
	private String mctt_estadistico;
	private String observacionDesactivar;
	private String convocatoria;
	private String estadoProceso;
	private String actualizacionConocimiento;
	private String aptAproboActualizar;

	public ReporteActualizacionConocDto() {

	}

	public ReporteActualizacionConocDto(String carrera, String nivel,
			String cedula, String apellido1, String apellido2, String nombres,
			String mail, String telefono, String mctt_estadistico,
			String observacionDesactivar, String convocatoria,
			String estadoProceso, String actualizacionConocimiento, String aptAproboActualizar) {
		super();
		this.carrera = carrera;
		this.nivel = nivel;
		this.cedula = cedula;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.nombres = nombres;
		this.mail = mail;
		this.telefono = telefono;
		this.mctt_estadistico = mctt_estadistico;
		this.observacionDesactivar = observacionDesactivar;
		this.convocatoria = convocatoria;
		this.estadoProceso = estadoProceso;
		this.actualizacionConocimiento = actualizacionConocimiento;
		this.aptAproboActualizar = aptAproboActualizar;
	}

	/*
	 * =========================================================================
	 * ====================== Métodos Getters y Setters ========================
	 * =========================================================================
	 */
	public String getCarrera() {
		return carrera;
	}

	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMctt_estadistico() {
		return mctt_estadistico;
	}

	public void setMctt_estadistico(String mctt_estadistico) {
		this.mctt_estadistico = mctt_estadistico;
	}

	public String getObservacionDesactivar() {
		return observacionDesactivar;
	}

	public void setObservacionDesactivar(String observacionDesactivar) {
		this.observacionDesactivar = observacionDesactivar;
	}

	public String getConvocatoria() {
		return convocatoria;
	}

	public void setConvocatoria(String convocatoria) {
		this.convocatoria = convocatoria;
	}

	public String getEstadoProceso() {
		return estadoProceso;
	}

	public void setEstadoProceso(String estadoProceso) {
		this.estadoProceso = estadoProceso;
	}

	public String getActualizacionConocimiento() {
		return actualizacionConocimiento;
	}

	public void setActualizacionConocimiento(String actualizacionConocimiento) {
		this.actualizacionConocimiento = actualizacionConocimiento;
	}
	

	public String getAptAproboActualizar() {
		return aptAproboActualizar;
	}

	public void setAptAproboActualizar(String aptAproboActualizar) {
		this.aptAproboActualizar = aptAproboActualizar;
	}
	
	

	public String getFacultad() {
		return facultad;
	}

	public void setFacultad(String facultad) {
		this.facultad = facultad;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("carrera:" + carrera == null ? "NULL" : carrera);
		sb.append(System.getProperty("line.separator"));
		sb.append("cedula:" + cedula == null ? "NULL" : cedula);
		sb.append(System.getProperty("line.separator"));
		sb.append("apellido1:" + apellido1 == null ? "NULL" : apellido1);
		sb.append(System.getProperty("line.separator"));
		sb.append("apellido2:" + apellido2 == null ? "NULL" : apellido2);
		sb.append(System.getProperty("line.separator"));
		sb.append("nombres:" + nombres == null ? "NULL" : nombres);
		sb.append(System.getProperty("line.separator"));
		sb.append("mail:" + mail == null ? "NULL" : mail);
		sb.append(System.getProperty("line.separator"));
		sb.append("telefono:" + telefono == null ? "NULL" : telefono);
		sb.append(System.getProperty("line.separator"));
		sb.append("estado proceso:" + estadoProceso == null ? "NULL" : estadoProceso);
		sb.append(System.getProperty("line.separator"));
		sb.append("actualización conocimiento:" + actualizacionConocimiento == null ? "NULL" : actualizacionConocimiento);
		sb.append(System.getProperty("line.separator"));
		sb.append("aptitud estado:" + aptAproboActualizar == null ? "NULL" : aptAproboActualizar);
		return sb.toString();
	}
}
