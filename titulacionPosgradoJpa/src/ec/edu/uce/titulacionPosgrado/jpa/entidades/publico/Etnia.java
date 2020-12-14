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
   
 ARCHIVO:     Etnia.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Etnia de la BD. 
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) Etnia.
 * Entity Bean que representa a la tabla Etnia de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "etnia", schema = "titulacion_posgrado")
@NamedQuery(
	    name="listarTodasEtnias",
	    query=" SELECT etn FROM Etnia etn "
	)
public class Etnia implements Serializable {

	private static final long serialVersionUID = -6957606655471654353L;
	private int etnId;
	private String etnCodigoSniese;
	private String etnDescripcion;
	private List<Persona> etnPersonas;

	public Etnia() {
	}

	public Etnia(Integer etnId) {
		this.etnId = etnId;
	}

	public Etnia(int etnId, String etnCodigoSniese, String etnDescripcion,
			List<Persona> etnPersonas) {
		this.etnId = etnId;
		this.etnCodigoSniese = etnCodigoSniese;
		this.etnDescripcion = etnDescripcion;
		this.etnPersonas = etnPersonas;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "etn_id", unique = true, nullable = false)
	public int getEtnId() {
		return this.etnId;
	}

	public void setEtnId(int etnId) {
		this.etnId = etnId;
	}

	@Column(name = "etn_codigo_sniese", length = 1)
	@Length(max = 1)
	public String getEtnCodigoSniese() {
		return this.etnCodigoSniese;
	}

	public void setEtnCodigoSniese(String etnCodigoSniese) {
		this.etnCodigoSniese = etnCodigoSniese;
	}

	@Column(name = "etn_descripcion", length = 20)
	@Length(max = 20)
	public String getEtnDescripcion() {
		return this.etnDescripcion;
	}

	public void setEtnDescripcion(String etnDescripcion) {
		this.etnDescripcion = etnDescripcion;
	}

	@OneToMany(mappedBy = "prsEtnia")
	public List<Persona> getEtnPersonas() {
		return etnPersonas;
	}

	public void setEtnPersonas(List<Persona> etnPersonas) {
		this.etnPersonas = etnPersonas;
	}
	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("etnId: " + etnId);
    	sb.append(tabulador);
    	sb.append("etnCodigoSniese: " + (etnCodigoSniese==null? "NULL":etnCodigoSniese));  
    	sb.append(tabulador);
    	sb.append("etnDescripcion: " + (etnDescripcion==null? "NULL":etnDescripcion));  
        return sb.toString();
	}




}
