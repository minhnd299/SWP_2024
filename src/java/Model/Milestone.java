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

public class Milestone {
    private int milestoneId;
    private String milestoneName;
    private Date startDate;
    private Date endDate;

    // Constructor
    public Milestone() {}

    public Milestone(int milestoneId, String milestoneName, Date startDate, Date endDate) {
        this.milestoneId = milestoneId;
        this.milestoneName = milestoneName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
