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
   
 ARCHIVO:     EstudianteActaGradoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la consulta estudiante acta grado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
26-OCTUBRE-2016		Gabriel Mafla				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteActaGradoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEmisionActaJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteActaGradoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.InstitucionAcademica;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Titulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;

/**
 * EJB EstudianteActaGradoDtoServicioJdbcImpl.
 * Clase Clase donde se implementan los metodos para el servicio jdbc de la consulta estudiante acta de grado.
 * @author gmafla
 * @version 1.0
 */
@Stateless
public class EstudianteActaGradoDtoServicioJdbcImpl implements EstudianteActaGradoDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	

	/**
	 * Método que busca un estudiante por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteActaGradoException 
	 * @throws EstudianteActaGradoNoEncontradoException 
	 * @throws EstudianteActaGradoJdbcDtoException 
	 * @throws EstudianteActaGradoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteActaGradoJdbcDto> buscarEstudianteXIndetificacionXCarreraXConvocatoria(String identificacion, String idSecretaria, CarreraDto carreraDto , int convocatoria) throws EstudianteActaGradoException, EstudianteActaGradoNoEncontradoException  {
		List<EstudianteActaGradoJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			//**** CAMPOS DE ETNIA ****/
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_DESCRIPCION);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_CODIGO_SNIESE);
			//**** CAMPOS DE UBICACION NACIONALIDAD ****/
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_GENTILICIO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_GENTILICIO); 
			sbSql.append(" END AS ");sbSql.append(" ubcNacionalidad");sbSql.append(JdbcConstantes.UBC_GENTILICIO);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_JERARQUIA); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_JERARQUIA); 
			sbSql.append(" END AS ");sbSql.append(" ubcNacionalidad");sbSql.append(JdbcConstantes.UBC_JERARQUIA);
			sbSql.append(" , CASE WHEN ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcNacionalidad");sbSql.append(JdbcConstantes.UBC_PADRE);
			//**** CAMPOS DE FICHA ESTUDIANTE ****/
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			sbSql.append(" ,CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO_COHORTE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_FIN_COHORTE);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			
			
//			sbSql.append(", fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
	
			
			
//			sbSql.append(", fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_CNCR_ID);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			
			//**** CAMPOS DE UBICACION CANTON ****/
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_ID); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_PADRE);
			
			//**** CAMPOS DE UBICACION PROVINCIA ****/
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_ID); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_PADRE);
			
			//**** CAMPOS DE UBICACION PAIS ****/
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_ID); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_PADRE);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_HORA_ACTA_GRADO);
			
			
			//**** CAMPOS DE MECANISMO TITULACION CARRERA ****/
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_ESTADO);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_CRR_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_PORCENTAJE);
			
			//**** CAMPOS DE MECANISMO TITULACION  ****/
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ESTADO);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_CODIGO_SNIESE);
			
			//**** CAMPOS DE INSTITUCION ACADEMICA  ****/
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
			
//			//**** CAMPOS DE TITULO  ****/
//			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_ID);
//			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_ESTADO);
//			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
//			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_SEXO);
//			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_TIPO);
			
			//**** CAMPOS DE TRAMITE TITULO  ****/
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_SUB_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			
			//**** CAMPOS DE CONVOCATORIA ****/
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO_FASE);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_INICIO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_FIN);
			
			//**** CAMPOS DE ASENTAMIENTO_NOTA ****/
			sbSql.append(" ,asno.");sbSql.append(JdbcConstantes.ASNO_PRM_DFN_ORAL);
			sbSql.append(" ,asno.");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			sbSql.append(" ,asno.");sbSql.append(JdbcConstantes.ASNO_TRB_TITULACION_FINAL);
			
			//************ CONDICIONES *******************/
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcNacionalidad ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcCanton ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);sbSql.append(" = ");sbSql.append(" ubcCanton.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcProv ON ");
						sbSql.append(" ubcCanton.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append(" ubcProv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPais ON ");
						sbSql.append(" ubcProv.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append(" ubcPais.");sbSql.append(JdbcConstantes.UBC_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttlBachiller ON ");
//						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);sbSql.append(" = ");sbSql.append(" ttlBachiller.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);						
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);						
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);sbSql.append(" = ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID);sbSql.append(" = ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON ");
						sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);sbSql.append(" = ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);					

			sbSql.append(" WHERE ");
			
			//*************AGREGAR LOS ESTADOS DE TRAMITE PROCESO DE APROBACION DE OTROS MECANISMO ****************/
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" in (");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITADO_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_VALUE);
			sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_VALUE);
			sbSql.append(" )");
			
			
			// Si fue seleccionada la carrera
			if(carreraDto.getCrrId()!= GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND crr.crr_id = ");sbSql.append(carreraDto.getCrrId());sbSql.append(" AND ");	
			}else{
				sbSql.append(" AND crr.crr_id > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" AND ");
			}
						
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" IN ( ");
			sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" from ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);	sbSql.append(" usr ");
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" LIKE ? "); 
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");
			sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");

			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			System.out.println(sbSql);
//			System.out.println(identificacion);
//			System.out.println(idSecretaria);
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt(2, convocatoria);
			pstmt.setString(3, idSecretaria);
//			System.out.println(sbSql.toString());
//			System.out.println(identificacion);
//			System.out.println(convocatoria);
//			System.out.println(idSecretaria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteActaGradoJdbcDto>();
			
			while(rs.next()){
				try {
					int comparador = (rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION)).compareTo(new BigDecimal(7));
					if(comparador== 0 || comparador==1){
						retorno.add(transformarResultSetADtoBuscarEstudianteXIndetificacionXCarreraXConvocatoria(rs));	
					}	
				} catch (Exception e) {
				}
				
			}
			
		} catch (SQLException e) {
			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		} catch (Exception e) {
			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null || retorno.size()<=0){
			throw new EstudianteActaGradoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
		}
		return retorno;
	}
	
	private EstudianteActaGradoJdbcDto transformarResultSetADtoBuscarEstudianteXIndetificacionXCarreraXConvocatoria(ResultSet rs) throws SQLException{
//		int valAux = 0;
		java.sql.Date fecha = null;
		EstudianteActaGradoJdbcDto retorno = new EstudianteActaGradoJdbcDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsTipoIdentificacionSniese(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		retorno.setPrsEtnId(rs.getInt(JdbcConstantes.PRS_ETN_ID));
		retorno.setPrsUbcId(rs.getInt(JdbcConstantes.PRS_UBC_ID));
		try {
			retorno.setPrsUbicacionFoto(rs.getString(JdbcConstantes.PRS_UBICACION_FOTO));
		} catch (Exception e) {
		}
		fecha = rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO);
		if(fecha != null ){
			retorno.setPrsFechaNacimiento(new Date(fecha.getTime()));
		}
		//**** CAMPOS DE ETNIA ****/
		retorno.setEtnId(rs.getInt(JdbcConstantes.ETN_ID));
		retorno.setEtnDescripcion(rs.getString(JdbcConstantes.ETN_DESCRIPCION));
		retorno.setEtnCodigoSniese(rs.getString(JdbcConstantes.ETN_CODIGO_SNIESE));
		
		//**** CAMPOS DE UBICACION NACIONALIDAD ****/
		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		retorno.setUbcGentilicio(rs.getString("ubcNacionalidad"+JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcCodSniese(rs.getString(JdbcConstantes.UBC_COD_SNIESE));
		retorno.setUbcJerarquia(rs.getInt("ubcNacionalidad"+JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcPadre(rs.getInt("ubcNacionalidad"+JdbcConstantes.UBC_PADRE));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE ****/
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcesFechaInicio(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO_COHORTE));
		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_FIN_COHORTE));
		retorno.setFcesFechaActaGrado(rs.getTimestamp(JdbcConstantes.FCES_FECHA_ACTA_GRADO));
		retorno.setFcesNumActaGrado(rs.getString(JdbcConstantes.FCES_NUM_ACTA_GRADO));
		retorno.setFcesFechaRefrendacion(rs.getDate("fces"+JdbcConstantes.FCES_FECHA_REFRENDACION));
		retorno.setFcesNumRefrendacion(rs.getString("fces"+JdbcConstantes.FCES_NUM_REFRENDACION));
		retorno.setFcesCrrEstudPrevios(rs.getString("fces"+JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
		
//		retorno.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
		
		
//		int aux =  rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC);
		if(rs.getInt("fces"+JdbcConstantes.FCES_TIPO_DURAC_REC)==GeneralesConstantes.APP_ID_BASE){
			retorno.setFcesTipoDuracionRec(GeneralesConstantes.APP_ID_BASE);
		}else{
		retorno.setFcesTipoDuracionRec(rs.getInt("fces"+JdbcConstantes.FCES_TIPO_DURAC_REC));
		}
		
		
//		int aux1 =  rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
		if(rs.getInt("fces"+JdbcConstantes.FCES_TIEMPO_ESTUD_REC)==GeneralesConstantes.APP_ID_BASE){
			retorno.setFcesTiempoEstudRecSt("");
		}else{
		retorno.setFcesTiempoEstudRec(rs.getInt("fces"+JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
		}
		
		
		retorno.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
		retorno.setFcesNotaPromAcumulado(rs.getBigDecimal("fces"+JdbcConstantes.FCES_NOTA_PROM_ACUMULADO));
		retorno.setFcesNotaTrabTitulacion(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION));
		retorno.setFcesLinkTesis(rs.getString("fces"+JdbcConstantes.FCES_LINK_TESIS));
		retorno.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
		retorno.setFcesRecEstuPreviosSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		retorno.setFcesHoraActaGrado(rs.getString(JdbcConstantes.FCES_HORA_ACTA_GRADO));
		
		retorno.setFcesTituloBachiller(rs.getString(JdbcConstantes.FCES_TITULO_BACHILLER));
		
		
		
		//**** CAMPOS DE FICHA_ESTUDIANTE UBICACION RESIDENCIA ****/
		retorno.setFcesUbcCantonResidencia(rs.getInt("fces"+JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA));
		//**** UBICACION CANTON ****//
		retorno.setUbcCantonId(rs.getInt("ubcCanton"+JdbcConstantes.UBC_ID));
		retorno.setUbcCantonDescripcion(rs.getString("ubcCanton"+JdbcConstantes.UBC_DESCRIPCION));
//		retorno.setUbcCantonGentilicio(rs.getString("ubcCanton"+JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcCantonCodSniese(rs.getString("ubcCanton"+JdbcConstantes.UBC_COD_SNIESE));
//		retorno.setUbcCantonJerarquia(rs.getInt("ubcCanton"+JdbcConstantes.UBC_JERARQUIA));asd
		retorno.setUbcCantonPadre(rs.getInt("ubcCanton"+JdbcConstantes.UBC_PADRE));
		
		//**** UBICACION PROVINCIA ****//
		retorno.setUbcProvId(rs.getInt("ubcProv"+JdbcConstantes.UBC_ID));
		retorno.setUbcProvDescripcion(rs.getString("ubcProv"+JdbcConstantes.UBC_DESCRIPCION));
//		retorno.setUbcProvGentilicio(rs.getString("ubcProv"+JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcProvCodSniese(rs.getString("ubcProv"+JdbcConstantes.UBC_COD_SNIESE));
//		retorno.setUbcProvJerarquia(rs.getInt("ubcProv"+JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcProvPadre(rs.getInt("ubcProv"+JdbcConstantes.UBC_PADRE));

		//**** UBICACION PAIS ****//
		retorno.setUbcPaisId(rs.getInt("ubcPais"+JdbcConstantes.UBC_ID));
		retorno.setUbcPaisDescripcion(rs.getString("ubcPais"+JdbcConstantes.UBC_DESCRIPCION));
//		retorno.setUbcPaisGentilicio(rs.getString("ubcPais"+JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcPaisCodSniese(rs.getString("ubcPais"+JdbcConstantes.UBC_COD_SNIESE));
//		retorno.setUbcPaisJerarquia(rs.getInt("ubcPais"+JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcPaisPadre(rs.getInt("ubcPais"+JdbcConstantes.UBC_PADRE));

		retorno.setFcesMcttcrId(rs.getInt(JdbcConstantes.FCES_MCCR_ID));
		//**** CAMPOS DE FICHA_ESTUDIANTE MECANISMO TITULACION CARRERA ****/
		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCCR_ID));
		retorno.setMcttcrEstado(rs.getInt(JdbcConstantes.MCCR_ESTADO));
		retorno.setMcttcrCrrId(rs.getInt(JdbcConstantes.MCCR_CRR_ID));
		retorno.setMcttcrMcttId(rs.getInt(JdbcConstantes.MCCR_MCTT_ID));
		retorno.setMcttcrPorcentaje(rs.getInt(JdbcConstantes.MCCR_PORCENTAJE));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE MECANISMO TITULACION ****/		
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		retorno.setMcttCodigoSniese(rs.getString(JdbcConstantes.MCTT_CODIGO_SNIESE));
		retorno.setMcttDescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
		retorno.setMcttEstado(rs.getInt(JdbcConstantes.MCTT_ESTADO));
		
		retorno.setFcesInacIdInstEstPrevios(rs.getInt(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS));		
		//**** CAMPOS DE FICHA_ESTUDIANTE INTITUCION ACADEMICA ****/
		retorno.setInacId(rs.getInt(JdbcConstantes.INAC_ID));
		retorno.setInacDescripcion(rs.getString(JdbcConstantes.INAC_DESCRIPCION));
		retorno.setInacCodigoSniese(rs.getString(JdbcConstantes.INAC_CODIGO_SNIESE));
		retorno.setInacNivel(rs.getInt(JdbcConstantes.INAC_NIVEL));
		retorno.setInacTipo(rs.getInt(JdbcConstantes.INAC_TIPO));
		retorno.setInacTipoSniese(rs.getString(JdbcConstantes.INAC_TIPO_SNIESE));
		retorno.setInacubcId(rs.getInt(JdbcConstantes.INAC_UBC_ID));
		
		retorno.setFcesPrsId(rs.getInt(JdbcConstantes.FCES_PRS_ID));		
		//**** CAMPOS DE FICHA_ESTUDIANTE TRAMITE TITULO ****/
		retorno.setFcesTrttId(rs.getInt(JdbcConstantes.FCES_TRTT_ID));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttNumTramite(rs.getString(JdbcConstantes.TRTT_NUM_TRAMITE));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		retorno.setTrttSubId(rs.getInt(JdbcConstantes.TRTT_SUB_ID));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		retorno.setTrttCnvId(rs.getInt(JdbcConstantes.TRTT_CNV_ID));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE - TRAMITE_TITULO - CONVOCATORIA ****/
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		retorno.setCnvEstado(rs.getInt(JdbcConstantes.CNV_ESTADO));
		retorno.setCnvEstadoFase(rs.getInt(JdbcConstantes.CNV_ESTADO_FASE));
		retorno.setCnvFechaInicio(rs.getDate(JdbcConstantes.CNV_FECHA_INICIO));
		retorno.setCnvFechaFin(rs.getDate(JdbcConstantes.CNV_FECHA_FIN));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE - TRAMITE_TITULO - CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrFclId(rs.getInt(JdbcConstantes.CRR_FCL_ID));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE - TRAMITE_TITULO - CARRERA - FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE ****//
//		retorno.setFcesTituloBachillerId(rs.getInt(JdbcConstantes.TTL_ID));
//		//**** CAMPOS DE FICHA_ESTUDIANTE - TRAMITE_TITULO - CARRERA - FACULTAD ****//
//		retorno.setTtlId(rs.getInt(JdbcConstantes.TTL_ID));
//		retorno.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
//		retorno.setTtlSexo(rs.getInt(JdbcConstantes.TTL_SEXO));
//		retorno.setTtlTipo(rs.getInt(JdbcConstantes.TTL_TIPO));
//		retorno.setTtlEstado(rs.getInt(JdbcConstantes.TTL_ESTADO));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE ****//
		retorno.setFcesCncrId(rs.getInt("fces"+JdbcConstantes.FCES_CNCR_ID));
		
		//CAMPOS DE ASENTAMIENTO NOTA
		try {
			retorno.setAsnoPrmDfnOral(rs.getBigDecimal(JdbcConstantes.ASNO_PRM_DFN_ORAL));
		} catch (Exception e) {
		}
		try {
			retorno.setAsnoPrmDfnEscrito(rs.getBigDecimal(JdbcConstantes.ASNO_PRM_TRB_ESCRITO));
		} catch (Exception e) {
		}
		try {
			retorno.setAsnoTrabTitulacionFinal(rs.getBigDecimal(JdbcConstantes.ASNO_TRB_TITULACION_FINAL));
		} catch (Exception e) {
		}
		return retorno;
	}
	
	/**
	 * Método que busca un estudiante por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteActaGradoException 
	 * @throws EstudianteActaGradoNoEncontradoException 
	 * @throws EstudianteActaGradoJdbcDtoException 
	 * @throws EstudianteActaGradoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteActaGradoJdbcDto> buscarEstudianteParaEdicionActaXIndetificacionXCarreraXConvocatoria(EstudianteActaGradoJdbcDto entidad, String idSecretaria) throws EstudianteActaGradoException, EstudianteActaGradoNoEncontradoException  {
		List<EstudianteActaGradoJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);
			//**** CAMPOS DE ETNIA ****/
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_DESCRIPCION);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_CODIGO_SNIESE);
			//**** CAMPOS DE UBICACION NACIONALIDAD ****/
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_GENTILICIO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_GENTILICIO); 
			sbSql.append(" END AS ");sbSql.append(" ubcNacionalidad");sbSql.append(JdbcConstantes.UBC_GENTILICIO);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_JERARQUIA); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_JERARQUIA); 
			sbSql.append(" END AS ");sbSql.append(" ubcNacionalidad");sbSql.append(JdbcConstantes.UBC_JERARQUIA);
			sbSql.append(" , CASE WHEN ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcNacionalidad");sbSql.append(JdbcConstantes.UBC_PADRE);
			//**** CAMPOS DE FICHA ESTUDIANTE ****/
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_FECHA_REFRENDACION);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_NUM_REFRENDACION);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			
			
//			sbSql.append(", fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
	
			
			
//			sbSql.append(", fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID); 
			sbSql.append(" END AS ");sbSql.append(" fces");sbSql.append(JdbcConstantes.FCES_CNCR_ID);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			
			//**** CAMPOS DE UBICACION CANTON ****/
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_ID); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcCanton.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcCanton.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcCanton");sbSql.append(JdbcConstantes.UBC_PADRE);
			
			//**** CAMPOS DE UBICACION PROVINCIA ****/
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_ID); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcProv.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcProv.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcProv");sbSql.append(JdbcConstantes.UBC_PADRE);
			
			//**** CAMPOS DE UBICACION PAIS ****/
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_ID); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			sbSql.append(" , CASE WHEN ubcPais.");sbSql.append(JdbcConstantes.UBC_PADRE); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append("  ubcPais.");sbSql.append(JdbcConstantes.UBC_PADRE); 
			sbSql.append(" END AS ");sbSql.append(" ubcPais");sbSql.append(JdbcConstantes.UBC_PADRE);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_HORA_ACTA_GRADO);
			
			
			//**** CAMPOS DE MECANISMO TITULACION CARRERA ****/
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_ESTADO);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_CRR_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_PORCENTAJE);
			
			//**** CAMPOS DE MECANISMO TITULACION  ****/
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ESTADO);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_CODIGO_SNIESE);
			
			//**** CAMPOS DE INSTITUCION ACADEMICA  ****/
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
			sbSql.append(" ,inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
			
			//**** CAMPOS DE TITULO  ****/
			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_ESTADO);
			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_SEXO);
			sbSql.append(" ,ttlBachiller.");sbSql.append(JdbcConstantes.TTL_TIPO);
			
			//**** CAMPOS DE TRAMITE TITULO  ****/
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_SUB_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			
			//**** CAMPOS DE CONVOCATORIA ****/
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO_FASE);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_INICIO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_FIN);
			
			//************ CONDICIONES *******************/
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcNacionalidad ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcCanton ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);sbSql.append(" = ");sbSql.append(" ubcCanton.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcProv ON ");
						sbSql.append(" ubcCanton.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append(" ubcProv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPais ON ");
						sbSql.append(" ubcProv.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append(" ubcPais.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttlBachiller ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);sbSql.append(" = ");sbSql.append(" ttlBachiller.");sbSql.append(JdbcConstantes.TTL_ID);						
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);						
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);sbSql.append(" = ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID);sbSql.append(" = ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON ");
						sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);sbSql.append(" = ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);					

			sbSql.append(" WHERE ");
			
			//*************AGREGAR LOS ESTADOS DE TRAMITE PROCESO DE APROBACION DE OTROS MECANISMO ****************/
			sbSql.append(" (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE);
			sbSql.append(" )");
			
			
			
						
			//identificacion
			sbSql.append(" AND UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			
			if(entidad.getCnvId() != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}
			
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" IN ( ");
			sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" from ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);	sbSql.append(" usr ");
			sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" LIKE ? "); 
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");
			sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");

			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(entidad.getPrsIdentificacion().toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt(2, entidad.getCnvId());
			pstmt.setString(3, idSecretaria);
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteActaGradoJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarEstudianteXIndetificacionXCarreraXConvocatoria(rs));
			}
			
		} catch (SQLException e) {
			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("HabilitarEdicion.sin.resultados")));
		} catch (Exception e) {
			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null || retorno.size()<=0){
			throw new EstudianteActaGradoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
		}
		return retorno;
	}
	
	
	public boolean editarActaGrado(EstudianteActaGradoJdbcDto estudiante, Etnia etnia, 
			Ubicacion ubicacionNac, Ubicacion ubicacionRes , 
			InstitucionAcademica inacFces , ConfiguracionCarrera cncrFces , Titulo ttlFces , RolFlujoCarrera rolFlujoCarrera,int tipoActa) 
					throws  EstudianteActaGradoNoEncontradoException , EstudianteActaGradoException{
		boolean retorno = false;
		
		if (estudiante != null && etnia != null && ubicacionNac != null 
				&& ubicacionRes != null
//				&& inacFces != null
				&& cncrFces != null
				) {
		
			try {
				Persona personaAux = em.find(Persona.class, estudiante.getPrsId());
				
				personaAux.setPrsTipoIdentificacion(estudiante.getPrsTipoIdentificacion());
				personaAux.setPrsTipoIdentificacionSniese(estudiante.getPrsTipoIdentificacionSniese());
				personaAux.setPrsIdentificacion(estudiante.getPrsIdentificacion());
				personaAux.setPrsPrimerApellido(estudiante.getPrsPrimerApellido());
				personaAux.setPrsSegundoApellido(estudiante.getPrsSegundoApellido());
				personaAux.setPrsFechaNacimiento(estudiante.getPrsFechaNacimiento());
				personaAux.setPrsNombres(estudiante.getPrsNombres());
				personaAux.setPrsSexo(estudiante.getPrsSexo());
				personaAux.setPrsSexoSniese(estudiante.getPrsSexoSniese());
				personaAux.setPrsMailInstitucional(estudiante.getPrsMailInstitucional());
				personaAux.setPrsMailPersonal(estudiante.getPrsMailPersonal());
				personaAux.setPrsTelefono(estudiante.getPrsTelefono());
				personaAux.setPrsEtnia(etnia);
				personaAux.setPrsUbicacionNacionalidad(ubicacionNac);
				em.merge(personaAux);
				em.flush();
				
				FichaEstudiante fcesAux = em.find(FichaEstudiante.class, estudiante.getFcesId());
				
				fcesAux.setFcesFechaInicio(estudiante.getFcesFechaInicio());
				fcesAux.setFcesFechaEgresamiento(estudiante.getFcesFechaEgresamiento());
				fcesAux.setFcesFechaActaGrado(estudiante.getFcesFechaActaGrado());
				fcesAux.setFcesNumActaGrado(estudiante.getFcesNumActaGrado());
				fcesAux.setFcesFechaRefrendacion(estudiante.getFcesFechaRefrendacion());
				fcesAux.setFcesTituloBachiller(estudiante.getFcesTituloBachiller().toUpperCase());
				try {
					fcesAux.setFcesCrrEstudPrevios(GeneralesUtilidades.eliminarTildes(estudiante.getFcesCrrEstudPrevios().toUpperCase()));
				} catch (Exception e) {
					fcesAux.setFcesCrrEstudPrevios(null);
				}
				
			
				fcesAux.setFcesTiempoEstudRec(estudiante.getFcesTiempoEstudRec());
				try {
					if(estudiante.getFcesTipoDuracionRec()==-99 || estudiante.getFcesInacIdInstEstPrevios() == GeneralesConstantes.APP_ID_BASE) {
						
					}else{
							fcesAux.setFcesTipoDuracRec(estudiante.getFcesTipoDuracionRec());
					}
				} catch (Exception e) {
					fcesAux.setFcesTipoDuracRec(null);
					fcesAux.setFcesTiempoEstudRec(null);
				}
				
				fcesAux.setFcesTipoColegio(estudiante.getFcesTipoColegio());
				fcesAux.setFcesTipoColegioSniese(estudiante.getFcesTipoColegioSniese());
				fcesAux.setFcesNotaPromAcumulado(estudiante.getFcesNotaPromAcumulado());
				fcesAux.setFcesNotaTrabTitulacion(estudiante.getFcesNotaTrabTitulacion());
				fcesAux.setFcesLinkTesis(estudiante.getFcesLinkTesis());
				fcesAux.setFcesRecEstudPrevios(estudiante.getFcesRecEstuPrevios());
				if(fcesAux.getFcesRecEstudPrevios()==FichaEstudianteConstantes.RECON_ESTUD_PREVIOS_SI_VALUE){
					fcesAux.setFcesRecEstudPrevSniese(String.valueOf(FichaEstudianteConstantes.RECON_ESTUD_PREVIOS_SNIESE_SI_VALUE));
				}else if(fcesAux.getFcesRecEstudPrevios()==FichaEstudianteConstantes.RECON_ESTUD_PREVIOS_NO_VALUE){
					fcesAux.setFcesRecEstudPrevSniese(String.valueOf(FichaEstudianteConstantes.RECON_ESTUD_PREVIOS_SNIESE_NO_VALUE));
				}
				
//				fcesAux.setFcesRecEstudPrevSniese(String.valueOf(estudiante.getFcesRecEstuPrevios()+1));
				fcesAux.setFcesHoraActaGrado(estudiante.getFcesHoraActaGrado());
				
				fcesAux.setFcesInacEstPrevios(inacFces);
				
				fcesAux.setFcesConfiguracionCarrera(cncrFces);
				em.merge(fcesAux);
				em.flush();
				//**********************************************************************
				//********** actualizar el tramite actual               ****************
				//**********************************************************************
				
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, estudiante.getTrttId());
				if(estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_VALUE
						||estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE
						||estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_VALUE ){
					trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE);
				}
				if(estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE
						||estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE
						||estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE
						||estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE
						||estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_VALUE){
					trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE);
					
				}
				em.merge(trttAux);
				em.flush();
				//**********************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el historial ***********
				//**********************************************************************
				if(estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE
						|| estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITADO_VALUE){
					trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE);
					ProcesoTramite nuevoPrtr = new ProcesoTramite();
					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_EMISION_TITULO_EDICION_VALUE);
					nuevoPrtr.setPrtrFechaEjecucion(estudiante.getPrtrFechaEjecucion());
					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
					nuevoPrtr.setPrtrTramiteTitulo(trttAux);
					em.persist(nuevoPrtr);
					
				}else if (estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE
						|| estudiante.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_VALUE){
					trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE);
					ProcesoTramite nuevoPrtr = new ProcesoTramite();
					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_EMISION_TITULO_EDICION_VALUE);
					nuevoPrtr.setPrtrFechaEjecucion(estudiante.getPrtrFechaEjecucion());
					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
					nuevoPrtr.setPrtrTramiteTitulo(trttAux);
					em.persist(nuevoPrtr);
				}else{
//					TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE
					ProcesoTramite nuevoPrtr = new ProcesoTramite();
					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_EMISION_ACTA_VALUE);
					nuevoPrtr.setPrtrFechaEjecucion(estudiante.getPrtrFechaEjecucion());
					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
					nuevoPrtr.setPrtrTramiteTitulo(trttAux);
					em.persist(nuevoPrtr);
				}
				em.flush();
//				if(tipoActa!=-99){
//					Carrera crrAux = em.find(Carrera.class, estudiante.getTrttCarreraId());
//					crrAux.setCrrNumActaGrado(tipoActa);	
//				}
				retorno=true;
			} catch (NoResultException e) {
				e.printStackTrace();
				throw new EstudianteActaGradoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteActaGradoDtoServicioJdbcImpl.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e){
				e.printStackTrace();
				throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteActaGradoDtoServicioJdbcImpl.buscar.por.id.non.unique.result.exception")));
			}catch (Exception e) {
				e.printStackTrace();
				throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteActaGradoDtoServicioJdbcImpl.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	@Override
	public boolean modificarEstadoActaGrado(
			EstudianteActaGradoJdbcDto estudiante, RolFlujoCarrera rolFlujoCarrera)
			throws EstudianteActaGradoNoEncontradoException,
			EstudianteActaGradoException {
		boolean retorno = false;
		try {
			//**********************************************************************
			//********** actualizar el tramite actual ******************************
			//**********************************************************************
			
			TramiteTitulo trttAux = em.find(TramiteTitulo.class, estudiante.getTrttId());
			if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
			}
			trttAux.setTrttObservacion(estudiante.getTrttObservacionDga());
			ProcesoTramite nuevoPrtr = new ProcesoTramite();
			nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_LISTO_EDICION_VALUE);
			nuevoPrtr.setPrtrFechaEjecucion(estudiante.getPrtrFechaEjecucion());
			nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
			nuevoPrtr.setPrtrTramiteTitulo(trttAux);
			em.persist(nuevoPrtr);
			retorno=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	
	@Override
	public boolean modificarEstadoActaGradoFinal(
			EstudianteEmisionActaJdbcDto estudiante, RolFlujoCarrera rolFlujoCarrera)
			throws EstudianteActaGradoNoEncontradoException,
			EstudianteActaGradoException {
		boolean retorno = false;
		try {
			//**********************************************************************
			//********** actualizar el tramite actual ******************************
			//**********************************************************************
			TramiteTitulo trttAux = em.find(TramiteTitulo.class, estudiante.getTrttId());
			if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE);
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTA_IMPRESA_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(estudiante.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTA_IMPRESA_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(estudiante.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			}
			retorno=true;
		} catch (Exception e) {
		}
		return retorno;
	}
	
	
	@Override
	public void actualizarAutoridadesComplexivo(Integer fcesId, String autoridad, Integer sexo, Integer tipo, Integer porcentaje){
		
		FichaEstudiante fces = em.find(FichaEstudiante.class, fcesId);
//		switch (tipo) {
//		case 1:
//			fces.setFcesDecano(autoridad);
//			fces.setFcesDecanoSexo(sexo);
//			fces.setFcesPorcentajeCOmplex(porcentaje);
//			break;
//		case 2:
//			fces.setFcesSubDecano(autoridad);
//			fces.setFcesSubDecanoSexo(sexo);
//			fces.setFcesPorcentajeCOmplex(porcentaje);
//			break;
//		case 3:
//			fces.setFcesSecretario(autoridad);
//			fces.setFcesSecretarioSexo(sexo);
//			fces.setFcesPorcentajeCOmplex(porcentaje);
//			break;
//		case 4:
//			fces.setFcesDirector(autoridad);
//			fces.setFcesDirectorSexo(sexo);
//			fces.setFcesPorcentajeCOmplex(porcentaje);
//			break;
//		default:
//			break;
//		}
		
		
	}
	
}
