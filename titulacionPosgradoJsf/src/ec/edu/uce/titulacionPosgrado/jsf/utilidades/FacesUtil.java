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
   
 ARCHIVO:     FacesUtil.java	  
 DESCRIPCION: Clase encargada de obtener usuario y roles del facesContext, 
 			  asi como tabien de manejar los mensajes de error para el usuario. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018 		  Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.utilidades;


import java.util.Collection;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.security.core.context.SecurityContextHolder;

import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity.DetalleUsuario;
import ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity.ProveedorPermisos;

/**
 * Clase FacesUtil.
 * Clase encargada de obtener usuario y roles del facesContext, asi como tabien de manejar los mensajes de error para el usuario.
 * @author dalbuja.
 * @version 1.0
 */
public class FacesUtil{
	
	/**
	 * Retorna el mensaje de informacion en el facesContext
	 * @param mensaje - mensaje de informacion
	 */
	public static void mensajeInfo(String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,mensaje,""));
    }
	
	/**
	 * Retorna el mensaje de error en el facesContext
	 * @param mensaje - mensaje de error
	 */
    public static void mensajeError(String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,mensaje,""));
    }
    
    /**
	 * Retorna el mensaje de advertencia en el facesContext
	 * @param mensaje - mensaje de advertencia
	 */
    public static void mensajeWarn(String mensaje){
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,mensaje,""));
    }
    
    /**
  	 * Retorna la lista de roles del usuario logueado
  	 * @return lista de roles del usuario logueado
  	 */
    @SuppressWarnings("unchecked")
	public static Collection<ProveedorPermisos> obtenerPerfiles(){
    	DetalleUsuario aux = (DetalleUsuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return (Collection<ProveedorPermisos>)aux.getAuthorities();
    }
    
    /**
  	 * Retorna el usuario logueado
  	 * @return el usuario (Entity) logueado
  	 */
   	public static Usuario obtenerUsuario(){
   		DetalleUsuario aux = (DetalleUsuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       	return aux.getUsuario();
    }
   	
    /**
  	 * Elimina los mensajes encolados
  	 */
	public static void limpiarMensaje()    {
        Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
        while(msgIterator.hasNext()){
            msgIterator.next();
            msgIterator.remove();
        }
    }
}
