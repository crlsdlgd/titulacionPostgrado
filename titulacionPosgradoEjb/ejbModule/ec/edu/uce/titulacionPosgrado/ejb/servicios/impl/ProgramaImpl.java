/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ProgramaImpl.java	  
 DESCRIPCION: EJB sin estado que maneja las peticiones de programas de los usuarios. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12/1/2021				Carlos Delgado 						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;

import java.sql.ResultSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProgramaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Duracion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Modalidad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TipoFormacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TipoSede;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Vigencia;

@Stateless
public class ProgramaImpl implements ProgramaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	//El tipo de sede se filtra x defecto por el regimen academico 2009 y 2013 (RGAC_ID IN (1,2))
	@Override
	public List<TipoSede> ListarTodosTipoSede() {
		List<TipoSede> retorno =null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("Select ts from TipoSede ts ");
		sbsql.append("where ts.tiseRegimenAcademico.rgacId in (1,2)");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			//throw new TipoSedeNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	//El tipo de Formacion se filtra x defecto por el regimen academico 2009 y 2013 (RGAC_ID IN (1,2))
	@Override
	public List<TipoFormacion> ListarTodosTipoFormacion() {
		List<TipoFormacion> retorno =null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("Select tf from TipoFormacion tf, NivelFormacion nf ");
		sbsql.append("where tf.tifrNivelFormacion.nvfrId=nf.nvfrId ");
		sbsql.append("and nf.nvfrRegimenAcademico.rgacId in (1,2) ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			//throw new TipoSedeNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	@Override
	public List<Modalidad> ListarTodosModalidad() {
		List<Modalidad> retorno =null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("Select mdl from Modalidad mdl ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			//throw new TipoSedeNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	@Override
	public List<Vigencia> ListarTodosVigencia() {
		List<Vigencia> retorno =null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("Select vgn from Vigencia vgn ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			//throw new TipoSedeNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	@Override
	public List<Duracion> ListarTodosDuracion() {
		List<Duracion> retorno =null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("Select drc from Duracion drc");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		if(retorno.size()<=0){
			//throw new TipoSedeNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TipoSede.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	@Override
	public void guardarPrograma(Carrera crr, ConfiguracionCarrera cncr) {
		try {
			StringBuffer sbsql = new StringBuffer();
			int crrId=0;
			sbsql.append("Select max(crr.crrId) as crrId from Carrera crr");
			Query q = em.createQuery(sbsql.toString());
			crrId=(int) q.getSingleResult();
			
			crr.setCrrId(crrId+1);
			crr.setCrrDetalle(crr.getCrrDescripcion()+" "+crr.getCrrFacultad());
			
			StringBuffer sbsql2 = new StringBuffer();
			sbsql2.append("Insert into Carrera crr ");
			sbsql2.append("(crr.crrId, crr.crrDescripcion, crr.crrCodSniese, crr.crrNivel, crr.crrEspeCodigo, crr.crrDetalle, crr.crrFacultad.fclId, crr.crrTipoEvaluacion) ");
			sbsql2.append("values (:crrId, :crrDescripcion, :crrCodSniese, :crrNivel, :crrEspeCodigo, :crrDetalle, :fclId, :crrTipoEvaluacion)");
			Query q2 = em.createQuery(sbsql2.toString());
			q2.setParameter("crrId", crr.getCrrId());
			q2.setParameter("crrDescripcion", crr.getCrrDescripcion());
			q2.setParameter("crrCodSniese", crr.getCrrCodSniese());
			q2.setParameter("crrNivel", crr.getCrrNivel());
			q2.setParameter("crrEspeCodigo", crr.getCrrEspeCodigo());
			q2.setParameter("crrDetalle", crr.getCrrDetalle());
			q2.setParameter("fclId", crr.getCrrFacultad().getFclId());
			q2.setParameter("crrTipoEvaluacion", crr.getCrrTipoEvaluacion());
			q2.executeUpdate();
//			em.persist(crr);// Por si acaso con el entity manager
			
			StringBuffer sbsql3 = new StringBuffer();
			int cncrId=0;
			sbsql3.append("Select max(cncr.cncrId) as cncrId from ConfiguracionCarrera cncr");
			Query q3 = em.createQuery(sbsql3.toString());
			cncrId=(int) q3.getSingleResult();
			cncr.setCncrId(cncrId+1);
			cncr.setCncrCarrera(crr);
			
			StringBuffer sbsql4 = new StringBuffer();
			sbsql4.append("Insert into ConfiguracionCarrera cncr ");
			sbsql4.append("(cncr.cncrId; cncr.cncrUbicacion.ubcId, cncr.cncrTipoSede.tiseId, cncr.cncrTitulo.ttlId, cncr.cncrTipoFormacion.tifrId, cncr.cncrCarrera.crrId, cncr.cncrVigencia.vgnId, cncr.cncrDuracion.drcId) ");
			sbsql4.append("values (:cncrId, :ubcId, :tiseId, :ttlId, :tifrId, :crrId, :vgnId, :drcId)");
			Query q4 = em.createQuery(sbsql4.toString());
			q4.setParameter("cncrId", cncr.getCncrId());
			q4.setParameter("ubcId", cncr.getCncrUbicacion().getUbcId());
			q4.setParameter("tiseId", cncr.getCncrTipoSede().getTiseId());
			q4.setParameter("ttlId", cncr.getCncrTitulo().getTtlId());
			q4.setParameter("tifrId", cncr.getCncrTipoFormacion().getTifrId());
			q4.setParameter("crrId", cncr.getCncrCarrera().getCrrId());
			q4.setParameter("vgnId", cncr.getCncrVigencia().getVgnId());
			q4.setParameter("drcId", cncr.getCncrDuracion().getDrcId());
			q4.executeUpdate();
//			em.persist(cncr);// Por si acaso con el entity manager
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
