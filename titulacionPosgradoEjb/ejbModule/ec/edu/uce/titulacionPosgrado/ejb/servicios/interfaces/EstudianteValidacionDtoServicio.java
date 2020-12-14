/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     EstudianteValidacionDtoServicio.java      
 DESCRIPCIÓN: Interface que define las operaciones sobre el proceso de validación  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 26/02/2018            Daniel Albuja                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;



/**
 * Interface EstudianteValidacionDtoServicio
 * Interfase que define las operaciones sobre el dto de la postulacion.
 * @author d.
 * @version 1.0
 */
public interface EstudianteValidacionDtoServicio {
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public boolean editar(EstudianteValidacionJdbcDto entidad, String identificacionSecretaria, Integer idCarrera , RolFlujoCarrera rolFlujoCarrera ) throws EstudianteValidacionDtoNoEncontradoException , EstudianteValidacionDtoException;

	
	/**
	 * Modifica el registro de actualización de conocimientos para proceder con el pago
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public boolean registrarActualizarConocimientoPago(Integer vldId, Integer trttId, String comprobante, Integer roflcrId)
			throws EstudianteValidacionDtoNoEncontradoException,
			EstudianteValidacionDtoException;


	public void modificarActualizacionConocimientos(Integer vldId, Integer trttId);


	public void modificarHomologación(Integer vldId, Integer trttId);


	public void retirarActualizacionConocimientos(Integer vldId, Integer trttId);



	public boolean modificarActualizacionConocimientos(
			EstudianteValidacionJdbcDto entidad,
			RolFlujoCarrera rolFlujoCarrera, String observacion)
			throws EstudianteValidacionDtoNoEncontradoException,
			EstudianteValidacionDtoException;


	public boolean guardarRegistroSenescyt(int fcesId, String registro)
			throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException;


	boolean registrarActualizarConocimientoPago(Integer vldId, Integer trttId, String comprobante, Integer roflcrId,
			String identificacion)
			throws EstudianteValidacionDtoNoEncontradoException, EstudianteValidacionDtoException;
	
	
	
	
	
}
