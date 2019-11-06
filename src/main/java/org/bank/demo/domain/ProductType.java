package org.bank.demo.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

// Table = "PRODUCT_TYPE"
@XmlRootElement
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productTypeCd; // "PRODUCT_TYPE_CD" (VARCHAR2)	
    private String name; // "NAME" (VARCHAR2)	

    public void setProductTypeCd( String productTypeCd ) {
        this.productTypeCd = productTypeCd ;
    }

    public String getProductTypeCd() {
        return this.productTypeCd;
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

		ProductType other = (ProductType) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(productTypeCd);
        sb.append("|");
        sb.append(name);
        return sb.toString(); 
    } 


}
