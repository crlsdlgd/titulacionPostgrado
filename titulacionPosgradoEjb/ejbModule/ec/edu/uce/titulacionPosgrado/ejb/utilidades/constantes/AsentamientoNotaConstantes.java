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
   
 ARCHIVO:     AsentamientoNotaConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Asentamiento_nota 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-septiembre-2016		 Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) AsentamientoNotaConstantes.
 * Clase que maneja las constantes de la entidad Asentamiento_nota.
 * @author dalbuja.
 * @version 1.0
 */
public class AsentamientoNotaConstantes {
	
	// constantes para el definir aprobacion de tutor
	public static final int SI_APROBO_TEMA_TUTOR_VALUE = Integer.valueOf(0);
	public static final String SI_APROBO_TEMA_TUTOR = "SI";
	
	public static final int NO_APROBO_TEMA_TUTOR_VALUE = Integer.valueOf(1);
	public static final String NO_APROBO_TEMA_TUTOR = "NO";
	
	public static final int NOTA_APROBACION= Integer.valueOf(14);
	
	public static final int EXAMEN_COMPLEXIVO_NORMAL_VALUE = Integer.valueOf(0);
	public static final String EXAMEN_COMPLEXIVO_NORMAL_LABEL = "EXAMEN COMPLEXIVO NORMAL";
	
	public static final int EXAMEN_COMPLEXIVO_GRACIA_VALUE = Integer.valueOf(1);
	public static final String EXAMEN_COMPLEXIVO_GRACIA_LABEL = "EXAMEN COMPLEXIVO DE GRACIA";
	
	public static final int EXAMEN_COMPLEXIVO_CORRECCION_VALUE = Integer.valueOf(2);
	public static final String EXAMEN_COMPLEXIVO_CORRECCION_LABEL = "EXAMEN COMPLEXIVO CORRECCION";
	
}
