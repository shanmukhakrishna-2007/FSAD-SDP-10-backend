package com.careercompass.ai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "career_paths")
public class CareerPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String icon;

    @Column(name = "avg_salary")
    private String avgSalary;

    @Column(name = "demand_level")
    private String demandLevel;

    public CareerPath() {}
    public CareerPath(Long id, String name, String description, String icon, String avgSalary, String demandLevel) {
        this.id = id; this.name = name; this.description = description;
        this.icon = icon; this.avgSalary = avgSalary; this.demandLevel = demandLevel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getAvgSalary() { return avgSalary; }
    public void setAvgSalary(String avgSalary) { this.avgSalary = avgSalary; }
    public String getDemandLevel() { return demandLevel; }
    public void setDemandLevel(String demandLevel) { this.demandLevel = demandLevel; }

    public static CareerPathBuilder builder() { return new CareerPathBuilder(); }
    public static class CareerPathBuilder {
        private Long id; private String name; private String description; private String icon;
        private String avgSalary; private String demandLevel;
        public CareerPathBuilder id(Long id) { this.id = id; return this; }
        public CareerPathBuilder name(String n) { this.name = n; return this; }
        public CareerPathBuilder description(String d) { this.description = d; return this; }
        public CareerPathBuilder icon(String i) { this.icon = i; return this; }
        public CareerPathBuilder avgSalary(String a) { this.avgSalary = a; return this; }
        public CareerPathBuilder demandLevel(String d) { this.demandLevel = d; return this; }
        public CareerPath build() { return new CareerPath(id, name, description, icon, avgSalary, demandLevel); }
    }
}
