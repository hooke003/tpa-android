# RangerWellness-NodeServer
**API calls for the Ranger Wellness server.**

**Last updated on: 6/20/2019**

**Updated by: Jacob Powers**

## Auth Controller routes
**Route Prefix: _https://rangerwellness-nodejs.herokuapp.com/api/auth_**
- **POST _/register_**
    - Use: Creates a new user model on the mongodb with the credentials provided.  This will also log the user in.
    - Parameters: Will be held in the payload of the body as a x-www-form-urlencoded
        - _name_
        - _email_
        - _password_
        - _admin_
            - **Admin parameter is a boolean value**
    - Returns:
        - _Status 200 code with authentication and a token that expires in 48 hours_
        
`NOTE: THE WFLAGGED ARRAY INITIALIZES WITH NULL IN THE 0 INDEX`
    

- **POST _/login_**
    - Use: Logs the user in and provides a new token.
    - Parameters: Will be held in the payload of the body as a x-www-form-urlencoded
        - _email_ **The email of the user**
        - _password_ **The password of the user**
    - Returns:
        - _A status 200 code with a new token for the user_
- **GET _/me_**
    - Use: Pulls the current users model from the mongo db and returns it
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Most recent token that has been issued to the user**
    - Returns:
        - _The most recent model for the user whos token was provided_
- **GET _/logout_**
    - Use: Logs the current user out
    - Parameters: none
    - Returns:
        - _Status 200 code with false auth and null for a token_
## User Controller routes - **All calls must be done as admin**
**Route Prefix: _https://rangerwellness-nodejs.herokuapp.com/api/users_**
- **GET _/_**
    - Use: Finds all users who are currently stored in the mongo db
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
    - Returns:
        - _An array of all users stored in the database_
- **GET _/findemail_**
    - Use: Finds a user by their registration email
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
    - Returns:
        - _The model of the user who matches the email_
- **POST _/deleteuser_**
    - Use: Deletes one user specified by their id
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
        - _id_ **ID of the user to be deleted**
    - Returns:
        - _Status code 204_
- **POST _/deleteallusers_**
    - Use: Deletes all users in the mongo db
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
    - Returns:
        - _Status code 204_
## Workout Controller routes
**Route Prefix: _https://rangerwellness-nodejs.herokuapp.com/api/workouts_**
- **GET _/_**
    - Use: Returns all workouts
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_
    - Returns:
        - _An array of all workouts in the database_
- **POST _/addwflag_**
    - Use: Allows the workout id's to be added to the wflagged array in the users model.  Will be pushed to the end of the array.
    - Parameters: Will be held in the payload of the headers 
        - _x-access-token_ **The token the user currently has**
        - _flag_ **The workout id to be added to the array**
    - Returns:
        - _The current users model as it reflects in mongo_
- **POST _/removewflag_**
    - Use: Allows the workout id's to be removed from the wflagged array in the users model.
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **The token the user currently has**
        - _flag_ **The workout id to be removed from the array**
    - Returns:
        - _The current users model as it reflects in mongo_   
- **GET _/id_**
    - Use: Returns a specific workout based on ID
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_
        - _id_  **ID of the workout**
    - Returns:
        - _The specific workout model that was requested_
- **GET _/title_**
    - Use: Finds workouts by title search
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_
        - _title_ **Name of workout to be looked up**
    - Returns:
        - _The workouts that contain the search in the title_
- **POST _/_**
    - Use: 
    - Parameters: Will be held in the payload of the body
        - _x-access-token_ **Admin token HAS TO BE PUT IN HEADER**
        
        `body is x-www-form-urlencoded`
        - _id_ **ID number for workout**
        - _title_ **Name of the workout**
        - _step_factor_ **Step factor**
    - Returns:
        - _Status code 201 and the model of the workout just created_
- **POST _/all_**
    - Use: Adds all workouts listed in the array in controllers/addAllWorkoutsController.js to the mongodb
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
    - Returns:
        - _Status code 201 as well as all workouts added to the database_
- **DELETE _/_**
    - Use: Deletes all workouts from the database
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
    - Returns:
        - _Status code 204_
- **DELETE _/id_**
    - Use: Deletes a specific workout from the database
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
        - _id_ **ID of workout to be deleted**
    - Returns:
        - _Status code 204_
## Events Controller routes
**Route Prefix: _https://rangerwellness-nodejs.herokuapp.com/api/events_**
- **GET _/_**
    - Use: Finds all events stored in the database
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ 
    - Returns:
        - _Array containing all the events_
- **POST _/addeflag_**
    - Use: Allows the event id's to be added to the eflagged array in the users model.  Will be pushed to the end of the array.
    - Parameters: Will be held in the payload of the headers 
        - _x-access-token_ **The token the user currently has**
        - _flag_ **The event id to be added to the array**
    - Returns:
        - _The current users model as it reflects in mongo_
- **POST _/removeeflag_**
    - Use: Allows the event id's to be removed from the eflagged array in the users model.
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **The token the user currently has**
        - _flag_ **The event id to be removed from the array**
    - Returns:
        - _The current users model as it reflects in mongo_     
- **GET _/id_**
    - Use: Getting a specific event by ID
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ 
        - _id_ **ID of event to lookup**
    - Returns:
        - _Model of the event searched_
- **GET _/displaydate_**
    - Use: Looks up events that are held on a specific day
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ 
        - _display_date_ **Date to search for events**
    - Returns:
        - _Model of the event searched_
- **GET _/flag_**
    - Use: Looks up an event by its flag
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ 
        - _flag_ **Flag of the event to be searched**
    - Returns:
        - _Model of the event searched_
- **POST _/_**
    - Use: Creating new event in the database
    - Parameters: `ALL PARAMETERS WILL BE HELD IN THE HEADER`
        - _x-access-token_ **Admin token**
        - _id_ **ID of the event**
        - _start_date_ **Start date of the event**
        - _end_date_ **End date of the event**
        - _display_date_ **Display date of the event**
        - _title_ **Title of the event**
        - _description_ **Description of the event**
        - _location_ **Location of the event**
        - _goal_ **Goal of the event**
        - _flag_ **Flag for the event**
    - Returns:
        - _Status code 201 and model that reflects the new event_
- **DELETE _/_**
    - Use: Deletes all events from the database
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
    - Returns:
        - _Status code 204_
- **DELETE _/id_**
    - Use: Deletes an event from the database by ID
    - Parameters: Will be held in the payload of the headers
        - _x-access-token_ **Admin token**
        - _id_ **ID of the event**
    - Returns:
        - _Status code 204_
