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
   
 ARCHIVO:     NotaValidator.java	  
 DESCRIPCION: Validator el cual verifica que el texto que se ingreso cumpla con los estándares de notas. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 10-NOVIEMBRE-2016 		 Gabriel Mafla                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Clase (Validator) NotaValidator.
 * Validator el cual verifica que el texto que se ingreso cumpla con los estándares de notas.
 * @author gmafla.
 * @version 1.0
 */

@FacesValidator("notaValidator")
public class NotaValidator implements Validator {

	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		BigDecimal valorDouble = null;
		if(valor instanceof BigDecimal){
			valorDouble= (BigDecimal)valor;
			if(valorDouble.doubleValue() < 0 || valorDouble.doubleValue() > 10){
				FacesMessage msg = new FacesMessage("Error","Valores entre 0 y 10 puntos");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}else{
			FacesMessage msg = new FacesMessage("Error","Sólo se permite número enteros");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	
	}
	
}
