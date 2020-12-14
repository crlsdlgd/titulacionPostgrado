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
   
 ARCHIVO:     RolFlujoCarreraServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla RolFlujoCarrera. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;

/**
 * Clase (Bean)RolFlujoCarreraServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class RolFlujoCarreraServicioImpl implements RolFlujoCarreraServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad RolFlujoCarrera por su id
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	@Override
	public RolFlujoCarrera buscarPorId(Integer id) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException {
		RolFlujoCarrera retorno = null;
		if (id != null) {
			try {
				retorno = em.find(RolFlujoCarrera.class, id);
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades RolFlujoCarrera existentes en la BD
	 * @return lista de todas las entidades RolFlujoCarrera existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoCarrera> listarTodos() throws RolFlujoCarreraNoEncontradoException{
		List<RolFlujoCarrera> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	

	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	@Override
	public RolFlujoCarrera buscarPorCarrera(Integer carrera, Integer usuarioId, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		RolFlujoCarrera retorno = null;
		if(carrera != null){
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrCarrera.crrId = :carrera");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId ");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", carrera );
				q.setParameter("rolId", rolId );
				q.setParameter("usuarioId", usuarioId );
				retorno = (RolFlujoCarrera)q.getSingleResult();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.no.result.exception",carrera)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.non.unique.result.exception",carrera)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.exception")));
			}
		}
		return retorno;
	}
	
	
	

/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	@Override
	public RolFlujoCarrera buscarPorCarreraXUsuarioDGA(Integer carrera) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		RolFlujoCarrera retorno = null;
		if(carrera != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrCarrera.crrId = :carrera");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId ");
//				sbsql.append(" and roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", carrera );
				q.setParameter("rolId", RolConstantes.ROL_BD_EDITOR_DGIP_VALUE );
//				q.setParameter("usuarioId", usuarioId );
				retorno = (RolFlujoCarrera)q.getSingleResult();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.no.result.exception",carrera)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.non.unique.result.exception",carrera)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public RolFlujoCarrera buscarPorCarreraXUsuarioSecretaria(Integer carrera) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		RolFlujoCarrera retorno = null;
		if(carrera != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrCarrera.crrId = :carrera");
				sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId ");
				sbsql.append(" and roflcr.roflcrEstado = ");sbsql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", carrera );
				q.setParameter("rolId", RolConstantes.ROL_BD_VALIDADOR_VALUE );
//				q.setParameter("usuarioId", usuarioId );
				retorno = (RolFlujoCarrera)q.getSingleResult();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.Carrera.no.result.exception",carrera)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.non.unique.result.exception",carrera)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id de usuario
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoCarrera> buscarPorIdUsuario(Usuario usuario) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException{
		List<RolFlujoCarrera> retorno = null;
		if(usuario != null){
			try{
				retorno=new ArrayList<RolFlujoCarrera>();
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
				sbsql.append(" where roflcr.roflcrUsuarioRol.usroUsuario.usrId = :usuarioId");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("usuarioId", usuario.getUsrId() );
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.IdUsuario.no.result.exception",usuario.getUsrId())));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.IdUsuario.non.unique.result.exception",usuario.getUsrId())));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.IdUsuario.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Desactiva el rol_flujo_carrera de acuerdo al roflcr_id
	 * @param roflcrId - id de rol_flujo_carrera
	 * @return boolean 
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	public boolean desactivarRolFlujoCarreraXUsuarioRol(Integer roflcrId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException{
		boolean retorno = false;
		if(roflcrId != 0){
			try{
				RolFlujoCarrera roflcrAux = em.find(RolFlujoCarrera.class, roflcrId);
				roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_INACTIVO_VALUE);
//				StringBuffer sbsql = new StringBuffer();
//				sbsql.append(" UPDATE RolFlujoCarrera AS roflcr SET roflcr.roflcrEstado = ");
//				sbsql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_INACTIVO_VALUE);
//				sbsql.append(" where roflcr.roflcrId = :roflcrId ");
//				Query q = em.createQuery(sbsql.toString());
//				q.setParameter("roflcrId", roflcrId );
//				q.executeUpdate();
				retorno=true;
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.no.result.exception",roflcrId)));
			} catch (Exception e) {
				throw new RolFlujoCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Activa el rol_flujo_carrera de acuerdo al roflcr_id
	 * @param roflcrId - id de rol_flujo_carrera
	 * @return boolean 
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	public boolean activarRolFlujoCarreraXUsuarioRol(Integer roflcrId, Integer rolId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException,
		RolFlujoCarreraValidacionException{
		boolean retorno = false;
		if(roflcrId != 0){
//			System.out.println(roflcrId);
			RolFlujoCarrera roflcrActivo = null;
			try{
				RolFlujoCarrera roflcrAux = em.find(RolFlujoCarrera.class, roflcrId);
				
				try {
//					System.out.println(roflcrAux.getRoflcrCarrera().getCrrId());
//					System.out.println(rolId);
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
					sbsql.append(" where roflcr.roflcrCarrera.crrId = :crrId");
					sbsql.append(" and roflcr.roflcrEstado = ");sbsql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
					sbsql.append(" and roflcr.roflcrUsuarioRol.usroRol.rolId = :rolId");
					
					Query q = em.createQuery(sbsql.toString());
					q.setParameter("crrId", roflcrAux.getRoflcrCarrera().getCrrId() );
					q.setParameter("rolId", rolId );
//					System.out.println(roflcrAux.getRoflcrCarrera().getCrrId());
//					System.out.println(rolId);
					roflcrActivo = (RolFlujoCarrera) q.getSingleResult();
//					System.out.println(roflcrActivo.getRoflcrUsuarioRol().getUsroUsuario().getUsrNick());
					if(roflcrActivo.getRoflcrUsuarioRol().getUsroUsuario().getUsrNick()!=null){
						
						throw new RolFlujoCarreraValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.usuario.activo.por.carrera",roflcrActivo.getRoflcrUsuarioRol().getUsroUsuario().getUsrNick())));
					}
				} catch (NoResultException e) {
					//En caso de no existir usuario en la carrera seleccionada con el mismo rol
					roflcrAux.setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
				}
				retorno=true;
			} catch (NoResultException e) {
				throw new RolFlujoCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.por.id.no.result.exception",roflcrId)));
			} catch (Exception e) {
				throw new RolFlujoCarreraValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoCarrera.buscar.usuario.activo.por.carrera",roflcrActivo.getRoflcrUsuarioRol().getUsroUsuario().getUsrNick())));
			}
		}
		return retorno;
	}

	@Override
	public boolean desactivarRolFlujoCarrerasXListaUsuarioRol(Integer usroId)
			throws RolFlujoCarreraException,
			RolFlujoCarreraNoEncontradoException {
		boolean retorno = false;
		if(usroId != 0){
				try {
					StringBuffer sbsql = new StringBuffer();
					sbsql.append(" Select roflcr from RolFlujoCarrera roflcr ");
					sbsql.append(" where roflcr.roflcrUsuarioRol.usroId = :usroId");
					sbsql.append(" and roflcr.roflcrEstado = ");sbsql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
					Query q = em.createQuery(sbsql.toString());
					q.setParameter("usroId", usroId );
					@SuppressWarnings("unchecked")
					List<RolFlujoCarrera> listaRolFlujoCarrera = q.getResultList();
					if(listaRolFlujoCarrera==null){
					}else{
						for (RolFlujoCarrera item : listaRolFlujoCarrera) {
							RolFlujoCarrera aux = em.find(RolFlujoCarrera.class, item.getRoflcrId());
							aux.setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_INACTIVO_VALUE);
						}
					}
				} catch (NoResultException e) {
				}
				retorno=true;
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoCarrera> listarCarrerasXUsroId(int idUsuarioRol) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException{
		List<RolFlujoCarrera> retorno = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" Select roflcr from RolFlujoCarrera roflcr ");
			sb.append(" where ");
			sb.append(" roflcr.roflcrUsuarioRol.usroId = :idUsuarioRol ");
			sb.append(" and ");sb.append(" roflcr.roflcrEstado = ");sb.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sb.toString());
			q.setParameter("idUsuarioRol", idUsuarioRol);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new RolFlujoCarreraNoEncontradoException("RolFlujoCarrera.buscar.por.usrRol.por.carrera.no.encontrado.exception");
		}catch (NonUniqueResultException e) {
			throw new RolFlujoCarreraException("RolFlujoCarrera.buscar.por.usrRol.por.carrera.no.resultado.unico.exception");
		} catch (Exception e) {
			throw new RolFlujoCarreraException("RolFlujoCarrera.buscar.por.usrRol.por.carrera.general.exception");
		}
		return retorno;
	}
	
}
