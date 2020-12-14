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
   
 ARCHIVO:     Convocatoria.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Convocatoria de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		   		Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) Convocatoria.
 * Entity Bean que representa a la tabla Convocatoria de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "convocatoria", schema = "titulacion_posgrado")
public class Convocatoria implements Serializable {

	private static final long serialVersionUID = -4973484177301950662L;
	private int cnvId;
	private String cnvDescripcion;
	private Date cnvFechaInicio;
	private Date cnvFechaFin;
	private Integer cnvEstado;
	private Integer cnvEstadoFase;
	private List<TramiteTitulo> cnvTramiteTitulo;

	public Convocatoria() {
	}

	public Convocatoria(int cnvId) {
		this.cnvId = cnvId;
	}

	
	public Convocatoria(int cnvId, String cnvDescripcion, Date cnvFechaInicio,
			Date cnvFechaFin, Integer cnvEstado, Integer cnvEstadoFase,
			List<TramiteTitulo> cnvTramiteTitulo) {
		this.cnvId = cnvId;
		this.cnvDescripcion = cnvDescripcion;
		this.cnvFechaInicio = cnvFechaInicio;
		this.cnvFechaFin = cnvFechaFin;
		this.cnvEstado = cnvEstado;
		this.cnvEstadoFase = cnvEstadoFase;
		this.cnvTramiteTitulo = cnvTramiteTitulo;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "cnv_id", unique = true, nullable = false)
	public int getCnvId() {
		return cnvId;
	}

	public void setCnvId(int cnvId) {
		this.cnvId = cnvId;
	}
	

	@Column(name = "cnv_descripcion", length = 32)
	@Length(max = 32)
	public String getCnvDescripcion() {
		return this.cnvDescripcion;
	}


	public void setCnvDescripcion(String cnvDescripcion) {
		this.cnvDescripcion = cnvDescripcion;
	}

	
	@Temporal(TemporalType.DATE)
	@Column(name = "cnv_fecha_inicio", length = 13)
	public Date getCnvFechaInicio() {
		return cnvFechaInicio;
	}

	public void setCnvFechaInicio(Date cnvFechaInicio) {
		this.cnvFechaInicio = cnvFechaInicio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "cnv_fecha_fin", length = 13)
	public Date getCnvFechaFin() {
		return cnvFechaFin;
	}

	public void setCnvFechaFin(Date cnvFechaFin) {
		this.cnvFechaFin = cnvFechaFin;
	}

	
	@Column(name = "cnv_estado")
	public Integer getCnvEstado() {
		return cnvEstado;
	}

	public void setCnvEstado(Integer cnvEstado) {
		this.cnvEstado = cnvEstado;
	}
	
	@Column(name = "cnv_estado_fase")
	public Integer getCnvEstadoFase() {
		return cnvEstadoFase;
	}

	public void setCnvEstadoFase(Integer cnvEstadoFase) {
		this.cnvEstadoFase = cnvEstadoFase;
	}

	@OneToMany(mappedBy = "trttConvocatoria")
	public List<TramiteTitulo> getCnvTramiteTitulo() {
		return cnvTramiteTitulo;
	}

	public void setCnvTramiteTitulo(List<TramiteTitulo> cnvTramiteTitulo) {
		this.cnvTramiteTitulo = cnvTramiteTitulo;
	}

	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("cnvId: " + cnvId);
    	sb.append(tabulador);
    	sb.append("cnvDescripcion: " + (cnvDescripcion==null? "NULL":cnvDescripcion));  
    	sb.append(tabulador);
    	sb.append("cnvFechaInicio: " + (cnvFechaInicio==null? "NULL":cnvFechaInicio)); 
    	sb.append(tabulador);
    	sb.append("cnvFechaFin: " + (cnvFechaFin==null? "NULL":cnvFechaFin));
    	sb.append(tabulador);
    	sb.append("cnvEstadoFase: " + (cnvEstadoFase==null? "NULL":cnvEstadoFase));
    	sb.append(tabulador);
    	sb.append("cnvEstado: " + (cnvEstado==null? "NULL":cnvEstado.intValue())); 
        return sb.toString();
	}
	

}
