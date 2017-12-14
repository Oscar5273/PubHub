package examples.pubhub.dao;

/*
 This file was to just to test different functionality  on the backend
 */

import java.util.ArrayList;
import java.util.List;

import examples.pubhub.model.Book;
import examples.pubhub.model.Tag;
import examples.pubhub.utilities.DAOUtilities;

public class TagTest {

	public static void main(String[] args) {
		BookDAO bookdao = DAOUtilities.getBookDAO();
		Book books =bookdao.getBookByISBN("1111111111111");
		System.out.println(books.getTag());
		
		TagDAO dao = DAOUtilities.getTagDAO();
		List<Tag> tags = new ArrayList<>();
		dao.addTag("Science Fiction", "1111111111111");
		dao.addTag("Fiction", "1111111111111");
		tags = dao.getTagsByBook("1111111111111");


	
		for (Tag o : tags)
		   System.out.println(o.getTag());
		

	}



	}



