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
   
 ARCHIVO:     FichaDocenteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Ficha_docente.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 23-09-2016           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FichaDocenteDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDocente;

/**
 * Interface FichaDocenteServicio
 * Interfase que define las operaciones sobre la tabla Ficha_docente.
 * @author dalbuja
 * @version 1.0
 */
public interface FichaDocenteServicio {
	/**
	 * Busca una entidad FichaDocente por su id
	 * @param id - de la FichaDocente a buscar
	 * @return FichaDocente con el id solicitado
	 * @throws FichaDocenteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaDocente con el id solicitado
	 * @throws FichaDocenteException - Excepcion general
	 */
	public FichaDocente buscarPorId(Integer id) throws FichaDocenteNoEncontradoException, FichaDocenteException;
	
	public FichaDocente buscarPorIdentificacion(String identificacion)  throws FichaDocenteNoEncontradoException, FichaDocenteException;
	
	public List<FichaDocenteDto> buscarTribunalFichasDocentesXAsignacionTitulacion(Integer asttId) throws FichaDcnAsgTitulacionException, FichaDcnAsgTitulacionNoEncontradoException  ;
	
	public FichaDocente buscarXIdentificacionXCarrera(String identificacion,Integer carrera)  throws FichaDocenteNoEncontradoException, FichaDocenteException , FichaDocenteValidacionException;
	
	/**
     * Añade una FichaDocente en la BD
     * @return lista de todas las entidades FichaDocente existentes en la BD
     * @throws FichaDocenteValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
     * @throws FichaDocenteException - Excepción general
     */
    public boolean anadir(FichaDocente entidad) throws FichaDocenteValidacionException, FichaDocenteException;
    
    /**
   	 * Elimina la entidad FichaDocente a traves de su id
   	 * @param id - id de la entidad FichaDocente a eliminar
   	 * @return true si se eliminó correctamente, false de lo contrario
   	 * @throws CargoValidacionException
   	 */
   	public void eliminar(FichaDocente entidad)throws FichaDocenteValidacionException , FichaDocenteNoEncontradoException , FichaDocenteException , FichaDcnAsgTitulacionNoEncontradoException , FichaDcnAsgTitulacionException;


	Integer ingresarFichaDocente(DocenteTutorTribunalLectorJdbcDto item);
}
