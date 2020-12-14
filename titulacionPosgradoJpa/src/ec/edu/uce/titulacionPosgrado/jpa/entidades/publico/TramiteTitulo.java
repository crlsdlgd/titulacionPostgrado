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
   
 ARCHIVO:     TramiteTitulo.java	  
 DESCRIPCION: Entity Bean que representa a la tabla TramiteTitulo de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		   		Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) TramiteTitulo.
 * Entity Bean que representa a la tabla TramiteTitulo de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "tramite_titulo", schema = "titulacion_posgrado")
public class TramiteTitulo implements  Serializable {

	private static final long serialVersionUID = -7676495443439322116L;
	private int trttId;
	private String trttNumTramite;
	private Integer trttEstadoTramite;
	private Integer trttEstadoProceso;
	
	
	private TramiteTitulo trttTramiteTitulo;
	private Convocatoria trttConvocatoria;
	private Integer trttCarreraId;
	private String trttObservacion;
	private List<ProcesoTramite> trttProcesosTramites;
	private List<TramiteTitulo> trttTramitesTitulos;
	private List<FichaEstudiante> trttFichaEstudiante;
	
	public TramiteTitulo() {
	}

	public TramiteTitulo(int trttId) {
		this.trttId = trttId;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "trtt_id", unique = true, nullable = false)
	public int getTrttId() {
		return trttId;
	}

	public void setTrttId(int trttId) {
		this.trttId = trttId;
	}


	@JoinColumn(name = "trtt_sub_id", referencedColumnName = "trtt_id")
	@ManyToOne
	public TramiteTitulo getTrttTramiteTitulo() {
		return trttTramiteTitulo;
	}

	public void setTrttTramiteTitulo(TramiteTitulo trttTramiteTitulo) {
		this.trttTramiteTitulo = trttTramiteTitulo;
	}
	
	@JoinColumn(name = "cnv_id", referencedColumnName = "cnv_id")
	@ManyToOne
	public Convocatoria getTrttConvocatoria() {
		return trttConvocatoria;
	}

	public void setTrttConvocatoria(Convocatoria trttConvocatoria) {
		this.trttConvocatoria = trttConvocatoria;
	}
	
	

	@Column(name = "trtt_num_tramite", length = 32)
	@Length(max = 32)
	public String getTrttNumTramite() {
		return trttNumTramite;
	}


	public void setTrttNumTramite(String trttNumTramite) {
		this.trttNumTramite = trttNumTramite;
	}

	@Column(name = "trtt_observacion", length = 512)
	@Length(max = 512)
	public String getTrttObservacion() {
		return trttObservacion;
	}

	public void setTrttObservacion(String trttObservacion) {
		this.trttObservacion = trttObservacion;
	}

	@Column(name = "trtt_estado_tramite")
	public Integer getTrttEstadoTramite() {
		return trttEstadoTramite;
	}

	public void setTrttEstadoTramite(Integer trttEstadoTramite) {
		this.trttEstadoTramite = trttEstadoTramite;
	}

	@Column(name = "trtt_estado_proceso")
	public Integer getTrttEstadoProceso() {
		return trttEstadoProceso;
	}

	public void setTrttEstadoProceso(Integer trttEstadoProceso) {
		this.trttEstadoProceso = trttEstadoProceso;
	}

	@OneToMany(mappedBy = "prtrTramiteTitulo")
	public List<ProcesoTramite> getTrttProcesosTramites() {
		return trttProcesosTramites;
	}

	public void setTrttProcesosTramites(List<ProcesoTramite> trttProcesosTramites) {
		this.trttProcesosTramites = trttProcesosTramites;
	}

	@OneToMany(mappedBy = "trttTramiteTitulo")
	public List<TramiteTitulo> getTrttTramitesTitulos() {
		return trttTramitesTitulos;
	}

	public void setTrttTramitesTitulos(List<TramiteTitulo> trttTramitesTitulos) {
		this.trttTramitesTitulos = trttTramitesTitulos;
	}


	@OneToMany(mappedBy = "fcesTramiteTitulo")
	public List<FichaEstudiante> getTrttFichaEstudiante() {
		return trttFichaEstudiante;
	}

	public void setTrttFichaEstudiante(List<FichaEstudiante> trttFichaEstudiante) {
		this.trttFichaEstudiante = trttFichaEstudiante;
	}

	@Column(name = "trtt_carrera_id")
	public Integer getTrttCarreraId() {
		return trttCarreraId;
	}

	public void setTrttCarreraId(Integer trttCarreraId) {
		this.trttCarreraId = trttCarreraId;
	}
	

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("trttId: " + trttId);
    	sb.append(tabulador);
    	sb.append("trttTramiteTitulo: " + (trttTramiteTitulo==null? "NULL":trttTramiteTitulo.getTrttId()));  
    	sb.append(tabulador);
    	sb.append("trttNumTramite: " + (trttNumTramite==null? "NULL":trttNumTramite));  
    	sb.append(tabulador);
    	sb.append("trttEstadoTramite: " + (trttEstadoTramite==null? "NULL":trttEstadoTramite.intValue()));  
    	sb.append(tabulador);
    	sb.append("trttCarreraId: " + (trttCarreraId==null? "NULL":trttCarreraId)); 
    	sb.append(tabulador);
    	sb.append("trttObservacion: " + (trttObservacion==null? "NULL":trttObservacion));
        return sb.toString();
	}





	
	

}
