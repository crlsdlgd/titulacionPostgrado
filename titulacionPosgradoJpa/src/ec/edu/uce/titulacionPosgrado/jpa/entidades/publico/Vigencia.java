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
   
 ARCHIVO:     Vigencia.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Vigencia de la BD. 
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) Vigencia.
 * Entity Bean que representa a la tabla Vigencia de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "vigencia", schema = "titulacion_posgrado")
public class Vigencia implements  Serializable {

	private static final long serialVersionUID = -1884023531139941578L;
	private int vgnId;
	private String vgnDescripcion;
	private List<ConfiguracionCarrera> vgnConfiguracionCarreras;

	public Vigencia() {
	}

	public Vigencia(int vgnId) {
		this.vgnId = vgnId;
	}


	public Vigencia(int vgnId, String vgnDescripcion,
			List<ConfiguracionCarrera> vgnConfiguracionCarreras) {
		this.vgnId = vgnId;
		this.vgnDescripcion = vgnDescripcion;
		this.vgnConfiguracionCarreras = vgnConfiguracionCarreras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "vgn_id", unique = true, nullable = false)
	public int getVgnId() {
		return this.vgnId;
	}

	public void setVgnId(int vgnId) {
		this.vgnId = vgnId;
	}

	@Column(name = "vgn_descripcion", length = 128)
	@Length(max = 128)
	public String getVgnDescripcion() {
		return this.vgnDescripcion;
	}

	public void setVgnDescripcion(String vgnDescripcion) {
		this.vgnDescripcion = vgnDescripcion;
	}

	@OneToMany(mappedBy = "cncrVigencia")
	public List<ConfiguracionCarrera> getVgnConfiguracionCarreras() {
		return vgnConfiguracionCarreras;
	}

	public void setVgnConfiguracionCarreras(
			List<ConfiguracionCarrera> vgnConfiguracionCarreras) {
		this.vgnConfiguracionCarreras = vgnConfiguracionCarreras;
	}
	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("vgnId: " + vgnId);
    	sb.append(tabulador);
    	sb.append("persvgnDescripcionona: " + (vgnDescripcion==null? "NULL":vgnDescripcion));  
        return sb.toString();
	}

}
