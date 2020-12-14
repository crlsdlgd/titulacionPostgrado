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
   
 ARCHIVO:     EstudianteEvaluacionJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos del estudiante evaluado. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018	 			 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) EstudianteEvaluacionJdbcDto.
 * DTO encargado de manejar los datos del estudiante evaluado.
 * @author dalbuja.
 * @version 1.0
 */
public class EstudianteAptitudJdbcDto implements Serializable {
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
	private Integer fcesTituloBachillerId;
	private BigDecimal fcesNotaPromAcumulado;
	private BigDecimal fcesNotaTrabTitulacion;
	private String fcesLinkTesis;
	private Integer fcesRecEstuPrevios;
	private String fcesRecEstuPreviosSt;
	private String fcesRecEstuPreviosSniese;
	private Timestamp fcesFechaCreacion;
	
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
	private String trttObservacionDga;
	private String trttObsDesactivarDga;
	
	//****************** PROCESO TRÁMITE *****************************
	private Integer prtrId;
	private Integer prtrTipoProceso;
	private Timestamp prtrFechaEjecucion;
	private Integer prtrRoflcrId;
	private Integer prtrTrttId;
	
	//****************** CONVOCATORIA *****************************
	private Integer cnvId;
	private String cnvDescripcion;
	private Date cnvFechaInicio;
	private Date cnvFechaFin;
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
	
	//****************** VALIDACION *****************************
	private Integer vldId;
	private Integer vldCulminoMalla;
	private Integer vldReproboComplexivo;
	private Integer vldAsignadoTutor;
	private Integer vldUltimoSemestre;
	private Integer vldRslHomologacion;
	private Integer vldRslExamenComplexivo;
	private Integer vldRslActConocimiento;
	private Integer vldRslGratuidad;
	private Integer vldRslIdoneidad;
	
	private Integer vldRslOtrosMecanismos;
	private Integer vldRslSlcMecanismo;
	
	//****************** ASIGNACION_TITULACION *****************************
	private Integer asttId;
	private String asttTemaTrabajo;
	private String asttTutor;
	
	//****************** MECANISMO_TITULACION_CARRERA *****************************
	private Integer mcttcrId;
	private Integer mcttcrEstado;
	private int mcttcrCrrId;
	
	//****************** MECANISMO_TITULACION *****************************
	private Integer mcttId;
	private String mcttCodigoSniese;
	private String mcttdescripcion;
	private Integer mcttEstado;
	
	//****************** APTITUD *****************************
	private Integer aptId;
	private Integer aptRequisitos;
	private Integer aptSuficienciaIngles; 
	private Integer aptFinMalla; 
	private Integer aptAproboActualizar; 
	private Integer aptAproboTutor; 
	private BigDecimal aptNotaDirector;
	
	//****************** REPORTES *****************************
	private String estadoReporte;
	private String validacion_mallaReporte;
	private String vldComplexivoReporte;
	private String validacion_tutorReporte;
	private String validacion_ultimoReporte;
	private String tema_asignadoReporte;
	private String tutor_asignadoReporte;
	
	// Constructores
	public EstudianteAptitudJdbcDto(){
		
	}


	
	

	public EstudianteAptitudJdbcDto(Integer fcesId, Date fcesFechaInicio, Date fcesFechaEgresamiento,
			Date fcesFechaActaGrado, String fcesNumActaGrado, Date fcesFechaRefrendacion, String fcesNumRefrendacion,
			String fcesCrrEstudPrevios, Integer fcesTiempoEstudRec, Integer fcesTipoDuracionRec,
			String fcesTipoDuracionRecSt, Integer fcesTipoColegio, String fcesTipoColegioSt,
			String fcesTipoColegioSniese, Integer fcesTituloBachillerId, BigDecimal fcesNotaPromAcumulado,
			BigDecimal fcesNotaTrabTitulacion, String fcesLinkTesis, Integer fcesRecEstuPrevios,
			String fcesRecEstuPreviosSt, String fcesRecEstuPreviosSniese, Timestamp fcesFechaCreacion,
			Integer ubcPaisId, String ubcPaisDescripcion, Integer ubcCantonId, String ubcCantonDescripcion,
			Integer etnId, String etnDescripcion, Integer inacUniversidadId, String inacUniversidadDescripcion,
			Integer prsId, Integer prsTipoIdentificacion, String prsTipoIdentificacionSt,
			Integer prsTipoIdentificacionSniese, String prsIdentificacion, String prsPrimerApellido,
			String prsSegundoApellido, String prsNombres, String prsMailPersonal, String prsMailInstitucional,
			String prsTelefono, Date prsFechaNacimiento, Integer prsSexo, String prsSexoSt, Integer prsSexoSniese,
			Integer trttId, String trttNumTramite, Integer trttEstadoTramite, Integer trttEstadoProceso,
			Integer trttCrrCodSniese, String trttObsValSec, Integer trttCarrera, Integer trttCarreraId,
			Integer trttMcttEstadistico, String trttMcttEstadisticoSt, String trttObservacionDga,
			String trttObsDesactivarDga, Integer prtrId, Integer prtrTipoProceso, Timestamp prtrFechaEjecucion,
			Integer prtrRoflcrId, Integer prtrTrttId, Integer cnvId, String cnvDescripcion, Date cnvFechaInicio,
			Date cnvFechaFin, Integer cnvEstado, Integer cnvEstadoFase, Integer crrId, String crrDescripcion,
			String crrDetalle, Integer fclId, String fclDescripcion, Integer ttlId, String ttlDescripcion,
			Integer vldId, Integer vldCulminoMalla, Integer vldReproboComplexivo, Integer vldAsignadoTutor,
			Integer vldUltimoSemestre, Integer vldRslHomologacion, Integer vldRslExamenComplexivo,
			Integer vldRslActConocimiento, Integer vldRslGratuidad, Integer vldRslIdoneidad,
			Integer vldRslOtrosMecanismos, Integer vldRslSlcMecanismo, Integer asttId, String asttTemaTrabajo,
			String asttTutor, Integer mcttcrId, Integer mcttcrEstado, int mcttcrCrrId, Integer mcttId,
			String mcttCodigoSniese, String mcttdescripcion, Integer mcttEstado, Integer aptId, Integer aptRequisitos,
			Integer aptSuficienciaIngles, Integer aptFinMalla, Integer aptAproboActualizar, Integer aptAproboTutor,
			BigDecimal aptNotaDirector, String estadoReporte, String validacion_mallaReporte,
			String vldComplexivoReporte, String validacion_tutorReporte, String validacion_ultimoReporte,
			String tema_asignadoReporte, String tutor_asignadoReporte) {
		this.fcesId = fcesId;
		this.fcesFechaInicio = fcesFechaInicio;
		this.fcesFechaEgresamiento = fcesFechaEgresamiento;
		this.fcesFechaActaGrado = fcesFechaActaGrado;
		this.fcesNumActaGrado = fcesNumActaGrado;
		this.fcesFechaRefrendacion = fcesFechaRefrendacion;
		this.fcesNumRefrendacion = fcesNumRefrendacion;
		this.fcesCrrEstudPrevios = fcesCrrEstudPrevios;
		this.fcesTiempoEstudRec = fcesTiempoEstudRec;
		this.fcesTipoDuracionRec = fcesTipoDuracionRec;
		this.fcesTipoDuracionRecSt = fcesTipoDuracionRecSt;
		this.fcesTipoColegio = fcesTipoColegio;
		this.fcesTipoColegioSt = fcesTipoColegioSt;
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
		this.fcesTituloBachillerId = fcesTituloBachillerId;
		this.fcesNotaPromAcumulado = fcesNotaPromAcumulado;
		this.fcesNotaTrabTitulacion = fcesNotaTrabTitulacion;
		this.fcesLinkTesis = fcesLinkTesis;
		this.fcesRecEstuPrevios = fcesRecEstuPrevios;
		this.fcesRecEstuPreviosSt = fcesRecEstuPreviosSt;
		this.fcesRecEstuPreviosSniese = fcesRecEstuPreviosSniese;
		this.fcesFechaCreacion = fcesFechaCreacion;
		this.ubcPaisId = ubcPaisId;
		this.ubcPaisDescripcion = ubcPaisDescripcion;
		this.ubcCantonId = ubcCantonId;
		this.ubcCantonDescripcion = ubcCantonDescripcion;
		this.etnId = etnId;
		this.etnDescripcion = etnDescripcion;
		this.inacUniversidadId = inacUniversidadId;
		this.inacUniversidadDescripcion = inacUniversidadDescripcion;
		this.prsId = prsId;
		this.prsTipoIdentificacion = prsTipoIdentificacion;
		this.prsTipoIdentificacionSt = prsTipoIdentificacionSt;
		this.prsTipoIdentificacionSniese = prsTipoIdentificacionSniese;
		this.prsIdentificacion = prsIdentificacion;
		this.prsPrimerApellido = prsPrimerApellido;
		this.prsSegundoApellido = prsSegundoApellido;
		this.prsNombres = prsNombres;
		this.prsMailPersonal = prsMailPersonal;
		this.prsMailInstitucional = prsMailInstitucional;
		this.prsTelefono = prsTelefono;
		this.prsFechaNacimiento = prsFechaNacimiento;
		this.prsSexo = prsSexo;
		this.prsSexoSt = prsSexoSt;
		this.prsSexoSniese = prsSexoSniese;
		this.trttId = trttId;
		this.trttNumTramite = trttNumTramite;
		this.trttEstadoTramite = trttEstadoTramite;
		this.trttEstadoProceso = trttEstadoProceso;
		this.trttCrrCodSniese = trttCrrCodSniese;
		this.trttObsValSec = trttObsValSec;
		this.trttCarrera = trttCarrera;
		this.trttCarreraId = trttCarreraId;
		this.trttMcttEstadistico = trttMcttEstadistico;
		this.trttMcttEstadisticoSt = trttMcttEstadisticoSt;
		this.trttObservacionDga = trttObservacionDga;
		this.trttObsDesactivarDga = trttObsDesactivarDga;
		this.prtrId = prtrId;
		this.prtrTipoProceso = prtrTipoProceso;
		this.prtrFechaEjecucion = prtrFechaEjecucion;
		this.prtrRoflcrId = prtrRoflcrId;
		this.prtrTrttId = prtrTrttId;
		this.cnvId = cnvId;
		this.cnvDescripcion = cnvDescripcion;
		this.cnvFechaInicio = cnvFechaInicio;
		this.cnvFechaFin = cnvFechaFin;
		this.cnvEstado = cnvEstado;
		this.cnvEstadoFase = cnvEstadoFase;
		this.crrId = crrId;
		this.crrDescripcion = crrDescripcion;
		this.crrDetalle = crrDetalle;
		this.fclId = fclId;
		this.fclDescripcion = fclDescripcion;
		this.ttlId = ttlId;
		this.ttlDescripcion = ttlDescripcion;
		this.vldId = vldId;
		this.vldCulminoMalla = vldCulminoMalla;
		this.vldReproboComplexivo = vldReproboComplexivo;
		this.vldAsignadoTutor = vldAsignadoTutor;
		this.vldUltimoSemestre = vldUltimoSemestre;
		this.vldRslHomologacion = vldRslHomologacion;
		this.vldRslExamenComplexivo = vldRslExamenComplexivo;
		this.vldRslActConocimiento = vldRslActConocimiento;
		this.vldRslGratuidad = vldRslGratuidad;
		this.vldRslIdoneidad = vldRslIdoneidad;
		this.vldRslOtrosMecanismos = vldRslOtrosMecanismos;
		this.vldRslSlcMecanismo = vldRslSlcMecanismo;
		this.asttId = asttId;
		this.asttTemaTrabajo = asttTemaTrabajo;
		this.asttTutor = asttTutor;
		this.mcttcrId = mcttcrId;
		this.mcttcrEstado = mcttcrEstado;
		this.mcttcrCrrId = mcttcrCrrId;
		this.mcttId = mcttId;
		this.mcttCodigoSniese = mcttCodigoSniese;
		this.mcttdescripcion = mcttdescripcion;
		this.mcttEstado = mcttEstado;
		this.aptId = aptId;
		this.aptRequisitos = aptRequisitos;
		this.aptSuficienciaIngles = aptSuficienciaIngles;
		this.aptFinMalla = aptFinMalla;
		this.aptAproboActualizar = aptAproboActualizar;
		this.aptAproboTutor = aptAproboTutor;
		this.aptNotaDirector = aptNotaDirector;
		this.estadoReporte = estadoReporte;
		this.validacion_mallaReporte = validacion_mallaReporte;
		this.vldComplexivoReporte = vldComplexivoReporte;
		this.validacion_tutorReporte = validacion_tutorReporte;
		this.validacion_ultimoReporte = validacion_ultimoReporte;
		this.tema_asignadoReporte = tema_asignadoReporte;
		this.tutor_asignadoReporte = tutor_asignadoReporte;
	}





	public Integer getVldRslOtrosMecanismos() {
		return vldRslOtrosMecanismos;
	}

	public void setVldRslOtrosMecanismos(Integer vldRslOtrosMecanismos) {
		this.vldRslOtrosMecanismos = vldRslOtrosMecanismos;
	}

	public Integer getVldRslSlcMecanismo() {
		return vldRslSlcMecanismo;
	}

	public void setVldRslSlcMecanismo(Integer vldRslSlcMecanismo) {
		this.vldRslSlcMecanismo = vldRslSlcMecanismo;
	}

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

	public Integer getVldId() {
		return vldId;
	}

	public void setVldId(Integer vldId) {
		this.vldId = vldId;
	}
	
	public Integer getVldCulminoMalla() {
		return vldCulminoMalla;
	}

	public void setVldCulminoMalla(Integer vldCulminoMalla) {
		this.vldCulminoMalla = vldCulminoMalla;
	}

	public Integer getVldReproboComplexivo() {
		return vldReproboComplexivo;
	}

	public void setVldReproboComplexivo(Integer vldReproboComplexivo) {
		this.vldReproboComplexivo = vldReproboComplexivo;
	}

	public Integer getVldAsignadoTutor() {
		return vldAsignadoTutor;
	}

	public void setVldAsignadoTutor(Integer vldAsignadoTutor) {
		this.vldAsignadoTutor = vldAsignadoTutor;
	}
	
	public Integer getVldUltimoSemestre() {
		return vldUltimoSemestre;
	}

	public void setVldUltimoSemestre(Integer vldUltimoSemestre) {
		this.vldUltimoSemestre = vldUltimoSemestre;
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

	public Integer getVldRslGratuidad() {
		return vldRslGratuidad;
	}

	public void setVldRslGratuidad(Integer vldRslGratuidad) {
		this.vldRslGratuidad = vldRslGratuidad;
	}

	public Integer getVldRslIdoneidad() {
		return vldRslIdoneidad;
	}

	public void setVldRslIdoneidad(Integer vldRslIdoneidad) {
		this.vldRslIdoneidad = vldRslIdoneidad;
	}

	public Integer getVldRslExamenComplexivo() {
		return vldRslExamenComplexivo;
	}

	public void setVldRslExamenComplexivo(Integer vldRslExamenComplexivo) {
		this.vldRslExamenComplexivo = vldRslExamenComplexivo;
	}

	public Integer getTrttCarreraId() {
		return trttCarreraId;
	}

	public void setTrttCarreraId(Integer trttCarreraId) {
		this.trttCarreraId = trttCarreraId;
	}

	public String getTrttObservacionDga() {
		return trttObservacionDga;
	}

	public void setTrttObservacionDga(String trttObservacionDga) {
		this.trttObservacionDga = trttObservacionDga;
	}

	public Integer getAsttId() {
		return asttId;
	}

	public void setAsttId(Integer asttId) {
		this.asttId = asttId;
	}

	public String getAsttTemaTrabajo() {
		return asttTemaTrabajo;
	}

	public void setAsttTemaTrabajo(String asttTemaTrabajo) {
		this.asttTemaTrabajo = asttTemaTrabajo;
	}

	public String getAsttTutor() {
		return asttTutor;
	}

	public void setAsttTutor(String asttTutor) {
		this.asttTutor = asttTutor;
	}

	public Integer getMcttcrId() {
		return mcttcrId;
	}

	public void setMcttcrId(Integer mcttcrId) {
		this.mcttcrId = mcttcrId;
	}

	public Integer getMcttcrEstado() {
		return mcttcrEstado;
	}

	public void setMcttcrEstado(Integer mcttcrEstado) {
		this.mcttcrEstado = mcttcrEstado;
	}

	public int getMcttcrCrrId() {
		return mcttcrCrrId;
	}

	public void setMcttcrCrrId(int mcttcrCrrId) {
		this.mcttcrCrrId = mcttcrCrrId;
	}

	public Integer getMcttId() {
		return mcttId;
	}

	public void setMcttId(Integer mcttId) {
		this.mcttId = mcttId;
	}

	public String getMcttCodigoSniese() {
		return mcttCodigoSniese;
	}

	public void setMcttCodigoSniese(String mcttCodigoSniese) {
		this.mcttCodigoSniese = mcttCodigoSniese;
	}

	public String getMcttdescripcion() {
		return mcttdescripcion;
	}

	public void setMcttdescripcion(String mcttdescripcion) {
		this.mcttdescripcion = mcttdescripcion;
	}

	public Integer getMcttEstado() {
		return mcttEstado;
	}

	public void setMcttEstado(Integer mcttEstado) {
		this.mcttEstado = mcttEstado;
	}

	public String getEstadoReporte() {
		return estadoReporte;
	}

	public void setEstadoReporte(String estadoReporte) {
		this.estadoReporte = estadoReporte;
	}

	public String getValidacion_mallaReporte() {
		return validacion_mallaReporte;
	}

	public void setValidacion_mallaReporte(String validacion_mallaReporte) {
		this.validacion_mallaReporte = validacion_mallaReporte;
	}

	public String getVldComplexivoReporte() {
		return vldComplexivoReporte;
	}

	public void setVldComplexivoReporte(String vldComplexivoReporte) {
		this.vldComplexivoReporte = vldComplexivoReporte;
	}

	public String getValidacion_tutorReporte() {
		return validacion_tutorReporte;
	}

	public void setValidacion_tutorReporte(String validacion_tutorReporte) {
		this.validacion_tutorReporte = validacion_tutorReporte;
	}

	public String getValidacion_ultimoReporte() {
		return validacion_ultimoReporte;
	}

	public void setValidacion_ultimoReporte(String validacion_ultimoReporte) {
		this.validacion_ultimoReporte = validacion_ultimoReporte;
	}

	public String getTema_asignadoReporte() {
		return tema_asignadoReporte;
	}

	public void setTema_asignadoReporte(String tema_asignadoReporte) {
		this.tema_asignadoReporte = tema_asignadoReporte;
	}

	public String getTutor_asignadoReporte() {
		return tutor_asignadoReporte;
	}

	public void setTutor_asignadoReporte(String tutor_asignadoReporte) {
		this.tutor_asignadoReporte = tutor_asignadoReporte;
	}

	public Integer getAptId() {
		return aptId;
	}

	public void setAptId(Integer aptId) {
		this.aptId = aptId;
	}


	public String getTrttObsDesactivarDga() {
		return trttObsDesactivarDga;
	}

	public void setTrttObsDesactivarDga(String trttObsDesactivarDga) {
		this.trttObsDesactivarDga = trttObsDesactivarDga;
	}

	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}





	public Integer getAptRequisitos() {
		return aptRequisitos;
	}





	public void setAptRequisitos(Integer aptRequisitos) {
		this.aptRequisitos = aptRequisitos;
	}





	public Integer getAptSuficienciaIngles() {
		return aptSuficienciaIngles;
	}





	public void setAptSuficienciaIngles(Integer aptSuficienciaIngles) {
		this.aptSuficienciaIngles = aptSuficienciaIngles;
	}

	public Integer getAptFinMalla() {
		return aptFinMalla;
	}

	public void setAptFinMalla(Integer aptFinMalla) {
		this.aptFinMalla = aptFinMalla;
	}

	public Integer getAptAproboActualizar() {
		return aptAproboActualizar;
	}

	public void setAptAproboActualizar(Integer aptAproboActualizar) {
		this.aptAproboActualizar = aptAproboActualizar;
	}

	public Integer getAptAproboTutor() {
		return aptAproboTutor;
	}

	public void setAptAproboTutor(Integer aptAproboTutor) {
		this.aptAproboTutor = aptAproboTutor;
	}

	public BigDecimal getAptNotaDirector() {
		return aptNotaDirector;
	}

	public void setAptNotaDirector(BigDecimal aptNotaDirector) {
		this.aptNotaDirector = aptNotaDirector;
	}
	
}
