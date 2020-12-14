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
   
 ARCHIVO:     AptitudConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Aptitud 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018		 Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) AptitudConstantes.
 * Clase que maneja las constantes de la entidad Aptitud.
 * @author dalbuja.
 * @version 1.0
 */
public class AptitudConstantes {
	
	// constantes para el campo de apt_requisitos
	public static final int SI_CUMPLE_REQUISITOS_VALUE= Integer.valueOf(0);
	public static final String SI_CUMPLE_REQUISITOS_LABEL = "SI";
	public static final int NO_CUMPLE_REQUISITOS_VALUE = Integer.valueOf(1);
	public static final String NO_CUMPLE_REQUISITOS_LABEL = "NO";
	
	// constantes para el campo de apt_reprobo_creditos
	public static final int SI_FIN_MALLA_VALUE= Integer.valueOf(0);
	public static final String SI_FIN_MALLA_LABEL = "SI";
	public static final int NO_FIN_MALLA_VALUE = Integer.valueOf(1);
	public static final String NO_FIN_MALLA_LABEL = "NO";
	
	// constantes para el campo de apt_segunda_carrera
	public static final int SI_SUFICIENCIA_VALUE= Integer.valueOf(0);
	public static final String SI_SUFICIENCIA_LABEL = "SI";
	public static final int NO_SUFICIENCIA_VALUE = Integer.valueOf(1);
	public static final String NO_SUFICIENCIA_LABEL = "NO";
	
	// constantes para el campo de apt_aprobo_actualizar
	public static final int SI_APROBO_ACTUALIZAR_VALUE= Integer.valueOf(0);
	public static final String SI_APROBO_ACTUALIZAR_LABEL = "APROBADO";
	public static final int NO_APROBO_ACTUALIZAR_VALUE = Integer.valueOf(1);
	public static final String NO_APROBO_ACTUALIZAR_LABEL = "NO APROBADO";
	public static final String NO_APLICA_ACTUALIZAR_LABEL = "NO APLICA";
	
	
	
}
