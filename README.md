[Spigot Resource Page](https://www.spigotmc.org/resources/admin360-reloaded.28285/) <br>
View this project on spigotmc.org.

![](https://img.shields.io/badge/Version-8.1.2-green) ![](https://img.shields.io/badge/Spigot-1.7+-lightgrey) ![](https://img.shields.io/badge/License-MIT-blue) ![](https://img.shields.io/badge/Language-Java-yellow) <br><br>

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
The feedback system allows users to express their satisfaction with the service by awarding the staff an 'honour point'. Staff members have the ability to view both their own and their colleagues' honour points, introducing a sense of motivation and competition to strive for better ratings. Moreover, this feature enables server owners to assess the performance of their staff team effectively. By incorporating the honour point system, you can encourage staff engagement, incentivize quality service, and provide server owners with valuable insights into their team's performance. <br><br>

## Workflow

**STEP 1: Creating Tickets (Player)** <br>
A player, named Amy, opens a support ticket using "/ticket create [details]". Her request is then added to the next position of the queue following the principle of "First come, first serve".

At this stage, Amy can:
- cancel the ticket using "/ticket cancel".
- check the ticket status using "/ticket status". <br><br>

**STEP2: Picking Tickets (Staff)** <br>
A staff, named David, proceeds to attend the next help request in the queue, which is Amy's ticket, using "/ticket next".

Apart from /ticket next, David can:
- manually pick a ticket from the queue using "/ticket pick <name>" based on the info from "/ticket list". <br>

[2 options here] <br><br>

**STEP3: Processing Tickets (Staff)** <br>
He is then teleported to Amy's location by Admin360. Please note that Admin360 does not provide a chat channel function. Your staff will have to figure out how to help their assigned players before closing the case, e.g. using /msg in-game.

At this stage, David can:
- teleport to Amy using "/ticket tp".
- retrieve information of Amy's ticket using "/ticket info". <br><br>

**STEP4: Closing Tickets (Staff)** <br>
After all the conversations, David closes the ticket by using "/ticket close". Amy'sticket is then done-marked. By following this workflow, your staff are able to process tickets one-by-one and to exercise their discretion case-by-case.

Apart from /ticket close, David can:
- use "/ticket redirect <name>" to redirect the ticket to another staff if he is not able to handle it.
- use "/ticket drop" if Amy's request is unreasonable. <br>

[3 options here] <br><br>

**STEP5: Giving Feedback (Player)** <br>
To ensure service quality, Amy is asked to give feedback about her service experience. David receives an honour point because Amy upvotes his service by typing "/ticket yes". A Green Creeper Firework is launched right at David's location to congratulate him.

Apart from /ticket yes, Amy can:
- use "/ticket no" to give David a bad rating if he isn't helpful at all.

[2 options here] <br><br>

- Technically, there are 4 kinds of ticket status:<br>
**In Queue :** The ticket is still in the queue, waiting to be processed. <br>
**In Progress :** It is when a staff is attending the request. <br>
**Awaiting Feedback :** It is when the ticket is closed, but the player still hasn't given any feedback.
Completed : Eveything is done. The details of that ticket will be stored as a historical record in the database. Tips: /ticket history


- When a player leaves the server, he/she will be removed from the system even if his/her tickets are in any of the statuses (except for the completed tickets).


- When a player joins the server, if he/she has the permission "admin360.staff.basic", then he/she will receive staff notifications.


- It's so hard for me to explain everything here. Just give it a try, then you will know how it works! <br><br>

## Commands

The following is the updated Commands and Permissions for Admin36-Reloaded 8.1.2. For the permissions to work, a permission plugin (eg. LuckPerms, PermissionEx, GroupManager, etc.) is required.

Commands without Permissions
> /ticket, /ticket help - (no permission) - Display a list of commands <br>
/admin360 help - (no permission) - Display a list of commands <br>
/admin360 - (no permission) - Display Admin360 information

Player Commands (admin360.player.*)
> []: Optional; <>: Compulsory <br>
/ticket create [details] - (admin360.player.basic) - Create a new ticket. <br>
/ticket cancel - (admin360.player.basic) - Remove your ticket from the queue. <br>
/ticket status - (admin360.player.status) - Check your ticket status. <br>
/ticket stats - (admin360.player.stats) - View ticket statistics. <br>
/ticket yes - (admin360.player.basic) - Feedback: Upvote a service. <br>
/ticket no - (admin360.player.basic) - Feedback: Downvote a service.

Staff Commands (admin360.staff.*)
>[]: Optional; <>: Compulsory

STEP 1: Choosing a ticket
/ticket list - (admin360.staff.basic) - Display a list of tickets in the queue await to be processed. A handy tool for /ticket pick.
/ticket pick <name> - (admin360.staff.basic) - Hand-pick a ticket from the queue, often based on the info from /ticket list.
/ticker next - (admin360.staff.basic) - Proceed to process the next ticket in the queue. Good for those who are indecisive.
[2 options here: You either pick one or do the next one.]

STEP 2: Processing a ticket
/ticket tp - (admin360.staff.tp) - Teleport to the player who opened the ticket that you are processing
/ticket info - (admin360.staff.info) - Retrieve the details about the ticket that you are processing
[Use other plugins for the communication part. e.g. /msg, /reply, etc.]

STEP 3: Making a decision
/ticket redirect <name> - (admin360.staff.redirect) - Redirect the ticket to another staff if it can only be resolved by staff of senior rank.
/ticket drop - (admin360.staff.drop) - Drop or abandon the ticket, maybe because the one who opened the ticket asked silly questions or raised unreasonable requests. This can avoid receiving a revenge rating from some bad guys.
/ticket close - (admin360.staff.basic) - Close and done-mark the ticket.
[3 options here: You either redirect the ticket to someone else, drop it or close it.]

Removal Tools
/ticket purge - (admin360.staff.purge) - Remove all tickets in the queue.
/ticket delete <name> - (admin360.staff.delete) - Delete silently everything related to a particular player regardless of his/her ticket status (except for completed tickets).

Honor Points System
/ticket hpstats [name] - (admin360.staff.hpstats) - Display honor points statistics.
/ticket hptop [#] - (admin360.staff.hptop) - Display honor points leaderboard.
/ticket history [#] - (admin360.staff.history) - Print honor points history. This can also be used as ticket history.
/ticket hpreset <name> - (admin360.staff.hpreset) - Reset all records of a particular staff. It will also affect his or her records in /ticket history.

Reload Config
/admin360 reload - (admin360.staff.reload) - Reload the config file.


IMPORTANT
These permissions are not given to any user groups by default. You have to add them to the suitable permission group(s) by yourself. Admin360 will recognise those who have the permission "admin360.staff.basic" as staff when they join the server.

To prevent boosting, your staff with permission "admin360.staff.basic" will not be allowed to use the command "/ticket create" by default. This is a hard-coded anti-exploit mechanism. <br><br>

## Prerequisites

- Java 8+
- SQLite (org.sqlite.jdbc) or MySQL (com.mysql.jdbc) Drivers
(Should have been installed by your hosting providers by default)
- Permission Plugin (e.g. LuckPerms, PermissionEx, GroupManager, etc.)
- Spigot 1.7.x/1.8.x/1.9.x/1.10.x/1.11.x/1.12.x/1.13.x/1.14.x/1.15.x/1.16.x/1.17.x/1.18.x/1.19.x/1.20.x (should be working on Bukkit, Paper or any of their forks)
- For versions that support Bukkit 1.6 or earlier, download it from the old abandoned project page: https://dev.bukkit.org/bukkit-plugins/admin360/ <br><br>

## Support

Spigot: [Private Conversation](https://www.spigotmc.org/members/jerryui.139798/) <br>
GitHub: [Issue Tracker](https://github.com/denniemok/Admin360-Reloaded/issues)

This project is released under [MIT License](https://opensource.org/license/mit/).