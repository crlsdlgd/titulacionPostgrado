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
   
 ARCHIVO:     ConfiguracionCarreraDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de una ConfiguracionCarrera. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-MAYO-2016 			David Arellano		          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) ConfiguracionCarreraDto.
 * DTO encargado de manejar los datos para el registro de una ConfiguracionCarrera.
 * @author darellano.
 * @version 1.0
 */
public class ConfiguracionCarreraDto implements Serializable {
	
	private static final long serialVersionUID = -7819704526393256290L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	/**********CONFIGURACION CARRERA *****************/
	private int cncrId;
	private Integer cncrUbicacion;
	private Integer cncrTipoSede;
	private Integer cncrTitulo;
	private Integer cncrTipoFormacion;
	private Integer cncrCarrera;
	private Integer cncrVigencia;
	private Integer cncrModalidad;
	private Integer cncrDuracion;
	private String cncrDetalleActaGrado;
	/**********UBICACION *****************/
	private int ubcId;
	private String ubcDescripcion;
	private Integer ubcJerarquia;
	private String ubcGentilicio;
	private String ubcCodSniese;
	private Integer ubcPadre;
	/**********TIPO SEDE *****************/
	private int tiseId;
	private String tiseDescripcion;
	private Integer tiseRegimenAcademico;
	/**********TITULO *****************/
	private int ttlId;
	private String ttlDescripcion;
	private Integer ttlSexo;
	private Integer ttlEstado;
	private Integer ttlTipo;
	/**********TIPO FORMACION *****************/
	private int tifrId;
	private String tifrDescripcion;
	private Integer tifrNivelFormacion;
	/**********CARRERA *****************/
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private Integer crrTipoDuracion;
	private Integer crrTipoDuracionSniese;
	private Integer crrDuracion;
	private Integer crrDuracionSniese;
	private Integer crrNivel;
	private String crrDetalle;
	private Integer crrFacultad;
	/**********VIGENCIA *****************/
	private int vgnId;
	private String vgnDescripcion;
	/**********MODALIDAD *****************/
	private int mdlId;
	private String mdlDescripcion;
	/**********DURACION *****************/
	private int drcId;
	private Integer drcTipo;
	private Integer drcTiempo;
	/**********NIVEL FORMACION *****************/
	private int nvfrId;
	private String nvfrDescripcion;
	private Integer nvfrRegimenAcademico;
	/**********REGIMEN ACADEMICO *****************/
	private int rgacId;
	private String rgacDescripcion;
	private Integer rgacEstado;
	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/
	public int getCncrId() {
		return cncrId;
	}
	public void setCncrId(int cncrId) {
		this.cncrId = cncrId;
	}
	public Integer getCncrUbicacion() {
		return cncrUbicacion;
	}
	public void setCncrUbicacion(Integer cncrUbicacion) {
		this.cncrUbicacion = cncrUbicacion;
	}
	public Integer getCncrTipoSede() {
		return cncrTipoSede;
	}
	public void setCncrTipoSede(Integer cncrTipoSede) {
		this.cncrTipoSede = cncrTipoSede;
	}
	public Integer getCncrTitulo() {
		return cncrTitulo;
	}
	public void setCncrTitulo(Integer cncrTitulo) {
		this.cncrTitulo = cncrTitulo;
	}
	public Integer getCncrTipoFormacion() {
		return cncrTipoFormacion;
	}
	public void setCncrTipoFormacion(Integer cncrTipoFormacion) {
		this.cncrTipoFormacion = cncrTipoFormacion;
	}
	public Integer getCncrCarrera() {
		return cncrCarrera;
	}
	public void setCncrCarrera(Integer cncrCarrera) {
		this.cncrCarrera = cncrCarrera;
	}
	public Integer getCncrVigencia() {
		return cncrVigencia;
	}
	public void setCncrVigencia(Integer cncrVigencia) {
		this.cncrVigencia = cncrVigencia;
	}
	public Integer getCncrModalidad() {
		return cncrModalidad;
	}
	public void setCncrModalidad(Integer cncrModalidad) {
		this.cncrModalidad = cncrModalidad;
	}
	public Integer getCncrDuracion() {
		return cncrDuracion;
	}
	public void setCncrDuracion(Integer cncrDuracion) {
		this.cncrDuracion = cncrDuracion;
	}
	public int getUbcId() {
		return ubcId;
	}
	public void setUbcId(int ubcId) {
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
	public int getTiseId() {
		return tiseId;
	}
	public void setTiseId(int tiseId) {
		this.tiseId = tiseId;
	}
	public String getTiseDescripcion() {
		return tiseDescripcion;
	}
	public void setTiseDescripcion(String tiseDescripcion) {
		this.tiseDescripcion = tiseDescripcion;
	}
	public Integer getTiseRegimenAcademico() {
		return tiseRegimenAcademico;
	}
	public void setTiseRegimenAcademico(Integer tiseRegimenAcademico) {
		this.tiseRegimenAcademico = tiseRegimenAcademico;
	}
	public int getTtlId() {
		return ttlId;
	}
	public void setTtlId(int ttlId) {
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
	public int getTifrId() {
		return tifrId;
	}
	public void setTifrId(int tifrId) {
		this.tifrId = tifrId;
	}
	public String getTifrDescripcion() {
		return tifrDescripcion;
	}
	public void setTifrDescripcion(String tifrDescripcion) {
		this.tifrDescripcion = tifrDescripcion;
	}
	public Integer getTifrNivelFormacion() {
		return tifrNivelFormacion;
	}
	public void setTifrNivelFormacion(Integer tifrNivelFormacion) {
		this.tifrNivelFormacion = tifrNivelFormacion;
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
	public Integer getCrrTipoDuracion() {
		return crrTipoDuracion;
	}
	public void setCrrTipoDuracion(Integer crrTipoDuracion) {
		this.crrTipoDuracion = crrTipoDuracion;
	}
	public Integer getCrrTipoDuracionSniese() {
		return crrTipoDuracionSniese;
	}
	public void setCrrTipoDuracionSniese(Integer crrTipoDuracionSniese) {
		this.crrTipoDuracionSniese = crrTipoDuracionSniese;
	}
	public Integer getCrrDuracion() {
		return crrDuracion;
	}
	public void setCrrDuracion(Integer crrDuracion) {
		this.crrDuracion = crrDuracion;
	}
	public Integer getCrrDuracionSniese() {
		return crrDuracionSniese;
	}
	public void setCrrDuracionSniese(Integer crrDuracionSniese) {
		this.crrDuracionSniese = crrDuracionSniese;
	}
	public Integer getCrrNivel() {
		return crrNivel;
	}
	public void setCrrNivel(Integer crrNivel) {
		this.crrNivel = crrNivel;
	}
	public String getCrrDetalle() {
		return crrDetalle;
	}
	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}
	public Integer getCrrFacultad() {
		return crrFacultad;
	}
	public void setCrrFacultad(Integer crrFacultad) {
		this.crrFacultad = crrFacultad;
	}
	public int getVgnId() {
		return vgnId;
	}
	public void setVgnId(int vgnId) {
		this.vgnId = vgnId;
	}
	public String getVgnDescripcion() {
		return vgnDescripcion;
	}
	public void setVgnDescripcion(String vgnDescripcion) {
		this.vgnDescripcion = vgnDescripcion;
	}
	public int getMdlId() {
		return mdlId;
	}
	public void setMdlId(int mdlId) {
		this.mdlId = mdlId;
	}
	public String getMdlDescripcion() {
		return mdlDescripcion;
	}
	public void setMdlDescripcion(String mdlDescripcion) {
		this.mdlDescripcion = mdlDescripcion;
	}
	public int getDrcId() {
		return drcId;
	}
	public void setDrcId(int drcId) {
		this.drcId = drcId;
	}
	public Integer getDrcTipo() {
		return drcTipo;
	}
	public void setDrcTipo(Integer drcTipo) {
		this.drcTipo = drcTipo;
	}
	public Integer getDrcTiempo() {
		return drcTiempo;
	}
	public void setDrcTiempo(Integer drcTiempo) {
		this.drcTiempo = drcTiempo;
	}
	public int getNvfrId() {
		return nvfrId;
	}
	public void setNvfrId(int nvfrId) {
		this.nvfrId = nvfrId;
	}
	public String getNvfrDescripcion() {
		return nvfrDescripcion;
	}
	public void setNvfrDescripcion(String nvfrDescripcion) {
		this.nvfrDescripcion = nvfrDescripcion;
	}
	public Integer getNvfrRegimenAcademico() {
		return nvfrRegimenAcademico;
	}
	public void setNvfrRegimenAcademico(Integer nvfrRegimenAcademico) {
		this.nvfrRegimenAcademico = nvfrRegimenAcademico;
	}
	public int getRgacId() {
		return rgacId;
	}
	public void setRgacId(int rgacId) {
		this.rgacId = rgacId;
	}
	public String getRgacDescripcion() {
		return rgacDescripcion;
	}
	public void setRgacDescripcion(String rgacDescripcion) {
		this.rgacDescripcion = rgacDescripcion;
	}
	public Integer getRgacEstado() {
		return rgacEstado;
	}
	public void setRgacEstado(Integer rgacEstado) {
		this.rgacEstado = rgacEstado;
	}
	
	public String getCncrDetalleActaGrado() {
		return cncrDetalleActaGrado;
	}
	public void setCncrDetalleActaGrado(String cncrDetalleActaGrado) {
		this.cncrDetalleActaGrado = cncrDetalleActaGrado;
	}
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	 /*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
	public String toString() {
	    	String tabulador = "\t";
	StringBuilder sb = new StringBuilder();
	sb.append("cncrId : " + cncrId);
	sb.append(tabulador + "crrId : " + crrId);
	sb.append(tabulador + "crrDescripcion : " + crrDescripcion);
	sb.append(tabulador + "vgnId : " + vgnId);
	sb.append(tabulador + "vgnDescripcion : " + vgnDescripcion);
	sb.append(tabulador + "mdlId : " + mdlId);
	sb.append(tabulador + "mdlDescripcion : " + mdlDescripcion);
	sb.append(tabulador + "ttlId : " + ttlId);
	sb.append(tabulador + "ttlDescripcion : " + ttlDescripcion);
	sb.append(tabulador + "ttlSexo : " + ttlSexo);
	sb.append(tabulador + "ubcId : " + ubcId);
	sb.append(tabulador + "ubcDescripcion : " + ubcDescripcion);
	sb.append(tabulador + "tiseId : " + tiseId);
	sb.append(tabulador + "tiseDescripcion : " + tiseDescripcion);
	sb.append(tabulador + "tifrId : " + tifrId);
	sb.append(tabulador + "tifrDescripcion : " + tifrDescripcion);
	sb.append(tabulador + "rgacId : " + rgacId);
	sb.append(tabulador + "rgacDescripcion : " + rgacDescripcion);
	sb.append(tabulador + "nvfrId : " + nvfrId);
	sb.append(tabulador + "nvfrDescripcion : " + nvfrDescripcion);
	return sb.toString();
	}

	
	
}
