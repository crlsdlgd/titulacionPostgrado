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


import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaDcnAsgTitulacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDcnAsgTitulacion;

/**
 * Clase (Bean)FichaDocenteServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class FichaDcnAsgTitulacionServicioImpl implements FichaDcnAsgTitulacionServicio{

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad FichaDocente por su id
	 * @param id - de la FichaDocente a buscar
	 * @return FichaDocente con el id solicitado
	 * @throws FichaDocenteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaDocente con el id solicitado
	 * @throws FichaDocenteException - Excepcion general
	 */
	
	public FichaDcnAsgTitulacion buscarPorId(Integer id) throws FichaDcnAsgTitulacionNoEncontradoException, FichaDcnAsgTitulacionException{
		FichaDcnAsgTitulacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(FichaDcnAsgTitulacion.class, id);
			} catch (NoResultException e) {
				throw new FichaDcnAsgTitulacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Dcn.Titulacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new FichaDcnAsgTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Dcn.Titulacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new FichaDcnAsgTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Dcn.Titulacion..buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	

	
	
	public boolean buscarXFichaDocenteID(int fichaDocenteID)  throws FichaDcnAsgTitulacionNoEncontradoException, FichaDcnAsgTitulacionException{
		boolean retorno = false;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select fcdcastt from FichaDcnAsgTitulacion fcdcastt  ");
			sbsql.append(" where fcdcastt.fcdcasttFichaDocente.fcdcId  = :fichaDocenteID");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("fichaDocenteID", fichaDocenteID);
			q.getSingleResult();
			retorno = true;
		} catch (NoResultException e) {
			retorno = false;
		} catch (NonUniqueResultException e) {
			retorno = true;
			throw new FichaDcnAsgTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Doc.Asg.Titulacion.buscar.por.ficha.docente.id.non.unique.result.exception",fichaDocenteID)));
		} catch (Exception e) {
			throw new FichaDcnAsgTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Ficha.Doc.Asg.Titulacion.buscar.por.ficha.docente.id.exception")));
		}
		return retorno;
	}
	
	
    
}
