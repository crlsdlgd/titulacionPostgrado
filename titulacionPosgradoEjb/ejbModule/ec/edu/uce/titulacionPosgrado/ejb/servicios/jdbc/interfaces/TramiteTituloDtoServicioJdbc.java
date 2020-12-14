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
   
 ARCHIVO:     TramiteTituloDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla TramiteTitulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018		Daniel Albuja 							Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TramiteTituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloNoEncontradoException;


/**
 * Interface TramiteTituloDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla TramiteTitulo.
 * @author dalbuja
 * @version 1.0
 */
public interface TramiteTituloDtoServicioJdbc {

	/**
	 * Actualiza la entidad TramiteTitulo para los estudiantes validados
	 * @param EstudiantePostuladoJdbcDto - estudiante a editar
	 * @return Boolean 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean actualizarTramiteTituloValidadosDinardapXId(EstudiantePostuladoJdbcDto entidad) throws TramiteTituloException, TramiteTituloNoEncontradoException;
	
	/**
	 * Devuelve el tramiteTitulo del postulante por identificación y por carrera
	 * @param String cedula - estudiante a buscar
	 * @param int carreraId - carrera del tramiteTitulo
	 * @return TramiteTituloDto 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public TramiteTituloDto buscarTramiteTituloXCedulaXCarrera(String cedula) throws TramiteTituloException, TramiteTituloNoEncontradoException;
	
	
	
	/**
	 * Actualiza la entidad TramiteTitulo desactivando el trtt
	 * @param Integer trttId - id de trtt
	 * @return Boolean 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean desactivarTramiteTituloXId(Integer trttId) throws TramiteTituloException, TramiteTituloNoEncontradoException;
	
	
	/**
	 * Busca el tramite_titulo desactivado de la cédula ingresada, si reprobó examen complexivo
	 * @param String cedula
	 * @return Integer 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Integer burcarTramiteTituloDesactivadoReproboComplex(String cedula) throws TramiteTituloException, TramiteTituloNoEncontradoException;
	
	/**
	 * Busca el tramite_titulo de la cédula ingresada, si ya pasó por la validación por la carrera seleccionada
	 * @param String cedula, int carreraId
	 * @return Integer 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Integer buscarTramiteTituloValidadoXCarrera(String cedula, int carreraId) throws TramiteTituloException, TramiteTituloNoEncontradoException;
	
	
}
