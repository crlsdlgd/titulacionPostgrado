/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     EstrategiaSesionInvalida.java      
 DESCRIPCION: Clase encargada de manejar las estrategias de una sesión inválida. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 08-04-2017            	 Daniel Albuja						Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity;

/**
 * Clase EstrategiaSesionInvalida.
 * Clase encargada de manejar las estrategias de una sesión inválida. 
 * @author dalbuja
 * @version 1.0
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.util.StringUtils;

public class EstrategiaSesionInvalida implements InvalidSessionStrategy{
	private static final String FACES_REQUEST_HEADER = "faces-request";
	private String invalidSessionUrl;
	
	@Override
	public void onInvalidSessionDetected(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		boolean ajaxRedirect = "partial/ajax".equals(request.getHeader(FACES_REQUEST_HEADER));
		if (ajaxRedirect) {
			String contextPath = request.getContextPath();
			String redirectUrl = contextPath + invalidSessionUrl;
			String ajaxRedirectXml = createAjaxRedirectXml(redirectUrl);
			response.setContentType("text/xml");
			response.getWriter().write(ajaxRedirectXml);
		}else{
			String requestURI = getRequestUrl(request);
			request.getSession(true);
			response.sendRedirect(requestURI);
		}
	}
	
	private String getRequestUrl(HttpServletRequest request){
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		if (StringUtils.hasText(queryString)) {
			requestURL.append("?").append(queryString);
		}
		return requestURL.toString();
	}

	private String createAjaxRedirectXml(String redirectUrl){
		return new StringBuilder()
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<partial-response><redirect url=\"")
				.append(redirectUrl)
				.append("\"></redirect></partial-response>").toString();
	}

	public void setInvalidSessionUrl(String invalidSessionUrl){
		this.invalidSessionUrl = invalidSessionUrl;
	}
}
