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
   
 ARCHIVO:     RegimenAcademico.java	  
 DESCRIPCION: Entity Bean que representa a la tabla RegimenAcademico de la BD. 
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) RegimenAcademico.
 * Entity Bean que representa a la tabla RegimenAcademico de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "regimen_academico", schema = "titulacion_posgrado")
public class RegimenAcademico implements  Serializable {

	private static final long serialVersionUID = 5465319519308185213L;
	private int rgacId;
	private String rgacDescripcion;
	private Integer rgacEstado;
	private List<NivelFormacion> rgacNivelFormaciones;
	private List<TipoSede> rgacTipoSedes;

	public RegimenAcademico() {
	}

	public RegimenAcademico(int rgacId) {
		this.rgacId = rgacId;
	}

	public RegimenAcademico(int rgacId, String rgacDescripcion,
			Integer rgacEstado, List<NivelFormacion> rgacNivelFormaciones,
			List<TipoSede> rgacTipoSedes) {
		this.rgacId = rgacId;
		this.rgacDescripcion = rgacDescripcion;
		this.rgacEstado = rgacEstado;
		this.rgacNivelFormaciones = rgacNivelFormaciones;
		this.rgacTipoSedes = rgacTipoSedes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "rgac_id", unique = true, nullable = false)
	public int getRgacId() {
		return this.rgacId;
	}

	public void setRgacId(int rgacId) {
		this.rgacId = rgacId;
	}

	@Column(name = "rgac_descripcion", length = 32)
	@Length(max = 32)
	public String getRgacDescripcion() {
		return this.rgacDescripcion;
	}

	public void setRgacDescripcion(String rgacDescripcion) {
		this.rgacDescripcion = rgacDescripcion;
	}

	@Column(name = "rgac_estado")
	public Integer getRgacEstado() {
		return this.rgacEstado;
	}

	public void setRgacEstado(Integer rgacEstado) {
		this.rgacEstado = rgacEstado;
	}


	@OneToMany(mappedBy = "nvfrRegimenAcademico")
	public List<NivelFormacion> getRgacNivelFormaciones() {
		return rgacNivelFormaciones;
	}

	public void setRgacNivelFormaciones(List<NivelFormacion> rgacNivelFormaciones) {
		this.rgacNivelFormaciones = rgacNivelFormaciones;
	}

	@OneToMany(mappedBy = "tiseRegimenAcademico")
	public List<TipoSede> getRgacTipoSedes() {
		return rgacTipoSedes;
	}

	public void setRgacTipoSedes(List<TipoSede> rgacTipoSedes) {
		this.rgacTipoSedes = rgacTipoSedes;
	}
	
	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("rgacId: " + rgacId);
    	sb.append(tabulador);
    	sb.append("rgacDescripcion: " + (rgacDescripcion==null? "NULL":rgacDescripcion));  
    	sb.append(tabulador);
    	sb.append("rgacEstado: " + (rgacEstado==null? "NULL":rgacEstado.intValue()));  
        return sb.toString();
	}
}
