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
   
 ARCHIVO:     TramiteTituloDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos del TramiteTitulo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018				Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;

import java.io.Serializable;

/**
 * Clase (DTO) TramiteTituloDto.
 * DTO encargado de manejar los datos del TramiteTitulo.
 * @author dalbuja.
 * @version 1.0
 */
public class TramiteTituloDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
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
	
	// Constructores
	public TramiteTituloDto(){
		
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
	
}
