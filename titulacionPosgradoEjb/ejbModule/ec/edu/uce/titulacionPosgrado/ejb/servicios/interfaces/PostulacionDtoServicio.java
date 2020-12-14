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
   
 ARCHIVO:     PostulacionDtoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre el dto de la postulacion.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018            Daniel Albuja                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PostulacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PostulacionDtoValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;



/**
 * Interface PostulacionDtoServicio
 * Interfase que define las operaciones sobre el dto de la postulacion.
 * @author dalbuja.
 * @version 1.0
 */
public interface PostulacionDtoServicio {
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public void anadir(PostulacionDto entidad, CarreraDto carreraPostulacion, Persona persona) throws PostulacionDtoValidacionException;
	
	
	/**
	 * Metodo que verifica la existencia de que una persona ya tenga una postulacion a la carrera seleccionada 
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarCarreraEnTramiteXCedula(Integer crrId, String cedula);
	
	
	/**
	 * Metodo que verifica la existencia de que una persona ya tenga una postulacion activa 
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarPostulacionActivaXIdentificacion(String cedula);
	
	
}
