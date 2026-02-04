package dev.madela.user.dto;

public class CreateUserRequest {
    private String name;
    private String email;

    public CreateUserRequest() {}

    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}
