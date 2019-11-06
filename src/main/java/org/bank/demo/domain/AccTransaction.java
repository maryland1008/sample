package org.bank.demo.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

// Table = "ACC_TRANSACTION"
@XmlRootElement
public class AccTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal txnId; // "TXN_ID" (NUMBER)	
    private Double amount; // "AMOUNT" (FLOAT)	
    private Date fundsAvailDate; // "FUNDS_AVAIL_DATE" (TIMESTAMP(6))	
    private Date txnDate; // "TXN_DATE" (TIMESTAMP(6))	
    private String txnTypeCd; // "TXN_TYPE_CD" (VARCHAR2)	
    private BigDecimal accountId; // "ACCOUNT_ID" (NUMBER)	
    private BigDecimal executionBranchId; // "EXECUTION_BRANCH_ID" (NUMBER)	
    private BigDecimal tellerEmpId; // "TELLER_EMP_ID" (NUMBER)	

    public void setTxnId( BigDecimal txnId ) {
        this.txnId = txnId ;
    }

    public BigDecimal getTxnId() {
        return this.txnId;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    public void setAmount( Double amount ) {
        this.amount = amount;
    }
	
    public Double getAmount() {
        return this.amount;
    }

    public void setFundsAvailDate( Date fundsAvailDate ) {
        this.fundsAvailDate = fundsAvailDate;
    }
	
    public Date getFundsAvailDate() {
        return this.fundsAvailDate;
    }

    public void setTxnDate( Date txnDate ) {
        this.txnDate = txnDate;
    }
	
    public Date getTxnDate() {
        return this.txnDate;
    }

    public void setTxnTypeCd( String txnTypeCd ) {
        this.txnTypeCd = txnTypeCd;
    }
	
    public String getTxnTypeCd() {
        return this.txnTypeCd;
    }

    public void setAccountId( BigDecimal accountId ) {
        this.accountId = accountId;
    }
	
    public BigDecimal getAccountId() {
        return this.accountId;
    }

    public void setExecutionBranchId( BigDecimal executionBranchId ) {
        this.executionBranchId = executionBranchId;
    }
	
    public BigDecimal getExecutionBranchId() {
        return this.executionBranchId;
    }

    public void setTellerEmpId( BigDecimal tellerEmpId ) {
        this.tellerEmpId = tellerEmpId;
    }
	
    public BigDecimal getTellerEmpId() {
        return this.tellerEmpId;
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

		AccTransaction other = (AccTransaction) obj;	
		
		return (this.hashCode()==other.hashCode());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append(txnId);
        sb.append("|");
        sb.append(amount);
        sb.append("|");
        sb.append(fundsAvailDate);
        sb.append("|");
        sb.append(txnDate);
        sb.append("|");
        sb.append(txnTypeCd);
        sb.append("|");
        sb.append(accountId);
        sb.append("|");
        sb.append(executionBranchId);
        sb.append("|");
        sb.append(tellerEmpId);
        return sb.toString(); 
    } 


}
