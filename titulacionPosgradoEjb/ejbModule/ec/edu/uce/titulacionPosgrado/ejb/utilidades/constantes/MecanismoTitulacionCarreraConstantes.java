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
   
 ARCHIVO:     CarreraConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad Carrera. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018				Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) CarreraConstantes. Clase que maneja las constantes de la
 * entidad Carrera.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class MecanismoTitulacionCarreraConstantes {

	// Constantes para estado de mecanismo de titulacion carrera
	public static final int ACTIVO_MECANISMO_TITULACION_CARRERA_VALUE = Integer.valueOf(0);
	public static final String ACTIVO_MECANISMO_TITULACION_CARRERA_LABEL = "ACTIVO";
	public static final int INACTIVO_MECANISMO_TITULACION_CARRERA_VALUE = Integer.valueOf(1);
	public static final String INACTIVO_MECANISMO_TITULACION_CARRERA_LABEL = "INACTIVO";
	
	public static final int MECANISMO_EXAMEN__COMPLEXIVO_VALUE = Integer.valueOf(20);
	public static final String MECANISMO_EXAMEN__COMPLEXIVO_LABEL = "EXAMEN COMPLEXIVO";
	public static final String MECANISMO_OTROS_MECANISMOS_LABEL = "OTROS MECANISMOS";
	public static final String ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL = "ACTUALIZAR CONOCIMIENTOS, SELECCIONAR OTROS MECANISMOS ";
	public static final int PORCENTAJE_CIEN_POR_CIENTO_MECANISMO_TITULACION = Integer.valueOf(100);
	
	// TODO: Verificar declaracion de constantes ESADO MCTTCRR - duplicados
	public static final int ESTADO_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_ACTIVO_LABEL = "ACTIVA";
	public static final int ESTADO_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_INACTIVO_LABEL = "INACTIVA";
	
	public static final int SELECCION_DEFENSA_ESCRITA_VALUE = Integer.valueOf(0);
	public static final String SELECCION_DEFENSA_ESCRITA_LABEL = "Defensa Escrita";
	public static final int SELECCION_DEFENSA_ORAL_VALUE = Integer.valueOf(1);
	public static final String SELECCION_DEFENSA_ORAL_LABEL = "Defensa Oral";

}
