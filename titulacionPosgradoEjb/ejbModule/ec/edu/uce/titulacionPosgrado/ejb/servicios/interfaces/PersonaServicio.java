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
   
 ARCHIVO:     PersonaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Persona.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018				Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;

/**
 * Interface PersonaServicio
 * Interfase que define las operaciones sobre la tabla Persona.
 * @author dalbuja
 * @version 1.0
 */
public interface PersonaServicio {
	/**
	 * Busca una entidad Persona por su id
	 * @param id - de la Persona a buscar
	 * @return Persona con el id solicitado
	 * @throws PersonaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Persona con el id solicitado
	 * @throws PersonaException - Excepcion general
	 */
	public Persona buscarPorId(Integer id) throws PersonaNoEncontradoException, PersonaException;
	
	/**
	 * Lista todas las entidades Persona existentes en la BD
	 * @return lista de todas las entidades Persona existentes en la BD
	 */
	public List<Persona> listarTodos() throws PersonaNoEncontradoException;
	
	/**
	 * Busca Persona por Identificacion
	 * @param indentificacion - identificacion de la persona que se va a buscar
	 * @return entidad persona
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	public Persona buscarPorIdentificacion(String indentificacion)  throws PersonaNoEncontradoException, PersonaException;
	
	
	/**
	 * Busca Persona por Identificacion
	 * @param indentificacion - identificacion de la persona que se va a buscar
	 * @return entidad persona
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	public List<Persona> listarPorIdentificacion(String indentificacion)  throws PersonaNoEncontradoException, PersonaException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PersonaNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws PersonaException - Excepcion general
	 */
	public Persona editar(Persona entidad) throws PersonaValidacionException, PersonaNoEncontradoException, PersonaException;
	
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PersonaNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws PersonaException - Excepcion general
	 */
	public Persona editarDocente(Persona entidad) throws PersonaValidacionException, PersonaNoEncontradoException, PersonaException;
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintMailPersona(Persona entidad, int tipo);
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintMailInstitucional(Persona entidad, int tipo);
	
	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintIdenficador(Persona entidad, int tipo);
	
	/**
     * Añade una Persona en la BD
     * @return lista de todas las entidades Persona existentes en la BD
     * @throws PersonaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
     * @throws PersonaException - Excepción general
     */
    public boolean anadir(Persona entidad) throws PersonaValidacionException, PersonaException;

	Persona editarXId(UsuarioRolJdbcDto entidad) throws PersonaValidacionException,
			PersonaNoEncontradoException, PersonaException;

	Persona buscarPersonaXRolXCarrera(Integer rolId, Integer carreraId);

	boolean actualizarFotoXPrsId(EstudianteValidacionJdbcDto estudiante)
			throws PersonaNoEncontradoException, PersonaException;

	boolean actualizarFotoXPrsIdEmisionTitulo(EstudianteValidacionJdbcDto estudiante, String ubicacion)
			throws PersonaNoEncontradoException, PersonaException;

}
