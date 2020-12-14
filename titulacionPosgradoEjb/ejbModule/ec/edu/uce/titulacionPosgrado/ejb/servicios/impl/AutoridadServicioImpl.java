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
   
 ARCHIVO:     AutoridadServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Autoridad. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 10-Mayo-2017            Diego García                         Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.AutoridadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AutoridadConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Autoridad;

/**
 * Clase (Bean)AutoridadServicioImpl. Bean declarado como Stateless.
 * @author dpgarciar
 * @version 1.0
 */

@Stateless
public class AutoridadServicioImpl implements AutoridadServicio {

	@PersistenceContext(unitName = GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Autoridad por su id
	 * @param id - de la Autoridad a buscar
	 * @return Autoridad con el estado activo solicitado
	 * @throws AutoridadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Autoridad con el id solicitado
	 * @throws AutoridadException - Excepcion general
	 */
	@Override
	public Autoridad buscarPorId(Integer id)
			throws AutoridadNoEncontradoException, AutoridadException {
		Autoridad retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Autoridad.class, id);
			} catch (NoResultException e) {
				throw new AutoridadNoEncontradoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"Autordad.buscar.por.id.no.result.exception",
										id)));
			} catch (NonUniqueResultException e) {
				throw new AutoridadException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"Autoridad.buscar.por.id.non.unique.result.exception",
										id)));
			} catch (Exception e) {
				throw new AutoridadException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"Autoridad.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Busca la Autoridad por Identificacion
	 * @param indentificacion - identificacion de la autordida que se va a buscar
	 * @return entidad persona
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	@Override
	public Autoridad buscarPorIdentificacion(String indentificacion)
			throws AutoridadNoEncontradoException, AutoridadException {

		Autoridad retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select a from Autoridad atr where ");
		sbsql.append(" art.prsIdentificacion =:indentificacion ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("indentificacion", indentificacion);
		retorno = (Autoridad) q.getSingleResult();

		return retorno;

	}

	/**
	 * Lista todas las entidades Autoridades existentes en la BD
	 * @return lista de todas las entidades Autoridades existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Autoridad> listarTodos() throws AutoridadNoEncontradoException {
		List<Autoridad> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select atr from Autoridad atr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();

		if (retorno.size() <= 0) {
			throw new AutoridadNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"Autoridad.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	/**
	 * Busca una entidad Autoridad por su id
	 * @param id - de la Autoridad a buscar
	 * @throws AutoridadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Autoridad con el id solicitado
	 * @throws AutoridadException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Autoridad> buscarXId(Integer idAutoridad)
			throws AutoridadNoEncontradoException, AutoridadException {
		List<Autoridad> retorno = null;
		try {
			StringBuilder ssql = new StringBuilder();
			ssql.append(" Select atr from Autoridad atr where ");

			if (idAutoridad.intValue() == GeneralesConstantes.APP_ID_BASE) {
				ssql.append(" atr.atrId > :idAutoridad ");
			} else {
				ssql.append(" atr.atrId = :idAutoridad ");
			}
			ssql.append(" order by atr.atrEstado asc");
			Query q = em.createQuery(ssql.toString());
			q.setParameter("idAutoridad", idAutoridad.intValue());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new AutoridadNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"Autoridad.buscar.x.id.no.result.exception")));
		} catch (Exception e) {
			throw new AutoridadException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"Autoridad.buscar.x.id.exception")));
		}

		return retorno;
	}

	/**
	 * Busca una entidad Autoridad por su Tipo y Estado
	 * @param tipo - de la Autoridad a buscar
	 * @param estado - de la Autoridad a buscar
	 * @param descripcion - de la facultad de la Autoridad a buscar
	 * @return Autoridad con el estado y tipo solicitado
	 * @throws AutoridadNoEncontradoException- Excepcion lanzada cuando no se encuentra una Autoridad con el id solicitado
	 * @throws AutoridadException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	public List<Autoridad> buscarXTipoXEstado(int tipo, int estado,int descripcion) 
			throws AutoridadNoEncontradoException, AutoridadException {
		List<Autoridad> retorno = null;
		try {
			StringBuilder sbsql = new StringBuilder();
			sbsql.append("Select atr from Autoridad atr");

			if (descripcion != GeneralesConstantes.APP_ID_BASE
					|| tipo != GeneralesConstantes.APP_ID_BASE
					|| estado != GeneralesConstantes.APP_ID_BASE) {
				sbsql.append(" Where");
			}
			if (descripcion != GeneralesConstantes.APP_ID_BASE) {
				sbsql.append(" atr.atrFacultad.fclId = :descripcion ");
			}
			if (tipo != GeneralesConstantes.APP_ID_BASE) {
				if (descripcion != GeneralesConstantes.APP_ID_BASE) {
					sbsql.append(" And");
				}
				sbsql.append(" atr.atrTipo =:tipo ");
			}
			if (estado != GeneralesConstantes.APP_ID_BASE) {
				if (descripcion != GeneralesConstantes.APP_ID_BASE
						|| tipo != GeneralesConstantes.APP_ID_BASE) {
					sbsql.append(" And");
				}
				sbsql.append(" atr.atrEstado = :estado ");
			}
			sbsql.append(" order by atr.atrTipo ");
			Query q = em.createQuery(sbsql.toString());
			if (tipo != GeneralesConstantes.APP_ID_BASE) {
				q.setParameter("tipo", tipo);
			}
			if (estado != GeneralesConstantes.APP_ID_BASE) {
				q.setParameter("estado", estado);
			}
			if (descripcion != GeneralesConstantes.APP_ID_BASE) {
				q.setParameter("descripcion", descripcion);
			}

			retorno = q.getResultList();

			if (retorno.size() == 0) {
				throw new NoResultException("No se encotro resultados");
			}
		} catch (NoResultException e) {
			throw new AutoridadNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"Autordad.buscar.por.id.no.result.exception")));
		} catch (NonUniqueResultException e) {
			throw new AutoridadException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"Autoridad.buscar.por.id.non.unique.result.exception")));
		} catch (Exception e) {
			throw new AutoridadException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"Autoridad.buscar.por.id.exception")));
		}

		return retorno;
	}

	/**
	 * Crea una nueva Autoridad, pone en inactivo a la otra autoridad si la hay
	 * @param Autoridad - entidad Autoridad de la que se extraera el nombre para la consulta
	 * @return la nueva autoridad
	 */
	@Override
	public Autoridad crearAutoridad(Autoridad autoridad)
			throws AutoridadNoEncontradoException, AutoridadException {
		Autoridad retorno = new Autoridad();
		Query query = em.createQuery("UPDATE Autoridad SET atrEstado = 1 "
				+ " WHERE atrEstado = 0 " + "AND atrTipo = :autoridadTipo "
				+ " AND atrFacultad.fclId = :autoridadFacultad");
		query.setParameter("autoridadTipo", autoridad.getAtrTipo());
		query.setParameter("autoridadFacultad", autoridad.getAtrFacultad().getFclId());
		query.executeUpdate();
		
		retorno.setAtrIdentificacion(autoridad.getAtrIdentificacion());
		retorno.setAtrPrimerApellido(autoridad.getAtrPrimerApellido());
		retorno.setAtrSegundoApellido(autoridad.getAtrSegundoApellido());
		retorno.setAtrNombres(autoridad.getAtrNombres());
		retorno.setAtrTipo(autoridad.getAtrTipo());
		retorno.setAtrEstado(autoridad.getAtrEstado());
		retorno.setAtrFacultad(autoridad.getAtrFacultad());
		retorno.setAtrSexo(autoridad.getAtrSexo());
		em.persist(retorno);
		return retorno;
	}

	/**
	 * Edita una Autoridad el tipo y el estado y si es activa descactiva a la autoridad anterior
	 * @param Autoridad - entidad Autoridad de la que se extraera el nombre para la consulta
	 * @return Autoridad editada nueva autoridad
	 */
	@Override
	public Autoridad editarAutoridad(Autoridad autoridad)
			throws AutoridadNoEncontradoException, AutoridadException {
		Autoridad retorno = null;
		if (autoridad != null) {

			retorno = buscarPorId(autoridad.getAtrId());

			if (retorno != null) {
				if(autoridad.getAtrEstado() == AutoridadConstantes.ESTADO_ACTIVO_VALUE ){
					Query query = em.createQuery("UPDATE Autoridad SET atrEstado = 1 "
							+ " WHERE atrEstado = 0 " + "AND atrTipo = :autoridadTipo "
							+ " AND atrFacultad.fclId = :autoridadFacultad");
					query.setParameter("autoridadTipo", autoridad.getAtrTipo());
					query.setParameter("autoridadFacultad", autoridad.getAtrFacultad().getFclId());
					query.executeUpdate();
				}
				retorno.setAtrEstado(autoridad.getAtrEstado());
				retorno.setAtrTipo(autoridad.getAtrTipo());
			}
		}
		return retorno;
	}

}
