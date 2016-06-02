package at.ac.tuwien.big.we16.ue4.error;

public class FormError {

    private Boolean titleError;
    private Boolean firstNameError;
    private Boolean lastNameError;
    private Boolean mailFormatError;
    private Boolean mailError;
    private Boolean dateFormatError;
    private Boolean dateError;
    private Boolean passwordError;

    public FormError() {
        this.titleError = false;
        this.firstNameError = false;
        this.lastNameError = false;
        this.mailFormatError = false;
        this.mailError = false;
        this.dateFormatError = false;
        this.dateError = false;
        this.passwordError = false;
    }

    public boolean isAnyError() {
        return  titleError || firstNameError || lastNameError || mailError || mailFormatError || dateError || dateFormatError || passwordError;
    }



    public Boolean getTitleError() {
        return titleError;
    }

    public void setTitleError(Boolean titleError) {
        this.titleError = titleError;
    }

    public Boolean getFirstNameError() {
        return firstNameError;
    }

    public void setFirstNameError(Boolean firstNameError) {
        this.firstNameError = firstNameError;
    }

    public Boolean getLastNameError() {
        return lastNameError;
    }

    public void setLastNameError(Boolean lastNameError) {
        this.lastNameError = lastNameError;
    }

    public Boolean getMailFormatError() {
        return mailFormatError;
    }

    public void setMailFormatError(Boolean mailFormatError) {
        this.mailFormatError = mailFormatError;
    }

    public Boolean getMailError() {
        return mailError;
    }

    public void setMailError(Boolean mailError) {
        this.mailError = mailError;
    }

    public Boolean getDateFormatError() {
        return dateFormatError;
    }

    public void setDateFormatError(Boolean dateFormatError) {
        this.dateFormatError = dateFormatError;
    }

    public Boolean getDateError() {
        return dateError;
    }

    public void setDateError(Boolean dateError) {
        this.dateError = dateError;
    }

    public Boolean getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(Boolean passwordError) {
        this.passwordError = passwordError;
    }
}
