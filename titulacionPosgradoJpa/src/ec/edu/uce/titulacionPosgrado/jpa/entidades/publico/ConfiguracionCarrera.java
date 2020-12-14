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
   
 ARCHIVO:     ConfiguracionCarrera.java	  
 DESCRIPCION: Entity Bean que representa a la tabla ConfiguracionCarrera de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
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
 * Clase (Entity Bean) ConfiguracionCarrera.
 * Entity Bean que representa a la tabla ConfiguracionCarrera de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "configuracion_carrera", schema = "titulacion_posgrado")
public class ConfiguracionCarrera implements Serializable {

	private static final long serialVersionUID = 131721379777516375L;
	private int cncrId;
	private Ubicacion cncrUbicacion;
	private TipoSede cncrTipoSede;
	private Titulo cncrTitulo;
	private TipoFormacion cncrTipoFormacion;
	private Carrera cncrCarrera;
	private Vigencia cncrVigencia;
	private Modalidad cncrModalidad;
	private Duracion cncrDuracion;
	private List<FichaEstudiante> cncrFichaEstudiante;

	public ConfiguracionCarrera() {
	}

	public ConfiguracionCarrera(Integer cncrId) {
		this.cncrId = cncrId;
	}


	public ConfiguracionCarrera(int cncrId, Ubicacion cncrUbicacion,
			TipoSede cncrTipoSede, Titulo cncrTitulo,
			TipoFormacion cncrTipoFormacion, Carrera cncrCarrera,
			Vigencia cncrVigencia, Modalidad cncrModalidad,
			List<FichaEstudiante> cncrFichaEstudiante) {
		this.cncrId = cncrId;
		this.cncrUbicacion = cncrUbicacion;
		this.cncrTipoSede = cncrTipoSede;
		this.cncrTitulo = cncrTitulo;
		this.cncrTipoFormacion = cncrTipoFormacion;
		this.cncrCarrera = cncrCarrera;
		this.cncrVigencia = cncrVigencia;
		this.cncrModalidad = cncrModalidad;
		this.cncrFichaEstudiante = cncrFichaEstudiante;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "cncr_id", unique = true, nullable = false)
	public int getCncrId() {
		return this.cncrId;
	}

	public void setCncrId(int cncrId) {
		this.cncrId = cncrId;
	}


	@JoinColumn(name = "ubc_id", referencedColumnName = "ubc_id")
	@ManyToOne
	public Ubicacion getCncrUbicacion() {
		return cncrUbicacion;
	}

	public void setCncrUbicacion(Ubicacion cncrUbicacion) {
		this.cncrUbicacion = cncrUbicacion;
	}

	@JoinColumn(name = "tise_id", referencedColumnName = "tise_id")
	@ManyToOne
	public TipoSede getCncrTipoSede() {
		return cncrTipoSede;
	}

	public void setCncrTipoSede(TipoSede cncrTipoSede) {
		this.cncrTipoSede = cncrTipoSede;
	}

	@JoinColumn(name = "ttl_id", referencedColumnName = "ttl_id")
	@ManyToOne
	public Titulo getCncrTitulo() {
		return cncrTitulo;
	}

	public void setCncrTitulo(Titulo cncrTitulo) {
		this.cncrTitulo = cncrTitulo;
	}

	@JoinColumn(name = "tifr_id", referencedColumnName = "tifr_id")
	@ManyToOne
	public TipoFormacion getCncrTipoFormacion() {
		return cncrTipoFormacion;
	}

	public void setCncrTipoFormacion(TipoFormacion cncrTipoFormacion) {
		this.cncrTipoFormacion = cncrTipoFormacion;
	}

	@JoinColumn(name = "crr_id", referencedColumnName = "crr_id")
	@ManyToOne
	public Carrera getCncrCarrera() {
		return cncrCarrera;
	}

	public void setCncrCarrera(Carrera cncrCarrera) {
		this.cncrCarrera = cncrCarrera;
	}

	@JoinColumn(name = "vgn_id", referencedColumnName = "vgn_id")
	@ManyToOne
	public Vigencia getCncrVigencia() {
		return cncrVigencia;
	}

	public void setCncrVigencia(Vigencia cncrVigencia) {
		this.cncrVigencia = cncrVigencia;
	}

	@JoinColumn(name = "mdl_id", referencedColumnName = "mdl_id")
	@ManyToOne
	public Modalidad getCncrModalidad() {
		return cncrModalidad;
	}

	public void setCncrModalidad(Modalidad cncrModalidad) {
		this.cncrModalidad = cncrModalidad;
	}

	
	@OneToMany(mappedBy = "fcesConfiguracionCarrera")
	public List<FichaEstudiante> getCncrFichaEstudiante() {
		return cncrFichaEstudiante;
	}

	public void setCncrFichaEstudiante(List<FichaEstudiante> cncrFichaEstudiante) {
		this.cncrFichaEstudiante = cncrFichaEstudiante;
	}

	@JoinColumn(name = "drc_id", referencedColumnName = "drc_id")
	@ManyToOne
	public Duracion getCncrDuracion() {
		return cncrDuracion;
	}

	public void setCncrDuracion(Duracion cncrDuracion) {
		this.cncrDuracion = cncrDuracion;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("cncrId: " + cncrId);
    	sb.append(tabulador);
    	sb.append("cncrUbicacion: " + (cncrUbicacion==null? "NULL":cncrUbicacion.getUbcId()));  
    	sb.append(tabulador);
    	sb.append("cncrTipoSede: " + (cncrTipoSede==null? "NULL":cncrTipoSede.getTiseId()));  
      	sb.append(tabulador);
    	sb.append("cncrTitulo: " + (cncrTitulo==null? "NULL":cncrTitulo.getTtlId()));
      	sb.append(tabulador);
    	sb.append("cncrTipoFormacion: " + (cncrTipoFormacion==null? "NULL":cncrTipoFormacion.getTifrId()));
      	sb.append(tabulador);
    	sb.append("cncrCarrera: " + (cncrCarrera==null? "NULL":cncrCarrera.getCrrId()));
      	sb.append(tabulador);
    	sb.append("cncrVigencia: " + (cncrVigencia==null? "NULL":cncrVigencia.getVgnId()));
    	sb.append(tabulador);
    	sb.append("cncrModalidad: " + (cncrModalidad==null? "NULL":cncrModalidad.getMdlId()));
        return sb.toString();
	}
}
