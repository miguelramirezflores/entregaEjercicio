package interfaz;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Concesionario;
import modelo.controladores.ConcesionarioControlador;

/**
 * Servlet implementation class ListadoConcesionario
 */
@WebServlet(description = "Primer Servlet", urlPatterns = { "/FichaConcesionario" })
public class FichaConcesionario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FichaConcesionario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Concesionario concesionario = new Concesionario();
		
		try {
			int idConcesionario = getIntParameter(request, "idConcesionario");
			System.out.println(request.getParameter("idConcesionario"));

			concesionario = (Concesionario) ConcesionarioControlador.getControlador().find(idConcesionario);
		} catch (Exception e) {}
		
		String mensajeAlUsuario = "";
		
		//eliminar el concesionario si es posible 
		
		if (request.getParameter("eliminar") != null) {
			try {
				
				ConcesionarioControlador.getControlador().remove(concesionario);
				response.sendRedirect(request.getContextPath() + "/ListadoConcesionario"); // Redirección del response hacia el listado

			}catch (Exception e) {
				mensajeAlUsuario = "Imposible eliminar. Es posible que existan restricciones.";
			}
		}
		
		//guardar el concesionario
		
		if (request.getParameter("guardar") != null) {
			// Obtengo todos los datos del profesor y los almaceno en BBDD
			try {
				concesionario.setCif(getStringParameter(request, "cfi"));
				concesionario.setNombre(getStringParameter(request, "nombre"));
				concesionario.setLocalidad(getStringParameter(request, "localidad"));
				ConcesionarioControlador.getControlador().save(concesionario);
				mensajeAlUsuario = "Guardado correctamente";
			}catch (Exception e) {
				throw new ServletException(e);
			}
		}
		
		
		//html
		
		
		response.getWriter().append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
				"<head>\r\n" + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\r\n" + 
				"<title>ficha</title>\r\n" + 
				"</head>\r\n" + 
				"\r\n" + 
				"<body " +((mensajeAlUsuario != null && mensajeAlUsuario != "")? "onLoad=\"alert('" + mensajeAlUsuario + "');\"" : "")  + " >\r\n" + 
				"<h1>Ficha de profesor</h1>\r\n" + 
				"<a href=\"ListadoConcesionario\">Ir al listado de concesionarios</a>" +
				"<form id=\"form1\" name=\"form1\" method=\"post\" action=\"FichaConcesionario\" onsubmit=\"return valida()\" >\r\n" +
				" <input type=\"hidden\" name=\"idconcesionario\" value=\"" + ((concesionario != null)? concesionario.getId() : "") + "\"\\>"+
				"  <p>\r\n" + 
				"    <label for=\"nombre\">Nombre:</label>\r\n" + 
				"    <input name=\"nombre\" type=\"text\" id=\"nombre\"  value=\"" + ((concesionario != null)? concesionario.getNombre() : "") + "\" />\r\n" + 
				"  </p>\r\n" + 
				"  <p>\r\n" + 
				"    <label for=\"localidad\">localidad:</label>\r\n" + 
				"    <input name=\"localidad\" type=\"text\" id=\"localidad\" value=\"" +  ((concesionario != null)? concesionario.getLocalidad() : "") + "\" />\r\n" + 
				"  </p>\r\n" + 
				"  <p>\r\n" + 
				"    <label for=\"cfi\">cfi:</label>\r\n" + 
				"    <input name=\"cfi\" type=\"text\" id=\"cfi\" value='" +  ((concesionario != null)? concesionario.getCif() : "")  + "'/>\r\n" + 
				"  </p>\r\n" + 
				"  <p>\r\n" + 
				"    <input type=\"submit\" name=\"guardar\" value=\"Guardar\" onclick=\"valida(form1)\" />\r\n" + 
				"    <input type=\"submit\" name=\"eliminar\" value=\"Eliminar\" />\r\n" + 
				"  </p>\r\n" + 
				"</form>"+ "<script>\r\n" + 
						"    function valida() {\r\n" + 
						"    var ok = true;\r\n" + 
						"    var msg = \"Debes escribir algo en todos campos\\n\";\r\n" + 
						"    var elementos = document.getElementById(\"form1\").elements;\r\n" + 
						"    for (var i = 0; i < elementos.length; i++) {\r\n" + 
						"        if(elementos[i].value == \"\"){\r\n" + 
						"            alert(msg);\r\n" + 
						"            return ok = false;\r\n" + 
						"         }\r\n" + 
						"    }\r\n" + 
						"    \r\n" + 
						"    \r\n" + 
						"  }\r\n" + 
						"</script>"+
				
				"</body>\r\n" + 
				"</html>\r\n" + 
				""
				);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public int getIntParameter (HttpServletRequest request, String nombreParametro) throws FormularioIncorrectoRecibidoException {
		try {
			return Integer.parseInt(request.getParameter(nombreParametro));
		} catch (Exception e) {
			throw new FormularioIncorrectoRecibidoException("No se ha recibido valor entero para el parámetro " + nombreParametro);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param nombreParametro
	 * @return
	 * @throws FormularioIncorrectoRecibidoException
	 */
	public String getStringParameter (HttpServletRequest request, String nombreParametro) throws FormularioIncorrectoRecibidoException {
		if (request.getParameter(nombreParametro) != null) {
			return request.getParameter(nombreParametro);
		}
		throw new FormularioIncorrectoRecibidoException("No se ha recibido valor para el parámetro " + nombreParametro);
	}

}
