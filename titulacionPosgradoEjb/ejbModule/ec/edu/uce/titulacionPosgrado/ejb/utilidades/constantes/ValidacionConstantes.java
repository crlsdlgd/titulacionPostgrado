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
   
 ARCHIVO:     ValidacionConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad ValidacionConstantes 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/20/2018            	Daniel Albuja			           Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) ValidacionConstantes.
 * Clase que maneja las constantes de la entidad Convocatoria.
 * @author dalbuja.
 * @version 1.0
 */
public class ValidacionConstantes {
	
	// constantes para el campo de vld_culmino_malla
	public static final int SI_CULMINO_MALLA_VALUE= Integer.valueOf(0);
	public static final String SI_CULMINO_MALLA_LABEL = "SI";
	public static final int NO_CULMINO_MALLA_VALUE = Integer.valueOf(1);
	public static final String NO_CULMINO_MALLA_LABEL = "NO";
	
	// constantes para el campo de vld_reprobo_complexivo
	public static final int SI_REPROBO_COMPLEXIVO_2014_VALUE= Integer.valueOf(0);
	public static final String SI_REPROBO_COMPLEXIVO_2014_LABEL = "SI 2014";
	public static final int SI_REPROBO_COMPLEXIVO_2015_VALUE= Integer.valueOf(1);
	public static final String SI_REPROBO_COMPLEXIVO_2015_LABEL = "SI 2015";
	public static final int NO_REPROBO_COMPLEXIVO_VALUE = Integer.valueOf(2);
	public static final String NO_REPROBO_COMPLEXIVO_LABEL = "NO";
	public static final int SI_REPROBO_COMPLEXIVO_VALUE = Integer.valueOf(3);
	public static final String SI_REPROBO_COMPLEXIVO_LABEL = "SI";
	
	

	public static final int SI_REPROBO_COMPLEXIVO_2014_VALUE_REPORTES= Integer.valueOf(0);
	public static final String SI_REPROBO_COMPLEXIVO_2014_LABEL_REPORTES = "SI_2014";
	public static final int SI_REPROBO_COMPLEXIVO_2015_VALUE_REPORTES= Integer.valueOf(1);
	public static final String SI_REPROBO_COMPLEXIVO_2015_LABEL_REPORTES = "SI_2015";
	
	
	// constantes para el campo de vld_asignado_tutor
	public static final int SI_ASIGNADO_TUTOR_VALUE= Integer.valueOf(0);
	public static final String SI_ASIGNADO_TUTOR_LABEL = "SI";
	public static final int NO_ASIGNADO_TUTOR_VALUE = Integer.valueOf(1);
	public static final String NO_ASIGNADO_TUTOR_LABEL = "NO";
	
	
	// constantes para el campo de vld_ultimo_semestre
	public static final int SI_ULTIMO_SEMESTRE_VALUE= Integer.valueOf(0);
	public static final String SI_ULTIMO_SEMESTRE_LABEL = "SI";
	public static final int NO_ULTIMO_SEMESTRE_VALUE = Integer.valueOf(1);
	public static final String NO_ULTIMO_SEMESTRE_LABEL = "NO";
	
	//*****************RESULTADOS DE LA VALIDACION**************************
	
	// HOMOLOGACION
	public static final int SI_DEBE_REALIZAR_HOMOLOGACION_VALUE= Integer.valueOf(0);
	public static final String SI_DEBE_REALIZAR_HOMOLOGACION_LABEL = "DEBE REALIZAR HOMOLOGACION";
	public static final int NO_DEBE_REALIZAR_HOMOLOGACION_VALUE = Integer.valueOf(1);
	public static final String NO_DEBE_REALIZAR_HOMOLOGACION_LABEL = "NO DEBE REALIZAR HOMOLOGACION";
	
	// EXAMENEN COMPLEXIVO
	public static final int SI_DEBE_TITULARSE_UNICAMENTE_POR_EXAMEN_COMPLEXIVO_VALUE= Integer.valueOf(0);
	public static final String SI_DEBE_TITULARSE_UNICAMENTE_POR_EXAMEN_COMPLEXIVO_LABEL = "DEBE TITULARSE POR EXAMEN COMPLEXIVO";
	public static final int NO_DEBE_TITULARSE_UNICAMENTE_POR_EXAMEN_COMPLEXIVO_VALUE = Integer.valueOf(1);
	public static final String NO_DEBE_TITULARSE_UNICAMENTE_POR_EXAMEN_COMPLEXIVO_LABEL = "NO DEBE TITULARSE POR EXAMEN COMPLEXIVO";
	
	// SELECIONA MECANISMO TITULACION
	public static final int SI_DEBE_TITULARSE_POR_CUALQUIER_MECANISMO_VALUE= Integer.valueOf(0);
	public static final String SI_DEBE_TITULARSE_POR_CUALQUIER_MECANISMO_LABEL = "DEBE TITULARSE POR CUALQUIER MECANISMO";
	public static final int NO_DEBE_TITULARSE_POR_CUALQUIER_MECANISMO_VALUE = Integer.valueOf(1);
	public static final String NO_DEBE_TITULARSE_POR_CUALQUIER_MECANISMO_LABEL = "NO DEBE TITULARSE POR CUALQUIER MECANISMO";
	
	// SELECIONA OTRS MECANISMO
	public static final int SI_DEBE_TITULARSE_POR_OTROS_MECANISMO_VALUE= Integer.valueOf(0);
	public static final String SI_DEBE_TITULARSE_POR_OTROS_MECANISMO_LABEL = "DEBE TITULARSE POR OTROS MECANISMOS";
	public static final int NO_DEBE_TITULARSE_POR_OTROS_MECANISMO_VALUE = Integer.valueOf(1);
	public static final String NO_DEBE_TITULARSE_POR_OTROS_MECANISMO_LABEL = "NO DEBE TITULARSE POR OTROS MECANISMOS";
	
	// SELECIONA ACTUALIZACION CONOCIMIENTOS
	public static final int SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE= Integer.valueOf(0);
	public static final String SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_LABEL = "DEBE ACTUALIZAR CONOCIMIENTOS";
	public static final int NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE = Integer.valueOf(1);
	public static final String NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_LABEL = "NO DEBE ACTUALIZAR CONOCIMIENTOS";
	public static final int ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE = Integer.valueOf(2);
	public static final String ACTUALIZACION_CONOCIMIENTOS_PAGAR_LABEL = "PAGO DE ACTUALIZAR CONOCIMIENTOS PENDIENTE";
	public static final int ACTUALIZACION_CONOCIMIENTOS_CANCELADA_VALUE = Integer.valueOf(3);
	public static final String ACTUALIZACION_CONOCIMIENTOS_CANCELADA_LABEL = "PAGO DE ACTUALIZAR CONOCIMIENTOS REALIZADO";
	
	// TIPO DE IDONEIDAD
	
	public static final int SI_ES_IDONEO_VALUE= Integer.valueOf(0);
	public static final String SI_ES_IDONEO_LABEL = "SI ES IDONEO";
	public static final int NO_ES_IDONEO_VALUE = Integer.valueOf(1);
	public static final String NO_ES_IDONEO_LABEL = "NO ES IDONEO";
	
	
	// TIPO DE RESULTADO REPORTE
	
		public static final int HOMOLOGACION_NO_IDONEO_VALUE= Integer.valueOf(0);
		public static final String HOMOLOGACION_NO_IDONEO_LABEL = "HOMOLOGACION - NO IDONEO";
		public static final int EXAMEN_COMPLEXIVO_IDONEO_VALUE = Integer.valueOf(1);
		public static final String EXAMEN_COMPLEXIVO_IDONEO_LABEL = "EXAMEN COMPLEXIVO - IDONEO";
		public static final int ACTUALIZACION_OTROS_MECANISMOS_IDONEO_VALUE = Integer.valueOf(2);
		public static final String ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL = "ACTUALIZAR CONOCIMIENTOS, SELECCIONAR OTROS MECANISMOS - IDONEO";
		public static final int OTROS_MECANISMOS_IDONEO_VALUE = Integer.valueOf(3);
		public static final String OTROS_MECANISMOS_IDONEO_LABEL = "SELECCIONAR OTROS MECANISMOS - IDONEO";
		public static final int CONTINUA_PROCESO_ANTERIOR_NO_IDONEO_VALUE = Integer.valueOf(4);
		public static final String CONTINUA_PROCESO_ANTERIOR_NO_IDONEO_LABEL = "CONTINUA PROCESO ANTERIOR - NO IDONEO";
		public static final int SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_VALUE = Integer.valueOf(5);
		public static final String SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_LABEL = "SELECCIONAR CUALQUIER MECANISMO - IDONEO";
		public static final int NO_IDONEO_VALUE = Integer.valueOf(6);
		public static final String NO_IDONEO_LABEL = "NO IDONEO";
		public static final int ACTUALIZACION_COMPLEXIVO_IDONEO_VALUE = Integer.valueOf(7);
		public static final String ACTUALIZACION_COMPLEXIVO_IDONEO_LABEL = "ACTUALIZAR CONOCIMIENTOS, SELECCIONAR CUALQUIER MECANISMO - IDONEO";
		public static final int ACTUALIZACION_COMPLEXIVO_IDONEO_DOS_PERIODOS_VALUE = Integer.valueOf(8);
		public static final String ACTUALIZACION_COMPLEXIVO_IDONEO_DOS_PERIODOS_LABEL = "ACTUALIZAR CONOCIMIENTOS, SELECCIONAR CUALQUIER MECANISMO - IDONEO - MAS DE DOS PERIODOS ACADEMICOS";
		public static final int ACTUALIZACION_OTROS_MECANISMOS_IDONEO_DOS_PERIODOS_VALUE = Integer.valueOf(9);
		public static final String ACTUALIZACION_OTROS_MECANISMOS_IDONEO_DOS_PERIODOS_LABEL = "ACTUALIZAR CONOCIMIENTOS, SELECCIONAR OTROS MECANISMOS - IDONEO - MAS DE DOS PERIODOS ACADEMICOS";
		public static final int HOMOLOGACION_NO_IDONEO_DOS_PERIODOS_VALUE = Integer.valueOf(10);
		public static final String HOMOLOGACION_NO_IDONEO_DOS_PERIODOS__LABEL = "HOMOLOGACION - NO IDONEO - MAS DE DOS PERIODOS ACADEMICOS";
		public static final int ACTUALIZACION_CONOCIMIENTOS_OTROS_MECANISMOS_ACTIVA_DIRECTOR_VALUE = Integer.valueOf(11);
		public static final String ACTUALIZACION_CONOCIMIENTOS_OTROS_MECANISMOS_ACTIVA_DIRECTOR_LABEL = "ACTUALIZAR CONOCIMIENTOS, SELECCIONAR OTROS MECANISMOS - IDONEO - ACTIVADO DIRECTOR";
		public static final int ACTUALIZACION_COMPLEXIVO_ACTIVA_DIRECTOR_VALUE = Integer.valueOf(8);
		public static final String ACTUALIZACION_COMPLEXIVO_ACTIVA_DIRECTOR_LABEL = "ACTUALIZAR CONOCIMIENTOS, SELECCIONAR CUALQUIER MECANISMO - IDONEO - ACTIVADO DIRECTOR";
		
		// VALOR DE ID DE EXAMEN COMPLEXIVO EN TABLA MECANISMO DE TITULACION
		public static final String EXAMEN_COMPLEXIVO_LABEL= "EXAMEN COMPLEXIVO";
		
		// CANTIDAD DE MESES QUE PUEDEN TRANSCURRIR PARA QUE SE DESCARTE EL MECANISMO DE TITULACION OTROS MECANISMOS
		public static final int MESES_MAXIMO_OTROS_MECANISMOS_VALUE= Integer.valueOf(18);
		
}
