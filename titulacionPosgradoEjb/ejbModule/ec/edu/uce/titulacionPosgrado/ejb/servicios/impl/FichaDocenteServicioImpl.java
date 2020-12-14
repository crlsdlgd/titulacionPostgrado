/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     FichaDocenteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla ficha docente. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 23-09-2016           	Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FichaDocenteDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaDcnAsgTitulacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaDocenteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDocente;

/**
 * Clase (Bean)FichaDocenteServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class FichaDocenteServicioImpl implements FichaDocenteServicio{

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@EJB FichaDcnAsgTitulacionServicio servFdsFichaDcnAsgTitulacionServicioImpl;

	/**
	 * Busca una entidad FichaDocente por su id
	 * @param id - de la FichaDocente a buscar
	 * @return FichaDocente con el id solicitado
	 * @throws FichaDocenteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaDocente con el id solicitado
	 * @throws FichaDocenteException - Excepcion general
	 */
	public FichaDocente buscarPorId(Integer id) throws FichaDocenteNoEncontradoException, FichaDocenteException{
		FichaDocente retorno = null;
		if (id != null) {
			try {
				retorno = em.find(FichaDocente.class, id);
			} catch (NoResultException e) {
				throw new FichaDocenteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	public FichaDocente buscarPorIdentificacion(String identificacion)  throws FichaDocenteNoEncontradoException, FichaDocenteException{
		FichaDocente retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fcdc from FichaDocente fcdc ");
			sbsql.append(" where fcdc.fcdcPersona.prsIdentificacion = :identificacion");
			Query q = em.createQuery(sbsql.toString()).setMaxResults(1);
			q.setParameter("identificacion", identificacion);
			retorno = (FichaDocente)q.getSingleResult();
		} catch (NoResultException e) {
			throw new FichaDocenteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.no.result.exception",identificacion)));
		} catch (NonUniqueResultException e) {
			throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.non.unique.result.exception",identificacion)));
		} catch (Exception e) {
			throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.exception")));
		}
		return retorno;
	}

	@Override
	public List<FichaDocenteDto> buscarTribunalFichasDocentesXAsignacionTitulacion(
			Integer asttId) throws FichaDcnAsgTitulacionException,
			FichaDcnAsgTitulacionNoEncontradoException {
		List<FichaDocenteDto> retorno = new ArrayList<FichaDocenteDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_DCN_ASG_TITULACION);sbSql.append(" fcdcastt, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID); sbSql.append(" =  fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_FCDC_ID); sbSql.append(" =  fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" AND fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ASTT_ID); sbSql.append(" =  ? ");
			sbSql.append(" ORDER BY fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID); 
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, asttId); //cargo la id de la tabla asignacion_titulacion
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarFichaDocente(rs));
			}
		} catch (SQLException e) {
		} catch (Exception e) {
		} finally {
			try {
				if(rs!=null){
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
		return retorno;
	}
	
	
	@Override
	public Integer ingresarFichaDocente(DocenteTutorTribunalLectorJdbcDto item){
		Integer retorno = 0;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" SELECT ");
//			sbSql.append(" max(prs_id) as contador");
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);
//			con = ds.getConnection();
//			pstmt = con.prepareStatement(sbSql.toString());
//			rs = pstmt.executeQuery();
//			while(rs.next()){
//				retorno = rs.getInt("contador");
//			}
			sbSql.append("INSERT INTO PERSONA (PRS_ID,PRS_IDENTIFICACION ,PRS_TIPO_IDENTIFICACION,"
					+ " PRS_TIPO_IDENTIFICACION_SNIESE,PRS_PRIMER_APELLIDO, PRS_SEGUNDO_APELLIDO, PRS_NOMBRES,PRS_SEXO, PRS_SEXO_SNIESE"
					+ " PRS_MAIL_INSTITUCIONAL) VALUES(DEFAULT ,");
			
			sbSql.append(" VALUES (");sbSql.append(retorno+1);
			sbSql.append(" , ");sbSql.append(item.getPrsIdentificacion());
			sbSql.append(" , ");sbSql.append(item.getPrsIdentificacion());
			
			
		} catch (Exception e) {
		} finally {
			try {
				if(rs!=null){
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
		return retorno;
	}
	
	public FichaDocenteDto transformarFichaDocente(ResultSet rs){
		FichaDocenteDto retorno = new FichaDocenteDto();
		try {
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		} catch (SQLException e) {
		}
		
		return retorno;
	}
	
	
	public FichaDocente buscarXIdentificacionXCarrera(String identificacion,Integer carrera)  throws FichaDocenteNoEncontradoException, FichaDocenteException, FichaDocenteValidacionException{
		
		FichaDocente retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fcdc from FichaDocente fcdc , Persona prs ");
			sbsql.append(" where fcdc.fcdcPersona.prsId  = prs.prsId");
			sbsql.append(" and fcdc.fcdcPersona.prsIdentificacion = :identificacion");
			sbsql.append(" and fcdc.fcdcCarrera.crrId = :carrera");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("identificacion", identificacion);
			q.setParameter("carrera", carrera);
			retorno =  (FichaDocente)q.getSingleResult();
		} catch (NoResultException e) {
			throw new FichaDocenteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Docente.buscar.por.identificacion.por.carrera.no.result.exception",identificacion)));
		} catch (NonUniqueResultException e) {
			throw new FichaDocenteValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Docente.buscar.por.identificacion.por.carrera.non.unique.result.exception",identificacion)));
		} catch (Exception e) {
			throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Docente.buscar.por.identificacion.por.carrera.exception")));
		}
		return retorno;
	}
	
	/**
     * Añade una Persona en la BD
     * @return lista de todas las entidades Persona existentes en la BD
     * @throws PersonaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
     * @throws PersonaException - Excepción general
     */
    @Override
    public boolean anadir(FichaDocente entidad) throws FichaDocenteValidacionException, FichaDocenteException {
        Boolean retorno = false;
        if (entidad != null) {
            try {
//                if (verificarDescripcion(entidad, GeneralesConstantes.APP_NUEVO)) {
                    em.persist(entidad);
                    retorno = true;
//                } else {
//                    throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.añadir.duplicado.error")));
//                }
            } catch (Exception e) {
                throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.añadir.exception")));
            }
        } else {
            throw new FichaDocenteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.añadir.null.exception")));
        }
        return retorno;
    }
	
    /**
	 * Elimina la entidad Cargo a traves de su id
	 * @param id - id de la entidad Cargo a eliminar
	 * @return true si se eliminó correctamente, false de lo contrario
	 * @throws CargoValidacionException
	 */
	@Override
	public void eliminar(FichaDocente entidad)throws FichaDocenteValidacionException , FichaDocenteNoEncontradoException , FichaDocenteException , FichaDcnAsgTitulacionNoEncontradoException , FichaDcnAsgTitulacionException{
		try {
			verificarEliminar(entidad);
			FichaDocente aux = buscarPorId(entidad.getFcdcId());
			if (aux != null) {
				em.remove(aux);
			}
		} catch (FichaDocenteValidacionException e) {
			throw e;
		} catch (FichaDocenteNoEncontradoException e) {
			e.getMessage();
		} catch (FichaDocenteException e) {
			e.getMessage();
		} catch (FichaDcnAsgTitulacionNoEncontradoException e) {
			e.getMessage();
		} catch (FichaDcnAsgTitulacionException e) {
			e.getMessage();
		}
	}
    
	/**
	 * Reliza verificaciones para determinar si la entidad esta ligada a otra tabla en la BD
	 * @param entidad - entidad Cargo a ser verificado
	 * @throws FichaDcnAsgTitulacionNoEncontradoException 
	 * @throws FichaDcnAsgTitulacionException 
	 */
	private void verificarEliminar(FichaDocente entidad) throws FichaDocenteValidacionException, FichaDcnAsgTitulacionNoEncontradoException, FichaDcnAsgTitulacionException{
			try {
				if(servFdsFichaDcnAsgTitulacionServicioImpl.buscarXFichaDocenteID(entidad.getFcdcId())){
					throw new FichaDocenteValidacionException("No se puede eliminar la Ficha docente debido a que está asignada a uno o varios mecanismos de titulación");
				}
			} catch (FichaDcnAsgTitulacionNoEncontradoException e) {
				throw e;
			} catch (FichaDcnAsgTitulacionException e) {
				throw e;
			}
	}
}
