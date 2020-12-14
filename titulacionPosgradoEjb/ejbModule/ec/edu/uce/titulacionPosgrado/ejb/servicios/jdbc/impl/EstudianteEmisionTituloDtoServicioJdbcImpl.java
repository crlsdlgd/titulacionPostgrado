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
   
 ARCHIVO:     EstudianteEmisionTituloDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se consulta todos los datos del estudiante para emigrar a EmisionTitulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 27-JULIO-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteTitulacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PersonaDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.InstitucionAcademicaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.MecanismoTitulacionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.MecanismoTitulacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UbicacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteEmisionTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;



/**
 * EJB EstudianteEmisionTituloDtoServicioJdbcImpl.
 * Class Clase donde se consulta todos los datos del estudiante para emigrar a EmisionTitulo.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class EstudianteEmisionTituloDtoServicioJdbcImpl implements EstudianteEmisionTituloDtoServicioJdbc{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	
	//@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_EMISION_TITULO)
	DataSource dsEmision;
	
	@EJB
	private PersonaServicio srvPersonaServicio;
	@EJB private EtniaServicio servRpfEtnia;
	@EJB private UbicacionServicio servUbicacionServicio;
	@EJB private FichaEstudianteServicio servFichaEstudianteServicio;
	@EJB private MecanismoTitulacionServicio servMecanismoTitulacionServicio;
	@EJB private TituloServicio servTituloServicio;
	@EJB private InstitucionAcademicaServicio servInstitucionAcademicaServicio;
	@EJB private ConfiguracionCarreraServicio servConfiguracionCarreraServicio;
	@EJB private MecanismoTitulacionCarreraServicio servMecanismoTitulacionCarreraServicio;
	@EJB private PersonaDtoServicioJdbc servPersonaDtoServicio;
	/**
	 * Método que devuelve todos los datos de graduacion de un estudiante
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<EstudianteTitulacionJdbcDto> listarPersonas()
			throws PersonaDtoJdbcException {
		Connection con = null;
		List<EstudianteTitulacionJdbcDto> retorno = new ArrayList<EstudianteTitulacionJdbcDto>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			StringBuffer ssql = new StringBuffer();
			ssql.append(" SELECT ");
			ssql.append(" to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			ssql.append(" ,TRIM(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			ssql.append(" , CASE WHEN fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE ");
			ssql.append(" TRIM(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" END AS ");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_FECHA_CREACION);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);
			ssql.append(" ,TRIM(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.TTL_DESCRIPCION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_IDENTIFICACION);
			ssql.append(" ,TRIM(TRANSLATE(prs.");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(" , CASE WHEN prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE ");
			ssql.append(" TRIM(TRANSLATE(prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" END AS ");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(" ,TRIM(TRANSLATE(prs.");
			ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  ')");//,'   ',' '),'\"',' '),' ')");
			ssql.append(")");
			ssql.append(" AS ");ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TELEFONO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_ETN_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBC_ID);
			ssql.append(" ,mcttcr.");ssql.append(JdbcConstantes.MCCR_MCTT_ID);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_DETALLE);
			ssql.append(" FROM ");
			ssql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);ssql.append(" fces, ");
			ssql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);ssql.append(" trtt, ");
			ssql.append(JdbcConstantes.TABLA_PERSONA);ssql.append(" prs, ");
			ssql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);ssql.append(" mcttcr, ");
			ssql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);ssql.append(" cncr, ");
			ssql.append(JdbcConstantes.TABLA_CARRERA);ssql.append(" crr ");
			ssql.append(" WHERE fces.");ssql.append(JdbcConstantes.FCES_TRTT_ID);ssql.append(" = trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_PRS_ID);ssql.append(" = prs.");ssql.append(JdbcConstantes.PRS_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);ssql.append(" = mcttcr.");ssql.append(JdbcConstantes.MCCR_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);ssql.append(" = cncr.");ssql.append(JdbcConstantes.CNCR_ID);
			ssql.append(" AND cncr.");ssql.append(JdbcConstantes.CNCR_CRR_ID);ssql.append(" = crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);ssql.append(" IN (");
			ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);
			ssql.append(",");ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE);
			ssql.append(",");ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_VALUE);
			ssql.append(",");ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITADO_VALUE);
			ssql.append(")");
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			ssql.append(" = ");ssql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			ssql.append(" ORDER BY crr.crr_id DESC");
			pstmt = con.prepareStatement(ssql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(resultSetAdtoEstudianteTitulacion(rs));
			}
		} catch (SQLException e) {
			throw new PersonaDtoJdbcException(e);
		} catch (Exception e) {
			throw new PersonaDtoJdbcException(e);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno==null || retorno.size()<=0){
			throw new PersonaDtoJdbcException("No existen estudiantes para migrar");
		}
		return retorno;
	}
	
	@Override
	public List<EstudianteTitulacionJdbcDto> listarPersonasMigradasPorFecha()
			throws PersonaDtoJdbcException {
		Connection con = null;
		List<EstudianteTitulacionJdbcDto> retorno = new ArrayList<EstudianteTitulacionJdbcDto>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			StringBuffer ssql = new StringBuffer();
			ssql.append(" SELECT ");
			ssql.append(" to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			ssql.append(" ,TRIM(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			ssql.append(" , CASE WHEN fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE ");
			ssql.append(" TRIM(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" END AS ");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_FECHA_CREACION);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);
			ssql.append(" ,TRIM(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.TTL_DESCRIPCION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_IDENTIFICACION);
			ssql.append(" ,TRIM(TRANSLATE(prs.");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(" , CASE WHEN prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE ");
			ssql.append(" TRIM(TRANSLATE(prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '))");//,'   ',' '),'\"',' '),' ')");
			ssql.append(" END AS ");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(" ,TRIM(TRANSLATE(prs.");
			ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  ')");//,'   ',' '),'\"',' '),' ')");
			ssql.append(")");
			ssql.append(" AS ");ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TELEFONO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_ETN_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBC_ID);
			ssql.append(" ,mcttcr.");ssql.append(JdbcConstantes.MCCR_MCTT_ID);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_DETALLE);
			ssql.append(" FROM ");
			ssql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);ssql.append(" fces, ");
			ssql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);ssql.append(" trtt, ");
			ssql.append(JdbcConstantes.TABLA_PERSONA);ssql.append(" prs, ");
			ssql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);ssql.append(" mcttcr, ");
			ssql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);ssql.append(" cncr, ");
			ssql.append(JdbcConstantes.TABLA_CARRERA);ssql.append(" crr ");
			ssql.append(" WHERE fces.");ssql.append(JdbcConstantes.FCES_TRTT_ID);ssql.append(" = trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_PRS_ID);ssql.append(" = prs.");ssql.append(JdbcConstantes.PRS_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);ssql.append(" = mcttcr.");ssql.append(JdbcConstantes.MCCR_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);ssql.append(" = cncr.");ssql.append(JdbcConstantes.CNCR_ID);
			ssql.append(" AND cncr.");ssql.append(JdbcConstantes.CNCR_CRR_ID);ssql.append(" = crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);ssql.append(" >= ");
			ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE);
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			ssql.append(" = ");ssql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
//			ssql.append(" > ");ssql.append("TO_DATE('01-08-2019 00:00:00', 'DD/MM/YYYY HH24:MI:SS')");
			ssql.append(" > ");ssql.append("TO_DATE(SYSDATE - 7, 'DD/MM/YYYY HH24:MI:SS')");
			
//			System.out.println(ssql);
			ssql.append(" ORDER BY crr.crr_id DESC");
			pstmt = con.prepareStatement(ssql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(resultSetAdtoEstudianteTitulacion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersonaDtoJdbcException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersonaDtoJdbcException(e);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno==null || retorno.size()<=0){
			throw new PersonaDtoJdbcException("No existen estudiantes para migrar");
		}
		return retorno;
	}
	
	/**
	 * Método que devuelve todos los datos de graduacion de un estudiante
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<EstudianteTitulacionJdbcDto> listarPersonasRevision()
			throws PersonaDtoJdbcException {
		Connection con = null;
		List<EstudianteTitulacionJdbcDto> retorno = new ArrayList<EstudianteTitulacionJdbcDto>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			StringBuffer ssql = new StringBuffer();
			ssql.append(" SELECT ");
			ssql.append(" to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			ssql.append(" ,TRIM(REPLACE(REPLACE(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '),'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			ssql.append(" , CASE WHEN fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE ");
			ssql.append(" TRIM(REPLACE(REPLACE(TRANSLATE(fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '),'   ',' '),'\"',' '),' ')");
			ssql.append(" END AS ");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_FECHA_CREACION);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);
			ssql.append(" ,TRIM(REPLACE(REPLACE(TRANSLATE(ttl.");ssql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '),'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.TTL_DESCRIPCION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_IDENTIFICACION);
			ssql.append(" ,TRIM(REPLACE(REPLACE(TRANSLATE(prs.");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '),'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(" , CASE WHEN prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE ");
			ssql.append(" TRIM(REPLACE(REPLACE(TRANSLATE(prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '),'   ',' '),'\"',' '),' ')");
			ssql.append(" END AS ");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(" ,TRIM(REPLACE(REPLACE(TRANSLATE(prs.");ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(",'ÀÁÂÃÄÅÉÈËÊÌÍÎÏÒÓÔÕÖÙÚÛÜàáâãäåèéêëìíîïòóôõöùúûüÇç-/,.:', 'AAAAAAEEEEIIIIOOOOOUUUUaaaaaaeeeeiiiiooooouuuuCc  '),'   ',' '),'\"',' '),' ')");
			ssql.append(" AS ");ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TELEFONO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_ETN_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBC_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			ssql.append(" ,mcttcr.");ssql.append(JdbcConstantes.MCCR_MCTT_ID);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_DETALLE);
			ssql.append(" FROM ");
			ssql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);ssql.append(" fces, ");
			ssql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);ssql.append(" trtt, ");
			ssql.append(JdbcConstantes.TABLA_PERSONA);ssql.append(" prs, ");
			ssql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);ssql.append(" mcttcr, ");
			ssql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);ssql.append(" cncr, ");
			ssql.append(JdbcConstantes.TABLA_TITULO);ssql.append(" ttl, ");
			ssql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);ssql.append(" prtr, ");
			ssql.append(JdbcConstantes.TABLA_CARRERA);ssql.append(" crr ");
			ssql.append(" WHERE fces.");ssql.append(JdbcConstantes.FCES_TRTT_ID);ssql.append(" = trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_PRS_ID);ssql.append(" = prs.");ssql.append(JdbcConstantes.PRS_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);ssql.append(" = mcttcr.");ssql.append(JdbcConstantes.MCCR_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);ssql.append(" = cncr.");ssql.append(JdbcConstantes.CNCR_ID);
			ssql.append(" AND cncr.");ssql.append(JdbcConstantes.CNCR_CRR_ID);ssql.append(" = crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);ssql.append(" IN (");
			ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE);
			ssql.append(",");ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE);
			ssql.append(")");
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			ssql.append(" = ");ssql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			ssql.append(" AND prtr.");ssql.append(JdbcConstantes.PRTR_TRTT_ID);ssql.append(" = trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" AND prtr.");ssql.append(JdbcConstantes.PRTR_TIPO_PROCESO);ssql.append(" in (");
			ssql.append(ProcesoTramiteConstantes.TIPO_PROCESO_ACTA_IMPRESA_VALUE);ssql.append(" ,");
			ssql.append(ProcesoTramiteConstantes.TIPO_PROCESO_MIGRACION_EMISION_TITULO_VALUE);ssql.append(" ) ");
			ssql.append(" AND prtr.");ssql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);ssql.append(" < ");
			ssql.append(" (SELECT CURRENT_DATE + INTERVAL '1 day')");
			ssql.append(" AND prtr.");ssql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);ssql.append(" > ");
			ssql.append(" (SELECT CURRENT_DATE - INTERVAL '7 day') ");
//			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_ESTADO_MIGRADO);
//			ssql.append(" = ");ssql.append(FichaEstudianteConstantes.TIPO_FCES_ESTADO_MIGRADO_AUTOMATICO_VALUE);
			ssql.append(" ORDER BY crr.crr_id DESC");
			pstmt = con.prepareStatement(ssql.toString());
			rs = pstmt.executeQuery();

			while(rs.next()){
				retorno.add(resultSetAdtoEstudianteTitulacion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersonaDtoJdbcException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersonaDtoJdbcException(e);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno==null || retorno.size()<=0){
			throw new PersonaDtoJdbcException("No existen estudiantes para migrar");
		}
		return retorno;
	}
	
	
	private EstudianteTitulacionJdbcDto resultSetAdtoEstudianteTitulacion(ResultSet rs) throws SQLException {
		EstudianteTitulacionJdbcDto retorno = new EstudianteTitulacionJdbcDto();
		retorno.setFcesFechaInicio((rs.getDate("fces_fecha_inicio")));
		retorno.setFcesFechaEgresamiento((rs.getDate("fces_fecha_egresamiento")));
		retorno.setFcesFechaActaGrado((rs.getDate("fces_fecha_acta_grado")));
		retorno.setFcesNumActaGrado((rs.getString("fces_num_acta_grado")));
		retorno.setFcesCrrEstudPrevios((rs.getString("fces_crr_estud_previos")));
		retorno.setFcesTiempoEstudRec((rs.getInt("fces_tiempo_estud_rec")));
		retorno.setFcesTipoDuracionRec((rs.getInt("fces_tipo_durac_rec")));
		retorno.setFcesTipoColegio((rs.getInt("fces_tipo_colegio")));
		retorno.setFcesTipoColegioSniese((rs.getString("fces_tipo_colegio_sniese")));
		retorno.setFcesNotaPromAcumulado((rs.getBigDecimal("fces_nota_prom_acumulado")));
		retorno.setFcesNotaTrabTitulacion((rs.getBigDecimal("fces_nota_trab_titulacion")));
		if(rs.getString("fces_link_tesis") == null){
			retorno.setFcesLinkTesis(new String());
		}else{
			retorno.setFcesLinkTesis((rs.getString("fces_link_tesis")));
		}
		retorno.setFcesRecEstuPrevios((rs.getInt("fces_rec_estud_previos")));
		retorno.setFcesRecEstuPreviosSniese((rs.getString("fces_rec_estud_previos")));
		retorno.setFcesFechaCreacion(new Timestamp(new Date().getTime()));
		retorno.setFcesCantonResidenciaId((rs.getInt("ubc_canton_residencia")));
		retorno.setFcesMecanismoTitulacionId((rs.getInt("mccr_id")));
		
		retorno.setFcesInacInstEstPreviosId((rs.getInt("inac_id_inst_est_previos")));

		retorno.setFcesConfCarreraId((rs.getInt("cncr_id")));
		retorno.setFcesTituloBachiller((rs.getString("ttl_descripcion")));
		retorno.setPrsTipoIdentificacion((rs.getInt("prs_tipo_identificacion")));
		retorno.setPrsTipoIdentificacionSniese((rs.getInt("prs_tipo_identificacion_sniese")));
		retorno.setPrsIdentificacion((rs.getString("prs_identificacion")));
		retorno.setPrsPrimerApellido((rs.getString("prs_primer_apellido")));
		retorno.setPrsSegundoApellido((rs.getString("prs_segundo_apellido")));
		retorno.setPrsNombres((rs.getString("prs_nombres")));
		retorno.setPrsSexo((rs.getInt("prs_sexo")));
		retorno.setPrsSexoSniese((rs.getInt("prs_sexo_sniese")));
		retorno.setPrsMailPersonal((rs.getString("prs_mail_personal")));
		retorno.setPrsMailInstitucional((rs.getString("prs_mail_institucional")));
		retorno.setPrsTelefono((rs.getString("prs_telefono")));
		retorno.setPrsFechaNacimiento((rs.getDate("prs_fecha_nacimiento")));
		retorno.setPrsFechaNacimiento((rs.getDate("prs_fecha_nacimiento")));
		retorno.setPrsEtniaId((rs.getInt("etn_id")));
		retorno.setPrsUbicacionId((rs.getInt("ubc_id")));
		retorno.setPrsUbicacionFoto((rs.getString("prs_ubicacion_foto")));
		retorno.setFcesMecanismoTitulacionId((rs.getInt("mctt_id")));
		retorno.setCrrId((rs.getInt("crr_id")));
		retorno.setCrrDetalle(rs.getString("crr_detalle"));
		retorno.setTrttId((rs.getInt("trtt_id")));
		retorno.setTrttEstadoProceso((rs.getInt("trtt_estado_proceso")));
		return retorno;
	}

	/**
	 * Método que devuelve todos los datos de graduacion de un estudiante que fue devuelto de emisionTitulo
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<EstudianteTitulacionJdbcDto> buscarRetornadosEmisionTitulos(String cedula, Integer crrId)
			throws PersonaDtoJdbcException {
		Connection con = null;
		List<EstudianteTitulacionJdbcDto> retorno = new ArrayList<EstudianteTitulacionJdbcDto>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			StringBuffer ssql = new StringBuffer();
			ssql.append(" SELECT ");
			ssql.append(" fces.");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			ssql.append(" , CASE WHEN fces.");ssql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); ssql.append(" IS NULL THEN ");ssql.append(GeneralesConstantes.APP_ID_BASE);
			ssql.append(" ELSE fces.");ssql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); 
			ssql.append(" END AS ");ssql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			ssql.append(" , CASE WHEN fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS); 
			ssql.append(" END AS ");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_FECHA_CREACION);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_IDENTIFICACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(" , CASE WHEN prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			ssql.append(" END AS ");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TELEFONO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_ETN_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBC_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			ssql.append(" ,mcttcr.");ssql.append(JdbcConstantes.MCCR_MCTT_ID);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_DETALLE);
			ssql.append(" FROM ");
			ssql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);ssql.append(" fces, ");
			ssql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);ssql.append(" trtt, ");
			ssql.append(JdbcConstantes.TABLA_PERSONA);ssql.append(" prs, ");
			ssql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);ssql.append(" mcttcr, ");
			ssql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);ssql.append(" cncr, ");
			ssql.append(JdbcConstantes.TABLA_TITULO);ssql.append(" ttl, ");
			ssql.append(JdbcConstantes.TABLA_CARRERA);ssql.append(" crr ");
			ssql.append(" WHERE fces.");ssql.append(JdbcConstantes.FCES_TRTT_ID);ssql.append(" = trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_PRS_ID);ssql.append(" = prs.");ssql.append(JdbcConstantes.PRS_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);ssql.append(" = mcttcr.");ssql.append(JdbcConstantes.MCCR_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);ssql.append(" = cncr.");ssql.append(JdbcConstantes.CNCR_ID);
			ssql.append(" AND cncr.");ssql.append(JdbcConstantes.CNCR_CRR_ID);ssql.append(" = crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);ssql.append(" IN (");
			ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_VALUE);
			ssql.append(",");ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_VALUE);
			ssql.append(")");
			
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			ssql.append(" = ");ssql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_CARRERA_ID);
			ssql.append(" = ? ");
			ssql.append(" AND prs.");ssql.append(JdbcConstantes.PRS_IDENTIFICACION);
			ssql.append(" = ? ");
			ssql.append(" ORDER BY crr.crr_id DESC");
			
			pstmt = con.prepareStatement(ssql.toString());
			pstmt.setInt(1, crrId); //cargo el id de la carrera
			pstmt.setString(2, cedula); //cargo el id del postulante
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(resultSetAdtoEstudianteTitulacion(rs));
			}
		} catch (SQLException e) {
			throw new PersonaDtoJdbcException(e);
		} catch (Exception e) {
			throw new PersonaDtoJdbcException(e);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno==null || retorno.size()==0){
			throw new PersonaDtoJdbcException("No existen postulantes que hayan retornado de emisionTitulos");
		}
		return retorno;
	}
	
	
	/**
	 * Método que devuelve todos los datos de graduacion de un estudiante
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<EstudianteTitulacionJdbcDto> listarPersonasEditadas()
			throws PersonaDtoJdbcException {
		Connection con = null;
		List<EstudianteTitulacionJdbcDto> retorno = new ArrayList<EstudianteTitulacionJdbcDto>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			StringBuffer ssql = new StringBuffer();
			ssql.append(" SELECT ");
			ssql.append(" to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_INICIO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			ssql.append(" ,to_char(fces.");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);ssql.append(",'YYYY-MM-DD')");ssql.append(" AS ");ssql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			ssql.append(" , CASE WHEN fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE fces.");ssql.append(JdbcConstantes.FCES_LINK_TESIS); 
			ssql.append(" END AS ");ssql.append(JdbcConstantes.FCES_LINK_TESIS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_FECHA_CREACION);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);
			ssql.append(" ,fces.");ssql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_IDENTIFICACION);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			ssql.append(" , CASE WHEN prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); ssql.append(" IS NULL THEN ");ssql.append("''");
			ssql.append(" ELSE prs.");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			ssql.append(" END AS ");ssql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_NOMBRES);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_TELEFONO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_ETN_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBC_ID);
			ssql.append(" ,prs.");ssql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			ssql.append(" ,mcttcr.");ssql.append(JdbcConstantes.MCCR_MCTT_ID);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" ,trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			ssql.append(" ,crr.");ssql.append(JdbcConstantes.CRR_DETALLE);
			ssql.append(" FROM ");
			ssql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);ssql.append(" fces, ");
			ssql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);ssql.append(" trtt, ");
			ssql.append(JdbcConstantes.TABLA_PERSONA);ssql.append(" prs, ");
			ssql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);ssql.append(" mcttcr, ");
			ssql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);ssql.append(" cncr, ");
			ssql.append(JdbcConstantes.TABLA_TITULO);ssql.append(" ttl, ");
			ssql.append(JdbcConstantes.TABLA_CARRERA);ssql.append(" crr ");
			ssql.append(" WHERE fces.");ssql.append(JdbcConstantes.FCES_TRTT_ID);ssql.append(" = trtt.");ssql.append(JdbcConstantes.TRTT_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_PRS_ID);ssql.append(" = prs.");ssql.append(JdbcConstantes.PRS_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_MCCR_ID);ssql.append(" = mcttcr.");ssql.append(JdbcConstantes.MCCR_ID);
			ssql.append(" AND fces.");ssql.append(JdbcConstantes.FCES_CNCR_ID);ssql.append(" = cncr.");ssql.append(JdbcConstantes.CNCR_ID);
			ssql.append(" AND cncr.");ssql.append(JdbcConstantes.CNCR_CRR_ID);ssql.append(" = crr.");ssql.append(JdbcConstantes.CRR_ID);
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);ssql.append(" IN (");
			ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITADO_VALUE);
			ssql.append(",");ssql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_VALUE);
			ssql.append(")");
			ssql.append(" AND trtt.");ssql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			ssql.append(" = ");ssql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			ssql.append(" ORDER BY crr.crr_id DESC");
			
			pstmt = con.prepareStatement(ssql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(resultSetAdtoEstudianteTitulacion(rs));
			}
		} catch (SQLException e) {
			throw new PersonaDtoJdbcException(e);
		} catch (Exception e) {
			throw new PersonaDtoJdbcException(e);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno==null || retorno.size()<=0){
			throw new PersonaDtoJdbcException("No existen estudiantes para migrar");
		}
		return retorno;
	}
	
	/**
	 * Método que guarda loas datos editados del graduado
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	@Override
	public void guardarEdicion(EstudianteTitulacionJdbcDto graduado)
			throws PersonaDtoJdbcException {
		
			
			TramiteTitulo trttAux = em.find(TramiteTitulo.class, graduado.getTrttId());
			if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITADO_VALUE);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITADO_VALUE);
			}
			
	}
	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public void crearActualizarPersona(EstudianteTitulacionJdbcDto item){
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		Connection con = null;
		try {
			PersonaDto prsAux=null;
			try {
				prsAux=servPersonaDtoServicio.buscarXIdentificacionEmisionTitulo(item.getPrsIdentificacion());	
			} catch (Exception e) {
				e.printStackTrace();
			}
			con=dsEmision.getConnection();
			if(prsAux!=null && prsAux.getPrsId()!=0){
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" UPDATE PERSONA SET prs_nombres = ? ");
				sbSql.append(" , prs_primer_apellido = ? ");
				sbSql.append(" , prs_segundo_apellido = ? ");
				sbSql.append(" , prs_fecha_nacimiento = ? ");
				sbSql.append(" , prs_tipo_identificacion = ? ");
				sbSql.append(" , prs_tipo_identificacion_sniese = ? ");
				sbSql.append(" , prs_mail_personal = ? ");
				sbSql.append(" , prs_mail_institucional = ? ");
				sbSql.append(" , prs_sexo = ? ");
				sbSql.append(" , prs_sexo_sniese = ? ");
				sbSql.append(" , prs_telefono = ? ");
				sbSql.append(" , prs_ubicacion_foto = ? ");
				sbSql.append(" WHERE prs_identificacion = ? ");
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, item.getPrsNombres());
				pstmt.setString(2, item.getPrsPrimerApellido());
				pstmt.setString(3, item.getPrsSegundoApellido());
				pstmt.setDate(4, java.sql.Date.valueOf(item.getPrsFechaNacimiento().toString()));
				pstmt.setInt(5, Integer.valueOf(item.getPrsTipoIdentificacion().toString()));
				pstmt.setInt(6, Integer.valueOf(item.getPrsTipoIdentificacionSniese().toString()));
				pstmt.setString(7, item.getPrsMailPersonal());
				pstmt.setString(8, item.getPrsMailInstitucional());
				pstmt.setInt(9, Integer.valueOf(item.getPrsSexo().toString()));
				pstmt.setInt(10, Integer.valueOf(item.getPrsSexoSniese().toString()));
				pstmt.setString(11, item.getPrsTelefono());
				pstmt.setString(12, item.getPrsUbicacionFoto().substring(item.getPrsUbicacionFoto().lastIndexOf("/") + 1,item.getPrsUbicacionFoto().length()));
				pstmt.setString(13, item.getPrsIdentificacion());
				pstmt.executeUpdate();
			}else{
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" INSERT INTO PERSONA VALUES ((select max(prs_id)+1 from persona)");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? ");
				sbSql.append(" ,  ? , ? , ?)");
				pstmt1 = con.prepareStatement(sbSql.toString());
				pstmt1.setInt(1, Integer.valueOf(item.getPrsTipoIdentificacion()));
				pstmt1.setInt(2, Integer.valueOf(item.getPrsTipoIdentificacion()+1));
				pstmt1.setString(3, item.getPrsIdentificacion());
				pstmt1.setString(4, item.getPrsPrimerApellido());
				pstmt1.setString(5, item.getPrsSegundoApellido());
				pstmt1.setString(6, item.getPrsNombres());
				pstmt1.setInt(7, Integer.valueOf(item.getPrsSexo()));
				pstmt1.setInt(8, Integer.valueOf(item.getPrsSexo()+1));
				pstmt1.setString(9, item.getPrsMailPersonal());
				pstmt1.setString(10, item.getPrsMailInstitucional());
				pstmt1.setString(11, item.getPrsTelefono());
				pstmt1.setDate(12, java.sql.Date.valueOf(item.getPrsFechaNacimiento().toString()));
				pstmt1.setString(13, item.getPrsUbicacionFoto().substring(item.getPrsUbicacionFoto().lastIndexOf("/") + 1,item.getPrsUbicacionFoto().length()));
				pstmt1.executeUpdate();
			}
		} catch (Exception e) {
		}finally{
			try {
				if(pstmt!=null){
					pstmt.close();
				}
				if(pstmt1!=null){
					pstmt1.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
			}
		}
	}		
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public void crearActualizarFichaEstudiante(EstudianteTitulacionJdbcDto item){
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
//			Integer fcesId = buscarFcesIdXPrsIdentificacion(item.getPrsIdentificacion() );
			con=dsEmision.getConnection();
//			if(fcesId==0){
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" INSERT INTO FICHA_ESTUDIANTE (fces_id,fces_fecha_inicio,fces_fecha_egresamiento,fces_fecha_acta_grado, "
						+ "fces_num_acta_grado,fces_crr_estud_previos,fces_tiempo_estud_rec,fces_tipo_durac_rec,fces_tipo_colegio,"
						+ "fces_tipo_colegio_sniese,fces_titulo_bachiller,fces_nota_prom_acumulado, fces_nota_trab_titulacion,"
						+ "fces_link_tesis,fces_rec_estud_previos,fces_rec_estud_prev_sniese, fces_fecha_creacion, ubc_pais_nacimiento,"
						+ "ubc_canton_residencia,etn_id,mctt_id,inac_id_inst_est_previos,prs_id, fces_estado_migrado,cncr_id)"
						+ " VALUES ((select max(fces_id)+1 from ficha_estudiante)");
				sbSql.append(" ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(select prs_id from persona where prs_identificacion = ?),?,?)");
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setDate(1, java.sql.Date.valueOf(item.getFcesFechaInicio().toString()));
				pstmt.setDate(2, java.sql.Date.valueOf(item.getFcesFechaEgresamiento().toString()));
				pstmt.setDate(3, java.sql.Date.valueOf(item.getFcesFechaActaGrado().toString()));
				pstmt.setString(4, item.getFcesNumActaGrado());
				pstmt.setString(5, item.getFcesCrrEstudPrevios());
				pstmt.setInt(6, Integer.valueOf(item.getFcesTiempoEstudRec()));
				pstmt.setInt(7, Integer.valueOf(item.getFcesTipoDuracionRec()));
				pstmt.setInt(8, Integer.valueOf(item.getFcesTipoColegio()));
				pstmt.setInt(9, Integer.valueOf(item.getFcesTipoColegio()+1));
				pstmt.setString(10, item.getFcesTituloBachiller());
				pstmt.setBigDecimal(11, item.getFcesNotaPromAcumulado());
				pstmt.setBigDecimal(12, item.getFcesNotaTrabTitulacion());
				if (item.getFcesLinkTesis().matches(
						"http  wwwdspaceuceeduec(.*)")) {
					StringBuilder sb1 = new StringBuilder();
					lazo: for (int i = item.getFcesLinkTesis().length() - 1; i > 0; i--) {
						sb1.append(item.getFcesLinkTesis().charAt(i));
						if (item.getFcesLinkTesis().charAt(i) == ' ') {
							break lazo;
						}
					}
					item.setFcesLinkTesis("http://www.dspace.uce.edu.ec/handle/25000/"
							+ sb1.reverse().toString());
				} else {
					item.setFcesLinkTesis(item.getFcesLinkTesis());
				}
				pstmt.setString(13, item.getFcesLinkTesis());
				pstmt.setInt(14, Integer.valueOf(item.getFcesRecEstuPrevios()));
				try {
					pstmt.setInt(15, Integer.valueOf(item.getFcesRecEstuPreviosSniese()));	
				} catch (Exception e) {
					pstmt.setBigDecimal(15, null);
				}
				
				pstmt.setTimestamp(16,  new Timestamp(System.currentTimeMillis()));
				pstmt.setInt(17, Integer.valueOf(item.getPrsUbicacionId()));
				pstmt.setInt(18, Integer.valueOf(item.getFcesCantonResidenciaId()));
				pstmt.setInt(19, Integer.valueOf(item.getPrsEtniaId()));
				pstmt.setInt(20, Integer.valueOf(item.getFcesMecanismoTitulacionId()));
				if(Integer.valueOf(item.getFcesInacInstEstPreviosId())==0){
					pstmt.setBigDecimal(21, null);
				}else{
					pstmt.setInt(21, item.getFcesInacInstEstPreviosId());	
				}
				
				pstmt.setString(22, item.getPrsIdentificacion());
				pstmt.setInt(23, 0);
				pstmt.setInt(24, item.getFcesConfCarreraId());
				pstmt.executeUpdate();
//			}else{
//				StringBuilder sbSql = new StringBuilder();
//				sbSql.append(" UPDATE FICHA_ESTUDIANTE SET fces_fecha_inicio=?,fces_fecha_egresamiento=?,fces_fecha_acta_grado=?, "
//				+ "fces_num_acta_grado=?,fces_crr_estud_previos=?,fces_tiempo_estud_rec=?,fces_tipo_durac_rec=?,fces_tipo_colegio=?,"
//				+ "fces_tipo_colegio_sniese=?,fces_titulo_bachiller=?,fces_nota_prom_acumulado=?, fces_nota_trab_titulacion=?,"
//				+ "fces_link_tesis=?,fces_rec_estud_previos=?,fces_rec_estud_prev_sniese=?, fces_fecha_creacion=?, ubc_pais_nacimiento=?,"
//				+ "ubc_canton_residencia=?,etn_id=?,mctt_id=?,inac_id_inst_est_previos=? , fces_estado_migrado=? , cncr_id = ? where fces_id=?");
//				pstmt = con.prepareStatement(sbSql.toString());
//				pstmt.setDate(1, java.sql.Date.valueOf(item.getFcesFechaInicio().toString()));
//				pstmt.setDate(2, java.sql.Date.valueOf(item.getFcesFechaEgresamiento().toString()));
//				pstmt.setDate(3, java.sql.Date.valueOf(item.getFcesFechaActaGrado().toString()));
//				pstmt.setString(4, item.getFcesNumActaGrado());
//				pstmt.setString(5, item.getFcesCrrEstudPrevios());
//				pstmt.setInt(6, Integer.valueOf(item.getFcesTiempoEstudRec()));
//				pstmt.setInt(7, Integer.valueOf(item.getFcesTipoDuracionRec()));
//				pstmt.setInt(8, Integer.valueOf(item.getFcesTipoColegio()));
//				pstmt.setInt(9, Integer.valueOf(Integer.valueOf(item.getFcesTipoColegio()+1)));
//				
//				pstmt.setString(10, item.getFcesTituloBachiller());
//				pstmt.setBigDecimal(11, item.getFcesNotaPromAcumulado());
//				pstmt.setBigDecimal(12, item.getFcesNotaTrabTitulacion());
//				if (item.getFcesLinkTesis().matches(
//						"http  wwwdspaceuceeduec(.*)")) {
//					StringBuilder sb1 = new StringBuilder();
//					lazo: for (int i = item.getFcesLinkTesis().length() - 1; i > 0; i--) {
//						sb1.append(item.getFcesLinkTesis().charAt(i));
//						if (item.getFcesLinkTesis().charAt(i) == ' ') {
//							break lazo;
//						}
//					}
//					item.setFcesLinkTesis("http://www.dspace.uce.edu.ec/handle/25000/"
//							+ sb1.reverse().toString());
//				} else {
//					item.setFcesLinkTesis(item.getFcesLinkTesis());
//				}
//				pstmt.setString(13, item.getFcesLinkTesis());
//				pstmt.setInt(14, Integer.valueOf(item.getFcesRecEstuPrevios()));
//				try {
//					pstmt.setInt(15, Integer.valueOf(item.getFcesRecEstuPreviosSniese()));	
//				} catch (Exception e) {
//					pstmt.setBigDecimal(15, null);
//				}
//				pstmt.setTimestamp(16, new Timestamp(System.currentTimeMillis()));
//				pstmt.setInt(17, Integer.valueOf(item.getPrsUbicacionId()));
//				pstmt.setInt(18, Integer.valueOf(item.getFcesCantonResidenciaId()));
//				pstmt.setInt(19, Integer.valueOf(item.getPrsEtniaId()));
//				pstmt.setInt(20, Integer.valueOf(item.getFcesMecanismoTitulacionId()));
//				if(Integer.valueOf(item.getFcesInacInstEstPreviosId())==0){
//					pstmt.setBigDecimal(21, null);
//				}else{
//					pstmt.setInt(21, item.getFcesInacInstEstPreviosId());	
//				}
//				pstmt.setInt(22, 0);
//				pstmt.setInt(23, item.getFcesConfCarreraId());
//				pstmt.setInt(24, Integer.valueOf(fcesId));
//				pstmt.executeUpdate();
//			}
		} catch (Exception e) {
		}finally{
			try {
				if(pstmt!=null)
					pstmt.close();
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
			}
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	@Override	
	public Integer buscarFcesIdXPrsIdentificacion(String identificacion) throws EstudianteActaGradoException{
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
						
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ?");
			
			con = dsEmision.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); //cargo el número de identificacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt(JdbcConstantes.FCES_ID);
			}else{
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new EstudianteActaGradoException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EstudianteActaGradoException();
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		
	}
	
}
