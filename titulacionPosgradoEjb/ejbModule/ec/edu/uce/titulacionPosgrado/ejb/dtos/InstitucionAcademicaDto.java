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
   
 ARCHIVO:     InstitucionAcademicaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de un InstitucionAcademica. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		         AUTOR     					COMENTARIOS
 26/02/2018				Daniel Albuja		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) InstitucionAcademicaDto.
 * DTO encargado de manejar los datos para el registro de un InstitucionAcademica.
 * @author dalbuja.
 * @version 1.0
 */
public class InstitucionAcademicaDto implements Serializable {
	
	private static final long serialVersionUID = 8581051781448182766L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private int inacId;
	private String inacCodigoSniese;
	private String inacDescripcion;
	private Integer inacNivel;
	private Integer inacTipo;
	private String inacTipoSniese;
	private int inacUbcId;
	private String inacUbcDescripcion;
	
	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	public int getInacId() {
		return inacId;
	}

	public void setInacId(int inacId) {
		this.inacId = inacId;
	}

	public String getInacCodigoSniese() {
		return inacCodigoSniese;
	}

	public void setInacCodigoSniese(String inacCodigoSniese) {
		this.inacCodigoSniese = inacCodigoSniese;
	}

	public String getInacDescripcion() {
		return inacDescripcion;
	}

	public void setInacDescripcion(String inacDescripcion) {
		this.inacDescripcion = inacDescripcion;
	}

	public Integer getInacNivel() {
		return inacNivel;
	}

	public void setInacNivel(Integer inacNivel) {
		this.inacNivel = inacNivel;
	}

	public Integer getInacTipo() {
		return inacTipo;
	}

	public void setInacTipo(Integer inacTipo) {
		this.inacTipo = inacTipo;
	}

	public String getInacTipoSniese() {
		return inacTipoSniese;
	}

	public void setInacTipoSniese(String inacTipoSniese) {
		this.inacTipoSniese = inacTipoSniese;
	}

	public int getInacUbcId() {
		return inacUbcId;
	}

	public void setInacUbcId(int inacUbcId) {
		this.inacUbcId = inacUbcId;
	}

	public String getInacUbcDescripcion() {
		return inacUbcDescripcion;
	}

	public void setInacUbcDescripcion(String inacUbcDescripcion) {
		this.inacUbcDescripcion = inacUbcDescripcion;
	}
	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("inacId : " + inacId);
		sb.append(tabulador + "inacCodigoSniese : " + (inacCodigoSniese==null? "NULL":inacCodigoSniese));
		sb.append(tabulador + "inacDescripcion : " + (inacDescripcion==null? "NULL":inacDescripcion));
		sb.append(tabulador + "inacNivel : " + inacNivel);
		sb.append(tabulador + "inacTipo : " + inacTipo);
		sb.append(tabulador + "inacTipoSniese : " + (inacTipoSniese==null? "NULL":inacTipoSniese));
		sb.append(tabulador + "inacUbcId : " + inacUbcId);
		sb.append(tabulador + "inacUbcDescripcion : " + (inacUbcDescripcion==null? "NULL":inacUbcDescripcion));
		return sb.toString();
    }

}
