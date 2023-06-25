[Spigot Resource Page](https://www.spigotmc.org/resources/admin360-reloaded.28285/) <br>
View this project on spigotmc.org.

![](https://img.shields.io/badge/Version-8.1.2-green) ![](https://img.shields.io/badge/Spigot-1.7+-lightgrey) ![](https://img.shields.io/badge/License-MIT-blue) ![](https://img.shields.io/badge/Language-Java-yellow) <br>

# Admin360-Reloaded

Admin360 is a Minecraft plugin. <br><br>

## Background

Have you ever been in a situation where many players need help, but there are only one or two admins online? If so, you should know how difficult it is to keep track of who needs help and in what order they asked for it. Another problem server owners often face is when hardworking players who get promoted to Admin start slacking off and not helping anymore. This plugin aims to solve both of these problems. <br><br>

## Motivation

First released by Vidhu in 2013, Admin360 is a lightweight but essential tool at all time. The new version aims to bring this inactive plugin back to life together with several major improvements to its base. Not only does it feature bug fixes, revised performance, backward compatibility and customizable configuration, the new version also introduces many additional features and QoL changes. <br><br>

## Features

1. Simple and lightweight _(stress tested on large server)_
2. Easy-to-understand config.yml
3. Works out-of-the-box
4. MySQL and SQLite support
5. 99% configurable <br><br>

## Components

**Support Ticket System: <br>
Foster Disciplined And Quality Service <br>**
_Automated Queuing, Status Queries, Organised Notifications and Informative Statistics <br>_
The support ticket module empowers your players (with the appropriate permission) to seek assistance from your staff by creating support tickets while they are online. These tickets are automatically added to the queue based on the "First come, first serve" principle to ensure fairness. As a result, your staff can prioritize providing help rather than deliberating on determining the next in line. Additionally, peripheral features such as the ability to cancel or delete tickets, along with the option to view the status and statistics of the current system, are included, creating a comprehensive and efficient help-request processing system. This capability enables you to deliver top-notch service and ensure a high-quality experience for your players.

**Honour Point System: <br>
Create A Competitive Environment For Your Staff <br>**
_Feedback Collection, Professional Statistics and Competitive Leaderboard <br>_
The feedback system allows users to express their satisfaction with the service provided by staff members by awarding them an 'honour point' through positive ratings. Staff members have the ability to view both their own and their colleagues' honour points, introducing a sense of motivation and competition to strive for better ratings. Moreover, this feature enables server owners to assess the performance of their staff team effectively. By incorporating the honour point system, you can encourage staff engagement, incentivize quality service, and provide server owners with valuable insights into their team's performance. <br> <br>

## Workflow

**STEP 1: Creating Tickets (Player)** <br>
A player, named Amy, opens a support ticket using "/ticket create [details]". Her request is then added to the next position of the queue following the principle of "First come, first serve".

At this stage, Amy can:
- cancel the ticket using "/ticket cancel".
- check the ticket status using "/ticket status".

**STEP2: Picking Tickets (Staff)** <br>
A staff, named David, proceeds to attend the next help request in the queue, which is Amy's ticket, using "/ticket next".

Apart from /ticket next, David can:
- manually pick a ticket from the queue using "/ticket pick <name>" based on the info from "/ticket list".
  \[2 options here\]

**STEP3: Processing Tickets (Staff)** <br>
He is then teleported to Amy's location by Admin360. Please note that Admin360 does not provide a chat channel function. Your staff will have to figure out how to help their assigned players before closing the case, e.g. using /msg in-game.

At this stage, David can:
- teleport to Amy using "/ticket tp".
- retrieve information of Amy's ticket using "/ticket info".

**STEP3: Closing Tickets (Staff)** <br>
After all the conversations, David closes the ticket by using "/ticket close". Amy'sticket is then done-marked. By following this workflow, your staff are able to process tickets one-by-one and to exercise their discretion case-by-case.

Apart from /ticket close, David can:
- use "/ticket redirect <name>" to redirect the ticket to another staff if he is not able to handle it.
- use "/ticket drop" if Amy's request is unreasonable.
  \[3 options here\]

**STEP4: Giving Feedback (Player)** <br>
To ensure service quality, Amy is asked to give feedback about her service experience. David receives an honour point because Amy upvotes his service by typing "/ticket yes". A Green Creeper Firework is launched right at David's location to congratulate him.

Apart from /ticket yes, Amy can:
- use "/ticket no" to give David a bad rating if he isn't helpful at all.
  \[2 options here\] <br><br>

- Technically, there are 4 kinds of ticket status:<br>
**In Queue :** The ticket is still in the queue, waiting to be processed. <br>
**In Progress :** It is when a staff is attending the request. <br>
**Awaiting Feedback :** It is when the ticket is closed, but the player still hasn't given any feedback.
Completed : Eveything is done. The details of that ticket will be stored as a historical record in the database. Tips: /ticket history


- When a player leaves the server, he/she will be removed from the system even if his/her tickets are in any of the statuses (except for the completed tickets).


- When a player joins the server, if he/she has the permission "admin360.staff.basic", then he/she will receive staff notifications.


- It's so hard for me to explain everything here. Just give it a try, then you will know how it works!


## Support

Spigot: [Private Conversation](https://www.spigotmc.org/members/jerryui.139798/) <br>
GitHub: [Issue Tracker](https://github.com/denniemok/Admin360-Reloaded/issues)

This project is released under [MIT License](https://opensource.org/license/mit/).