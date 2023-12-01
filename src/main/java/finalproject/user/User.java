package finalproject.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import finalproject.user.enums.BloodType;
import finalproject.user.enums.Sex;
import finalproject.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder(builderClassName = "UserBuilder")
@JsonIgnoreProperties({"createdAt", "password", "authorities", "bills", "enabled", "credentialsNonExpired", "accountNonExpired", "accountNonLocked"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private LocalDate birthday;
    private String address;
    private BloodType bloodtype;
    private String avatarUrl;
    private double height;
    private double weight;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.name + " " + this.surname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public static class UserBuilder {
        Faker faker = new Faker(Locale.ITALY);
        private String name = this.faker.name().firstName();
        private String surname = this.faker.name().lastName();
        private String email = name + "." + surname + "@gmail.com";
        private String avatarUrl = "https://ui-avatars.com/api/?name=" + name + "+" + surname;
        private String password = this.faker.phoneNumber().cellPhone();
        private String username = this.faker.funnyName().name();
        private String phone = this.faker.phoneNumber().cellPhone();
        private double height = Double.parseDouble(new DecimalFormat("0.0").format(new Random().nextDouble(1.50, 2.10)).replaceAll(",", "."));
        private double weight = Double.parseDouble(new DecimalFormat("0.0").format(new Random().nextDouble(57, 110)).replaceAll(",", "."));
        private LocalDate birthday = faker.date().birthday(18, 67).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        private String address = faker.address().streetAddress();
        private UserRole role = UserRole.USER;
    }
}
