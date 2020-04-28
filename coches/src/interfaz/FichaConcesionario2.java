package interfaz;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Concesionario;
import modelo.controladores.ConcesionarioControlador;
import utils.RequestUtils;
import utils.SuperTipoServlet;

/**
 * Servlet implementation class ListadoConcesionario
 */
@WebServlet(description = "Primer Servlet", urlPatterns = { "/FichaConcesionarios" })
public class FichaConcesionario2 extends SuperTipoServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FichaConcesionario2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Obtengo una HashMap con todos los parámetros del request, sea este del tipo que sea;
		HashMap<String, Object> hashMap = RequestUtils.requestToHashMap(request);
		
		// Para plasmar la información de un profesor determinado utilizaremos un parámetro, que debe llegar a este Servlet obligatoriamente
		// El parámetro se llama "idProfesor" y gracias a él podremos obtener la información del profesor y mostrar sus datos en pantalla
		Concesionario concesionario = null;
		// Obtengo el profesor a editar, en el caso de que el profesor exista se cargarán sus datos, en el caso de que no exista quedará a null
		try {
			int idConcesionario = RequestUtils.getIntParameterFromHashMap(hashMap, "idConcesionario"); // Necesito obtener el id del profesor que se quiere editar. En caso de un alta
			// de profesor obtendríamos el valor 0 como idProfesor
			if (idConcesionario != 0) {
				concesionario = (Concesionario)ConcesionarioControlador.getControlador().find(idConcesionario);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Inicializo unos valores correctos para la presentación del profesor
		if (concesionario == null) {
			concesionario = new Concesionario();
		}
		if (concesionario.getNombre() == null) concesionario.setNombre("");
		if (concesionario.getCif()== null) concesionario.setCif("");
		if (concesionario.getLocalidad() == null) concesionario.setLocalidad("");
		
		
		
		// Ahora debo determinar cuál es la acción que este página debería llevar a cabo, en función de los parámetros de entrada al Servlet.
		// Las acciones que se pueden querer llevar a cabo son tres:
		//    - "eliminar". Sé que está es la acción porque recibiré un un parámetro con el nombre "eliminar" en el request
		//    - "guardar". Sé que está es la acción elegida porque recibiré un parámetro en el request con el nombre "guardar"
		//    - Sin acción. En este caso simplemente se quiere editar la ficha
		
		// Variable con mensaje de información al usuario sobre alguna acción requerida
		String mensajeAlUsuario = "";
		
		// Primera acción posible: eliminar
		if (RequestUtils.getStringParameterFromHashMap(hashMap, "eliminar") != null) {
			// Intento eliminar el registro, si el borrado es correcto redirijo la petición hacia el listado de profesores
			try {
				ConcesionarioControlador.getControlador().remove(concesionario);
				response.sendRedirect(request.getContextPath() + "/ListadoConcesionario"); // Redirección del response hacia el listado
			}
			catch (Exception ex) {
				mensajeAlUsuario = "Imposible eliminar. Es posible que existan restricciones.";
			}
		}
		
		// Segunda acción posible: guardar
		if (RequestUtils.getStringParameterFromHashMap(hashMap, "guardar") != null) {
			// Obtengo todos los datos del profesor y los almaceno en BBDD
			try {
				concesionario.setNombre(RequestUtils.getStringParameterFromHashMap(hashMap, "nombre"));
				concesionario.setCif(RequestUtils.getStringParameterFromHashMap(hashMap, "cfi"));
				concesionario.setLocalidad(RequestUtils.getStringParameterFromHashMap(hashMap, "localidad"));
				byte[] posibleImagen = RequestUtils.getByteArrayFromHashMap(hashMap, "ficheroImagen");
				if (posibleImagen != null && posibleImagen.length > 0) {
					concesionario.setImagen(posibleImagen);
				}
				
				// Finalmente guardo el objeto de tipo profesor 
				ConcesionarioControlador.getControlador().save(concesionario);
				mensajeAlUsuario = "Guardado correctamente";
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		
		//html
		
		response.getWriter().append(this.getCabeceraHTML("ficha concesionario"));
		response.getWriter().append("\r\n" + 
				"<body " +((mensajeAlUsuario != null && mensajeAlUsuario != "")? "onLoad=\"alert('" + mensajeAlUsuario + "');\"" : "")  + " >\r\n" + 
				"<h1>Ficha de concesionario</h1>\r\n" + 
				"<a href=\"ListadoConcesionario\">Ir al listado de concesionarios</a>" +
				"<form id=\"form1\" name=\"form1\" method=\"post\" action=\"FichaConcesionarios\" enctype=\"multipart/form-data\"  onsubmit=\"return valida()\">\r\n" + 
				" <img src=\"utils/DownloadImagenConcesionario?idConcesionario=" + concesionario.getId() + "\" width='100px' height='100px'/>" +
				" <input type=\"hidden\" name=\"idConcesionario\" value=\"" + concesionario.getId() + "\"\\>" +
				"  <p>\r\n" + 
				"    <label for=\"ficheroImagen\">Imagen:</label>\r\n" + 
				"    <input name=\"ficheroImagen\" type=\"file\" id=\"ficheroImagen\" />\r\n" + 
				"  </p>\r\n" + 
				"  <p>\r\n" + 
				"    <label for=\"nombre\">Nombre:</label>\r\n" + 
				"    <input name=\"nombre\" type=\"text\" id=\"nombre\"  value=\"" + concesionario.getNombre() + "\" />\r\n" + 
				"  </p>\r\n" + 
				"  <p>\r\n" + 
				"    <label for=\"cfi\">CFI:</label>\r\n" + 
				"    <input name=\"cfi\" type=\"text\" id=\"cfi\" value='" + concesionario.getCif() + "' />\r\n" + 
				"  </p>\r\n" + 
				"  <p>\r\n" + 
				"    <label for=\"localidad\">localidad:</label>\r\n" + 
				"    <input name=\"localidad\" type=\"text\" id=\"localidad\" value='" + concesionario.getLocalidad() + "' />\r\n" + 
				"  </p>\r\n" + 
				"  <p>\r\n" + 
				"    <input type=\"submit\" name=\"guardar\" value=\"Guardar\" onclick=\"valida()\" />\r\n" + 
				"    <input type=\"submit\" name=\"eliminar\" value=\"Eliminar\" />\r\n" + 
				"  </p>\r\n" + 
				"</form>"+ "<script>\r\n" + 
				"    function valida() {\r\n" + 
				"    var ok = true;\r\n" + 
				"    var msg = \"Debes escribir algo en todos campos\\n\";\r\n" + 
				"    var elementos = document.getElementById(\"form1\").elements;\r\n" + 
				"    for (var i = 0; i < elementos.length; i++) {\r\n" + 
				"        if(elementos[i].value == \"\" && elementos[i]!=document.getElementById(\"ficheroImagen\")){\r\n" + 
				"            alert(msg);\r\n" + 
				"            return ok = false;\r\n" + 
				"         }\r\n" + 
				"    }\r\n" + 
				"    \r\n" + 
				"    \r\n" + 
				"  }\r\n" + 
				"</script>");
				response.getWriter().append(this.getPieHTML());
		
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
