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
   
 ARCHIVO:     MecanismoTitulacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla MecanismoTitulacion.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 24-04-2018           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;

/**
 * Interface MecanismoTitulacionServicio Interfase que define las operaciones
 * sobre la tabla MecanismoTitulacion.
 * 
 * @author dalbuja
 * @version 1.0
 */
public interface MecanismoTitulacionCarreraServicio {
	/**
	 * Busca una entidad MecanismoTitulacion por su id 
	 * @param id - de la MecanismoTitulacion a buscar
	 * @return MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionNoEncontradoException - Excepcion lanzada cuando no se encuentra un MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionException - Excepcion general
	 */
	public MecanismoCarrera buscarPorId(Integer id) throws MecanismoTitulacionCarreraNoEncontradoException,
			MecanismoTitulacionCarreraException;

	/**
	 * Lista todas las entidades MecanismoTitulacion existentes en la BD
	 * @return lista de todas las entidades MecanismoTitulacion existentes en la  BD
	 */
	public List<MecanismoCarrera> listarTodos()
			throws MecanismoTitulacionCarreraNoEncontradoException;

	public List<MecanismoCarrera> listaMccttXcarrera(Integer crrId)
			throws MecanismoTitulacionCarreraNoEncontradoException,
			MecanismoTitulacionCarreraException;

	
	/**
	 * Actualiza el porcentaje del examen complexivo de la entidad
	 * Mecanismo_titulacion_carrera
	 * @return
	 */
	public void actualizaPorcentaje(MecanismoTitulacionCarreraDto mcttcrDto)
			throws MecanismoTitulacionCarreraNoEncontradoException,
			MecanismoTitulacionCarreraException;
	
	/**
	 * Busca una lista de Mecanismos de Titulación por el ID de la Carrera
	 * @param id de la carrera
	 * @return Lista de mecanismos de titulacion por Carrera
	 * @throws MecanismoTitulacionCarreraNoEncontradoException
	 * @throws MecanismoTitulacionCarreraException
	 */
	public List<MecanismoCarrera> listaMccttXcarreraSinEstado(Integer crrId)
			throws MecanismoTitulacionCarreraNoEncontradoException,	MecanismoTitulacionCarreraException;

	/**
     * Guarda un Mecanismos de Titulación en la BD
     * @param entidad -> tipo MecanismoTitulacionCarrera, dato para guardar en db.
     * @return entidad Mecanismos de Titulación nula si el dato de entrada es NULL, caso contrario entidad guardada en db. 
     * @throws MecanismoTitulacionCarreraException - Excepción general
     */
	public MecanismoCarrera guardar(MecanismoCarrera entidad) throws MecanismoTitulacionCarreraException;

	MecanismoCarrera buscarXMecanismoTitulacionXCarrera(
			Integer crrId, Integer mcttId)
			throws MecanismoTitulacionCarreraNoEncontradoException,
			MecanismoTitulacionCarreraException;

	
}
