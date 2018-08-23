## Java GUI NameSayer_2.0

### What is a “creation”?
The purpose of this application is to author/create videos, known as “creations”, that would be helpful in assistingusers to practise unfamiliar names: 
•A creation would be for a full name.
•For the video component, it displays that name as ordinary text in the video, e.g. “Sean Zhang”.
•For the audio component, it sounds the name. The sound will come from the user recording themselves saying that name.
•The video and audio components are merged together to form the creation.(l)ist existing creations

This menu option will list all the current creations that the user has created. 
It is important that you list these tidily. You should list the creation name (see last feature in “create”), not the filename (i.e. don’t display filename extensions or file paths). Any display format is fine, as long as it clearly shows the user how many creations there are (without the user having to count them), it is tidy, and so on. Think carefully also what happens when there are no creations (e.g. none were created yet, or all were deleted).

### (p)lay an existing creation
Here the user would like to watch a particular creation, assuming at least one exists. You should remind the user what existing creations exist (by listing them again), and allow them to select one to play. As soon as the creation is played, the video should automatically close and show the menu again. Think carefully how you program this part. You don’t want to copy-paste the same functionality as the list option. Think in terms of code reuse.

### (d)elete an existing creation
Very  similar  to  the  play  functionality  above,  except  you  delete  the  creation  the  user  specifies.  You  therefore  initially  show  them  the  list  of  creations  they  can  select  from,  and  once  they  select  one  to  delete,  you  should  confirm with them if they are sure they want to delete it (make sure you spell out what they are attempting to delete). Again, think in terms of code reuse when displaying the existing creations.

### (c)reate a new creation
This is the main part of the authoring aid, where the user will generate a new creation. The process is as follows:1.Ask the user to enter a full name for the new creation. You need to make sure that there isn’t an existing creation with that same name. If there is already one with that name, you need to keep asking the user to enter another name until they enter one that doesn’t already exist.2.You generate the video part of the creation, where the name forms the text content of the video. It doesn’t matter how the text is displayed, as long as it’s clearly visible in the video. The video should be 5 seconds long also.3.Tell the user that they need to record the audio for the creation by saying the name. To make sure they are ready, you should have a prompt along the lines of “Press any key to start recording”. As soon as they enter any key, it starts recording their voice for 5 seconds.4.

You should offer the user the opportunity to hear the recorded audio, and ask them if they want to (k)eep it or (r)edo the audio. It the user wants to redo the recording, you repeat step 3 above. This is repeated until the user is happy with the audio recording.5.You then combine the video (from step 2) with the audio (from step 4) into a single video, and this is your creation.(q)uit authoring toolEvery time the user performs an operation, they should be presented again with the main menu (printed again). This should continue indefinitely until the user selects to quit
