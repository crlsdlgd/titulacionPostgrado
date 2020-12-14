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
   
 ARCHIVO:     FichaEstudiante.java	  
 DESCRIPCION: Entity Bean que representa a la tabla FichaEstudiante de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) FichaEstudiante.
 * Entity Bean que representa a la tabla FichaEstudiante de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "ficha_estudiante", schema = "titulacion_posgrado")
public class FichaEstudiante implements Serializable {

	private static final long serialVersionUID = 3985818892637114744L;
	private int fcesId;
	private Ubicacion fcesUbcCantonResidencia;
	private InstitucionAcademica fcesInacEstPrevios;
	private Persona fcesPersona;
	private Date fcesFechaInicio;
	private Date fcesFechaEgresamiento;
	private Date fcesFechaActaGrado;
	private String fcesNumActaGrado;
	private String fcesHoraActaGrado;
	private Date fcesFechaRefrendacion;
	private String fcesNumRefrendacion;
	private String fcesCrrEstudPrevios;
	private Integer fcesTiempoEstudRec;
	private Integer fcesTipoDuracRec;
	private Integer fcesTipoColegio;
	private String fcesTipoColegioSniese;
	
	private BigDecimal fcesNotaPromAcumulado;
	private BigDecimal fcesNotaTrabTitulacion;
	private BigDecimal fcesNotaFinalGraduacion;
	
	private String fcesLinkTesis;
	private Integer fcesRecEstudPrevios;
	private String fcesRecEstudPrevSniese;
	private Timestamp fcesFechaCreacion;
	private Integer fcesEstadoMigrado;
	private String fcesRegTTlSenescyt;
	
	private MecanismoCarrera fcesMecanismoCarrera;
	private ConfiguracionCarrera fcesConfiguracionCarrera;
	private TramiteTitulo fcesTramiteTitulo;
	
	private String fcesTituloBachiller;
	private Date fcesFechaInicioCohorte;
	private Date fcesFechaFinCohorte;
	
	public FichaEstudiante() {
	}

	public FichaEstudiante(Integer fcesId) {
		this.fcesId = fcesId;
	}

	public FichaEstudiante(int fcesId, Ubicacion fcesUbcCantonResidencia,
			InstitucionAcademica fcesInacEstPrevios, Persona fcesPersona,
			Date fcesFechaInicio, Date fcesFechaEgresamiento,
			Date fcesFechaActaGrado, String fcesNumActaGrado,
			Date fcesFechaRefrendacion, String fcesNumRefrendacion,
			String fcesCrrEstudPrevios, Integer fcesTiempoEstudRec,
			Integer fcesTipoDuracRec, Integer fcesTipoColegio,
			String fcesTipoColegioSniese, BigDecimal fcesNotaPromAcumulado,
			BigDecimal fcesNotaTrabTitulacion,
			BigDecimal fcesNotaFinalGraduacion, String fcesLinkTesis,
			Integer fcesRecEstudPrevios, String fcesRecEstudPrevSniese,
			Timestamp fcesFechaCreacion, String fcesHoraActaGrado,
			MecanismoCarrera fcesMecanismoCarrera,
			ConfiguracionCarrera fcesConfiguracionCarrera,
			TramiteTitulo fcesTramiteTitulo, String fcesTituloBachiller) {
		this.fcesId = fcesId;
		this.fcesUbcCantonResidencia = fcesUbcCantonResidencia;
		this.fcesInacEstPrevios = fcesInacEstPrevios;
		this.fcesPersona = fcesPersona;
		this.fcesFechaInicio = fcesFechaInicio;
		this.fcesFechaEgresamiento = fcesFechaEgresamiento;
		this.fcesFechaActaGrado = fcesFechaActaGrado;
		this.fcesNumActaGrado = fcesNumActaGrado;
		this.fcesFechaRefrendacion = fcesFechaRefrendacion;
		this.fcesNumRefrendacion = fcesNumRefrendacion;
		this.fcesCrrEstudPrevios = fcesCrrEstudPrevios;
		this.fcesTiempoEstudRec = fcesTiempoEstudRec;
		this.fcesTipoDuracRec = fcesTipoDuracRec;
		this.fcesTipoColegio = fcesTipoColegio;
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
		this.fcesNotaPromAcumulado = fcesNotaPromAcumulado;
		this.fcesNotaTrabTitulacion = fcesNotaTrabTitulacion;
		this.fcesLinkTesis = fcesLinkTesis;
		this.fcesRecEstudPrevios = fcesRecEstudPrevios;
		this.fcesRecEstudPrevSniese = fcesRecEstudPrevSniese;
		this.fcesFechaCreacion = fcesFechaCreacion;
		this.fcesMecanismoCarrera = fcesMecanismoCarrera;
		this.fcesConfiguracionCarrera = fcesConfiguracionCarrera;
		this.fcesTramiteTitulo = fcesTramiteTitulo;
		this.fcesTituloBachiller = fcesTituloBachiller;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "fces_id", unique = true, nullable = false)
	public int getFcesId() {
		return this.fcesId;
	}

	public void setFcesId(int fcesId) {
		this.fcesId = fcesId;
	}

	@JoinColumn(name = "ubc_canton_residencia", referencedColumnName = "ubc_id")
	@ManyToOne
	public Ubicacion getFcesUbcCantonResidencia() {
		return fcesUbcCantonResidencia;
	}

	public void setFcesUbcCantonResidencia(Ubicacion fcesUbcCantonResidencia) {
		this.fcesUbcCantonResidencia = fcesUbcCantonResidencia;
	}


	@JoinColumn(name = "inac_id_inst_est_previos", referencedColumnName = "inac_id")
	@ManyToOne
	public InstitucionAcademica getFcesInacEstPrevios() {
		return fcesInacEstPrevios;
	}

	public void setFcesInacEstPrevios(InstitucionAcademica fcesInacEstPrevios) {
		this.fcesInacEstPrevios = fcesInacEstPrevios;
	}

	@JoinColumn(name = "prs_id", referencedColumnName = "prs_id")
	@ManyToOne
	public Persona getFcesPersona() {
		return fcesPersona;
	}

	public void setFcesPersona(Persona fcesPersona) {
		this.fcesPersona = fcesPersona;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "fces_fecha_inicio", length = 13)
	public Date getFcesFechaInicio() {
		return this.fcesFechaInicio;
	}

	public void setFcesFechaInicio(Date fcesFechaInicio) {
		this.fcesFechaInicio = fcesFechaInicio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fces_fecha_egresamiento", length = 13)
	public Date getFcesFechaEgresamiento() {
		return this.fcesFechaEgresamiento;
	}

	public void setFcesFechaEgresamiento(Date fcesFechaEgresamiento) {
		this.fcesFechaEgresamiento = fcesFechaEgresamiento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fces_fecha_acta_grado", length = 13)
	public Date getFcesFechaActaGrado() {
		return this.fcesFechaActaGrado;
	}

	public void setFcesFechaActaGrado(Date fcesFechaActaGrado) {
		this.fcesFechaActaGrado = fcesFechaActaGrado;
	}

	@Column(name = "fces_num_acta_grado", length = 40)
	@Length(max = 40)
	public String getFcesNumActaGrado() {
		return this.fcesNumActaGrado;
	}

	public void setFcesNumActaGrado(String fcesNumActaGrado) {
		this.fcesNumActaGrado = fcesNumActaGrado;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fces_fecha_refrendacion", length = 13)
	public Date getFcesFechaRefrendacion() {
		return this.fcesFechaRefrendacion;
	}

	public void setFcesFechaRefrendacion(Date fcesFechaRefrendacion) {
		this.fcesFechaRefrendacion = fcesFechaRefrendacion;
	}

	@Column(name = "fces_num_refrendacion", length = 20)
	@Length(max = 20)
	public String getFcesNumRefrendacion() {
		return this.fcesNumRefrendacion;
	}

	public void setFcesNumRefrendacion(String fcesNumRefrendacion) {
		this.fcesNumRefrendacion = fcesNumRefrendacion;
	}


	@Column(name = "fces_crr_estud_previos", length = 512)
	@Length(max = 512)
	public String getFcesCrrEstudPrevios() {
		return this.fcesCrrEstudPrevios;
	}

	public void setFcesCrrEstudPrevios(String fcesCrrEstudPrevios) {
		this.fcesCrrEstudPrevios = fcesCrrEstudPrevios;
	}

	@Column(name = "fces_tiempo_estud_rec")
	public Integer getFcesTiempoEstudRec() {
		return this.fcesTiempoEstudRec;
	}

	public void setFcesTiempoEstudRec(Integer fcesTiempoEstudRec) {
		this.fcesTiempoEstudRec = fcesTiempoEstudRec;
	}

	@Column(name = "fces_tipo_durac_rec")
	public Integer getFcesTipoDuracRec() {
		return this.fcesTipoDuracRec;
	}

	public void setFcesTipoDuracRec(Integer fcesTipoDuracRec) {
		this.fcesTipoDuracRec = fcesTipoDuracRec;
	}

	
	@Column(name = "fces_tipo_colegio")
	public Integer getFcesTipoColegio() {
		return fcesTipoColegio;
	}

	public void setFcesTipoColegio(Integer fcesTipoColegio) {
		this.fcesTipoColegio = fcesTipoColegio;
	}

	@Column(name = "fces_tipo_colegio_sniese", length = 1)
	@Length(max = 1)
	public String getFcesTipoColegioSniese() {
		return fcesTipoColegioSniese;
	}

	public void setFcesTipoColegioSniese(String fcesTipoColegioSniese) {
		this.fcesTipoColegioSniese = fcesTipoColegioSniese;
	}

	@Column(name = "fces_nota_prom_acumulado", precision = 4)
	public BigDecimal getFcesNotaPromAcumulado() {
		return this.fcesNotaPromAcumulado;
	}

	public void setFcesNotaPromAcumulado(BigDecimal fcesNotaPromAcumulado) {
		this.fcesNotaPromAcumulado = fcesNotaPromAcumulado;
	}

	@Column(name = "fces_nota_trab_titulacion", precision = 4)
	public BigDecimal getFcesNotaTrabTitulacion() {
		return this.fcesNotaTrabTitulacion;
	}

	public void setFcesNotaTrabTitulacion(BigDecimal fcesNotaTrabTitulacion) {
		this.fcesNotaTrabTitulacion = fcesNotaTrabTitulacion;
	}
	

	@Column(name = "fces_link_tesis", length = 512)
	@Length(max = 512)
	public String getFcesLinkTesis() {
		return this.fcesLinkTesis;
	}

	public void setFcesLinkTesis(String fcesLinkTesis) {
		this.fcesLinkTesis = fcesLinkTesis;
	}

	@Column(name = "fces_rec_estud_previos")
	public Integer getFcesRecEstudPrevios() {
		return this.fcesRecEstudPrevios;
	}

	public void setFcesRecEstudPrevios(Integer fcesRecEstudPrevios) {
		this.fcesRecEstudPrevios = fcesRecEstudPrevios;
	}

	@Column(name = "fces_rec_estud_prev_sniese", length = 1)
	@Length(max = 1)
	public String getFcesRecEstudPrevSniese() {
		return this.fcesRecEstudPrevSniese;
	}

	public void setFcesRecEstudPrevSniese(String fcesRecEstudPrevSniese) {
		this.fcesRecEstudPrevSniese = fcesRecEstudPrevSniese;
	}

	@Column(name = "fces_fecha_creacion", length = 29)
	public Timestamp getFcesFechaCreacion() {
		return this.fcesFechaCreacion;
	}

	public void setFcesFechaCreacion(Timestamp fcesFechaCreacion) {
		this.fcesFechaCreacion = fcesFechaCreacion;
	}


	@JoinColumn(name = "mccr_id", referencedColumnName = "mccr_id")
	@ManyToOne
	public MecanismoCarrera getFcesMecanismoCarrera() {
		return fcesMecanismoCarrera;
	}

	public void setFcesMecanismoCarrera(
			MecanismoCarrera fcesMecanismoCarrera) {
		this.fcesMecanismoCarrera = fcesMecanismoCarrera;
	}
	
	
	@JoinColumn(name = "cncr_id", referencedColumnName = "cncr_id")
	@ManyToOne
	public ConfiguracionCarrera getFcesConfiguracionCarrera() {
		return fcesConfiguracionCarrera;
	}

	public void setFcesConfiguracionCarrera(
			ConfiguracionCarrera fcesConfiguracionCarrera) {
		this.fcesConfiguracionCarrera = fcesConfiguracionCarrera;
	}

	@JoinColumn(name = "trtt_id", referencedColumnName = "trtt_id")
	@ManyToOne
	public TramiteTitulo getFcesTramiteTitulo() {
		return fcesTramiteTitulo;
	}

	public void setFcesTramiteTitulo(TramiteTitulo fcesTramiteTitulo) {
		this.fcesTramiteTitulo = fcesTramiteTitulo;
	}
	
	@Column(name = "fces_titulo_bachiller", length = 256)
	public String getFcesTituloBachiller() {
		return fcesTituloBachiller;
	}

	public void setFcesTituloBachiller(String fcesTituloBachiller) {
		this.fcesTituloBachiller = fcesTituloBachiller;
	}

	
	@Column(name = "fces_nota_final_graduacion")
	public BigDecimal getFcesNotaFinalGraduacion() {
		return fcesNotaFinalGraduacion;
	}

	public void setFcesNotaFinalGraduacion(BigDecimal fcesNotaFinalGraduacion) {
		this.fcesNotaFinalGraduacion = fcesNotaFinalGraduacion;
	}

	@Column(name = "fces_estado_migrado")
	public Integer getFcesEstadoMigrado() {
		return fcesEstadoMigrado;
	}

	public void setFcesEstadoMigrado(Integer fcesEstadoMigrado) {
		this.fcesEstadoMigrado = fcesEstadoMigrado;
	}

	
	
	@Column(name = "fces_hora_acta_grado")
	public String getFcesHoraActaGrado() {
		return fcesHoraActaGrado;
	}

	public void setFcesHoraActaGrado(String fcesHoraActaGrado) {
		this.fcesHoraActaGrado = fcesHoraActaGrado;
	}

	@Column(name = "fces_reg_ttl_senescyt")
	public String getFcesRegTTlSenescyt() {
		return fcesRegTTlSenescyt;
	}

	public void setFcesRegTTlSenescyt(String fcesRegTTlSenescyt) {
		this.fcesRegTTlSenescyt = fcesRegTTlSenescyt;
	}

	
	@Temporal(TemporalType.DATE)
	@Column(name = "fces_fecha_inicio_cohorte", length = 13)
	public Date getFcesFechaInicioCohorte() {
		return fcesFechaInicioCohorte;
	}

	public void setFcesFechaInicioCohorte(Date fcesFechaInicioCohorte) {
		this.fcesFechaInicioCohorte = fcesFechaInicioCohorte;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fces_fecha_fin_cohorte", length = 13)
	public Date getFcesFechaFinCohorte() {
		return fcesFechaFinCohorte;
	}

	public void setFcesFechaFinCohorte(Date fcesFechaFinCohorte) {
		this.fcesFechaFinCohorte = fcesFechaFinCohorte;
	}

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("fcesId: " + fcesId);
    	sb.append(tabulador);
    	sb.append("fcesUbcCantonResidencia: " + (fcesUbcCantonResidencia==null? "NULL":fcesUbcCantonResidencia.getUbcId()));  
    	sb.append(tabulador);
    	sb.append("fcesInacEstPrevios: " + (fcesInacEstPrevios==null? "NULL":fcesInacEstPrevios.getInacId())); 
    	sb.append(tabulador);
    	sb.append("fcesPersona: " + (fcesPersona==null? "NULL":fcesPersona.getPrsId())); 
    	sb.append(tabulador);
    	sb.append("fcesFechaInicio: " + (fcesFechaInicio==null? "NULL":fcesFechaInicio)); 
    	sb.append(tabulador);
    	sb.append("fcesFechaEgresamiento: " + (fcesFechaEgresamiento==null? "NULL":fcesFechaEgresamiento)); 
    	sb.append(tabulador);
    	sb.append("fcesFechaActaGrado: " + (fcesFechaActaGrado==null? "NULL":fcesFechaActaGrado));
    	sb.append(tabulador);
    	sb.append("fcesNumActaGrado: " + (fcesNumActaGrado==null? "NULL":fcesNumActaGrado)); 
    	sb.append(tabulador);
    	sb.append("fcesFechaRefrendacion: " + (fcesFechaRefrendacion==null? "NULL":fcesFechaRefrendacion)); 
    	sb.append(tabulador);
    	sb.append("fcesNumRefrendacion: " + (fcesNumRefrendacion==null? "NULL":fcesNumRefrendacion)); 
    	sb.append(tabulador);
    	sb.append("fcesCrrEstudPrevios: " + (fcesCrrEstudPrevios==null? "NULL":fcesCrrEstudPrevios)); 
    	sb.append(tabulador);
    	sb.append("fcesTiempoEstudRec: " + (fcesTiempoEstudRec==null? "NULL":fcesTiempoEstudRec.intValue()));
    	sb.append(tabulador);
    	sb.append("fcesTipoDuracRec: " + (fcesTipoDuracRec==null? "NULL":fcesTipoDuracRec));
    	sb.append(tabulador);
    	sb.append("fcesTipoColegio: " + (fcesTipoColegio==null? "NULL":fcesTipoColegio));
    	sb.append(tabulador);
    	sb.append("fcesTipoColegioSniese: " + (fcesTipoColegioSniese==null? "NULL":fcesTipoColegioSniese));
    	sb.append(tabulador);
    	sb.append("fcesNotaPromAcumulado: " + (fcesNotaPromAcumulado==null? "NULL":fcesNotaPromAcumulado));
    	sb.append(tabulador);
    	sb.append("fcesNotaTrabTitulacion: " + (fcesNotaTrabTitulacion==null? "NULL":fcesNotaTrabTitulacion));
    	sb.append(tabulador);
    	sb.append("fcesLinkTesis: " + (fcesLinkTesis==null? "NULL":fcesLinkTesis));
    	sb.append(tabulador);
    	sb.append("fcesRecEstudPrevios: " + (fcesRecEstudPrevios==null? "NULL":fcesRecEstudPrevios.intValue()));
    	sb.append(tabulador);
    	sb.append("fcesRecEstudPrevSniese: " + (fcesRecEstudPrevSniese==null? "NULL":fcesRecEstudPrevSniese));
    	sb.append(tabulador);
    	sb.append("fcesFechaCreacion: " + (fcesFechaCreacion==null? "NULL":fcesFechaCreacion));
    	sb.append(tabulador);
    	sb.append("fcesMecanismoCarrera: " + (fcesMecanismoCarrera==null? "NULL":fcesMecanismoCarrera.getMccrId()));
        return sb.toString();
	}
}
