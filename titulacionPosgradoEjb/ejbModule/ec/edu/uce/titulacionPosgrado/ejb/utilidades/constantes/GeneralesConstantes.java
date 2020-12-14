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
   
 ARCHIVO:     ConstantesGenerales.java	  
 DESCRIPCION: Clase de constantes generales del sistema 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  			          Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

import java.util.ResourceBundle;


/**
 * Clase (constantes) ConstantesGenerales.
 * Clase de constantes generales del sistema.
 * @author dalbuja.
 * @version 1.0
 */
public class GeneralesConstantes {
	/**
     * Define el nombre de la unidad de persistencia.
     */
	public static final String APP_UNIDAD_PERSISTENCIA = "titulacionPosgradoJpa";
	
	/**
     * Define el nombre del data source del proyecto.
     */
	public static final String APP_DATA_SURCE = "java:jboss/datasources/titulacionPosgradoDS";
	public static final String APP_DATA_SOURCE_SIIU = "java:jboss/datasources/omniumDS";
	public static final String APP_DATA_SOURCE_EMISION_TITULO = "java:jboss/datasources/emisionTituloDS";
	/**
	 * Define el valor del secuenacial base para obtener todos los registros 
	 */
	public static final Integer APP_ID_BASE = Integer.valueOf(-99);
	
	/**
	 * Define el tipo de operacion 0 nuevo 
	 */
	public static final Integer APP_NUEVO = Integer.valueOf(0);
	
	/**
	 * Define el tipo de operacion 1 editar 
	 */
	public static final Integer APP_EDITAR = Integer.valueOf(1);
	
	/**
	 * Define la etiqueta que representa a la opcion de seleccionar. 
	 */
	public static final String APP_SELECCIONE = "SELECCIONE...";
	
	/**
	 * Define la etiqueta que representa a la opcion de seleccionar todos. 
	 */
	public static final String APP_SELECCIONE_TODOS = "TODAS";
	
	/**
	 * Define el numero de maximo de la lista de busquedas
	 */
	public static final Integer APP_MAX_NUM_BUSQUEDA = Integer.valueOf(30);
	
	/**
	 * Define el valor de la escala de pixeles para graficos
	 */
	public static final Integer APP_ESCALA_PIXELES = Integer.valueOf(2);
	
	/**
	 * Define el valor para las opciones de si
	 */
	public static final Integer APP_SI_VALUE = Integer.valueOf(0);
	
	/**
	 * Define el label para las opciones de si
	 */
	public static final String APP_SI_LABEL = "SI";
	
	/**
	 * Define el valor para las opciones de no
	 */
	public static final Integer APP_NO_VALUE = Integer.valueOf(1);
	
	/**
	 * Define el label para las opciones de si
	 */
	public static final String APP_NO_LABEL = "NO";
	
	/**
	 * Define el valor del cero en las consultas 
	 */
	public static final Integer APP_CERO_VALUE = Integer.valueOf(0);
	/*************************************************************/
	/************** CONSTANTES DE WEB SERVICE MAIL ***************/
	/*************************************************************/
	
	/**
	 * Define el usuario de Web service
	 */
	public static final String APP_USUARIO_WS = "rccs09";
	
	/**
	 * Define el usuario de Web service
	 */
	public static final String APP_CLAVE_WS = "rccs1984";
	
	/**
     * Define el formato de fecha y hora usado por el sistema.
     */
	public static final String APP_FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm";
	
	/**
     * Define la dirección de la cola de mensajería de objetos
     */
	public static final String APP_NIO_ACTIVEMQ = "nio://10.20.1.64:61616";
	
	/**
     * Define el nombre de la cola de mensajería de objetos
     */
	public static final String APP_COLA_ACTIVEMQ = "COLA_MAIL_DTO";
	
	/*************************************************************/
	/**************** CONSTANTES DE CONFIGURACIONES **************/
	/*************************************************************/
	
	/**
	 * Define la etiqueta que representa al path de archivos 
	 */
	public static final String APP_PATH_ARCHIVOS;
	
	/**
     * Define el url del sistema
     */
	public static final String APP_URL_SISTEMA;
	
	/**
     * Define el url del sistema
     */
	public static final String APP_URL_WEB_SERVICE_MAIL;
	
	/**
     * Define la fecha maxima para seleccionar en culminacion de malla para postulacion de la primera convocatoria
     */
	public static final String APP_MAX_FECHA_CULMINACION_MALLA;
	
	/**
     * Define la fecha maxima para seleccionar en culminacion de malla para postulacion de la segunda convocatoria
     */
	public static final String APP_MAX_FECHA_CULMINACION_MALLA_CONVOCATORIA;
	
	/**
     * Define la fecha maxima para seleccionar en culminacion de malla para postulacion de la segunda convocatoria
     */
	public static final String APP_MIN_FECHA_CULMINACION_MALLA;
	
	/**
     * Define la fecha maxima para seleccionar en culminacion de malla para postulacion de la segunda convocatoria
     */
	public static final String APP_FECHA_INICIO_CONVOCATORIA;

	/**
     * Define la fecha maxima para seleccionar en periodo academico
     */
	public static final String APP_MAX_FECHA_PERIODO_ACADEMICO;
	
	/**
     * Define la fecha maxima para calculo en validacion
     */
	public static final String APP_MAX_FECHA_INFERIOR;
	
	/**
     * Define la fecha inicial de las preguntas de validación de correción de examen complexivo de EDITOR_DGA
     */
	public static final String APP_FECHA_INICIO_PREGUNTAS_VALIDACION_DGA;
	
	/**
	 * Define el valor del web service de emisionTitulo
	 */
	public static final String APP_PATH_JAXRS_EMISION_TITULO_GRADUADOS;
	
	static{
		String appPathArchivos = null, appUrlSistema = null, appUrlWsMail = null, appMaxFchMalla = null,appMaxFchMallaConvocatoria = null,
				appFechaInicioConvocatoria = null,appMinFchMalla = null, appMaxFchPeridodoAcademico=null , 
				appMaxFchInferior=null, appFechaInicioPregValidacion = null, appPathJaxrsEmisiontitulosGraduados = null; 
		try {
			ResourceBundle rb = ResourceBundle.getBundle("META-INF.configuracion.configuraciones");
			appPathArchivos = rb.getString("path.archivos");
			appUrlSistema = rb.getString("url.aplicacion");
			appUrlWsMail = rb.getString("url.aplicacion.mail");
			appMaxFchMalla = rb.getString("fecha.max.culminacion.malla");
			appMaxFchMallaConvocatoria = rb.getString("fecha.max.culminacion.malla.convocatoria");
			appMinFchMalla = rb.getString("fecha.min.culminacion.malla");
			appFechaInicioConvocatoria = rb.getString("fecha.inicio.convocatoria");
			appMaxFchPeridodoAcademico = rb.getString("fecha.max.periodo.academico");
			appMaxFchInferior = rb.getString("fecha.max.inferior");
			appFechaInicioPregValidacion = rb.getString("fecha.inicio.preguntas.validacion.dga");
			appPathJaxrsEmisiontitulosGraduados = rb.getString("path.jaxrs.emisionTitulos.graduados");
		} catch (Exception e) {}
		if(appPathArchivos == null){
			appPathArchivos = "/prodComplexivo/archivos/";
		}
		APP_PATH_ARCHIVOS = appPathArchivos;
		APP_URL_SISTEMA = appUrlSistema;
		APP_URL_WEB_SERVICE_MAIL = appUrlWsMail;
		APP_MAX_FECHA_CULMINACION_MALLA = appMaxFchMalla;
		APP_MAX_FECHA_CULMINACION_MALLA_CONVOCATORIA = appMaxFchMallaConvocatoria;
		APP_MIN_FECHA_CULMINACION_MALLA = appMinFchMalla;
		APP_FECHA_INICIO_CONVOCATORIA = appFechaInicioConvocatoria;
		APP_MAX_FECHA_PERIODO_ACADEMICO = appMaxFchPeridodoAcademico;
		APP_MAX_FECHA_INFERIOR = appMaxFchInferior;
		APP_FECHA_INICIO_PREGUNTAS_VALIDACION_DGA = appFechaInicioPregValidacion;
		APP_PATH_JAXRS_EMISION_TITULO_GRADUADOS = appPathJaxrsEmisiontitulosGraduados;
	}
	

	
	/*************************************************************/
	/***************** CONSTANTES DE MENSAJES WEB ****************/
	/*************************************************************/
	/**
	 * Define la etiqueta que representa al mensaje de campo requerido 
	 */
	public static final String APP_MSG_CAMP_REQUERIDO = "Campo requerido";
	/**
	 * Define la etiqueta que representa al mensaje de campo registro guardado exitosamente 
	 */
	public static final String APP_MSG_AGREGAR_REGISTRO_EXITOSO = "Registro guardado exitosamente";
	/**
	 * Define la etiqueta que representa al mensaje de campo registro eliminadi exitosamente 
	 */
	public static final String APP_MSG_ELIMINAR_REGISTRO_EXITOSO = "Registro eliminado exitosamente";
	
	/**
	 * Define la etiqueta que representa al mensaje de campo registro actualizado exitosamente 
	 */
	public static final String APP_MSG_ACTUALIZAR_REGISTRO_EXITOSO = "Registro actualizado exitosamente";
	
	/**
     * Define el formato de fecha usado por el sistema.
     */
	public static final String APP_FORMATO_FECHA = "dd/MM/yyyy";
	
	
	/*************************************************************/
	/************* CONSTANTES DE AUSNTOS ENVIO MAILS *************/
	/*************************************************************/
	
	/**
	 * Define el nombre del mail base para el envio de mails,  ejemplo infomail01, infomail02, infomail03, entonces el mailbase es infomail 
	 */
	public static final String APP_MAIL_BASE = "infomail";
	
	/**
	 * Define el asunto para el registro de titulacion 
	 */
	public static final String APP_ASUNTO_REGITRO = "Comunicación Proceso de Titulación Posgrado - Registro";
	
	/**
	 * Define el asunto para la postulacion de titulacion 
	 */
	public static final String APP_ASUNTO_POSTULACION = "Comunicación Proceso de Titulación Posgrado - Postulación";
	
	/**
	 * Define el asunto para la validación de titulacion 
	 */
	public static final String APP_ASUNTO_VALIDACION = "Comunicación Proceso de Titulación Posgrado - Validación";
	
	/**
	 * Define el asunto para el registro de actualización de conocimientos
	 */
	public static final String APP_ASUNTO_REGISTRO_ACTUALIZACION = "Comunicación Proceso de Titulación ";
	
	/**
	 * Define el asunto para el registro de actualización de conocimientos
	 */
	public static final String APP_TIPO_PROCESO_REGISTRO_ACTUALIZACION = " - Registro actualización";
	
	/**
	 * Define el asunto para la aptitud de la postulación 
	 */
	public static final String APP_ASUNTO_APTITUD = "Comunicación Proceso de Titulación Posgrado - Declaratoria de Aptitud";
	
	/**
	 * Define el asunto para la no aptitud de la postulación 
	 */
	public static final String APP_ASUNTO_NO_APTITUD = "Comunicación Proceso de Titulación Posgrado - Declaratoria de No Aptitud";
	
	/**
	 * Define el asunto para la reconsideraciónd de aptitud de la postulación 
	 */
	public static final String APP_ASUNTO_RECONSIDERACION_APTITUD = "Comunicación Proceso de Titulación Posgrado - Reconsideración de Aptitud";
	
	/**
	 * Define el asunto para la superaciónd e dos períodos 
	 */
	public static final String APP_ASUNTO_SUPERACION_DOS_PERIODOS = "Comunicación Proceso de Titulación Posgrado - Superación de períodos";
	
	/**
	 * Define el asunto para la reconsideraciónd de aptitud de la postulación 
	 */
	public static final String APP_ASUNTO_RECONSIDERACION_NO_APTITUD = "Comunicación Proceso de Titulación Posgrado - Reconsideración de No Aptitud";

	/**
	 * Define el asunto para la activación de actualización de la postulación 
	 */
	public static final String APP_ASUNTO_ACTIVAR_DESACTIVAR_ACTUALIZACION = "Comunicación Proceso de Titulación - Actualización de conocimientos";
	
	
	/**
	 * Define el asunto para la notificación de examen complexivo 
	 */
	public static final String APP_ASUNTO_NOTIFICACION_EXAMEN_COMPLEXIVO = "Notificación de resultado del examen complexivo";
	
	
	/**
	 * Define el asunto para la notificación de examen complexivo de gracia 
	 */
	public static final String APP_ASUNTO_NOTIFICACION_EXAMEN_COMPLEXIVO_GRACIA = "Notificación de resultado del examen complexivo de gracia";
	
	/**
	 * Define el asunto para la el cambio de fase 
	 */
	public static final String APP_ASUNTO_CAMBIO_FASE = "Comunicación Proceso de Titulación Posgrado 2018 - 2018 - Cambio de fase";
	
	/**
	 * Define el asunto para la el cambio de fase 
	 */
	public static final String APP_ASUNTO_RECTIFICACION_COMPLEXIVO = "Rectificación de notas de examen complexivo ";
	
	/**
	 * Define el asunto para la el cambio de fase 
	 */
	public static final String APP_ASUNTO_CODIGO_PIN = "Correo de confirmación de código PIN ";
	
	/**
	 * Define el asunto para la desactivación del postulante 
	 */
	public static final String APP_ASUNTO_DESACTIVACION_POSTULANTE = "Desactivación de la plataforma de titulación ";
	
	/**
	 * Define el asunto para la desactivación del postulante 
	 */
	public static final String APP_ASUNTO_ACTIVACION_POSTULANTE = "Activación de la plataforma de titulación ";
	
	/**
	 * Define el asunto para mail copia a coordinador DGA de cambio de fase 
	 */
	public static final String APP_MAIL_DGA ="xaescobar@uce.edu.ec";
	
	/**
	 * Define el asunto para mail copia a director DGA de cambio de fase 
	 */
//	public static final String APP_MAIL_DIRECTOR_DGA ="dortiz.dga@uce.edu.ec";
	public static final String APP_MAIL_DIRECTOR_DGA ="mamedina@uce.edu.ec";
	/**
	 * Define el nombre del director DGA 
	 */
//	public static final String APP_NOMBRES_DIRECTOR_DGA ="JORGE DANIEL ORTIZ HERRERA";
	public static final String APP_NOMBRES_DIRECTOR_DGA ="MARGARITA ANGELICA MEDINA NICOLALDE";
	
	/**
	 * Define el asunto para mail copia a soporte de cambio de fase 
	 */
	public static final String APP_MAIL_SOPORTE = "soporte.tecnico@uce.edu.ec"; 
	
	/**
	 * Define el asunto para mail copia a produccion de cambio de fase 
	 */
	public static final String APP_MAIL_PRODUCCION ="mmoyano@uce.edu.ec"; 
	
	/**
	 * Define el asunto para mail copia a director dtic de cambio de fase 
	 */
	public static final String APP_MAIL_DTIC ="scadena@uce.edu.ec";
	
	/**
	 * Define el valor para bloquear la edicion de datos si el postulante aprobó de proceso de titulacion
	 */
	public static final Integer APP_BLOQUEO_PROCESO_TITULACION = Integer.valueOf(100);
	
	/**
	 * Define el valor de la fecha de inicio del proceso de postulación
	 */
	public static final String APP_FECHA_MAXIMA_POSTULACION = "23-01-2007";  
	
	
	/**
	 * Define el valor del usuario del correo para el envío de ingreso de seguridad
	 */
	public static final String APP_USUARIO_MAIL = "uce.informacion50@uce.edu.ec";
	
	/**
	 * Define el valor del password del correo para el envío de ingreso de seguridad
	 */
	public static final String APP_PASSWORD_MAIL = "Postulantes2014";
	
	/**
	 * Define el valor del servidor de correo
	 */
	public static final String APP_SERVIDOR_MAIL = "smtp.office365.com";
	
	/**
	 * Define el valor del puerto del servidor de correo
	 */
	public static final String APP_PUERTO_SERVIDOR_MAIL = "587";
	
	/**
	 * Define el valor de habilitar TLS en servidor de correo
	 */
	public static final String APP_TLS_SMTP_STARTTLS_ENABLE_MAIL = "true";
	
	/**
	 * Define el valor de auth en el servidor de correo
	 */
	public static final String APP_TLS_SMTP_AUTH_MAIL = "true";
	
	/**
	 * Define el valor de protocolo en el servidor de correo
	 */
	public static final String APP_PROTOCOLO_SERVIDOR_MAIL = "smtps";
	

	
}
