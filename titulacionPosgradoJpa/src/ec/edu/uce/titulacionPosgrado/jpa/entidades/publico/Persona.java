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
   
 ARCHIVO:     Persona.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Persona de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018		   		Daniel Albuja  			          Emisión Inicia
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) Persona.
 * Entity Bean que representa a la tabla Persona de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "persona", schema = "titulacion_posgrado")
public class Persona implements Serializable {

	private static final long serialVersionUID = 7408068776822746344L;
	private int prsId;
	private Integer prsTipoIdentificacion;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsNombres;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private String prsCelular;
	private Date prsFechaNacimiento;
	private Integer prsSexo;
	private Integer prsSexoSniese;
	private String prsSegundoApellido;
	private Etnia prsEtnia;
	private Ubicacion prsUbicacionNacionalidad;
	private Integer prsDiscapacidad;
	private Integer prsTipoDiscapacidad;
	private Integer prsPocentajeDiscapacidad;
	private String prsCarnetConadis;
	private String prsUbicacionFoto;
	private List<Usuario> prsUsuarios;
	private List<FichaEstudiante> prsFichaEstudiantes;
	
	public Persona() {
	}

	public Persona(int prsId) {
		this.prsId = prsId;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "prs_id", unique = true, nullable = false)
	public int getPrsId() {
		return this.prsId;
	}

	public void setPrsId(int prsId) {
		this.prsId = prsId;
	}

	@Column(name = "prs_tipo_identificacion")
	public Integer getPrsTipoIdentificacion() {
		return this.prsTipoIdentificacion;
	}

	public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
		this.prsTipoIdentificacion = prsTipoIdentificacion;
	}

	@Column(name = "prs_tipo_identificacion_sniese")
	public Integer getPrsTipoIdentificacionSniese() {
		return this.prsTipoIdentificacionSniese;
	}

	public void setPrsTipoIdentificacionSniese(
			Integer prsTipoIdentificacionSniese) {
		this.prsTipoIdentificacionSniese = prsTipoIdentificacionSniese;
	}

	@Column(name = "prs_identificacion", length = 13)
	@Length(max = 13)
	public String getPrsIdentificacion() {
		return this.prsIdentificacion;
	}

	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}

	@Column(name = "prs_primer_apellido", length = 32)
	@Length(max = 32)
	public String getPrsPrimerApellido() {
		return this.prsPrimerApellido;
	}

	public void setPrsPrimerApellido(String prsPrimerApellido) {
		this.prsPrimerApellido = prsPrimerApellido;
	}

	@Column(name = "prs_nombres", length = 50)
	@Length(max = 50)
	public String getPrsNombres() {
		return this.prsNombres;
	}

	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}

	@Column(name = "prs_mail_personal", length = 50)
	@Length(max = 50)
	@Email
	public String getPrsMailPersonal() {
		return this.prsMailPersonal;
	}

	public void setPrsMailPersonal(String prsMailPersonal) {
		this.prsMailPersonal = prsMailPersonal;
	}

	@Column(name = "prs_mail_institucional", length = 50)
	@Length(max = 50)
	@Email
	public String getPrsMailInstitucional() {
		return this.prsMailInstitucional;
	}

	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
	}

	@Column(name = "prs_telefono", length = 10)
	@Length(max = 10)
	public String getPrsTelefono() {
		return this.prsTelefono;
	}

	public void setPrsTelefono(String prsTelefono) {
		this.prsTelefono = prsTelefono;
	}

	@Column(name = "prs_celular", length = 13)
	@Length(max = 13)
	public String getPrsCelular() {
		return this.prsCelular;
	}

	public void setPrsCelular(String prsCelular) {
		this.prsCelular = prsCelular;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "prs_fecha_nacimiento", length = 13)
	public Date getPrsFechaNacimiento() {
		return this.prsFechaNacimiento;
	}

	public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
		this.prsFechaNacimiento = prsFechaNacimiento;
	}

	@Column(name = "prs_sexo")
	public Integer getPrsSexo() {
		return this.prsSexo;
	}

	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}

	@Column(name = "prs_sexo_sniese")
	public Integer getPrsSexoSniese() {
		return this.prsSexoSniese;
	}

	public void setPrsSexoSniese(Integer prsSexoSniese) {
		this.prsSexoSniese = prsSexoSniese;
	}

	@Column(name = "prs_segundo_apellido", length = 32)
	@Length(max = 32)
	public String getPrsSegundoApellido() {
		return this.prsSegundoApellido;
	}

	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}

	
	
	
	@Column(name = "prs_discapacidad")
	public Integer getPrsDiscapacidad() {
		return prsDiscapacidad;
	}

	public void setPrsDiscapacidad(Integer prsDiscapacidad) {
		this.prsDiscapacidad = prsDiscapacidad;
	}

	@Column(name = "prs_tipo_discapacidad")
	public Integer getPrsTipoDiscapacidad() {
		return prsTipoDiscapacidad;
	}

	public void setPrsTipoDiscapacidad(Integer prsTipoDiscapacidad) {
		this.prsTipoDiscapacidad = prsTipoDiscapacidad;
	}
	
	@Column(name = "prs_porcentaje_discapacidad")
	public Integer getPrsPocentajeDiscapacidad() {
		return prsPocentajeDiscapacidad;
	}

	public void setPrsPocentajeDiscapacidad(Integer prsPocentajeDiscapacidad) {
		this.prsPocentajeDiscapacidad = prsPocentajeDiscapacidad;
	}

	@Column(name = "prs_carnet_conadis", length = 10)
	@Length(max = 10)
	public String getPrsCarnetConadis() {
		return prsCarnetConadis;
	}

	public void setPrsCarnetConadis(String prsCarnetConadis) {
		this.prsCarnetConadis = prsCarnetConadis;
	}

	@JoinColumn(name = "etn_id", referencedColumnName = "etn_id")
	@ManyToOne	
	public Etnia getPrsEtnia() {
		return prsEtnia;
	}

	public void setPrsEtnia(Etnia prsEtnia) {
		this.prsEtnia = prsEtnia;
	}

	
	@JoinColumn(name = "ubc_id", referencedColumnName = "ubc_id")
	@ManyToOne	
	public Ubicacion getPrsUbicacionNacionalidad() {
		return prsUbicacionNacionalidad;
	}

	public void setPrsUbicacionNacionalidad(Ubicacion prsUbicacionNacionalidad) {
		this.prsUbicacionNacionalidad = prsUbicacionNacionalidad;
	}
	
	
	
	
	
	@OneToMany(mappedBy = "usrPersona")
	public List<Usuario> getPrsUsuarios() {
		return prsUsuarios;
	}

	public void setPrsUsuarios(List<Usuario> prsUsuarios) {
		this.prsUsuarios = prsUsuarios;
	}

	@OneToMany(mappedBy = "fcesPersona")
	public List<FichaEstudiante> getPrsFichaEstudiantes() {
		return prsFichaEstudiantes;
	}

	public void setPrsFichaEstudiantes(List<FichaEstudiante> prsFichaEstudiantes) {
		this.prsFichaEstudiantes = prsFichaEstudiantes;
	}


	
	@Column(name = "prs_ubicacion_foto")
	public String getPrsUbicacionFoto() {
		return prsUbicacionFoto;
	}

	public void setPrsUbicacionFoto(String prsUbicacionFoto) {
		this.prsUbicacionFoto = prsUbicacionFoto;
	}

	
	

	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("prsId: " + prsId);
    	sb.append(tabulador);
    	sb.append("prsTipoIdentificacion: " + (prsTipoIdentificacion==null? "NULL":prsTipoIdentificacion.intValue()));  
    	sb.append(tabulador);
    	sb.append("prsTipoIdentificacionSniese: " + (prsTipoIdentificacionSniese==null? "NULL":prsTipoIdentificacionSniese));  
    	sb.append(tabulador);
    	sb.append("prsIdentificacion: " + (prsIdentificacion==null? "NULL":prsIdentificacion));  
    	sb.append(tabulador);
    	sb.append("prsPrimerApellido: " + (prsPrimerApellido==null? "NULL":prsPrimerApellido));  
    	sb.append(tabulador);
    	sb.append("prsSegundoApellido: " + (prsSegundoApellido==null? "NULL":prsSegundoApellido));
    	sb.append(tabulador);
    	sb.append("prsNombres: " + (prsNombres==null? "NULL":prsNombres));  
    	sb.append(tabulador);
    	sb.append("prsMailPersonal: " + (prsMailPersonal==null? "NULL":prsMailPersonal));  
    	sb.append(tabulador);
    	sb.append("prsMailInstitucional: " + (prsMailInstitucional==null? "NULL":prsMailInstitucional));  
    	sb.append(tabulador);
    	sb.append("prsTelefono: " + (prsTelefono==null? "NULL":prsTelefono));
    	sb.append(tabulador);
    	sb.append("prsFechaNacimiento: " + (prsFechaNacimiento==null? "NULL":prsFechaNacimiento));
    	sb.append(tabulador);
    	sb.append("prsSexo: " + (prsSexo==null? "NULL":prsSexo.intValue()));
    	sb.append(tabulador);
    	sb.append("prsSexoSniese: " + (prsSexoSniese==null? "NULL":prsSexoSniese));
        return sb.toString();
	}
}
