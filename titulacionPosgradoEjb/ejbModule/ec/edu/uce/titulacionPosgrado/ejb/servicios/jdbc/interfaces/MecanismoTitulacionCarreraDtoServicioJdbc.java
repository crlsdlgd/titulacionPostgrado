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
   
 ARCHIVO:     MecanismoTitulacionCarreraDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla mecanismo_titulacion_carrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 03-AGO-2016		Daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionDtoJdbcNoEncontradoException;

/**
 * Interface MecanismoTitulacionCarreraDtoServicioJdbc. Interface donde se
 * registran los metodos para el servicio jdbc de la tabla
 * mecanismo_titulacion_carrera.
 * 
 * @author dalbuja
 * @version 1.0
 */
public interface MecanismoTitulacionCarreraDtoServicioJdbc {
	/**
	 * Realiza la busqueda de un mecanismo de titulacion de carrera por id
	 * 
	 * @param mecTitulId
	 *            - id del mecanismo de titulacion carrera
	 * @return Mecanismo de titulacion con el id solicitado
	 * @throws MecanismoTitulacionDtoJdbcException
	 *             - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionDtoJdbcNoEncontradoException
	 *             - Lanzada cuando la busqueda no retorna resultados
	 */
	public MecanismoTitulacionCarreraDto buscarXId(int mecTitulId)
			throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de todos los mecanismos de titulacion de carrera de
	 * la aplicacion
	 * 
	 * @return Lista todos los mecanismos de titulacion carrera de la aplicacion
	 * @throws MecanismoTitulacionDtoJdbcException
	 *             - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionDtoJdbcNoEncontradoException
	 *             - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MecanismoTitulacionCarreraDto> listarTodos()
			throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de un mecanismo de titulacion carrera por id de la
	 * carrera
	 * 
	 * @param mecTitulId
	 *            - id del mecanismo de titulacion carrera
	 * @return Mecanismo de titulacion con el id solicitado
	 * @throws MecanismoTitulacionCarreraDtoJdbcException
	 *             - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionCarreraDtoJdbcNoEncontradoException
	 *             - Lanzada cuando la busqueda no retorna resultados
	 */
	public MecanismoTitulacionCarreraDto buscarXCarreraMecanismoExamenComplexivo(
			int carreraId) throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException;

	/**
	 * Busca una lista de Mecanismos de Titulación por el ID de la Facultad
	 * 
	 * @param id de la facultad
	 * @return Lista de mecanismos de titulacion por Facultad
	 * @throws MecanismoTitulacionCarreraNoEncontradoException
	 * @throws MecanismoTitulacionCarreraException
	 */
	public List<MecanismoTitulacionCarreraDto> listar( Integer crrIds) throws MecanismoTitulacionCarreraNoEncontradoException, MecanismoTitulacionCarreraException;

	public Integer buscarOpcionMecanismoXfcesId(int fcesId)
			throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException;

}
