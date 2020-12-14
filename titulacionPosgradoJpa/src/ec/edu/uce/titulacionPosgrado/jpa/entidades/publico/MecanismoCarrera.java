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
   
 ARCHIVO:     MecanismoTitulacionCarrera.java	  
 DESCRIPCION: Entity Bean que representa a la tabla MecanismoTitulacionCarrera de la BD. 
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

/**
 * Clase (Entity Bean) MecanismoTitulacionCarrera.
 * Entity Bean que representa a la tabla MecanismoTitulacionCarrera de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "mecanismo_carrera", schema = "titulacion_posgrado")
public class MecanismoCarrera implements Serializable {

	private static final long serialVersionUID = -6957606655471654353L;
	
	private int mccrId;
	private Integer mccrEstado;
	private Integer mccrPorcentaje;
	private MecanismoTitulacion mccrMecanismoTitulacion;
	private Carrera mccrCarrera;
	private List<FichaEstudiante> mccrFichaEstudiantes;

	public MecanismoCarrera() {
	}

	public MecanismoCarrera(Integer mccrId) {
		this.mccrId = mccrId;
	}


	public MecanismoCarrera(int mccrId, Integer mccrEstado,
			Integer mccrPorcentaje,
			MecanismoTitulacion mccrMecanismoTitulacion,
			Carrera mccrCarrera, List<FichaEstudiante> mccrFichaEstudiantes) {
		this.mccrId = mccrId;
		this.mccrEstado = mccrEstado;
		this.mccrPorcentaje = mccrPorcentaje;
		this.mccrMecanismoTitulacion = mccrMecanismoTitulacion;
		this.mccrCarrera = mccrCarrera;
		this.mccrFichaEstudiantes = mccrFichaEstudiantes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mccr_id", unique = true, nullable = false)
	public int getMccrId() {
		return mccrId;
	}

	public void setMccrId(int mccrId) {
		this.mccrId = mccrId;
	}

	@Column(name = "mccr_estado")
	public Integer getMccrEstado() {
		return mccrEstado;
	}

	public void setMccrEstado(Integer mccrEstado) {
		this.mccrEstado = mccrEstado;
	}

	@Column(name = "mccr_porcentaje")
	public Integer getMccrPorcentaje() {
		return mccrPorcentaje;
	}

	public void setMccrPorcentaje(Integer mccrPorcentaje) {
		this.mccrPorcentaje = mccrPorcentaje;
	}
	
	@JoinColumn(name = "mctt_id", referencedColumnName = "mctt_id")
	@ManyToOne
	public MecanismoTitulacion getMccrMecanismoTitulacion() {
		return mccrMecanismoTitulacion;
	}

	public void setMccrMecanismoTitulacion(
			MecanismoTitulacion mccrMecanismoTitulacion) {
		this.mccrMecanismoTitulacion = mccrMecanismoTitulacion;
	}

	@JoinColumn(name = "crr_id", referencedColumnName = "crr_id")
	@ManyToOne
	public Carrera getMccrCarrera() {
		return mccrCarrera;
	}

	public void setMccrCarrera(Carrera mccrCarrera) {
		this.mccrCarrera = mccrCarrera;
	}

	@OneToMany(mappedBy = "fcesMecanismoCarrera")
	public List<FichaEstudiante> getMccrFichaEstudiantes() {
		return mccrFichaEstudiantes;
	}

	public void setMccrFichaEstudiantes(
			List<FichaEstudiante> mccrFichaEstudiantes) {
		this.mccrFichaEstudiantes = mccrFichaEstudiantes;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("mccrId: " + mccrId);
    	sb.append(tabulador);
    	sb.append("mccrEstado: " + (mccrEstado==null? "NULL":mccrEstado));  
    	sb.append(tabulador);
    	sb.append("mccrPorcentaje: " + (mccrPorcentaje==null? "NULL":mccrPorcentaje));  
    	sb.append(tabulador);
    	sb.append("mccrMecanismoTitulacion: " + (mccrMecanismoTitulacion.getMcttId()));
        return sb.toString();
	}
	
	
	
	
//	public int getMcttId() {
//		return this.mcttId;
//	}
//
//	public void setMcttId(int mcttId) {
//		this.mcttId = mcttId;
//	}
//
//	@Column(name = "mctt_codigo_sniese", length = 1)
//	@Length(max = 1)
//	public String getMcttCodigoSniese() {
//		return this.mcttCodigoSniese;
//	}
//
//	public void setMcttCodigoSniese(String mcttCodigoSniese) {
//		this.mcttCodigoSniese = mcttCodigoSniese;
//	}
//
//	@Column(name = "mctt_descripcion", length = 20)
//	@Length(max = 20)
//	public String getMcttDescripcion() {
//		return this.mcttDescripcion;
//	}
//
//	public void setMcttDescripcion(String mcttDescripcion) {
//		this.mcttDescripcion = mcttDescripcion;
//	}
//
//	
//	@Column(name = "mctt_estado")
//	public Integer getMcttEstado() {
//		return mcttEstado;
//	}
//
//	public void setMcttEstado(Integer mcttEstado) {
//		this.mcttEstado = mcttEstado;
//	}
//
//	@OneToMany(mappedBy = "fcesMecanismoTitulacion")
//	public List<FichaEstudiante> getMcttFichaEstudiantes() {
//		return mcttFichaEstudiantes;
//	}
//
//	public void setMcttFichaEstudiantes(List<FichaEstudiante> mcttFichaEstudiantes) {
//		this.mcttFichaEstudiantes = mcttFichaEstudiantes;
//	}
//

}
