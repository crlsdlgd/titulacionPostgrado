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
   
 ARCHIVO:     Facultad.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Facultad de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		   		Daniel Albuja  			          Emisión Inicia
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
 * Clase (Entity Bean) Facultad.
 * Entity Bean que representa a la tabla Facultad de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "facultad", schema = "titulacion_posgrado")
public class Facultad implements Serializable {

	private static final long serialVersionUID = -4973484177301950662L;
	private int fclId;
	private String fclDescripcion;
	private String fclCodSori;
	private String fclUej;
	private List<Carrera> fclCarreras;
	
	public Facultad() {
	}

	public Facultad(int fclId) {
		this.fclId = fclId;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "fcl_id", unique = true, nullable = false)
	public int getFclId() {
		return this.fclId;
	}

	public void setFclId(int fclId) {
		this.fclId = fclId;
	}

	@Column(name = "fcl_descripcion", length = 128)
	@Length(max = 128)
	public String getFclDescripcion() {
		return this.fclDescripcion;
	}

	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}


	@Column(name = "cod_sori", length = 2)
	@Length(max = 2)
	public String getFclCodSori() {
		return fclCodSori;
	}

	public void setFclCodSori(String fclCodSori) {
		this.fclCodSori = fclCodSori;
	}

	@Column(name = "uej", length = 2)
	@Length(max = 2)
	public String getFclUej() {
		return fclUej;
	}

	public void setFclUej(String fclUej) {
		this.fclUej = fclUej;
	}

	@OneToMany(mappedBy = "crrFacultad")
	public List<Carrera> getFclCarreras() {
		return fclCarreras;
	}

	public void setFclCarreras(List<Carrera> fclCarreras) {
		this.fclCarreras = fclCarreras;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("fclId: " + fclId);
    	sb.append(tabulador);
    	sb.append("fclDescripcion: " + (fclDescripcion==null? "NULL":fclDescripcion));  
        return sb.toString();
	}

}
