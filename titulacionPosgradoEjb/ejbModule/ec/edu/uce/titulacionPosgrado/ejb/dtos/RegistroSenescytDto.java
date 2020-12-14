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
   
 ARCHIVO:     RegistroSenescytDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para la búsqueda del registro de la Senescyt. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-04-2018				Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) RegistroSenescytDto.
 * DTO encargado de manejar los datos para la búsqueda del registro de la Senescyt. 
 * @author dalbuja.
 * @version 1.0
 */
public class RegistroSenescytDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private String registroSenescyt;
	private String detalleSenescyt;
	private String fechaRegistroSenescyt;
	private String universidadSenescyt;
	private Integer sexo;
	
	public RegistroSenescytDto() {
	}

	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	

	public String getRegistroSenescyt() {
		return registroSenescyt;
	}



	public void setRegistroSenescyt(String registroSenescyt) {
		this.registroSenescyt = registroSenescyt;
	}



	public String getDetalleSenescyt() {
		return detalleSenescyt;
	}



	public void setDetalleSenescyt(String detalleSenescyt) {
		this.detalleSenescyt = detalleSenescyt;
	}



	public String getFechaRegistroSenescyt() {
		return fechaRegistroSenescyt;
	}



	public void setFechaRegistroSenescyt(String fechaRegistroSenescyt) {
		this.fechaRegistroSenescyt = fechaRegistroSenescyt;
	}

	public String getUniversidadSenescyt() {
		return universidadSenescyt;
	}

	public void setUniversidadSenescyt(String universidadSenescyt) {
		this.universidadSenescyt = universidadSenescyt;
	}

	public Integer getSexo() {
		return sexo;
	}

	public void setSexo(Integer sexo) {
		this.sexo = sexo;
	}
	
	
	
	
	
	
	
}
