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
 04-AGOSTO-2016		 Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) AptitudConstantes.
 * Clase que maneja las constantes de la entidad Aptitud.
 * @author dalbuja.
 * @version 1.0
 */
public class AutoridadConstantes {
	
	// constantes para el campo de apt_requisitos
	public static final int DECANO_FACULTAD_VALUE= Integer.valueOf(0);
	public static final String DECANO_FACULTAD_LABEL = "DECANO";
	public static final int SUBDECANO_FACULTAD_VALUE = Integer.valueOf(1);
	public static final String SUBDECANO_FACULTAD_LABEL = "SUBDECANO";
	public static final int SECRETARIO_FACULTAD_VALUE = Integer.valueOf(2);
	public static final String SECRETARIO_FACULTAD_LABEL = "SECRETARIO";
	
	public static final int ESTADO_ACTIVO_VALUE= Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVO";
	
	public static final int ESTADO_INACTIVO_VALUE= Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVO";
}
