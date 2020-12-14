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
   
 ARCHIVO:     FechaNacimientoValidator.java	  
 DESCRIPCION: Validator el cual valida que la fecha de nacimiento sea minimo de hace 18 anios 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018 		  Daniel Albuja                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;


import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;

/**
 * Clase (Validator) FechaNacimientoValidator.
 * Validator el cual valida que la fecha de nacimiento sea minimo de hace 18 anios
 * @author dalbuja.
 * @version 1.0
 */

@FacesValidator("fechaNacimientoValidator")
public class FechaNacimientoValidator implements Validator {
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		Date valorDate = null;
		
		if(valor instanceof Date){
			valorDate = (Date)valor;
			if(!GeneralesUtilidades.verificarEdad(valorDate)){
				FacesMessage msg = new FacesMessage("Error","Fecha incorrecta");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
			if(!GeneralesUtilidades.verificarEdadMaxima(valorDate)){
				FacesMessage msg = new FacesMessage("Error","La edad excede el límite superior de años");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}else{
			FacesMessage msg = new FacesMessage("Error","Fecha incorrecta");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
