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
   
 ARCHIVO:     VigenciaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Vigencia. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-04-2017           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ValidacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ValidacionValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ValidacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Validacion;

/**
 * Clase (Bean)ValidacionServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class ValidacionServicioImpl implements ValidacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	@Override
	public Validacion buscarPorId(Integer id)
			throws ValidacionNoEncontradoException, ValidacionValidacionException, ValidacionException{
		Validacion retorno = new Validacion();
		if (id != null) {
			try {
				retorno = em.find(Validacion.class, id);
			} catch (NoResultException e) {
				throw new ValidacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Validacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ValidacionValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Validacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Validacion.buscar.por.id.exception")));
			}
		}
		
		return retorno;
	}

	@Override
	public Validacion buscarXtrttId(Integer trttId) throws ValidacionNoEncontradoException {
		Validacion retorno = new Validacion();
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select vld from Validacion vld ");
			sbsql.append(" where vld.vldProcesoTramite.prtrTramiteTitulo.trttId = :trttId  ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("trttId",trttId);
			retorno = (Validacion)q.getSingleResult();
		} catch (NoResultException e) {
			throw new ValidacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Validacion.buscar.por.id.no.result.exception",trttId)));
		}
		
		return retorno;
	}
	
	@Override
	public Validacion cambiarCulminoMalla(Integer vldId) throws ValidacionNoEncontradoException {
		Validacion retorno = new Validacion();
		try {
			retorno = em.find(Validacion.class, vldId);
//			retorno.setVldCulminoMalla(ValidacionConstantes.SI_CULMINO_MALLA_VALUE);
		} catch (Exception e) {
			throw new ValidacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Validacion.buscar.por.id.no.result.exception",vldId)));
		}
		
		return retorno;
	}

	
	@Override
	public void modificarActualizarConocimientos(Integer vldId) throws ValidacionNoEncontradoException {
		Validacion retorno = new Validacion();
		try {
			retorno = em.find(Validacion.class, vldId);
			retorno.setVldRslActConocimiento(ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
		} catch (Exception e) {
			throw new ValidacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Validacion.buscar.por.id.no.result.exception",vldId)));
		}
		
	}
}
