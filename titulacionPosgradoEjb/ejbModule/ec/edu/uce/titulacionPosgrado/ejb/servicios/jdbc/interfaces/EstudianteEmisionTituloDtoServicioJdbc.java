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
   
 ARCHIVO:     EstudianteEmisionTituloDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se consulta todos los datos del estudiante para emigrar a EmisionTitulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-JULIO-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteTitulacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;



/**
 * Interface EstudianteEmisionTituloDtoServicioJdbc.
 * Interface Interface donde se consulta todos los datos del estudiante para emigrar a EmisionTitulo.
 * @author dalbuja
 * @version 1.0
 */
public interface EstudianteEmisionTituloDtoServicioJdbc {
	
	
	/**
	 * Método que devuelve todos los datos de graduacion de un estudiante
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteTitulacionJdbcDto> listarPersonas() throws PersonaDtoJdbcException;
	/**
	 * Método que devuelve todos los datos de graduacion de un estudiante que fue devuelto de emisionTitulo
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteTitulacionJdbcDto> buscarRetornadosEmisionTitulos(String cedula, Integer crrId)
			throws PersonaDtoJdbcException;
	public List<EstudianteTitulacionJdbcDto> listarPersonasEditadas()
			throws PersonaDtoJdbcException;
	public void guardarEdicion(EstudianteTitulacionJdbcDto graduado)
			throws PersonaDtoJdbcException;
	public List<EstudianteTitulacionJdbcDto> listarPersonasRevision()
			throws PersonaDtoJdbcException;
	List<EstudianteTitulacionJdbcDto> listarPersonasMigradasPorFecha() throws PersonaDtoJdbcException;
	void crearActualizarFichaEstudiante(EstudianteTitulacionJdbcDto item);
	void crearActualizarPersona(EstudianteTitulacionJdbcDto item);
	Integer buscarFcesIdXPrsIdentificacion(String identificacion) throws EstudianteActaGradoException;
	
}
