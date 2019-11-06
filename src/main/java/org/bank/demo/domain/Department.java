package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "DEPARTMENT"
@XmlRootElement
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal deptId; // "DEPT_ID" (NUMBER)	
    private String name; // "NAME" (VARCHAR2)	

    public void setDeptId( BigDecimal deptId ) {
        this.deptId = deptId ;
    }

    public BigDecimal getDeptId() {
        return this.deptId;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setName( String name ) {
        this.name = name;
    }
	
    public String getName() {
        return this.name;
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

		Department other = (Department) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(deptId);
        sb.append("|");
        sb.append(name);
        return sb.toString(); 
    } 


}
