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
   
 ARCHIVO:     ProcesoTramite.java	  
 DESCRIPCION: Entity Bean que representa a la tabla ProcesoTramite de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		   		Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) ProcesoTramite.
 * Entity Bean que representa a la tabla ProcesoTramite de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "proceso_tramite", schema = "titulacion_posgrado")
public class ProcesoTramite implements Serializable {

	private static final long serialVersionUID = 4332700057890825008L;
	private int prtrId;
	private RolFlujoCarrera prtrRolFlujoCarrera;
	private TramiteTitulo prtrTramiteTitulo;
	private Integer prtrTipoProceso;
	private Timestamp prtrFechaEjecucion;
	private String prtrRegTtlSenecyt;
	
	public ProcesoTramite() {
	}

	public ProcesoTramite(int prtrId) {
		this.prtrId = prtrId;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "prtr_id", unique = true, nullable = false)
	public int getPrtrId() {
		return this.prtrId;
	}

	public void setPrtrId(int prtrId) {
		this.prtrId = prtrId;
	}

	@JoinColumn(name = "roflcr_id", referencedColumnName = "roflcr_id")
	@ManyToOne
	public RolFlujoCarrera getPrtrRolFlujoCarrera() {
		return prtrRolFlujoCarrera;
	}

	public void setPrtrRolFlujoCarrera(RolFlujoCarrera prtrRolFlujoCarrera) {
		this.prtrRolFlujoCarrera = prtrRolFlujoCarrera;
	}

	@JoinColumn(name = "trtt_id", referencedColumnName = "trtt_id")
	@ManyToOne
	public TramiteTitulo getPrtrTramiteTitulo() {
		return prtrTramiteTitulo;
	}

	public void setPrtrTramiteTitulo(TramiteTitulo prtrTramiteTitulo) {
		this.prtrTramiteTitulo = prtrTramiteTitulo;
	}

	@Column(name = "prtr_tipo_proceso")
	public Integer getPrtrTipoProceso() {
		return this.prtrTipoProceso;
	}

	public void setPrtrTipoProceso(Integer prtrTipoProceso) {
		this.prtrTipoProceso = prtrTipoProceso;
	}

	@Column(name = "prtr_fecha_ejecucion", length = 29)
	public Timestamp getPrtrFechaEjecucion() {
		return prtrFechaEjecucion;
	}

	public void setPrtrFechaEjecucion(Timestamp prtrFechaEjecucion) {
		this.prtrFechaEjecucion = prtrFechaEjecucion;
	}


	@Column(name = "prtr_reg_ttl_senecyt", length = 25)
	@Length(max = 25)
	public String getPrtrRegTtlSenecyt() {
		return prtrRegTtlSenecyt;
	}

	public void setPrtrRegTtlSenecyt(String prtrRegTtlSenecyt) {
		this.prtrRegTtlSenecyt = prtrRegTtlSenecyt;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("prtrId: " + prtrId);
    	sb.append(tabulador);
    	sb.append("prtrRolFlujoCarrera: " + (prtrRolFlujoCarrera==null? "NULL":prtrRolFlujoCarrera.getRoflcrId()));  
    	sb.append(tabulador);
    	sb.append("prtrTramiteTitulo: " + (prtrTramiteTitulo==null? "NULL":prtrTramiteTitulo.getTrttId()));  
    	sb.append(tabulador);
    	sb.append("prtrTipoProceso: " + (prtrTipoProceso==null? "NULL":prtrTipoProceso.intValue()));  
    	sb.append(tabulador);
    	sb.append("prtrFechaEjecucion: " + (prtrFechaEjecucion==null? "NULL":prtrFechaEjecucion));  
        return sb.toString();
	}


}
