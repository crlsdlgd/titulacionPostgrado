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
   
 ARCHIVO:     MecanismoTitulacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla MecanismoTitulacion de la BD. 
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) MecanismoTitulacion.
 * Entity Bean que representa a la tabla MecanismoTitulacion de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "mecanismo_titulacion", schema = "titulacion_posgrado")
public class MecanismoTitulacion implements Serializable {

	private static final long serialVersionUID = -6957606655471654353L;
	
	private int mcttId;
	private String mcttCodigoSniese;
	private String mcttDescripcion;
	private Integer mcttEstado;
	private List<MecanismoCarrera> mcttListMecanismosCarreras;

	public MecanismoTitulacion() {
	}

	public MecanismoTitulacion(int mcttId) {
		this.mcttId = mcttId;
	}

	public MecanismoTitulacion(
			int mcttId,
			String mcttCodigoSniese,
			String mcttDescripcion,
			Integer mcttEstado) {
		this.mcttId = mcttId;
		this.mcttCodigoSniese = mcttCodigoSniese;
		this.mcttDescripcion = mcttDescripcion;
		this.mcttEstado = mcttEstado;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mctt_id", unique = true, nullable = false)
	public int getMcttId() {
		return this.mcttId;
	}

	public void setMcttId(int mcttId) {
		this.mcttId = mcttId;
	}

	@Column(name = "mctt_codigo_sniese", length = 1)
	@Length(max = 1)
	public String getMcttCodigoSniese() {
		return this.mcttCodigoSniese;
	}

	public void setMcttCodigoSniese(String mcttCodigoSniese) {
		this.mcttCodigoSniese = mcttCodigoSniese;
	}

	@Column(name = "mctt_descripcion", length = 20)
	@Length(max = 20)
	public String getMcttDescripcion() {
		return this.mcttDescripcion;
	}

	public void setMcttDescripcion(String mcttDescripcion) {
		this.mcttDescripcion = mcttDescripcion;
	}

	
	@Column(name = "mctt_estado")
	public Integer getMcttEstado() {
		return mcttEstado;
	}

	public void setMcttEstado(Integer mcttEstado) {
		this.mcttEstado = mcttEstado;
	}

	@OneToMany(mappedBy = "mccrMecanismoTitulacion")
	public List<MecanismoCarrera> getMcttListMecanismosCarreras() {
		return mcttListMecanismosCarreras;
	}

	public void setMcttListMecanismosCarreras(
			List<MecanismoCarrera> mcttListMecanismosCarreras) {
		this.mcttListMecanismosCarreras = mcttListMecanismosCarreras;
	}
	
	
	
	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("mcttId: " + mcttId);
    	sb.append(tabulador);
    	sb.append("mcttCodigoSniese: " + (mcttCodigoSniese==null? "NULL":mcttCodigoSniese));  
    	sb.append(tabulador);
    	sb.append("mcttDescripcion: " + (mcttDescripcion==null? "NULL":mcttDescripcion));  
    	sb.append(tabulador);
    	sb.append("mcttEstado: " + (mcttEstado==null? "NULL":mcttEstado.intValue()));
        return sb.toString();
	}



	
}
