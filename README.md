# Neptune-Discord-Bot
A Discord Bot based on the character Neptune from HyperDimension Neptunia

[Invite Link](https://discordapp.com/api/oauth2/authorize?client_id=545565550768816138&permissions=37087296&scope=bot)

## Sources
Built using the [Discord JDA Library by DV8FromTheWorld](https://github.com/DV8FromTheWorld/JDA)
https://github.com/DV8FromTheWorld/JDA
Music folder is from the YUI bot's Lavaplayer implimentation.

Errors are handled silently in the background as to not spam the chat with useless errors.

## Notable Bot Features
* Play quotes from the Neptunia Games in Voice Chat on a server
* Translate the previous message into NepNep
* Optional TTS supports for some commands, Disabled by default and requires a Server Manager to enable, TTS permission required by the user aswell

## Commands
* !Nep Say (Quote) | Plays one of the stored quotes, leaving quote empty returns the complete list of quotes, The list is delivered via DM. If there are multiple matches the results will be DMed to you.
* !Nep Translate   | Translates the previous message in the current channel to NepNep. Translation is based on length of the word.
* !Nep nep nep...  | Neptune counts the number of neps in the message and adds one more.
* !Nep nepu....    | Same as !nep nep but for nepu. Nep and Nepu cannot be mixed.
* !Nep Leave       | Have neptune disconnect from voice chat. This automatically happens when everyone else leaves the channel.
* !Nep about       | Drops links to the Bot Invite link as well as my discord ID.


## Storage Format

NoSql via Google Cloud FireStore. The Cloud SDK needs to be set up beforehand for this bot to function.

### Guild Collection
The data stored for each guild/server.

* Custom-Sounds "Boolean"
* ID "String"            
* Name "String"          
* USE_TTS "Boolean"   

### Analytics Collection
The analytics data the bot tracks. Each command is its own Field

#### Commands
* Command Name "Integer"

### Say
Tracks what quotes are used and how often. Each quote has its own field.

* Quote Name "Int"

### Custom-Sounds
Any custom sound file outside of !Nep say. This is currently disabled except for a few specific servers

* Sound File "Int"
