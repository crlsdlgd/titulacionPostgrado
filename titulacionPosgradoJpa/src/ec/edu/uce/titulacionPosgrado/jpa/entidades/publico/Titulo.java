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
   
 ARCHIVO:     Titulo.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Titulo de la BD. 
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
 * Clase (Entity Bean) Titulo.
 * Entity Bean que representa a la tabla Titulo de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "titulo", schema = "titulacion_posgrado")
public class Titulo implements  Serializable {

	private static final long serialVersionUID = 4885767925200975907L;
	private int ttlId;
	private String ttlDescripcion;
	private Integer ttlSexo;
	private Integer ttlEstado;
	private Integer ttlTipo;
	private List<ConfiguracionCarrera> ttlConfiguracionCarreras;
	
	public Titulo() {
	}

	public Titulo(int ttlId) {
		this.ttlId = ttlId;
	}

	public Titulo(int ttlId, String ttlDescripcion, Integer ttlSexo,
			Integer ttlEstado, Integer ttlTipo,
			List<ConfiguracionCarrera> ttlConfiguracionCarreras,
			List<FichaEstudiante> ttlFichasEstudiantes) {
		this.ttlId = ttlId;
		this.ttlDescripcion = ttlDescripcion;
		this.ttlSexo = ttlSexo;
		this.ttlEstado = ttlEstado;
		this.ttlTipo = ttlTipo;
		this.ttlConfiguracionCarreras = ttlConfiguracionCarreras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ttl_id", unique = true, nullable = false)
	public int getTtlId() {
		return this.ttlId;
	}

	public void setTtlId(int ttlId) {
		this.ttlId = ttlId;
	}

	@Column(name = "ttl_descripcion", length = 512)
	@Length(max = 512)
	public String getTtlDescripcion() {
		return this.ttlDescripcion;
	}

	public void setTtlDescripcion(String ttlDescripcion) {
		this.ttlDescripcion = ttlDescripcion;
	}

	@Column(name = "ttl_sexo")
	public Integer getTtlSexo() {
		return this.ttlSexo;
	}

	public void setTtlSexo(Integer ttlSexo) {
		this.ttlSexo = ttlSexo;
	}

	@Column(name = "ttl_estado")
	public Integer getTtlEstado() {
		return ttlEstado;
	}

	public void setTtlEstado(Integer ttlEstado) {
		this.ttlEstado = ttlEstado;
	}

	@Column(name = "ttl_tipo")
	public Integer getTtlTipo() {
		return ttlTipo;
	}

	public void setTtlTipo(Integer ttlTipo) {
		this.ttlTipo = ttlTipo;
	}


	@OneToMany(mappedBy = "cncrTitulo")
	public List<ConfiguracionCarrera> getTtlConfiguracionCarreras() {
		return ttlConfiguracionCarreras;
	}

	public void setTtlConfiguracionCarreras(
			List<ConfiguracionCarrera> ttlConfiguracionCarreras) {
		this.ttlConfiguracionCarreras = ttlConfiguracionCarreras;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("ttlId: " + ttlId);
    	sb.append(tabulador);
    	sb.append("ttlDescripcion: " + (ttlDescripcion==null? "NULL":ttlDescripcion));  
    	sb.append(tabulador);
    	sb.append("ttlSexo: " + (ttlSexo==null? "NULL":ttlSexo.intValue()));
    	sb.append(tabulador);
    	sb.append("ttlEstado: " + (ttlEstado==null? "NULL":ttlEstado.intValue()));  
    	sb.append(tabulador);
    	sb.append("ttlTipo: " + (ttlTipo==null? "NULL":ttlTipo.intValue()));  
        return sb.toString();
	}
}
