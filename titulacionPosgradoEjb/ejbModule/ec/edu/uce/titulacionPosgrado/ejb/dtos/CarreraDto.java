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
   
 ARCHIVO:     CarreraDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de una Carrera. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) CarreraDto.
 * DTO encargado de manejar los datos para el registro de una Carrera.
 * @author dalbuja.
 * @version 1.0
 */
public class CarreraDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private String crrDetalle;
	private Integer crrFacultad;
	private Integer crrTipoEvaluacion;
	
	
	
	public CarreraDto() {
	}
	
	public CarreraDto(int crrId) {
		this.crrId = crrId;
	}
	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	public int getCrrId() {
		return crrId;
	}
	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}
	public String getCrrDescripcion() {
		return crrDescripcion;
	}
	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}
	public String getCrrCodSniese() {
		return crrCodSniese;
	}
	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}

	public Integer getCrrFacultad() {
		return crrFacultad;
	}

	public void setCrrFacultad(Integer crrFacultad) {
		this.crrFacultad = crrFacultad;
	}
	
	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	
	
	
	public Integer getCrrTipoEvaluacion() {
		return crrTipoEvaluacion;
	}

	public void setCrrTipoEvaluacion(Integer crrTipoEvaluacion) {
		this.crrTipoEvaluacion = crrTipoEvaluacion;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append((crrDescripcion==null? "NULL":crrDescripcion));
		return sb.toString();
    }
	
}
