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
   
 ARCHIVO:     FichaEstudianteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla FichaEstudiante.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 21/02/2018				Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;

/**
 * Interface FichaEstudianteServicio
 * Interfase que define las operaciones sobre la tabla FichaEstudiante.
 * @author dalbuja
 * @version 1.0
 */
public interface FichaEstudianteServicio {
	/**
	 * Busca una entidad FichaEstudiante por su id
	 * @param id - de la FichaEstudiante a buscar
	 * @return FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteException - Excepcion general
	 */
	public FichaEstudiante buscarPorId(Integer id) throws FichaEstudianteNoEncontradoException, FichaEstudianteException;
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 */
	public List<FichaEstudiante> listarTodos() throws FichaEstudianteNoEncontradoException;
	
	/**
	 * Busca la ficha estudiante por id de persona
	 * @param idPersona - id de persona con el que se va a buscar
	 * @return lista ficha estudiante por id de persona
	 */
	public FichaEstudiante buscarPorIdPersona(Integer idPersona) throws FichaEstudianteNoEncontradoException,FichaEstudianteException;

	/**
	 * Actualiza la ficha_estudiante, guardando la nota final de graduación
	 * @param idPersona - id de ficha_estudiante con el que se va a buscar
	 * @return void
	 */
	public void guardarNotaFinalGraduacion(Integer fcesId,BigDecimal notaFinal, Integer trttId) throws FichaEstudianteNoEncontradoException,FichaEstudianteException;

	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD, que coincida su persona 
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD, por prs_id 
	 * @throws FichaEstudianteNoEncontradoException - FichaEstudianteNoEncontradoException Excepcion lanzada cuando no se encuentra fichas estudiante por id de trámite
	 * @throws FichaEstudianteException - FichaEstudianteException Excepción general
	 */
	public boolean buscarFichaEstudiantePorPersonaId(
			String prsIdentificacion)
			throws FichaEstudianteNoEncontradoException,
			FichaEstudianteException;
	
	public boolean actualizarFichaEstudianteFechaEgresamiento(
			Integer fcesId, Date fecha)
			throws FichaEstudianteNoEncontradoException,
			FichaEstudianteException;
	
	boolean guardarAutoridad(Integer fcesId, String autoridad, Integer sexo,
			Integer tipo) throws FichaEstudianteNoEncontradoException,
			FichaEstudianteException;
}
