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
   
 ARCHIVO:     InstitucionAcademicaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla institucion_academica.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaDtoJdbcNoEncontradoException;


/**
 * Interface InstitucionAcademicaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla institucion_academica.
 * @author dalbuja
 * @version 1.0
 */
public interface InstitucionAcademicaDtoServicioJdbc {
	
	/**
	 * Realiza la busqueda de una institucion academica por id
	 * @param instAcademicaId - id del institucion academica
	 * @return Institucion academica con el id solicitado 
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public InstitucionAcademicaDto buscarXId(int instAcademicaId) throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todas las instituciones academicas de la aplicacion
	 * @return Lista todas las instituciones academicas de la aplicacion
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarTodos() throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de las instituciones academicas por nivel, tipo y descripcion
	 * @param nivel - nivel de la institucion
	 * @param niveles- niveles de la institucion
	 * @param tipo - tipo de la institucion
	 * @param tipos - tipos de la institucion
	 * @param ubicacionId - id de la unicacion de la institucion
	 * @param ubicaciones - ubicaciones de la institucion
	 * @param descripcion - descripcion o nombre de la institucion
	 * @return list de las instituciones academicas por nivel, tipo y descripcion
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarXNivelXTipoXUbicacionXDescripcion(int nivel, List<Integer> niveles, int tipo, List<Integer> tipos
			                             , int ubicacionId, List<UbicacionDto>  ubicaciones, String descripcion) throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de una institucion academica por Nivel
	 * @param Nivel - Nivel de la institucion academica
	 * @return Lista de Institucion academica con el nivel solicitado 
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public  List<InstitucionAcademicaDto> listarXNivel(int nivel) throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException;


}
