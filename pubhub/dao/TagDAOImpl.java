package examples.pubhub.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import examples.pubhub.model.Book;
import examples.pubhub.utilities.DAOUtilities;

import examples.pubhub.model.Book;
import examples.pubhub.model.Tag;
import examples.pubhub.utilities.DAOUtilities;

public class TagDAOImpl implements TagDAO{

	Connection connection = null;
	PreparedStatement stmt = null;
	
	@Override
	public List<Tag> getAllTags() {
		
		List<Tag> tags = new ArrayList<>();

		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			String sql = "SELECT * FROM public.\"TAGS\"";			// Our SQL query
			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			ResultSet rs = stmt.executeQuery();			// Queries the database

			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				// We need to populate a Book object with info for each row from our query result
				Tag tag = new Tag();

				// Each variable in our Book object maps to a column in a row from our results.
				tag.setTag(rs.getString("tags_name"));


				// Finally we add it to the list of Book objects returned by this query.
				tags.add(tag);
				
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		
		// return the list of Book objects populated by the DB.
		return tags;

	}

	@Override
	public List<Book> getBooksByTag(String tag) {
		List<Book> books = new ArrayList<>();
		List<String> isbnlist=new ArrayList<>();
		

		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			String sql = "SELECT * FROM public.\"BOOKS_TAGS\" where tags_name='"+tag+"'";			// Our SQL query
			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			ResultSet rs = stmt.executeQuery();			// Queries the database

			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				Book book=new Book();
				isbnlist.add(rs.getString("isbn_13"));
				BookDAO dao = DAOUtilities.getBookDAO();
				book = dao.getBookByISBN(rs.getString("isbn_13"));
				books.add(book);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			System.out.println("There are no books in wih that tag");
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		
		// return the list of Book objects populated by the DB.
		return books;
	}

	@Override
	public boolean addTag(Tag tag) {
		try {
			connection = DAOUtilities.getConnection();
			String sql = "INSERT INTO public.\"TAGS\" VALUES (?)";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, tag.getTag());
			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			System.out.println("Tag already exists2");
			//e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}
	
	@Override
	public boolean addTag(String tag, String isbn) {
		Tag newTag=new Tag(tag);
		addTag(newTag);
		try {
			connection = DAOUtilities.getConnection();
			String sql = "INSERT INTO public.\"BOOKS_TAGS\"(\r\n" + 
					"	isbn_13, tags_name)\r\n" + 
					"	VALUES ('"+isbn+"', '"+tag+"');";
			stmt = connection.prepareStatement(sql);

			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			System.out.println("Tag and ISBN pair already exists");
			//e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}

	@Override
	public boolean deleteTag(String isbn,String tag) {
		stmt = null;
		try {
			connection = DAOUtilities.getConnection();
			String sql = "Delete FROM public.\"BOOKS_TAGS\" where tags_name='"+tag+"' and isbn_13='"+isbn+"'";
			stmt = connection.prepareStatement(sql);
			stmt.executeUpdate();
			System.out.println(stmt);
			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}
	
	private void closeResources() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			System.out.println("Could not close statement!");
			e.printStackTrace();
		}
		
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close connection!");
			e.printStackTrace();
		}
	}

	@Override
	public List<Tag> getTagsByBook(String isbn_13) {
		List<Tag> tags=new ArrayList<>();
		

		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			String sql = "SELECT * FROM public.\"BOOKS_TAGS\" where isbn_13='"+isbn_13+"' ";			// Our SQL query
			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			ResultSet rs = stmt.executeQuery();			// Queries the database
	
			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				
				// We need to populate a Book object with info for each row from our query result
				Tag tag = new Tag();

				// Each variable in our Book object maps to a column in a row from our results.
				tag.setTag(rs.getString("tags_name"));


				// Finally we add it to the list of Book objects returned by this query.
				tags.add(tag);
				
			}
			if(tags.isEmpty()) {
				Tag tag = new Tag();
				tag.setTag("N/A");
				// Finally we add it to the list of Book objects returned by this query.
				tags.add(tag);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		
		// return the list of Book objects populated by the DB.
		return tags;

	}




}
