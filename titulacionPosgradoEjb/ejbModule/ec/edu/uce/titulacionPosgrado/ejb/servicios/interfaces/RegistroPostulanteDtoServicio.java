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
   
 ARCHIVO:     RegistroEstudianteDtoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre el dto de registro postulante.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 21/02/2018                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import ec.edu.uce.titulacionPosgrado.ejb.dtos.RegistroPostulanteDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoValidacionException;



/**
 * Interface RegistroEstudianteDtoServicio
 * Interfase que define las operaciones sobre el dto de registro postulante.
 * @author dalbuja.
 * @version 1.0
 */
public interface RegistroPostulanteDtoServicio {
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return boolean - TRUE si se registró exitosamente, FALSE caso contrario
	 * @throws RegistroPostulanteDtoValidacionException - lanzada cuando la validación de la entidad RegistroPostulanteDto falla.
	 * @throws RegistroPostulanteDtoException - Excepcion general.
	 */
	public boolean anadir(RegistroPostulanteDto entidad) throws RegistroPostulanteDtoValidacionException,RegistroPostulanteDtoException;
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return boolean - TRUE si se registró exitosamente, FALSE caso contrario
	 * @throws RegistroPostulanteDtoValidacionException - lanzada cuando la validación de la entidad RegistroPostulanteDto falla.
	 * @throws RegistroPostulanteDtoException - Excepcion general.
	 */
	public boolean anadirUsuarioRolFlujoCarrera(UsuarioCreacionDto entidad,  Integer usroId, Integer carreraId) throws RegistroPostulanteDtoValidacionException,RegistroPostulanteDtoException;


	boolean anadirRolFlujoCarrera(UsuarioRolJdbcDto entidad, Integer rolId,
			Integer carreraId) throws RegistroPostulanteDtoValidacionException,
			RegistroPostulanteDtoException;
	
	
	
}
