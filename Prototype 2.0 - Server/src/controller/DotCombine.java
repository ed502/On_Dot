package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DotVO;

/**
 * Servlet implementation class DotCombine
 */
@WebServlet("/DotCombine")
public class DotCombine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DotCombine() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String dotCombine = CombineData.data;
		
		if(dotCombine != null) {
			StringBuffer dotCombineXML = new StringBuffer(2048);
			dotCombineXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			dotCombineXML.append("<Dots>");
			dotCombineXML.append("<entry>");
			dotCombineXML.append("<dot>");
			dotCombineXML.append(dotCombine);
			dotCombineXML.append("</dot>");
			dotCombineXML.append("</entry>");
	           
			dotCombineXML.append("</Dots>");
	        System.out.println(dotCombineXML.toString());
		}
		
		
        
      
        
        // ¿¿¥‰
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/xml; charset=utf-8");
        response.getWriter().println(dotCombine.toString());
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
