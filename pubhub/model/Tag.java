package examples.pubhub.model;

import java.time.LocalDate;
import java.util.List;

import examples.pubhub.dao.TagDAO;
import examples.pubhub.utilities.DAOUtilities;
public class Tag {
	private String tag;
	
	public Tag() {
		this.tag=null;
	}
	public Tag(String name) {
		this.tag=name;
	}

	public String getTag() {
		return tag;
	}
	public void setTag(String name) {
		this.tag=name;
	}
	
}
