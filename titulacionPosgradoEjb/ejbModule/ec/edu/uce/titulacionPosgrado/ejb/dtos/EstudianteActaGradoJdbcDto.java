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
 DESCRIPCION: DTO encargado de manejar los datos del estudiante acta de grado. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-04-2018 		Daniel Albuj 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) EstudianteActaGradoJdbcDto.
 * DTO encargado de manejar los datos del estudiante para el acta de grado.
 * @author dalbuja.
 * @version 1.0
 */
public class EstudianteActaGradoJdbcDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
			
		//****************** PERSONA *****************************
		private Integer prsId;
		private Integer prsTipoIdentificacion;
		private Integer prsTipoIdentificacionSniese;
		private String prsIdentificacion;
		private String prsPrimerApellido;
		private String prsSegundoApellido;
		private String prsNombres;
		private Integer prsSexo;
		private Integer prsSexoSniese;	
		private String prsMailPersonal;
		private String prsMailInstitucional;
		private String prsTelefono;
		private Date prsFechaNacimiento;
		private Integer prsEtnId;
		private Integer prsUbcId;
		private String prsUbicacionFoto;
		
		//****************** ETNIA *****************************
		private Integer etnId;
		private String etnDescripcion;
		private String etnCodigoSniese;
				
		//****************** INSTITUCION ACADEMICA *****************************
		private Integer inacId;
		private String inacCodigoSniese;
		private String inacDescripcion;
		private Integer inacNivel;
		private Integer inacTipo;
		private String inacTipoSniese;
		private Integer inacubcId;
		
		//****************** UBICACION NACIONALIDAD*****************************
		private Integer ubcId;
		private String ubcDescripcion;
		private Integer ubcJerarquia;
		private String ubcGentilicio;
		private String ubcCodSniese;
		private Integer ubcPadre;
		
		//****************** UBICACION CANTON*****************************
		private Integer ubcCantonId;
		private String ubcCantonDescripcion;
		private Integer ubcCantonJerarquia;
		private String ubcCantonGentilicio;
		private String ubcCantonCodSniese;
		private Integer ubcCantonPadre;
		
		//****************** UBICACION PROVINCIA*****************************
		private Integer ubcProvId;
		private String ubcProvDescripcion;
		private Integer ubcProvJerarquia;
		private String ubcProvGentilicio;
		private String ubcProvCodSniese;
		private Integer ubcProvPadre;
		
		//****************** UBICACION PAIS*****************************
		private Integer ubcPaisId;
		private String ubcPaisDescripcion;
		private Integer ubcPaisJerarquia;
		private String ubcPaisGentilicio;
		private String ubcPaisCodSniese;
		private Integer ubcPaisPadre;
		
	
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
		private String fcesTiempoEstudRecSt;
		private Integer fcesTipoDuracionRec;
		private String fcesTipoDuracionRecSt;
		private Integer fcesTipoColegio;
		private String fcesTipoColegioSniese;
		private BigDecimal fcesNotaPromAcumulado;
		private BigDecimal fcesNotaTrabTitulacion;
		private String fcesLinkTesis;
		private Integer fcesRecEstuPrevios;
		private String fcesRecEstuPreviosSniese;
		private Timestamp fcesFechaCreacion;
		private Integer fcesUbcCantonResidencia;
		private Integer fcesMcttcrId;
		private Integer fcesInacIdInstEstPrevios;
		private Integer fcesPrsId;
		private Integer fcesTrttId;
		private Integer fcesCncrId;
		private Integer fcesTituloBachillerId;
		private String fcesHoraActaGrado;
		private String fcesTituloBachiller;
		
		//****************** TRAMITE TITULO *****************************
		private Integer trttId;
		private String trttNumTramite;
		private Integer trttEstadoTramite;
		private Integer trttEstadoProceso;
		private Integer trttSubId;
		private Integer trttCrrCodSniese;
		private String trttObsValSec;
		private Integer trttCarrera;
		private Integer trttCarreraId;
		private Integer trttMcttEstadistico;
		private String trttMcttEstadisticoSt;
		private Integer trttCnvId;
		private String trttObservacionDga;
	
		//****************** PROCESO TRÁMITE *****************************
		private Integer prtrId;
		private Integer prtrTipoProceso;
		private Timestamp prtrFechaEjecucion;
		private Integer prtrRoflcrId;
		private Integer prtrTrttId;
		private String fechaAsignacionTitulacion;
	
		//****************** CONVOCATORIA *****************************
		private Integer cnvId;
		private String cnvDescripcion;
		private Date cnvFechaInicio;
		private Date cnvFechaFin;
		private Integer cnvEstado;
		private Integer cnvEstadoFase;
	
		//****************** CARRRERA *****************************
		private Integer crrId;
		private String crrCodSniese;
		private String crrDescripcion;
		private String crrDetalle;
		private Integer crrFclId;
	
		//****************** FACULTAD *****************************
		private Integer fclId;
		private String fclDescripcion;
	
		//****************** TITULO *****************************
		private Integer ttlId;
		private String ttlDescripcion;
		private Integer ttlSexo;
		private Integer ttlEstado;
		private Integer ttlTipo;
		
		//****************** MECANISMO_TITULACION_CARRERA *****************************
		private Integer mcttcrId;
		private Integer mcttcrEstado;
		private Integer mcttcrCrrId;
		private Integer mcttcrMcttId;
		private Integer mcttcrPorcentaje;

		//****************** MECANISMO_TITULACION *****************************
		private Integer mcttId;
		private String mcttCodigoSniese;
		private String mcttDescripcion;
		private Integer mcttEstado;
	
		//****************** ASIGNACION_TITULACION *****************************
		private Integer asttId;
		private String asttTemaTrabajo;
		private String asttTutor;
		private Integer asttPrtr;
		private Integer asttAprobacionTutor;
		
		//****************** FICHA_DCN_ASG_TITULACION *****************************
		private Integer fcdcasttId;
		private Integer fcdcasttTipoTribunal;
		private Integer fcdcasttAsttId;
		private Integer fcdcasttFcdcId;
		
		//****************** FICHA_DCN_ASG_TITULACION *****************************
		private Integer fcdcId;
		private Integer fcdcEstado;
		private Integer fcdcAbreviaturaTitulo;
		private Integer fcdcCrrId;
		private Integer fcdcPrsId;
		
		//****************** ASENTAMIENTO_NOTA *****************************
		private int asnoId;
		private BigDecimal asnoPrmDfnOral;
		private BigDecimal asnoPrmDfnEscrito;
		private BigDecimal asnoTrabTitulacionFinal;
		
		
	public EstudianteActaGradoJdbcDto(){
	
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



	public Integer getPrsEtnId() {
		return prsEtnId;
	}



	public void setPrsEtnId(Integer prsEtnId) {
		this.prsEtnId = prsEtnId;
	}



	public Integer getPrsUbcId() {
		return prsUbcId;
	}



	public void setPrsUbcId(Integer prsUbcId) {
		this.prsUbcId = prsUbcId;
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



	public String getEtnCodigoSniese() {
		return etnCodigoSniese;
	}



	public void setEtnCodigoSniese(String etnCodigoSniese) {
		this.etnCodigoSniese = etnCodigoSniese;
	}



	public Integer getInacId() {
		return inacId;
	}



	public void setInacId(Integer inacId) {
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



	public Integer getInacubcId() {
		return inacubcId;
	}



	public void setInacubcId(Integer inacubcId) {
		this.inacubcId = inacubcId;
	}



	public Integer getUbcId() {
		return ubcId;
	}



	public void setUbcId(Integer ubcId) {
		this.ubcId = ubcId;
	}



	public String getUbcDescripcion() {
		return ubcDescripcion;
	}



	public void setUbcDescripcion(String ubcDescripcion) {
		this.ubcDescripcion = ubcDescripcion;
	}



	public Integer getUbcJerarquia() {
		return ubcJerarquia;
	}



	public void setUbcJerarquia(Integer ubcJerarquia) {
		this.ubcJerarquia = ubcJerarquia;
	}



	public String getUbcGentilicio() {
		return ubcGentilicio;
	}



	public void setUbcGentilicio(String ubcGentilicio) {
		this.ubcGentilicio = ubcGentilicio;
	}



	public String getUbcCodSniese() {
		return ubcCodSniese;
	}



	public void setUbcCodSniese(String ubcCodSniese) {
		this.ubcCodSniese = ubcCodSniese;
	}



	public Integer getUbcPadre() {
		return ubcPadre;
	}



	public void setUbcPadre(Integer ubcPadre) {
		this.ubcPadre = ubcPadre;
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



	public Integer getUbcCantonJerarquia() {
		return ubcCantonJerarquia;
	}



	public void setUbcCantonJerarquia(Integer ubcCantonJerarquia) {
		this.ubcCantonJerarquia = ubcCantonJerarquia;
	}



	public String getUbcCantonGentilicio() {
		return ubcCantonGentilicio;
	}



	public void setUbcCantonGentilicio(String ubcCantonGentilicio) {
		this.ubcCantonGentilicio = ubcCantonGentilicio;
	}



	public String getUbcCantonCodSniese() {
		return ubcCantonCodSniese;
	}



	public void setUbcCantonCodSniese(String ubcCantonCodSniese) {
		this.ubcCantonCodSniese = ubcCantonCodSniese;
	}



	public Integer getUbcCantonPadre() {
		return ubcCantonPadre;
	}



	public void setUbcCantonPadre(Integer ubcCantonPadre) {
		this.ubcCantonPadre = ubcCantonPadre;
	}



	public Integer getUbcProvId() {
		return ubcProvId;
	}



	public void setUbcProvId(Integer ubcProvId) {
		this.ubcProvId = ubcProvId;
	}



	public String getUbcProvDescripcion() {
		return ubcProvDescripcion;
	}



	public void setUbcProvDescripcion(String ubcProvDescripcion) {
		this.ubcProvDescripcion = ubcProvDescripcion;
	}



	public Integer getUbcProvJerarquia() {
		return ubcProvJerarquia;
	}



	public void setUbcProvJerarquia(Integer ubcProvJerarquia) {
		this.ubcProvJerarquia = ubcProvJerarquia;
	}



	public String getUbcProvGentilicio() {
		return ubcProvGentilicio;
	}



	public void setUbcProvGentilicio(String ubcProvGentilicio) {
		this.ubcProvGentilicio = ubcProvGentilicio;
	}



	public String getUbcProvCodSniese() {
		return ubcProvCodSniese;
	}



	public void setUbcProvCodSniese(String ubcProvCodSniese) {
		this.ubcProvCodSniese = ubcProvCodSniese;
	}



	public Integer getUbcProvPadre() {
		return ubcProvPadre;
	}



	public void setUbcProvPadre(Integer ubcProvPadre) {
		this.ubcProvPadre = ubcProvPadre;
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



	public Integer getUbcPaisJerarquia() {
		return ubcPaisJerarquia;
	}



	public void setUbcPaisJerarquia(Integer ubcPaisJerarquia) {
		this.ubcPaisJerarquia = ubcPaisJerarquia;
	}



	public String getUbcPaisGentilicio() {
		return ubcPaisGentilicio;
	}



	public void setUbcPaisGentilicio(String ubcPaisGentilicio) {
		this.ubcPaisGentilicio = ubcPaisGentilicio;
	}



	public String getUbcPaisCodSniese() {
		return ubcPaisCodSniese;
	}



	public void setUbcPaisCodSniese(String ubcPaisCodSniese) {
		this.ubcPaisCodSniese = ubcPaisCodSniese;
	}



	public Integer getUbcPaisPadre() {
		return ubcPaisPadre;
	}



	public void setUbcPaisPadre(Integer ubcPaisPadre) {
		this.ubcPaisPadre = ubcPaisPadre;
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



	public Timestamp getFcesFechaCreacion() {
		return fcesFechaCreacion;
	}



	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}



	public Integer getFcesUbcCantonResidencia() {
		return fcesUbcCantonResidencia;
	}



	public void setFcesUbcCantonResidencia(Integer fcesUbcCantonResidencia) {
		this.fcesUbcCantonResidencia = fcesUbcCantonResidencia;
	}



	public Integer getFcesMcttcrId() {
		return fcesMcttcrId;
	}



	public void setFcesMcttcrId(Integer fcesMcttcrId) {
		this.fcesMcttcrId = fcesMcttcrId;
	}



	public Integer getFcesInacIdInstEstPrevios() {
		return fcesInacIdInstEstPrevios;
	}



	public void setFcesInacIdInstEstPrevios(Integer fcesInacIdInstEstPrevios) {
		this.fcesInacIdInstEstPrevios = fcesInacIdInstEstPrevios;
	}



	public Integer getFcesPrsId() {
		return fcesPrsId;
	}



	public void setFcesPrsId(Integer fcesPrsId) {
		this.fcesPrsId = fcesPrsId;
	}



	public Integer getFcesTrttId() {
		return fcesTrttId;
	}



	public void setFcesTrttId(Integer fcesTrttId) {
		this.fcesTrttId = fcesTrttId;
	}



	public Integer getFcesCncrId() {
		return fcesCncrId;
	}



	public void setFcesCncrId(Integer fcesCncrId) {
		this.fcesCncrId = fcesCncrId;
	}



	public Integer getFcesTituloBachillerId() {
		return fcesTituloBachillerId;
	}



	public void setFcesTituloBachillerId(Integer fcesTituloBachillerId) {
		this.fcesTituloBachillerId = fcesTituloBachillerId;
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



	public Integer getTrttSubId() {
		return trttSubId;
	}



	public void setTrttSubId(Integer trttSubId) {
		this.trttSubId = trttSubId;
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



	public Integer getTrttCnvId() {
		return trttCnvId;
	}



	public void setTrttCnvId(Integer trttCnvId) {
		this.trttCnvId = trttCnvId;
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



	public String getFechaAsignacionTitulacion() {
		return fechaAsignacionTitulacion;
	}



	public void setFechaAsignacionTitulacion(String fechaAsignacionTitulacion) {
		this.fechaAsignacionTitulacion = fechaAsignacionTitulacion;
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



	public String getCrrCodSniese() {
		return crrCodSniese;
	}



	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
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



	public Integer getCrrFclId() {
		return crrFclId;
	}



	public void setCrrFclId(Integer crrFclId) {
		this.crrFclId = crrFclId;
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



	public Integer getTtlSexo() {
		return ttlSexo;
	}



	public void setTtlSexo(Integer ttlSexo) {
		this.ttlSexo = ttlSexo;
	}



	public Integer getTtlEstado() {
		return ttlEstado;
	}



	public void setTtlEstado(Integer ttlEstado) {
		this.ttlEstado = ttlEstado;
	}



	public Integer getTtlTipo() {
		return ttlTipo;
	}



	public void setTtlTipo(Integer ttlTipo) {
		this.ttlTipo = ttlTipo;
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



	public Integer getMcttcrCrrId() {
		return mcttcrCrrId;
	}



	public void setMcttcrCrrId(Integer mcttcrCrrId) {
		this.mcttcrCrrId = mcttcrCrrId;
	}



	public Integer getMcttcrMcttId() {
		return mcttcrMcttId;
	}



	public void setMcttcrMcttId(Integer mcttcrMcttId) {
		this.mcttcrMcttId = mcttcrMcttId;
	}



	public Integer getMcttcrPorcentaje() {
		return mcttcrPorcentaje;
	}



	public void setMcttcrPorcentaje(Integer mcttcrPorcentaje) {
		this.mcttcrPorcentaje = mcttcrPorcentaje;
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



	public Integer getAsttPrtr() {
		return asttPrtr;
	}



	public void setAsttPrtr(Integer asttPrtr) {
		this.asttPrtr = asttPrtr;
	}



	public Integer getAsttAprobacionTutor() {
		return asttAprobacionTutor;
	}



	public void setAsttAprobacionTutor(Integer asttAprobacionTutor) {
		this.asttAprobacionTutor = asttAprobacionTutor;
	}



	public Integer getFcdcasttId() {
		return fcdcasttId;
	}



	public void setFcdcasttId(Integer fcdcasttId) {
		this.fcdcasttId = fcdcasttId;
	}



	public Integer getFcdcasttTipoTribunal() {
		return fcdcasttTipoTribunal;
	}



	public void setFcdcasttTipoTribunal(Integer fcdcasttTipoTribunal) {
		this.fcdcasttTipoTribunal = fcdcasttTipoTribunal;
	}



	public Integer getFcdcasttAsttId() {
		return fcdcasttAsttId;
	}



	public void setFcdcasttAsttId(Integer fcdcasttAsttId) {
		this.fcdcasttAsttId = fcdcasttAsttId;
	}



	public Integer getFcdcasttFcdcId() {
		return fcdcasttFcdcId;
	}



	public void setFcdcasttFcdcId(Integer fcdcasttFcdcId) {
		this.fcdcasttFcdcId = fcdcasttFcdcId;
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



	public Integer getFcdcAbreviaturaTitulo() {
		return fcdcAbreviaturaTitulo;
	}



	public void setFcdcAbreviaturaTitulo(Integer fcdcAbreviaturaTitulo) {
		this.fcdcAbreviaturaTitulo = fcdcAbreviaturaTitulo;
	}



	public Integer getFcdcCrrId() {
		return fcdcCrrId;
	}



	public void setFcdcCrrId(Integer fcdcCrrId) {
		this.fcdcCrrId = fcdcCrrId;
	}

	public Integer getFcdcPrsId() {
		return fcdcPrsId;
	}

	public void setFcdcPrsId(Integer fcdcPrsId) {
		this.fcdcPrsId = fcdcPrsId;
	}

	public String getFcesTipoDuracionRecSt() {
		return fcesTipoDuracionRecSt;
	}

	public void setFcesTipoDuracionRecSt(String fcesTipoDuracionRecSt) {
		this.fcesTipoDuracionRecSt = fcesTipoDuracionRecSt;
	}

	public String getFcesTiempoEstudRecSt() {
		return fcesTiempoEstudRecSt;
	}

	public void setFcesTiempoEstudRecSt(String fcesTiempoEstudRecSt) {
		this.fcesTiempoEstudRecSt = fcesTiempoEstudRecSt;
	}

	public String getFcesHoraActaGrado() {
		return fcesHoraActaGrado;
	}

	public void setFcesHoraActaGrado(String fcesHoraActaGrado) {
		this.fcesHoraActaGrado = fcesHoraActaGrado;
	}

	public String getTrttObservacionDga() {
		return trttObservacionDga;
	}

	public void setTrttObservacionDga(String trttObservacionDga) {
		this.trttObservacionDga = trttObservacionDga;
	}

	public String getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}

	public void setFcesTituloBachiller(String fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}

	public BigDecimal getAsnoPrmDfnOral() {
		return asnoPrmDfnOral;
	}

	public void setAsnoPrmDfnOral(BigDecimal asnoPrmDfnOral) {
		this.asnoPrmDfnOral = asnoPrmDfnOral;
	}

	public BigDecimal getAsnoPrmDfnEscrito() {
		return asnoPrmDfnEscrito;
	}

	public void setAsnoPrmDfnEscrito(BigDecimal asnoPrmDfnEscrito) {
		this.asnoPrmDfnEscrito = asnoPrmDfnEscrito;
	}

	public BigDecimal getAsnoTrabTitulacionFinal() {
		return asnoTrabTitulacionFinal;
	}

	public void setAsnoTrabTitulacionFinal(BigDecimal asnoTrabTitulacionFinal) {
		this.asnoTrabTitulacionFinal = asnoTrabTitulacionFinal;
	}

	public int getAsnoId() {
		return asnoId;
	}

	public void setAsnoId(int asnoId) {
		this.asnoId = asnoId;
	}





	public String getPrsUbicacionFoto() {
		return prsUbicacionFoto;
	}





	public void setPrsUbicacionFoto(String prsUbicacionFoto) {
		this.prsUbicacionFoto = prsUbicacionFoto;
	}




}