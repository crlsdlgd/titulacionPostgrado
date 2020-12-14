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
   
 ARCHIVO:     ConstantesJdbc.java	  
 DESCRIPCION: Clase de constantes de tablas y campos de la BD para el uso de JDBC.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018				Daniel Albuja  	                    Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes;

import java.util.ResourceBundle;

public class JdbcConstantes {

	//****************************************************************************
	//**************** ESQUEMA public*******************************************
	//****************************************************************************
	public static final String ESQUEMA_PUBLIC;

	//****************************************************************************
	//**************** TABLA APTITUD*******************************************
	//****************************************************************************
	public static final String TABLA_APTITUD;
	public static final String APT_ID;
	public static final String APT_REQUISITOS;
	public static final String APT_SUFICIENCIA_INGLES;
	public static final String APT_FIN_MALLA;
	public static final String APT_APROBO_ACTUALIZAR;
	public static final String APT_APROBO_TUTOR;
	public static final String APT_NOTA_DIRECTOR;
	public static final String APT_PRTR_ID;

	//****************************************************************************
	//**************** TABLA ASENTAMIENTO_NOTA*******************************************
	//****************************************************************************
	public static final String TABLA_ASENTAMIENTO_NOTA;
	public static final String ASNO_ID;
	public static final String ASNO_PRTR_ID;
	public static final String ASNO_COMPLEXIVO_TEORICO;
	public static final String ASNO_COMPLEXIVO_PRACTICO;
	public static final String ASNO_FECHA_CMP_TEORICO;
	public static final String ASNO_COMPLEXIVO_FINAL;
	public static final String ASNO_CMP_GRACIA_TEORICO;
	public static final String ASNO_FECHA_CMP_GRACIA_TEORICO;
	public static final String ASNO_CMP_GRACIA_PRACTICO;
	public static final String ASNO_FECHA_CMP_GRACIA_PRACTICO;
	public static final String ASNO_CMP_GRACIA_FINAL;
	public static final String ASNO_TRB_LECTOR1;
	public static final String ASNO_TRB_LECTOR2;
	public static final String ASNO_TRB_LECTOR3;
	public static final String ASNO_FECHA_TRB_ESCRITO;
	public static final String ASNO_PRM_TRB_ESCRITO;
	public static final String ASNO_DFN_ORAL1;
	public static final String ASNO_DFN_ORAL2;
	public static final String ASNO_DFN_ORAL3;
	public static final String ASNO_PRM_DFN_ORAL;
	public static final String ASNO_TRB_TITULACION_FINAL;
	public static final String ASNO_FECHA_DFN_ORAL;

	//****************************************************************************
	//**************** TABLA ASIGNACION_TITULACION*******************************************
	//****************************************************************************
	public static final String TABLA_ASIGNACION_TITULACION;
	public static final String ASTT_ID;
	public static final String ASTT_TEMA_TRABAJO;
	public static final String ASTT_TUTOR_METODOLOGICO;
	public static final String ASTT_DIRECTOR_CIENTIFICO;
	public static final String ASTT_PRTR_ID;
	public static final String ASTT_APROBACION_TUTOR;

	//****************************************************************************
	//**************** TABLA AUTORIDAD*******************************************
	//****************************************************************************
	public static final String TABLA_AUTORIDAD;
	public static final String ATR_ID;
	public static final String ATR_IDENTIFICACION;
	public static final String ATR_NOMBRES;
	public static final String ATR_PRIMER_APELLIDO;
	public static final String ATR_SEGUNDO_APELLIDO;
	public static final String ATR_ESTADO;
	public static final String ATR_TIPO;
	public static final String ATR_FCL_ID;
	public static final String ATR_SEXO;

	//****************************************************************************
	//**************** TABLA CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_CARRERA;
	public static final String CRR_ID;
	public static final String CRR_DESCRIPCION;
	public static final String CRR_COD_SNIESE;
//	public static final String CRR_TIPO_DURACION;
//	public static final String CRR_TIPO_DURACION_SNIESE;
//	public static final String CRR_DURACION;
//	public static final String CRR_DURACION_SNIESE;
	public static final String CRR_NIVEL;
	public static final String CRR_FCL_ID;
	public static final String CRR_DETALLE;
	public static final String CRR_TIPO_EVALUACION;
//	public static final String CRR_NUM_ACTA_GRADO;

	//****************************************************************************
	//**************** TABLA CONFIGURACION_CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_CONFIGURACION_CARRERA;
	public static final String CNCR_ID;
	public static final String CNCR_CRR_ID;
	public static final String CNCR_VGN_ID;
	public static final String CNCR_MDL_ID;
	public static final String CNCR_TTL_ID;
	public static final String CNCR_UBC_ID;
	public static final String CNCR_TISE_ID;
	public static final String CNCR_TIFR_ID;
	public static final String CNCR_DRC_ID;

	//****************************************************************************
	//**************** TABLA CONVOCATORIA*******************************************
	//****************************************************************************
	public static final String TABLA_CONVOCATORIA;
	public static final String CNV_ID;
	public static final String CNV_DESCRIPCION;
	public static final String CNV_FECHA_INICIO;
	public static final String CNV_FECHA_FIN;
	public static final String CNV_ESTADO;
	public static final String CNV_ESTADO_FASE;

//	//****************************************************************************
//	//**************** TABLA DETALLE_COMPLEXIVO*******************************************
//	//****************************************************************************
//	public static final String TABLA_DETALLE_COMPLEXIVO;
//	public static final String DTCM_ID;
//	public static final String DTCM_NOTA_DIRECTA;
//	public static final String DTCM_NOTA_TRANSFORMADA;
//	public static final String DTCM_ACIERTOS;
//	public static final String DTCM_ERRORES;
//	public static final String DTCM_BLANCOS;
//	public static final String DTCM_DOBLES;
//	public static final String DTCM_PRUEBA;
//	public static final String DTCM_TIPO_DETALLE;
//	public static final String DTCM_ASNO_ID;
//	public static final String DTCM_OBSERVACION;

	//****************************************************************************
	//**************** TABLA DURACION*******************************************
	//****************************************************************************
	public static final String TABLA_DURACION;
	public static final String DRC_ID;
	public static final String DRC_TIPO;
	public static final String DRC_TIEMPO;

	//****************************************************************************
	//**************** TABLA ETNIA*******************************************
	//****************************************************************************
	public static final String TABLA_ETNIA;
	public static final String ETN_ID;
	public static final String ETN_CODIGO_SNIESE;
	public static final String ETN_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA FACULTAD*******************************************
	//****************************************************************************
	public static final String TABLA_FACULTAD;
	public static final String FCL_ID;
	public static final String FCL_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA FICHA_DCN_ASG_TITULACION*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_DCN_ASG_TITULACION;
	public static final String FCDCASTT_ID;
	public static final String FCDCASTT_TIPO_TRIBUNAL;
	public static final String FCDCASTT_ASTT_ID;
	public static final String FCDCASTT_FCDC_ID;
	public static final String FCDCASTT_ESTADO;
	public static final String FCDCASTT_NOTA;
	public static final String FCDCASTT_FECHA_ASENTAMIENTO;

	//****************************************************************************
	//**************** TABLA FICHA_DOCENTE*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_DOCENTE;
	public static final String FCDC_ID;
	public static final String FCDC_ESTADO;
	public static final String FCDC_ABREVIATURA_TITULO;
	public static final String FCDC_CRR_ID;
	public static final String FCDC_PRS_ID;
	public static final String FCDC_TIPO;

	//****************************************************************************
	//**************** TABLA FICHA_ESTUDIANTE*******************************************
	//****************************************************************************
	public static final String TABLA_FICHA_ESTUDIANTE;
	public static final String FCES_ID;
	public static final String FCES_FECHA_INICIO;
	public static final String FCES_FECHA_EGRESAMIENTO;
	public static final String FCES_FECHA_ACTA_GRADO;
	public static final String FCES_NUM_ACTA_GRADO;
	public static final String FCES_FECHA_REFRENDACION;
	public static final String FCES_NUM_REFRENDACION;
	public static final String FCES_CRR_ESTUD_PREVIOS;
	public static final String FCES_TIEMPO_ESTUD_REC;
	public static final String FCES_TIPO_DURAC_REC;
	public static final String FCES_TIPO_COLEGIO;
	public static final String FCES_TIPO_COLEGIO_SNIESE;
	public static final String FCES_NOTA_PROM_ACUMULADO;
	public static final String FCES_NOTA_TRAB_TITULACION;
	public static final String FCES_LINK_TESIS;
	public static final String FCES_REC_ESTUD_PREVIOS;
	public static final String FCES_REC_ESTUD_PREV_SNIESE;
	public static final String FCES_FECHA_CREACION;
	public static final String FCES_UBC_CANTON_RESIDENCIA;
	public static final String FCES_MCCR_ID;
	public static final String FCES_INAC_ID_INST_EST_PREVIOS;
	public static final String FCES_PRS_ID;
	public static final String FCES_TRTT_ID;
	public static final String FCES_CNCR_ID;
	public static final String FCES_TITULO_BACHILLER;
	public static final String FCES_HORA_ACTA_GRADO;
	public static final String FCES_NOTA_FINAL_GRADUACION;
	public static final String FCES_ESTADO_MIGRADO;
	public static final String FCES_REG_TTL_SENESCYT;
	public static final String FCES_FECHA_INICIO_COHORTE;
	public static final String FCES_FECHA_FIN_COHORTE;
	public static final String FCES_DECANO;
	public static final String FCES_SUBDECANO;
	public static final String FCES_SECRETARIO;
	public static final String FCES_DIRECTOR;
	public static final String FCES_PORCENTAJE_COMPLEX;
	public static final String FCES_DECANO_SEXO;
	public static final String FCES_SUBDECANO_SEXO;
	public static final String FCES_SECRETARIO_SEXO;
	public static final String FCES_DIRECTOR_SEXO;
	
	//****************************************************************************
	//**************** TABLA INSTITUCION_ACADEMICA*******************************************
	//****************************************************************************
	public static final String TABLA_INSTITUCION_ACADEMICA;
	public static final String INAC_ID;
	public static final String INAC_CODIGO_SNIESE;
	public static final String INAC_DESCRIPCION;
	public static final String INAC_NIVEL;
	public static final String INAC_TIPO;
	public static final String INAC_TIPO_SNIESE;
	public static final String INAC_UBC_ID;

	//****************************************************************************
	//**************** TABLA MECANISMO_TITULACION*******************************************
	//****************************************************************************
	public static final String TABLA_MECANISMO_TITULACION;
	public static final String MCTT_ID;
	public static final String MCTT_CODIGO_SNIESE;
	public static final String MCTT_DESCRIPCION;
	public static final String MCTT_ESTADO;

	//****************************************************************************
	//**************** TABLA MECANISMO_TITULACION_CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_MECANISMO_CARRERA;
	public static final String MCCR_ID;
	public static final String MCCR_ESTADO;
	public static final String MCCR_CRR_ID;
	public static final String MCCR_MCTT_ID;
	public static final String MCCR_PORCENTAJE;

	//****************************************************************************
	//**************** TABLA MODALIDAD*******************************************
	//****************************************************************************
	public static final String TABLA_MODALIDAD;
	public static final String MDL_ID;
	public static final String MDL_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA NIVEL_FORMACION*******************************************
	//****************************************************************************
	public static final String TABLA_NIVEL_FORMACION;
	public static final String NVFR_ID;
	public static final String NVFR_RGAC_ID;
	public static final String NVFR_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA PERSONA*******************************************
	//****************************************************************************
	public static final String TABLA_PERSONA;
	public static final String PRS_ID;
	public static final String PRS_TIPO_IDENTIFICACION;
	public static final String PRS_TIPO_IDENTIFICACION_SNIESE;
	public static final String PRS_IDENTIFICACION;
	public static final String PRS_PRIMER_APELLIDO;
	public static final String PRS_SEGUNDO_APELLIDO;
	public static final String PRS_NOMBRES;
	public static final String PRS_SEXO;
	public static final String PRS_SEXO_SNIESE;
	public static final String PRS_MAIL_PERSONAL;
	public static final String PRS_MAIL_INSTITUCIONAL;
	public static final String PRS_TELEFONO;
	public static final String PRS_FECHA_NACIMIENTO;
	public static final String PRS_UBICACION_FOTO;
	public static final String PRS_ETN_ID;
	public static final String PRS_UBC_ID;

	//****************************************************************************
	//**************** TABLA PROCESO_TRAMITE*******************************************
	//****************************************************************************
	public static final String TABLA_PROCESO_TRAMITE;
	public static final String PRTR_ID;
	public static final String PRTR_TIPO_PROCESO;
	public static final String PRTR_FECHA_EJECUCION;
//	public static final String PRTR_RUTA_ARCHIVO_AUTORIZACION;
//	public static final String PRTR_OBS_ACTUALIZACION;
	public static final String PRTR_REG_TTL_SENECYT;
	public static final String PRTR_ROFLCR_ID;
	public static final String PRTR_TRTT_ID;

	//****************************************************************************
	//**************** TABLA REGIMEN_ACADEMICO*******************************************
	//****************************************************************************
	public static final String TABLA_REGIMEN_ACADEMICO;
	public static final String RGAC_ID;
	public static final String RGAC_DESCRIPCION;
	public static final String RGAC_ESTADO;

	//****************************************************************************
	//**************** TABLA ROL*******************************************
	//****************************************************************************
	public static final String TABLA_ROL;
	public static final String ROL_ID;
	public static final String ROL_DESCRIPCION;
	public static final String ROL_TIPO;

	//****************************************************************************
	//**************** TABLA ROL_FLUJO_CARRERA*******************************************
	//****************************************************************************
	public static final String TABLA_ROL_FLUJO_CARRERA;
	public static final String ROFLCR_ID;
	public static final String ROFLCR_ESTADO;
	public static final String ROFLCR_USRO_ID;
	public static final String ROFLCR_CRR_ID;

//	//****************************************************************************
//	//**************** TABLA SEGURIDAD*******************************************
//	//****************************************************************************
//	public static final String TABLA_SEGURIDAD;
//	public static final String SGR_ID;
//	public static final String SGR_PREGUNTA;
//	public static final String SGR_RESPUESTA;
//	public static final String SGR_USR_ID;

	//****************************************************************************
	//**************** TABLA TIPO_FORMACION*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_FORMACION;
	public static final String TIFR_ID;
	public static final String TIFR_DESCRIPCION;
	public static final String TIFR_NVFR_ID;

	//****************************************************************************
	//**************** TABLA TIPO_SEDE*******************************************
	//****************************************************************************
	public static final String TABLA_TIPO_SEDE;
	public static final String TISE_ID;
	public static final String TISE_RGAC_ID;
	public static final String TISE_DESCRIPCION;

	//****************************************************************************
	//**************** TABLA TITULO*******************************************
	//****************************************************************************
	public static final String TABLA_TITULO;
	public static final String TTL_ID;
	public static final String TTL_DESCRIPCION;
	public static final String TTL_SEXO;
	public static final String TTL_ESTADO;
	public static final String TTL_TIPO;

	//****************************************************************************
	//**************** TABLA TRAMITE_TITULO*******************************************
	//****************************************************************************
	public static final String TABLA_TRAMITE_TITULO;
	public static final String TRTT_ID;
	public static final String TRTT_NUM_TRAMITE;
	public static final String TRTT_OBSERVACION;
	public static final String TRTT_ESTADO_TRAMITE;
	public static final String TRTT_ESTADO_PROCESO;
	public static final String TRTT_SUB_ID;
	public static final String TRTT_CARRERA;
	public static final String TRTT_MCTT_ESTADISTICO;
	public static final String TRTT_CNV_ID;
	public static final String TRTT_CARRERA_ID;

	//****************************************************************************
	//**************** TABLA UBICACION*******************************************
	//****************************************************************************
	public static final String TABLA_UBICACION;
	public static final String UBC_ID;
	public static final String UBC_DESCRIPCION;
	public static final String UBC_JERARQUIA;
	public static final String UBC_GENTILICIO;
	public static final String UBC_COD_SNIESE;
	public static final String UBC_PADRE;

	//****************************************************************************
	//**************** TABLA USUARIO*******************************************
	//****************************************************************************
	public static final String TABLA_USUARIO;
	public static final String USR_ID;
	public static final String USR_IDENTIFICACION;
	public static final String USR_NICK;
	public static final String USR_PASSWORD;
	public static final String USR_FECHA_CREACION;
	public static final String USR_FECHA_CADUCIDAD;
	public static final String USR_FECHA_CAD_PASS;
	public static final String USR_ESTADO;
	public static final String USR_EST_SESION;
	public static final String USR_ACTIVE_DIRECTORY;
	public static final String USR_PRS_ID;

	//****************************************************************************
	//**************** TABLA USUARIO_ROL*******************************************
	//****************************************************************************
	public static final String TABLA_USUARIO_ROL;
	public static final String USRO_ID;
	public static final String USRO_ESTADO;
	public static final String USRO_USR_ID;
	public static final String USRO_ROL_ID;

	//****************************************************************************
	//**************** TABLA VALIDACION*******************************************
	//****************************************************************************
	public static final String TABLA_VALIDACION;
	public static final String VLD_ID;
	public static final String VLD_SEGUNDA_PRORROGA;
	public static final String VLD_RSL_PRORROGA;
	public static final String VLD_RSL_ACTUALIZ_APROB;
	public static final String VLD_RSL_CURSO_ACT_APROB;
	public static final String VLD_RSL_HOMOLOGACION_APROB;
	public static final String VLD_RSL_HOMOLOGACION;
	public static final String VLD_RSL_ACT_CONOCIMIENTO;
	public static final String VLD_RSL_IDONEIDAD;
//	public static final String VLD_RSL_EXAMEN_COMPLEXIVO;
//	public static final String VLD_RSL_OTROS_MECANISMOS;
//	public static final String VLD_RSL_SLC_MECANISMO;
	public static final String VLD_PRTR_ID;
//	public static final String VLD_FECHA_ASIGNACION_TUTOR_2015;
//	public static final String VLD_ASIGNACION_TUTOR_2015;
	public static final String VLD_COMPROBANTE_PAGO;
	public static final String VLD_APROBACION_INGLES;
	
	//****************************************************************************
	//**************** TABLA VIGENCIA*******************************************
	//****************************************************************************
	public static final String TABLA_VIGENCIA;
	public static final String VGN_ID;
	public static final String VGN_DESCRIPCION;


	static{
		ResourceBundle rb = ResourceBundle.getBundle("META-INF.configuracion.tablasJdbc");
		ESQUEMA_PUBLIC = rb.getString("esquema.titulacion_posgrado");
		TABLA_APTITUD = rb.getString("titulacion_posgrado.aptitud");
		APT_ID = rb.getString("titulacion_posgrado.aptitud.apt.id");
		APT_REQUISITOS = rb.getString("titulacion_posgrado.aptitud.apt.requisitos");
		APT_SUFICIENCIA_INGLES = rb.getString("titulacion_posgrado.aptitud.apt.suficiencia.ingles");
		APT_FIN_MALLA = rb.getString("titulacion_posgrado.aptitud.apt.fin.malla");
		APT_APROBO_ACTUALIZAR = rb.getString("titulacion_posgrado.aptitud.apt.aprobo.actualizar");
		APT_APROBO_TUTOR = rb.getString("titulacion_posgrado.aptitud.apt.aprobo.tutor");
		APT_NOTA_DIRECTOR = rb.getString("titulacion_posgrado.aptitud.apt.nota.director");
		APT_PRTR_ID = rb.getString("titulacion_posgrado.aptitud.prtr.id");
		TABLA_ASENTAMIENTO_NOTA = rb.getString("titulacion_posgrado.asentamiento.nota");
		ASNO_ID = rb.getString("titulacion_posgrado.asentamiento.nota.asno.id");
		ASNO_PRTR_ID = rb.getString("titulacion_posgrado.asentamiento.nota.prtr.id");
		ASNO_COMPLEXIVO_TEORICO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.complexivo.teorico");
		ASNO_COMPLEXIVO_PRACTICO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.complexivo.practico");
		ASNO_FECHA_CMP_TEORICO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.fecha.cmp.teorico");
		ASNO_COMPLEXIVO_FINAL = rb.getString("titulacion_posgrado.asentamiento.nota.asno.complexivo.final");
		ASNO_CMP_GRACIA_TEORICO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.cmp.gracia.teorico");
		ASNO_FECHA_CMP_GRACIA_TEORICO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.fecha.cmp.gracia.teorico");
		ASNO_CMP_GRACIA_PRACTICO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.cmp.gracia.practico");
		ASNO_FECHA_CMP_GRACIA_PRACTICO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.fecha.cmp.gracia.practico");
		ASNO_CMP_GRACIA_FINAL = rb.getString("titulacion_posgrado.asentamiento.nota.asno.cmp.gracia.final");
		ASNO_TRB_LECTOR1 = rb.getString("titulacion_posgrado.asentamiento.nota.asno.trb.lector1");
		ASNO_TRB_LECTOR2 = rb.getString("titulacion_posgrado.asentamiento.nota.asno.trb.lector2");
		ASNO_TRB_LECTOR3 = rb.getString("titulacion_posgrado.asentamiento.nota.asno.trb.lector3");
		ASNO_FECHA_TRB_ESCRITO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.fecha.trb.escrito");
		ASNO_PRM_TRB_ESCRITO = rb.getString("titulacion_posgrado.asentamiento.nota.asno.prm.trb.escrito");
		ASNO_DFN_ORAL1 = rb.getString("titulacion_posgrado.asentamiento.nota.asno.dfn.oral1");
		ASNO_DFN_ORAL2 = rb.getString("titulacion_posgrado.asentamiento.nota.asno.dfn.oral2");
		ASNO_DFN_ORAL3 = rb.getString("titulacion_posgrado.asentamiento.nota.asno.dfn.oral3");
		ASNO_PRM_DFN_ORAL = rb.getString("titulacion_posgrado.asentamiento.nota.asno.prm.dfn.oral");
		ASNO_TRB_TITULACION_FINAL = rb.getString("titulacion_posgrado.asentamiento.nota.asno.trb.titulacion.final");
		ASNO_FECHA_DFN_ORAL = rb.getString("titulacion_posgrado.asentamiento.nota.asno.fecha.dfn.oral");
		TABLA_ASIGNACION_TITULACION = rb.getString("titulacion_posgrado.asignacion.titulacion");
		ASTT_ID = rb.getString("titulacion_posgrado.asignacion.titulacion.astt.id");
		ASTT_TEMA_TRABAJO = rb.getString("titulacion_posgrado.asignacion.titulacion.astt.tema.trabajo");
		ASTT_TUTOR_METODOLOGICO = rb.getString("titulacion_posgrado.asignacion.titulacion.astt.tutor.metodologico");
		ASTT_DIRECTOR_CIENTIFICO = rb.getString("titulacion_posgrado.asignacion.titulacion.astt.director.cientifico");
		ASTT_PRTR_ID = rb.getString("titulacion_posgrado.asignacion.titulacion.prtr.id");
		ASTT_APROBACION_TUTOR = rb.getString("titulacion_posgrado.asignacion.titulacion.astt.aprobacion.tutor");
		TABLA_AUTORIDAD = rb.getString("titulacion_posgrado.autoridad");
		ATR_ID = rb.getString("titulacion_posgrado.autoridad.atr.id");
		ATR_IDENTIFICACION = rb.getString("titulacion_posgrado.autoridad.atr.identificacion");
		ATR_NOMBRES = rb.getString("titulacion_posgrado.autoridad.atr.nombres");
		ATR_PRIMER_APELLIDO = rb.getString("titulacion_posgrado.autoridad.atr.primer.apellido");
		ATR_SEGUNDO_APELLIDO = rb.getString("titulacion_posgrado.autoridad.atr.segundo.apellido");
		ATR_ESTADO = rb.getString("titulacion_posgrado.autoridad.atr.estado");
		ATR_TIPO = rb.getString("titulacion_posgrado.autoridad.atr.tipo");
		ATR_SEXO = rb.getString("titulacion_posgrado.autoridad.atr.sexo");
		ATR_FCL_ID = rb.getString("titulacion_posgrado.autoridad.fcl.id");
		TABLA_CARRERA = rb.getString("titulacion_posgrado.carrera");
		CRR_ID = rb.getString("titulacion_posgrado.carrera.crr.id");
		CRR_DESCRIPCION = rb.getString("titulacion_posgrado.carrera.crr.descripcion");
		CRR_COD_SNIESE = rb.getString("titulacion_posgrado.carrera.crr.cod.sniese");
//		CRR_TIPO_DURACION = rb.getString("titulacion_posgrado.carrera.crr.tipo.duracion");
//		CRR_TIPO_DURACION_SNIESE = rb.getString("titulacion_posgrado.carrera.crr.tipo.duracion.sniese");
//		CRR_DURACION = rb.getString("titulacion_posgrado.carrera.crr.duracion");
//		CRR_DURACION_SNIESE = rb.getString("titulacion_posgrado.carrera.crr.duracion.sniese");
		CRR_NIVEL = rb.getString("titulacion_posgrado.carrera.crr.nivel");
		CRR_FCL_ID = rb.getString("titulacion_posgrado.carrera.fcl.id");
		CRR_DETALLE = rb.getString("titulacion_posgrado.carrera.crr.detalle");
		CRR_TIPO_EVALUACION = rb.getString("titulacion_posgrado.carrera.crr.tipo.evaluacion");
//		CRR_NUM_ACTA_GRADO = rb.getString("titulacion_posgrado.carrera.crr.num.acta.grado");
		TABLA_CONFIGURACION_CARRERA = rb.getString("titulacion_posgrado.configuracion.carrera");
		CNCR_ID = rb.getString("titulacion_posgrado.configuracion.carrera.cncr.id");
		CNCR_CRR_ID = rb.getString("titulacion_posgrado.configuracion.carrera.crr.id");
		CNCR_VGN_ID = rb.getString("titulacion_posgrado.configuracion.carrera.vgn.id");
		CNCR_MDL_ID = rb.getString("titulacion_posgrado.configuracion.carrera.mdl.id");
		CNCR_TTL_ID = rb.getString("titulacion_posgrado.configuracion.carrera.ttl.id");
		CNCR_UBC_ID = rb.getString("titulacion_posgrado.configuracion.carrera.ubc.id");
		CNCR_TISE_ID = rb.getString("titulacion_posgrado.configuracion.carrera.tise.id");
		CNCR_TIFR_ID = rb.getString("titulacion_posgrado.configuracion.carrera.tifr.id");
		CNCR_DRC_ID = rb.getString("titulacion_posgrado.configuracion.carrera.drc.id");
		TABLA_CONVOCATORIA = rb.getString("titulacion_posgrado.convocatoria");
		CNV_ID = rb.getString("titulacion_posgrado.convocatoria.cnv.id");
		CNV_DESCRIPCION = rb.getString("titulacion_posgrado.convocatoria.cnv.descripcion");
		CNV_FECHA_INICIO = rb.getString("titulacion_posgrado.convocatoria.cnv.fecha.inicio");
		CNV_FECHA_FIN = rb.getString("titulacion_posgrado.convocatoria.cnv.fecha.fin");
//		CNV_FECHA_LIM_EGRESADO = rb.getString("titulacion_posgrado.convocatoria.cnv.fecha.lim.egresado");
		CNV_ESTADO = rb.getString("titulacion_posgrado.convocatoria.cnv.estado");
		CNV_ESTADO_FASE = rb.getString("titulacion_posgrado.convocatoria.cnv.estado.fase");
//		TABLA_DETALLE_COMPLEXIVO = rb.getString("titulacion_posgrado.detalle.complexivo");
//		DTCM_ID = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.id");
//		DTCM_NOTA_DIRECTA = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.nota.directa");
//		DTCM_NOTA_TRANSFORMADA = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.nota.transformada");
//		DTCM_ACIERTOS = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.aciertos");
//		DTCM_ERRORES = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.errores");
//		DTCM_BLANCOS = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.blancos");
//		DTCM_DOBLES = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.dobles");
//		DTCM_PRUEBA = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.prueba");
//		DTCM_TIPO_DETALLE = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.tipo.detalle");
//		DTCM_ASNO_ID = rb.getString("titulacion_posgrado.detalle.complexivo.asno.id");
//		DTCM_OBSERVACION = rb.getString("titulacion_posgrado.detalle.complexivo.dtcm.observacion");
		TABLA_DURACION = rb.getString("titulacion_posgrado.duracion");
		DRC_ID = rb.getString("titulacion_posgrado.duracion.drc.id");
		DRC_TIPO = rb.getString("titulacion_posgrado.duracion.drc.tipo");
		DRC_TIEMPO = rb.getString("titulacion_posgrado.duracion.drc.tiempo");
		TABLA_ETNIA = rb.getString("titulacion_posgrado.etnia");
		ETN_ID = rb.getString("titulacion_posgrado.etnia.etn.id");
		ETN_CODIGO_SNIESE = rb.getString("titulacion_posgrado.etnia.etn.codigo.sniese");
		ETN_DESCRIPCION = rb.getString("titulacion_posgrado.etnia.etn.descripcion");
		TABLA_FACULTAD = rb.getString("titulacion_posgrado.facultad");
		FCL_ID = rb.getString("titulacion_posgrado.facultad.fcl.id");
		FCL_DESCRIPCION = rb.getString("titulacion_posgrado.facultad.fcl.descripcion");
		TABLA_FICHA_DCN_ASG_TITULACION = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion");
		FCDCASTT_ID = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion.fcdcastt.id");
		FCDCASTT_TIPO_TRIBUNAL = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion.fcdcastt.tipo.tribunal");
		FCDCASTT_ASTT_ID = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion.astt.id");
		FCDCASTT_FCDC_ID = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion.fcdc.id");
		FCDCASTT_ESTADO = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion.fcdcastt.estado");
		FCDCASTT_NOTA = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion.fcdcastt.nota");
		FCDCASTT_FECHA_ASENTAMIENTO = rb.getString("titulacion_posgrado.ficha.dcn.asg.titulacion.fcdcastt.fecha.asentamiento");
		TABLA_FICHA_DOCENTE = rb.getString("titulacion_posgrado.ficha.docente");
		FCDC_ID = rb.getString("titulacion_posgrado.ficha.docente.fcdc.id");
		FCDC_ESTADO = rb.getString("titulacion_posgrado.ficha.docente.fcdc.estado");
		FCDC_ABREVIATURA_TITULO = rb.getString("titulacion_posgrado.ficha.docente.fcdc.abreviatura.titulo");
		FCDC_CRR_ID = rb.getString("titulacion_posgrado.ficha.docente.crr.id");
		FCDC_PRS_ID = rb.getString("titulacion_posgrado.ficha.docente.prs.id");
		FCDC_TIPO = rb.getString("titulacion_posgrado.ficha.docente.fcdc.tipo");
		TABLA_FICHA_ESTUDIANTE = rb.getString("titulacion_posgrado.ficha.estudiante");
		FCES_ID = rb.getString("titulacion_posgrado.ficha.estudiante.fces.id");
		FCES_FECHA_INICIO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.fecha.inicio");
		FCES_FECHA_EGRESAMIENTO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.fecha.egresamiento");
		FCES_FECHA_ACTA_GRADO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.fecha.acta.grado");
		FCES_NUM_ACTA_GRADO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.num.acta.grado");
		FCES_FECHA_REFRENDACION = rb.getString("titulacion_posgrado.ficha.estudiante.fces.fecha.refrendacion");
		FCES_NUM_REFRENDACION = rb.getString("titulacion_posgrado.ficha.estudiante.fces.num.refrendacion");
		FCES_CRR_ESTUD_PREVIOS = rb.getString("titulacion_posgrado.ficha.estudiante.fces.crr.estud.previos");
		FCES_TIEMPO_ESTUD_REC = rb.getString("titulacion_posgrado.ficha.estudiante.fces.tiempo.estud.rec");
		FCES_TIPO_DURAC_REC = rb.getString("titulacion_posgrado.ficha.estudiante.fces.tipo.durac.rec");
		FCES_TIPO_COLEGIO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.tipo.colegio");
		FCES_TIPO_COLEGIO_SNIESE = rb.getString("titulacion_posgrado.ficha.estudiante.fces.tipo.colegio.sniese");
		FCES_NOTA_PROM_ACUMULADO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.nota.prom.acumulado");
		FCES_NOTA_TRAB_TITULACION = rb.getString("titulacion_posgrado.ficha.estudiante.fces.nota.trab.titulacion");
		FCES_LINK_TESIS = rb.getString("titulacion_posgrado.ficha.estudiante.fces.link.tesis");
		FCES_REC_ESTUD_PREVIOS = rb.getString("titulacion_posgrado.ficha.estudiante.fces.rec.estud.previos");
		FCES_REC_ESTUD_PREV_SNIESE = rb.getString("titulacion_posgrado.ficha.estudiante.fces.rec.estud.prev.sniese");
		FCES_FECHA_CREACION = rb.getString("titulacion_posgrado.ficha.estudiante.fces.fecha.creacion");
		FCES_UBC_CANTON_RESIDENCIA = rb.getString("titulacion_posgrado.ficha.estudiante.ubc.canton.residencia");
		FCES_MCCR_ID = rb.getString("titulacion_posgrado.ficha.estudiante.mccr.id");
		FCES_INAC_ID_INST_EST_PREVIOS = rb.getString("titulacion_posgrado.ficha.estudiante.inac.id.inst.est.previos");
		FCES_PRS_ID = rb.getString("titulacion_posgrado.ficha.estudiante.prs.id");
		FCES_TRTT_ID = rb.getString("titulacion_posgrado.ficha.estudiante.trtt.id");
		FCES_CNCR_ID = rb.getString("titulacion_posgrado.ficha.estudiante.cncr.id");
		FCES_TITULO_BACHILLER = rb.getString("titulacion_posgrado.ficha.estudiante.fces.titulo.bachiller");
		FCES_HORA_ACTA_GRADO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.hora.acta.grado");
		FCES_NOTA_FINAL_GRADUACION = rb.getString("titulacion_posgrado.ficha.estudiante.fces.nota.final.graduacion");
		FCES_ESTADO_MIGRADO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.estado.migrado");
		FCES_DECANO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.decano");
		FCES_SUBDECANO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.subdecano");
		FCES_SECRETARIO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.secretario");
		FCES_DIRECTOR = rb.getString("titulacion_posgrado.ficha.estudiante.fces.director");
		FCES_PORCENTAJE_COMPLEX = rb.getString("titulacion_posgrado.ficha.estudiante.fces.porcentaje.complex");
		FCES_DECANO_SEXO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.decano.sexo");
		FCES_SUBDECANO_SEXO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.subdecano.sexo");
		FCES_SECRETARIO_SEXO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.secretario.sexo");
		FCES_DIRECTOR_SEXO = rb.getString("titulacion_posgrado.ficha.estudiante.fces.director.sexo");
		FCES_REG_TTL_SENESCYT = rb.getString("titulacion_posgrado.ficha.estudiante.fces.reg.ttl.senescyt");
		TABLA_INSTITUCION_ACADEMICA = rb.getString("titulacion_posgrado.institucion.academica");
		FCES_FECHA_INICIO_COHORTE = rb.getString("titulacion_posgrado.ficha.estudiante.fces.fecha.inicio.cohorte");
		FCES_FECHA_FIN_COHORTE = rb.getString("titulacion_posgrado.ficha.estudiante.fces.fecha.fin.cohorte");
		INAC_ID = rb.getString("titulacion_posgrado.institucion.academica.inac.id");
		INAC_CODIGO_SNIESE = rb.getString("titulacion_posgrado.institucion.academica.inac.codigo.sniese");
		INAC_DESCRIPCION = rb.getString("titulacion_posgrado.institucion.academica.inac.descripcion");
		INAC_NIVEL = rb.getString("titulacion_posgrado.institucion.academica.inac.nivel");
		INAC_TIPO = rb.getString("titulacion_posgrado.institucion.academica.inac.tipo");
		INAC_TIPO_SNIESE = rb.getString("titulacion_posgrado.institucion.academica.inac.tipo.sniese");
		INAC_UBC_ID = rb.getString("titulacion_posgrado.institucion.academica.ubc.id");
		TABLA_MECANISMO_TITULACION = rb.getString("titulacion_posgrado.mecanismo.titulacion");
		MCTT_ID = rb.getString("titulacion_posgrado.mecanismo.titulacion.mctt.id");
		MCTT_CODIGO_SNIESE = rb.getString("titulacion_posgrado.mecanismo.titulacion.mctt.codigo.sniese");
		MCTT_DESCRIPCION = rb.getString("titulacion_posgrado.mecanismo.titulacion.mctt.descripcion");
		MCTT_ESTADO = rb.getString("titulacion_posgrado.mecanismo.titulacion.mctt.estado");
		TABLA_MECANISMO_CARRERA = rb.getString("titulacion_posgrado.mecanismo.carrera");
		MCCR_ID = rb.getString("titulacion_posgrado.mecanismo.carrera.mccr.id");
		MCCR_ESTADO = rb.getString("titulacion_posgrado.mecanismo.carrera.mccr.estado");
		MCCR_CRR_ID = rb.getString("titulacion_posgrado.mecanismo.carrera.crr.id");
		MCCR_MCTT_ID = rb.getString("titulacion_posgrado.mecanismo.carrera.mctt.id");
		MCCR_PORCENTAJE = rb.getString("titulacion_posgrado.mecanismo.carrera.mccr.porcentaje");
		TABLA_MODALIDAD = rb.getString("titulacion_posgrado.modalidad");
		MDL_ID = rb.getString("titulacion_posgrado.modalidad.mdl.id");
		MDL_DESCRIPCION = rb.getString("titulacion_posgrado.modalidad.mdl.descripcion");
		TABLA_NIVEL_FORMACION = rb.getString("titulacion_posgrado.nivel.formacion");
		NVFR_ID = rb.getString("titulacion_posgrado.nivel.formacion.nvfr.id");
		NVFR_RGAC_ID = rb.getString("titulacion_posgrado.nivel.formacion.rgac.id");
		NVFR_DESCRIPCION = rb.getString("titulacion_posgrado.nivel.formacion.nvfr.descripcion");
		TABLA_PERSONA = rb.getString("titulacion_posgrado.persona");
		PRS_ID = rb.getString("titulacion_posgrado.persona.prs.id");
		PRS_TIPO_IDENTIFICACION = rb.getString("titulacion_posgrado.persona.prs.tipo.identificacion");
		PRS_TIPO_IDENTIFICACION_SNIESE = rb.getString("titulacion_posgrado.persona.prs.tipo.identificacion.sniese");
		PRS_IDENTIFICACION = rb.getString("titulacion_posgrado.persona.prs.identificacion");
		PRS_PRIMER_APELLIDO = rb.getString("titulacion_posgrado.persona.prs.primer.apellido");
		PRS_SEGUNDO_APELLIDO = rb.getString("titulacion_posgrado.persona.prs.segundo.apellido");
		PRS_NOMBRES = rb.getString("titulacion_posgrado.persona.prs.nombres");
		PRS_SEXO = rb.getString("titulacion_posgrado.persona.prs.sexo");
		PRS_SEXO_SNIESE = rb.getString("titulacion_posgrado.persona.prs.sexo.sniese");
		PRS_MAIL_PERSONAL = rb.getString("titulacion_posgrado.persona.prs.mail.personal");
		PRS_MAIL_INSTITUCIONAL = rb.getString("titulacion_posgrado.persona.prs.mail.institucional");
		PRS_TELEFONO = rb.getString("titulacion_posgrado.persona.prs.telefono");
		PRS_FECHA_NACIMIENTO = rb.getString("titulacion_posgrado.persona.prs.fecha.nacimiento");
		PRS_ETN_ID = rb.getString("titulacion_posgrado.persona.etn.id");
		PRS_UBC_ID = rb.getString("titulacion_posgrado.persona.ubc.id");
		PRS_UBICACION_FOTO = rb.getString("titulacion_posgrado.persona.prs.ubicacion.foto");
		TABLA_PROCESO_TRAMITE = rb.getString("titulacion_posgrado.proceso.tramite");
		PRTR_ID = rb.getString("titulacion_posgrado.proceso.tramite.prtr.id");
		PRTR_TIPO_PROCESO = rb.getString("titulacion_posgrado.proceso.tramite.prtr.tipo.proceso");
		PRTR_FECHA_EJECUCION = rb.getString("titulacion_posgrado.proceso.tramite.prtr.fecha.ejecucion");
//		PRTR_RUTA_ARCHIVO_AUTORIZACION = rb.getString("titulacion_posgrado.proceso.tramite.prtr.ruta.archivo.autorizacion");
//		PRTR_OBS_ACTUALIZACION = rb.getString("titulacion_posgrado.proceso.tramite.prtr.obs.actualizacion");
		PRTR_REG_TTL_SENECYT = rb.getString("titulacion_posgrado.proceso.tramite.prtr.reg.ttl.senecyt");
		PRTR_ROFLCR_ID = rb.getString("titulacion_posgrado.proceso.tramite.roflcr.id");
		PRTR_TRTT_ID = rb.getString("titulacion_posgrado.proceso.tramite.trtt.id");
		TABLA_REGIMEN_ACADEMICO = rb.getString("titulacion_posgrado.regimen.academico");
		RGAC_ID = rb.getString("titulacion_posgrado.regimen.academico.rgac.id");
		RGAC_DESCRIPCION = rb.getString("titulacion_posgrado.regimen.academico.rgac.descripcion");
		RGAC_ESTADO = rb.getString("titulacion_posgrado.regimen.academico.rgac.estado");
		TABLA_ROL = rb.getString("titulacion_posgrado.rol");
		ROL_ID = rb.getString("titulacion_posgrado.rol.rol.id");
		ROL_DESCRIPCION = rb.getString("titulacion_posgrado.rol.rol.descripcion");
		ROL_TIPO = rb.getString("titulacion_posgrado.rol.rol.tipo");
		TABLA_ROL_FLUJO_CARRERA = rb.getString("titulacion_posgrado.rol.flujo.carrera");
		ROFLCR_ID = rb.getString("titulacion_posgrado.rol.flujo.carrera.roflcr.id");
		ROFLCR_ESTADO = rb.getString("titulacion_posgrado.rol.flujo.carrera.roflcr.estado");
		ROFLCR_USRO_ID = rb.getString("titulacion_posgrado.rol.flujo.carrera.usro.id");
		ROFLCR_CRR_ID = rb.getString("titulacion_posgrado.rol.flujo.carrera.crr.id");
//		TABLA_SEGURIDAD = rb.getString("titulacion_posgrado.seguridad");
//		SGR_ID = rb.getString("titulacion_posgrado.seguridad.sgr.id");
//		SGR_PREGUNTA = rb.getString("titulacion_posgrado.seguridad.sgr.pregunta");
//		SGR_RESPUESTA = rb.getString("titulacion_posgrado.seguridad.sgr.respuesta");
//		SGR_USR_ID = rb.getString("titulacion_posgrado.seguridad.usr.id");
		TABLA_TIPO_FORMACION = rb.getString("titulacion_posgrado.tipo.formacion");
		TIFR_ID = rb.getString("titulacion_posgrado.tipo.formacion.tifr.id");
		TIFR_DESCRIPCION = rb.getString("titulacion_posgrado.tipo.formacion.tifr.descripcion");
		TIFR_NVFR_ID = rb.getString("titulacion_posgrado.tipo.formacion.nvfr.id");
		TABLA_TIPO_SEDE = rb.getString("titulacion_posgrado.tipo.sede");
		TISE_ID = rb.getString("titulacion_posgrado.tipo.sede.tise.id");
		TISE_RGAC_ID = rb.getString("titulacion_posgrado.tipo.sede.rgac.id");
		TISE_DESCRIPCION = rb.getString("titulacion_posgrado.tipo.sede.tise.descripcion");
		TABLA_TITULO = rb.getString("titulacion_posgrado.titulo");
		TTL_ID = rb.getString("titulacion_posgrado.titulo.ttl.id");
		TTL_DESCRIPCION = rb.getString("titulacion_posgrado.titulo.ttl.descripcion");
		TTL_SEXO = rb.getString("titulacion_posgrado.titulo.ttl.sexo");
		TTL_ESTADO = rb.getString("titulacion_posgrado.titulo.ttl.estado");
		TTL_TIPO = rb.getString("titulacion_posgrado.titulo.ttl.tipo");
		TABLA_TRAMITE_TITULO = rb.getString("titulacion_posgrado.tramite.titulo");
		TRTT_ID = rb.getString("titulacion_posgrado.tramite.titulo.trtt.id");
		TRTT_NUM_TRAMITE = rb.getString("titulacion_posgrado.tramite.titulo.trtt.num.tramite");
		TRTT_OBSERVACION = rb.getString("titulacion_posgrado.tramite.titulo.trtt.observacion");
		TRTT_ESTADO_TRAMITE = rb.getString("titulacion_posgrado.tramite.titulo.trtt.estado.tramite");
		TRTT_ESTADO_PROCESO = rb.getString("titulacion_posgrado.tramite.titulo.trtt.estado.proceso");
		TRTT_SUB_ID = rb.getString("titulacion_posgrado.tramite.titulo.trtt.sub.id");
		TRTT_CARRERA = rb.getString("titulacion_posgrado.tramite.titulo.trtt.carrera");
		TRTT_MCTT_ESTADISTICO = rb.getString("titulacion_posgrado.tramite.titulo.trtt.mctt.estadistico");
		TRTT_CNV_ID = rb.getString("titulacion_posgrado.tramite.titulo.cnv.id");
		TRTT_CARRERA_ID = rb.getString("titulacion_posgrado.tramite.titulo.trtt.carrera.id");
		TABLA_UBICACION = rb.getString("titulacion_posgrado.ubicacion");
		UBC_ID = rb.getString("titulacion_posgrado.ubicacion.ubc.id");
		UBC_DESCRIPCION = rb.getString("titulacion_posgrado.ubicacion.ubc.descripcion");
		UBC_JERARQUIA = rb.getString("titulacion_posgrado.ubicacion.ubc.jerarquia");
		UBC_GENTILICIO = rb.getString("titulacion_posgrado.ubicacion.ubc.gentilicio");
		UBC_COD_SNIESE = rb.getString("titulacion_posgrado.ubicacion.ubc.cod.sniese");
		UBC_PADRE = rb.getString("titulacion_posgrado.ubicacion.ubc.padre");
		TABLA_USUARIO = rb.getString("titulacion_posgrado.usuario");
		USR_ID = rb.getString("titulacion_posgrado.usuario.usr.id");
		USR_IDENTIFICACION = rb.getString("titulacion_posgrado.usuario.usr.identificacion");
		USR_NICK = rb.getString("titulacion_posgrado.usuario.usr.nick");
		USR_PASSWORD = rb.getString("titulacion_posgrado.usuario.usr.password");
		USR_FECHA_CREACION = rb.getString("titulacion_posgrado.usuario.usr.fecha.creacion");
		USR_FECHA_CADUCIDAD = rb.getString("titulacion_posgrado.usuario.usr.fecha.caducidad");
		USR_FECHA_CAD_PASS = rb.getString("titulacion_posgrado.usuario.usr.fecha.cad.pass");
		USR_ESTADO = rb.getString("titulacion_posgrado.usuario.usr.estado");
		USR_EST_SESION = rb.getString("titulacion_posgrado.usuario.usr.est.sesion");
		USR_ACTIVE_DIRECTORY = rb.getString("titulacion_posgrado.usuario.usr.active.directory");
		USR_PRS_ID = rb.getString("titulacion_posgrado.usuario.prs.id");
		TABLA_USUARIO_ROL = rb.getString("titulacion_posgrado.usuario.rol");
		USRO_ID = rb.getString("titulacion_posgrado.usuario.rol.usro.id");
		USRO_ESTADO = rb.getString("titulacion_posgrado.usuario.rol.usro.estado");
		USRO_USR_ID = rb.getString("titulacion_posgrado.usuario.rol.usr.id");
		USRO_ROL_ID = rb.getString("titulacion_posgrado.usuario.rol.rol.id");
		TABLA_VALIDACION = rb.getString("titulacion_posgrado.validacion");
		VLD_ID = rb.getString("titulacion_posgrado.validacion.vld.id");
		VLD_SEGUNDA_PRORROGA = rb.getString("titulacion_posgrado.validacion.vld.segunda.prorroga");
		VLD_RSL_PRORROGA = rb.getString("titulacion_posgrado.validacion.vld.rsl.prorroga");
//		VLD_ASIGNADO_TUTOR = rb.getString("titulacion_posgrado.validacion.vld.asignado.tutor");
//		VLD_ULTIMO_SEMESTRE = rb.getString("titulacion_posgrado.validacion.vld.ultimo.semestre");
		VLD_RSL_HOMOLOGACION = rb.getString("titulacion_posgrado.validacion.vld.rsl.homologacion");
		VLD_RSL_ACT_CONOCIMIENTO = rb.getString("titulacion_posgrado.validacion.vld.rsl.act.conocimiento");
//		VLD_RSL_GRATUIDAD = rb.getString("titulacion_posgrado.validacion.vld.rsl.gratuidad");
		VLD_RSL_IDONEIDAD = rb.getString("titulacion_posgrado.validacion.vld.rsl.idoneidad");
		VLD_RSL_ACTUALIZ_APROB = rb.getString("titulacion_posgrado.validacion.vld.rsl.actualiza.aprob");
		VLD_RSL_CURSO_ACT_APROB = rb.getString("titulacion_posgrado.validacion.vld.rsl.curso.act.aprob");
		VLD_RSL_HOMOLOGACION_APROB = rb.getString("titulacion_posgrado.validacion.vld.rsl.homologacion.aprob");
//		VLD_RSL_EXAMEN_COMPLEXIVO = rb.getString("titulacion_posgrado.validacion.vld.rsl.examen.complexivo");
//		VLD_RSL_OTROS_MECANISMOS = rb.getString("titulacion_posgrado.validacion.vld.rsl.otros.mecanismos");
//		VLD_RSL_SLC_MECANISMO = rb.getString("titulacion_posgrado.validacion.vld.rsl.slc.mecanismo");
		VLD_PRTR_ID = rb.getString("titulacion_posgrado.validacion.prtr.id");
//		VLD_FECHA_ASIGNACION_TUTOR_2015=rb.getString("titulacion_posgrado.validacion.vld.fecha.asignacion.tutor.2015");
//		VLD_ASIGNACION_TUTOR_2015=rb.getString("titulacion_posgrado.validacion.vld.asignacion.tutor.2015");
		VLD_COMPROBANTE_PAGO=rb.getString("titulacion_posgrado.validacion.vld.comprobante.pago");
		VLD_APROBACION_INGLES=rb.getString("titulacion_posgrado.validacion.vld.aprobacion.ingles");
		TABLA_VIGENCIA = rb.getString("titulacion_posgrado.vigencia");
		VGN_ID = rb.getString("titulacion_posgrado.vigencia.vgn.id");
		VGN_DESCRIPCION = rb.getString("titulacion_posgrado.vigencia.vgn.descripcion");
	}
}
