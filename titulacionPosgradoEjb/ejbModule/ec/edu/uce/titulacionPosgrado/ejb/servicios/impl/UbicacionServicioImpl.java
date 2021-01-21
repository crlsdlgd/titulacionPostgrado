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
   
 ARCHIVO:     UbicacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Ubicacion. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UbicacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;

/**
 * Clase (Bean)UbicacionServicioImpl.OSb
 * Bean declarado como Stateless.
 * @author gmafla
 * @version 1.0
 */

@Stateless
public class UbicacionServicioImpl implements UbicacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Ubicacion por su id
	 * @param id - de la Ubicacion a buscar
	 * @return Ubicacion con el id solicitado
	 * @throws UbicacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Ubicacion con el id solicitado
	 * @throws UbicacionException - Excepcion general
	 */
	@Override
	public Ubicacion buscarPorId(Integer id) throws UbicacionNoEncontradoException, UbicacionException {
		Ubicacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Ubicacion.class, id);
			} catch (NoResultException e) {
				throw new UbicacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ubicacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new UbicacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ubicacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new UbicacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ubicacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Ubicacion existentes en la BD
	 * @return lista de todas las entidades Ubicacion existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Ubicacion> listarTodos() throws UbicacionNoEncontradoException{
		List<Ubicacion> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select ubc from Ubicacion ubc ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new UbicacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ubicacion.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ubicacion> listarCantones() throws UbicacionNoEncontradoException{
		List<Ubicacion> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select ubc from Ubicacion ubc where ubc.ubcJerarquia = 2 order by ubc.ubcDescripcion");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new UbicacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ubicacion.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

}
