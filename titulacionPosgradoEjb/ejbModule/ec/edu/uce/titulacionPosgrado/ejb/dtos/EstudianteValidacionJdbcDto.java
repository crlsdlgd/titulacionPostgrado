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
   
 ARCHIVO:     EstudianteValidacionJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos del estudiante validado. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) EstudianteValidacionJdbcDto.
 * DTO encargado de manejar los datos del estudiante validado.
 * @author dalbuja.
 * @version 1.0
 */
public class EstudianteValidacionJdbcDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
	//****************** FICHA ESTUDIANTE *****************************
	private Integer fcesId;
	private Date fcesFechaInicio;
	private Date fcesFechaEgresamiento;
	private Date fcesFechaActaGrado;
	private String fcesNumActaGrado;
	private Date fcesFechaRefrendacion;
	private String fcesNumRefrendacion;
	private String fcesCrrEstudPrevios;
	private Integer fcesTiempoEstudRec;
	private Integer fcesTipoDuracionRec;
	private String fcesTipoDuracionRecSt;
	private Integer fcesTipoColegio;
	private String fcesTipoColegioSt;
	private String fcesTipoColegioSniese;
	private String fcesTituloBachiller;
	private Integer fcesTituloBachillerId;
	private BigDecimal fcesNotaPromAcumulado;
	private BigDecimal fcesNotaTrabTitulacion;
	private String fcesLinkTesis;
	private Integer fcesRecEstuPrevios;
	private String fcesRecEstuPreviosSt;
	private String fcesRecEstuPreviosSniese;
	private Timestamp fcesFechaCreacion;
	private Date fcesFechaInicioCohorte;
	private Date fcesFechaFinCohorte;

	private String fcesRegTTlSenescyt;
	
	//****************** UBICACION *****************************
	private Integer ubcPaisId;
	private String ubcPaisDescripcion;
	private Integer ubcCantonId;
	private String ubcCantonDescripcion;
	
	//****************** ETNIA *****************************
	private Integer etnId;
	private String etnDescripcion;
	
	//****************** INSTITUCION ACADEMICA *****************************
	private Integer inacUniversidadId;
	private String inacUniversidadDescripcion;
	
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
	private String prsUbicacionFoto;
		
	//****************** TRAMITE TITULO *****************************
	private Integer trttId;
	private String trttNumTramite;
	private Integer trttEstadoTramite;
	private Integer trttEstadoProceso;
	private Integer trttCrrCodSniese;
	private String trttObsValSec;
	private Integer trttCarrera;
	private Integer trttCarreraId;
	private Integer trttMcttEstadistico;
	private String trttMcttEstadisticoSt;
	
	//****************** PROCESO TRÁMITE *****************************
	private Integer prtrId;
	private Integer prtrTipoProceso;
	private Timestamp prtrFechaEjecucion;
	private Integer prtrRoflcrId;
	private Integer prtrTrttId;
	private String prtrTtlRegSenescyt;
	
	//****************** CONVOCATORIA *****************************
	private Integer cnvId;
	private String cnvDescripcion;
	private Date cnvFechaInicio;
	private Date cnvFechaFin;
	private Date cnvFechaLimiteEgresado;
	private Integer cnvEstado;
	private Integer cnvEstadoFase;
	
	//****************** CARRRERA *****************************
	private Integer crrId;
	private String crrDescripcion;
	private String crrDetalle;
	
	//****************** FACULTAD *****************************
	private Integer fclId;
	private String fclDescripcion;
	
	//****************** TITULO *****************************
	private Integer ttlId;
	private String ttlDescripcion;
	
	public EstudianteValidacionJdbcDto(){
	
	}

	//****************** VALIDACION *****************************
	private int vldId;
	private Integer vldSegundaProrroga;
	private Integer vldRslHomologacion;
	private Integer vldRslActConocimiento;
	private Integer vldRslProrroga;
	private Integer vldRslIdoneidad;
	private Integer vldRslAprobacionIngles;
	private Integer vldRslActualizAprob;
	private Integer vldRslCursoActAprob;
	private Integer vldRslHomologacionAprob;
	//****************** VALIDACION *****************************
	private Integer aptAproboActualizacion;

	public Integer getFcesId() {
		return fcesId;
	}
	public void setFcesId(Integer fcesId) {
		this.fcesId = fcesId;
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
	public Date getFcesFechaRefrendacion() {
		return fcesFechaRefrendacion;
	}
	public void setFcesFechaRefrendacion(Date fcesFechaRefrendacion) {
		this.fcesFechaRefrendacion = fcesFechaRefrendacion;
	}
	public String getFcesNumRefrendacion() {
		return fcesNumRefrendacion;
	}
	public void setFcesNumRefrendacion(String fcesNumRefrendacion) {
		this.fcesNumRefrendacion = fcesNumRefrendacion;
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
	public String getFcesTipoDuracionRecSt() {
		return fcesTipoDuracionRecSt;
	}
	public void setFcesTipoDuracionRecSt(String fcesTipoDuracionRecSt) {
		this.fcesTipoDuracionRecSt = fcesTipoDuracionRecSt;
	}
	public Integer getFcesTipoColegio() {
		return fcesTipoColegio;
	}
	public void setFcesTipoColegio(Integer fcesTipoColegio) {
		this.fcesTipoColegio = fcesTipoColegio;
	}
	public String getFcesTipoColegioSt() {
		return fcesTipoColegioSt;
	}
	public void setFcesTipoColegioSt(String fcesTipoColegioSt) {
		this.fcesTipoColegioSt = fcesTipoColegioSt;
	}
	public String getFcesTipoColegioSniese() {
		return fcesTipoColegioSniese;
	}
	public void setFcesTipoColegioSniese(String fcesTipoColegioSniese) {
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
	}
	public Integer getFcesTituloBachillerId() {
		return fcesTituloBachillerId;
	}
	public void setFcesTituloBachillerId(Integer fcesTituloBachillerId) {
		this.fcesTituloBachillerId = fcesTituloBachillerId;
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
	public String getFcesRecEstuPreviosSt() {
		return fcesRecEstuPreviosSt;
	}
	public void setFcesRecEstuPreviosSt(String fcesRecEstuPreviosSt) {
		this.fcesRecEstuPreviosSt = fcesRecEstuPreviosSt;
	}
	public String getFcesRecEstuPreviosSniese() {
		return fcesRecEstuPreviosSniese;
	}
	public void setFcesRecEstuPreviosSniese(String fcesRecEstuPreviosSniese) {
		this.fcesRecEstuPreviosSniese = fcesRecEstuPreviosSniese;
	}
	public Timestamp getFcesFechaCreacion() {
		return fcesFechaCreacion;
	}
	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}
	public Date getFcesFechaInicioCohorte() {
		return fcesFechaInicioCohorte;
	}
	public void setFcesFechaInicioCohorte(Date fcesFechaInicioCohorte) {
		this.fcesFechaInicioCohorte = fcesFechaInicioCohorte;
	}
	public Date getFcesFechaFinCohorte() {
		return fcesFechaFinCohorte;
	}
	public void setFcesFechaFinCohorte(Date fcesFechaFinCohorte) {
		this.fcesFechaFinCohorte = fcesFechaFinCohorte;
	}
	public String getFcesRegTTlSenescyt() {
		return fcesRegTTlSenescyt;
	}
	public void setFcesRegTTlSenescyt(String fcesRegTTlSenescyt) {
		this.fcesRegTTlSenescyt = fcesRegTTlSenescyt;
	}
	public Integer getUbcPaisId() {
		return ubcPaisId;
	}
	public void setUbcPaisId(Integer ubcPaisId) {
		this.ubcPaisId = ubcPaisId;
	}
	public String getUbcPaisDescripcion() {
		return ubcPaisDescripcion;
	}
	public void setUbcPaisDescripcion(String ubcPaisDescripcion) {
		this.ubcPaisDescripcion = ubcPaisDescripcion;
	}
	public Integer getUbcCantonId() {
		return ubcCantonId;
	}
	public void setUbcCantonId(Integer ubcCantonId) {
		this.ubcCantonId = ubcCantonId;
	}
	public String getUbcCantonDescripcion() {
		return ubcCantonDescripcion;
	}
	public void setUbcCantonDescripcion(String ubcCantonDescripcion) {
		this.ubcCantonDescripcion = ubcCantonDescripcion;
	}
	public Integer getEtnId() {
		return etnId;
	}
	public void setEtnId(Integer etnId) {
		this.etnId = etnId;
	}
	public String getEtnDescripcion() {
		return etnDescripcion;
	}
	public void setEtnDescripcion(String etnDescripcion) {
		this.etnDescripcion = etnDescripcion;
	}
	public Integer getInacUniversidadId() {
		return inacUniversidadId;
	}
	public void setInacUniversidadId(Integer inacUniversidadId) {
		this.inacUniversidadId = inacUniversidadId;
	}
	public String getInacUniversidadDescripcion() {
		return inacUniversidadDescripcion;
	}
	public void setInacUniversidadDescripcion(String inacUniversidadDescripcion) {
		this.inacUniversidadDescripcion = inacUniversidadDescripcion;
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
	public Integer getTrttId() {
		return trttId;
	}
	public void setTrttId(Integer trttId) {
		this.trttId = trttId;
	}
	public String getTrttNumTramite() {
		return trttNumTramite;
	}
	public void setTrttNumTramite(String trttNumTramite) {
		this.trttNumTramite = trttNumTramite;
	}
	public Integer getTrttEstadoTramite() {
		return trttEstadoTramite;
	}
	public void setTrttEstadoTramite(Integer trttEstadoTramite) {
		this.trttEstadoTramite = trttEstadoTramite;
	}
	public Integer getTrttEstadoProceso() {
		return trttEstadoProceso;
	}
	public void setTrttEstadoProceso(Integer trttEstadoProceso) {
		this.trttEstadoProceso = trttEstadoProceso;
	}
	public Integer getTrttCrrCodSniese() {
		return trttCrrCodSniese;
	}
	public void setTrttCrrCodSniese(Integer trttCrrCodSniese) {
		this.trttCrrCodSniese = trttCrrCodSniese;
	}
	public String getTrttObsValSec() {
		return trttObsValSec;
	}
	public void setTrttObsValSec(String trttObsValSec) {
		this.trttObsValSec = trttObsValSec;
	}
	public Integer getTrttCarrera() {
		return trttCarrera;
	}
	public void setTrttCarrera(Integer trttCarrera) {
		this.trttCarrera = trttCarrera;
	}
	public Integer getTrttCarreraId() {
		return trttCarreraId;
	}
	public void setTrttCarreraId(Integer trttCarreraId) {
		this.trttCarreraId = trttCarreraId;
	}
	public Integer getTrttMcttEstadistico() {
		return trttMcttEstadistico;
	}
	public void setTrttMcttEstadistico(Integer trttMcttEstadistico) {
		this.trttMcttEstadistico = trttMcttEstadistico;
	}
	public String getTrttMcttEstadisticoSt() {
		return trttMcttEstadisticoSt;
	}
	public void setTrttMcttEstadisticoSt(String trttMcttEstadisticoSt) {
		this.trttMcttEstadisticoSt = trttMcttEstadisticoSt;
	}
	public Integer getPrtrId() {
		return prtrId;
	}
	public void setPrtrId(Integer prtrId) {
		this.prtrId = prtrId;
	}
	public Integer getPrtrTipoProceso() {
		return prtrTipoProceso;
	}
	public void setPrtrTipoProceso(Integer prtrTipoProceso) {
		this.prtrTipoProceso = prtrTipoProceso;
	}
	public Timestamp getPrtrFechaEjecucion() {
		return prtrFechaEjecucion;
	}
	public void setPrtrFechaEjecucion(Timestamp prtrFechaEjecucion) {
		this.prtrFechaEjecucion = prtrFechaEjecucion;
	}
	public Integer getPrtrRoflcrId() {
		return prtrRoflcrId;
	}
	public void setPrtrRoflcrId(Integer prtrRoflcrId) {
		this.prtrRoflcrId = prtrRoflcrId;
	}
	public Integer getPrtrTrttId() {
		return prtrTrttId;
	}
	public void setPrtrTrttId(Integer prtrTrttId) {
		this.prtrTrttId = prtrTrttId;
	}
	public String getPrtrTtlRegSenescyt() {
		return prtrTtlRegSenescyt;
	}
	public void setPrtrTtlRegSenescyt(String prtrTtlRegSenescyt) {
		this.prtrTtlRegSenescyt = prtrTtlRegSenescyt;
	}
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
	public Date getCnvFechaLimiteEgresado() {
		return cnvFechaLimiteEgresado;
	}
	public void setCnvFechaLimiteEgresado(Date cnvFechaLimiteEgresado) {
		this.cnvFechaLimiteEgresado = cnvFechaLimiteEgresado;
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
	public String getCrrDetalle() {
		return crrDetalle;
	}
	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}
	public Integer getFclId() {
		return fclId;
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
	public Integer getTtlId() {
		return ttlId;
	}
	public void setTtlId(Integer ttlId) {
		this.ttlId = ttlId;
	}
	public String getTtlDescripcion() {
		return ttlDescripcion;
	}
	public void setTtlDescripcion(String ttlDescripcion) {
		this.ttlDescripcion = ttlDescripcion;
	}
	public int getVldId() {
		return vldId;
	}
	public void setVldId(int vldId) {
		this.vldId = vldId;
	}
	public Integer getVldSegundaProrroga() {
		return vldSegundaProrroga;
	}
	public void setVldSegundaProrroga(Integer vldSegundaProrroga) {
		this.vldSegundaProrroga = vldSegundaProrroga;
	}
	public Integer getVldRslHomologacion() {
		return vldRslHomologacion;
	}
	public void setVldRslHomologacion(Integer vldRslHomologacion) {
		this.vldRslHomologacion = vldRslHomologacion;
	}
	public Integer getVldRslActConocimiento() {
		return vldRslActConocimiento;
	}
	public void setVldRslActConocimiento(Integer vldRslActConocimiento) {
		this.vldRslActConocimiento = vldRslActConocimiento;
	}
	public Integer getVldRslProrroga() {
		return vldRslProrroga;
	}
	public void setVldRslProrroga(Integer vldRslProrroga) {
		this.vldRslProrroga = vldRslProrroga;
	}
	public Integer getVldRslIdoneidad() {
		return vldRslIdoneidad;
	}
	public void setVldRslIdoneidad(Integer vldRslIdoneidad) {
		this.vldRslIdoneidad = vldRslIdoneidad;
	}
	public Integer getVldRslAprobacionIngles() {
		return vldRslAprobacionIngles;
	}
	public void setVldRslAprobacionIngles(Integer vldRslAprobacionIngles) {
		this.vldRslAprobacionIngles = vldRslAprobacionIngles;
	}
	public Integer getVldRslActualizAprob() {
		return vldRslActualizAprob;
	}
	public void setVldRslActualizAprob(Integer vldRslActualizAprob) {
		this.vldRslActualizAprob = vldRslActualizAprob;
	}
	public Integer getVldRslCursoActAprob() {
		return vldRslCursoActAprob;
	}
	public void setVldRslCursoActAprob(Integer vldRslCursoActAprob) {
		this.vldRslCursoActAprob = vldRslCursoActAprob;
	}
	public Integer getVldRslHomologacionAprob() {
		return vldRslHomologacionAprob;
	}
	public void setVldRslHomologacionAprob(Integer vldRslHomologacionAprob) {
		this.vldRslHomologacionAprob = vldRslHomologacionAprob;
	}
	public Integer getAptAproboActualizacion() {
		return aptAproboActualizacion;
	}
	public void setAptAproboActualizacion(Integer aptAproboActualizacion) {
		this.aptAproboActualizacion = aptAproboActualizacion;
	}
	public String getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}
	public void setFcesTituloBachiller(String fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}
	public String getPrsUbicacionFoto() {
		return prsUbicacionFoto;
	}
	public void setPrsUbicacionFoto(String prsUbicacionFoto) {
		this.prsUbicacionFoto = prsUbicacionFoto;
	}


	
}
