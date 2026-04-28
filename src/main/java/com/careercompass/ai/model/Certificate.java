package com.careercompass.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "certificate_title", nullable = false)
    private String certificateTitle;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "certificate_id", unique = true)
    private String certificateId;

    @Column(name = "verification_statement", columnDefinition = "TEXT")
    private String verificationStatement;

    @PrePersist
    protected void onIssue() {
        this.issuedAt = LocalDateTime.now();
        this.certificateId = "CC-" + System.currentTimeMillis();
    }

    public Certificate() {}
    public Certificate(Long id, User user, Course course, String certificateTitle, String studentName, LocalDateTime issuedAt, String certificateId, String verificationStatement) {
        this.id = id; this.user = user; this.course = course; this.certificateTitle = certificateTitle;
        this.studentName = studentName; this.issuedAt = issuedAt; this.certificateId = certificateId;
        this.verificationStatement = verificationStatement;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public String getCertificateTitle() { return certificateTitle; }
    public void setCertificateTitle(String certificateTitle) { this.certificateTitle = certificateTitle; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public String getCertificateId() { return certificateId; }
    public String getVerificationStatement() { return verificationStatement; }
    public void setVerificationStatement(String verificationStatement) { this.verificationStatement = verificationStatement; }

    public static CertificateBuilder builder() { return new CertificateBuilder(); }
    public static class CertificateBuilder {
        private User user; private Course course; private String certificateTitle;
        private String studentName; private String verificationStatement;
        public CertificateBuilder user(User u) { this.user = u; return this; }
        public CertificateBuilder course(Course c) { this.course = c; return this; }
        public CertificateBuilder certificateTitle(String t) { this.certificateTitle = t; return this; }
        public CertificateBuilder studentName(String n) { this.studentName = n; return this; }
        public CertificateBuilder verificationStatement(String v) { this.verificationStatement = v; return this; }
        public Certificate build() {
            Certificate c = new Certificate();
            c.user = user; c.course = course; c.certificateTitle = certificateTitle;
            c.studentName = studentName; c.verificationStatement = verificationStatement;
            return c;
        }
    }
}
