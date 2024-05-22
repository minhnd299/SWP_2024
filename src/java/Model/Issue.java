/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author admin
 */
import java.util.Date;

public class Issue {
    private int issueId;
    private int milestoneId;
    private String title;
    private String content;
    private String status;
    private Date date;
    private int accountId;

    // Constructor
    public Issue() {}

    public Issue(int issueId, int milestoneId, String title, String content, String status, Date date, int accountId) {
        this.issueId = issueId;
        this.milestoneId = milestoneId;
        this.title = title;
        this.content = content;
        this.status = status;
        this.date = date;
        this.accountId = accountId;
    }

    // Getters and Setters

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
