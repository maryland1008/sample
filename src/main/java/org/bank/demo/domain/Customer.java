package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "CUSTOMER"
@XmlRootElement
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal custId; // "CUST_ID" (NUMBER)	
    private String address; // "ADDRESS" (VARCHAR2)	
    private String city; // "CITY" (VARCHAR2)	
    private String custTypeCd; // "CUST_TYPE_CD" (VARCHAR2)	
    private String fedId; // "FED_ID" (VARCHAR2)	
    private String postalCode; // "POSTAL_CODE" (VARCHAR2)	
    private String state; // "STATE" (VARCHAR2)	

    public void setCustId( BigDecimal custId ) {
        this.custId = custId ;
    }

    public BigDecimal getCustId() {
        return this.custId;
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

    public void setCustTypeCd( String custTypeCd ) {
        this.custTypeCd = custTypeCd;
    }
	
    public String getCustTypeCd() {
        return this.custTypeCd;
    }

    public void setFedId( String fedId ) {
        this.fedId = fedId;
    }
	
    public String getFedId() {
        return this.fedId;
    }

    public void setPostalCode( String postalCode ) {
        this.postalCode = postalCode;
    }
	
    public String getPostalCode() {
        return this.postalCode;
    }

    public void setState( String state ) {
        this.state = state;
    }
	
    public String getState() {
        return this.state;
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

		Customer other = (Customer) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(custId);
        sb.append("|");
        sb.append(address);
        sb.append("|");
        sb.append(city);
        sb.append("|");
        sb.append(custTypeCd);
        sb.append("|");
        sb.append(fedId);
        sb.append("|");
        sb.append(postalCode);
        sb.append("|");
        sb.append(state);
        return sb.toString(); 
    } 


}
