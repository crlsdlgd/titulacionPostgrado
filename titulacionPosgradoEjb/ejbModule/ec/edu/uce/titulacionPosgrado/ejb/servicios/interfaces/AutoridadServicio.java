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
   
 ARCHIVO:     AutoridadServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Autoridad.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 09-Mayo-2017          Diego García                          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Autoridad;

/**
 * Interface AutoridadServicio
 * Interfase que define las operaciones sobre la tabla Autoridad.
 * @author dpgarciar.
 * @version 1.0
 */
public interface AutoridadServicio {
	/**
	 * Busca una entidad Autoridad por su id
	 * @param id - de la Autoridad a buscar
	 * @return Autoridad con el id solicitado
	 * @throws AutoridadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Autoridad con el id solicitado
	 * @throws AutoridadException - Excepcion general
	 */
	public Autoridad buscarPorId(Integer id) throws AutoridadNoEncontradoException, AutoridadException;
	
	/**
	 * Lista todas las entidades Autoridad existentes en la BD
	 * @return lista de todas las entidades Autoridad existentes en la BD
	 */
	public List<Autoridad> listarTodos() throws AutoridadNoEncontradoException;

	/**
	 * Busca una entidad Autoridad por su id
	 * @param id - de la Autoridad a buscar
	 * @return Autoridad con el estado activo solicitado
	 * @throws AutoridadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Autoridad con el id solicitado
	 * @throws AutoridadException - Excepcion general
	 */
	public List<Autoridad> buscarXId(Integer idAutoridad) throws AutoridadNoEncontradoException, AutoridadException;
	
	/**
	 * Busca una entidad Autoridad por su Tipo y Estado
	 * @param tipo - de la Autoridad a buscar
	 * @param estado - de la Autoridad a buscar
	 * @param descripcion - de la facultad de la Autoridad a buscar
	 * @return Autoridad con el estado y tipo solicitado
	 * @throws AutoridadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Autoridad con el id solicitado
	 * @throws AutoridadException - Excepcion general
	 */
	public List<Autoridad> buscarXTipoXEstado(int tipo, int estado, int descripcion) throws AutoridadNoEncontradoException, AutoridadException ;
	
	/**
	 * Crea una nueva Autoridad, pone en inactivo a la otra autoridad si la hay
	 * @param Autoridad - entidad Autoridad de la que se extraera el nombre para la consulta
	 * @return la nueva autoridad 
	*/
	public Autoridad crearAutoridad(Autoridad autoridad)throws AutoridadNoEncontradoException, AutoridadException ;
	
	/**
	 * Edita una Autoridad el tipo y el estado
	 * @param Autoridad - entidad Autoridad de la que se extraera el nombre para la consulta
	 * @return utoridad editada nueva autoridad 
	*/
	public Autoridad editarAutoridad(Autoridad autoridad)throws AutoridadNoEncontradoException, AutoridadException ;
	
	/**
	 * Busca la Autoridad por Identificacion
	 * @param indentificacion - identificacion de la autordidad que se va a buscar
	 * @return entidad persona
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	public Autoridad buscarPorIdentificacion(String indentificacion)  throws AutoridadNoEncontradoException, AutoridadException;

	
}



