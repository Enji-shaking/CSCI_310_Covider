# CSCI_310_Covider

## Icon Library
- [Bootstrap Icons](https://icons.getbootstrap.com/)
- [Font Awesome](https://fontawesome.com/)


## Testing

### Required device
Pixel 5 API 27

### Prefilled values in database:

There are four courses {CS310, CS350, CS360, CS585}  
There are there buildings {sal, thh, kap, rth}  
There are three student_users {Enji, Zhihan, Shuning}  
There are three instructors {Negar, Tanya, Saty}  
The default password for all users are 12345678  

#### Course -> Building

|CourseId      |  Course Name    |   Course Building   |    Instructor  |
| ---- | ---- | ---- | ---- |
|   60310   | CS310     | sal      | Negar     |
|   60350   | CS350     | thh     | Tanya     |
|   60360   | CS360     | kap     | Negar     |
|   60585   | CS585    | rth     | Saty     |

#### CheckIn: User -> List\<Building\>
Enji checked in buildings {sal, thh, kap}  
Zhihan checked in {sal, thh}  
Shuning checked in {} (None)  

#### Enrollment: User -> List\<Courses\>
Enji takes courses CS310, CS350, CS585  
Zhihan takes courses CS360, CS310  
Shuning takes course CS310  
Negar teach courses: CS310, CS360  
Tanya teach courses: CS350  
Saty teach courses: CS585  

#### TestResult:
Enji reported positive.  
(Zhihan should have a notifications of being in close contact with a positive patient.)

### Terms 

4 base pages:  
**Map/List view** : The first tab from left in the navigation bar. This is where user browser buildings on campus

**Report view** : The second tab in the navigation bar. This is where user fill and submit their daily report a.k.a. Trojan Check

**(Personal) Profile view** : The third tab in the navigation bar. This is where the user brows their courses and submit Daily reports (ordered in Date submitted in descending order)

**Notification view** : The forth tab from left in the navigation bar, This is where the user read their incoming notifications.

We used the term ***view*** and ***page*** interchangeably in our document.

### Start Testing

(The testing procedure we want you to follow:  )

**Log in as Zhihan (pwd: 12345678).**

- Go to the notification page and see there is one notification. The notification says: <u>“You got close contact with a positive patient, BEWARE!”</u>. Click the cross buttons to delete the notification. 
- Click map view and click notification page again to see the notification disappeared. 
- Log out.

**Log in as Shuning:**

- Go to the map view, click building ***kap*** (KAP is on the west edge of the campus, right above Downey Way Entrace). You should see there has been one student checked in *kap* with one of them being positive (which is enji, we manually entered that checkin record to our database). Click checkin in the popped up page for *kap*
- Go to the report page (the second tab from left in navigation bar). Fill a report indicating Shuning is positive, which you have to select the first option to be true. 
- Go to the personal profile page, you should see there's one report indicating Shuning is positive.
- Log out.

**Log in as Enji:**

- Go to the personal profile page to see there's already a report showing Enji's positive for that day. Go to the notification page and see there's one notification indicating he's been in close contact with a positive user (because Enji and Shuning both visited ***kap***).
- Go to the map page, click the map view toggle on the top right to see "***sal***, ***thh***, and ***rth***" as the buildings he needs to go according to his schedule. Scroll down to see "***sal***, ***thh***, and ***kap***" as his frequently visited buildings. 
- Click on any of the buildings to see the building's status. Currently, most of the buildings such as *esh* should have no check in records. 
- Log out.

**Log in as Negar:**

- Go to the personal profile page to see the courses she teaches (which are CS310 and CS360). Click the assess button next to any of the courses to see the course status. Click the button to change the status of CS310 to online.
- Log out.

**Log in as Enji:**

- Go to the notification page to see the notification that the course CS310 has been made online. 
- Go to the personal page to see the course CS310 status has been changed to online.
- Log out.

**Log in as Zhihan:**

- Go to the notification page to see the notification that the course CS310 has been made online. 
- Go to the personal page to see the course CS310 status has been changed to online.
- Log out.

**Log in as Negar:**

- Go to the daily report page and fill a form indicating she's positive.
- Go to the personal profile page to see both CS310 and CS360's status are online now.
- Log out.

**Log in as Zhihan:**

- Go to the notification page to see the notification that the course CS310 and CS360 has been made online. 
- Go to the personal page to see the course CS310 and CS360 status has been changed to online.
- Log out.

### Special Note:
If a user check in a building and they have no previous daily report record they will be by default considered as positive, so that the other user can get prepared for the worst case scenario.

### Optional: Page and functionalities
1. Map page   
Click the first button on the bottom navigation bar to show you the map view.  
1.1 Test checking building status:  
Click on any building in the map or in the list will show you a pop up window, indicating the risk of building and the number of people checking in, including high risk users and low risk users.  
In the check in button, you may select check in to check in a building.  
1.2 View user’s frequently visited locations, and locations that they should visit based on their daily schedule.
Click on the Map View toggle on the top right to show the list. You can also click any building in the list to see the building risk level and check in the building. 


2. Daily report page:  
Click the second button on the bottom navigation bar to show you the form to fill out.   
2.1. Test reporting negative:  
Select everything to be "no" to report a safe report. You will see a success pop up afterward.  
2.2. Test reporting positive:  
Select true for the first question. Every other student/professor who has been in close contact with you will see the notification of "You got close contact with a positive patient, BEWARE!". I will talk about how to view the notification soon. You will see a success pop up afterward.  
2.3 Test reporting with symptoms:  
Select true for the first question. Every other student/professor who has been in close contact with you will see the notification of "You got close contact with a student with covid related symptoms, BEWARE!". I will talk about how to view the notification soon. You will see a success pop up afterward.  

3. Personal profile page  
Click the third button on the bottom navigation bar to show you the form to fill out.   
3.1 View personal schedule and past reports  
You can directly see the classes you take/teach on the top section. Then in the following section, you can see the past reports you made. This list will only show "positive" and "negative" but not including the symptoms.  
3.2 Log out  
If you click the log out button on the top, you will be logged out.  
3.3 View course status  
You can view course status, including the risk factor and number of students is positive by clicking the button "in person/ online"  
3.4 Change course status if the user if professor  
And all students in the course status will receive notification  


### Thank You : )
