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
   
 ARCHIVO:     UsuarioServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Usuario. 
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

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;

/**
 * Clase (Bean)UsuarioServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class UsuarioServicioImpl implements UsuarioServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Usuario por su id
	 * @param id - del Usuario a buscar
	 * @return Usuario con el id solicitado
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada cuando no se encuentra un Usuario con el id solicitado
	 * @throws UsuarioException - Excepcion general
	 */
	@Override
	public Usuario buscarPorId(Integer id) throws UsuarioNoEncontradoException, UsuarioException {
		Usuario retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Usuario.class, id);
			} catch (NoResultException e) {
				throw new UsuarioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Usuario existentes en la BD
	 * @return lista de todas las entidades Usuario existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> listarTodos() {
		List<Usuario> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select usu from Usuario usu ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;
	}
	
	/**
	 * Busca a un usuario por su correo
	 * @param correo - correo con el que se va a buscar
	 * @return usuario con el correo solicitado
	 * @throws UsuarioNoEncontradoException 
	 * @throws UsuarioException 
	 */
	@Override
	public Usuario buscarPorCorreo(String correo) throws UsuarioNoEncontradoException, UsuarioException	{
		Usuario retorno = null;
		try{
			if(correo != null){
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" Select u from Usuario u where ");
				sbSql.append(" upper(u.usrPersona.prsMailPersonal)= :correo ");
				Query q = em.createQuery(sbSql.toString());
				q.setParameter("correo",correo.toUpperCase());
				retorno = (Usuario)q.getSingleResult();
			}
		}catch(NoResultException nre){
			throw new UsuarioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.correo.no.result.exception", correo)));
		}catch(NonUniqueResultException nre){
			throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.correo.non.unique.result.exception",correo)));
		}catch (Exception nre) {
			throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.correo.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Busca a un usuario por su nick
	 * @param nickName - nick con el que se va a buscar
	 * @return usuario con el nick solicitado
	 * @throws UsuarioNoEncontradoException, UsuarioException 
	 */
	@Override
	public Usuario buscarPorNick(String nickName) throws UsuarioNoEncontradoException, UsuarioException	{
		Usuario retorno = null;
		try{
			if(nickName != null){
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" Select u from Usuario u where ");
				sbSql.append(" upper(u.usrNick)=upper(:nickName) ");

				Query q = em.createQuery(sbSql.toString());
				q.setParameter("nickName",nickName);
				retorno = (Usuario)q.getSingleResult();
			}
		}catch(NoResultException nre){
			throw new UsuarioNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.nickName.no.result.exception", nickName)));
		}catch(NonUniqueResultException nre){
			throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.nickName.non.unique.result.exception",nickName)));
		}catch (Exception nre) {
			throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.nickName.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca a un usuario por su Identificación
	 * @param identificacion - identificacion con la que se va a buscar
	 * @return usuario con la identificacion solicitado 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscarPorIndentificacion(String identificacion)	{
		List<Usuario> retorno = null;
		if(identificacion != null){
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usr from Usuario usr where ");
			sbSql.append("usr.usrIdentificacion like  :identificacion");
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion","%"+identificacion+"%");
			retorno = q.getResultList();
		}
	return retorno;
	}

	/**
	 * Busca a un usuario por su Identificación
	 * @param identificacion - identificacion con la que se va a buscar
	 * @return usuario con la identificacion solicitado 
	 */
	@Override
	public Usuario buscarIndentificacion(String identificacion)	{
		Usuario retorno = null;
		if(identificacion != null){
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usr from Usuario usr where ");
			sbSql.append("usr.usrIdentificacion like  :identificacion");
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion","%"+identificacion+"%");
			retorno = (Usuario) q.getSingleResult();
		}
	return retorno;
	}

	
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	private boolean verificarConstraintMailPersona(Persona entidad, int tipo, String mailPersonal){
		boolean retorno = false;
		Persona perAux = null;
		
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsMailPersonal)= :mailPersonal ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND p.prsId != :personaId ");
			}
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("mailPersonal",mailPersonal);
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("personaId",entidad.getPrsId());
			}
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
		retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Reliza verificaciones para determinar si existe la entidad la busca por el nombre
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @param tipo - tipo de verificacion que necesita 0 nuevo 1 editar
	 * @throws UsuarioValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 */
	private void verificar(Usuario entidad, int tipo) throws UsuarioValidacionException {
		//verifico si la entidad es null
		if(entidad==null){
			throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.entidad.null")));
		}
		
		//validacion de constraint de identificacion
		if(!verificarConstraintIdenficador(entidad, tipo)){
			throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.identificador")));
		}
		
		//validacion de constraint de nick
		if(!verificarConstraintNick(entidad, tipo)){
			throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.validar.constraint.nick")));
		}
		
	}
	
	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	@Override
	public boolean verificarConstraintIdenficador(Usuario entidad, int tipo){
		boolean retorno = false;
		Usuario usuAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select u from Usuario u where ");
			sbSql.append(" upper(u.usrIdentificacion)= :identificacion ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND u.usrId != :usuarioId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion",entidad.getUsrIdentificacion().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("usuarioId",entidad.getUsrId());
			}
			usuAux = (Usuario)q.getSingleResult();
		}catch(NoResultException nre){
			usuAux = null;
		}catch(NonUniqueResultException nure){
			usuAux = new Usuario();
		}
		
		if(usuAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que verifica la existencia de un nick en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintNick(Usuario entidad, int tipo){
		boolean retorno = false;
		Usuario usuAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select u from Usuario u where ");
			sbSql.append(" upper(u.usrNick)= :nickName ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND u.usrId != :usuarioId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("nickName",entidad.getUsrNick().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("usuarioId",entidad.getUsrId());
			}
			usuAux = (Usuario)q.getSingleResult();
		}catch(NoResultException nre){
			usuAux = null;
		}catch(NonUniqueResultException nure){
			usuAux = new Usuario(-99);
		}
		
		if(usuAux==null){
			retorno = true;
		}
		return retorno;
	}
	
	/**
	 * Actualizar clave de usuario y correo de persona del usuario
	 * @param idUsuario
	 * @param clave
	 * @param idPersona
	 * @param mailPersonal
	 * @throws UsuarioValidacionException
	 */
	@Override
	public void actualizarClaveUsuarioMailPersona(int idUsuario, String clave, int idPersona, String mailPersonal) throws UsuarioValidacionException, UsuarioException{
		Usuario usuAux = em.find(Usuario.class, idUsuario);
		if(usuAux==null){
			throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.actualizar.clave.exception")));
		}else{
			Persona persAux = em.find(Persona.class, idPersona);
			if(persAux==null){
				throw new UsuarioException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.actualizar.clave.exception")));
			}else{
				if(!verificarConstraintMailPersona(persAux, GeneralesConstantes.APP_EDITAR,mailPersonal)){
					throw new UsuarioValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.actualizar.clave.validacion.exception")));
				}else{
					usuAux.setUsrPassword(clave);
					persAux.setPrsMailPersonal(mailPersonal);
				}
			}
		}
	}
	
	/**
	 * Actualiza el password de la entidad
	 * @param entidad - entidad a actualizar
	 * @return la entidad editada
	 * @throws UsuarioValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada si no se encontro la entidad a actualizar
	 * @throws UsuarioException - Excepcion general
	 */
	public Usuario actualizaPassword(Usuario entidad) throws UsuarioValidacionException, UsuarioNoEncontradoException, UsuarioException {
		Usuario retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getUsrId());
				if (retorno != null) {
					verificar(entidad, GeneralesConstantes.APP_EDITAR);
					retorno.setUsrPassword(entidad.getUsrPassword());
				}
			}
		} catch (UsuarioValidacionException e) {
			throw e;
		} catch (UsuarioNoEncontradoException e) {
			throw e;
		} catch (UsuarioException e) {
			throw e;
		}
		return retorno;
	}

	 /**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws UsuarioValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws UsuarioException - Excepcion general
	 */
	@Override
	public Usuario editar(Usuario entidad) throws UsuarioValidacionException, UsuarioNoEncontradoException, UsuarioException {
		Usuario retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getUsrId());
					if (retorno != null) {
						verificar(entidad, GeneralesConstantes.APP_EDITAR);
						retorno.setUsrIdentificacion(entidad.getUsrIdentificacion());
						retorno.setUsrNick(entidad.getUsrNick());
						retorno.setUsrPassword(entidad.getUsrPassword());
						retorno.setUsrFechaCreacion(entidad.getUsrFechaCreacion());
						retorno.setUsrFechaCaducidad(entidad.getUsrFechaCaducidad());
						retorno.setUsrFechaCadPass(entidad.getUsrFechaCadPass());
						retorno.setUsrEstado(entidad.getUsrEstado());
						retorno.setUsrEstSesion(entidad.getUsrEstSesion());
				}
			}
		} catch (UsuarioValidacionException e) {
		throw e;
		} catch (UsuarioNoEncontradoException e) {
		throw e;
		} catch (UsuarioException e) {
		throw e;
		}
		return retorno;
	}

}
