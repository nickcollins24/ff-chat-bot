package ncollins.model.espn;

import java.util.Objects;

public class Member {
    private String displayName;
    private String firstName;
    private String id;
    private String lastName;

    // GET
    public String getDisplayName() { return displayName; }
    public String getFirtName() { return firstName; }
    public String getId() { return id; }
    public String getLastName() { return lastName; }

    // SET
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setFirtName(String firtName) { this.firstName = firtName; }
    public void setId(String id) { this.id = id; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Member)) return false;

        Member m = (Member) o;
        return m.getId().equals(id) ||
                m.getDisplayName().equals(displayName) ||
                (m.firstName + m.lastName).equals(firstName + lastName);
    }

    @Override
    public String toString(){
        return this.getId() + ", " + this.getDisplayName() + ", " + this.getFirtName() + " " + this.getLastName();
    }

    /**
     * Note: this currently breaks hashmap implementation.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
