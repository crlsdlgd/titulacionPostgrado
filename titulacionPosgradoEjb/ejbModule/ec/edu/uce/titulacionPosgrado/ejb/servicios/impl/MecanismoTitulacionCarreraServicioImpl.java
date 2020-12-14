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
   
 ARCHIVO:     MecanismoTitulacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla MecanismoTitulacion. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 24-04-2018					Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.MecanismoTitulacionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;

/**
 * Clase (Bean)MecanismoTitulacionCarreraervicioImpl. Bean declarado como
 * Stateless.
 * 
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class MecanismoTitulacionCarreraServicioImpl implements
		MecanismoTitulacionCarreraServicio {

	@PersistenceContext(unitName = GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad MecanismoTitulacionCarrera por su id
	 * @param id - de la MecanismoTitulacion a buscar
	 * @return MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionCarreraException - Excepcion general
	 */
	public MecanismoCarrera buscarPorId(Integer id) throws MecanismoTitulacionCarreraNoEncontradoException, MecanismoTitulacionCarreraException {
		MecanismoCarrera retorno = null;
		if (id != null) {
			try {
				retorno = em.find(MecanismoCarrera.class, id);
			} catch (NoResultException e) {
				throw new MecanismoTitulacionCarreraNoEncontradoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.no.result.exception",
										id)));
			} catch (NonUniqueResultException e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.non.unique.result.exception",
										id)));
			} catch (Exception e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades MecanismoTitulacion existentes en la BD
	 * @return lista de todas las entidades MecanismoTitulacion existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	public List<MecanismoCarrera> listarTodos()
			throws MecanismoTitulacionCarreraNoEncontradoException {
		List<MecanismoCarrera> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select mcttcr from MecanismoTitulacionCarrera mctt ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if (retorno.size() <= 0) {
			throw new MecanismoTitulacionCarreraNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacion.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera 
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	@SuppressWarnings("unchecked")
	public List<MecanismoCarrera> listaMccttXcarrera(Integer crrId) throws MecanismoTitulacionCarreraNoEncontradoException, MecanismoTitulacionCarreraException {
		List<MecanismoCarrera> retorno = null;
		if (crrId != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select mccr from MecanismoCarrera mccr ");
				sbsql.append(" where mccr.mccrCarrera.crrId = :carrera");
				sbsql.append(" and mccr.mccrEstado = :estado ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", crrId);
				q.setParameter("estado", MecanismoTitulacionCarreraConstantes.ACTIVO_MECANISMO_TITULACION_CARRERA_VALUE);
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new MecanismoTitulacionCarreraNoEncontradoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.todos.no.result.exception")));
			} catch (NonUniqueResultException e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Actualiza el porcentaje del examen complexivo de la entidad Mecanismo_titulacion_carrera
	 * @return
	 */
	public void actualizaPorcentaje(MecanismoTitulacionCarreraDto mcttcrDto) throws MecanismoTitulacionCarreraNoEncontradoException, MecanismoTitulacionCarreraException {
		try {
			MecanismoCarrera mcttcr = new MecanismoCarrera();
			mcttcr = buscarPorId(mcttcrDto.getMcttcrId());
			mcttcr.setMccrPorcentaje(mcttcrDto.getMcttcrPorcentajeComplexivo());
		} catch (Exception e) {
		}
	}

	/**
	 * Busca una lista de Mecanismos de Titulación por el ID de la Carrera
	 * @param id de la carrera
	 * @return Lista de mecanismos de titulacion por Carrera
	 * @throws MecanismoTitulacionCarreraNoEncontradoException
	 * @throws MecanismoTitulacionCarreraException
	 */
	@SuppressWarnings("unchecked")
	public List<MecanismoCarrera> listaMccttXcarreraSinEstado(Integer crrId) throws MecanismoTitulacionCarreraNoEncontradoException, MecanismoTitulacionCarreraException {
		List<MecanismoCarrera> retorno = null;
		if (crrId != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select mcttcr from MecanismoTitulacionCarrera mcttcr ");
				sbsql.append(" where mcttcr.mcttcrCarrera.crrId = :carrera");
	
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", crrId);
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new MecanismoTitulacionCarreraNoEncontradoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.todos.no.result.exception")));
			} catch (NonUniqueResultException e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Busca una lista de Mecanismos de Titulación por el ID de la Carrera
	 * @param id de la carrera
	 * @return Lista de mecanismos de titulacion por Carrera
	 * @throws MecanismoTitulacionCarreraNoEncontradoException
	 * @throws MecanismoTitulacionCarreraException
	 */
	@Override
	public MecanismoCarrera buscarXMecanismoTitulacionXCarrera(Integer crrId, Integer mcttId) throws MecanismoTitulacionCarreraNoEncontradoException, MecanismoTitulacionCarreraException {
		MecanismoCarrera retorno = null;
		if (crrId != null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select mcttcr from MecanismoTitulacionCarrera mcttcr ");
				sbsql.append(" where mcttcr.mcttcrCarrera.crrId = :carrera");
				sbsql.append(" and mcttcr.mcttcrMecanismoTitulacion.mcttId = :mecanismoTitulacion");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", crrId);
				q.setParameter("mecanismoTitulacion", mcttId);
				retorno = (MecanismoCarrera) q.getSingleResult();
			} catch (NoResultException e) {
				throw new MecanismoTitulacionCarreraNoEncontradoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.todos.no.result.exception")));
			} catch (NonUniqueResultException e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new MecanismoTitulacionCarreraException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"MecanismoTitulacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
     * Guarda un Mecanismos de Titulación en la BD
     * @param entidad -> tipo MecanismoTitulacionCarrera, dato para guardar en db.
     * @return entidad Mecanismos de Titulación nula si el dato de entrada es NULL, caso contrario entidad guardada en db. 
     * @throws MecanismoTitulacionCarreraException - Excepción general
     */
	public MecanismoCarrera guardar(MecanismoCarrera entidad) throws MecanismoTitulacionCarreraException {
		
		MecanismoCarrera retorno = null;
		
		if (entidad != null) {
				try {
		        	retorno = em.find(MecanismoCarrera.class, entidad.getMccrId());
		        	
		    		if (retorno == null) {
		        		
		        		retorno = new MecanismoCarrera();
		        		retorno.setMccrCarrera(entidad.getMccrCarrera());
		        		retorno.setMccrEstado(entidad.getMccrEstado());
		        		retorno.setMccrMecanismoTitulacion(entidad.getMccrMecanismoTitulacion());
		        	    
		        		em.persist(retorno);
		        		
		        	}else {
		        		retorno.setMccrEstado(entidad.getMccrEstado());
					}
			        
		        } catch (Exception e) {
		        	throw new MecanismoTitulacionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacionCarrera.añadir.exception")));
				}
			}
	   return retorno;
	}

}
