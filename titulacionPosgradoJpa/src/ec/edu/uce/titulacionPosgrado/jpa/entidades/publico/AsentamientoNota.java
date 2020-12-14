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
   
 ARCHIVO:     AsentamientoNota.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Asentamiento_Nota de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 17-AGO-2016		 Gabriel Mafla  			          Emisión Inicial
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
 * Clase (Entity Bean) AsentamientoNota.
 * Entity Bean que representa a la tabla AsentamientoNota de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "asentamiento_nota", schema = "titulacion_posgrado")
public class AsentamientoNota implements Serializable {

	private static final long serialVersionUID = 4332700057890825008L;
	
	private int asnoId;
	private BigDecimal asnoComplexivoTeorico;
	private BigDecimal asnoComplexivoPractico; 
	private Timestamp  asnoFechaCmpTeorico;
	private BigDecimal asnoComplexivoFinal;
	private BigDecimal asnoCmpGraciaTeorico;
	private Timestamp asnoFechaCmpGraciaTeorico;
	private BigDecimal asnoCmpGraciaPractico;
	private Timestamp asnoFechaCmpGraciaPractico;
	private BigDecimal asnoCmpGraciaFinal;
	private BigDecimal asnoTrbLector1;
	private BigDecimal asnoTrbLector2;
	private BigDecimal asnoTrbLector3;
	private BigDecimal asnoPrmTrbEscrito;
	private Timestamp asnoFechaTrbEscrito;
	private BigDecimal asnoDfnOral1;
	private BigDecimal asnoDfnOral2;
	private BigDecimal asnoDfnOral3;
	private BigDecimal asnoPrmDfnOral;
	private BigDecimal asnoTrbTitulacionFinal;
	private Timestamp asnoFechaDfnOral;
	  
	private ProcesoTramite asnoProcesoTramite;
	
	
	public AsentamientoNota() {
	}

	public AsentamientoNota(int asnoId) {
		this.asnoId = asnoId;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "asno_id", unique = true, nullable = false)
	public int getAsnoId() {
		return asnoId;
	}

	public void setAsnoId(int asnoId) {
		this.asnoId = asnoId;
	}

	@Column(name = "asno_complexivo_teorico", precision = 3)
	public BigDecimal getAsnoComplexivoTeorico() {
		return asnoComplexivoTeorico;
	}

	public void setAsnoComplexivoTeorico(BigDecimal asnoComplexivoTeorico) {
		this.asnoComplexivoTeorico = asnoComplexivoTeorico;
	}

	@Column(name = "asno_complexivo_practico", precision = 3)
	public BigDecimal getAsnoComplexivoPractico() {
		return asnoComplexivoPractico;
	}

	public void setAsnoComplexivoPractico(BigDecimal asnoComplexivoPractico) {
		this.asnoComplexivoPractico = asnoComplexivoPractico;
	}

	@Column(name = "asno_fecha_cmp_teorico", length = 29)
	public Timestamp getAsnoFechaCmpTeorico() {
		return asnoFechaCmpTeorico;
	}

	public void setAsnoFechaCmpTeorico(Timestamp asnoFechaCmpTeorico) {
		this.asnoFechaCmpTeorico = asnoFechaCmpTeorico;
	}

	@Column(name = "asno_complexivo_final", precision = 3)
	public BigDecimal getAsnoComplexivoFinal() {
		return asnoComplexivoFinal;
	}

	public void setAsnoComplexivoFinal(BigDecimal asnoComplexivoFinal) {
		this.asnoComplexivoFinal = asnoComplexivoFinal;
	}

	@Column(name = "asno_cmp_gracia_teorico", precision = 3)
	public BigDecimal getAsnoCmpGraciaTeorico() {
		return asnoCmpGraciaTeorico;
	}

	public void setAsnoCmpGraciaTeorico(BigDecimal asnoCmpGraciaTeorico) {
		this.asnoCmpGraciaTeorico = asnoCmpGraciaTeorico;
	}

	@Column(name = "asno_fecha_cmp_gracia_teorico", length = 29)
	public Timestamp getAsnoFechaCmpGraciaTeorico() {
		return asnoFechaCmpGraciaTeorico;
	}

	public void setAsnoFechaCmpGraciaTeorico(Timestamp asnoFechaCmpGraciaTeorico) {
		this.asnoFechaCmpGraciaTeorico = asnoFechaCmpGraciaTeorico;
	}

	@Column(name = "asno_cmp_gracia_practico", precision = 3)
	public BigDecimal getAsnoCmpGraciaPractico() {
		return asnoCmpGraciaPractico;
	}

	public void setAsnoCmpGraciaPractico(BigDecimal asnoCmpGraciaPractico) {
		this.asnoCmpGraciaPractico = asnoCmpGraciaPractico;
	}

	@Column(name = "asno_fecha_cmp_gracia_practico", length = 29)
	public Timestamp getAsnoFechaCmpGraciaPractico() {
		return asnoFechaCmpGraciaPractico;
	}

	public void setAsnoFechaCmpGraciaPractico(Timestamp asnoFechaCmpGraciaPractico) {
		this.asnoFechaCmpGraciaPractico = asnoFechaCmpGraciaPractico;
	}

	@Column(name = "asno_cmp_gracia_final", precision = 3)
	public BigDecimal getAsnoCmpGraciaFinal() {
		return asnoCmpGraciaFinal;
	}

	
	public void setAsnoCmpGraciaFinal(BigDecimal asnoCmpGraciaFinal) {
		this.asnoCmpGraciaFinal = asnoCmpGraciaFinal;
	}

	@Column(name = "asno_trb_lector1", precision = 3)
	public BigDecimal getAsnoTrbLector1() {
		return asnoTrbLector1;
	}

	public void setAsnoTrbLector1(BigDecimal asnoTrbLector1) {
		this.asnoTrbLector1 = asnoTrbLector1;
	}

	@Column(name = "asno_trb_lector2", precision = 3)
	public BigDecimal getAsnoTrbLector2() {
		return asnoTrbLector2;
	}

	public void setAsnoTrbLector2(BigDecimal asnoTrbLector2) {
		this.asnoTrbLector2 = asnoTrbLector2;
	}

	@Column(name = "asno_trb_lector3", precision = 3)
	public BigDecimal getAsnoTrbLector3() {
		return asnoTrbLector3;
	}

	public void setAsnoTrbLector3(BigDecimal asnoTrbLector3) {
		this.asnoTrbLector3 = asnoTrbLector3;
	}

	@Column(name = "asno_prm_trb_escrito", precision = 3)
	public BigDecimal getAsnoPrmTrbEscrito() {
		return asnoPrmTrbEscrito;
	}

	public void setAsnoPrmTrbEscrito(BigDecimal asnoPrmTrbEscrito) {
		this.asnoPrmTrbEscrito = asnoPrmTrbEscrito;
	}

	@Column(name = "asno_fecha_trb_escrito", length = 29)
	public Timestamp getAsnoFechaTrbEscrito() {
		return asnoFechaTrbEscrito;
	}

	public void setAsnoFechaTrbEscrito(Timestamp asnoFechaTrbEscrito) {
		this.asnoFechaTrbEscrito = asnoFechaTrbEscrito;
	}

	@Column(name = "asno_dfn_oral1", precision = 3)
	public BigDecimal getAsnoDfnOral1() {
		return asnoDfnOral1;
	}

	public void setAsnoDfnOral1(BigDecimal asnoDfnOral1) {
		this.asnoDfnOral1 = asnoDfnOral1;
	}

	@Column(name = "asno_dfn_oral2", precision = 3)
	public BigDecimal getAsnoDfnOral2() {
		return asnoDfnOral2;
	}

	public void setAsnoDfnOral2(BigDecimal asnoDfnOral2) {
		this.asnoDfnOral2 = asnoDfnOral2;
	}

	@Column(name = "asno_dfn_oral3", precision = 3)
	public BigDecimal getAsnoDfnOral3() {
		return asnoDfnOral3;
	}

	public void setAsnoDfnOral3(BigDecimal asnoDfnOral3) {
		this.asnoDfnOral3 = asnoDfnOral3;
	}

	@Column(name = "asno_prm_dfn_oral", precision = 3)
	public BigDecimal getAsnoPrmDfnOral() {
		return asnoPrmDfnOral;
	}

	public void setAsnoPrmDfnOral(BigDecimal asnoPrmDfnOral) {
		this.asnoPrmDfnOral = asnoPrmDfnOral;
	}

	@Column(name = "asno_trb_titulacion_final", precision = 3)
	public BigDecimal getAsnoTrbTitulacionFinal() {
		return asnoTrbTitulacionFinal;
	}

	public void setAsnoTrbTitulacionFinal(BigDecimal asnoTrbTitulacionFinal) {
		this.asnoTrbTitulacionFinal = asnoTrbTitulacionFinal;
	}

	@Column(name = "asno_fecha_dfn_oral", length = 29)
	public Timestamp getAsnoFechaDfnOral() {
		return asnoFechaDfnOral;
	}

	public void setAsnoFechaDfnOral(Timestamp asnoFechaDfnOral) {
		this.asnoFechaDfnOral = asnoFechaDfnOral;
	}

	@JoinColumn(name = "prtr_id", referencedColumnName = "prtr_id")
	@ManyToOne
	public ProcesoTramite getAsnoProcesoTramite() {
		return asnoProcesoTramite;
	}

	public void setAsnoProcesoTramite(ProcesoTramite asnoProcesoTramite) {
		this.asnoProcesoTramite = asnoProcesoTramite;
	}


	
	
	
	

//	@Override
//	public String toString() {
//		String tabulador = "\t";
//    	StringBuilder sb = new StringBuilder();
//    	sb.append("asnoId: " + asnoId);
//     	sb.append(tabulador);
//    	sb.append("asnoCulminoMalla: " + (asnoCulminoMalla==null? "NULL":asnoCulminoMalla));
//    	sb.append(tabulador);
//    	sb.append("asnoReproboComplexivo: " + (asnoReproboComplexivo==null? "NULL":asnoReproboComplexivo));
//    	sb.append(tabulador);
//    	sb.append("asnoAsignadoTutor: " + (asnoAsignadoTutor==null? "NULL":asnoAsignadoTutor));
//    	sb.append(tabulador);
//    	sb.append("asnoRslHomologacion: " + (asnoRslHomologacion==null? "NULL":asnoRslHomologacion)); 
//    	sb.append(tabulador);
//    	sb.append("asnoRslExamenComplexivo: " + (asnoRslExamenComplexivo==null? "NULL":asnoRslExamenComplexivo));
//    	sb.append(tabulador);
//    	sb.append("asnoRslActConocimiento: " + (asnoRslActConocimiento==null? "NULL":asnoRslActConocimiento)); 
//    	sb.append(tabulador);
//    	sb.append("asnoRslTieneGratuidad: " + (asnoRslGratuidad==null? "NULL":asnoRslGratuidad)); 
//    	sb.append(tabulador);
//    	sb.append("asnoRslIdoneidad: " + (asnoRslIdoneidad==null? "NULL":asnoRslIdoneidad));
//    	sb.append(tabulador);
//    	sb.append("asnoRslOtrosMecanismos: " + (asnoRslOtrosMecanismos==null? "NULL":asnoRslOtrosMecanismos));
//    	sb.append(tabulador);
//    	sb.append("asnoRslSlcMecanismo: " + (asnoRslSlcMecanismo==null? "NULL":asnoRslSlcMecanismo));
//    	sb.append(tabulador);
//    	sb.append("asnoProcesoTramite: " + (asnoProcesoTramite==null? "NULL":asnoProcesoTramite.getPrtrId()));
//        return sb.toString();
//	}


}
