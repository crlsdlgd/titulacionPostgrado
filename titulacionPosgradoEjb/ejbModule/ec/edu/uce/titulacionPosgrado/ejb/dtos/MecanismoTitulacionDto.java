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
   
 ARCHIVO:     MecanismoTitulacionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de un mecanismo_titulacion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) MecanismoTitulacionDto.
 * DTO encargado de manejar los datos para el registro de un mecanismo_titulacion.
 * @author dalbuja.
 * @version 1.0
 */
public class MecanismoTitulacionDto implements Serializable {
	
	private static final long serialVersionUID = 8581051781448182766L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	private int mcttId;
	private String mcttCodigoSniese;
	private String mcttDescripcion;
	private Integer mcttEstado;
	
	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	public int getMcttId() {
		return mcttId;
	}

	public void setMcttId(int mcttId) {
		this.mcttId = mcttId;
	}

	public String getMcttCodigoSniese() {
		return mcttCodigoSniese;
	}

	public void setMcttCodigoSniese(String mcttCodigoSniese) {
		this.mcttCodigoSniese = mcttCodigoSniese;
	}

	public String getMcttDescripcion() {
		return mcttDescripcion;
	}

	public void setMcttDescripcion(String mcttDescripcion) {
		this.mcttDescripcion = mcttDescripcion;
	}

	public Integer getMcttEstado() {
		return mcttEstado;
	}

	public void setMcttEstado(Integer mcttEstado) {
		this.mcttEstado = mcttEstado;
	}
	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("mcttId : " + mcttId);
		sb.append(tabulador + "mcttCodigoSniese : " + (mcttCodigoSniese==null? "NULL":mcttCodigoSniese));
		sb.append(tabulador + "mcttDescripcion : " + (mcttDescripcion==null? "NULL":mcttDescripcion));
		sb.append(tabulador + "mcttEstado : " + mcttEstado);
		return sb.toString();
    }

	

}
