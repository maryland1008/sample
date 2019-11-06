package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "OFFICER"
@XmlRootElement
public class Officer implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal officerId; // "OFFICER_ID" (NUMBER)	
    private Date endDate; // "END_DATE" (DATE)	
    private String firstName; // "FIRST_NAME" (VARCHAR2)	
    private String lastName; // "LAST_NAME" (VARCHAR2)	
    private Date startDate; // "START_DATE" (DATE)	
    private String title; // "TITLE" (VARCHAR2)	
    private BigDecimal custId; // "CUST_ID" (NUMBER)	

    public void setOfficerId( BigDecimal officerId ) {
        this.officerId = officerId ;
    }

    public BigDecimal getOfficerId() {
        return this.officerId;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }
	
    public Date getEndDate() {
        return this.endDate;
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

    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }
	
    public Date getStartDate() {
        return this.startDate;
    }

    public void setTitle( String title ) {
        this.title = title;
    }
	
    public String getTitle() {
        return this.title;
    }

    public void setCustId( BigDecimal custId ) {
        this.custId = custId;
    }
	
    public BigDecimal getCustId() {
        return this.custId;
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

		Officer other = (Officer) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(officerId);
        sb.append("|");
        sb.append(endDate);
        sb.append("|");
        sb.append(firstName);
        sb.append("|");
        sb.append(lastName);
        sb.append("|");
        sb.append(startDate);
        sb.append("|");
        sb.append(title);
        sb.append("|");
        sb.append(custId);
        return sb.toString(); 
    } 


}
