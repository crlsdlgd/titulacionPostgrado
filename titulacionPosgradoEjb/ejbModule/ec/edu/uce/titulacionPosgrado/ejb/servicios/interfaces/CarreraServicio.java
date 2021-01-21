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
   
 ARCHIVO:     CarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018          Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;

/**
 * Interface CarreraServicio
 * Interfase que define las operaciones sobre la tabla Carrera.
 * @author dalbuja
 * @version 1.0
 */
public interface CarreraServicio {
	/**
	 * Busca una entidad Carrera por su id
	 * @param id - de la Carrera a buscar
	 * @return Carrera con el id solicitado
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws CarreraException - Excepcion general
	 */
	public Carrera buscarPorId(Integer id) throws CarreraNoEncontradoException, CarreraException;
	
	/**
	 * Lista todas las entidades Carrera existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 */
	public List<Carrera> listarTodos() throws CarreraNoEncontradoException;

	/**
	 * Lista todas las entidades Carrera existentes en la BD que pertenecen a una facultad
	 * @return lista de todas las entidades Carrera existentes en la BD de una facultad
	 */
	public List<CarreraDto> listarCarrerasXFacultad(FacultadDto facultadDto) throws CarreraNoEncontradoException;

	public List<Carrera> listarCarrerasXFacultad(Facultad facultad)throws CarreraNoEncontradoException;

	public boolean ifExistEspeCodigo(Integer valorSt) throws Exception;
	
	
	
}
