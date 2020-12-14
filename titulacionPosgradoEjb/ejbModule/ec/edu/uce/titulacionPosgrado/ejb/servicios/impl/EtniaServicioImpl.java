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
   
 ARCHIVO:     EtniaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Etnia. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 21/02/2018					Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;

/**
 * Clase (Bean)EtniaServicioImpl.
 * Bean declarado como Stateless.
 * @author gmafla
 * @version 1.0
 */

@Stateless
public class EtniaServicioImpl implements EtniaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Etnia por su id
	 * @param id - de la Etnia a buscar
	 * @return Etnia con el id solicitado
	 * @throws EtniaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Etnia con el id solicitado
	 * @throws EtniaException - Excepcion general
	 */
	@Override
	public Etnia buscarPorId(Integer id) throws EtniaNoEncontradoException, EtniaException {
		Etnia retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Etnia.class, id);
			} catch (NoResultException e) {
				throw new EtniaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new EtniaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new EtniaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Etnia existentes en la BD
	 * @return lista de todas las entidades Etnia existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Etnia> listarTodos() throws EtniaNoEncontradoException{
		List<Etnia> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select etn from Etnia etn ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new EtniaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Etnia.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

}
