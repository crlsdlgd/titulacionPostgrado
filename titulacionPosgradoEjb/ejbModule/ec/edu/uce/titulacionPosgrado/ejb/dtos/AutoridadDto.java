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
   
 ARCHIVO:     AutoridadDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de una Autoridad. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-12-2015 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) AutoridadDto.
 * DTO encargado de manejar los datos de autoridades para el registro de una Carrera.
 * @author dalbuja.
 * @version 1.0
 */
public class AutoridadDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	private int atrId;
	private String atrNombres;
	private String atrPrimerApellido;
	private String atrSegundoApellido;
	private Integer atrTipo;
	private Integer atrSexo;
	
	public AutoridadDto() {
	}

	public int getAtrId() {
		return atrId;
	}

	public void setAtrId(int atrId) {
		this.atrId = atrId;
	}
//
	public String getAtrNombres() {
		return atrNombres;
	}

	public void setAtrNombres(String atrNombres) {
		this.atrNombres = atrNombres;
	}

	public String getAtrPrimerApellido() {
		return atrPrimerApellido;
	}

	public void setAtrPrimerApellido(String atrPrimerApellido) {
		this.atrPrimerApellido = atrPrimerApellido;
	}

	public String getAtrSegundoApellido() {
		return atrSegundoApellido;
	}

	public void setAtrSegundoApellido(String atrSegundoApellido) {
		this.atrSegundoApellido = atrSegundoApellido;
	}

	public Integer getAtrTipo() {
		return atrTipo;
	}

	public void setAtrTipo(Integer atrTipo) {
		this.atrTipo = atrTipo;
	}
	
	public Integer getAtrSexo() {
		return atrSexo;
	}

	public void setAtrSexo(Integer atrSexo) {
		this.atrSexo = atrSexo;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("atrId : " + atrId);
		sb.append(tabulador + "atrPrimerApellido : " + (atrPrimerApellido==null? "NULL":atrPrimerApellido));
		sb.append(tabulador + "atrSegundoApellido : " + (atrSegundoApellido==null? "NULL":atrSegundoApellido));
		sb.append(tabulador + "atrNombres : " + (atrNombres==null? "NULL":atrNombres));
		return sb.toString();
    }
	
}
