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
   
 ARCHIVO:     SesionUsuarioForm.java	  
 DESCRIPCION: Clase encargada de obtener usuario y roles a traves del facesUtils. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 05-MAY-2016		 Dennis Collaguazo 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.manejoSesion;


import java.io.Serializable;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.GrantedAuthority;

import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity.ProveedorPermisos;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (manageBean) SesionUsuarioForm.
 * Clase encargada de obtener usuario y roles a traves del facesUtils.
 * @author dalbuja.
 * @version 1.0
 */
@ManagedBean(name="sesionUsuarioForm")
@SessionScoped
public class SesionUsuarioForm implements Serializable{
	private static final long serialVersionUID = 2300187948955776410L;
	
	//****************************************************
	//************** PARA TITULACIÓN *********************
	
	private Usuario frmSuUsuario;
	
	/**
	 * Retorna el usuario (entity bean) logueado
	 * @return el usuario (entity bean) logueado
	 */
	public Usuario getFrmSuUsuario() {
		frmSuUsuario = FacesUtil.obtenerUsuario();
		return frmSuUsuario;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol POSTULANTE, false de lo contrario
	 * @return true si el usuario tiene el rol POSTULANTE, false de lo contrario
	 */
	public boolean isPostulante(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_POSTULANTE)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol VALIDADOR, false de lo contrario
	 * @return true si el usuario tiene el rol VALIDADOR, false de lo contrario
	 */
	public boolean isSecretaria(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_VALIDADOR)){
				return true;
			}
		}
		return false;
	}
	
	
	
	//****************************************
	//************** FIN *********************
	
	/**
	 * Retorna true si el usuario tiene el rol ADMINISTRADOR, false de lo contrario
	 * @return true si el usuario tiene el rol ADMINISTRADOR, false de lo contrario
	 */
	public boolean isAdministrador(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_ADMINISTRADOR)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna true si el usuario tiene el rol EVALUADOR, false de lo contrario
	 * @return true si el usuario tiene el rol EVALUADOR, false de lo contrario
	 */
	public boolean isEvaluador(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_EVALUADOR)){
				return true;
			}
		}
		return false;
	}
//	
//	/**
//	 * Retorna true si el usuario tiene el rol CONSULTOR, false de lo contrario
//	 * @return true si el usuario tiene el rol CONSULTOR, false de lo contrario
//	 */
	public boolean isConsultor(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_CONSULTOR)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Retorna true si el usuario tiene el rol EDITOR_DGA, false de lo contrario
	 * @return true si el usuario tiene el rol EDITOR_DGA, false de lo contrario
	 */
	public boolean isEditorDgip(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_EDITOR_DGIP)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true si el usuario tiene el rol SEGRETARIAABOGADO, false de lo contrario
	 * @return true si el usuario tiene el rol SEGRETARIAABOGADO, false de lo contrario
	 */
	public boolean isEditorActa(){
		Collection<ProveedorPermisos> aux = FacesUtil.obtenerPerfiles();
		for(GrantedAuthority item : aux){
			if(item.getAuthority().equals(RolConstantes.ROL_EDITOR_ACTA)){
				return true;
			}
		}
		return false;
	}
	
}
