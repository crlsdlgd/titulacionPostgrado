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
   
 ARCHIVO:     Carrera.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Carrera de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		   		Daniel Albuja  			          Emisión Inicial
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
 * Clase (Entity Bean) Carrera.
 * Entity Bean que representa a la tabla Carrera de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table (name = "carrera", schema = "titulacion_posgrado")
public class Carrera implements Serializable {

	private static final long serialVersionUID = -4273730586280953983L;

	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private Integer crrNivel;
	private Integer crrEspeCodigo;
	private String crrDetalle;
	private Facultad crrFacultad;
	private Integer crrTipoEvaluacion;
	

	public Carrera() {
	}

	public Carrera(int crrId) {
		this.crrId = crrId;
	}

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "crr_id", unique = true, nullable = false)
	public int getCrrId() {
		return this.crrId;
	}

	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}

	@Column(name = "crr_descripcion", length = 128)
	@Length(max = 128)
	public String getCrrDescripcion() {
		return this.crrDescripcion;
	}

	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}

	@Column(name = "crr_cod_sniese", length = 32)
	@Length(max = 32)
	public String getCrrCodSniese() {
		return this.crrCodSniese;
	}

	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}


	@Column(name = "crr_nivel")
	public Integer getCrrNivel() {
		return crrNivel;
	}

	public void setCrrNivel(Integer crrNivel) {
		this.crrNivel = crrNivel;
	}

	@JoinColumn(name = "fcl_id", referencedColumnName = "fcl_id")
	@ManyToOne	
	public Facultad getCrrFacultad() {
		return crrFacultad;
	}

	public void setCrrFacultad(Facultad crrFacultad) {
		this.crrFacultad = crrFacultad;
	}

	@Column(name = "crr_detalle")
	public String getCrrDetalle() {
		return crrDetalle;
	}

	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}


	@Column(name = "crr_espe_codigo")
	public Integer getCrrEspeCodigo() {
		return crrEspeCodigo;
	}

	public void setCrrEspeCodigo(Integer crrEspeCodigo) {
		this.crrEspeCodigo = crrEspeCodigo;
	}

	
	@Column(name = "crr_tipo_evaluacion")
	public Integer getCrrTipoEvaluacion() {
		return crrTipoEvaluacion;
	}

	public void setCrrTipoEvaluacion(Integer crrTipoEvaluacion) {
		this.crrTipoEvaluacion = crrTipoEvaluacion;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("crrId: " + crrId);
    	sb.append(tabulador);
    	sb.append("crrDescripcion: " + (crrDescripcion==null? "NULL":crrDescripcion));  
    	sb.append(tabulador);
    	sb.append("crrCodSniese: " + (crrCodSniese==null? "NULL":crrCodSniese));
    	sb.append(tabulador);
    	sb.append(tabulador);
    	sb.append("crrNivel: " + (crrNivel==null? "NULL":crrNivel));
        return sb.toString();
	}
	
}
