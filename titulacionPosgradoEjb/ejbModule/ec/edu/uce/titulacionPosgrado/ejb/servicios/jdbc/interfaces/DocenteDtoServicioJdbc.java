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
   
 ARCHIVO:     DocenteDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se consulta todos los datos del docente.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20-Septiembre-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.sql.SQLException;
import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;



/**
 * Interface DocenteDtoServicioJdbc.OS
 * Interface Interface donde se consulta todos los datos del docente.
 * @author dalbuja
 * @version 1.0
 */
public interface DocenteDtoServicioJdbc {
	
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public List<DocenteTutorTribunalLectorJdbcDto> buscarDocenteXIdentificacion(String identificacion,String primer_apellido) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
	
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public List<DocenteTutorTribunalLectorJdbcDto> listarCarrerasXIdentificacion(String identificacion) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
	
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public List<DocenteTutorTribunalLectorJdbcDto> listarDocenteXIdentificacionXApellidoPaterno(String identificacion, String apellido) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
			
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public DocenteTutorTribunalLectorJdbcDto buscarXIdentificacion(String identificacion) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
	
	/**
	 * @throws SQLException 
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public boolean guardarDocente(Persona persona, Integer carrera) throws SQLException;
	
	/**
	 * Método que busca un docente por identificacion y devuelve el primer registro encontrado
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public Integer buscarFcdcIdXIdentificacion(String cedula) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;

	List<DocenteTutorTribunalLectorJdbcDto> buscarDocenteXIdentificacionTitulacion(
			String identificacion, String apellido)
			throws DocenteTutorTribunalLectorJdbcDtoException,
			DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
	
}
