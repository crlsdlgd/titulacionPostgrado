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
   
 ARCHIVO:     DuracionReconocimientoValidator.java	  
 DESCRIPCION: Validator el cual valida el tiempo de reconocimiento según el tipo de duración
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 25-ABRIL-2016 		  David Arellano                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaEstudianteConstantes;

/**
 * Clase (Validator) CedulaValidator.
 * Validator el cual valida el tiempo de reconocimiento según el tipo de duración
 * @author darellano.
 * @version 1.0
 */

@FacesValidator("duracionReconocimientoValidator")
public class DuracionReconocimientoValidator implements Validator {
	
	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		Integer tipoId = (Integer)comp.getAttributes().get("tipoDurRec");
		if(valor instanceof Integer){
			Integer valorInteger = (Integer)valor;
			if(tipoId == FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_SEMESTRES_VALUE){
				if(valorInteger > 12){
					FacesMessage msg = new FacesMessage("Error","No puede ser mas de 12 semestres");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}else if(tipoId == FichaEstudianteConstantes.TIPO_DURACION_RECONOCIMIENTO_ANIOS_VALUE){
				if(valorInteger > 6){
					FacesMessage msg = new FacesMessage("Error","No puede ser mas de 6 años");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
		}else{
			FacesMessage msg = new FacesMessage("Error","Solo debe ingresar números");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
