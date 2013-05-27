
public class DynPersonBean {
	
	String FirstName;
	String LastName;
	String Mail;
	String Mobile;
	String HomeAddress;
	String HomeCity;
	String Company;
	String JobTitle;
	String Categories;
	
	
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getMail() {
		return Mail;
	}
	public void setMail(String mail) {
		Mail = mail;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getHomeAddress() {
		return HomeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		HomeAddress = homeAddress;
	}
	public String getHomeCity() {
		return HomeCity;
	}
	public void setHomeCity(String homeCity) {
		HomeCity = homeCity;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	public String getJobTitle() {
		return JobTitle;
	}
	public void setJobTitle(String jobTitle) {
		JobTitle = jobTitle;
	}
	public String getCategories() {
		return Categories;
	}
	public void setCategories(String categories) {
		Categories = categories;
	}
	@Override
	public String toString() {
		return "DynPersonBean [FirstName=" + FirstName + ", LastName="
				+ LastName + ", Mail=" + Mail + ", Mobile=" + Mobile
				+ ", HomeAddress=" + HomeAddress + ", HomeCity=" + HomeCity
				+ ", Company=" + Company + ", JobTitle=" + JobTitle
				+ ", Categories=" + Categories + ", getClass()=" + getClass()
				+ "]";
	}
	
	
}
