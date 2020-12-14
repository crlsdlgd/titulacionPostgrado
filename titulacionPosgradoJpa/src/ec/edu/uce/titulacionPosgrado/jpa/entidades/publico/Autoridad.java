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
   
 ARCHIVO:     Autoridad.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Autoridad de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 05-Mayo-2016		 Gabriel Mafla  			          Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;




/**
 * Clase (Entity Bean) Autoridad.
 * Entity Bean que representa a la tabla Autoridad de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table (name = "autoridad", schema = "titulacion_posgrado")
public class Autoridad implements Serializable {

	private static final long serialVersionUID = -4273730586280953983L;

	private int atrId;
	private String atrIdentificacion;
	private String atrNombres;
	private String atrPrimerApellido;
	private String atrSegundoApellido;
	private Integer atrEstado;
	private Integer atrTipo;
	private Facultad atrFacultad;
	private Integer atrSexo;
	
	public Autoridad() {
	}

	public Autoridad(int atrId) {
		this.atrId = atrId;
	}

	
	public Autoridad(int atrId, String atrIdentificacion, String atrNombres,
			String atrPrimerApellido, String atrSegundoApellido,
			Integer atrEstado, Integer atrTipo, Facultad atrFacultad) {
		this.atrId = atrId;
		this.atrIdentificacion = atrIdentificacion;
		this.atrNombres = atrNombres;
		this.atrPrimerApellido = atrPrimerApellido;
		this.atrSegundoApellido = atrSegundoApellido;
		this.atrEstado = atrEstado;
		this.atrTipo = atrTipo;
		this.atrFacultad = atrFacultad;
	}
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "atr_id", unique = true, nullable = false)
	public int getAtrId() {
		return atrId;
	}

	public void setAtrId(int atrId) {
		this.atrId = atrId;
	}

	@Column(name = "atr_identificacion", length = 128)
	@Length(max = 13)
	public String getAtrIdentificacion() {
		return atrIdentificacion;
	}

	public void setAtrIdentificacion(String atrIdentificacion) {
		this.atrIdentificacion = atrIdentificacion;
	}

	@Column(name = "atr_nombres", length = 128)
	@Length(max = 50)
	public String getAtrNombres() {
		return atrNombres;
	}

	public void setAtrNombres(String atrNombres) {
		this.atrNombres = atrNombres;
	}

	@Column(name = "atr_primer_apellido", length = 128)
	@Length(max = 32)
	public String getAtrPrimerApellido() {
		return atrPrimerApellido;
	}

	public void setAtrPrimerApellido(String atrPrimerApellido) {
		this.atrPrimerApellido = atrPrimerApellido;
	}

	@Column(name = "atr_segundo_apellido", length = 128)
	@Length(max = 32)
	public String getAtrSegundoApellido() {
		return atrSegundoApellido;
	}

	public void setAtrSegundoApellido(String atrSegundoApellido) {
		this.atrSegundoApellido = atrSegundoApellido;
	}

	@Column(name = "atr_estado")
	public Integer getAtrEstado() {
		return atrEstado;
	}

	public void setAtrEstado(Integer atrEstado) {
		this.atrEstado = atrEstado;
	}

	@Column(name = "atr_tipo")
	public Integer getAtrTipo() {
		return atrTipo;
	}

	public void setAtrTipo(Integer atrTipo) {
		this.atrTipo = atrTipo;
	}
	
	@JoinColumn(name = "fcl_id", referencedColumnName = "fcl_id")
	@ManyToOne
	public Facultad getAtrFacultad() {
		return atrFacultad;
	}

	public void setAtrFacultad(Facultad atrFacultad) {
		this.atrFacultad = atrFacultad;
	}

	
	
	@Column(name = "atr_sexo")
	public Integer getAtrSexo() {
		return atrSexo;
	}

	public void setAtrSexo(Integer atrSexo) {
		this.atrSexo = atrSexo;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("atrId: " + atrId);
    	sb.append(tabulador);
    	sb.append("atrIdentificacion: " + (atrIdentificacion==null? "NULL":atrIdentificacion));  
    	sb.append(tabulador);
    	sb.append("atrNombres: " + (atrNombres==null? "NULL":atrNombres));
    	sb.append(tabulador);
    	sb.append("atrPrimerApellido: " + (atrPrimerApellido==null? "NULL":atrPrimerApellido)); 
    	sb.append(tabulador);
    	sb.append("atrSegundoApellido: " + (atrSegundoApellido==null? "NULL":atrSegundoApellido)); 
    	sb.append(tabulador);
    	sb.append("atrEstado: " + (atrEstado==null? "NULL":atrEstado)); 
    	sb.append(tabulador);
    	sb.append("atrTipo: " + (atrTipo==null? "NULL":atrTipo));
    	sb.append(tabulador);
    	sb.append("atrFacultad: " + (atrFacultad==null? "NULL":atrFacultad.getFclId()));
        return sb.toString();
	}
	
}
