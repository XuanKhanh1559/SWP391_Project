package config;

public enum UserRole {
    USER("user"),
    ADMIN("admin");
    
    private final String label;
    
    UserRole(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static UserRole fromLabel(String label) {
        for (UserRole role : UserRole.values()) {
            if (role.label.equalsIgnoreCase(label)) {
                return role;
            }
        }
        return USER;
    }
}


