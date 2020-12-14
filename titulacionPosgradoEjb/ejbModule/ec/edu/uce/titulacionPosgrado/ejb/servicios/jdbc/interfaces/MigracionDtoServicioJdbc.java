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

import java.sql.Connection;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.PersonaDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcNoEncontradoException;


/**
 * Interface PersonaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla persona.
 * @author dalbuja
 * @version 1.0
 */
public interface MigracionDtoServicioJdbc {







	PersonaDto buscarXIdentificacionEmisionTitulo(String identificacion, Connection con)
			throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException;



	Integer buscarFcesIdXPrsIdentificacion(String identificacion, Integer cncrId) throws Exception;


	
}
