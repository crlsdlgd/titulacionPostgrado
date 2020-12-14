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
   
 ARCHIVO:     RecuperaClaveForm.java	  
 DESCRIPCION: Bean de sesion que maneja el recuperar la clave cuando el usuario la olvida
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.registroPostulante;


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

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.EncriptadorUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (session bean) RecuperaClaveForm.
 * Bean de sesion que maneja el olvido de clave de usuario.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="recuperaClaveForm")
@SessionScoped
public class RecuperaClaveForm implements Serializable {	
	private static final long serialVersionUID = 8689415317989690328L;
	
	private String rcfrmPassword;
	private Usuario rcfrmUsuario;
	private String rcfrmUsrParametroNickPass;
	private Integer rcfrmTipoSeleccionado;
	
	@EJB
	UsuarioServicio servRcfUsuario;
	@EJB
	private ConvocatoriaServicio servRcfConvocatoria;
	
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	@PostConstruct
	public void inicializar(Usuario usuario){
		rcfrmUsrParametroNickPass = "";
		rcfrmTipoSeleccionado = 0;
	}
	
	/**
	 * Maneja la recuperación de clave del usuario 
	 * @param nick parámetro que ingresa el usuario por pantalla
	 * @return retorna la navegación al inicio de la página
	 */
	public String recuperaClave(){
		try {
			//busqueda de convocatoria activa
//			@SuppressWarnings("unused")
//			Convocatoria pstFrmConvocatoria = servRcfConvocatoria.buscarPorIdPorEstado(ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
			rcfrmTipoSeleccionado = 0;
			rcfrmUsuario = servRcfUsuario.buscarPorNick(rcfrmUsrParametroNickPass);
			
//			if(rcfrmTipoSeleccionado == 0){ // selección por nick
//				rcfrmUsuario = servRcfUsuario.buscarPorNick(rcfrmUsrParametroNickPass);
//			}
//			if(rcfrmTipoSeleccionado == 1){ // selección por correo
//				rcfrmUsuario = servRcfUsuario.buscarPorCorreo(rcfrmUsrParametroNickPass);
//			}
			
			if(rcfrmUsuario != null){
				//verifico si el usuario es o no del active directory
				if(rcfrmUsuario.getUsrActiveDirectory().intValue() == UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE){//si es del active
					FacesUtil.mensajeError("Para cambiar su contraseña, debe realizarlo desde el correo electrónico institucional ");
					return "cancelarRecuperaClave";
				}
				
				rcfrmPassword = GeneralesUtilidades.generarClave(); //password
				rcfrmUsuario.setUsrPassword(EncriptadorUtilidades.resumirMensaje(rcfrmPassword, EncriptadorUtilidades.MD5));
				servRcfUsuario.actualizaPassword(rcfrmUsuario);
				
				
				//******************************************************************************
				//************************* ACA INICIA EL ENVIO DE MAIL ************************
				//******************************************************************************
				//defino los datos para la plantilla
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("nombres", rcfrmUsuario.getUsrPersona().getPrsNombres());
				parametros.put("apellidos", rcfrmUsuario.getUsrPersona().getPrsPrimerApellido()+ " " 
											+ (rcfrmUsuario.getUsrPersona().getPrsSegundoApellido()==null?"": rcfrmUsuario.getUsrPersona().getPrsSegundoApellido()));
				SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
				parametros.put("fechaHora", sdf.format(new Date()));
				parametros.put("usuario", rcfrmUsuario.getUsrNick());
				parametros.put("clave", rcfrmPassword);
				parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
				
				//lista de correos a los que se enviará el mail
				List<String> correosTo = new ArrayList<>();
				correosTo.add(rcfrmUsuario.getUsrPersona().getPrsMailPersonal());
				
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
				
			}
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecuperaClave.recupera.actualiza.clave.exitoso")));
			rcfrmUsrParametroNickPass = "";  //MF
		} catch (UsuarioNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecuperaClave.recupera.buscar.correo.noencontrado")));
			return null;
		} catch (UsuarioException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecuperaClave.recupera.actualiza.clave.nogenera")));
		} catch (NoSuchAlgorithmException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecuperaClave.recupera.genera.algoritmo.nogenera")));
		} catch (UsuarioValidacionException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecuperaClave.recupera.genera.clave.nogenera")));
//		} catch (ConvocatoriaNoEncontradoException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.no.result.exception")));
//		} catch (ConvocatoriaException e) {
//			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.exception")));
		} catch (Exception e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RecuperaClave.recupera.genera.clave.general")));
		}	
		return "irVerPaginaCorreo";
	}
	
	/**
	 * Maneja de cancelacion del recupera de clave
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarRecuperaClave(){
		rcfrmUsuario = null;
		rcfrmUsrParametroNickPass = "";
		return "cancelarRecuperaClave";
		
	}
	
	//**************************************************************//
	//************** METODOS ACCESORES Y MUTADORES *****************//
	//**************************************************************//
	public String getRcfrmPassword() {
		return rcfrmPassword;
	}
	public void setRcfrmPassword(String rcfrmPassword) {
		this.rcfrmPassword = rcfrmPassword;
	}
	public Usuario getRcfrmUsuario() {
		return rcfrmUsuario;
	}
	public void setRcfrmUsuario(Usuario rcfrmUsuario) {
		this.rcfrmUsuario = rcfrmUsuario;
	}
	public String getRcfrmUsrParametroNickPass() {
		return rcfrmUsrParametroNickPass;
	}
	public void setRcfrmUsrParametroNickPass(String rcfrmUsrParametroNickPass) {
		this.rcfrmUsrParametroNickPass = rcfrmUsrParametroNickPass;
	}
	public Integer getRcfrmTipoSeleccionado() {
		return rcfrmTipoSeleccionado;
	}
	public void setRcfrmTipoSeleccionado(Integer rcfrmTipoSeleccionado) {
		this.rcfrmTipoSeleccionado = rcfrmTipoSeleccionado;
	}
		
}
