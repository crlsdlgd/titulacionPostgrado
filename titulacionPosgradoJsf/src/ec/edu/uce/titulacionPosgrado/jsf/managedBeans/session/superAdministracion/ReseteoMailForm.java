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
   
 ARCHIVO:     ReseteoContraseniaForm.java	  
 DESCRIPCION: Bean de sesion que maneja la administración de la tabla Usuario. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01/04/2018				Daniel Albuja                         Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.superAdministracion;


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import ec.edu.uce.envioMail.excepciones.ValidacionMailException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.EncriptadorUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (managed bean) ReseteoContraseniaForm.
 * Managed Bean que administra la tabla Usuario.
 * @author dalbuja.
 * @version 1.0
 */


@ManagedBean(name="reseteoMailForm")
@SessionScoped
public class ReseteoMailForm implements Serializable {

	private static final long serialVersionUID = -3589543508831737297L;
		
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	
	private Usuario rcfUsuarioBuscar;
	
	
	private Usuario rcfUsuarioEditar;
	private List<Usuario> rcfListUsuario;
	
	
	private String rcfPasword;
	private String rcfPaswordConfirmar;
	
	private Persona rcfPersona;
	private Persona rcfPersonaEditar;
	private List<Persona> rcfListPersona;
	private String rcfCorreoNuevo;
	
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	private UsuarioServicio servRcfUsuario;
	
	@EJB
	private PersonaServicio servRcfPersona;
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	
	/**
	 * Maneja de inicio del bean
	 * @return - cadena de navegacion a la pagina de reseteo contraseña
	 */
	public String irListaReseteoMail(Usuario usuario){
		//verifico si el usuario es o no del active directory
		if(usuario.getUsrActiveDirectory().intValue() == UsuarioConstantes.ACTIVE_DIRECTORY_NO_VALUE){//si es del active es 0
			FacesUtil.mensajeInfo("No puede ingresar a esta funcionalidad");
			return null;
		}
		iniciarParametros();
		return "irListaReseteoMail";
	}
	
	/**
	 * Maneja de cancelacion de registro
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarReseteoMail(){
		limpiar();
		return "cancelarReseteoMail";
		
	}
	
	/**
	 * Busca un estudiante según los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscar(){
		//anulo la lista de estudiantes
//		rcfPersonaBuscar = null;
		
		try {
			if(rcfPersonaEditar.getPrsIdentificacion()!=null || !rcfPersonaEditar.getPrsIdentificacion().equals("")){
				rcfPersona =  servRcfPersona.buscarPorIdentificacion(rcfPersonaEditar.getPrsIdentificacion());
			}else{
				FacesUtil.mensajeWarn("Ingrese la identifición de la persona que desea buscar");
			}
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.no.result.exception",rcfPersonaEditar.getPrsIdentificacion())));
		} catch (PersonaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.exception")));
		}
		
		
	}
	
	/**
	 * Limpia los parámetros ingresados en el panel de busqueda del estudiante
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	
	/**
	 * obtiene la entidad a la que va a editar y redirige a la pagina de editarUsuario
	 * la bandera se pone en 0 ya que se necesitan los botones de Editar
	 * @param ufEditarUsuario - ufEditarUsuario entidad a la que se va a editar
	 * @return navegacion a la pagina de editar
	 */
	public String irEditar(Persona rcfPersonaEditar){
		this.rcfPersonaEditar=rcfPersonaEditar;
		return "irEditar";
	}
	
	

	
	/**
	 * cancelar la edicion de la entidad
	 * @return navegacion a la pagina de listar usuario
	 */
	public String cancelarEditarUsuario(){
		
		iniciarParametros();
		
		return "irListaReseteoMail";
	}
	
	
	/**
	 * Genera la edicion en la base de datos de la entidad a la que se deseaba modificar
	 * @return navegacion a la pagina listar usuario
	 */
	public String guardarCambios (){
		
		try {
			
			rcfUsuarioBuscar = servRcfUsuario.buscarIndentificacion(rcfPersonaEditar.getPrsIdentificacion());
			
			
			rcfPasword = GeneralesUtilidades.generarClave();
			rcfPaswordConfirmar = EncriptadorUtilidades.resumirMensaje(rcfPasword, EncriptadorUtilidades.MD5);
			rcfCorreoNuevo = rcfPersonaEditar.getPrsMailPersonal();
			rcfCorreoNuevo = rcfCorreoNuevo.toLowerCase().replaceAll(" +", " ").trim();
			
			
			servRcfUsuario.actualizarClaveUsuarioMailPersona(rcfUsuarioBuscar.getUsrId(), rcfPaswordConfirmar, rcfPersonaEditar.getPrsId(), rcfCorreoNuevo);
			
			//******************************************************************************
			//************************* ACA INICIA EL ENVIO DE MAIL ************************
			//******************************************************************************
			//defino los datos para la plantilla
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("nombres", rcfPersonaEditar.getPrsNombres());
			parametros.put("apellidos", rcfPersonaEditar.getPrsPrimerApellido()+ " " 
			+ (rcfPersonaEditar.getPrsSegundoApellido()==null?"": rcfPersonaEditar.getPrsSegundoApellido()));
			SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
			parametros.put("fechaHora", sdf.format(new Date()));
			parametros.put("usuario", rcfUsuarioBuscar.getUsrNick());
			parametros.put("clave", rcfPasword);
			parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);

			//lista de correos a los que se enviara el mail
			List<String> correosTo = new ArrayList<>();
			correosTo.add(rcfCorreoNuevo);
			
			
			//path de la plantilla del mail
			String pathTemplate = "/ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-cambio-clave.vm";

			//llamo al generador de mails
			GeneradorMails genMail = new GeneradorMails();
			String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_REGITRO, 
			GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
			//****envio el mail a la cola
			//cliente web service
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(GeneralesConstantes.APP_URL_WEB_SERVICE_MAIL+"/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
			MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
			postForm.add("mail", mailjsonSt);
			target.request().post(Entity.form(postForm),String.class);

			FacesUtil.mensajeInfo("Registro actualizado y notificado al correo electrónico del usuario.");
			
			
		} catch (NoSuchAlgorithmException e) {
			FacesUtil.mensajeError("Error al cambiar la contraseña, por favor intente más tarde.");
			return "irListaReseteoMail";
		} catch (UsuarioValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
			return "irListaReseteoMail";
		} catch (UsuarioException e) {
			FacesUtil.mensajeError(e.getMessage());
			return "irListaReseteoMail";
		} catch (ValidacionMailException e) {
			FacesUtil.mensajeError("Error al enviar el mail de registro");
			return "irListaReseteoMail";
		} catch (Exception e) {
			FacesUtil.mensajeError("Error al generar contraseña, por favor intente más tarde.");
			return "listarUsuario";
		}
		
		
		return "irListaReseteoMail";
	}
	
	
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
		
		//iniciar parametros de busqueda
		public void iniciarParametros(){
			//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
			rcfPersonaEditar = new Persona();
			rcfPersona =  null;
			rcfPersonaEditar.setPrsIdentificacion("");
		}
		
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/		

		public Usuario getRcfUsuarioBuscar() {
			return rcfUsuarioBuscar;
		}

		public void setRcfUsuarioBuscar(Usuario rcfUsuarioBuscar) {
			this.rcfUsuarioBuscar = rcfUsuarioBuscar;
		}

		public Usuario getRcfUsuarioEditar() {
			return rcfUsuarioEditar;
		}

		public void setRcfUsuarioEditar(Usuario rcfUsuarioEditar) {
			this.rcfUsuarioEditar = rcfUsuarioEditar;
		}

		public List<Usuario> getRcfListUsuario() {
			rcfListUsuario = rcfListUsuario==null?(new ArrayList<Usuario>()):rcfListUsuario;
			return rcfListUsuario;
		}

		public void setRcfListUsuario(List<Usuario> rcfListUsuario) {
			this.rcfListUsuario = rcfListUsuario;
		}
		
		
		
		

		public List<Persona> getRcfListPersona() {
			rcfListPersona = rcfListPersona==null?(new ArrayList<Persona>()):rcfListPersona;
			return rcfListPersona;
		}

		public void setRcfListPersona(List<Persona> rcfListPersona) {
			this.rcfListPersona = rcfListPersona;
		}

		public String getRcfPasword() {
			return rcfPasword;
		}

		public void setRcfPasword(String rcfPasword) {
			this.rcfPasword = rcfPasword;
		}

		public String getRcfPaswordConfirmar() {
			return rcfPaswordConfirmar;
		}

		public void setRcfPaswordConfirmar(String rcfPaswordConfirmar) {
			this.rcfPaswordConfirmar = rcfPaswordConfirmar;
		}

		public Persona getRcfPersona() {
			return rcfPersona;
		}

		public void setRcfPersona(Persona rcfPersona) {
			this.rcfPersona = rcfPersona;
		}

		public String getRcfCorreoNuevo() {
			return rcfCorreoNuevo;
		}

		public void setRcfCorreoNuevo(String rcfCorreoNuevo) {
			this.rcfCorreoNuevo = rcfCorreoNuevo;
		}

		public Persona getRcfPersonaEditar() {
			return rcfPersonaEditar;
		}

		public void setRcfPersonaEditar(Persona rcfPersonaEditar) {
			this.rcfPersonaEditar = rcfPersonaEditar;
		}
	

	
}
