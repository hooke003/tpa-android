package appfactory.uwp.edu.parksideapp2.extensions

// Only allows *@rangers.uwp.edu or *@uwp.edu domains to register
val EMAIL_REGEX = Regex("[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(rangers\\.)?(uwp.edu)")

fun String.isValidEmailAddress(): Boolean {
    return this.matches(EMAIL_REGEX) //this.isNotEmpty()
}

/*  PASSWORD REQUIREMENTS
    - Length: 8-64 characters
    - 1 uppercase
    - 1 lowercase
    - 1 numeric
    - 1 special character
 */
val PASSWORD_REGEX = Regex("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,64})")

fun String.isValidPassword(): Boolean {
    // TODO change password validation back to use regex
    return  this.matches(PASSWORD_REGEX) //this.isNotEmpty() (this notempty for testing only)
}