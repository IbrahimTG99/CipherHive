UUID vs aliases UUId better

store data on firebase realtime database

sync data between devices

encrypt data using master password

Login options and biometric for app login once logged in

Exporting and importing

Only encrypt password or the whole model?

functionalities app offers

- add new password
- edit password
- delete password

- multiple Uri can be added to password

- add note
- edit note
- delete note

- icon for each password

- copy password securely to clipboard using flags
- generate password
- password security level

- service to autofill password on websites and apps

- search for password
- sort passwords by title
- auto lock app


model for entry rn
- Title
- username/email
- password
- uuid for keystore access
- uri(multiple)
- note max 256 chars
- icon identifier

data class PasswordEntry(
    val id: String,
    val title: String,
    val username: String,
    val password: ByteArray,
    val urls: List<String>,
    val notes: String,
    val icon: String
)
