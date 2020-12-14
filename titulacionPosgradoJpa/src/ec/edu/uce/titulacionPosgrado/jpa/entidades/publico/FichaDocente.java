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
   
 ARCHIVO:     FichaDocente.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Ficha_Docente de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018				Daniel Albuja  			          Emisión Inicial
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
 * Clase (Entity Bean) FichaDocente.
 * Entity Bean que representa a la tabla Ficha_Docente de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "ficha_docente", schema = "titulacion_posgrado")
public class FichaDocente implements Serializable {

	private static final long serialVersionUID = 4332700057890825008L;
	
	private int fcdcId;
	private Integer fcdcEstado;
	private String fcdcAbreviaturaTitulo; 
	
	private Carrera fcdcCarrera;
	private Persona fcdcPersona;
	
	private List<FichaDcnAsgTitulacion> fcdcListFichasDcnAsgTitulacion;
	
	public FichaDocente() {
	}

	public FichaDocente(int fcdcId) {
		this.fcdcId = fcdcId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "fcdc_id", unique = true, nullable = false)
	public int getFcdcId() {
		return fcdcId;
	}

	public void setFcdcId(int fcdcId) {
		this.fcdcId = fcdcId;
	}

	@Column(name = "fcdc_estado")
	public Integer getFcdcEstado() {
		return fcdcEstado;
	}

	public void setFcdcEstado(Integer fcdcEstado) {
		this.fcdcEstado = fcdcEstado;
	}

	@Column(name = "fcdc_abreviatura_titulo", length = 24)
	@Length(max = 24)
	public String getFcdcAbreviaturaTitulo() {
		return fcdcAbreviaturaTitulo;
	}

	public void setFcdcAbreviaturaTitulo(String fcdcAbreviaturaTitulo) {
		this.fcdcAbreviaturaTitulo = fcdcAbreviaturaTitulo;
	}

	@JoinColumn(name = "crr_id", referencedColumnName = "crr_id")
	@ManyToOne
	public Carrera getFcdcCarrera() {
		return fcdcCarrera;
	}

	public void setFcdcCarrera(Carrera fcdcCarrera) {
		this.fcdcCarrera = fcdcCarrera;
	}

	@JoinColumn(name = "prs_id", referencedColumnName = "prs_id")
	@ManyToOne
	public Persona getFcdcPersona() {
		return fcdcPersona;
	}

	public void setFcdcPersona(Persona fcdcPersona) {
		this.fcdcPersona = fcdcPersona;
	}

	@OneToMany(mappedBy = "fcdcasttFichaDocente")
	public List<FichaDcnAsgTitulacion> getFcdcListFichasDcnAsgTitulacion() {
		return fcdcListFichasDcnAsgTitulacion;
	}

	public void setFcdcListFichasDcnAsgTitulacion(
			List<FichaDcnAsgTitulacion> fcdcListFichasDcnAsgTitulacion) {
		this.fcdcListFichasDcnAsgTitulacion = fcdcListFichasDcnAsgTitulacion;
	}


	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("fcdcId: " + fcdcId);
    	sb.append(tabulador);
    	sb.append("fcdcEstado: " + (fcdcEstado==null? "NULL":fcdcEstado)); 
    	sb.append(tabulador);
    	sb.append("fcdcAbreviaturaTitulo: " + (fcdcAbreviaturaTitulo==null? "NULL":fcdcAbreviaturaTitulo));
    	sb.append(tabulador);
    	sb.append(tabulador);
    	sb.append("fcdcCarrera: " + (fcdcCarrera==null? "NULL":fcdcCarrera.getCrrId())); 
    	sb.append(tabulador);
    	sb.append("fcdcPersona: " + (fcdcPersona==null? "NULL":fcdcPersona.getPrsId())); 
        return sb.toString();
	}


	


}
