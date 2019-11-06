package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "BUSINESS"
@XmlRootElement
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal custId; // "CUST_ID" (NUMBER)	
    private Date incorpDate; // "INCORP_DATE" (DATE)	
    private String name; // "NAME" (VARCHAR2)	
    private String stateId; // "STATE_ID" (VARCHAR2)	

    public void setCustId( BigDecimal custId ) {
        this.custId = custId ;
    }

    public BigDecimal getCustId() {
        return this.custId;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setIncorpDate( Date incorpDate ) {
        this.incorpDate = incorpDate;
    }
	
    public Date getIncorpDate() {
        return this.incorpDate;
    }

    public void setName( String name ) {
        this.name = name;
    }
	
    public String getName() {
        return this.name;
    }

    public void setStateId( String stateId ) {
        this.stateId = stateId;
    }
	
    public String getStateId() {
        return this.stateId;
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

		Business other = (Business) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(incorpDate);
        sb.append("|");
        sb.append(name);
        sb.append("|");
        sb.append(stateId);
        sb.append("|");
        sb.append(custId);
        return sb.toString(); 
    } 


}
