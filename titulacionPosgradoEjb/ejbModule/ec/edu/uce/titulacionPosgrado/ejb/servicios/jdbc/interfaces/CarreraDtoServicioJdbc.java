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
   
 ARCHIVO:     CarreraDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla carrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;


/**
 * Interface CarreraDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla carrera.
 * @author dalbuja
 * @version 1.0
 */
public interface CarreraDtoServicioJdbc {
	
	/**
	 * Realiza la busqueda de un carrera por id
	 * @param carreraId - id del carrera
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public CarreraDto buscarXId(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarTodos() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFl(int idUsuario, String descRol, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	public List<CarreraDto> listarXTipoFormacion(int tipoFormacionId, List<CarreraDto> listaCarrerasAsignadas) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	public List<CarreraDto> listarXTipoFormacionXSede(int tipoFormacionId, int tipoSede, List<CarreraDto> listaCarrerasAsignadas) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de un carrera por id de facultad
	 * @param facultadId - id de la facultad
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> buscarXfacultad(int facultadId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de un carrera por id de codigo sniese
	 * @param cod_sniese - código sniese del carrera
	 * @return Carrera con el cod solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public CarreraDto buscarXCodSniese(String cod_sniese) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	/**
	 * Método que devuelve una lista de carreras en las que la persona se ha postulado
	 * @param cedula
	 * @return
	 * @throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException
	 */
	public EstudiantePostuladoJdbcDto buscarCarrerasPostulacionesXCedula(String cedula, int carreraId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;

	public List<CarreraDto> buscarXfacultadId(int facultadId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException;
	
	
	
}
