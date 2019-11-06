package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "BRANCH"
@XmlRootElement
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal branchId; // "BRANCH_ID" (NUMBER)	
    private String address; // "ADDRESS" (VARCHAR2)	
    private String city; // "CITY" (VARCHAR2)	
    private String name; // "NAME" (VARCHAR2)	
    private String state; // "STATE" (VARCHAR2)	
    private String zipCode; // "ZIP_CODE" (VARCHAR2)	

    public void setBranchId( BigDecimal branchId ) {
        this.branchId = branchId ;
    }

    public BigDecimal getBranchId() {
        return this.branchId;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setAddress( String address ) {
        this.address = address;
    }
	
    public String getAddress() {
        return this.address;
    }

    public void setCity( String city ) {
        this.city = city;
    }
	
    public String getCity() {
        return this.city;
    }

    public void setName( String name ) {
        this.name = name;
    }
	
    public String getName() {
        return this.name;
    }

    public void setState( String state ) {
        this.state = state;
    }
	
    public String getState() {
        return this.state;
    }

    public void setZipCode( String zipCode ) {
        this.zipCode = zipCode;
    }
	
    public String getZipCode() {
        return this.zipCode;
    }


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} 
		if (obj == null) {
			return false;
		} 
		if (getClass() != obj.getClass()) {
			return false;
		}

		Branch other = (Branch) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(branchId);
        sb.append("|");
        sb.append(address);
        sb.append("|");
        sb.append(city);
        sb.append("|");
        sb.append(name);
        sb.append("|");
        sb.append(state);
        sb.append("|");
        sb.append(zipCode);
        return sb.toString(); 
    } 


}
