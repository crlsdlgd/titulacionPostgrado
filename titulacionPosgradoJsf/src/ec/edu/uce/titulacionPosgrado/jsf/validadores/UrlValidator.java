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
   
 ARCHIVO:     UrlValidator.java	  
 DESCRIPCION: Validator el cual verifica que el texto que se ingreso sea una direcciónde correo válida. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 06-MAY-2016 		  Gabriel Mafla                         Emisión Inicial
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
 * Clase (Validator) urlValidator.
 * Validator el cual verifica que e texto que se ingreso sea un link de tesis válida.
 * @author gmafla.
 * @version 1.0
 */

@FacesValidator("urlValidator")
public class UrlValidator implements Validator {
		private static final String PATRON =  "http://www.dspace.uce.edu.ec/handle/[0-9]+/[0-9]+";
	
		@Override
		public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
			String valorSt = null;
			
			if(valor instanceof String){
				valorSt = (String)valor;
				//verifico si tiene datos
				if(GeneralesUtilidades.eliminarEspaciosEnBlanco(valorSt).length()>0){
					if(!Pattern.matches(PATRON, valorSt)){
						FacesMessage msg = new FacesMessage("Por favor debe ingresar un link de tesis valido, ejemplo: http://www.dspace.uce.edu.ec/handle/25000/6892");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						throw new ValidatorException(msg);
					}
				}
			}else{
				FacesMessage msg = new FacesMessage("Error","Debe tener la siguente estructura http://www.dspace.uce.edu.ec/handle/25000/6892");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		
		}
	

	
}
