/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private int task_id;
    private String title;
    private String description;
    private String status;
    private Objective objecttive;
    private Date start_date;
    private Date end_date;

    public Task(String title, String description, String status, Objective objecttive) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.objecttive = objecttive;
    }
    
    
}
