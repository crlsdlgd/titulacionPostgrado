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
   
 ARCHIVO:     Usuario.java	  
 DESCRIPCION: Entity Bean que representa a la tabla Usuario de la BD. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jpa.entidades.publico;


import java.io.Serializable;
import java.sql.Timestamp;
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

import org.hibernate.validator.constraints.Length;

/**
 * Clase (Entity Bean) Usuario.
 * Entity Bean que representa a la tabla Usuario de la BD.
 * @author dalbuja.
 * @version 1.0
 */
@Entity
@Table(name = "usuario", schema = "titulacion_posgrado")
public class Usuario implements  Serializable {
	private static final long serialVersionUID = 309064250396133949L;
	private int usrId;
	private Persona usrPersona;
	private String usrIdentificacion;
	private String usrNick;
	private String usrPassword;
	private Timestamp usrFechaCreacion;
	private Date usrFechaCaducidad;
	private Date usrFechaCadPass;
	private Integer usrEstado;
	private Integer usrEstSesion;
	private Integer usrActiveDirectory;
	private List<UsuarioRol> usrUsuarioRoles;

	public Usuario() {
	}

	public Usuario(int usrId) {
		this.usrId = usrId;
	}



//	public Usuario(int usrId, Persona persona, String usrIdentificacion,
//			String usrNick, String usrPassword, Timestamp usrFechaCreacion,
//			Date usrFechaCaducidad, Date usrFechaCadPass, Integer usrEstado,
//			Integer usrEstSesion, Integer usrActiveDirectory,
//			List<UsuarioRol> usrUsuarioRoles) {
//		this.usrId = usrId;
//		this.persona = persona;
//		this.usrIdentificacion = usrIdentificacion;
//		this.usrNick = usrNick;
//		this.usrPassword = usrPassword;
//		this.usrFechaCreacion = usrFechaCreacion;
//		this.usrFechaCaducidad = usrFechaCaducidad;
//		this.usrFechaCadPass = usrFechaCadPass;
//		this.usrEstado = usrEstado;
//		this.usrEstSesion = usrEstSesion;
//		this.usrActiveDirectory = usrActiveDirectory;
//		this.usrUsuarioRoles = usrUsuarioRoles;
//	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "usr_id", unique = true, nullable = false)
	public int getUsrId() {
		return this.usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	@JoinColumn(name = "prs_id", referencedColumnName = "prs_id")
	@ManyToOne
	public Persona getUsrPersona() {
		return usrPersona;
	}

	public void setUsrPersona(Persona usrPersona) {
		this.usrPersona = usrPersona;
	}	
	
	@Column(name = "usr_identificacion", length = 13)
	@Length(max = 13)
	public String getUsrIdentificacion() {
		return this.usrIdentificacion;
	}


	public void setUsrIdentificacion(String usrIdentificacion) {
		this.usrIdentificacion = usrIdentificacion;
	}

	@Column(name = "usr_nick", length = 20)
	@Length(max = 20)
	public String getUsrNick() {
		return this.usrNick;
	}

	public void setUsrNick(String usrNick) {
		this.usrNick = usrNick;
	}

	@Column(name = "usr_password", length = 64)
	@Length(max = 64)
	public String getUsrPassword() {
		return this.usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	@Column(name = "usr_fecha_creacion", length = 29)
	public Timestamp getUsrFechaCreacion() {
		return usrFechaCreacion;
	}

	public void setUsrFechaCreacion(Timestamp usrFechaCreacion) {
		this.usrFechaCreacion = usrFechaCreacion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "usr_fecha_caducidad", length = 13)
	public Date getUsrFechaCaducidad() {
		return this.usrFechaCaducidad;
	}

	public void setUsrFechaCaducidad(Date usrFechaCaducidad) {
		this.usrFechaCaducidad = usrFechaCaducidad;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "usr_fecha_cad_pass", length = 13)
	public Date getUsrFechaCadPass() {
		return this.usrFechaCadPass;
	}

	public void setUsrFechaCadPass(Date usrFechaCadPass) {
		this.usrFechaCadPass = usrFechaCadPass;
	}

	@Column(name = "usr_estado")
	public Integer getUsrEstado() {
		return this.usrEstado;
	}

	public void setUsrEstado(Integer usrEstado) {
		this.usrEstado = usrEstado;
	}

	@Column(name = "usr_est_sesion")
	public Integer getUsrEstSesion() {
		return this.usrEstSesion;
	}

	public void setUsrEstSesion(Integer usrEstSesion) {
		this.usrEstSesion = usrEstSesion;
	}

	@Column(name = "usr_active_directory")
	public Integer getUsrActiveDirectory() {
		return this.usrActiveDirectory;
	}

	public void setUsrActiveDirectory(Integer usrActiveDirectory) {
		this.usrActiveDirectory = usrActiveDirectory;
	}

	@OneToMany(mappedBy = "usroUsuario")
	public List<UsuarioRol> getUsrUsuarioRoles() {
		return usrUsuarioRoles;
	}

	public void setUsrUsuarioRoles(List<UsuarioRol> usrUsuarioRoles) {
		this.usrUsuarioRoles = usrUsuarioRoles;
	}

	
	@Override
	public String toString() {
		String tabulador = "\t";
    	StringBuilder sb = new StringBuilder();
    	sb.append("usrId: " + usrId);
    	sb.append(tabulador);
    	sb.append("usrPersona: " + (usrPersona==null? "NULL":usrPersona.getPrsId()));  
    	sb.append(tabulador);
    	sb.append("usrIdentificacion: " + (usrIdentificacion==null? "NULL":usrIdentificacion));  
    	sb.append(tabulador);
    	sb.append("usrNick: " + (usrNick==null? "NULL":usrNick));  
    	sb.append(tabulador);
    	sb.append("usrPassword: " + (usrPassword==null? "NULL":"*********"));  
    	sb.append(tabulador);
    	sb.append("usrFechaCreacion: " + (usrFechaCreacion==null? "NULL":usrFechaCreacion));  
    	sb.append(tabulador);
    	sb.append("usrFechaCaducidad: " + (usrFechaCaducidad==null? "NULL":usrFechaCaducidad)); 
    	sb.append(tabulador);
    	sb.append("usrFechaCadPass: " + (usrFechaCadPass==null? "NULL":usrFechaCadPass)); 
    	sb.append(tabulador);
    	sb.append("usrEstado: " + (usrEstado==null? "NULL":usrEstado.intValue())); 
    	sb.append(tabulador);
    	sb.append("usrEstSesion: " + (usrEstSesion==null? "NULL":usrEstSesion.intValue()));
    	sb.append(tabulador);
    	sb.append("usrActiveDirectory: " + (usrActiveDirectory==null? "NULL":usrActiveDirectory.intValue()));
        return sb.toString();
	}
}
