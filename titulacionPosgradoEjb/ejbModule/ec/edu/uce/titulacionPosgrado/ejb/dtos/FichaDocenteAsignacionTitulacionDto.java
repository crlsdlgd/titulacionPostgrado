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
   
 ARCHIVO:     FichaDocenteAsignacionTitulacionDto.java	  
 DESCRIPCION: DTO encargado de almacenar la asignacion de tribunales para otros mecanismos. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) FichaDocenteAsignacionTitulacionDto.
 * DTO encargado de manejar los datos para el registro de una Carrera.
 * @author gmafla.
 * @version 1.0
 */
public class FichaDocenteAsignacionTitulacionDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	
	/*******************************************************/
	/********* Declaración de FichaDcnAsgTitulacion ********/
	/*******************************************************/
	private Integer fcdcasttId;
	private Integer fcdcasttTipotribunal;
	private Integer asttId;
	
	
	/*******************************************************/
	/********* Declaración de FichaDocente  ****************/
	/*******************************************************/
	private Integer fcdcId;
	private Integer fcdcEstado;
	private String fcdcAbreviaturaTitulo;
	private Integer crrId;
	
	/*******************************************************/
	/********* Declaración de FichaDocente  ****************/
	/*******************************************************/
	private Integer prsId;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailPersonal;
	private Integer prsSexo;
	
	private Float asnoNota; 
	private Integer asnoNumNota;
	
	
	/***************************************************************/
	/***************** Métodos Getter y Setter *********************/
	/***************************************************************/

	public Integer getFcdcasttId() {
		return fcdcasttId;
	}

	public void setFcdcasttId(Integer fcdcasttId) {
		this.fcdcasttId = fcdcasttId;
	}

	public Integer getFcdcasttTipotribunal() {
		return fcdcasttTipotribunal;
	}

	public void setFcdcasttTipotribunal(Integer fcdcasttTipotribunal) {
		this.fcdcasttTipotribunal = fcdcasttTipotribunal;
	}

	public Integer getAsttId() {
		return asttId;
	}

	public void setAsttId(Integer asttId) {
		this.asttId = asttId;
	}

	public Integer getFcdcId() {
		return fcdcId;
	}

	public void setFcdcId(Integer fcdcId) {
		this.fcdcId = fcdcId;
	}

	public Integer getFcdcEstado() {
		return fcdcEstado;
	}

	public void setFcdcEstado(Integer fcdcEstado) {
		this.fcdcEstado = fcdcEstado;
	}

	public String getFcdcAbreviaturaTitulo() {
		return fcdcAbreviaturaTitulo;
	}

	public void setFcdcAbreviaturaTitulo(String fcdcAbreviaturaTitulo) {
		this.fcdcAbreviaturaTitulo = fcdcAbreviaturaTitulo;
	}

	public Integer getCrrId() {
		return crrId;
	}

	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}

	public Integer getPrsId() {
		return prsId;
	}

	public void setPrsId(Integer prsId) {
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
	
	public Integer getPrsSexo() {
		return prsSexo;
	}

	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}

	public Float getAsnoNota() {
		return asnoNota;
	}

	public void setAsnoNota(Float asnoNota) {
		this.asnoNota = asnoNota;
	}

	public Integer getAsnoNumNota() {
		return asnoNumNota;
	}

	public void setAsnoNumNota(Integer asnoNumNota) {
		this.asnoNumNota = asnoNumNota;
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
		return sb.toString();
    }


}
