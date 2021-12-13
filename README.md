System for tracking of employees and their workloads by department.
<br/>An employee has an opportunity:
- to log in the system
-	to log out the system
-	to see all previous, current, future projects where he was assigned
-	to see all current projects
-	to see information about a specific project
-	to see all employees in a specific project
-	to see all departments
-	to see a specific department
<br/>A supervisor has an opportunity:
-	to log in the system
-	to log out the system
-	to see all projects
-	to see information about a specific project
-	to see all employees in a specific project
-	to see all employees
-	to see all employees of a specific department
-	to see a specific employee
-	to download reports about department’s workloads
-	to download reports about employees’ availability within current month or any others
<br/>An admin has an opportunity:
-	to log in the system
-	to log out the system
-	to see all projects
-	to see information about a specific project
-	to add a new project
-	to change information about a specific project
-	to delete a specific project
-	to see all employees in a specific project
-	to assign a specific user to a specific project
-	to remove a specific user from a specific project
-	to see all users
-	to see a specific user
-	to add a new user
-	to change personal information about a specific user
-	to deactivate a specific user
-	to see all departments
-	to see a specific department
-	to add a new department
-	to update information about a specific department
-	to delete a specific department
-	to see employees without an active project now or within a specified period
-	to load new users from CSV file
-	to download reports about department’s workloads
-	to download reports on the availability of employees in the current or any other month
<br/>System must:
-	create reports about employees’ workloads by the department on monthly basis
-	create reports on the availability of employees in the current or any other month

`GET /users/`
`GET	/user/{id}`
`POST /users`
`PUT	/users/{id}`
`DELETE /users/{id}`

`GET /projects`
`GET /projects/{id}`
`POST /projects`
`PUT /projects/{id}`
`DELETE /projects/{id}`

`GET /users/{id}/projects`
`GET /users/{id}/projects/current`
`PUT /users/{userId}/projects/{projectId}`
`DELETE /users/{userId}/projects/{projectId}`
`GET	/projects/{id}/users`
`PUT /projects/{projectId}/users/{userId}`
`DELETE /projects/{projectId}/users/{userId}`

`GET	/departments`
`GET	/departments/{id}`
`GET	/departments/{id}/users`
`POST /departments`
`PUT	/departments/{id}`
`DELETE /departments/{id}`

`GET /users/available/{days}`
Available users within period (at this moment if days was not specified)

`POST users/load`
Load users from the CSV file

`GET reports/{departmentId/{month}`
a report for a specific department for a specific month (current if a month was not defined)

`GET report/available/{month}`
available employees in a specific month (current if a month was not defined) 