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
   
 ARCHIVO:     SinCaracteresEspecialesValidator.java	  
 DESCRIPCION: Validator el cual verifica que el texto no tenga caracteres epeciales 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 19/01/2020 		  Carlos Delgado                     Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;


import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;

/**
 * Clase (Validator) comprobarEspeCodigo.
 * Validator el cual verifica que el Espe Codigo no se encuentre en la base de datos
 * @author Carlos Delgado.
 * @version 1.0
 */

@FacesValidator("comprobarEspeCodigo")
public class ComprobarEspeCodigo implements Validator {

	
	@EJB
	private CarreraServicio servCarreraServicio; //no se puede utilizar EJB's en un validador, pues los validadores solo se ejecutan en el cliente y el EJB en el servidor 
	private boolean flag;


	@Override
	public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
		//boolean flag=false;
		flag=false;
		int valorSt;
		if(valor instanceof Integer){
			valorSt=(int) valor;
			try{
			flag=servCarreraServicio.ifExistEspeCodigo(valorSt);
			if(flag){
				FacesMessage msg = new FacesMessage("Código ESPE Duplicado");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
//		String valorSt = null;
//		
//		if(valor instanceof String){
//			valorSt = (String)valor;
//			//verifico si tiene datos
//			if(GeneralesUtilidades.eliminarEspaciosEnBlanco(valorSt).length()>0){
//				if(!Pattern.matches(PATRON, valorSt)){
//					FacesMessage msg = new FacesMessage("Error","No debe contener caracteres especiales");
//					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//					throw new ValidatorException(msg);
//				}
//			}
//		}else{
//			FacesMessage msg = new FacesMessage("Error","Sólo permite caracteres");
//			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//			throw new ValidatorException(msg);
//		}
	
	}


	public static void espeCodDuplicado(boolean flag) throws ValidatorException {
		if(flag){
			FacesMessage msg = new FacesMessage("Código ESPE Duplicado");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}else{
			FacesMessage msg = new FacesMessage("Error al momento de leer el ESPE Código");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
			
		}

	}

	
}
