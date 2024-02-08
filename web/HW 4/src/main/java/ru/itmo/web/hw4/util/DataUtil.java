package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.User;
import ru.itmo.web.hw4.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", User.UserColors.BLUE),
            new User(6, "pashka", "Pavel Mavrin", User.UserColors.GREEN),
            new User(9, "geranazavr555", "Georgiy Nazarov", User.UserColors.RED),
            new User(11, "tourist", "Gennady Korotkevich", User.UserColors.GREEN)
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(
                    1,
                    "Romeo and Juliet by William Shakespeare",
                    "Romeo and Juliet is a tragedy written by William Shakespeare between 1591-1595, and it remains one of his most popular and frequently performed plays. The romance between Romeo and Juliet has become the foundation for many derivative romantic works and established the title characters as the best known of any young lovers in literature. Shakespeare borrowed from other famous tales written earlier in the sixteenth century but expanded upon the plot and characters to create his own version of the famous story. The play is set in Verona, Italy, and begins with a scuffle between members the rival families Montague and Capulet. The two families are sworn enemies. The beginning scuffle is between servants of those two houses, and shows that the ill will and animosity of the families runs through the families from the primary members of the family down to their servants. Capulet, the head of the house of Capulet, has begun receiving interested suitors for his young daughter Juliet. Though her father asks Count Paris, a kinsman of Prince Escalus of Verona, to wait two years for their betrothal, he invites him to attend a Capulet ball. Juliet's mother and nurse try to convince Juliet that a match with Paris would be a good one",
                    9
            ),
            new Post(
                    23,
                    "Спасибо!",
                    "Также большое спасибо Mike Mirzayanov за системы Polygon и Codeforces.",
                    1
            ),
            new Post(
                    512,
                    "ITMO Algorithms, Semester 4",
                    "Hello Codeforces! The final semester starts this week! As usual, the live lectures will be on Twitch on Fridays at 18:00 MSK, and the recorded videos will be on Youtube.",
                    6
            ),
            new Post(
                    513,
                    "ITMO Algorithms, Semester 4",
                    "Hello Codeforces! The final semester starts this week! As usual, the live lectures will be on Twitch on Fridays at 18:00 MSK, and the recorded videos will be on Youtube.",
                    6
            )
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
