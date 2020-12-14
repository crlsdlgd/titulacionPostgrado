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
   
 ARCHIVO:     ProcesoTramiteConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad ProcesoTramite. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018			 Daniel Albuja							Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;


/**
 * Clase (constantes) ProcesoTramiteConstantes.
 * Clase que maneja las constantes de la entidad ProcesoTramite.
 * @author dalbuja.
 * @version 1.0
 */
public class ProcesoTramiteConstantes {
	//------------POSTULACION-----------
	//Constantes para el campo tipo proceso
	public static final int TIPO_PROCESO_POSTULACION_VALUE = Integer.valueOf(0);
	public static final String TIPO_PROCESO_POSTULACION_LABEL = "POSTULACIÓN";
	
	//------------VALIDACION-----------	
	public static final int TIPO_PROCESO_VALIDACION_TIPO_IDONEIDAD_VALUE = Integer.valueOf(1);
	public static final String TIPO_PROCESO_VALIDACION_TIPO_IDONEIDAD_LABEL = "VALIDACION_IDONEIDAD";
	
	//------------REVERSADO DE VALIDACION ERRONEA-----------	
	public static final int TIPO_PROCESO_VALIDACION_ERRONEA_VALUE = Integer.valueOf(2);
	public static final String TIPO_PROCESO_VALIDACION_ERRONE_LABEL = "VALIDACION ERRONEA";
	
	//------------ASIGNADO MECANISMO-----------	
	public static final int TIPO_PROCESO_ASIGNADO_MECANISMO_VALUE = Integer.valueOf(3);
	public static final String TIPO_PROCESO_ASIGNADO_MECANISMO_LABEL = "ASIGNADO MECANISMO";

	//-----------APTITUD-----------	
	public static final int TIPO_PROCESO_APTO_VALUE = Integer.valueOf(4);
	public static final String TIPO_PROCESO_APTO_LABEL = "APTO";
	
	public static final int TIPO_PROCESO_NO_APTO_VALUE = Integer.valueOf(5);
	public static final String TIPO_PROCESO_NO_APTO_LABEL = "NO APTO";
	
	public static final int TIPO_PROCESO_APTO_EVALUADO_VALUE = Integer.valueOf(6);
	public static final String TIPO_PROCESO_APTO_EVALUADO_LABEL = "APTO - EVALUADO";
	
	public static final int TIPO_PROCESO_NO_APTO_EVALUADO_VALUE = Integer.valueOf(7);
	public static final String TIPO_PROCESO_NO_APTO_EVALUADO_LABEL = "NO APTO - EVALUADO";
	
	public static final int TIPO_PROCESO_TRIBUNAL_LECTOR_PENDIENTE_VALUE = Integer.valueOf(8);
	public static final String TIPO_PROCESO_TRIBUNAL_LECTOR_PENDIENTE_LABEL = "NO APRUEBA TRIBUNAL LECTOR";
	
	//-----------ASENTAMIENTO NOTAS-----------	
	public static final int TIPO_PROCESO_COMPLEX_PRACTICO_CARGADO_VALUE = Integer.valueOf(9);
	public static final String TIPO_PROCESO_COMPLEX_PRACTICO_CARGADO_LABEL = "CARGA NOTAS EXAMEN COMPLEXIVO PRACTICO";
	
	public static final int TIPO_PROCESO_COMPLEX_CARGADO_VALUE = Integer.valueOf(10);
	public static final String TIPO_PROCESO_COMPLEX_CARGADO_LABEL = "CARGA NOTAS EXAMEN COMPLEXIVO TEORICO";
	
	public static final int TIPO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE = Integer.valueOf(11);
	public static final String TIPO_PROCESO_COMPLEX_FINAL_CARGADO_LABEL = "CARGA NOTAS EXAMEN COMPLEXIVO";
	
	public static final int TIPO_PROCESO_COMPLEX_PRACTICO_GRACIA_CARGADO_VALUE = Integer.valueOf(12);
	public static final String TIPO_PROCESO_COMPLEX_PRACTICO_GRACIA_CARGADO_LABEL = "CARGA NOTAS EXAMEN COMPLEXIVO PRACTICO DE GRACIA";
	
	public static final int TIPO_PROCESO_COMPLEX_GRACIA_CARGADO_VALUE = Integer.valueOf(13);
	public static final String TIPO_PROCESO_COMPLEX_GRACIA_CARGADO_LABEL = "CARGA NOTAS EXAMEN COMPLEXIVO TEORICO DE GRACIA";
	public static final int TIPO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE = Integer.valueOf(14);
	public static final String TIPO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_LABEL = "CARGA NOTAS EXAMEN COMPLEXIVO DE GRACIA";
	
	public static final int TIPO_PROCESO_DEFENSA_ORAL_CARGADO_VALUE = Integer.valueOf(15);
	public static final String TIPO_PROCESO_DEFENSA_ORAL_CARGADO__LABEL = "CARGA DE NOTAS DE DEFENSA ORAL";

	//-----------EMISION ACTA-----------
	public static final int TIPO_PROCESO_EMISION_ACTA_VALUE = Integer.valueOf(16);
	public static final String TIPO_PROCESO_EMISION_ACTA_LABEL = "EMISION DE ACTA DE GRADO";

	public static final int TIPO_PROCESO_LISTO_EDICION_VALUE = Integer.valueOf(17);
	public static final String TIPO_PROCESO_LISTO_EDICION_LABEL = "LISTO PARA EDICION DE ACTA DE GRADO";
	
	public static final int TIPO_PROCESO_ACTA_IMPRESA_VALUE = Integer.valueOf(18);
	public static final String TIPO_PROCESO_ACTA_IMPRESA_LABEL = "ACTA IMPRESA FINAL";
	
	public static final int TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_ACEPTA_VALUE = Integer.valueOf(19);
	public static final String TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_ACEPTA_LABEL = "ACEPTACION POR PARTE DEL ESTUDIANTE ACTUALIZAR CONOCIMIENTOS";

	public static final int TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_VALUE = Integer.valueOf(20);
	public static final String TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_LABEL = "PAGO POR PARTE DEL ESTUDIANTE ACTUALIZAR CONOCIMIENTOS";
	
	public static final int TIPO_PROCESO_RECTIFICACION_TRIBUNAL_NOTAS_VALUE = Integer.valueOf(21);
	public static final String TIPO_PROCESO_RECTIFICACION_TRIBUNAL_NOTAS_LABEL = "EDICION DE TRIBUNAL - MIEMBROS Y NOTAS";
	
	public static final int TIPO_PROCESO_MODIFICACION_COMPLEXIVO_VALUE = Integer.valueOf(22);
	public static final String TIPO_PROCESO_MODIFICACION_COMPLEXIVO_LABEL = "MODIFICACION DE NOTAS COMPLEXIVO POR PARTE DE DGA";
	
	public static final int TIPO_PROCESO_MIGRACION_EMISION_TITULO_VALUE = Integer.valueOf(23);
	public static final String TIPO_PROCESO_MIGRACION_EMISION_TITULO_LABEL = "MIGRACION A EMISION TITULO";
	
	public static final int TIPO_PROCESO_RETORNO_MIGRACION_EMISION_TITULO_VALUE = Integer.valueOf(24);
	public static final String TIPO_PROCESO_RETORNO_MIGRACION_EMISION_TITULO_LABEL = "RETORNO PARA EDICION DESDE MIGRACION A EMISION TITULO";
	
	public static final int TIPO_PROCESO_EMISION_TITULO_EDITADO_VALUE = Integer.valueOf(25);
	public static final String TIPO_PROCESO_EMISION_TITULO_EDITADO_LABEL = "RETORNO DESDE EDICION A EMISION TITULO";
	
	public static final int TIPO_PROCESO_EMISION_TITULO_EDICION_VALUE = Integer.valueOf(26);
	public static final String TIPO_PROCESO_EMISION_TITULO_EDICION_LABEL = "EDICION DE ACTA YA IMPRESA";
	
	public static final int TIPO_PROCESO_EMISION_TITULO_HABILITAR_EDICION_VALUE = Integer.valueOf(27);
	public static final String TIPO_PROCESO_EMISION_TITULO_HABILITAR_EDICION_LABEL = "HABILITAR EDICION GRADUADO DESDE EMISION TITULO";
	
	public static final int TIPO_PROCESO_ACTIVAR_ACTUALIZACION_VALUE = Integer.valueOf(28);
	public static final String TIPO_PROCESO_ACTIVAR_ACTUALIZACION_LABEL = "ACTIVAR ACTUALIZACION CONOCIMIENTOS -DIRECTOR";
	
	public static final int TIPO_PROCESO_DESACTIVAR_ACTUALIZACION_VALUE = Integer.valueOf(28);
	public static final String TIPO_PROCESO_DESACTIVAR_ACTUALIZACION_LABEL = "DESACTIVAR ACTUALIZACION CONOCIMIENTOS -DIRECTOR";
}
