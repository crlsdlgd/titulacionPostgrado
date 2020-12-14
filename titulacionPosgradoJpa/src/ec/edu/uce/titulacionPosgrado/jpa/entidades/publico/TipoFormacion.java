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
   
 ARCHIVO:     TipoFormacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla TipoFormacion de la BD. 
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

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) TipoFormacion.
 * Entity Bean que representa a la tabla TipoFormacion de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "tipo_formacion", schema = "titulacion_posgrado")
public class TipoFormacion implements  Serializable {

	private static final long serialVersionUID = -7962543761518888168L;
	private int tifrId;
	private String tifrDescripcion;
	private NivelFormacion tifrNivelFormacion;
	private List<ConfiguracionCarrera> tifrConfiguracionCarreras;

	public TipoFormacion() {
	}

	public TipoFormacion(int tifrId) {
		this.tifrId = tifrId;
	}


	public TipoFormacion(int tifrId, String tifrDescripcion,
			NivelFormacion tifrNivelFormacion,
			List<ConfiguracionCarrera> tifrConfiguracionCarreras) {
		this.tifrId = tifrId;
		this.tifrDescripcion = tifrDescripcion;
		this.tifrNivelFormacion = tifrNivelFormacion;
		this.tifrConfiguracionCarreras = tifrConfiguracionCarreras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "tifr_id", unique = true, nullable = false)
	public int getTifrId() {
		return this.tifrId;
	}

	public void setTifrId(int tifrId) {
		this.tifrId = tifrId;
	}

	
	@JoinColumn(name = "nvfr_id", referencedColumnName = "nvfr_id")
	@ManyToOne
	public NivelFormacion getTifrNivelFormacion() {
		return tifrNivelFormacion;
	}

	public void setTifrNivelFormacion(NivelFormacion tifrNivelFormacion) {
		this.tifrNivelFormacion = tifrNivelFormacion;
	}


	@Column(name = "tifr_descripcion", length = 128)
	@Length(max = 128)
	public String getTifrDescripcion() {
		return this.tifrDescripcion;
	}

	public void setTifrDescripcion(String tifrDescripcion) {
		this.tifrDescripcion = tifrDescripcion;
	}

	@OneToMany(mappedBy = "cncrTipoFormacion")
	public List<ConfiguracionCarrera> getTifrConfiguracionCarreras() {
		return tifrConfiguracionCarreras;
	}

	public void setTifrConfiguracionCarreras(
			List<ConfiguracionCarrera> tifrConfiguracionCarreras) {
		this.tifrConfiguracionCarreras = tifrConfiguracionCarreras;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("tifrId: " + tifrId);
    	sb.append(tabulador);
    	sb.append("tifrDescripcion: " + (tifrDescripcion==null? "NULL":tifrDescripcion));  
    	sb.append(tabulador);
    	sb.append("tifrNivelFormacion: " + (tifrNivelFormacion==null? "NULL":tifrNivelFormacion.getNvfrId()));  
        return sb.toString();
	}
	
}
