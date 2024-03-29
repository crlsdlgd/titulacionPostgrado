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
   
 ARCHIVO:     SinCaracteresEspecialesConNumerosValidator.java	  
 DESCRIPCION: Validator el cual verifica que el texto contenga números y no tenga caracteres epeciales  
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018 		  Daniel Albuja                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;


import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;


/**
 * Clase (Validator) SinCaracteresEspecialesConNumerosValidator.
 * Validator el cual verifica que el texto contenga números y no tenga caracteres epeciales 
 * @author dalbuja.
 * @version 1.0
 */

@FacesValidator("sinCaracteresEspecialesConNumerosValidator")
public class SinCaracteresEspecialesConNumerosValidator implements Validator {
	private static final String PATRON = "^[a-zA-Z0-9-\\s-ñÑ-áÁ-éÉ-íÍ-óÓ-úÚ]+";
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		String valorSt = null;
		
		if(valor instanceof String){
			valorSt = (String)valor;
			//verifico si tiene datos
			if(GeneralesUtilidades.eliminarEspaciosEnBlanco(valorSt).length()>0){
				if(!Pattern.matches(PATRON, valorSt)){
					FacesMessage msg = new FacesMessage("Error","No debe contener caracteres especiales");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
		}else{
			FacesMessage msg = new FacesMessage("Error","Sólo permite caracteres, números y guiones");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	
	}
	
}
