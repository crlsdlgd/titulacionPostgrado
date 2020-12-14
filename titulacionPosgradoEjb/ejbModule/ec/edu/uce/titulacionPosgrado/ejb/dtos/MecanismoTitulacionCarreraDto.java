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
   
 ARCHIVO:     MecanismoTitulacionDto.java	  
 DESCRIPCION: DTO encargado de manejar los datos para el registro de un mecanismo_titulacion_carrera. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018		  	daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.dtos;


import java.io.Serializable;

/**
 * Clase (DTO) MecanismoTitulacionDto.
 * DTO encargado de manejar los datos para el registro de un mecanismo_titulacion_carrera.
 * @author dalbuja.
 * @version 1.0
 */
public class MecanismoTitulacionCarreraDto implements Serializable {
	
	private static final long serialVersionUID = 8581051781448182766L;

	/*******************************************************/
	/********* Declaración de variables para el DTO ********/
	/*******************************************************/
	
	// atributos entidad carrera
	private int crrId;
	private String crrDescripcion;
	private String crrCodSniese;
	private Integer crrNivel;
	private String crrDetalle;
	private Integer crrFacultad;
	
	// atributos entidad facultad
	private int fclId;
	private String fclDescripcion;
	
	// atributos entidad facultad
	private int mcttId;
	private String mcttCodigoSniese;
	private String mcttDescripcion;
	private Integer mcttEstado;
	
	//atributos entidad mecanismo titulacion carrera
	private int mcttcrId;
	private Integer mcttcrEstado;
	private Integer mcttcrCrrId;
	private Integer mcttcrMcttId;
	private Integer mcttcrPorcentajeComplexivo;
	
	public MecanismoTitulacionCarreraDto() {
	}


	/*******************************************************/
	/***************** Métodos Geters y Seters**************/
	/*******************************************************/

	public int getCrrId() {
		return crrId;
	}


	public void setCrrId(int crrId) {
		this.crrId = crrId;
	}


	public String getCrrDescripcion() {
		return crrDescripcion;
	}


	public void setCrrDescripcion(String crrDescripcion) {
		this.crrDescripcion = crrDescripcion;
	}


	public String getCrrCodSniese() {
		return crrCodSniese;
	}


	public void setCrrCodSniese(String crrCodSniese) {
		this.crrCodSniese = crrCodSniese;
	}


	public Integer getCrrNivel() {
		return crrNivel;
	}


	public void setCrrNivel(Integer crrNivel) {
		this.crrNivel = crrNivel;
	}


	public String getCrrDetalle() {
		return crrDetalle;
	}


	public void setCrrDetalle(String crrDetalle) {
		this.crrDetalle = crrDetalle;
	}


	public Integer getCrrFacultad() {
		return crrFacultad;
	}


	public void setCrrFacultad(Integer crrFacultad) {
		this.crrFacultad = crrFacultad;
	}


	public int getFclId() {
		return fclId;
	}


	public void setFclId(int fclId) {
		this.fclId = fclId;
	}


	public String getFclDescripcion() {
		return fclDescripcion;
	}


	public void setFclDescripcion(String fclDescripcion) {
		this.fclDescripcion = fclDescripcion;
	}


	public int getMcttId() {
		return mcttId;
	}


	public void setMcttId(int mcttId) {
		this.mcttId = mcttId;
	}


	public String getMcttCodigoSniese() {
		return mcttCodigoSniese;
	}


	public void setMcttCodigoSniese(String mcttCodigoSniese) {
		this.mcttCodigoSniese = mcttCodigoSniese;
	}


	public String getMcttDescripcion() {
		return mcttDescripcion;
	}


	public void setMcttDescripcion(String mcttDescripcion) {
		this.mcttDescripcion = mcttDescripcion;
	}


	public Integer getMcttEstado() {
		return mcttEstado;
	}


	public void setMcttEstado(Integer mcttEstado) {
		this.mcttEstado = mcttEstado;
	}


	public int getMcttcrId() {
		return mcttcrId;
	}


	public void setMcttcrId(int mcttcrId) {
		this.mcttcrId = mcttcrId;
	}


	public Integer getMcttcrEstado() {
		return mcttcrEstado;
	}


	public void setMcttcrEstado(Integer mcttcrEstado) {
		this.mcttcrEstado = mcttcrEstado;
	}


	public Integer getMcttcrCrrId() {
		return mcttcrCrrId;
	}


	public void setMcttcrCrrId(Integer mcttcrCrrId) {
		this.mcttcrCrrId = mcttcrCrrId;
	}


	public Integer getMcttcrMcttId() {
		return mcttcrMcttId;
	}


	public void setMcttcrMcttId(Integer mcttcrMcttId) {
		this.mcttcrMcttId = mcttcrMcttId;
	}


	public Integer getMcttcrPorcentajeComplexivo() {
		return mcttcrPorcentajeComplexivo;
	}


	public void setMcttcrPorcentajeComplexivo(Integer mcttcrPorcentajeComplexivo) {
		this.mcttcrPorcentajeComplexivo = mcttcrPorcentajeComplexivo;
	}

	
	
	
	/*******************************************************/
	/***************** Método toString *********************/
	/*******************************************************/
    public String toString() {
    	String tabulador = "\t";
		StringBuilder sb = new StringBuilder();
		sb.append("crrId : " + crrId);
		sb.append(tabulador + "crrDescripcion : " + crrDescripcion);
		sb.append(tabulador + "crrCodSniese : " + crrCodSniese);
		sb.append(tabulador + "crrNivel : " + crrNivel);
		sb.append(tabulador + "crrDetalle : " + crrDetalle);
		sb.append(tabulador + "crrFacultad : " + crrFacultad);
		sb.append(tabulador + "fclId : " + fclId);
		sb.append(tabulador + "fclDescripcion : " + fclDescripcion);
		sb.append(tabulador + "mcttId : " + mcttId);
		sb.append(tabulador + "mcttCodigoSniese : " + mcttCodigoSniese);
		sb.append(tabulador + "mcttDescripcion : " + mcttDescripcion);
		sb.append(tabulador + "mcttEstado : " + mcttEstado);
		sb.append(tabulador + "mcttcrId : " + mcttcrId);
		sb.append(tabulador + "mcttcrEstado : " + mcttcrEstado);
		sb.append(tabulador + "mcttcrCrrId : " + mcttcrCrrId);
		sb.append(tabulador + "mcttcrMcttId : " + mcttcrMcttId);
		sb.append(tabulador + "mcttcrPorcentajeComplexivo : " + mcttcrPorcentajeComplexivo);
		return sb.toString();
    }

}
