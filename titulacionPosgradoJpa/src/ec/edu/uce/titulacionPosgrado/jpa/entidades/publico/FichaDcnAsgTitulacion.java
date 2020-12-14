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
   
 ARCHIVO:     FichaDcnAsgTitulacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla ficha_dcn_asg_titulacion de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
 * Clase (Entity Bean) FichaDcnAsgTitulacion.
 * Entity Bean que representa a la tabla ficha_dcn_asg_titulacion de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "ficha_dcn_asg_titulacion", schema = "titulacion_posgrado")
public class FichaDcnAsgTitulacion implements Serializable {

	private static final long serialVersionUID = 4332700057890825008L;

	private int fcdcasttId;
	private Integer fcdcasttTipotribunal;
	private Integer fcdcasttEstado;
	private BigDecimal fcdcasttNota;
	private Timestamp fcdcasttFechaAsentamiento;
	
	private FichaDocente fcdcasttFichaDocente;
	private AsignacionTitulacion fcdcasttAsignacionTitulacion;
	
	public FichaDcnAsgTitulacion() {
	}

	public FichaDcnAsgTitulacion(int fcdcasttId) {
		this.fcdcasttId = fcdcasttId;
	}

	public FichaDcnAsgTitulacion(int fcdcasttId,
			FichaDocente fcdcasttFichaDocente,
			AsignacionTitulacion fcdcasttAsignacionTitulacion) {
		this.fcdcasttId = fcdcasttId;
		this.fcdcasttFichaDocente = fcdcasttFichaDocente;
		this.fcdcasttAsignacionTitulacion = fcdcasttAsignacionTitulacion;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "fcdcastt_id", unique = true, nullable = false)
	public int getFcdcasttId() {
		return fcdcasttId;
	}

	public void setFcdcasttId(int fcdcasttId) {
		this.fcdcasttId = fcdcasttId;
	}

	@Column(name = "fcdcastt_tipo_tribunal")
	public Integer getFcdcasttTipotribunal() {
		return fcdcasttTipotribunal;
	}

	public void setFcdcasttTipotribunal(Integer fcdcasttTipotribunal) {
		this.fcdcasttTipotribunal = fcdcasttTipotribunal;
	}
	
	@Column(name = "fcdcastt_estado")
	public Integer getFcdcasttEstado() {
		return fcdcasttEstado;
	}

	public void setFcdcasttEstado(Integer fcdcasttEstado) {
		this.fcdcasttEstado = fcdcasttEstado;
	}

	@Column(name = "fcdcastt_nota")
	public BigDecimal getFcdcasttNota() {
		return fcdcasttNota;
	}

	public void setFcdcasttNota(BigDecimal fcdcasttNota) {
		this.fcdcasttNota = fcdcasttNota;
	}

	@Column(name = "fcdcastt_fecha_asentamiento")
	public Timestamp getFcdcasttFechaAsentamiento() {
		return fcdcasttFechaAsentamiento;
	}

	public void setFcdcasttFechaAsentamiento(Timestamp fcdcasttFechaAsentamiento) {
		this.fcdcasttFechaAsentamiento = fcdcasttFechaAsentamiento;
	}

	
	@JoinColumn(name = "fcdc_id", referencedColumnName = "fcdc_id")
	@ManyToOne
	public FichaDocente getFcdcasttFichaDocente() {
		return fcdcasttFichaDocente;
	}

	public void setFcdcasttFichaDocente(FichaDocente fcdcasttFichaDocente) {
		this.fcdcasttFichaDocente = fcdcasttFichaDocente;
	}

	@JoinColumn(name = "astt_id", referencedColumnName = "astt_id")
	@ManyToOne
	public AsignacionTitulacion getFcdcasttAsignacionTitulacion() {
		return fcdcasttAsignacionTitulacion;
	}

	public void setFcdcasttAsignacionTitulacion(
			AsignacionTitulacion fcdcasttAsignacionTitulacion) {
		this.fcdcasttAsignacionTitulacion = fcdcasttAsignacionTitulacion;
	}
	
}
