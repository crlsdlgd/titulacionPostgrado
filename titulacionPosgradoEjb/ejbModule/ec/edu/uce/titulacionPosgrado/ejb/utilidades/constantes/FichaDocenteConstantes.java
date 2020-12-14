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
   
 ARCHIVO:     FichaDocenteConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad FichaDocente. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-octubre-2016		 Gabriel Mafla 			          Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) FichaDocenteConstantes.
 * Clase que maneja las constantes de la entidad FichaDocente.
 * @author dalbuja.
 * @version 1.0
 */	
public class FichaDocenteConstantes {
	
	//Constantes para tipo de duracion de la carrera
	public static final int ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ACTIVO_LABEL = "FICHA DOCENTE ACTIVA";
	public static final int INACTIVO_VALUE = Integer.valueOf(1);
	public static final String INACTIVO_LABEL = "FICHA DOCENTE INACTIVA";
	
	
}
