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
   
 ARCHIVO:     EstudiantePostuladoDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se consulta todos los datos del estudiante postulado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/08/2018				Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;



/**
 * Interface EstudiantePostuladoDtoServicioJdbc.
 * Interface Interface donde se consulta todos los datos del estudiante postulado.
 * @author dalbuja
 * @version 1.0
 */
public interface EstudiantePostuladoDtoServicioJdbc {
	
	/**
	 * Método que busca un estudiante por identificacion, por carrera, por estado convocatoria
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param listCarreras - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulacionesEstudianteXFacultadXcarreraXIndetificacionXEstadoConvocatoria(String identificacion, int idFacultad, String idCarrera, int estadoConvocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException;
			
	/**
	 * Método que busca un estudiante por identificacion y convocatoria
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulacionesEstudianteXIndetificacion(String identificacion) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException;

	/**
	 * Método que busca las postulaciones de un estudiante por identificacion, por carrera, por estado convocatoria
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param listCarreras - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulacionesVistaEstudiante(String identificacion, int idFacultad, String idCarrera, int estadoConvocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de del tipo proceso tramite por idendtificacion
	 * @param identificacion - id de la facultad
	 * @return EstudiantePostuladoJdbcDto con el idendtificacion solicitado 
	 * @throws EstudiantePostuladoJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public EstudiantePostuladoJdbcDto buscaTipoTramiteXIdentificacion(String identificacion) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException;
	
	
	/**
	 * Método que busca las postulaciones de un estudiante por identificacionpor facultad y convocatoria
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulantesParaDesactivar(String identificacion, int idFacultad, int convocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;
	
	/**
	 * Método que busca las postulaciones de un estudiante por identificacion por facultad y convocatoria
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulantesParaValidar( int idFacultad, int idConvocatoria , int idCarrera  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;
	
	/**
	 * Método que busca si culminó la malla un estudiante por identificacion por carrera
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public EstudiantePostuladoJdbcDto buscarCulminoMalla( String identificacion, int idConvocatoria , int idCarrera  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;
	
	
	/**
	 * Método que busca las fechas requeridas para aptitud de un estudiante por identificacion por carrera
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public EstudiantePostuladoJdbcDto buscarFechaEgresamiento( String identificacion, int idConvocatoria , int idCarrera  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;
	
	
	/**
	 * Método que busca las fechas requeridas para aptitud de un estudiante por identificacion por carrera
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public EstudiantePostuladoJdbcDto buscarFechaAsignacionMecanismo( String identificacion, int idConvocatoria , int idCarrera  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;

	public List<EstudiantePostuladoJdbcDto> buscarPostulantesParaDesactivarPorCarrera(
			String identificacion, int idCarrera, int convocatoria, String idEvaluador)
			throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException;
	
}
