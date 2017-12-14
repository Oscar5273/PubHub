package examples.pubhub.dao;

import java.util.List;

import examples.pubhub.model.Book;
import examples.pubhub.model.Tag;


public interface TagDAO {
	public List<Tag> getAllTags();
	public List<Book> getBooksByTag(String tag);
	public List<Tag> getTagsByBook(String isbn_13);
	public boolean addTag(Tag tag);
	public boolean addTag(String tag, String isbn);
	public boolean deleteTag(String isbn,String tag);
}
