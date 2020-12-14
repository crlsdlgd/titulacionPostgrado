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
   
 ARCHIVO:     EstudianteAsignacionDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se consulta todos los datos del estudiante por asignar
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.sql.SQLException;
import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAsignacionMecanismoDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteDetalleComplexivoDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;



/**
 * Interface EstudianteAsignacionDtoServicioJdbc.
 * Interface Interface donde se consulta todos los datos del estudiante por asignar
 * @author dalbuja
 * @version 1.0
 */
public interface EstudianteAsignacionDtoServicioJdbc {
	
	/**
	 * Método que busca las carreras de acuerdo al Evaluador
	 * @param identificacion -identificacion del Evaluador
	 * @return Lista de carreras
	 */
	public List<CarreraDto> buscarCarrerasXEvaluador(String cedulaSecretaria) throws CarreraDtoJdbcNoEncontradoException, SQLException;

	/**
	 * Método que busca la lista de estudiantes validados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteAsignacionMecanismoDto> buscarEstudiantesXAsignarXIndetificacionXCarreraXConvocatoria(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EstudianteValidacionDtoException,
			EstudianteValidacionDtoNoEncontradoException;
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public boolean editar(EstudianteAsignacionMecanismoDto entidad, String identificacionEvaluador, RolFlujoCarrera rolFlujoCarrera, MecanismoCarrera mecanismo) throws  EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException;
	
	/**
	 * Método que busca la lista de estudiantes evaluados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteAsignacionMecanismoDto> buscarEstudiantesEvaluadosXIndetificacionXCarreraXConvocatoria(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EstudianteValidacionDtoException,
			EstudianteValidacionDtoNoEncontradoException;
	
	/**
	 * Actualiza la nueva entidad indicada en la tabla asignacion_titulacion 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public boolean editarReasignar(EstudianteAsignacionMecanismoDto entidad, String identificacionEvaluador, RolFlujoCarrera rolFlujoCarrera, MecanismoCarrera mecanismo) throws  EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException;
	
	/**
	 * Método que busca la lista de estudiantes asignados mecanismos por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteAptitudJdbcDto> buscarEstudiantesAsignadoMecanismosXEstudianteXValidadorXCarreraXconvocatoria(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException ;
	
	
	/**PARA APTITUD OTROS MECANISMOS
	 * Método que busca la lista de estudiantes evaluados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarEstudiantesAsignadoMecanismosOtrosMecanismos(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException;
	
	
	/**PARA APTITUD EDICION DGA
	 * Método que busca la lista de estudiantes evaluados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteAptitudJdbcDto> buscarEstudiantesAsignadoMecanismosDGA(
			String identificacion, String idSecretaria, FacultadDto facultadDto,
			int convocatoria) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException ;
	
	/**
	 * Método que devuelve una lista de estudiantes por carrera que tenga complexivo y convocatoria
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	public List<EstudianteDetalleComplexivoDto> listarEstudiantesXIdCarreraXComplexivoXConvocatoria(int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException;

	
	/**
	 * Método que devuelve una lista de estudiantes por carrera que tenga complexivo de gracia y convocatoria
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	List<EstudianteDetalleComplexivoDto> listarEstudiantesXIdCarreraXComplexivoGraciaXConvocatoria(
			int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException,
			EstudianteAsignacionDtoException;

	
	/**
	 * Método que devuelve una lista de estudiantes por carrera que tenga complexivo práctico y convocatoria
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	List<EstudianteDetalleComplexivoDto> listarEstudiantesAptosXIdCarreraXComplexivoXConvocatoria(
			int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException,
			EstudianteAsignacionDtoException;

	/**
	 * Método que devuelve una lista de estudiantes por carrera que hayan sido declarados su aptitud
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	List<EstudianteAptitudJdbcDto> listarEstudiantesAptosNoAptosXIdCarreraXConvocatoria(
			String cedula, String cedulaEvaluador,int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException,
			EstudianteAsignacionDtoException;
	/**
	 * Método que devuelve una lista de estudiantes por carrera que hayan sido declarados su aptitud en otros mecanismos
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	public List<EstudianteAptitudJdbcDto> listarEstudiantesAptosNoAptosXIdCarreraXConvocatoriaOtrosMecanismos(
			String cedula, String cedulaEvaluador,int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException;
	
}
