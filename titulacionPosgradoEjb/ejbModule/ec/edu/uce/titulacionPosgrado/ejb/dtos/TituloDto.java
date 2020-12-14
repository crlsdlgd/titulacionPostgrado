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
   
 ARCHIVO:     TituloDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de un Titulo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) TituloDto.
 * DTO encargado de manejar los datos para el registro de un Titulo.
 * @author dalbuja.
 * @version 1.0
 */
public class TituloDto implements Serializable {
	
	
	private static final long serialVersionUID = 5566929134251440998L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private int ttlId;
	private String ttlDescripcion;
	private int ttlSexo;
	private int ttlEstado;
	private int ttlTipo;
	
	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	public int getTtlId() {
		return ttlId;
	}
	public void setTtlId(int ttlId) {
		this.ttlId = ttlId;
	}
	public String getTtlDescripcion() {
		return ttlDescripcion;
	}
	public void setTtlDescripcion(String ttlDescripcion) {
		this.ttlDescripcion = ttlDescripcion;
	}
	public int getTtlSexo() {
		return ttlSexo;
	}
	public void setTtlSexo(int ttlSexo) {
		this.ttlSexo = ttlSexo;
	}
	public int getTtlEstado() {
		return ttlEstado;
	}
	public void setTtlEstado(int ttlEstado) {
		this.ttlEstado = ttlEstado;
	}
	public int getTtlTipo() {
		return ttlTipo;
	}
	public void setTtlTipo(int ttlTipo) {
		this.ttlTipo = ttlTipo;
	}
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("ttlId : " + ttlId);
		sb.append(tabulador + "ttlDescripcion : " + (ttlDescripcion==null? "NULL":ttlDescripcion));
		sb.append(tabulador + "ttlSexo : " + ttlSexo);
		sb.append(tabulador + "ttlEstado : " + ttlEstado);
		sb.append(tabulador + "ttlTipo : " + ttlTipo);
		return sb.toString();
    }
	
}
