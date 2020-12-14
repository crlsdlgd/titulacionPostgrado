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
   
 ARCHIVO:     AutoridadDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se trabaja con la tabla Autoridad
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-12-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.AutoridadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadNoEncontradoException;



/**
 * Interface AutoridadDtoServicioJdbc.
 * Interface Interface donde se trabaja con la tabla Autoridad
 * @author dalbuja
 * @version 1.0
 */
public interface AutoridadDtoServicioJdbc {
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public List<AutoridadDto> buscarAutoridades(Integer carreraId) throws  AutoridadNoEncontradoException , AutoridadException;
	
	

	
	
}
