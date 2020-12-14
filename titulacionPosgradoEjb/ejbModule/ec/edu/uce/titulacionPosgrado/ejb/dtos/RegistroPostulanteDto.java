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
   
 ARCHIVO:     RegistroPostulanteDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el regitro de un postulante. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			Daniel Albuja			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase (DTO) RegistroPostulanteDto.
 * DTO encargado de manejar los datos para el regitro de un postulante.
 * @author dalbuja.
 * @version 1.0
 */
public class RegistroPostulanteDto implements Serializable {
	private static final long serialVersionUID = -657122440738721554L;
	
	//****************** PERSONA *****************************
	private Integer prsId;
	private Integer prsTipoIdentificacion;
	private Integer prsTipoIdentificacionSniese;
	private String prsIdentificacion;
	private String prsPrimerApellido;
	private String prsSegundoApellido;
	private String prsNombres;
	private Integer prsSexo;
	private Integer prsSexoSniese;
	private String prsMailPersonal;
	private String prsMailInstitucional;
	private String prsTelefono;
	private Date prsFechaNacimiento;
	private Integer prsEtnia;
	private Integer prsUbcNacionalidad;
	
	//****************** USUARIO *****************************
	private Integer usrId;
	private String usrIdentificacion;
	private String usrNick;
	private String usrPassword;
	private Timestamp usrFechaCreacion;
	private Date usrFechaCaducidad;
	private Date usrFechaCadPass;
	private Integer usrEstado;
	private Integer usrEstSession;
	private Integer usrActiveDirectory;
	private Integer usrUbcNacionalidad;
	
	//****************** USUARIO_ROL *****************************
	private Integer usroId;
	private Integer usroEstado;
	private Integer usroUsrId;
	private Integer usroRol;
	
	
	public RegistroPostulanteDto(){
		
	}
	
	public RegistroPostulanteDto(Integer prsId, Integer prsTipoIdentificacion,
			Integer prsTipoIdentificacionSniese, String prsIdentificacion,
			String prsPrimerApellido, String prsSegundoApellido,
			String prsNombres, Integer prsSexo, Integer prsSexoSniese,
			String prsMailPersonal, String prsMailInstitucional,
			String prsTelefono, Date prsFechaNacimiento, Integer prsEtnia,
			Integer prsUbcNacionalidad, Integer usrId,
			String usrIdentificacion, String usrNick, String usrPassword,
			Timestamp usrFechaCreacion, Date usrFechaCaducidad,
			Date usrFechaCadPass, Integer usrEstado, Integer usrEstSession,
			Integer usrActiveDirectory, Integer usrUbcNacionalidad,
			Integer usroId, Integer usroEstado, Integer usroUsrId,
			Integer usroRol) {
		this.prsId = prsId;
		this.prsTipoIdentificacion = prsTipoIdentificacion;
		this.prsTipoIdentificacionSniese = prsTipoIdentificacionSniese;
		this.prsIdentificacion = prsIdentificacion;
		this.prsPrimerApellido = prsPrimerApellido;
		this.prsSegundoApellido = prsSegundoApellido;
		this.prsNombres = prsNombres;
		this.prsSexo = prsSexo;
		this.prsSexoSniese = prsSexoSniese;
		this.prsMailPersonal = prsMailPersonal;
		this.prsMailInstitucional = prsMailInstitucional;
		this.prsTelefono = prsTelefono;
		this.prsFechaNacimiento = prsFechaNacimiento;
		this.prsEtnia = prsEtnia;
		this.prsUbcNacionalidad = prsUbcNacionalidad;
		this.usrId = usrId;
		this.usrIdentificacion = usrIdentificacion;
		this.usrNick = usrNick;
		this.usrPassword = usrPassword;
		this.usrFechaCreacion = usrFechaCreacion;
		this.usrFechaCaducidad = usrFechaCaducidad;
		this.usrFechaCadPass = usrFechaCadPass;
		this.usrEstado = usrEstado;
		this.usrEstSession = usrEstSession;
		this.usrActiveDirectory = usrActiveDirectory;
		this.usrUbcNacionalidad = usrUbcNacionalidad;
		this.usroId = usroId;
		this.usroEstado = usroEstado;
		this.usroUsrId = usroUsrId;
		this.usroRol = usroRol;
	}

	public Integer getPrsId() {
		return prsId;
	}
	public void setPrsId(Integer prsId) {
		this.prsId = prsId;
	}
	public Integer getPrsTipoIdentificacion() {
		return prsTipoIdentificacion;
	}
	public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
		this.prsTipoIdentificacion = prsTipoIdentificacion;
	}
	public Integer getPrsTipoIdentificacionSniese() {
		return prsTipoIdentificacionSniese;
	}
	public void setPrsTipoIdentificacionSniese(Integer prsTipoIdentificacionSniese) {
		this.prsTipoIdentificacionSniese = prsTipoIdentificacionSniese;
	}
	public String getPrsIdentificacion() {
		return prsIdentificacion;
	}
	public void setPrsIdentificacion(String prsIdentificacion) {
		this.prsIdentificacion = prsIdentificacion;
	}
	public String getPrsPrimerApellido() {
		return prsPrimerApellido;
	}
	public void setPrsPrimerApellido(String prsPrimerApellido) {
		this.prsPrimerApellido = prsPrimerApellido;
	}
	public String getPrsSegundoApellido() {
		return prsSegundoApellido;
	}
	public void setPrsSegundoApellido(String prsSegundoApellido) {
		this.prsSegundoApellido = prsSegundoApellido;
	}
	public String getPrsNombres() {
		return prsNombres;
	}
	public void setPrsNombres(String prsNombres) {
		this.prsNombres = prsNombres;
	}
	public Integer getPrsSexo() {
		return prsSexo;
	}
	public void setPrsSexo(Integer prsSexo) {
		this.prsSexo = prsSexo;
	}
	public Integer getPrsSexoSniese() {
		return prsSexoSniese;
	}
	public void setPrsSexoSniese(Integer prsSexoSniese) {
		this.prsSexoSniese = prsSexoSniese;
	}
	public String getPrsMailPersonal() {
		return prsMailPersonal;
	}
	public void setPrsMailPersonal(String prsMailPersonal) {
		this.prsMailPersonal = prsMailPersonal;
	}
	public String getPrsMailInstitucional() {
		return prsMailInstitucional;
	}
	public void setPrsMailInstitucional(String prsMailInstitucional) {
		this.prsMailInstitucional = prsMailInstitucional;
	}
	public String getPrsTelefono() {
		return prsTelefono;
	}
	public void setPrsTelefono(String prsTelefono) {
		this.prsTelefono = prsTelefono;
	}
	public Date getPrsFechaNacimiento() {
		return prsFechaNacimiento;
	}
	public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
		this.prsFechaNacimiento = prsFechaNacimiento;
	}
	public Integer getPrsEtnia() {
		return prsEtnia;
	}
	public void setPrsEtnia(Integer prsEtnia) {
		this.prsEtnia = prsEtnia;
	}
	public Integer getPrsUbcNacionalidad() {
		return prsUbcNacionalidad;
	}
	public void setPrsUbcNacionalidad(Integer prsUbcNacionalidad) {
		this.prsUbcNacionalidad = prsUbcNacionalidad;
	}
	public Integer getUsrId() {
		return usrId;
	}
	public void setUsrId(Integer usrId) {
		this.usrId = usrId;
	}
	public String getUsrIdentificacion() {
		return usrIdentificacion;
	}
	public void setUsrIdentificacion(String usrIdentificacion) {
		this.usrIdentificacion = usrIdentificacion;
	}
	public String getUsrNick() {
		return usrNick;
	}
	public void setUsrNick(String usrNick) {
		this.usrNick = usrNick;
	}
	public String getUsrPassword() {
		return usrPassword;
	}
	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}
	public Timestamp getUsrFechaCreacion() {
		return usrFechaCreacion;
	}
	public void setUsrFechaCreacion(Timestamp usrFechaCreacion) {
		this.usrFechaCreacion = usrFechaCreacion;
	}
	public Date getUsrFechaCaducidad() {
		return usrFechaCaducidad;
	}
	public void setUsrFechaCaducidad(Date usrFechaCaducidad) {
		this.usrFechaCaducidad = usrFechaCaducidad;
	}
	public Date getUsrFechaCadPass() {
		return usrFechaCadPass;
	}
	public void setUsrFechaCadPass(Date usrFechaCadPass) {
		this.usrFechaCadPass = usrFechaCadPass;
	}
	public Integer getUsrEstado() {
		return usrEstado;
	}
	public void setUsrEstado(Integer usrEstado) {
		this.usrEstado = usrEstado;
	}
	public Integer getUsrEstSession() {
		return usrEstSession;
	}
	public void setUsrEstSession(Integer usrEstSession) {
		this.usrEstSession = usrEstSession;
	}
	public Integer getUsrActiveDirectory() {
		return usrActiveDirectory;
	}
	public void setUsrActiveDirectory(Integer usrActiveDirectory) {
		this.usrActiveDirectory = usrActiveDirectory;
	}
	public Integer getUsrUbcNacionalidad() {
		return usrUbcNacionalidad;
	}
	public void setUsrUbcNacionalidad(Integer usrUbcNacionalidad) {
		this.usrUbcNacionalidad = usrUbcNacionalidad;
	}
	public Integer getUsroId() {
		return usroId;
	}
	public void setUsroId(Integer usroId) {
		this.usroId = usroId;
	}
	public Integer getUsroEstado() {
		return usroEstado;
	}
	public void setUsroEstado(Integer usroEstado) {
		this.usroEstado = usroEstado;
	}
	public Integer getUsroUsrId() {
		return usroUsrId;
	}
	public void setUsroUsrId(Integer usroUsrId) {
		this.usroUsrId = usroUsrId;
	}
	public Integer getUsroRol() {
		return usroRol;
	}
	public void setUsroRol(Integer usroRol) {
		this.usroRol = usroRol;
	}
	
	
	
}
