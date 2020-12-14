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
   
 ARCHIVO:     ConfiguracionCarreraDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla ConfiguracionCarrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-NOVIEMBRE-2016		Gabriel Mafla 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.ConfiguracionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ConfiguracionCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.DuracionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB TituloDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla ConfiguracionCarrera.
 * @author gmafla
 * @version 1.0
 */
@Stateless
public class ConfiguracionCarreraDtoServicioJdbcImpl implements ConfiguracionCarreraDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	
	/**
	 * Realiza la busqueda de todas las configuraciones carrea con títulos por carrera por sexo en la aplicacion
	 * @return Lista todos las configuraciones carrea con títulos por carrera por sexo de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ConfiguracionCarreraDto> listarTitulosXCarreraXSexo(Integer carreraId, Integer sexoId) throws ConfiguracionCarreraDtoException, ConfiguracionCarreraDtoNoEncontradoException{
		List<ConfiguracionCarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(", rgac.");sbSql.append(JdbcConstantes.RGAC_ID);
			sbSql.append(", rgac.");sbSql.append(JdbcConstantes.RGAC_DESCRIPCION);
			sbSql.append(", nvfr.");sbSql.append(JdbcConstantes.NVFR_ID);
			sbSql.append(", nvfr.");sbSql.append(JdbcConstantes.NVFR_DESCRIPCION);
			sbSql.append(", tifr.");sbSql.append(JdbcConstantes.TIFR_ID);
			sbSql.append(", tifr.");sbSql.append(JdbcConstantes.TIFR_DESCRIPCION);
			sbSql.append(", vgn.");sbSql.append(JdbcConstantes.VGN_ID);
			sbSql.append(", vgn.");sbSql.append(JdbcConstantes.VGN_DESCRIPCION);
			sbSql.append(", mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			sbSql.append(", mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(", tise.");sbSql.append(JdbcConstantes.TISE_ID);
			sbSql.append(", tise.");sbSql.append(JdbcConstantes.TISE_DESCRIPCION);
			sbSql.append(", ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(", ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(", ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(", ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(", ttl.");sbSql.append(JdbcConstantes.TTL_ESTADO);
			sbSql.append(", ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);
			
			sbSql.append(", crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(", crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(", drc.");sbSql.append(JdbcConstantes.DRC_ID);
			
			sbSql.append(", drc.");sbSql.append(JdbcConstantes.DRC_TIPO);
			sbSql.append(", drc.");sbSql.append(JdbcConstantes.DRC_TIEMPO);
			
			sbSql.append(", fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr , ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
			sbSql.append(JdbcConstantes.TABLA_VIGENCIA);sbSql.append(" vgn , ");
			sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl , ");
			sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl , ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc , ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_SEDE);sbSql.append(" tise , ");
			sbSql.append(JdbcConstantes.TABLA_TIPO_FORMACION);sbSql.append(" tifr ,");
			sbSql.append(JdbcConstantes.TABLA_DURACION);sbSql.append(" drc , ");
			sbSql.append(JdbcConstantes.TABLA_NIVEL_FORMACION);sbSql.append(" nvfr , ");
			sbSql.append(JdbcConstantes.TABLA_REGIMEN_ACADEMICO);sbSql.append(" rgac ,");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl  ");
			sbSql.append(" WHERE ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_VGN_ID);sbSql.append(" = ");
			sbSql.append(" vgn.");sbSql.append(JdbcConstantes.VGN_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);sbSql.append(" = ");
			sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID);sbSql.append(" = ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_UBC_ID);sbSql.append(" = ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TISE_ID);sbSql.append(" = ");
			sbSql.append(" tise.");sbSql.append(JdbcConstantes.TISE_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TIFR_ID);sbSql.append(" = ");
			sbSql.append(" tifr.");sbSql.append(JdbcConstantes.TIFR_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_DRC_ID);sbSql.append(" = ");
			sbSql.append(" drc.");sbSql.append(JdbcConstantes.DRC_ID);
			sbSql.append(" AND tifr.");sbSql.append(JdbcConstantes.TIFR_NVFR_ID);sbSql.append(" = ");
			sbSql.append(" nvfr.");sbSql.append(JdbcConstantes.NVFR_ID);
			sbSql.append(" AND nvfr.");sbSql.append(JdbcConstantes.NVFR_RGAC_ID);sbSql.append(" = ");
			sbSql.append(" rgac.");sbSql.append(JdbcConstantes.RGAC_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND ttl.");sbSql.append(JdbcConstantes.TTL_TIPO);sbSql.append(" = ");
			sbSql.append(TituloConstantes.TIPO_TITULO_CUARTO_NIVEL_VALUE); 
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			sbSql.append(" AND ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);sbSql.append(" IN ");
			sbSql.append(" (?,2) "); //cambair a contantes
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo la carrera
			pstmt.setInt(2, sexoId); //cargo la carrera
//			System.out.println(sbSql);
//			System.out.println(sexoId);
//			System.out.println(carreraId);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ConfiguracionCarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new ConfiguracionCarreraDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConfiguracionCarreraDtoServicioImpl.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new ConfiguracionCarreraDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConfiguracionCarreraDtoServicioImpl.listar.titulos.carrera.sexo.exception")));
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
			throw new ConfiguracionCarreraDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConfiguracionCarreraDtoServicioImpl.listar.titulos.carrera.sexo.no.result.exception")));
			//TODO HAcer el mensaje
		}	
		return retorno;
	}
	
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private ConfiguracionCarreraDto transformarResultSetADto(ResultSet rs) throws SQLException{
		ConfiguracionCarreraDto retorno = new ConfiguracionCarreraDto();
		
		
		retorno.setCncrId(rs.getInt(JdbcConstantes.CNCR_ID));
		retorno.setRgacId(rs.getInt(JdbcConstantes.RGAC_ID));
		retorno.setRgacDescripcion(rs.getString(JdbcConstantes.RGAC_DESCRIPCION));
		retorno.setNvfrId(rs.getInt(JdbcConstantes.NVFR_ID));
		retorno.setNvfrDescripcion(rs.getString(JdbcConstantes.NVFR_DESCRIPCION));
		
		retorno.setTifrId(rs.getInt(JdbcConstantes.TIFR_ID));
		retorno.setTifrDescripcion(rs.getString(JdbcConstantes.TIFR_DESCRIPCION));

		retorno.setVgnId(rs.getInt(JdbcConstantes.VGN_ID));
		retorno.setVgnDescripcion(rs.getString(JdbcConstantes.VGN_DESCRIPCION));
		
		retorno.setMdlId(rs.getInt(JdbcConstantes.MDL_ID));
		retorno.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));

		retorno.setTiseId(rs.getInt(JdbcConstantes.TISE_ID));
		retorno.setTiseDescripcion(rs.getString(JdbcConstantes.TISE_DESCRIPCION));
		
		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		
		retorno.setTtlId(rs.getInt(JdbcConstantes.TTL_ID));
		retorno.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
		retorno.setTtlSexo((rs.getInt(JdbcConstantes.TTL_SEXO)));
		retorno.setTtlEstado((rs.getInt(JdbcConstantes.TTL_ESTADO)));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		
		retorno.setDrcId(rs.getInt(JdbcConstantes.DRC_ID));
		retorno.setDrcTipo(rs.getInt(JdbcConstantes.DRC_TIPO));
		retorno.setDrcTiempo(rs.getInt(JdbcConstantes.DRC_TIEMPO));
		StringBuilder sb = new StringBuilder();
		sb.append(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
		sb.append(" - ");
		sb.append(rs.getString(JdbcConstantes.VGN_DESCRIPCION));
		sb.append(" - ");
		sb.append(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
		sb.append(" - DURACION : ");
		sb.append(rs.getString(JdbcConstantes.DRC_TIEMPO));
		sb.append(" ");
		
		if(rs.getInt(JdbcConstantes.DRC_TIPO)==DuracionConstantes.DURACION_TIPO_ANIOS_VALUE){
			sb.append(DuracionConstantes.DURACION_TIPO_ANIOS_LABEL);
		}else if(rs.getInt(JdbcConstantes.DRC_TIPO)==DuracionConstantes.DURACION_TIPO_SEMESTRES_VALUE){
			sb.append(DuracionConstantes.DURACION_TIPO_SEMESTRES_LABEL);
		}else if(rs.getInt(JdbcConstantes.DRC_TIPO)==DuracionConstantes.DURACION_TIPO_CREDITOS_VALUE){
			sb.append(DuracionConstantes.DURACION_TIPO_CREDITOS_LABEL);
		}
		switch (rs.getInt(JdbcConstantes.FCL_ID)) {
		case 18:
		case 19:
		case 20:
			sb.append(" - TIPO : ");
			sb.append(rs.getString(JdbcConstantes.TISE_DESCRIPCION));
			sb.append(" ");
			break;
		default:
			break;
		}
		sb.append(" - REGIMEN : ");
		sb.append(rs.getString(JdbcConstantes.RGAC_DESCRIPCION));
		sb.append(" ");
		
		retorno.setCncrDetalleActaGrado(sb.toString());
		return retorno;
	} 
	
}
