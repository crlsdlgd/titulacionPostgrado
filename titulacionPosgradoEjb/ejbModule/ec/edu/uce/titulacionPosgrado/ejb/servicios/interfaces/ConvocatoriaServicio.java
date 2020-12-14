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
   
 ARCHIVO:     ConvocatoriaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Convocatoria.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018           Daniel Albuja                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;

/**
 * Interface ConvocatoriaServicio
 * Interfase que define las operaciones sobre la tabla Convocatoria.
 * @author dalbuja
 * @version 1.0
 */
public interface ConvocatoriaServicio {
	/**
	 * Busca una entidad Convocatoria por su id
	 * @param id - de la Convocatoria a buscar
	 * @return Convocatoria con el id solicitado
	 * @throws ConvocatoriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Convocatoria con el id solicitado
	 * @throws ConvocatoriaException - Excepcion general
	 */
	public Convocatoria buscarPorId(Integer id) throws ConvocatoriaNoEncontradoException, ConvocatoriaException;
	
	/**
	 * Lista todas las entidades Convocatoria existentes en la BD
	 * @return lista de todas las entidades Convocatoria existentes en la BD
	 */
	public List<Convocatoria> listarTodos() throws ConvocatoriaNoEncontradoException;

	/**
	 * Busca una entidad Convocatoria por su estado activo
	 * @param id - de la Convocatoria a buscar
	 * @return Convocatoria con el estado activo solicitado
	 * @throws ConvocatoriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Convocatoria con el id solicitado
	 * @throws ConvocatoriaException - Excepcion general
	 */
	public Convocatoria buscarPorIdPorEstado(Integer idEstado) throws ConvocatoriaNoEncontradoException, ConvocatoriaException;
	
	/**
	 * Busca una entidad Convocatoria por su id
	 * @param id - de la Convocatoria a buscar
	 * @return Convocatoria con el estado activo solicitado
	 * @throws ConvocatoriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Convocatoria con el id solicitado
	 * @throws ConvocatoriaException - Excepcion general
	 */
	public List<Convocatoria> buscarXId(Integer idConvocatoria) throws ConvocatoriaNoEncontradoException, ConvocatoriaException;
	
	/**
	 * Edita todos los atributos de la entidad Convocatoria
	 * @param entidad - entidad Convocatoria a editar
	 * @return null si no se encuentró la entidad a editar, la entidad Convocatoria editada de lo contrario
	 * @throws ConvocatoriaValidacionException - excepcion de validacion de edicion
	 */
	public Convocatoria editar(Convocatoria entidad) throws ConvocatoriaValidacionException , ConvocatoriaNoEncontradoException , ConvocatoriaException;
	
	/**
	 * Busca la Convocatoria por nombre y por tipo de operacion 0.nuevo 1.editar
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @param tipo - tipo de operacion 0.Nuevo   1.Editar
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	public Convocatoria buscarConvocatoriaXnombre(Convocatoria convocatoria, int tipo);
	
	
	/**
	 * Crea una nueva convocatoria, pone en inactivo a las demás convocatorias
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	public Convocatoria crearNuevaConvocatoria(Convocatoria convocatoria);
	/**
	 * Busca la convocatoria que se encuentre activa y en estado de registro y postulación
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	public Convocatoria buscarConvocatoriaActivaFaseRegistro();
	
	
	/**
	 * Busca la convocatoria que se encuentre activa y en estado de Idoneidad
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	public List<Convocatoria> buscarConvocatoriasActivaFaseIdoneidad();
	
	
	/**
	 * modifica la convocatoria activa y en estado de registro a fase de idoneidad
	 * @param 
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	public Convocatoria modificarEstadoFaseIdoneidad();
	
	
	/**
	 * modifica la convocatoria activa y en estado de oposicion a fase de resultados
	 * @param 
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	public Convocatoria modificarEstadoFaseResultados();
	
	

	public List<Convocatoria> listarTodosActivas() throws ConvocatoriaNoEncontradoException;

	public Convocatoria activarEstadoFaseRegistro();
	
	/**
	 * Busca la convocatoria que se encuentre activa 
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	public Convocatoria buscarConvocatoriaActiva();

	public List<Convocatoria> buscarConvocatoriasPorIdPorEstado(Integer idEstado)
			throws ConvocatoriaNoEncontradoException, ConvocatoriaException;

	List<Convocatoria> buscarConvocatoriasFaseTitulacionPorId()
			throws ConvocatoriaNoEncontradoException, ConvocatoriaException;

	Convocatoria buscarPorIdPorEstadoFaseRegistro(Integer idEstado)
			throws ConvocatoriaNoEncontradoException, ConvocatoriaException;
}
