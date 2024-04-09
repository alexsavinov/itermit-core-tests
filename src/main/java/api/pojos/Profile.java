package api.pojos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {

    private Long id;
    private String name;
    private String surname;
    private String gender;
    private String city;
    private String address;
    private String company;
    private String mobile;
    private String tele;
    private String website;
    private String date;
    private String avatar;

}
