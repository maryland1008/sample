package org.bank.demo.domain;

import java.io.Serializable;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "PRODUCT"
@XmlRootElement
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productCd; // "PRODUCT_CD" (VARCHAR2)	
    private Date dateOffered; // "DATE_OFFERED" (DATE)	
    private Date dateRetired; // "DATE_RETIRED" (DATE)	
    private String name; // "NAME" (VARCHAR2)	
    private String productTypeCd; // "PRODUCT_TYPE_CD" (VARCHAR2)	

    public void setProductCd( String productCd ) {
        this.productCd = productCd ;
    }

    public String getProductCd() {
        return this.productCd;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setDateOffered( Date dateOffered ) {
        this.dateOffered = dateOffered;
    }
	
    public Date getDateOffered() {
        return this.dateOffered;
    }

    public void setDateRetired( Date dateRetired ) {
        this.dateRetired = dateRetired;
    }
	
    public Date getDateRetired() {
        return this.dateRetired;
    }

    public void setName( String name ) {
        this.name = name;
    }
	
    public String getName() {
        return this.name;
    }

    public void setProductTypeCd( String productTypeCd ) {
        this.productTypeCd = productTypeCd;
    }
	
    public String getProductTypeCd() {
        return this.productTypeCd;
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

		Product other = (Product) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(productCd);
        sb.append("|");
        sb.append(dateOffered);
        sb.append("|");
        sb.append(dateRetired);
        sb.append("|");
        sb.append(name);
        sb.append("|");
        sb.append(productTypeCd);
        return sb.toString(); 
    } 


}
