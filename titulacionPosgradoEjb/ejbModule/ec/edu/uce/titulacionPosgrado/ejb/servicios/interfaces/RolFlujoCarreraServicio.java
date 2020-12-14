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
   
 ARCHIVO:     RolFlujoCarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla RolFlujoCarrera.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;

/**
 * Interface RolFlujoCarreraServicio
 * Interfase que define las operaciones sobre la tabla RolFlujoCarrera.
 * @author dalbuja
 * @version 1.0
 */
public interface RolFlujoCarreraServicio {
	/**
	 * Busca una entidad RolFlujoCarrera por su id
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	public RolFlujoCarrera buscarPorId(Integer id) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	/**
	 * Lista todas las entidades RolFlujoCarrera existentes en la BD
	 * @return lista de todas las entidades RolFlujoCarrera existentes en la BD
	 */
	public List<RolFlujoCarrera> listarTodos() throws RolFlujoCarreraNoEncontradoException;
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	public RolFlujoCarrera buscarPorCarrera(Integer carrera, Integer usuarioId, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	

	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	public RolFlujoCarrera buscarPorCarreraXUsuarioDGA(Integer carrera) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id de usuario
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return List<RolFlujoCarrera> con el id solicitado
	 */
	public List<RolFlujoCarrera> buscarPorIdUsuario(Usuario usuario) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	
	/**
	 * Desactiva el rol_flujo_carrera de acuerdo al roflcr_id
	 * @param roflcrId - id de rol_flujo_carrera
	 * @return boolean 
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	public boolean desactivarRolFlujoCarreraXUsuarioRol(Integer roflcrId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException;
	
	
	/**
	 * Activa el rol_flujo_carrera de acuerdo al roflcr_id
	 * @param roflcrId - id de rol_flujo_carrera
	 * @return boolean 
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	public boolean activarRolFlujoCarreraXUsuarioRol(Integer roflcrId, Integer rolId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException,
		RolFlujoCarreraValidacionException;
		
	/**
	 * Desactiva la lista de rol_flujo_carreras de acuerdo al usuario_rol
	 * @param roflcrId - id de rol_flujo_carrera
	 * @return boolean 
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	public boolean desactivarRolFlujoCarrerasXListaUsuarioRol(Integer usroId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException;

	public RolFlujoCarrera buscarPorCarreraXUsuarioSecretaria(Integer carrera)
			throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;

	List<RolFlujoCarrera> listarCarrerasXUsroId(int idUsuarioRol)
			throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException;
	
	
		
}
