# taskmaster
This repository is going to hold my Android tasks provided by ASAC team 

## Version 1 : lab 26 - Beginning TaskMaster

For this lab I created a home page with a text , picture and two buttons :
- one leads me to add task page which enables me to add a task with title and body and gives me a response message when I click on add task button (this page doesn't save the task to the database yet).
- second button leads me to all tasks page that only has an image for now.
![Home page](./screenshots/homePage.png)
Pages other than the home page have a back home arrow in the header.


## Version 2 : lab 27 - Data in TaskMaster

In this lab I learnt how to deal with data , I added four buttons for the home page :
- Three buttons for hard coded tasks leading me to taskDetails activity.
  ![Home page](./screenshots/Screenshot_20220430_030917.png)
  ![Home page](./screenshots/Screenshot_20220430_031007.png)

- Button number four takes me to the settings activity where you can set your username and you will have your username rendered on the home page.
  ![Task Details page](./screenshots/Screenshot_20220430_030947.png)

## Version 3 : lab 28 - RecyclerView 

In this lab I learnt how to display a recyclerView and send its data to the next activity. 
For this lab I created a task class, deleted my three tasks buttons and created a recyclerView instead that sends all the task data to the taskDetails activity.
![Home page](./screenshots/lab28-1.jpg)
![Task details page](./screenshots/280126320_680192909606899_8356462126591563157_n.jpg)
![Task details page](./screenshots/279932427_751247939203799_4797524901374250625_n.jpg)

In addition to that I added a spinner in the addTask activity for setting the task state.
![Add task page](./screenshots/280115071_1168435083991273_6580235935301626749_n.jpg)
![Add task page](./screenshots/280132455_304112168577831_4152853509198637669_n.jpg)

## Version 4 : lab 29 - Room 
For this lab I learnt how to create a database with Room and how to add an entity to it and how to deal with them using DAO.  
In this version I created a database for my application so now I am able to take tasks from the user.
In addition to that I rendered these tasks from the database to the main activity page and I am able to send each task's data to te task details activity. 
Nothing changed visually so no need for screenshots for this version. 

## Version 5 : lab 31 - Espresso and Polish

For this lab I added UI tests for :
- Navigating to settings activity and changing the username 
- Rendering the username in the main activity 
- Navigating to add tasks activity and adding a task. 
  Nothing changed visually so no need for screenshots for this version. 