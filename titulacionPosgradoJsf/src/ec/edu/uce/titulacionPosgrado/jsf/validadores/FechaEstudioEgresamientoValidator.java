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
 10-NOVIEMBRE-2016 		  Gabriel Mafla                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (Validator) FechaEstudioValidator.
 * Valida las fechas de ingreso , egresamiento y de grado
 * @author gmafla.
 * @version 1.0
 */

@FacesValidator("fechaEstudioEgresamientoValidator")
public class FechaEstudioEgresamientoValidator implements Validator {
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		Integer tipoFecha = Integer.parseInt((String)comp.getAttributes().get("tipoFecha")); //0.- ingreso, 1.- egreso, 2.- grado
		EstudianteAptitudJdbcDto estudiante = (EstudianteAptitudJdbcDto)comp.getAttributes().get("estudiante");
		
		Date fechaIng = null;
		Date fechaEgr = null;
		Date fechaGra = null;
		
		fechaIng = tipoFecha == 0 ? (Date)valor: estudiante.getFcesFechaInicio();
		fechaEgr = tipoFecha == 1 ? (Date)valor: estudiante.getFcesFechaEgresamiento();
		fechaGra = tipoFecha == 2 ? (Date)valor: estudiante.getFcesFechaActaGrado();
		
		if(fechaIng != null && fechaEgr != null && fechaGra != null){
			if(!GeneralesUtilidades.verificarEntreFechas(fechaIng, fechaGra, fechaEgr)){
				FacesMessage msg = new FacesMessage("Error","La fecha de egresamiento debe estar entre las fechas de ingreso y de grado");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}else{
			if(fechaIng != null && fechaEgr != null){
				if(GeneralesUtilidades.calcularDiferenciFechas(fechaIng, fechaEgr)<=0){
					FacesMessage msg = new FacesMessage("Error","La fecha de ingreso debe ser menor a la fecha de egresamiento");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
			if(fechaIng != null && fechaGra != null){
				if(GeneralesUtilidades.calcularDiferenciFechas(fechaIng, fechaGra)<=0){
					FacesMessage msg = new FacesMessage("Error","La fecha de ingreso debe ser menor a la fecha de grado");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
			if(fechaEgr != null && fechaGra != null){
				if(GeneralesUtilidades.calcularDiferenciFechas(fechaIng, fechaGra)<=0){
					FacesMessage msg = new FacesMessage("Error","La fecha de egresamiento debe ser menor a la fecha de grado");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
		}
		Date hoy = new Date();
		if(fechaEgr!=null && fechaEgr.after(hoy) ){
			FacesUtil.limpiarMensaje();
			FacesMessage msg = new FacesMessage("Error","La fecha de culminación de malla no puede ser mayor que la fecha actual");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
		if(fechaGra!=null && fechaGra.after(hoy) ){
			FacesUtil.limpiarMensaje();
			FacesMessage msg = new FacesMessage("Error","La fecha de grado no puede ser mayor que la fecha actual");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
		if(fechaIng!=null &&  fechaIng.after(hoy)){
			FacesUtil.limpiarMensaje();
			FacesMessage msg = new FacesMessage("Error","La fecha de ingreso no puede ser mayor que la fecha actual");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}

