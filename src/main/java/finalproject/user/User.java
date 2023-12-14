package finalproject.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import finalproject.confirmationToken.ConfirmationToken;
import finalproject.donation.Donation;
import finalproject.user.enums.BloodType;
import finalproject.user.enums.Region;
import finalproject.user.enums.Sex;
import finalproject.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder(builderClassName = "UserBuilder")
@JsonIgnoreProperties({"password", "donations", "tokens", "authorities", "enable", "credentialsNonExpired", "accountNonExpired", "accountNonLocked"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private LocalDate birthday;
    private String address;
    @Enumerated(EnumType.STRING)
    private Region region;
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type")
    private BloodType bloodtype;
    @Column(name = "avatar_url")
    private String avatarUrl;
    private double height;
    private double weight;
    private Boolean enable = false;
    private Boolean locked = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Donation> donations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ConfirmationToken> tokens = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
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
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    public Sex getSex() {
        return Objects.requireNonNullElse(this.sex, Sex.NONE);
    }

    public BloodType getBloodtype() {
        return Objects.requireNonNullElse(this.bloodtype, BloodType.NONE);

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
