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
   
 ARCHIVO:     ConvocatoriaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de una Convocatoria. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.sql.Date;

/**
 * Clase (DTO) ConvocatoriaDto.
 * DTO encargado de manejar los datos para el registro de una Convocatoria.
 * @author dalbuja.
 * @version 1.0
 */
public class ConvocatoriaDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private Integer cnvId;
	private String cnvDescripcion;
	private Date cnvFechaInicio;
	private Date cnvFechaFin;
	private Integer cnvEstado;
	private Integer cnvEstadoFase;
	
	public ConvocatoriaDto() {
	}
	

	public ConvocatoriaDto(Integer cnvId, String cnvDescripcion,
			Date cnvFechaInicio, Date cnvFechaFin, Integer cnvEstado,
			Integer cnvEstadoFase) {
		this.cnvId = cnvId;
		this.cnvDescripcion = cnvDescripcion;
		this.cnvFechaInicio = cnvFechaInicio;
		this.cnvFechaFin = cnvFechaFin;
		this.cnvEstado = cnvEstado;
		this.cnvEstadoFase = cnvEstadoFase;
	}
	
	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	

	public Integer getCnvId() {
		return cnvId;
	}


	public void setCnvId(Integer cnvId) {
		this.cnvId = cnvId;
	}


	public String getCnvDescripcion() {
		return cnvDescripcion;
	}


	public void setCnvDescripcion(String cnvDescripcion) {
		this.cnvDescripcion = cnvDescripcion;
	}


	public Date getCnvFechaInicio() {
		return cnvFechaInicio;
	}


	public void setCnvFechaInicio(Date cnvFechaInicio) {
		this.cnvFechaInicio = cnvFechaInicio;
	}


	public Date getCnvFechaFin() {
		return cnvFechaFin;
	}


	public void setCnvFechaFin(Date cnvFechaFin) {
		this.cnvFechaFin = cnvFechaFin;
	}


	public Integer getCnvEstado() {
		return cnvEstado;
	}


	public void setCnvEstado(Integer cnvEstado) {
		this.cnvEstado = cnvEstado;
	}


	public Integer getCnvEstadoFase() {
		return cnvEstadoFase;
	}


	public void setCnvEstadoFase(Integer cnvEstadoFase) {
		this.cnvEstadoFase = cnvEstadoFase;
	}







	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("cnvId : " + cnvId);
		sb.append(tabulador + "cnvDescripcion : " + (cnvDescripcion==null? "NULL":cnvDescripcion));
		sb.append(tabulador + "cnvFechaInicio : " + (cnvFechaInicio==null? "NULL":cnvFechaInicio));
		sb.append(tabulador + "crrDetalle : " + (cnvFechaFin==null? "NULL":cnvFechaFin));
		sb.append(tabulador + "cnvEstado : " + (cnvEstado==null? "NULL":cnvEstado));
		sb.append(tabulador + "cnvEstadoFase : " + (cnvEstadoFase==null? "NULL":cnvEstadoFase));
		return sb.toString();
    }


	
	
	
}
