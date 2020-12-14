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
   
 ARCHIVO:     UsuarioRolDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla Usuario_Rol
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-04-2018			Daniel Albuja 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;


/**
 * Interface UsuarioRolDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Usuario_Rol.
 * @author dalbuja
 * @version 1.0
 */
public interface UsuarioRolDtoServicioJdbc {
	/**
	 * Realiza la busqueda de una lista de usuario_rol por cédula, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarrea(String identificacion, int facultadId,int carreraId) throws UsuarioRolJdbcDtoException, UsuarioRolJdbcDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por cédula, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarCarrerasXIdentificacion(String identificacion) throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraXRol(
			int rolId, int facultadId, int carreraId)throws UsuarioRolJdbcDtoException,UsuarioRolJdbcDtoNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una lista de usuario por identificacion
	 * @param String identificacion - cadena de identificacion
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UsuarioRolJdbcDto buscarUsuariosXIdentificacion(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException ;
}
