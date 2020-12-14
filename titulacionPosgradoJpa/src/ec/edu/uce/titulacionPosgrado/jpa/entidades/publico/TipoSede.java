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
   
 ARCHIVO:     TipoSede.java	  
 DESCRIPCION: Entity Bean que representa a la tabla TipoSede de la BD. 
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

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) TipoSede.
 * Entity Bean que representa a la tabla TipoSede de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "tipo_sede", schema = "titulacion_posgrado")
public class TipoSede implements  Serializable {

	private static final long serialVersionUID = 8731588433922716916L;
	private int tiseId;
	private String tiseDescripcion;
	private RegimenAcademico tiseRegimenAcademico;
	private List<ConfiguracionCarrera> tiseConfiguracionCarreras;

	public TipoSede() {
	}

	public TipoSede(int tiseId) {
		this.tiseId = tiseId;
	}

	public TipoSede(int tiseId, String tiseDescripcion,
			RegimenAcademico tiseRegimenAcademico,
			List<ConfiguracionCarrera> tiseConfiguracionCarreras) {
		this.tiseId = tiseId;
		this.tiseDescripcion = tiseDescripcion;
		this.tiseRegimenAcademico = tiseRegimenAcademico;
		this.tiseConfiguracionCarreras = tiseConfiguracionCarreras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "tise_id", unique = true, nullable = false)
	public int getTiseId() {
		return this.tiseId;
	}

	public void setTiseId(int tiseId) {
		this.tiseId = tiseId;
	}

	
	
	@JoinColumn(name = "rgac_id", referencedColumnName = "rgac_id")
	@ManyToOne
	public RegimenAcademico getTiseRegimenAcademico() {
		return tiseRegimenAcademico;
	}

	public void setTiseRegimenAcademico(RegimenAcademico tiseRegimenAcademico) {
		this.tiseRegimenAcademico = tiseRegimenAcademico;
	}


	@Column(name = "tise_descripcion", length = 128)
	@Length(max = 128)
	public String getTiseDescripcion() {
		return this.tiseDescripcion;
	}

	public void setTiseDescripcion(String tiseDescripcion) {
		this.tiseDescripcion = tiseDescripcion;
	}

	@OneToMany(mappedBy = "cncrTipoSede")
	public List<ConfiguracionCarrera> getTiseConfiguracionCarreras() {
		return tiseConfiguracionCarreras;
	}

	public void setTiseConfiguracionCarreras(
			List<ConfiguracionCarrera> tiseConfiguracionCarreras) {
		this.tiseConfiguracionCarreras = tiseConfiguracionCarreras;
	}

	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("tiseId: " + tiseId);
    	sb.append(tabulador);
    	sb.append("tiseDescripcion: " + (tiseDescripcion==null? "NULL":tiseDescripcion));  
    	sb.append(tabulador);
    	sb.append("tiseRegimenAcademico: " + (tiseRegimenAcademico==null? "NULL":tiseRegimenAcademico.getRgacId()));  
        return sb.toString();
	}


}
