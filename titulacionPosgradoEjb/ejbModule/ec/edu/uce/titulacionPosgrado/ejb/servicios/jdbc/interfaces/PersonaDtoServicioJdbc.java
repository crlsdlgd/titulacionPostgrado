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
   
 ARCHIVO:     PersonaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla persona.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-MAY-2016		Gabriel Mafla                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PersonaDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcNoEncontradoException;


/**
 * Interface PersonaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla persona.
 * @author dalbuja
 * @version 1.0
 */
public interface PersonaDtoServicioJdbc {

	/**
	 * Realiza la busqueda de la persona en la aplicacion
	 * @return Lista a la persona de la aplicacion
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<PersonaDto> listaPersonaXIdentificacion(String identificacion) throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException;

	

	/**
	 * Realiza la busqueda de la persona en la aplicacion que sea evaluador en la carrera buscada
	 * @return persona evaluador
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public PersonaDto buscarEvaluadorXCarrera(Integer carreraId) throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de la persona de acuerdo a su nombre y apellidos
	 * @return persona 
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean buscarPersonaXApellidosNombres(String persona) throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException;



	EstudianteValidacionJdbcDto buscarPersonaXPrsId(String identificacion, Integer crrId)
			throws EstudianteValidacionDtoException;



	PersonaDto buscarXIdentificacionEmisionTitulo(String identificacion)
			throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException;



	Integer buscarFcesIdXPrsIdentificacion(String identificacion) throws Exception ;


	
}
