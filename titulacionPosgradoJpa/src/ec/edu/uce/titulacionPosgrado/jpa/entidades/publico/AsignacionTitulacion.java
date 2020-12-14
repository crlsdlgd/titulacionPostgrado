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
   
 ARCHIVO:     AsignacionTitulacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla AsignacionTitulacion de lOa BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018				Daniel Albuja  			          Emisión Inicial
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

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) AsignacionTitulacion.
 * Entity Bean que representa a la tabla AsignacionTitulacion de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "asignacion_titulacion", schema = "titulacion_posgrado")
public class AsignacionTitulacion implements Serializable {

	private static final long serialVersionUID = 4332700057890825008L;
	
	private int asttId;
	private String asttTemaTrabajo;
	private String asttTutorMetodologico;
	private String asttDirectorCientifico;
	private Integer asttAprobacionTutor;
	private ProcesoTramite asttProcesoTramite;
	
	private List<FichaDcnAsgTitulacion> asttListFichasDcnAsgTitulacion;
	
	public AsignacionTitulacion() {
	}

	public AsignacionTitulacion(int asttId) {
		this.asttId = asttId;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "astt_id", unique = true, nullable = false)
	public int getAsttId() {
		return asttId;
	}

	public void setAsttId(int asttId) {
		this.asttId = asttId;
	}

	@Column(name = "astt_tema_trabajo", length = 512)
	@Length(max = 512)
	public String getAsttTemaTrabajo() {
		return asttTemaTrabajo;
	}

	public void setAsttTemaTrabajo(String asttTemaTrabajo) {
		this.asttTemaTrabajo = asttTemaTrabajo;
	}

	@Column(name = "astt_tutor_metodologico", length = 128)
	@Length(max = 128)
	public String getAsttTutorMetodologico() {
		return asttTutorMetodologico;
	}

	public void setAsttTutorMetodologico(String asttTutorMetodologico) {
		this.asttTutorMetodologico = asttTutorMetodologico;
	}

	@Column(name = "astt_director_cientifico", length = 128)
	@Length(max = 128)
	public String getAsttDirectorCientifico() {
		return asttDirectorCientifico;
	}

	public void setAsttDirectorCientifico(String asttDirectorCientifico) {
		this.asttDirectorCientifico = asttDirectorCientifico;
	}

	@JoinColumn(name = "prtr_id", referencedColumnName = "prtr_id")
	@ManyToOne
	public ProcesoTramite getAsttProcesoTramite() {
		return asttProcesoTramite;
	}

	public void setAsttProcesoTramite(ProcesoTramite asttProcesoTramite) {
		this.asttProcesoTramite = asttProcesoTramite;
	}  
	
	
	@OneToMany(mappedBy = "fcdcasttAsignacionTitulacion")
	public List<FichaDcnAsgTitulacion> getAsttListFichasDcnAsgTitulacion() {
		return asttListFichasDcnAsgTitulacion;
	}

	public void setAsttListFichasDcnAsgTitulacion(
			List<FichaDcnAsgTitulacion> asttListFichasDcnAsgTitulacion) {
		this.asttListFichasDcnAsgTitulacion = asttListFichasDcnAsgTitulacion;
	}

	@Column(name = "astt_aprobacion_tutor")
	public Integer getAsttAprobacionTutor() {
		return asttAprobacionTutor;
	}

	public void setAsttAprobacionTutor(Integer asttAprobacionTutor) {
		this.asttAprobacionTutor = asttAprobacionTutor;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append(tabulador);
    	sb.append("asttId: " + asttId); 
    	sb.append(tabulador);
    	sb.append("asttTemaTrabajo: " + (asttTemaTrabajo==null? "NULL":asttTemaTrabajo)); 
    	sb.append(tabulador);
    	sb.append("asttProcesoTramite: " + (asttProcesoTramite==null? "NULL":asttProcesoTramite.getPrtrId()));
        return sb.toString();
	}


}
