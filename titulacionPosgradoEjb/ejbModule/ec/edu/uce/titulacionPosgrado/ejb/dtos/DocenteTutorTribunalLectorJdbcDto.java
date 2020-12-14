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
   
 ARCHIVO:     DocenteTutorTribunalLectorJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos del docente 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018 	Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase (DTO) DocenteTutorTribunalLectorJdbcDto.
 * DTO encargado de manejar los datos del docente
 * @author dalbuja.
 * @version 1.0
 */
public class DocenteTutorTribunalLectorJdbcDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
	//****************** FICHA DOCENTE *****************************
	private Integer fcdcId;
	private Integer fcdcEstado;
	private String fcdcAbreviaturaTitulo;
	private Integer fcdcCrr_id;
	private Integer fcdcPrs_id;
	
	//****************** PERSONA *****************************
	private Integer prsId;
	private Integer prsTipoIdentificacion;
	private String prsTipoIdentificacionSt;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private Date prsFechaNacimiento;
	private Integer prsSexo;
	private String prsSexoSt;
	private Integer prsSexoSniese;
	private Integer prsEtnia;
		
	//****************** CARRRERA *****************************
	private Integer crrId;
	private String crrDescripcion;
	private String crrDetalle;
	private Integer crrFcl_id;
	
	//****************** FACULTAD *****************************
	private Integer fclId;
	private String fclDescripcion;
	
	
	public DocenteTutorTribunalLectorJdbcDto(){
	
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

	
	public Integer getFcdcCrr_id() {
		return fcdcCrr_id;
	}

	public void setFcdcCrr_id(Integer fcdcCrr_id) {
		this.fcdcCrr_id = fcdcCrr_id;
	}

	public Integer getFcdcPrs_id() {
		return fcdcPrs_id;
	}

	public void setFcdcPrs_id(Integer fcdcPrs_id) {
		this.fcdcPrs_id = fcdcPrs_id;
	}

	public Integer getPrsId() {
		return prsId;
	}


	public void setPrsId(Integer prsId) {
		this.prsId = prsId;
	}


	public Integer getPrsTipoIdentificacion() {
		return prsTipoIdentificacion;
	}


	public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
		this.prsTipoIdentificacion = prsTipoIdentificacion;
	}


	public String getPrsTipoIdentificacionSt() {
		return prsTipoIdentificacionSt;
	}


	public void setPrsTipoIdentificacionSt(String prsTipoIdentificacionSt) {
		this.prsTipoIdentificacionSt = prsTipoIdentificacionSt;
	}


	public Integer getPrsTipoIdentificacionSniese() {
		return prsTipoIdentificacionSniese;
	}


	public void setPrsTipoIdentificacionSniese(Integer prsTipoIdentificacionSniese) {
		this.prsTipoIdentificacionSniese = prsTipoIdentificacionSniese;
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


	public String getPrsMailInstitucional() {
		return prsMailInstitucional;
	}


	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
	}


	public String getPrsTelefono() {
		return prsTelefono;
	}


	public void setPrsTelefono(String prsTelefono) {
		this.prsTelefono = prsTelefono;
	}


	public Date getPrsFechaNacimiento() {
		return prsFechaNacimiento;
	}


	public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
		this.prsFechaNacimiento = prsFechaNacimiento;
	}


	public Integer getPrsSexo() {
		return prsSexo;
	}


	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}


	public String getPrsSexoSt() {
		return prsSexoSt;
	}


	public void setPrsSexoSt(String prsSexoSt) {
		this.prsSexoSt = prsSexoSt;
	}


	public Integer getPrsSexoSniese() {
		return prsSexoSniese;
	}


	public void setPrsSexoSniese(Integer prsSexoSniese) {
		this.prsSexoSniese = prsSexoSniese;
	}


	public Integer getPrsEtnia() {
		return prsEtnia;
	}


	public void setPrsEtnia(Integer prsEtnia) {
		this.prsEtnia = prsEtnia;
	}


	public Integer getCrrId() {
		return crrId;
	}


	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}


	public String getCrrDescripcion() {
		return crrDescripcion;
	}


	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}


	public Integer getFclId() {
		return fclId;
	}

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}

	public Integer getCrrFcl_id() {
		return crrFcl_id;
	}

	public void setCrrFcl_id(Integer crrFcl_id) {
		this.crrFcl_id = crrFcl_id;
	}

	public void setFclId(Integer fclId) {
		this.fclId = fclId;
	}


	public String getFclDescripcion() {
		return fclDescripcion;
	}


	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}

	
}


