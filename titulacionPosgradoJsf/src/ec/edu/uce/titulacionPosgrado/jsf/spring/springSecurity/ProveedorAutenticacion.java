/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ProveedorAutenticacion.java      
 DESCRIPCION: Clase encargada de confirmar el login del usuario
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018 		  Daniel Albuja						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity;

/**
 * Clase ProveedorAutenticacion.
 * Clase encargada de confirmar el login del usuario 
 * @author dalbuja
 * @version 1.0
 */
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.EncriptadorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.spring.excepciones.AutenticacionGeneralException;
import ec.edu.uce.titulacionPosgrado.jsf.spring.excepciones.UsuarioInactivoException;



@Named("proveedorAutenticacion")
public class ProveedorAutenticacion implements AuthenticationProvider, Serializable{
	private static final long serialVersionUID = 3572187630900863258L;

	private UsuarioServicio srvUsuarioEjb;
	private UsuarioRolServicio srvUsuarioRolEjb;
	private String mensaje;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		try {
			
			HttpServletRequest request = 
			        ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
			                .getRequest();
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			boolean verificado = VerificarRecaptcha.verificar(gRecaptchaResponse);
			
			String username = authentication.getName();
			String password = (String) authentication.getCredentials();
			//consulta usuario
			Usuario usu = srvUsuarioEjb.buscarPorNick(username);
			// verifica que el estado del usario este activo // todo esto para el caso de cambio de usuarios.
			//0: activo
			//1: inactivo
			if(usu.getUsrEstado() == 0 && verificado){
				//verifico si el usuario es o no del active directory
				if(usu.getUsrActiveDirectory().intValue() == UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE){
					ConexionLdap ldap = new ConexionLdap("10.20.1.50", "389", "DC=uce,DC=edu,DC=ec");

					//					ConexionLdap ldap = new ConexionLdap("uce.edu.ec", "389", "DC=uce,DC=edu,DC=ec");
					if(!ldap.verificarLoginUsuario(username, password) || password.length() == 0 ){
						mensaje = "Usuario y/o contraseña mal ingresados";
						throw new AutenticacionGeneralException("");
					}
				}else{//si es del active directory
					//verificacion de usuario y password
					if(username.equalsIgnoreCase(usu.getUsrNick())){
						String passEncript = null;
						try {
							passEncript = EncriptadorUtilidades.resumirMensaje(password, EncriptadorUtilidades.MD5);

						} catch (NoSuchAlgorithmException e) {
							mensaje = "Usuario y/o contraseña mal ingresados .";
							throw new AutenticacionGeneralException("");
						}
						if(!passEncript.equals(usu.getUsrPassword())){
							mensaje = "Usuario y/o contraseña mal ingresados.";
							throw new AutenticacionGeneralException("");
						}
					}else{
						mensaje = "Usuario y/o contraseña mal ingresados..";
						throw new AutenticacionGeneralException("");
					}
				}

				//**************************************************************
				//**************** ASIGNACION DE PERMISOS **********************
				//**************************************************************
				//definicion de lista de permisos
				List<ProveedorPermisos> permisos = new ArrayList<ProveedorPermisos>();

				//consulta de roles
				List<UsuarioRol> usuRoles = srvUsuarioRolEjb.buscarXUsuario(usu.getUsrId());
				for (UsuarioRol item : usuRoles) {
					permisos.add(new ProveedorPermisos(item.getUsroRol().getRolDescripcion()));
				}
				
				DetalleUsuario usuario = new DetalleUsuario(usu, permisos);
				return new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), permisos);
			}else{
				if(!verificado){
					mensaje = "Error al verificar el Test de Turing público y automático (ReCaptcha).";
					throw new UsuarioInactivoException("Error al validar Recaptcha");
				}else{
					mensaje = "Usuario inactivo";
					throw new UsuarioInactivoException("Usuario inactivo");	
				}
			}
		} catch (UsuarioNoEncontradoException e1) {
			mensaje = "Usuario y/o contraseña mal ingresados";
			throw new BadCredentialsException("");
		} catch (UsuarioException e1) {
			mensaje = "Usuario y/o contraseña mal ingresados.";
			throw new BadCredentialsException("");
		} catch (UsuarioInactivoException e) {
			throw new BadCredentialsException("");	
		} catch (AutenticacionGeneralException e) {
			throw new BadCredentialsException("");	
		} catch (Exception e1) {
			mensaje = "Usuario y/o contraseña mal ingresados...";
			throw new BadCredentialsException("");
		} 

		
	}

	@Override
	public boolean supports(Class<?> arg0){
		return true;
	}
	public UsuarioServicio getSrvUsuarioEjb() {
		return srvUsuarioEjb;
	}
	public void setSrvUsuarioEjb(UsuarioServicio srvUsuarioEjb) {
		this.srvUsuarioEjb = srvUsuarioEjb;
	}
	public UsuarioRolServicio getSrvUsuarioRolEjb() {
		return srvUsuarioRolEjb;
	}
	public void setSrvUsuarioRolEjb(UsuarioRolServicio srvUsuarioRolEjb) {
		this.srvUsuarioRolEjb = srvUsuarioRolEjb;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
