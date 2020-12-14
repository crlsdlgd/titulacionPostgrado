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
   
 ARCHIVO:     Duracion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Duracion de la BD. 
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

/**
 * Clase (Entity Bean) Duracion.
 * Entity Bean que representa a la tabla Duracion de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "duracion", schema = "titulacion_posgrado")
public class Duracion implements  Serializable {

	private static final long serialVersionUID = -1884023531139941578L;
	private int drcId;
	private Integer drcTipo;
	private Integer drcTiempo;
	private List<ConfiguracionCarrera> drcConfiguracionCarreras;

	public Duracion() {
	}

	public Duracion(int drcId) {
		this.drcId = drcId;
	}



	public Duracion(int drcId, Integer drcTipo, Integer drcTiempo,
			List<ConfiguracionCarrera> drcConfiguracionCarreras) {
		this.drcId = drcId;
		this.drcTipo = drcTipo;
		this.drcTiempo = drcTiempo;
		this.drcConfiguracionCarreras = drcConfiguracionCarreras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "drc_id", unique = true, nullable = false)
	public int getDrcId() {
		return drcId;
	}

	public void setDrcId(int drcId) {
		this.drcId = drcId;
	}

	@Column(name = "drc_tipo")
	public Integer getDrcTipo() {
		return drcTipo;
	}

	public void setDrcTipo(Integer drcTipo) {
		this.drcTipo = drcTipo;
	}

	@Column(name = "drc_tiempo")
	public Integer getDrcTiempo() {
		return drcTiempo;
	}

	
	public void setDrcTiempo(Integer drcTiempo) {
		this.drcTiempo = drcTiempo;
	}

	@OneToMany(mappedBy = "cncrDuracion")
	public List<ConfiguracionCarrera> getDrcConfiguracionCarreras() {
		return drcConfiguracionCarreras;
	}

	public void setDrcConfiguracionCarreras(
			List<ConfiguracionCarrera> drcConfiguracionCarreras) {
		this.drcConfiguracionCarreras = drcConfiguracionCarreras;
	}
	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("drcId: " + drcId);
    	sb.append(tabulador);
    	sb.append("drcTipo: " + (drcTipo==null? "NULL":drcTipo));  
      	sb.append(tabulador);
    	sb.append("drcTiempo: " + (drcTiempo==null? "NULL":drcTiempo));  
        return sb.toString();
	}

}
