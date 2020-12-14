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
   
 ARCHIVO:     NotaConverter.java	  
 DESCRIPCION: Converter el cual valida que el texto que se ingresa es un numero decimal para notas. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 10-NOVIEMBRE-2016 		  Gabriel Mafla                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.validadores;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Clase (Converter) NotaConverter.
 * Converter el cual valida que el texto que se ingresa es un numero decimal para notas.
 * @author gmafla.
 * @version 1.0
 */

@FacesConverter("notaConverter")
public class NotaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent comp, String valor) {
		BigDecimal valorObjetual = null;
		
		//VERIFICO SI EL VALOR INGRESADO ES UN NÚMERO DECIMAL 
		try {
			if(comp.isRendered()){
				@SuppressWarnings("unused")
				Double valorDouble = Double.parseDouble(valor);
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error","Debe ser un numero decimal, ejm: 15.23 ");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

		// VERIFICO QUE EL VALOR INGRESADO SEA UN NÚMERO Y QUE TENGA MÁXIMO 2 DECIMALES
		String [] partes = valor.split("\\.");
		if(partes.length > 2 ){
			FacesMessage msg = new FacesMessage("Error","Debe ser un número");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}else{
			if(partes.length == 2){
				if(partes[1].length()>2){
					FacesMessage msg = new FacesMessage("Error","El número debe tener máximo dos decimales");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ConverterException(msg);
				}
			}
		}
		
		if(comp.isRendered()){
			valorObjetual = new BigDecimal(valor);
		}else{
			valorObjetual = null;
		}
		
		return valorObjetual;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return ((BigDecimal) arg2).toString();
	}
	

}
