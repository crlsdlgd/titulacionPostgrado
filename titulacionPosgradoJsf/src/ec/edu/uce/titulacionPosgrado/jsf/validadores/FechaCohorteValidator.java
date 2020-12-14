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
   
 ARCHIVO:     FechaEstudioValidator.java	  
 DESCRIPCION: Valida las fechas de ingreso , egresamiento y de grado 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-04-0218 		  Danie Albuja                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;

/**
 * Clase (Validator) FechaEstudioValidator.
 * Valida las fechas de ingreso , egresamiento y de grado
 * @author dalbuja.
 * @version 1.0
 */

@FacesValidator("fechaCohorteValidator")
public class FechaCohorteValidator implements Validator {
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		Integer tipoFecha = Integer.parseInt((String)comp.getAttributes().get("tipoFecha")); //0.- ingreso, 1.- egreso, 2.- grado
		EstudianteValidacionJdbcDto estudiante = (EstudianteValidacionJdbcDto)comp.getAttributes().get("estudiante");
		
		Date fechaIng = null;
		Date fechaEgr = null;
		
		fechaIng = tipoFecha == 0 ? (Date)valor: estudiante.getFcesFechaInicioCohorte();
		fechaEgr = tipoFecha == 1 ? (Date)valor: estudiante.getFcesFechaFinCohorte();
		Date hoy = new Date();
			if(fechaIng != null && fechaEgr != null){
				if(GeneralesUtilidades.calcularDiferenciFechas(fechaIng, fechaEgr)<=0){
					FacesMessage msg = new FacesMessage("Error","La fecha de inicio de la Cohorte debe ser menor a la fecha de fin de la Cohorte");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
				if(GeneralesUtilidades.calcularDiferenciFechas(fechaIng, fechaEgr)>1460){
					FacesMessage msg = new FacesMessage("Error","La fecha de fin de Cohorte no puede ser mayor a 4 años con la fecha de inicio de la cohorte");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
			if(fechaIng != null && fechaIng.after(hoy)){
					FacesMessage msg = new FacesMessage("Error","La fecha de inicio de la Cohorte no puede ser mayor a la fecha actual");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
			}
		
//		if(fechaEgr!=null && fechaEgr.after(hoy) ){
//			Calendar c = Calendar.getInstance();
//			c.setTime(fechaEgr);
//			FacesUtil.limpiarMensaje();
//			FacesMessage msg = new FacesMessage("Error","La fecha de fin de Cohorte no puede ser mayor que la fecha actual");
//			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//			throw new ValidatorException(msg);
//		}
//		if(fechaEgr!=null ){
//			Calendar c = Calendar.getInstance();
//			c.setTime(hoy);
//			c.add(Calendar.YEAR, -10);
//			Date newDate = c.getTime();
//			if(fechaEgr.before(newDate)){
//				FacesUtil.limpiarMensaje();
//				FacesMessage msg = new FacesMessage("Error","La fecha de fin de Cohorte no puede ser mayor a 10 años de la fecha actual");
//				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//				throw new ValidatorException(msg);
//			}
//		}
//		if(fechaIng!=null ){
//			Calendar c = Calendar.getInstance();
//			c.setTime(hoy);
//			c.add(Calendar.YEAR, -10);
//			Date newDate = c.getTime();
//			if(fechaIng.before(newDate)){
//				FacesUtil.limpiarMensaje();
//				FacesMessage msg = new FacesMessage("Error","La fecha de inicio de Cohorte no puede ser mayor a 10 años de la fecha actual");
//				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//				throw new ValidatorException(msg);
//			}
//		}
	}
}

