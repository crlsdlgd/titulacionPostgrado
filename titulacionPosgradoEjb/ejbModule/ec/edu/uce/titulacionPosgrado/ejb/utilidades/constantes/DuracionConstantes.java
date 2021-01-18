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
   
 ARCHIVO:     DuracionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Duración 
 *************************************************************************
                           	
                            
 FECHA         		     AUTOR          					COMENTARIOS
 04-AGOSTO-2016		 Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) DuracionConstantes.
 * Clase que maneja las constantes de la entidad Duración.
 * @author dalbuja.
 * @version 1.0
 */
public class DuracionConstantes {
	
	// constantes para el campo de apt_requisitos duracion
	public static final int DURACION_TIPO_ANIOS_VALUE= Integer.valueOf(0);
	public static final String DURACION_TIPO_ANIOS_LABEL = "AÑOS";
	public static final int DURACION_TIPO_SEMESTRES_VALUE = Integer.valueOf(1);
	public static final String DURACION_TIPO_SEMESTRES_LABEL = "SEMESTRES";
	public static final int DURACION_TIPO_CREDITOS_VALUE = Integer.valueOf(2);
	public static final String DURACION_TIPO_CREDITOS_LABEL = "CREDITOS";
}
