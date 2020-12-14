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
   
 ARCHIVO:     TramiteTituloConstantes.java	  
 DESCRIPCION: Clase que maneja las constantes de la entidad TramiteTitulo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018				Daniel Albuja							Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

/**
 * Clase (constantes) TramiteTituloConstantes. Clase que maneja las constantes
 * de la entidad TramiteTitulo.
 * 
 * @author dalbuja.
 * @version 1.0
 */
public class TramiteTituloConstantes {

	

	// Constantes para el campo estado_tramite
	public static final int ESTADO_TRAMITE_ACTIVO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_TRAMITE_ACTIVO_LABEL = "ACTIVO";
	public static final int ESTADO_TRAMITE_INACTIVO_VALUE = Integer.valueOf(1);
	public static final String ESTADO_TRAMITE_INACTIVO_LABEL = "INACTIVO";
	public static final int ESTADO_TRAMITE_VALIDADO_VALUE = Integer.valueOf(2);
	public static final String ESTADO_TRAMITE_VALIDADO_LABEL = "VALIDADO DINARDAP";
	public static final int ESTADO_TRAMITE_NO_VALIDADO_VALUE = Integer.valueOf(3);
	public static final String ESTADO_TRAMITE_NO_VALIDADO_LABEL = "NO VALIDADO DINARDAP";

	// Constantes para el campo estado_proceso
	public static final int ESTADO_PROCESO_POSTULADO_VALUE = Integer.valueOf(0);
	public static final String ESTADO_PROCESO_POSTULADO_LABEL = "POSTULADO";

	public static final int EST_PROC_VALIDADO_VALUE = Integer.valueOf(1);
	public static final String EST_PROC_VALIDADO_LABEL = "VALIDADO";

	public static final int EST_PROC_ASIGNADO_REVERSADO_VALUE = Integer.valueOf(2);
	public static final String EST_PROC_REVERSADO_LABEL = "REVERSADO";

	public static final int EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE = Integer.valueOf(3);
	public static final String EST_PROC_ASIGNADO_MECANISMO_TITULACION_LABEL = "ASIGNADO MECANISMO TITULACION";

	// public static final int ESTADO_PROCESO_EVALUADO_VALUE =
	// Integer.valueOf(3);
	// public static final String ESTADO_PROCESO_EVALUADO_LABEL = "EVALUADO";

	// public static final int EST_PROC_ASIGNADO_MECANISMO_ERRONEO_VALUE =
	// Integer.valueOf(4);
	// public static final String EST_PROC_ASIGNADO_MECANISMO_ERRONEO_LABEL =
	// "ASIGNADO MECANISMO ERRONEO";

	public static final int ESTADO_PROCESO_APTO_VALUE = Integer.valueOf(4);
	public static final String ESTADO_PROCESO_APTO_LABEL = "APTO";

	public static final int ESTADO_PROCESO_APTO_EDICION_VALUE = Integer.valueOf(19);
	public static final String ESTADO_PROCESO_APTO_EDICION_LABEL = "APTO - EDICION";

	public static final int ESTADO_PROCESO_NO_APTO_VALUE = Integer.valueOf(5);
	public static final String ESTADO_PROCESO_NO_APTO_LABEL = "NO APTO";

	public static final int ESTADO_PROCESO_APTO_EVALUADO_VALUE = Integer.valueOf(6);
	public static final String ESTADO_PROCESO_APTO_EVALUADO_LABEL = "APTO - EVALUADO";

	public static final int ESTADO_PROCESO_APTO_EVALUADO_EDICION_VALUE = Integer.valueOf(21);
	public static final String ESTADO_PROCESO_APTO_EVALUADO_EDICION_LABEL = "APTO - EVALUADO EDICION";

	public static final int ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE = Integer.valueOf(7);
	public static final String ESTADO_PROCESO_NO_APTO_EVALUADO_LABEL = "NO APTO - EVALUADO";

	public static final int ESTADO_PROCESO_TRIBUNAL_LECTOR_PENDIENTE_VALUE = Integer.valueOf(8);
	public static final String ESTADO_PROCESO_TRIBUNAL_LECTOR_PENDIENTE_LABEL = "NO APRUEBA TRIBUNAL LECTOR";

	public static final int ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_VALUE = Integer.valueOf(9);
	public static final String ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_LABEL = "NOTAS EXAMEN COMPLEXIVO PRACTICO CARGADAS";

	public static final int ESTADO_PROCESO_COMPLEX_CARGADO_VALUE = Integer.valueOf(10);
	public static final String ESTADO_PROCESO_COMPLEX_CARGADO_LABEL = "NOTAS EXAMEN COMPLEXIVO TEORICO CARGADAS";

	public static final int ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE = Integer.valueOf(11);
	public static final String ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_LABEL = "EXAMEN COMPLEXIVO REPROBADO";

	public static final int ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_VALUE = Integer.valueOf(12);
	public static final String ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_LABEL = "NOTAS EXAMEN COMPLEXIVO DE GRACIA PRACTICO CARGADAS";

	public static final int ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_VALUE = Integer.valueOf(13);
	public static final String ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_LABEL = "NOTAS EXAMEN COMPLEXIVO DE GRACIA TEORICO CARGADAS";

	public static final int ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE = Integer.valueOf(14);
	public static final String ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_LABEL = "EXAMEN COMPLEXIVO DE GRACIA REPROBADO";

	public static final int ESTADO_PROCESO_DEFENSA_ORAL_VALUE = Integer.valueOf(15);
	public static final String ESTADO_PROCESO_DEFENSA_ORAL_LABEL = "NOTAS CARGADAS DE DEFENSA ORAL";

	public static final int ESTADO_PROCESO_DEFENSA_ORAL_EDICION_VALUE = Integer.valueOf(30);
	public static final String ESTADO_PROCESO_DEFENSA_ORAL_EDICION_LABEL = "NOTAS CARGADAS DE DEFENSA ORAL EDICION";

	public static final int ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_VALUE = Integer.valueOf(16);
	public static final String ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_LABEL = "NOTAS CARGADAS DE DEFENSA ORAL VALIDADO POR DINARDAP";

	public static final int ESTADO_PROCESO_CAMBIO_MECANISMO_VALUE = Integer.valueOf(17);
	public static final String ESTADO_PROCESO_CAMBIO_MECANISMO_VALIDADO_LABEL = "EDICION MECANISMO TITULACION";
	
	public static final int ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_EDICION_VALUE = Integer.valueOf(31);
	public static final String ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_EDICION_LABEL = "NOTAS CARGADAS DE DEFENSA ORAL VALIDADO POR DINARDAP EDICION";

	public static final int ESTADO_PROCESO_ACTA_GENERADA_MANUALMENTE_VALUE = Integer.valueOf(97);
	public static final String ESTADO_PROCESO_ACTA_GENERADA_MANUALMENTE_DEFENSA_LABEL = "ACTA GENERADA MANUALMENTE ";
	
	public static final int ESTADO_PROCESO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE = Integer.valueOf(98);
	public static final String ESTADO_PROCESO_APROBACION_TUTOR_TRIBUNAL_LECTOR_LABEL = "APROBACION TUTOR TRIBUNAL LECTOR";

	public static final int ESTADO_PROCESO_NO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE = Integer.valueOf(99);
	public static final String ESTADO_PROCESO_NO_APROBACION_TUTOR_TRIBUNAL_LECTOR_LABEL = "NO APROBACION_TUTOR_TRIBUNAL_LECTOR";
	
	public static final int ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE = Integer.valueOf(100);
	public static final String ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_LABEL = "APROBADO PROCESO DE TITULACION";

	public static final int ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE = Integer.valueOf(101);
	public static final String ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_LABEL = "APROBADO PROCESO DE TITULACION - COMPLEXIVO GRACIA";

	public static final int ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE = Integer.valueOf(102);
	public static final String ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_LABEL = "APROBADO PROCESO DE TITULACION VALIDADO DINARDAP";

	public static final int ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE = Integer.valueOf(103);
	public static final String ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_LABEL = "APROBADO PROCESO DE TITULACION VALIDADO DINARDAP - GRACIA";

	public static final int ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE = Integer.valueOf(104);
	public static final String ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_LABEL = "EMITIDO ACTA DE GRADO";

	public static final int ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE = Integer.valueOf(105);
	public static final String ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_LABEL = "EMITIDO ACTA DE GRADO EN DEFENSA ORAL";

	public static final int ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_EDICION_VALUE = Integer.valueOf(120);
	public static final String ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_EDICION_LABEL = "EMITIDO ACTA DE GRADO EN DEFENSA ORAL EDICION";

	public static final int ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE = Integer.valueOf(106);
	public static final String ESTADO_PROCESO_LISTO_EDICION_ACTA_LABEL = "LISTO PARA EDICION DE ACTA DE GRADO";

	public static final int ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE = Integer.valueOf(107);
	public static final String ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_LABEL = "LISTO PARA EDICION DE ACTA DE GRADO - DEFENSA ORAL";

	public static final int ESTADO_PROCESO_ACTA_IMPRESA_VALUE = Integer.valueOf(108);
	public static final String ESTADO_PROCESO_ACTA_IMPRESA_LABEL = "ACTA DE GRADO IMPRESA";

	public static final int ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE = Integer.valueOf(109);
	public static final String ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_LABEL = "ACTA DE GRADO IMPRESA DE DEFENSA ORAL";

	public static final int ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_EDICION_VALUE = Integer.valueOf(124);
	public static final String ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_EDICION_LABEL = "ACTA DE GRADO IMPRESA DE DEFENSA ORAL EDICION";

	public static final int ESTADO_PROCESO_EMISION_TITULO_VALUE = Integer.valueOf(110);
	public static final String ESTADO_PROCESO_EMISION_TITULO_LABEL = "MIGRACION A EMISION TITULO";

	public static final int ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE = Integer.valueOf(111);
	public static final String ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_LABEL = "MIGRACION A EMISION TITULO DEFENSA ORAL";

	public static final int ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_VALUE = Integer.valueOf(112);
	public static final String ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_LABEL = "EDICION DESDE MIGRACION A EMISION TITULO";

	public static final int ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_VALUE = Integer.valueOf(113);
	public static final String ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_LABEL = "EDICION DESDE MIGRACION A EMISION TITULO DEFENSA ORAL";

	public static final int ESTADO_PROCESO_EMISION_TITULO_EDITADO_VALUE = Integer.valueOf(114);
	public static final String ESTADO_PROCESO_EMISION_TITULO_EDITADO_LABEL = "LISTO EDICION DESDE MIGRACION A EMISION TITULO";

	public static final int ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_VALUE = Integer.valueOf(115);
	public static final String ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_LABEL = "LISTO EDICION DESDE MIGRACION A EMISION TITULO DEFENSA ORAL";

	public static final int ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDICION_VALUE = Integer.valueOf(126);
	public static final String ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDICION_LABEL = "MIGRACION A EMISION TITULO DEFENSA ORAL EDICION";

	// constantes que indican el tipo de modalidad asignada
	public static final int TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE = Integer.valueOf(0);
	public static final String TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL = "EXAMEN COMPLEXIVO";
	public static final int TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_VALUE = Integer.valueOf(1);
	public static final String TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL = "OTROS MECANISMOS DE TITULACIÓN";
	public static final int TIPO_MODALIDAD_ESTA_PROYECTO_INTEGRADOR_VALUE = Integer.valueOf(2);
	public static final String TIPO_MODALIDAD_ESTA_PROYECTO_INTEGRADOR_LABEL = "PROYECTO INTEGRADOR";
	public static final int TIPO_MODALIDAD_ESTA_PROYECTO_INVESTIGACION_VALUE = Integer.valueOf(3);
	public static final String TIPO_MODALIDAD_ESTA_PROYECTO_INVESTIGACION_LABEL = "PROYECTO DE INVESTIGACIÓN";

	// constantes que indican la observación para la desactivación del
	// postulante
	public static final String OBSERVACION_DGA_DESACTIVAR_LABEL = "Desactivación solicitada por la DGA al Administrador";

	
	/**
	 * Genera la lista de items para el combo estado_tramite
	 * @return lista de items para el combo examen complexivo
	 */
	public static String getEstadoProcesoEstudiante(int estado)	{
		String retorno = null;
		if(estado==TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_LABEL;
		}else if(estado==TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.EST_PROC_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE){
			retorno=TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APTO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_EDICION_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_EDICION_LABEL;
		}else if(estado==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_GENERADA_MANUALMENTE_VALUE){
			retorno=TramiteTituloConstantes.ESTADO_PROCESO_ACTA_GENERADA_MANUALMENTE_DEFENSA_LABEL;
		}
	
		return retorno;
	}
}
