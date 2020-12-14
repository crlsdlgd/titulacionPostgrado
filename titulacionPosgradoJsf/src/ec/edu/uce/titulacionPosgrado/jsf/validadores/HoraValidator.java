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
   
 ARCHIVO:     horaValidator.java	  
 DESCRIPCION: Validator el cual verifica que el texto tenga formato de hora 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-DICIEMBRE-2016 		 GABRIEL MAFLA                 Emisión Inicial
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
 * Clase (Validator) SinCaracteresEspecialesValidator.
 * Validator el cual verifica que el texto tenga formato de hora 
 * @author gmafla.
 * @version 1.0
 */

@FacesValidator("horaValidator")
public class HoraValidator implements Validator {
	private static final String PATRON = "([01][0-9]|2[0-4])H[0-5][0-9]";
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		String valorSt = null;
		
		if(valor instanceof String){
			valorSt = (String)valor;
			//verifico si tiene datos
			if(GeneralesUtilidades.eliminarEspaciosEnBlanco(valorSt).length()>0){
				if(!Pattern.matches(PATRON, valorSt)){
					FacesMessage msg = new FacesMessage("Por favor debe ingresar una hora valida");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
		}else{
			FacesMessage msg = new FacesMessage("Error","Sólo permite como separador ' : ' ");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	
	}
	
}
