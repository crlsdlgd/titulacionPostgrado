/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     UsuarioRolServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla UsuarioRol.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018				Daniel Albuja                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;

/**
 * Interface UsuarioRolServicio
 * Interfase que define las operaciones sobre la tabla UsuarioRol.
 * @author dalbuja
 * @version 1.0
 */
public interface UsuarioRolServicio {
	/**
	 * Busca una entidad UsuarioRol por su id
	 * @param id - del UsuarioRol a buscar
	 * @return UsuarioRol con el id solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorId(Integer id) throws UsuarioRolNoEncontradoException, UsuarioRolException;
	
	/**
	 * Lista todas las entidades UsuarioRol existentes en la BD
	 * @return lista de todas las entidades UsuarioRol existentes en la BD
	 */
	public List<UsuarioRol> listarTodos();
	
	/**
	 * Lista todos los roles de los usuarios existentes en la BD
	 * @param usrId -  usrId id del usuario a consultar
	 * @return - retorna la lista de todos los roles de los usuarios existentes en la BD
	 */
	public List<UsuarioRol> buscarXUsuario(int usrId);
	
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacion(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException;

	/**
	 * Busca una entidad UsuarioRol por su identificacion de editor_acta
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionEditorActa(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException;
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion  de evaluador
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionEvaluador(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException;
		
	/**
	 * Busca una entidad UsuarioRol por su identificacion  de dga
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionDga(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException;
	
	
	/**
	 * Busca una entidad UsuarioRol por su id y habilita el usuario
	 * @param id- del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public void desactivarUsuarioRolXid(Integer id, Integer usrId) throws  UsuarioRolException;

	void activarUsuarioRolXid(Integer id, Integer usrId) throws UsuarioRolException;
	
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion y en estado activo
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionActivos(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException;

	UsuarioRol buscarPorIdentificacionValidador(String identificacion)
			throws UsuarioRolNoEncontradoException, UsuarioRolException;


	UsuarioRol buscarXUsuarioXrol(int idUsuario, int idRol) throws UsuarioRolException, UsuarioRolNoEncontradoException;
}
