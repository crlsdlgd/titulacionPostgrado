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
   
 ARCHIVO:     NivelFormacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla NivelFormacion de la BD. 
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
 * Clase (Entity Bean) NivelFormacion.
 * Entity Bean que representa a la tabla NivelFormacion de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "nivel_formacion", schema = "titulacion_posgrado")
public class NivelFormacion implements Serializable {

	private static final long serialVersionUID = 2982343988547637301L;
	private int nvfrId;
	private String nvfrDescripcion;
	private RegimenAcademico nvfrRegimenAcademico;
	private List<TipoFormacion> nvfrTipoFormacions;

	public NivelFormacion() {
	}

	public NivelFormacion(Integer nvfrId) {
		this.nvfrId = nvfrId;
	}

	public NivelFormacion(int nvfrId, String nvfrDescripcion,
			RegimenAcademico nvfrRegimenAcademico,
			List<TipoFormacion> nvfrTipoFormacions) {
		this.nvfrId = nvfrId;
		this.nvfrDescripcion = nvfrDescripcion;
		this.nvfrRegimenAcademico = nvfrRegimenAcademico;
		this.nvfrTipoFormacions = nvfrTipoFormacions;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "nvfr_id", unique = true, nullable = false)
	public int getNvfrId() {
		return this.nvfrId;
	}

	public void setNvfrId(int nvfrId) {
		this.nvfrId = nvfrId;
	}

	@JoinColumn(name = "rgac_id", referencedColumnName = "rgac_id")
	@ManyToOne
	public RegimenAcademico getNvfrRegimenAcademico() {
		return nvfrRegimenAcademico;
	}

	public void setNvfrRegimenAcademico(RegimenAcademico nvfrRegimenAcademico) {
		this.nvfrRegimenAcademico = nvfrRegimenAcademico;
	}


	@Column(name = "nvfr_descripcion", length = 128)
	@Length(max = 128)
	public String getNvfrDescripcion() {
		return this.nvfrDescripcion;
	}

	public void setNvfrDescripcion(String nvfrDescripcion) {
		this.nvfrDescripcion = nvfrDescripcion;
	}

	@OneToMany(mappedBy = "tifrNivelFormacion")
	public List<TipoFormacion> getNvfrTipoFormacions() {
		return nvfrTipoFormacions;
	}

	public void setNvfrTipoFormacions(List<TipoFormacion> nvfrTipoFormacions) {
		this.nvfrTipoFormacions = nvfrTipoFormacions;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("nvfrId: " + nvfrId);
    	sb.append(tabulador);
    	sb.append("nvfrDescripcion: " + (nvfrDescripcion==null? "NULL":nvfrDescripcion));  
      	sb.append(tabulador);
    	sb.append("nvfrRegimenAcademico: " + (nvfrRegimenAcademico==null? "NULL":nvfrRegimenAcademico.getRgacId()));
        return sb.toString();
	}
}
