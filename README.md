## Security Note

The `application.yml` file uses environment variables for all sensitive values
(JWT secret key, database credentials).  
This is intentional and follows industry best practices.

To run the project locally, create a `.env` file in the project root

This file is excluded from Git and not pushed to GitHub.

More setup steps will be added as the project evolves.


