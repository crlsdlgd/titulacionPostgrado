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
   
 ARCHIVO:     EstudianteDetalleComplexivoDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro del detalle_complexivo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     	AUTOR          					COMENTARIOS
 24-04-2018 			Daniel Albuja   		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) EstudianteDetalleComplexivoDto.
 * DTO encargado de manejar los datos para el registro del detalle_complexivo.
 * @author dalbuja.
 * @version 1.0
 */
public class EstudianteDetalleComplexivoDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	//****************** DETALLE COMPLEXIVO *****************************
	private int dtcmId;
	private BigDecimal dtcmNotaDirecta;
	private BigDecimal dtcmNotaTransformada; 
	private Integer  dtcmAciertos;
	private Integer dtcmErrores;
	private Integer dtcmBlancos;
	private Integer dtcmDobles;
	private String dtcmPrueba;
	private Integer dtcmTipoDetalle;
	private String dtcmObservacion;
	
	//****************** PERSONA *****************************
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private String prsMailPersonal;
	
	//****************** FICHA ESTUDIANTE *****************************
	private Integer fcesId;
	private BigDecimal fcesNotaTrabTitulacion;
	private Integer fcesMecanismoTitulacionCarrera;
	private BigDecimal fcesNotaPromAcumulado;
	
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
	
	
	//********** MECANISMO_TITULACION_CARRERA ********************
	private Integer mcttcrId;
	private Integer mcttcrPorcentaje;
	
	//********** MECANISMO_TITULACION ********************
	private Integer mcttId;
	
	//********** CARRERA ********************
	private String crrDescripcion;
	private String crrDetalle;
	
	//********** FACULTAD ********************
	private String fclDescripcion;
	
	//********** ASENTAMIENTO_NOTA ********************
	private Integer asnoId;
	private BigDecimal asnoComplexivoPractico;
	private BigDecimal asnoComplexivoTeorico;
	private Timestamp asnoFechaCmpteorico;
	private BigDecimal asnoComplexivoFinal;
	private Timestamp asnoFechaCmpGraciaTeorico;
	private Timestamp asnoFechaCmpGraciaPractico;
	private BigDecimal asnoCmpGraciaTeorico;
	private BigDecimal asnoCmpGraciaPractico;
	private BigDecimal asnoCmpGraciaFinal;
	
	//********** CONVOCATORIA ********************
	private Integer cnvId;
	private String cnvDescripcion;
	
	private String cadenaAux;
	
	private Date asnoFechaTeorico;
	
	public EstudianteDetalleComplexivoDto() {
	}


	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/

	public int getDtcmId() {
		return dtcmId;
	}

	public void setDtcmId(int dtcmId) {
		this.dtcmId = dtcmId;
	}

	public BigDecimal getDtcmNotaDirecta() {
		return dtcmNotaDirecta;
	}

	public void setDtcmNotaDirecta(BigDecimal dtcmNotaDirecta) {
		this.dtcmNotaDirecta = dtcmNotaDirecta;
	}

	public BigDecimal getDtcmNotaTransformada() {
		return dtcmNotaTransformada;
	}

	public void setDtcmNotaTransformada(BigDecimal dtcmNotaTransformada) {
		this.dtcmNotaTransformada = dtcmNotaTransformada;
	}

	public Integer getDtcmAciertos() {
		return dtcmAciertos;
	}

	public void setDtcmAciertos(Integer dtcmAciertos) {
		this.dtcmAciertos = dtcmAciertos;
	}

	public Integer getDtcmErrores() {
		return dtcmErrores;
	}

	public void setDtcmErrores(Integer dtcmErrores) {
		this.dtcmErrores = dtcmErrores;
	}

	public Integer getDtcmBlancos() {
		return dtcmBlancos;
	}

	public void setDtcmBlancos(Integer dtcmBlancos) {
		this.dtcmBlancos = dtcmBlancos;
	}

	public Integer getDtcmDobles() {
		return dtcmDobles;
	}

	public void setDtcmDobles(Integer dtcmDobles) {
		this.dtcmDobles = dtcmDobles;
	}

	public String getDtcmPrueba() {
		return dtcmPrueba;
	}

	public void setDtcmPrueba(String dtcmPrueba) {
		this.dtcmPrueba = dtcmPrueba;
	}

	public Integer getDtcmTipoDetalle() {
		return dtcmTipoDetalle;
	}

	public void setDtcmTipoDetalle(Integer dtcmTipoDetalle) {
		this.dtcmTipoDetalle = dtcmTipoDetalle;
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

	public String getTrttObservacionDga() {
		return trttObservacionDga;
	}

	public void setTrttObservacionDga(String trttObservacionDga) {
		this.trttObservacionDga = trttObservacionDga;
	}

	public String getTrttObsDesactivarDga() {
		return trttObsDesactivarDga;
	}

	public void setTrttObsDesactivarDga(String trttObsDesactivarDga) {
		this.trttObsDesactivarDga = trttObsDesactivarDga;
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

	public Integer getFcesId() {
		return fcesId;
	}

	public void setFcesId(Integer fcesId) {
		this.fcesId = fcesId;
	}

	public Integer getMcttcrId() {
		return mcttcrId;
	}

	public void setMcttcrId(Integer mcttcrId) {
		this.mcttcrId = mcttcrId;
	}

	public Integer getFcesMecanismoTitulacionCarrera() {
		return fcesMecanismoTitulacionCarrera;
	}

	public void setFcesMecanismoTitulacionCarrera(
			Integer fcesMecanismoTitulacionCarrera) {
		this.fcesMecanismoTitulacionCarrera = fcesMecanismoTitulacionCarrera;
	}

	public Integer getMcttcrPorcentaje() {
		return mcttcrPorcentaje;
	}

	public void setMcttcrPorcentaje(Integer mcttcrPorcentaje) {
		this.mcttcrPorcentaje = mcttcrPorcentaje;
	}

	public String getCrrDescripcion() {
		return crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	public Integer getAsnoId() {
		return asnoId;
	}
	
	public void setAsnoId(Integer asnoId) {
		this.asnoId = asnoId;
	}

	public BigDecimal getAsnoComplexivoPractico() {
		return asnoComplexivoPractico;
	}

	public void setAsnoComplexivoPractico(BigDecimal asnoComplexivoPractico) {
		this.asnoComplexivoPractico = asnoComplexivoPractico;
	}

	public BigDecimal getAsnoComplexivoTeorico() {
		return asnoComplexivoTeorico;
	}

	public void setAsnoComplexivoTeorico(BigDecimal asnoComplexivoTeorico) {
		this.asnoComplexivoTeorico = asnoComplexivoTeorico;
	}

	public Timestamp getAsnoFechaCmpteorico() {
		return asnoFechaCmpteorico;
	}

	public void setAsnoFechaCmpteorico(Timestamp asnoFechaCmpteorico) {
		this.asnoFechaCmpteorico = asnoFechaCmpteorico;
	}

	public Date getAsnoFechaTeorico() {
		return asnoFechaTeorico;
	}

	public void setAsnoFechaTeorico(Date asnoFechaTeorico) {
		this.asnoFechaTeorico = asnoFechaTeorico;
	}

	
	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}
	
	public String getFclDescripcion() {
		return fclDescripcion;
	}

	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}
	
	public String getCadenaAux() {
		return cadenaAux;
	}

	public void setCadenaAux(String cadenaAux) {
		this.cadenaAux = cadenaAux;
	}

	public BigDecimal getAsnoComplexivoFinal() {
		return asnoComplexivoFinal;
	}

	public void setAsnoComplexivoFinal(BigDecimal asnoComplexivoFinal) {
		this.asnoComplexivoFinal = asnoComplexivoFinal;
	}

	public BigDecimal getFcesNotaTrabTitulacion() {
		return fcesNotaTrabTitulacion;
	}

	public void setFcesNotaTrabTitulacion(BigDecimal fcesNotaTrabTitulacion) {
		this.fcesNotaTrabTitulacion = fcesNotaTrabTitulacion;
	}

	public Timestamp getAsnoFechaCmpGraciaTeorico() {
		return asnoFechaCmpGraciaTeorico;
	}

	public void setAsnoFechaCmpGraciaTeorico(Timestamp asnoFechaCmpGraciaTeorico) {
		this.asnoFechaCmpGraciaTeorico = asnoFechaCmpGraciaTeorico;
	}

	public Timestamp getAsnoFechaCmpGraciaPractico() {
		return asnoFechaCmpGraciaPractico;
	}

	public void setAsnoFechaCmpGraciaPractico(Timestamp asnoFechaCmpGraciaPractico) {
		this.asnoFechaCmpGraciaPractico = asnoFechaCmpGraciaPractico;
	}

	public BigDecimal getAsnoCmpGraciaTeorico() {
		return asnoCmpGraciaTeorico;
	}

	public void setAsnoCmpGraciaTeorico(BigDecimal asnoCmpGraciaTeorico) {
		this.asnoCmpGraciaTeorico = asnoCmpGraciaTeorico;
	}

	public BigDecimal getAsnoCmpGraciaPractico() {
		return asnoCmpGraciaPractico;
	}

	public void setAsnoCmpGraciaPractico(BigDecimal asnoCmpGraciaPractico) {
		this.asnoCmpGraciaPractico = asnoCmpGraciaPractico;
	}

	public BigDecimal getAsnoCmpGraciaFinal() {
		return asnoCmpGraciaFinal;
	}

	public void setAsnoCmpGraciaFinal(BigDecimal asnoCmpGraciaFinal) {
		this.asnoCmpGraciaFinal = asnoCmpGraciaFinal;
	}

	public String getPrsMailPersonal() {
		return prsMailPersonal;
	}

	public void setPrsMailPersonal(String prsMailPersonal) {
		this.prsMailPersonal = prsMailPersonal;
	}

	public String getDtcmObservacion() {
		return dtcmObservacion;
	}
	
	public void setDtcmObservacion(String dtcmObservacion) {
		this.dtcmObservacion = dtcmObservacion;
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
	
	public BigDecimal getFcesNotaPromAcumulado() {
		return fcesNotaPromAcumulado;
	}

	public void setFcesNotaPromAcumulado(BigDecimal fcesNotaPromAcumulado) {
		this.fcesNotaPromAcumulado = fcesNotaPromAcumulado;
	}

	public Integer getMcttId() {
		return mcttId;
	}

	public void setMcttId(Integer mcttId) {
		this.mcttId = mcttId;
	}

	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("dtcmId : " + dtcmId);
		sb.append(tabulador + "dtcmNotaDirecta : " + (dtcmNotaDirecta==null? "NULL":dtcmNotaDirecta));
		sb.append(tabulador + "dtcmNotaTransformada : " + (dtcmNotaTransformada==null? "NULL":dtcmNotaTransformada));
		sb.append(tabulador + "dtcmAciertos : " + (dtcmAciertos==null? "NULL":dtcmAciertos));
		sb.append(tabulador + "dtcmErrores : " + (dtcmErrores==null? "NULL":dtcmErrores));
		sb.append(tabulador + "dtcmBlancos : " + (dtcmBlancos==null? "NULL":dtcmBlancos));
		sb.append(tabulador + "dtcmDobles : " + (dtcmDobles==null? "NULL":dtcmDobles));
		sb.append(tabulador + "dtcmPrueba : " + (dtcmPrueba==null? "NULL":dtcmPrueba));
		sb.append(tabulador + "dtcmTipoDetalle : " + (dtcmTipoDetalle==null? "NULL":dtcmTipoDetalle));
		sb.append(tabulador + "asntId : " + (asnoId==null? "NULL":asnoId));
		sb.append(tabulador + "prsIdentificacion : " + (prsIdentificacion==null? "NULL":prsIdentificacion));
		return sb.toString();
    }
}
