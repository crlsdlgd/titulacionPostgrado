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
   
 ARCHIVO:     UsuarioRolServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla UsuarioRol. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018				Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;

/**
 * Clase (Bean)UsuarioRolServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class UsuarioRolServicioImpl implements UsuarioRolServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad UsuarioRol por su id
	 * @param id - del UsuarioRol a buscar
	 * @return UsuarioRol con el id solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	@Override
	public UsuarioRol buscarPorId(Integer id) throws UsuarioRolNoEncontradoException, UsuarioRolException {
		UsuarioRol retorno = null;
		if (id != null) {
			try {
				retorno = em.find(UsuarioRol.class, id);
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioRol> listarTodos() {
		List<UsuarioRol> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select usuRol from UsuarioRol usuRol ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;
		
	}
	
	/**
	 * Lista todos los roles de los usuarios existentes en la BD
	 * @param usrId -  usrId id del usuario a consultar
	 * @return - retorna la lista de todos los roles de los usuarios existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioRol> buscarXUsuario(int usrId){
		List<UsuarioRol> retorno = null;
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" Select ur from UsuarioRol ur where ");
		sbSql.append(" ur.usroUsuario.usrId =:usrId ");

		Query q = em.createQuery(sbSql.toString());
		q.setParameter("usrId",usrId);
		retorno = q.getResultList();
		return retorno;
	}
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion de validador
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacion(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				sbsql.append(" and (usro.usroRol.rolId = :rolValidador ");
				sbsql.append(" or usro.usroRol.rolId = :rolEditorActa )");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				q.setParameter("rolValidador", RolConstantes.ROL_BD_VALIDADOR_VALUE);
				q.setParameter("rolEditorActa", RolConstantes.ROL_BD_EDITOR_ACTA_VALUE);
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
		
		
	}
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion de editor_acta
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionEditorActa(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				sbsql.append(" and usro.usroRol.rolId = :rolEditor ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				q.setParameter("rolEditor", RolConstantes.ROL_BD_EDITOR_ACTA_VALUE);
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
		
		
	}
	
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion  de evaluador
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionEvaluador(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				sbsql.append(" and usro.usroRol.rolId = :rolEvaluador ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				q.setParameter("rolEvaluador", RolConstantes.ROL_BD_EVALUADOR_VALUE);
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion de editor_acta
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	@Override
	public UsuarioRol buscarPorIdentificacionValidador(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				sbsql.append(" and usro.usroRol.rolId = :rolValidador ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				q.setParameter("rolValidador", RolConstantes.ROL_BD_VALIDADOR_VALUE);
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
		
		
	}
	
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion  de DGA
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionDga(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				sbsql.append(" and usro.usroRol.rolId = :rolDga ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				q.setParameter("rolDga", RolConstantes.ROL_BD_EDITOR_DGIP_VALUE);
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
	}

	@Override
	public void desactivarUsuarioRolXid(Integer usroId, Integer usrId)
			throws  UsuarioRolException {
		if(usroId!=null && usroId!=0){
			try {
				UsuarioRol usuarioRolAux = em.find(UsuarioRol.class, usroId);
				usuarioRolAux.setUsroEstado(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
				Usuario usrAux = em.find(Usuario.class, usrId);
				usrAux.setUsrEstado(UsuarioConstantes.ESTADO_INACTIVO_VALUE);
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
			
		}
	}
	
	@Override
	public void activarUsuarioRolXid(Integer usroId, Integer usrId)
			throws  UsuarioRolException {
		if(usroId!=null && usroId!=0){
			try {
				UsuarioRol usuarioRolAux = em.find(UsuarioRol.class, usroId);
				usuarioRolAux.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				Usuario usrAux = em.find(Usuario.class, usrId);
				usrAux.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
			
		}
	}
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion y en estado activo
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionActivos(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
		
		
	}
	@Override
	public UsuarioRol buscarXUsuarioXrol(int idUsuario, int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException{
		UsuarioRol retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select ur from UsuarioRol ur where ");
			sbSql.append(" ur.usroEstado =:estado ");
			sbSql.append(" and ur.usroUsuario.usrId =:idUsuario ");
			sbSql.append(" and ur.usroRol.rolId =:idRol ");

			Query q = em.createQuery(sbSql.toString());
			q.setParameter("estado",UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idUsuario",idUsuario);
			q.setParameter("idRol",idRol);
			retorno = (UsuarioRol) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.non.unique.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.exception")));
		}
		return retorno;
	}
	
}
