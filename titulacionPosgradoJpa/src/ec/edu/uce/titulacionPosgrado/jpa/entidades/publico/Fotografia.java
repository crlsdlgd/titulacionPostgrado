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
   
 ARCHIVO:     FichaEstudiante.java	  
 DESCRIPCION: Entity Bean que representa a la tabla FichaEstudiante de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Clase (Entity Bean) FichaEstudiante.
 * Entity Bean que representa a la tabla FichaEstudiante de la BD.
 * @author dalbuja.
 * @version 1.0
 */
//@Entity
//@Table(name = "fotografia", schema = "titulacion_posgrado")
public class Fotografia implements Serializable {

	private static final long serialVersionUID = 3985818892637114744L;
	private int ftgId;
	private byte[] prsFoto;
	private Persona ftgPersona;
	
	
	public Fotografia() {
	}

	public Fotografia(Integer ftgId) {
		this.ftgId = ftgId;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ftg_id", unique = true, nullable = false)
	public int getFtgId() {
		return this.ftgId;
	}

	public void setFtgId(int ftgId) {
		this.ftgId = ftgId;
	}


//	@JoinColumn(name = "prs_id", referencedColumnName = "prs_id")
//	@ManyToOne
	public Persona getFtgPersona() {
		return ftgPersona;
	}

	public void setFtgPersona(Persona ftgPersona) {
		this.ftgPersona = ftgPersona;
	}


//	@Column(name = "ftg_foto", columnDefinition = "LONGVARBINARY")
//	@Basic(fetch = FetchType.LAZY) 
	public byte[] getPrsFoto() {
		return prsFoto;
	}

	public void setPrsFoto(byte[] prsFoto) {
		this.prsFoto = prsFoto;
	}
}
