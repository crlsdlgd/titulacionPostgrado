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
   
 ARCHIVO:     Aptitud.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Aptitud de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26 - Julio - 2018		 Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Clase (Entity Bean) Aptitud.
 * Entity Bean que representa a la tabla Aptitud de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "aptitud", schema = "titulacion_posgrado")
public class Aptitud implements Serializable{

	private static final long serialVersionUID = 2768088200730826865L;
	
	private int aptId;
	private Integer aptRequisitos;
	private Integer aptSuficienciaIngles; 
	private Integer aptFinMalla; 
	private Integer aptAproboActualizar; 
	private Integer aptAproboTutor; 
	private BigDecimal aptNotaDirector;
	private ProcesoTramite aptProcesoTramite;
	
	public Aptitud() {
	}

	public Aptitud(int aptId) {
		this.aptId = aptId;
	}
	
	public Aptitud(int aptId, Integer aptRequisitos, Integer aptSuficienciaIngles, Integer aptFinMalla,
			Integer aptAproboActualizar, Integer aptAproboTutor, BigDecimal aptNotaDirector,
			ProcesoTramite aptProcesoTramite) {
		this.aptId = aptId;
		this.aptRequisitos = aptRequisitos;
		this.aptSuficienciaIngles = aptSuficienciaIngles;
		this.aptFinMalla = aptFinMalla;
		this.aptAproboActualizar = aptAproboActualizar;
		this.aptAproboTutor = aptAproboTutor;
		this.aptNotaDirector = aptNotaDirector;
		this.aptProcesoTramite = aptProcesoTramite;
	}
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "apt_id", unique = true, nullable = false)
	public int getAptId() {
		return aptId;
	}

	public void setAptId(int aptId) {
		this.aptId = aptId;
	}

	@Column(name = "apt_requisitos")
	public Integer getAptRequisitos() {
		return aptRequisitos;
	}

	public void setAptRequisitos(Integer aptRequisitos) {
		this.aptRequisitos = aptRequisitos;
	}

	
	@Column(name = "apt_suficiencia_ingles")
	public Integer getAptSuficienciaIngles() {
		return aptSuficienciaIngles;
	}

	public void setAptSuficienciaIngles(Integer aptSuficienciaIngles) {
		this.aptSuficienciaIngles = aptSuficienciaIngles;
	}

	@Column(name = "apt_fin_malla")
	public Integer getAptFinMalla() {
		return aptFinMalla;
	}

	public void setAptFinMalla(Integer aptFinMalla) {
		this.aptFinMalla = aptFinMalla;
	}

	@Column(name = "apt_aprobo_tutor")
	public Integer getAptAproboTutor() {
		return aptAproboTutor;
	}

	public void setAptAproboTutor(Integer aptAproboTutor) {
		this.aptAproboTutor = aptAproboTutor;
	}

	@Column(name = "apt_nota_director")
	public BigDecimal getAptNotaDirector() {
		return aptNotaDirector;
	}

	public void setAptNotaDirector(BigDecimal aptNotaDirector) {
		this.aptNotaDirector = aptNotaDirector;
	}
	
	
	@Column(name = "apt_aprobo_actualizar")
	public Integer getAptAproboActualizar() {
		return aptAproboActualizar;
	}
	
	public void setAptAproboActualizar(Integer aptAproboActualizar) {
		this.aptAproboActualizar = aptAproboActualizar;
	}
	

	@JoinColumn(name = "prtr_id", referencedColumnName = "prtr_id")
	@ManyToOne
	public ProcesoTramite getAptProcesoTramite() {
		return aptProcesoTramite;
	}

	public void setAptProcesoTramite(ProcesoTramite aptProcesoTramite) {
		this.aptProcesoTramite = aptProcesoTramite;
	}  
	

}
