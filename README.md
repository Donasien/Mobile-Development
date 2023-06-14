# Donasien App - Cloud Computing

## Bangkit Capstone Project 2023
Bangkit Capstone Team ID : C23-PS011 <br>
Here is our repository for Bangkit 2023 Capstone project - Mobile Development

## Mobile Development Schedule
|     Week 1     |       Week 2        |            Week 3          |           Week 4          |
| :------------: | :-----------------: | :------------------------: |:------------------------: |
| Create Design/Resource App   |  Build User API     | Build Donation API  | Build Machine Learning and Testing App  |

## Donasien - Changing Lives Through Giving
![CloudArchitecture](https://github.com/Donasien/backend/blob/assets/img/Architecture_GCP.png)

## Backend Description
Donasien is an android-based application developed using Kotlin language and some additional libraries. Donasien is a humanitarian donation application that focuses on the medical field. This application uses firebase as the main server and web backend as the second server. In this application there is also local database storage such as shared preferences. Donasien also supports payment gateway (Midtrans) using midtrans library. This application supports donation payments via e-wallet or bank transactions.
<br>
<br>
Donasien are divided into two roles, namely anonymous and user.
1. Anonymous
The anonymous role is where you run the donpatient application without doing the login/register process and without completing the profile provided. In this role you cannot submit donations and blood donations, you can only help existing donations.

2. User
The user role is where you run the donation application by doing the login/register process, then completing the files and personal data on the profile page. In this role you can apply for donations and blood donations.

## WEB & API URL
[Donasien Web & API](https://donasien.me/)

[Machine Learning API](https://ml-api-rt4pbfoggq-et.a.run.app/)

[News API](https://newsapi.org)

## <a name="api"></a>Donasien Documentation API
### Endpoint Documentation
[Donasien Endpoint Documentation](https://documenter.getpostman.com/view/27663700/2s93sf1VvX)

### Article API
[Article API Documentation](https://newsapi.org/docs/endpoints/everything)
<br>
|  Endpoint |  Method	     |      Query Params |           Description          |
| :----: | :------------: | :-----------------: | :------------------------: |
| /v2/everything | GET   | q, sortBy and apiKey      | HTTP GET REQUEST Show all of the Article about Health  |

We opt for this API due to its ease of implementation and cost-effectiveness, as it doesn't impose additional system load or incur any extra expenses on the Google Cloud Platform

### Security
We implement API protection using Firebase tokens to restrict access to authorized users only. These tokens provide secure authentication, ensuring that only valid and authenticated users can utilize the API

## How To Run This Code
* To utilize this code, it is required to have PHP ^8.0.2 installed, and you need to set up a local database using XAMPP or any other MySQL database
* After making the database then download this repository
* Open the folder in VSCode
* Copy the .env.example file and rename it to .env
* Next open the .env file and edit the DB_DATABASE and other relevant fields with the name of your local database and other necessary configurations. Additionally, set FILESYSTEM_DISK to "public" to allow public access to donation photos
* Next open VSCode terminal
* Type ```composer install``` or ```composer update``` and hit enter
* Next type ```php artisan key:generate``` and hit enter
* Next type ```php artisan storage:link``` and hit enter
* Next type ```php artisan migrate:fresh --seed``` and hit enter
* Then type ```php artisan serve``` to start the server
* It will run on http://localhost:8000/
![Run](https://github.com/Donasien/backend/blob/assets/img/Run.png)

## How To Use The Endpoint
* To use this endpoint, need to use a special token that our team provided
* After getting the token then Open a Postman Application and fill the token in param
* Enter URL request bar with https://donasien.me/api/profile
* Select method GET then Send the request
* If success then postman will return your profile
![Endpoint](https://github.com/Donasien/backend/blob/assets/img/Endpoint.png)
* For complete Documentation please visit [Donasien Documentation API](#api)
