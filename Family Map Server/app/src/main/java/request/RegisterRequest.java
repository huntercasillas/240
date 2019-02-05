package request;

/**
 * Class that contains all information for a register request.
 */
public class RegisterRequest {

    private String userName, password, email, firstName, lastName, gender;

    public RegisterRequest (String userName, String password, String email, String firstName, String lastName, String gender) {

        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}