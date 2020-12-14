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
   
 ARCHIVO:     PostulacionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para la postulacion. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018	 		 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;

/**
 * Clase (DTO) PostulacionDto.
 * DTO encargado de manejar los datos para la postulacion.
 * @author dalbuja.
 * @version 1.0
 */
public class PostulacionDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
	//****************** FICHA ESTUDIANTE *****************************
	private Integer fcesId;
	private Integer fcesTipoColegio;
	private Integer fcesTipoColegioSniese;
	private Integer fcesTipoIdentificacionSniese;
	private Integer fcesTituloBachiller;
	private Timestamp fcesFechaCreacion;
	private Integer fcesUbicacionResidencia;
	private Integer fcesPrsId;
	private Integer fcesTrttId;
	private Date fcesFechaEgresamiento;
	
	
	//****************** TRAMITE TITULO *****************************
	private Integer trttId;
	private String trttNumTramite;
	private Integer trttEstadoTramite;
	private Integer trttEstadoProceso;
	private Integer trttCnvId;
	private String trttCrrCodSniese;
	private Integer trttCrrCarreraId;
	
	//****************** PROCESO TRAMITE *****************************
	private Integer prtrId;
	private Integer prtrTipoProceso;
	private Timestamp prtrFechaEjecucion;
	private String prtrRegTtlSenecyt;


	private Integer prtrRoflcrId;
	private Integer prtrTrttId;
	
	//****************** FACULTAD *****************************
	private String fclDescripcion;
	
	
	//****************** POSTULACION *****************************
	private Integer pstTipoColegio;
	private String pstNombreColegio;
	private Integer pstUniversidadId;
	private Integer pstTipoUniversidad;
	private String pstNombreUniversidad;
	private Integer pstComprobanteId;
	private String pstDireccion;
	private Integer pstPrtrId;
	
	//****************** COMPROBANTE *****************************
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
	
	//****************** RUTA IMAGEN *****************************
	private String rutaImagen;
	
	private String prsUbicacionFoto;
	private byte[] prsFoto;
	
	public PostulacionDto(){
	
	}




	public Integer getFcesId() {
		return fcesId;
	}

	public void setFcesId(Integer fcesId) {
		this.fcesId = fcesId;
	}

	public Integer getFcesTipoColegio() {
		return fcesTipoColegio;
	}

	public void setFcesTipoColegio(Integer fcesTipoColegio) {
		this.fcesTipoColegio = fcesTipoColegio;
	}

	public Integer getFcesTipoColegioSniese() {
		return fcesTipoColegioSniese;
	}

	public void setFcesTipoColegioSniese(Integer fcesTipoColegioSniese) {
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
	}

	public Integer getFcesTipoIdentificacionSniese() {
		return fcesTipoIdentificacionSniese;
	}

	public void setFcesTipoIdentificacionSniese(Integer fcesTipoIdentificacionSniese) {
		this.fcesTipoIdentificacionSniese = fcesTipoIdentificacionSniese;
	}

	public Integer getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}

	public void setFcesTituloBachiller(Integer fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}


	public Timestamp getFcesFechaCreacion() {
		return fcesFechaCreacion;
	}

	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}

	public Integer getFcesUbicacionResidencia() {
		return fcesUbicacionResidencia;
	}

	public void setFcesUbicacionResidencia(Integer fcesUbicacionResidencia) {
		this.fcesUbicacionResidencia = fcesUbicacionResidencia;
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


	public String getTrttCrrCodSniese() {
		return trttCrrCodSniese;
	}

	public void setTrttCrrCodSniese(String trttCrrCodSniese) {
		this.trttCrrCodSniese = trttCrrCodSniese;
	}


	public Integer getTrttCrrCarreraId() {
		return trttCrrCarreraId;
	}

	public void setTrttCrrCarreraId(Integer trttCrrCarreraId) {
		this.trttCrrCarreraId = trttCrrCarreraId;
	}

	public Date getFcesFechaEgresamiento() {
		return fcesFechaEgresamiento;
	}

	public void setFcesFechaEgresamiento(Date fcesFechaEgresamiento) {
		this.fcesFechaEgresamiento = fcesFechaEgresamiento;
	}


	public Integer getPstTipoColegio() {
		return pstTipoColegio;
	}


	public void setPstTipoColegio(Integer pstTipoColegio) {
		this.pstTipoColegio = pstTipoColegio;
	}


	public String getPstNombreColegio() {
		return pstNombreColegio;
	}


	public void setPstNombreColegio(String pstNombreColegio) {
		this.pstNombreColegio = pstNombreColegio;
	}


	public Integer getPstTipoUniversidad() {
		return pstTipoUniversidad;
	}


	public void setPstTipoUniversidad(Integer pstTipoUniversidad) {
		this.pstTipoUniversidad = pstTipoUniversidad;
	}


	public String getPstNombreUniversidad() {
		return pstNombreUniversidad;
	}


	public void setPstNombreUniversidad(String pstNombreUniversidad) {
		this.pstNombreUniversidad = pstNombreUniversidad;
	}


	public Integer getPstComprobanteId() {
		return pstComprobanteId;
	}


	public void setPstComprobanteId(Integer pstComprobanteId) {
		this.pstComprobanteId = pstComprobanteId;
	}


	public String getPstDireccion() {
		return pstDireccion;
	}


	public void setPstDireccion(String pstDireccion) {
		this.pstDireccion = pstDireccion;
	}


	public Integer getPstPrtrId() {
		return pstPrtrId;
	}


	public void setPstPrtrId(Integer pstPrtrId) {
		this.pstPrtrId = pstPrtrId;
	}

	public String getPrtrRegTtlSenecyt() {
		return prtrRegTtlSenecyt;
	}

	public void setPrtrRegTtlSenecyt(String prtrRegTtlSenecyt) {
		this.prtrRegTtlSenecyt = prtrRegTtlSenecyt;
	}
	public Integer getPstUniversidadId() {
		return pstUniversidadId;
	}

	public void setPstUniversidadId(Integer pstUniversidadId) {
		this.pstUniversidadId = pstUniversidadId;
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

	public String getFclDescripcion() {
		return fclDescripcion;
	}

	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}
	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public String getPrsUbicacionFoto() {
		return prsUbicacionFoto;
	}

	public void setPrsUbicacionFoto(String prsUbicacionFoto) {
		this.prsUbicacionFoto = prsUbicacionFoto;
	}

	public byte[] getPrsFoto() {
		return prsFoto;
	}

	public void setPrsFoto(byte[] prsFoto) {
		this.prsFoto = prsFoto;
	}
	
}
