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
   
 ARCHIVO:     CambioClaveForm.java	  
 DESCRIPCION: Bean de sesion que maneja la administración de la tabla Usuario. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01/04/2018				Daniel Albuja                         Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.superAdministracion;


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.EncriptadorUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) CambioClaveForm.
 * Managed Bean que administra la tabla Usuario.
 * @author dalbuja.
 * @version 1.0
 */


@ManagedBean(name="cambioClaveForm")
@SessionScoped
public class CambioClaveForm implements Serializable {

	private static final long serialVersionUID = -3589543508831737297L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	
	private Usuario ccfUsuarioEditar;
	
	private String ccfClaveActual;
	private String ccfClaveNueva;
	private String ccfClaveConfirmacion;
	
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
	private UsuarioServicio servccfUsuarioServicio;
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	
	/**
	 * Maneja el inicio del bean 
	 * @return - cadena de navegacion a l apagina de cambioClave
	 */
	
	
	
	public String irCambioClave(Usuario usuario){
		//vericacion de que el usuario no pertenesca al active directory 0.-si 1.-no
		if(usuario.getUsrActiveDirectory().intValue() == UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE){
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.iniciar.si.active.directory")));
			return null;
		}
		ccfUsuarioEditar = usuario;
		iniciarParametros();
		return "irCambioClave";
	}
	
	/**
	 * Maneja de cancelacion del cambio de clave
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarCambioClave(){
		ccfUsuarioEditar = null;
		return "irInicio";

	}

	/**
	 * Maneja el cambio de clave del usuario 
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cambiarClave(){
		try {
			if(ccfClaveNueva.length()<UsuarioConstantes.LARGO_CLAVE.intValue()){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.largo.clave.error")));
//				,UsuarioConstantes.LARGO_CLAVE,UsuarioConstantes.LARGO_CLAVE
				return null;
			}
			if(!Pattern.matches(UsuarioConstantes.REGEX_CLAVE, ccfClaveNueva)){
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.caracteres.error")));
				return null;
			}
			String claveActualPerfil = EncriptadorUtilidades.resumirMensaje(ccfClaveActual, EncriptadorUtilidades.MD5);
			String claveNueva = EncriptadorUtilidades.resumirMensaje(ccfClaveNueva, EncriptadorUtilidades.MD5);
			String claveConfirmacion = EncriptadorUtilidades.resumirMensaje(ccfClaveConfirmacion, EncriptadorUtilidades.MD5);
	
			//verifico si la clave actual es igual a la del usuario
			if(claveActualPerfil.equals(ccfUsuarioEditar.getUsrPassword())){
				if(!claveActualPerfil.equals(claveNueva)){
					if(claveNueva.equals(claveConfirmacion)){
						ccfUsuarioEditar.setUsrPassword(claveNueva);
						servccfUsuarioServicio.editar(ccfUsuarioEditar);
					}else{
						FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.no.coinciden.contraseñas")));
						return null;
					}
				}else{
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.contraseñas.iguales")));
					return null;
				}
			}else{
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.clave.anterior.incorrecta")));
				return null;
				}
			
			} catch (UsuarioValidacionException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.usuario.validacion.exception")));
				return null;
			} catch (UsuarioNoEncontradoException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.usuario.no.encontrado.exception")));
				return null;
			} catch (UsuarioException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.usuario.general.exception")));
				return null;
			} catch (NoSuchAlgorithmException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.encriptar.contraseña.exception")));
				return null;
			}
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Cambio.clave.cambiar.exitoso")));
			return "irInicio";

	}
	
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
		
	//iniciar parametros de busqueda
	public void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		ccfClaveActual = new String();
		ccfClaveNueva = new String();
		ccfClaveConfirmacion = new String();
	}
	
	
	
	
	
	
	

	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/		
		public Usuario getCcfUsuarioEditar() {
			return ccfUsuarioEditar;
		}

		public void setCcfUsuarioEditar(Usuario ccfUsuarioEditar) {
			this.ccfUsuarioEditar = ccfUsuarioEditar;
		}

		public String getCcfClaveActual() {
			return ccfClaveActual;
		}

		public void setCcfClaveActual(String ccfClaveActual) {
			this.ccfClaveActual = ccfClaveActual;
		}

		public String getCcfClaveNueva() {
			return ccfClaveNueva;
		}

		public void setCcfClaveNueva(String ccfClaveNueva) {
			this.ccfClaveNueva = ccfClaveNueva;
		}

		public String getCcfClaveConfirmacion() {
			return ccfClaveConfirmacion;
		}

		public void setCcfClaveConfirmacion(String ccfClaveConfirmacion) {
			this.ccfClaveConfirmacion = ccfClaveConfirmacion;
		}
		


	
}
