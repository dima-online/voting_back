package kz.bsbnb.common.bean;



/**
 * Created by ruslan on 19/10/2016.
 */
public class RegUserBean {

    private Long id;
    private String login;
    private String role;
    private String iin;
    private String email;
    private String phone;
    private String fullName;
    private Boolean isOrg;
    private String lastName;
    private String firstName;
    private String middleName;
    private String password;
    private String oldPassword;
    private Integer shareCount;
    private String voterIin;
    private String executiveOfficer;
    private String executiveOfficerName;
    private Boolean smsNotification;
    private Boolean emailNotification;
    private String dateOfBirth;             //"dd-MM-yyyy"
    private String documentNumber;
    private String documentGivenAgency;
    private String documentGivenDate;       //"dd-MM-yyyy"
    private String documentExpireDate;      //"dd-MM-yyyy"
    private String documentType;

    public RegUserBean() {
    }

    public Boolean getSmsNotification() {
        return smsNotification;
    }

    public void setSmsNotification(Boolean smsNotification) {
        this.smsNotification = smsNotification;
    }

    public Boolean getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public String getVoterIin() {
        return voterIin;
    }

    public void setVoterIin(String voterIin) {
        this.voterIin = voterIin;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getOrg() {
        return isOrg;
    }

    public void setOrg(Boolean org) {
        isOrg = org;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getExecutiveOfficer() {
        return executiveOfficer;
    }

    public void setExecutiveOfficer(String executiveOfficer) {
        this.executiveOfficer = executiveOfficer;
    }

    public String getExecutiveOfficerName() {
        return executiveOfficerName;
    }

    public void setExecutiveOfficerName(String executiveOfficerName) {
        this.executiveOfficerName = executiveOfficerName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentGivenAgency() {
        return documentGivenAgency;
    }

    public void setDocumentGivenAgency(String documentGivenAgency) {
        this.documentGivenAgency = documentGivenAgency;
    }

    public String getDocumentGivenDate() {
        return documentGivenDate;
    }

    public void setDocumentGivenDate(String documentGivenDate) {
        this.documentGivenDate = documentGivenDate;
    }

    public String getDocumentExpireDate() {
        return documentExpireDate;
    }

    public void setDocumentExpireDate(String documentExpireDate) {
        this.documentExpireDate = documentExpireDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
