package examples.pubhub.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import examples.pubhub.dao.BookDAO;
import examples.pubhub.dao.TagDAO;
import examples.pubhub.model.Book;
import examples.pubhub.model.Tag;
import examples.pubhub.utilities.DAOUtilities;

/**
 * Servlet implementation class UpdateBookServlet
 * Changes made: Added functionality to allow user to update tag information
 */
@WebServlet("/UpdateBook")
public class UpdateBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isSuccess= false;
		String isbn13 = request.getParameter("isbn13");
		String x= request.getParameter("x");
		String newTag=request.getParameter("newTag");
		List<String> tags=new ArrayList<>();
		int r = Integer.parseInt(x);
		for(int i=0;i<r;i++) {
			if(request.getParameter( "tag"+i ) != null)
			tags.add(request.getParameter( "tag"+i));
		
		}
		System.out.println("The New Tag is"+newTag);
		BookDAO dao = DAOUtilities.getBookDAO();
		Book book = dao.getBookByISBN(isbn13);
		TagDAO tagdao = DAOUtilities.getTagDAO();
		
		if(book != null){
			// The only fields we want to be updatable are title, author and price. A new ISBN has to be applied for
			// And a new edition of a book needs to be re-published.
			book.setTitle(request.getParameter("title"));
			book.setAuthor(request.getParameter("author"));
			book.setPrice(Double.parseDouble(request.getParameter("price")));
			if(newTag!=null)
				tagdao.addTag(newTag, isbn13);
				
			request.setAttribute("book", book);
			for (String o : tags)
			tagdao.deleteTag(isbn13,o);
			isSuccess = dao.updateBook(book);
		}else {
			//ASSERT: couldn't find book with isbn. Update failed.
			isSuccess = false;
		}
		
		if(isSuccess){
			request.getSession().setAttribute("message", "Book successfully updated");
			request.getSession().setAttribute("messageClass", "alert-success");
			response.sendRedirect("ViewBookDetails?isbn13=" + isbn13);
		}else {
			request.getSession().setAttribute("message", "There was a problem updating this book");
			request.getSession().setAttribute("messageClass", "alert-danger");
			request.getRequestDispatcher("bookDetails.jsp").forward(request, response);
		}
	}

}
