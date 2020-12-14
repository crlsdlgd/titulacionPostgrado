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
   
 ARCHIVO:     ReportesDtoJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de los reportes.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018		Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteDetalleComplexivoDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEstadoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteActualizacionConocDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteEvaluadosDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteRegistradosDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteValidadosDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TotalesDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteDetalleComplexivoDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteDetalleComplexivoDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PostuladosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PostuladosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistradosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistradosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;

/**
 * Interface ReportesDtoJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de los reportes
 * @author dalbuja
 * @version 1.0
 */
public interface ReportesDtoJdbc {
	
	/**
	 * Realiza la busqueda del estado del tramite titulo y actualizacion de conocimientos.
	 * @param cnvId - id de la convocaria
	 * @param crrId - id de la carrera
	 * @return lista de postulados 
	 * @throws PostuladosDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PostuladosDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteActualizacionConocDto> listaActualizacionConocimientosXConvocatoriaCarrera(Integer cnvId,String crrId) throws PostuladosDtoJdbcException, PostuladosDtoJdbcNoEncontradoException;

	
	/**
	 * Realiza la busqueda del estado del tramite titulo y actualizacion de conocimientos.
	 * @param cnvId - id de la convocaria
	 * @param fclId - id de la facultad
	 * @return lista de postulados 
	 * @throws PostuladosDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PostuladosDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteActualizacionConocDto> listaActualizacionConocimientosXConvocatoriaFacultad(Integer cnvId,Integer fclId) throws PostuladosDtoJdbcException, PostuladosDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de la persona en la aplicacion para los reportes
	 * @return Lista de personas de la consulta
	 * @throws RegistradosDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RegistradosDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteRegistradosDto> listaPersonaXConvocatoria(String convocatoria) throws RegistradosDtoJdbcException, RegistradosDtoJdbcNoEncontradoException;

	
	/**
	 * Realiza la busqueda de la personas postuladas en la aplicacion para los reportes
	 * @return Lista de personas de la consulta
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteActualizacionConocDto> listaPostuladosXConvocatoriaFacultad(String convocatoria,String facultad) throws PostuladosDtoJdbcException, PostuladosDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza el conteo de registrados y postulados  
	 * @return Lista de resultados de totales
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TotalesDto> listaTotales(String convocatoria,String facultad) throws PostuladosDtoJdbcException, PostuladosDtoJdbcNoEncontradoException,RegistradosDtoJdbcException, RegistradosDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de la personas postuladas en la aplicacion para los reportes para secretaria
	 * @return Lista de personas de la consulta
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteActualizacionConocDto> listaPostuladosXSecretaria(String convocatoria,String carrera, Usuario usuario) throws PostuladosDtoJdbcException, PostuladosDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de la personas validadas en la aplicacion para los reportes para secretaria
	 * @return Lista de personas de la consulta
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteValidadosDto> listaValidadosXSecretaria(
			String convocatoria, String carrera, Usuario usuario)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de la personas validadas en la aplicacion para los reportes para secretaria
	 * @return Lista de personas de la consulta
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteValidadosDto> listaValidadosXDGA(
			String convocatoria, String carrera)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException ;
	
	/**
	 * Realiza la busqueda de la personas evaluados en la aplicacion para los reportes para evaluador
	 * @return Lista de personas de la consulta
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteEvaluadosDto> listaEvaluadosXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException ;
	
	
	/**
	 * Realiza la busqueda de la personas evaluados en la aplicacion para los reportes para DGA
	 * @return Lista de personas de la consulta
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ReporteEvaluadosDto> listaEvaluadosXFacultadXConvocatoriaXDGA(
			String convocatoria, String carrera)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException ;
	
	/** Método que busca los postulados no activos por la convocatoria y la facultad para los reportes de la DGA
	 * 
	 * return List<ReportePostuladosDto> Lista de Postulados
	 */
	public List<ReporteActualizacionConocDto> listaNoActivosXConvocatoriaFacultad(String convocatoria, String facultad, String cedula)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException;


	List<ReporteEvaluadosDto> listaEvaluadosExamenComplexivoXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException;


	List<ReporteEvaluadosDto> listaEvaluadosOtrosMecanismosXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException;
	
	/**Método que devuelve los resultados del examen complexivo para los directores
	 * 
	 */
	public List<EstudianteDetalleComplexivoDto> listaResultadosExamenComplexivoXCarreraXConvocatoriaXDirector(
			Integer convocatoria, String carrera, Usuario usuario)
			throws EstudianteDetalleComplexivoDtoNoEncontradoException,
			EstudianteDetalleComplexivoDtoException;


	List<EstudianteDetalleComplexivoDto> listaResultadosExamenComplexivoGraciaXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EstudianteDetalleComplexivoDtoNoEncontradoException,
			EstudianteDetalleComplexivoDtoException;


	List<EstudianteDetalleComplexivoDto> listaResultadosExamenComplexivoDGA(
			Integer convocatoria, Integer carrera, Integer tipo,
			Integer facultad)
			throws EstudianteDetalleComplexivoDtoNoEncontradoException,
			EstudianteDetalleComplexivoDtoException;

 
	List<EstudianteEstadoJdbcDto> listaPersonaEstado(Integer convocatoria, Integer facultad, String cedula)
			throws RegistradosDtoJdbcException, RegistradosDtoJdbcNoEncontradoException;
}
