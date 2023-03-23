package appfactory.uwp.edu.parksideapp2.tpa2.auth;

import java.util.regex.Pattern;

/**
 * Email parser to check for the validity of the entered e-mail.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class EmailParse {
    // Pattern for finding a @rangers.uwp.edu or @uwp.edu
    public static final Pattern VALID_EMAIL_UWP_ADDRESS_REGEX = Pattern.compile(
            "^[A-Z0-9]+@(rangers\\.uwp)|(uwp)\\.(edu)$",
            Pattern.CASE_INSENSITIVE
    );

    // Check the e-mail pattern
    public static boolean Parse(String email) {
        return VALID_EMAIL_UWP_ADDRESS_REGEX.matcher(email).find();
    }
}
