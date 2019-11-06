package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "INDIVIDUAL"
@XmlRootElement
public class Individual implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal custId; // "CUST_ID" (NUMBER)	
    private Date birthDate; // "BIRTH_DATE" (DATE)	
    private String firstName; // "FIRST_NAME" (VARCHAR2)	
    private String lastName; // "LAST_NAME" (VARCHAR2)	

    public void setCustId( BigDecimal custId ) {
        this.custId = custId ;
    }

    public BigDecimal getCustId() {
        return this.custId;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setBirthDate( Date birthDate ) {
        this.birthDate = birthDate;
    }
	
    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }
	
    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }
	
    public String getLastName() {
        return this.lastName;
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

		Individual other = (Individual) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(birthDate);
        sb.append("|");
        sb.append(firstName);
        sb.append("|");
        sb.append(lastName);
        sb.append("|");
        sb.append(custId);
        return sb.toString(); 
    } 


}
