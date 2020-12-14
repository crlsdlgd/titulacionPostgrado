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
 15-JUNIO-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.sql.SQLException;
import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;



/**
 * Interface EstudiantePostuladoDtoServicioJdbc.
 * Interface Interface donde se consulta todos los datos del estudiante postulado.
 * @author dalbuja
 * @version 1.0
 */
public interface EstudianteValidacionDtoServicioJdbc {
	
	/**
	 * Método que busca un estudiante por identificacion, por carrera, por estado convocatoria
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param listCarreras - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteValidacionJdbcDto> buscarPostulacionesEstudianteXFacultadXcarreraXIndetificacionXEstadoConvocatoria(String identificacion, int idFacultad, String idCarrera, int estadoConvocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException;
			
	/**
	 * Método que busca un estudiante por identificacion y convocatoria
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteValidacionJdbcDto> buscarPostulacionesEstudianteXIndetificacion(String identificacion) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException;

	
	/**
	 * Método que busca un estudiante por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteValidacionJdbcDto> buscarPostulacionesEstudianteXIndetificacionXCarreraXConvocatoria(String identificacion, String idSecretaria, CarreraDto carreraDto, int convocatoria) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;

	/**
	 * Método que busca las carreras de acuerdo a la secretaria
	 * @param identificacion -identificacion de la secretaria
	 * @return Lista de carreras
	 */
	public List<CarreraDto> buscarCarrerasXSecretaria(String cedulaSecretaria) throws CarreraDtoJdbcNoEncontradoException, SQLException;


	/**
	 * Método que busca un estudiante validado por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesValidadosXIndetificacionXCarreraXConvocatoria(String identificacion, String idSecretaria, CarreraDto carreraDto, int convocatoria) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;

	/**
	 * Método que los datos de validacion de un estudiante validado por la secretaria
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public EstudianteValidacionJdbcDto buscarValidacionXEstudiante(EstudiantePostuladoJdbcDto item) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;

	
	/**
	 * Método que guarda la aptitud del postulante por parte del validador
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EvaluadosDtoJdbcException 
	 * @throws EvaluadosDtoJdbcNoEncontradoException 
	 */
	public Integer guardarAptitud(EstudianteAptitudJdbcDto item, String validadorIdentificacion,RolFlujoCarrera roflcrr) throws EvaluadosDtoJdbcException, EvaluadosDtoJdbcNoEncontradoException ;

	
	/**
	 * Método que guarda la edición de aptitud del postulante por parte de la DGA
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EvaluadosDtoJdbcException 
	 * @throws EvaluadosDtoJdbcNoEncontradoException 
	 */
	public Integer guardarEdicionAptitud(EstudianteAptitudJdbcDto item, String validadorIdentificacion) throws EvaluadosDtoJdbcException, EvaluadosDtoJdbcNoEncontradoException ;

	/**
	 * Método que lista los datos de validacion de un estudiante validado por la secretaria
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public EstudianteValidacionJdbcDto buscarValidacionXEstudianteIdoneidad(EstudiantePostuladoJdbcDto item) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException;
	
	/**
	 * Método que busca la lista de estudiantes validados por identificacion y convocatoria de acuerdo a su carrera y que deben actualizar conocimientos
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesValidadosXIndetificacionXCarreraXConvocatoriaActualizar(
			String identificacion, String idSecretaria) throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException;
	
	public List<EstudianteValidacionJdbcDto> buscarValidacionXEstudianteActualizar(String cedula) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException ;

	public List<EstudianteValidacionJdbcDto> buscarAptitudActualizacionConocimientos(
			String cedula) throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException;

	public List<EstudianteValidacionJdbcDto> buscarEstudiantesAptitudXIndetificacionXCarreraXConvocatoriaActualizar(
			String identificacion, String idSecretaria)
			throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException;

	public List<EstudianteValidacionJdbcDto> buscarEstudiantesProcesoCambioActualizacion();

	public List<EstudianteValidacionJdbcDto> buscarEstudiantesProcesoCambioActualizacionProcesoInactivo();

	public List<EstudianteValidacionJdbcDto> buscarEstudiantesProcesoCambioActualizacionRevision();

	public List<EstudianteValidacionJdbcDto> buscarEstudiantesValidadosXIndetificacionXCarreraXConvocatoria(
			String identificacion, String idSecretaria)
			throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException;

}
