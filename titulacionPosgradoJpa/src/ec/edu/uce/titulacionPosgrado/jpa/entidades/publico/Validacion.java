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
   
 ARCHIVO:     Validacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Validacion de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17/05/2018		 Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Clase (Entity Bean) Validacion.
 * Entity Bean que representa a la tabla Validacion de la BD.
 * @author dalbuja.
 * @version 1.0
 */

@Entity
@Table(name = "validacion", schema = "titulacion_posgrado")
public class Validacion implements Serializable {

	private static final long serialVersionUID = 4332700057890825008L;
	
	private int vldId;
	private Integer vldSegundaProrroga;
	private Integer vldRslHomologacion;
	private Integer vldRslActConocimiento;
	private Integer vldRslProrroga;
	private Integer vldRslIdoneidad;
	private Integer vldRslAprobacionIngles;
	private Integer vldRslActualizAprob;
	private Integer vldRslCursoActAprob;
	private Integer vldRslHomologacionAprob;
	private String vldComprobantePago;
	private ProcesoTramite vldProcesoTramite;
	
	public Validacion() {
	}

	public Validacion(int vldId) {
		this.vldId = vldId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "vld_id", unique = true, nullable = false)
	public int getVldId() {
		return vldId;
	}

	public void setVldId(int vldId) {
		this.vldId = vldId;
	}

	
	
	
	@Column(name = "vld_segunda_prorroga")
	public Integer getVldSegundaProrroga() {
		return vldSegundaProrroga;
	}

	public void setVldSegundaProrroga(Integer vldSegundaProrroga) {
		this.vldSegundaProrroga = vldSegundaProrroga;
	}

	@Column(name = "vld_rsl_act_conocimiento")
	public Integer getVldRslActConocimiento() {
		return vldRslActConocimiento;
	}

	public void setVldRslActConocimiento(Integer vldRslActConocimiento) {
		this.vldRslActConocimiento = vldRslActConocimiento;
	}

	@Column(name = "vld_rsl_prorroga")
	public Integer getVldRslProrroga() {
		return vldRslProrroga;
	}

	public void setVldRslProrroga(Integer vldRslProrroga) {
		this.vldRslProrroga = vldRslProrroga;
	}

	@Column(name = "vld_rsl_idoneidad")
	public Integer getVldRslIdoneidad() {
		return vldRslIdoneidad;
	}

	public void setVldRslIdoneidad(Integer vldRslIdoneidad) {
		this.vldRslIdoneidad = vldRslIdoneidad;
	}

	@Column(name = "vld_rsl_homologacion")
	public Integer getVldRslHomologacion() {
		return vldRslHomologacion;
	}

	public void setVldRslHomologacion(Integer vldRslHomologacion) {
		this.vldRslHomologacion = vldRslHomologacion;
	}

	@JoinColumn(name = "prtr_id", referencedColumnName = "prtr_id")
	@ManyToOne
	public ProcesoTramite getVldProcesoTramite() {
		return vldProcesoTramite;
	}

	public void setVldProcesoTramite(ProcesoTramite vldProcesoTramite) {
		this.vldProcesoTramite = vldProcesoTramite;
	}

	@Column(name = "vld_rsl_aprobacion_ingles")
	public Integer getVldRslAprobacionIngles() {
		return vldRslAprobacionIngles;
	}

	public void setVldRslAprobacionIngles(Integer vldRslAprobacionIngles) {
		this.vldRslAprobacionIngles = vldRslAprobacionIngles;
	}

	@Column(name = "vld_rsl_actualiz_aprob")
	public Integer getVldRslActualizAprob() {
		return vldRslActualizAprob;
	}

	public void setVldRslActualizAprob(Integer vldRslActualizAprob) {
		this.vldRslActualizAprob = vldRslActualizAprob;
	}

	@Column(name = "vld_rsl_curso_act_aprob")
	public Integer getVldRslCursoActAprob() {
		return vldRslCursoActAprob;
	}

	public void setVldRslCursoActAprob(Integer vldRslCursoActAprob) {
		this.vldRslCursoActAprob = vldRslCursoActAprob;
	}

	@Column(name = "vld_rsl_homologacion_aprob")
	public Integer getVldRslHomologacionAprob() {
		return vldRslHomologacionAprob;
	}

	public void setVldRslHomologacionAprob(Integer vldRslHomologacionAprob) {
		this.vldRslHomologacionAprob = vldRslHomologacionAprob;
	}

	@Column(name = "vld_comprobante_pago")
	public String getVldComprobantePago() {
		return vldComprobantePago;
	}

	public void setVldComprobantePago(String vldComprobantePago) {
		this.vldComprobantePago = vldComprobantePago;
	}
	
}
