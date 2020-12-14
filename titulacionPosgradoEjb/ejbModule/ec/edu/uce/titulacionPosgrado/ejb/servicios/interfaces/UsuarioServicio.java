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
   
 ARCHIVO:     UsuarioServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Usuario.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018				Daniel Albuja                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;

/**
 * Interface UsuarioServicio
 * Interfase que define las operaciones sobre la tabla Usuario.
 * @author dalbuja
 * @version 1.0
 */
public interface UsuarioServicio {
	/**
	 * Busca una entidad Usuario por su id
	 * @param id - del Usuario a buscar
	 * @return Usuario con el id solicitado
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada cuando no se encuentra un Usuario con el id solicitado
	 * @throws UsuarioException - Excepcion general
	 */
	public Usuario buscarPorId(Integer id) throws UsuarioNoEncontradoException, UsuarioException;
	
	/**
	 * Lista todas las entidades Usuario existentes en la BD
	 * @return lista de todas las entidades Usuario existentes en la BD
	 */
	public List<Usuario> listarTodos();
	
	/**
	 * Busca a un usuario por su nick
	 * @param nickName - nick con el que se va a buscar
	 * @return usuario con el nick solicitado
	 * @throws UsuarioNoEncontradoException, UsuarioException 
	 */
	public Usuario buscarPorNick(String nickName) throws UsuarioNoEncontradoException, UsuarioException;
	
	/**
	 * Busca a un usuario por su Identificación
	 * @param identificacion - identificacion con la que se va a buscar
	 * @return usuario con la identificacion solicitado 
	 */
	public List<Usuario> buscarPorIndentificacion(String identificacion);
	
	
	/**
	 * Busca a un usuario por su Identificación
	 * @param identificacion - identificacion con la que se va a buscar
	 * @return usuario con la identificacion solicitado 
	 */
	public Usuario buscarIndentificacion(String identificacion);
	
	/**
	 * Busca a un usuario por su correo
	 * @param correo - correo con el que se va a buscar
	 * @return usuario con el correo solicitado
	 * @throws UsuarioNoEncontradoException 
	 * @throws UsuarioException 
	 */
	public Usuario buscarPorCorreo(String correo) throws UsuarioNoEncontradoException, UsuarioException;

	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintIdenficador(Usuario entidad, int tipo);
	
	/**
	 * Metodo que verifica la existencia de un nick en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintNick(Usuario entidad, int tipo);
	
	/**
	 * Actualizar clave de usuario y correo de persona del usuario
	 * @param idUsuario
	 * @param clave
	 * @param idPersona
	 * @param mailPersonal
	 * @throws UsuarioValidacionException
	 */
	public void actualizarClaveUsuarioMailPersona(int idUsuario, String clave, int idPersona, String mailPersonal) throws UsuarioValidacionException, UsuarioException;
	
	/**
	 * Actualiza el password de la entidad
	 * @param entidad - entidad a actualizar
	 * @return la entidad editada
	 * @throws UsuarioValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada si no se encontro la entidad a actualizar
	 * @throws UsuarioException - Excepcion general
	 */
	public Usuario actualizaPassword(Usuario entidad) throws UsuarioValidacionException, UsuarioNoEncontradoException, UsuarioException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws UsuarioValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws UsuarioException - Excepcion general
	*/
	public Usuario editar(Usuario entidad) throws UsuarioValidacionException, UsuarioNoEncontradoException, UsuarioException;
	
}
