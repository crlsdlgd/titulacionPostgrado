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
   
 ARCHIVO:     TotalesDto.java	  
 DESCRIPCION: Clase encargada de almacenar los datos de los totales de registrados y postulados
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-0218		 Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTOs) TotalesDto.
 * Clase encargada de almacenar los datos de los totales de registrados y postulados
 * @author dalbuja.
 * @version 1.0
 */
public class TotalesDto implements Serializable {
	private static final long serialVersionUID = -3803788069661683139L;
	// *****************************************************************/
	// ******************* Variables de TotalesDto *****************/
	// *****************************************************************/
	private String facultad;
	private String carrera;
	private String totalPostulados;
	
	/*
	 * =========================================================================
	 * ====================== Métodos Getters y Setters ========================
	 * =========================================================================
	 */
	public String getFacultad() {
		return facultad;
	}
	public void setFacultad(String facultad) {
		this.facultad = facultad;
	}
	public String getCarrera() {
		return carrera;
	}
	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}
	
	public String getTotalPostulados() {
		return totalPostulados;
	}
	public void setTotalPostulados(String totalPostulados) {
		this.totalPostulados = totalPostulados;
	}
	
}
