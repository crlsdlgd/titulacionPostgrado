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
   
 ARCHIVO:     Modalidad.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Modalidad de la BD. 
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
 * Clase (Entity Bean) Modalidad.
 * Entity Bean que representa a la tabla Modalidad de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "modalidad", schema = "titulacion_posgrado")
public class Modalidad implements Serializable {

	private static final long serialVersionUID = -4973484177301950662L;
	private int mdlId;
	private String mdlDescripcion;
	private List<ConfiguracionCarrera> mdlConfiguracionCarreras;

	public Modalidad() {
	}

	public Modalidad(int mdlId) {
		this.mdlId = mdlId;
	}

	public Modalidad(int mdlId, String mdlDescripcion,
			List<ConfiguracionCarrera> mdlConfiguracionCarreras) {
		this.mdlId = mdlId;
		this.mdlDescripcion = mdlDescripcion;
		this.mdlConfiguracionCarreras = mdlConfiguracionCarreras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mdl_id", unique = true, nullable = false)
	public int getMdlId() {
		return this.mdlId;
	}

	public void setMdlId(int mdlId) {
		this.mdlId = mdlId;
	}

	@Column(name = "mdl_descripcion", length = 128)
	@Length(max = 128)
	public String getMdlDescripcion() {
		return this.mdlDescripcion;
	}

	public void setMdlDescripcion(String mdlDescripcion) {
		this.mdlDescripcion = mdlDescripcion;
	}


	
	@OneToMany(mappedBy = "cncrModalidad")
	public List<ConfiguracionCarrera> getMdlConfiguracionCarreras() {
		return mdlConfiguracionCarreras;
	}

	public void setMdlConfiguracionCarreras(
			List<ConfiguracionCarrera> mdlConfiguracionCarreras) {
		this.mdlConfiguracionCarreras = mdlConfiguracionCarreras;
	}

	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("mdlId: " + mdlId);
    	sb.append(tabulador);
    	sb.append("mdlDescripcion: " + (mdlDescripcion==null? "NULL":mdlDescripcion));  
        return sb.toString();
	}

}
