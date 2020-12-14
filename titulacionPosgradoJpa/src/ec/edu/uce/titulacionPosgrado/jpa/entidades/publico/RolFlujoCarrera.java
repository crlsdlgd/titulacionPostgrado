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
   
 ARCHIVO:     RolFlujoCarrera.java	  
 DESCRIPCION: Entity Bean que representa a la tabla RolFlujoCarrera de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Clase (Entity Bean) RolFlujoCarrera.
 * Entity Bean que representa a la tabla RolFlujoCarrera de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "rol_flujo_carrera", schema = "titulacion_posgrado")
public class RolFlujoCarrera implements  Serializable {

	private static final long serialVersionUID = 7521217436249380150L;
	private int roflcrId;
	private Integer roflcrEstado;
	private Carrera roflcrCarrera;
	private UsuarioRol roflcrUsuarioRol;
	private List<ProcesoTramite> roflcrProcesoTramites;

	public RolFlujoCarrera() {
	}

	public RolFlujoCarrera(int roflcrId) {
		this.roflcrId = roflcrId;
	}

	public RolFlujoCarrera(int roflcrId, Integer roflcrEstado,
			Carrera roflcrCarrera, UsuarioRol roflcrUsuarioRol,
			List<ProcesoTramite> roflcrProcesoTramites) {
		this.roflcrId = roflcrId;
		this.roflcrEstado = roflcrEstado;
		this.roflcrCarrera = roflcrCarrera;
		this.roflcrUsuarioRol = roflcrUsuarioRol;
		this.roflcrProcesoTramites = roflcrProcesoTramites;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "roflcr_id", unique = true, nullable = false)
	public int getRoflcrId() {
		return this.roflcrId;
	}

	public void setRoflcrId(int roflcrId) {
		this.roflcrId = roflcrId;
	}

	@JoinColumn(name = "crr_id", referencedColumnName = "crr_id")
	@ManyToOne	
	public Carrera getRoflcrCarrera() {
		return roflcrCarrera;
	}

	public void setRoflcrCarrera(Carrera roflcrCarrera) {
		this.roflcrCarrera = roflcrCarrera;
	}

	@JoinColumn(name = "usro_id", referencedColumnName = "usro_id")
	@ManyToOne
	public UsuarioRol getRoflcrUsuarioRol() {
		return roflcrUsuarioRol;
	}

	public void setRoflcrUsuarioRol(UsuarioRol roflcrUsuarioRol) {
		this.roflcrUsuarioRol = roflcrUsuarioRol;
	}

	@Column(name = "roflcr_estado")
	public Integer getRoflcrEstado() {
		return roflcrEstado;
	}

	public void setRoflcrEstado(Integer roflcrEstado) {
		this.roflcrEstado = roflcrEstado;
	}
	
	@OneToMany(mappedBy = "prtrRolFlujoCarrera")
	public List<ProcesoTramite> getRoflcrProcesoTramites() {
		return roflcrProcesoTramites;
	}

	public void setRoflcrProcesoTramites(List<ProcesoTramite> roflcrProcesoTramites) {
		this.roflcrProcesoTramites = roflcrProcesoTramites;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("roflcrId: " + roflcrId);
    	sb.append(tabulador);
    	sb.append("roflcrEstado: " + (roflcrEstado==null? "NULL":roflcrEstado.intValue()));  
    	sb.append(tabulador);
    	sb.append("roflcrCarrera: " + (roflcrCarrera==null? "NULL":roflcrCarrera.getCrrId()));
    	sb.append(tabulador);
    	sb.append("roflcrUsuarioRol: " + (roflcrUsuarioRol==null? "NULL":roflcrUsuarioRol.getUsroId()));
    	
        return sb.toString();
	}
	
}
