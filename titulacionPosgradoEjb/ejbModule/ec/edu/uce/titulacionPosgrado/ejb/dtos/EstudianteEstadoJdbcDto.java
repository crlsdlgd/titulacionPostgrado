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
   
 ARCHIVO:     EstudianteEstadoJdbcDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos del postulante 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 02-07-2017 			 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;

/**
 * Clase (DTO) EstudianteEstadoJdbcDto.
 * DTO encargado de manejar los datos del postulante
 * @author dalbuja.
 * @version 1.0
 */
public class EstudianteEstadoJdbcDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
	//****************** FICHA ESTUDIANTE *****************************
	private Integer fcesId;
	private Integer fcesUbcCantonResidencia;
	private String fcesRegTtlSenecyt;
	
	//****************** COMPROBANTE *****************************
	private int cmpId;
	private String cmpNumComprobante;
	private Integer cmpNumComprobanteSecuencial;
	private String cmpDescripcion;
	private BigDecimal cmpTotalPago;
	private Integer cmpEstado;
	private TramiteTitulo cmpTramiteTitulo;
	private BigDecimal cmpTotalFacultad;
	private Integer cmpProcSau;
	private Integer cmpTipoUnidad;
	private BigDecimal cmpValorPagado;
	private Integer cmpCantidad;
	private Integer cmpIdArancel;
	private Integer cmpPaiCodigo;
	private Integer cmpAplicaGratuidad;
	private Integer cmpMatrTipo;
	private Integer cmpModalidad;
	private Integer cmpEspeCodigo;
	private Timestamp cmpFechaCaduca; 
	private Timestamp cmpFechaEmision;
	
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
	
	//****************** ETNIA *****************************
	private Integer etnId;
	private String etnDescripcion;
	
	//****************** INSTITUCION ACADEMICA *****************************
	private Integer inacUniversidadId;
	private String inacUniversidadDescripcion;
	
	//****************** PERSONA *****************************
	private int prsId;
	private Integer prsTipoIdentificacion;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsNombres;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private String prsCelular;
	private Date prsFechaNacimiento;
	private Integer prsSexo;
	private Integer prsSexoSniese;
	private String prsSegundoApellido;
	private Integer prsEtnId;
	private Integer prsUbcId;
	private Integer prsDiscapacidad;
	private Integer prsTipoDiscapacidad;
	private Integer prsPocentajeDiscapacidad;
	private String prsCarnetConadis;
	
		
	//****************** TRAMITE TITULO *****************************
	private int trttId;
	private String trttNumTramite;
	private String trttObsValSecAbg;
	private Integer trttEstadoTramite;
	private Integer trttEstadoProceso;
	private Integer trttCarreraId;
	
	//****************** PROCESO TRÁMITE *****************************
	private int prtrId;
	private Integer prtrTipoProceso;
	private Timestamp prtrFechaEjecucion;
	
	
	//****************** CONVOCATORIA *****************************
	private int cnvId;
	private String cnvDescripcion;
	private Date cnvFechaInicio;
	private Date cnvFechaFin;
	private Integer cnvEstado;
	private Integer cnvEstadoFase;
	
	//****************** CARRRERA *****************************
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private Integer crrNivel;
	private Integer crrEspeCodigo;
	private String crrDetalle;
	
	//****************** FACULTAD *****************************
	private int fclId;
	private String fclDescripcion;
	private String fclCodSori;
	private String fclUej;
	private Integer fclEstado;
	
	//****************** MERITOS *****************************
	private BigDecimal mrtCalificacion;
	//****************** OPOSICION *****************************
	private Integer opsEntrevista;
	private BigDecimal opsNotaFinal;
	private BigDecimal opsNotaOposicion;
	

	public EstudianteEstadoJdbcDto(){
		
	}



	public int getCmpId() {
		return cmpId;
	}



	public void setCmpId(int cmpId) {
		this.cmpId = cmpId;
	}



	public String getCmpNumComprobante() {
		return cmpNumComprobante;
	}



	public void setCmpNumComprobante(String cmpNumComprobante) {
		this.cmpNumComprobante = cmpNumComprobante;
	}



	public Integer getCmpNumComprobanteSecuencial() {
		return cmpNumComprobanteSecuencial;
	}



	public void setCmpNumComprobanteSecuencial(Integer cmpNumComprobanteSecuencial) {
		this.cmpNumComprobanteSecuencial = cmpNumComprobanteSecuencial;
	}



	public String getCmpDescripcion() {
		return cmpDescripcion;
	}



	public void setCmpDescripcion(String cmpDescripcion) {
		this.cmpDescripcion = cmpDescripcion;
	}



	public BigDecimal getCmpTotalPago() {
		return cmpTotalPago;
	}



	public void setCmpTotalPago(BigDecimal cmpTotalPago) {
		this.cmpTotalPago = cmpTotalPago;
	}



	public Integer getCmpEstado() {
		return cmpEstado;
	}



	public void setCmpEstado(Integer cmpEstado) {
		this.cmpEstado = cmpEstado;
	}



	public TramiteTitulo getCmpTramiteTitulo() {
		return cmpTramiteTitulo;
	}



	public void setCmpTramiteTitulo(TramiteTitulo cmpTramiteTitulo) {
		this.cmpTramiteTitulo = cmpTramiteTitulo;
	}



	public BigDecimal getCmpTotalFacultad() {
		return cmpTotalFacultad;
	}



	public void setCmpTotalFacultad(BigDecimal cmpTotalFacultad) {
		this.cmpTotalFacultad = cmpTotalFacultad;
	}



	public Integer getCmpProcSau() {
		return cmpProcSau;
	}



	public void setCmpProcSau(Integer cmpProcSau) {
		this.cmpProcSau = cmpProcSau;
	}



	public Integer getCmpTipoUnidad() {
		return cmpTipoUnidad;
	}



	public void setCmpTipoUnidad(Integer cmpTipoUnidad) {
		this.cmpTipoUnidad = cmpTipoUnidad;
	}



	public BigDecimal getCmpValorPagado() {
		return cmpValorPagado;
	}



	public void setCmpValorPagado(BigDecimal cmpValorPagado) {
		this.cmpValorPagado = cmpValorPagado;
	}



	public Integer getCmpCantidad() {
		return cmpCantidad;
	}



	public void setCmpCantidad(Integer cmpCantidad) {
		this.cmpCantidad = cmpCantidad;
	}



	public Integer getCmpIdArancel() {
		return cmpIdArancel;
	}



	public void setCmpIdArancel(Integer cmpIdArancel) {
		this.cmpIdArancel = cmpIdArancel;
	}



	public Integer getCmpPaiCodigo() {
		return cmpPaiCodigo;
	}



	public void setCmpPaiCodigo(Integer cmpPaiCodigo) {
		this.cmpPaiCodigo = cmpPaiCodigo;
	}



	public Integer getCmpAplicaGratuidad() {
		return cmpAplicaGratuidad;
	}



	public void setCmpAplicaGratuidad(Integer cmpAplicaGratuidad) {
		this.cmpAplicaGratuidad = cmpAplicaGratuidad;
	}



	public Integer getCmpMatrTipo() {
		return cmpMatrTipo;
	}



	public void setCmpMatrTipo(Integer cmpMatrTipo) {
		this.cmpMatrTipo = cmpMatrTipo;
	}



	public Integer getCmpModalidad() {
		return cmpModalidad;
	}



	public void setCmpModalidad(Integer cmpModalidad) {
		this.cmpModalidad = cmpModalidad;
	}



	public Integer getCmpEspeCodigo() {
		return cmpEspeCodigo;
	}



	public void setCmpEspeCodigo(Integer cmpEspeCodigo) {
		this.cmpEspeCodigo = cmpEspeCodigo;
	}



	public Timestamp getCmpFechaCaduca() {
		return cmpFechaCaduca;
	}



	public void setCmpFechaCaduca(Timestamp cmpFechaCaduca) {
		this.cmpFechaCaduca = cmpFechaCaduca;
	}



	public Timestamp getCmpFechaEmision() {
		return cmpFechaEmision;
	}



	public void setCmpFechaEmision(Timestamp cmpFechaEmision) {
		this.cmpFechaEmision = cmpFechaEmision;
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



	public int getPrsId() {
		return prsId;
	}



	public void setPrsId(int prsId) {
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



	public String getPrsCelular() {
		return prsCelular;
	}



	public void setPrsCelular(String prsCelular) {
		this.prsCelular = prsCelular;
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



	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}



	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
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



	public Integer getPrsDiscapacidad() {
		return prsDiscapacidad;
	}



	public void setPrsDiscapacidad(Integer prsDiscapacidad) {
		this.prsDiscapacidad = prsDiscapacidad;
	}



	public Integer getPrsTipoDiscapacidad() {
		return prsTipoDiscapacidad;
	}



	public void setPrsTipoDiscapacidad(Integer prsTipoDiscapacidad) {
		this.prsTipoDiscapacidad = prsTipoDiscapacidad;
	}



	public Integer getPrsPocentajeDiscapacidad() {
		return prsPocentajeDiscapacidad;
	}



	public void setPrsPocentajeDiscapacidad(Integer prsPocentajeDiscapacidad) {
		this.prsPocentajeDiscapacidad = prsPocentajeDiscapacidad;
	}



	public String getPrsCarnetConadis() {
		return prsCarnetConadis;
	}



	public void setPrsCarnetConadis(String prsCarnetConadis) {
		this.prsCarnetConadis = prsCarnetConadis;
	}



	public int getTrttId() {
		return trttId;
	}



	public void setTrttId(int trttId) {
		this.trttId = trttId;
	}



	public String getTrttNumTramite() {
		return trttNumTramite;
	}



	public void setTrttNumTramite(String trttNumTramite) {
		this.trttNumTramite = trttNumTramite;
	}



	public String getTrttObsValSecAbg() {
		return trttObsValSecAbg;
	}



	public void setTrttObsValSecAbg(String trttObsValSecAbg) {
		this.trttObsValSecAbg = trttObsValSecAbg;
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



	public Integer getTrttCarreraId() {
		return trttCarreraId;
	}



	public void setTrttCarreraId(Integer trttCarreraId) {
		this.trttCarreraId = trttCarreraId;
	}



	public int getPrtrId() {
		return prtrId;
	}



	public void setPrtrId(int prtrId) {
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




	public int getCnvId() {
		return cnvId;
	}



	public void setCnvId(int cnvId) {
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



	public int getCrrId() {
		return crrId;
	}



	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}



	public String getCrrDescripcion() {
		return crrDescripcion;
	}



	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}



	public String getCrrCodSniese() {
		return crrCodSniese;
	}



	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}



	public Integer getCrrNivel() {
		return crrNivel;
	}



	public void setCrrNivel(Integer crrNivel) {
		this.crrNivel = crrNivel;
	}



	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}



	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}



	public String getCrrDetalle() {
		return crrDetalle;
	}



	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}



	public int getFclId() {
		return fclId;
	}



	public void setFclId(int fclId) {
		this.fclId = fclId;
	}



	public String getFclDescripcion() {
		return fclDescripcion;
	}



	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}



	public String getFclCodSori() {
		return fclCodSori;
	}



	public void setFclCodSori(String fclCodSori) {
		this.fclCodSori = fclCodSori;
	}



	public String getFclUej() {
		return fclUej;
	}



	public void setFclUej(String fclUej) {
		this.fclUej = fclUej;
	}



	public Integer getFclEstado() {
		return fclEstado;
	}



	public void setFclEstado(Integer fclEstado) {
		this.fclEstado = fclEstado;
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



	public Integer getFcesId() {
		return fcesId;
	}



	public void setFcesId(Integer fcesId) {
		this.fcesId = fcesId;
	}



	public Integer getFcesUbcCantonResidencia() {
		return fcesUbcCantonResidencia;
	}



	public void setFcesUbcCantonResidencia(Integer fcesUbcCantonResidencia) {
		this.fcesUbcCantonResidencia = fcesUbcCantonResidencia;
	}



	public BigDecimal getMrtCalificacion() {
		return mrtCalificacion;
	}



	public void setMrtCalificacion(BigDecimal mrtCalificacion) {
		this.mrtCalificacion = mrtCalificacion;
	}



	public Integer getOpsEntrevista() {
		return opsEntrevista;
	}



	public void setOpsEntrevista(Integer opsEntrevista) {
		this.opsEntrevista = opsEntrevista;
	}



	public BigDecimal getOpsNotaFinal() {
		return opsNotaFinal;
	}



	public void setOpsNotaFinal(BigDecimal opsNotaFinal) {
		this.opsNotaFinal = opsNotaFinal;
	}



	public BigDecimal getOpsNotaOposicion() {
		return opsNotaOposicion;
	}



	public void setOpsNotaOposicion(BigDecimal opsNotaOposicion) {
		this.opsNotaOposicion = opsNotaOposicion;
	}



	public String getFcesRegTtlSenecyt() {
		return fcesRegTtlSenecyt;
	}



	public void setFcesRegTtlSenecyt(String fcesRegTtlSenecyt) {
		this.fcesRegTtlSenecyt = fcesRegTtlSenecyt;
	}

	
	
}
