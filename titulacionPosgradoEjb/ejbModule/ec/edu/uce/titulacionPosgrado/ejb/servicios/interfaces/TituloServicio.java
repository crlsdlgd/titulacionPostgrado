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
   
 ARCHIVO:     TituloServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Titulo.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.RegistroSenescytDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Titulo;

/**
 * Interface TituloServicio
 * Interfase que define las operaciones sobre la tabla Titulo.
 * @author gmafla
 * @version 1.0
 */
public interface TituloServicio {
	/**
	 * Busca una entidad Titulo por su id
	 * @param id - de la Titulo a buscar
	 * @return Titulo con el id solicitado
	 * @throws TituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una Titulo con el id solicitado
	 * @throws TituloException - Excepcion general
	 */
	public Titulo buscarPorId(Integer id) throws TituloNoEncontradoException, TituloException;
	
	/**
	 * Lista todas las entidades Titulo existentes en la BD
	 * @return lista de todas las entidades Titulo existentes en la BD
	 */
	public List<Titulo> listarTodos() throws TituloNoEncontradoException;

	Titulo buscarPorDescripcion(String descripcion)
			throws TituloNoEncontradoException, TituloException;

	Titulo crearNuevoTitulo(RegistroSenescytDto nuevoTitulo) throws TituloNoEncontradoException, TituloException;

}
