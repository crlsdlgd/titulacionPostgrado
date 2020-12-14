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
   
 ARCHIVO:     InstitucionAcademica.java	  
 DESCRIPCION: Entity Bean que representa a la tabla InstitucionAcademica de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		   		Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) InstitucionAcademica.
 * Entity Bean que representa a la tabla InstitucionAcademica de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "institucion_academica", schema = "titulacion_posgrado")
public class InstitucionAcademica implements Serializable {

	private static final long serialVersionUID = -7876679286124765519L;
	private int inacId;
	private Ubicacion inacUbicacion;
	private String inacCodigoSniese;
	private String inacDescripcion;
	private Integer inacNivel;
	private Integer inacTipo;
	private String inacTipoSniese;
	private List<FichaEstudiante> inacFcEsEstPrevios;

	public InstitucionAcademica() {
	}

	public InstitucionAcademica(int inacId) {
		this.inacId = inacId;
	}

	public InstitucionAcademica(int inacId, Ubicacion inacUbicacion,
			String inacCodigoSniese, String inacDescripcion, Integer inacNivel,
			Integer inacTipo, String inacTipoSniese,
			List<FichaEstudiante> inacFcEsEstPrevios) {
		this.inacId = inacId;
		this.inacUbicacion = inacUbicacion;
		this.inacCodigoSniese = inacCodigoSniese;
		this.inacDescripcion = inacDescripcion;
		this.inacNivel = inacNivel;
		this.inacTipo = inacTipo;
		this.inacTipoSniese = inacTipoSniese;
		this.inacFcEsEstPrevios = inacFcEsEstPrevios;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "inac_id", unique = true, nullable = false)
	public int getInacId() {
		return this.inacId;
	}

	public void setInacId(int inacId) {
		this.inacId = inacId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ubc_id")
	public Ubicacion getInacUbicacion() {
		return inacUbicacion;
	}

	public void setInacUbicacion(Ubicacion inacUbicacion) {
		this.inacUbicacion = inacUbicacion;
	}
	
	
	@Column(name = "inac_codigo_sniese", length = 6)
	@Length(max = 6)
	public String getInacCodigoSniese() {
		return this.inacCodigoSniese;
	}

	public void setInacCodigoSniese(String inacCodigoSniese) {
		this.inacCodigoSniese = inacCodigoSniese;
	}

	@Column(name = "inac_descripcion", length = 512)
	@Length(max = 512)
	public String getInacDescripcion() {
		return this.inacDescripcion;
	}

	public void setInacDescripcion(String inacDescripcion) {
		this.inacDescripcion = inacDescripcion;
	}

	@Column(name = "inac_nivel")
	public Integer getInacNivel() {
		return this.inacNivel;
	}

	public void setInacNivel(Integer inacNivel) {
		this.inacNivel = inacNivel;
	}

	@Column(name = "inac_tipo")
	public Integer getInacTipo() {
		return this.inacTipo;
	}

	public void setInacTipo(Integer inacTipo) {
		this.inacTipo = inacTipo;
	}

	@Column(name = "inac_tipo_sniese", length = 4)
	@Length(max = 4)
	public String getInacTipoSniese() {
		return this.inacTipoSniese;
	}

	public void setInacTipoSniese(String inacTipoSniese) {
		this.inacTipoSniese = inacTipoSniese;
	}


	@OneToMany(mappedBy = "fcesInacEstPrevios")
	public List<FichaEstudiante> getInacFcEsEstPrevios() {
		return inacFcEsEstPrevios;
	}

	public void setInacFcEsEstPrevios(List<FichaEstudiante> inacFcEsEstPrevios) {
		this.inacFcEsEstPrevios = inacFcEsEstPrevios;
	}

	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("inacId: " + inacId);
    	sb.append(tabulador);
    	sb.append("inacUbicacion: " + (inacUbicacion==null? "NULL":inacUbicacion.getUbcId()));  
    	sb.append(tabulador);
    	sb.append("inacCodigoSniese: " + (inacCodigoSniese==null? "NULL":inacCodigoSniese));  
    	sb.append(tabulador);
    	sb.append("inacDescripcion: " + (inacDescripcion==null? "NULL":inacDescripcion));  
    	sb.append(tabulador);
    	sb.append("inacNivel: " + (inacNivel==null? "NULL":inacNivel.intValue()));  
    	sb.append(tabulador);
    	sb.append("inacTipo: " + (inacTipo==null? "NULL":inacTipo.intValue()));  
    	sb.append(tabulador);
    	sb.append("inacTipoSniese: " + (inacTipoSniese==null? "NULL":inacTipoSniese));  
        return sb.toString();
	}
}
