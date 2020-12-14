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
   
 ARCHIVO:     EstudianteTitulacionJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos del postulante para la migración a emisionTitulo 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-10-2018 			 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) EstudianteTitulacionJdbcDto.
 * DTO encargado de manejar los datos del postulante para la migración a emisionTitulo
 * @author dalbuja.
 * @version 1.0
 */
public class EstudianteTitulacionJdbcDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
	//****************** FICHA ESTUDIANTE *****************************
	private Date fcesFechaInicio;
	private Date fcesFechaEgresamiento;
	private Date fcesFechaActaGrado;
	private String fcesNumActaGrado;
	private String fcesCrrEstudPrevios;
	private Integer fcesTiempoEstudRec;
	private Integer fcesTipoDuracionRec;
	private Integer fcesTipoColegio;
	private String fcesTipoColegioSniese;
	private BigDecimal fcesNotaPromAcumulado;
	private BigDecimal fcesNotaTrabTitulacion;
	private String fcesLinkTesis;
	private Integer fcesRecEstuPrevios;
	private String fcesRecEstuPreviosSniese;
	private Timestamp fcesFechaCreacion;
	private Integer fcesConfCarreraId;
	private Integer fcesMecanismoTitulacionId;
	private Integer fcesCantonResidenciaId;
	private Integer fcesInacInstEstPreviosId;
	private String fcesTituloBachiller;

	//****************** PERSONA *****************************
	private Integer prsTipoIdentificacion;
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
	private Integer prsSexoSniese;
	private Integer prsUbicacionId;
	private Integer prsEtniaId;
	private String prsUbicacionFoto;
//		
//	//****************** TRAMITE TITULO *****************************
	private Integer trttId;
	private Integer trttEstadoProceso;
//	
//	//****************** CARRRERA *****************************
	private Integer crrId;
	private String crrDetalle;
	
	// Constructores
	public EstudianteTitulacionJdbcDto(){
		
	}




	public Date getFcesFechaInicio() {
		return fcesFechaInicio;
	}




	public void setFcesFechaInicio(Date fcesFechaInicio) {
		this.fcesFechaInicio = fcesFechaInicio;
	}




	public Date getFcesFechaEgresamiento() {
		return fcesFechaEgresamiento;
	}




	public void setFcesFechaEgresamiento(Date fcesFechaEgresamiento) {
		this.fcesFechaEgresamiento = fcesFechaEgresamiento;
	}




	public Date getFcesFechaActaGrado() {
		return fcesFechaActaGrado;
	}




	public void setFcesFechaActaGrado(Date fcesFechaActaGrado) {
		this.fcesFechaActaGrado = fcesFechaActaGrado;
	}




	public String getFcesNumActaGrado() {
		return fcesNumActaGrado;
	}




	public void setFcesNumActaGrado(String fcesNumActaGrado) {
		this.fcesNumActaGrado = fcesNumActaGrado;
	}




	public String getFcesCrrEstudPrevios() {
		return fcesCrrEstudPrevios;
	}




	public void setFcesCrrEstudPrevios(String fcesCrrEstudPrevios) {
		this.fcesCrrEstudPrevios = fcesCrrEstudPrevios;
	}




	public Integer getFcesTiempoEstudRec() {
		return fcesTiempoEstudRec;
	}




	public void setFcesTiempoEstudRec(Integer fcesTiempoEstudRec) {
		this.fcesTiempoEstudRec = fcesTiempoEstudRec;
	}




	public Integer getFcesTipoDuracionRec() {
		return fcesTipoDuracionRec;
	}




	public void setFcesTipoDuracionRec(Integer fcesTipoDuracionRec) {
		this.fcesTipoDuracionRec = fcesTipoDuracionRec;
	}




	public Integer getFcesTipoColegio() {
		return fcesTipoColegio;
	}




	public void setFcesTipoColegio(Integer fcesTipoColegio) {
		this.fcesTipoColegio = fcesTipoColegio;
	}




	public String getFcesTipoColegioSniese() {
		return fcesTipoColegioSniese;
	}




	public void setFcesTipoColegioSniese(String fcesTipoColegioSniese) {
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
	}




	public BigDecimal getFcesNotaPromAcumulado() {
		return fcesNotaPromAcumulado;
	}




	public void setFcesNotaPromAcumulado(BigDecimal fcesNotaPromAcumulado) {
		this.fcesNotaPromAcumulado = fcesNotaPromAcumulado;
	}




	public BigDecimal getFcesNotaTrabTitulacion() {
		return fcesNotaTrabTitulacion;
	}




	public void setFcesNotaTrabTitulacion(BigDecimal fcesNotaTrabTitulacion) {
		this.fcesNotaTrabTitulacion = fcesNotaTrabTitulacion;
	}




	public String getFcesLinkTesis() {
		return fcesLinkTesis;
	}




	public void setFcesLinkTesis(String fcesLinkTesis) {
		this.fcesLinkTesis = fcesLinkTesis;
	}




	public Integer getFcesRecEstuPrevios() {
		return fcesRecEstuPrevios;
	}




	public void setFcesRecEstuPrevios(Integer fcesRecEstuPrevios) {
		this.fcesRecEstuPrevios = fcesRecEstuPrevios;
	}




	public String getFcesRecEstuPreviosSniese() {
		return fcesRecEstuPreviosSniese;
	}




	public void setFcesRecEstuPreviosSniese(String fcesRecEstuPreviosSniese) {
		this.fcesRecEstuPreviosSniese = fcesRecEstuPreviosSniese;
	}




	public Integer getFcesConfCarreraId() {
		return fcesConfCarreraId;
	}




	public void setFcesConfCarreraId(Integer fcesConfCarreraId) {
		this.fcesConfCarreraId = fcesConfCarreraId;
	}




	public Integer getFcesMecanismoTitulacionId() {
		return fcesMecanismoTitulacionId;
	}




	public void setFcesMecanismoTitulacionId(Integer fcesMecanismoTitulacionId) {
		this.fcesMecanismoTitulacionId = fcesMecanismoTitulacionId;
	}




	public Integer getFcesCantonResidenciaId() {
		return fcesCantonResidenciaId;
	}




	public void setFcesCantonResidenciaId(Integer fcesCantonResidenciaId) {
		this.fcesCantonResidenciaId = fcesCantonResidenciaId;
	}




	public Integer getFcesInacInstEstPreviosId() {
		return fcesInacInstEstPreviosId;
	}




	public void setFcesInacInstEstPreviosId(Integer fcesInacInstEstPreviosId) {
		this.fcesInacInstEstPreviosId = fcesInacInstEstPreviosId;
	}




	public String getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}




	public void setFcesTituloBachiller(String fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}




	public Timestamp getFcesFechaCreacion() {
		return fcesFechaCreacion;
	}




	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}




	public Integer getPrsTipoIdentificacion() {
		return prsTipoIdentificacion;
	}




	public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
		this.prsTipoIdentificacion = prsTipoIdentificacion;
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




	public Integer getPrsSexoSniese() {
		return prsSexoSniese;
	}




	public void setPrsSexoSniese(Integer prsSexoSniese) {
		this.prsSexoSniese = prsSexoSniese;
	}




	public Integer getPrsUbicacionId() {
		return prsUbicacionId;
	}




	public void setPrsUbicacionId(Integer prsUbicacionId) {
		this.prsUbicacionId = prsUbicacionId;
	}




	public Integer getPrsEtniaId() {
		return prsEtniaId;
	}




	public void setPrsEtniaId(Integer prsEtniaId) {
		this.prsEtniaId = prsEtniaId;
	}




	public Integer getTrttId() {
		return trttId;
	}




	public void setTrttId(Integer trttId) {
		this.trttId = trttId;
	}




	public Integer getTrttEstadoProceso() {
		return trttEstadoProceso;
	}




	public void setTrttEstadoProceso(Integer trttEstadoProceso) {
		this.trttEstadoProceso = trttEstadoProceso;
	}




	public Integer getCrrId() {
		return crrId;
	}




	public void setCrrId(Integer crrId) {
		this.crrId = crrId;
	}




	public String getCrrDetalle() {
		return crrDetalle;
	}




	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}




	public String getPrsUbicacionFoto() {
		return prsUbicacionFoto;
	}




	public void setPrsUbicacionFoto(String prsUbicacionFoto) {
		this.prsUbicacionFoto = prsUbicacionFoto;
	}
	
	

	
	
}
