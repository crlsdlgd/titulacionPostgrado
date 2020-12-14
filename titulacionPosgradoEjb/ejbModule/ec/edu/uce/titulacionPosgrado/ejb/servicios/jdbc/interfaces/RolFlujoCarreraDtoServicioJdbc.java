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
   
 ARCHIVO:     TituloDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla titulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26-MAYO-2016		David Arellano				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.RolDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolNoEncontradoException;

/**
 * Interface TituloDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Titulo.
 * @author dalbuja
 * @version 1.0
 */
public interface RolFlujoCarreraDtoServicioJdbc  {
	
	/**
	 * Realiza la busqueda de un rol por cédula
	 * @param String cedula - cedula de la persona
	 * @return RolDto 
	 * @throws RolException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RolNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public  RolDto buscarRolXCedula(String cedula) throws RolException, RolNoEncontradoException;
	
}
