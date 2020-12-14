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
   
 ARCHIVO:     Ubicacion.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Ubicacion de la BD. 
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) Ubicacion.
 * Entity Bean que representa a la tabla Ubicacion de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "ubicacion", schema = "titulacion_posgrado")
public class Ubicacion implements  Serializable {

	private static final long serialVersionUID = 232130888039125826L;
	private int ubcId;
	private String ubcDescripcion;
	private Integer ubcJerarquia;
	private String ubcGentilicio;
	private String ubcCodSniese;
	private Ubicacion ubcPadre;
	private List<ConfiguracionCarrera> ubcConfiguracionCarreras;
	private List<Ubicacion> ubcUbicaciones;
	private List<Persona> ubcPersonaNacionalidad;
	private List<InstitucionAcademica> ubcInstitucionesAcademicas;
	private List<FichaEstudiante> ubcResidencia;

	public Ubicacion() {
	}

	public Ubicacion(int ubcId) {
		this.ubcId = ubcId;
	}

	public Ubicacion(int ubcId, String ubcDescripcion, Integer ubcJerarquia,
			String ubcGentilicio, String ubcCodSniese, Ubicacion ubcPadre,
			List<ConfiguracionCarrera> ubcConfiguracionCarreras,
			List<Ubicacion> ubcUbicaciones,
			List<Persona> ubcPersonaNacionalidad,
			List<InstitucionAcademica> ubcInstitucionesAcademicas,
			List<FichaEstudiante> ubcResidencia) {
		this.ubcId = ubcId;
		this.ubcDescripcion = ubcDescripcion;
		this.ubcJerarquia = ubcJerarquia;
		this.ubcGentilicio = ubcGentilicio;
		this.ubcCodSniese = ubcCodSniese;
		this.ubcPadre = ubcPadre;
		this.ubcConfiguracionCarreras = ubcConfiguracionCarreras;
		this.ubcUbicaciones = ubcUbicaciones;
		this.ubcPersonaNacionalidad = ubcPersonaNacionalidad;
		this.ubcInstitucionesAcademicas = ubcInstitucionesAcademicas;
		this.ubcResidencia = ubcResidencia;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ubc_id", unique = true, nullable = false)
	public int getUbcId() {
		return this.ubcId;
	}

	public void setUbcId(int ubcId) {
		this.ubcId = ubcId;
	}

	
	@JoinColumn(name = "ubc_padre", referencedColumnName = "ubc_id")
	@ManyToOne
	public Ubicacion getUbcPadre() {
		return ubcPadre;
	}

	public void setUbcPadre(Ubicacion ubcPadre) {
		this.ubcPadre = ubcPadre;
	}
	
	@Column(name = "ubc_descripcion", length = 128)
	@Length(max = 128)
	public String getUbcDescripcion() {
		return this.ubcDescripcion;
	}



	public void setUbcDescripcion(String ubcDescripcion) {
		this.ubcDescripcion = ubcDescripcion;
	}

	@Column(name = "ubc_jerarquia")
	public Integer getUbcJerarquia() {
		return this.ubcJerarquia;
	}

	public void setUbcJerarquia(Integer ubcJerarquia) {
		this.ubcJerarquia = ubcJerarquia;
	}

	@Column(name = "ubc_gentilicio", length = 128)
	@Length(max = 128)
	public String getUbcGentilicio() {
		return this.ubcGentilicio;
	}

	public void setUbcGentilicio(String ubcGentilicio) {
		this.ubcGentilicio = ubcGentilicio;
	}

	@Column(name = "ubc_cod_sniese", length = 32)
	@Length(max = 32)
	public String getUbcCodSniese() {
		return this.ubcCodSniese;
	}

	public void setUbcCodSniese(String ubcCodSniese) {
		this.ubcCodSniese = ubcCodSniese;
	}

	@OneToMany(mappedBy = "cncrUbicacion")
	public List<ConfiguracionCarrera> getUbcConfiguracionCarreras() {
		return ubcConfiguracionCarreras;
	}

	public void setUbcConfiguracionCarreras(
			List<ConfiguracionCarrera> ubcConfiguracionCarreras) {
		this.ubcConfiguracionCarreras = ubcConfiguracionCarreras;
	}

	@OneToMany(mappedBy = "ubcPadre")
	public List<Ubicacion> getUbcUbicaciones() {
		return ubcUbicaciones;
	}

	public void setUbcUbicaciones(List<Ubicacion> ubcUbicaciones) {
		this.ubcUbicaciones = ubcUbicaciones;
	}


	@OneToMany(mappedBy = "prsUbicacionNacionalidad")
	public List<Persona> getUbcPersonaNacionalidad() {
		return ubcPersonaNacionalidad;
	}

	public void setUbcPersonaNacionalidad(List<Persona> ubcPersonaNacionalidad) {
		this.ubcPersonaNacionalidad = ubcPersonaNacionalidad;
	}
	

	@OneToMany(mappedBy = "inacUbicacion")
	public List<InstitucionAcademica> getUbcInstitucionesAcademicas() {
		return ubcInstitucionesAcademicas;
	}


	public void setUbcInstitucionesAcademicas(
			List<InstitucionAcademica> ubcInstitucionesAcademicas) {
		this.ubcInstitucionesAcademicas = ubcInstitucionesAcademicas;
	}

	
	@OneToMany(mappedBy = "fcesUbcCantonResidencia")
	public List<FichaEstudiante> getUbcResidencia() {
		return ubcResidencia;
	}

	public void setUbcResidencia(List<FichaEstudiante> ubcResidencia) {
		this.ubcResidencia = ubcResidencia;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("ubcId: " + ubcId);
    	sb.append(tabulador);
    	sb.append("ubcDescripcion: " + (ubcDescripcion==null? "NULL":ubcDescripcion));  
    	sb.append(tabulador);
    	sb.append("ubcJerarquia: " + (ubcJerarquia==null? "NULL":ubcJerarquia.intValue()));  
    	sb.append(tabulador);
    	sb.append("ubcGentilicio: " + (ubcGentilicio==null? "NULL":ubcGentilicio));  
    	sb.append(tabulador);
    	sb.append("ubcCodSniese: " + (ubcCodSniese==null? "NULL":ubcCodSniese));  
    	sb.append(tabulador);
    	sb.append("ubcPadre: " + (ubcPadre==null? "NULL":ubcPadre.getUbcId()));  
        return sb.toString();
	}

}
