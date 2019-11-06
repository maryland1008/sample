package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "ACCOUNT"
@XmlRootElement
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal accountId; // "ACCOUNT_ID" (NUMBER)	
    private Double availBalance; // "AVAIL_BALANCE" (FLOAT)	
    private Date closeDate; // "CLOSE_DATE" (DATE)	
    private Date lastActivityDate; // "LAST_ACTIVITY_DATE" (DATE)	
    private Date openDate; // "OPEN_DATE" (DATE)	
    private Double pendingBalance; // "PENDING_BALANCE" (FLOAT)	
    private String status; // "STATUS" (VARCHAR2)	
    private BigDecimal custId; // "CUST_ID" (NUMBER)	
    private BigDecimal openBranchId; // "OPEN_BRANCH_ID" (NUMBER)	
    private BigDecimal openEmpId; // "OPEN_EMP_ID" (NUMBER)	
    private String productCd; // "PRODUCT_CD" (VARCHAR2)	

    public void setAccountId( BigDecimal accountId ) {
        this.accountId = accountId ;
    }

    public BigDecimal getAccountId() {
        return this.accountId;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setAvailBalance( Double availBalance ) {
        this.availBalance = availBalance;
    }
	
    public Double getAvailBalance() {
        return this.availBalance;
    }

    public void setCloseDate( Date closeDate ) {
        this.closeDate = closeDate;
    }
	
    public Date getCloseDate() {
        return this.closeDate;
    }

    public void setLastActivityDate( Date lastActivityDate ) {
        this.lastActivityDate = lastActivityDate;
    }
	
    public Date getLastActivityDate() {
        return this.lastActivityDate;
    }

    public void setOpenDate( Date openDate ) {
        this.openDate = openDate;
    }
	
    public Date getOpenDate() {
        return this.openDate;
    }

    public void setPendingBalance( Double pendingBalance ) {
        this.pendingBalance = pendingBalance;
    }
	
    public Double getPendingBalance() {
        return this.pendingBalance;
    }

    public void setStatus( String status ) {
        this.status = status;
    }
	
    public String getStatus() {
        return this.status;
    }

    public void setCustId( BigDecimal custId ) {
        this.custId = custId;
    }
	
    public BigDecimal getCustId() {
        return this.custId;
    }

    public void setOpenBranchId( BigDecimal openBranchId ) {
        this.openBranchId = openBranchId;
    }
	
    public BigDecimal getOpenBranchId() {
        return this.openBranchId;
    }

    public void setOpenEmpId( BigDecimal openEmpId ) {
        this.openEmpId = openEmpId;
    }
	
    public BigDecimal getOpenEmpId() {
        return this.openEmpId;
    }

    public void setProductCd( String productCd ) {
        this.productCd = productCd;
    }
	
    public String getProductCd() {
        return this.productCd;
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

		Account other = (Account) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(accountId);
        sb.append("|");
        sb.append(availBalance);
        sb.append("|");
        sb.append(closeDate);
        sb.append("|");
        sb.append(lastActivityDate);
        sb.append("|");
        sb.append(openDate);
        sb.append("|");
        sb.append(pendingBalance);
        sb.append("|");
        sb.append(status);
        sb.append("|");
        sb.append(custId);
        sb.append("|");
        sb.append(openBranchId);
        sb.append("|");
        sb.append(openEmpId);
        sb.append("|");
        sb.append(productCd);
        return sb.toString(); 
    } 


}
