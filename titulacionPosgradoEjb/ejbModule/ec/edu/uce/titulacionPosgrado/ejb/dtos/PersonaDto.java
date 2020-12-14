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
   
 ARCHIVO:     PersonaDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de una Persona. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26-07-2018 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) CarreraDto.
 * DTO encargado de manejar los datos para el registro de una Carrera.
 * @author dalbuja.
 * @version 1.0
 */
public class PersonaDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	private int prsId;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailPersonal;
	private String prsTelefono;
	private Integer prsSexo;
	
	public PersonaDto() {
	}


	public int getPrsId() {
		return prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}

	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}

	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}

	public String getPrsPrimerApellido() {
		return prsPrimerApellido;
	}

	public void setPrsPrimerApellido(String prsPrimerApellido) {
		this.prsPrimerApellido = prsPrimerApellido;
	}

	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}

	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}

	public String getPrsNombres() {
		return prsNombres;
	}

	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}

	public String getPrsMailPersonal() {
		return prsMailPersonal;
	}

	public void setPrsMailPersonal(String prsMailPersonal) {
		this.prsMailPersonal = prsMailPersonal;
	}

	public String getPrsTelefono() {
		return prsTelefono;
	}

	public void setPrsTelefono(String prsTelefono) {
		this.prsTelefono = prsTelefono;
	}

	
	
	public Integer getPrsSexo() {
		return prsSexo;
	}


	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}


	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("prsId : " + prsId);
		sb.append(tabulador + "prsIdentificacion : " + (prsIdentificacion==null? "NULL":prsIdentificacion));
		sb.append(tabulador + "prsPrimerApellido : " + (prsPrimerApellido==null? "NULL":prsPrimerApellido));
		sb.append(tabulador + "prsSegundoApellido : " + (prsSegundoApellido==null? "NULL":prsSegundoApellido));
		sb.append(tabulador + "prsNombres : " + (prsNombres==null? "NULL":prsNombres));
		sb.append(tabulador + "prsMailPersonal : " + (prsMailPersonal==null? "NULL":prsMailPersonal));
		sb.append(tabulador + "prsTelefono : " + (prsTelefono==null? "NULL":prsTelefono));
		return sb.toString();
    }
	
}
