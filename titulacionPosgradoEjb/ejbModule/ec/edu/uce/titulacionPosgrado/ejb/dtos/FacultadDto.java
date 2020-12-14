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
   
 ARCHIVO:     FacultadDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de una Facultad. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) FacultadDto.
 * DTO encargado de manejar los datos para el registro de una Facultad.
 * @author darellano.
 * @version 1.0
 */
public class FacultadDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private int fclId;
	private String fclDescripcion;
	
	public FacultadDto() {
	}
	
	public FacultadDto(int fclId) {
		this.fclId = fclId;
	}
	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	public int getFclId() {
		return fclId;
	}

	public void setFclId(int fclId) {
		this.fclId = fclId;
	}

	public String getFclDescripcion() {
		return fclDescripcion;
	}

	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("fclId : " + fclId);
		sb.append(tabulador + "fclDescripcion : " + (fclDescripcion==null? "NULL":fclDescripcion));
		return sb.toString();
    }

	
	
}
